package Common;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;

import java.util.*;

import Client.ClientConfig;
import Common.GameBoard.GameBoard;
import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.PassCommand;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.State.GameState;
import Common.State.PlayerState;
import Common.State.PlayerStateImpl;
import Common.State.PublicPlayerState;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;
import Player.PlayerSetupInf;
import Player.PlayerTakeTurnInf;
import Player.PlayerWinInf;
import Player.Strategy.BadAskForTilesStrategy;
import Player.Strategy.NoFitStrategy;
import Player.Strategy.NonAdjacentCoordinateStrategy;
import Player.Strategy.NotALineStrategy;
import Player.Strategy.TileNotOwnedStrategy;
import Player.player;
import Player.playerImpl;
import Player.Strategy.DagStrategy;
import Player.Strategy.LdasgStrategy;
import Player.playerNewTilesException;
import Player.playerNewTilesInf;
import Player.playerSetupException;
import Player.Strategy.Strategy;
import Player.playerTakeTurnException;
import Player.playerWinException;
import Referee.RefereeConfig;
import Common.Scorer.ScorerConfig;
import Server.ServerConfig;

public class JsonToQGame {

    public static ClientConfig ClientConfigJSONToClientConfig(JsonObject o, int port) {
      return new ClientConfig(port, o.get("host").getAsString(),
            o.get("wait").getAsInt(), o.get("quiet").getAsBoolean(),
              JActorsToPlayerList(o.get("players").getAsJsonArray()));
  }

  public static ServerConfig ServerConfigJSONToServerConfig(JsonObject o, int port) {
      try {
        return new ServerConfig(port, o.get("server-tries").getAsInt(),
                o.get("server-wait").getAsInt(), o.get("wait-for-signup").getAsInt(),
                o.get("quiet").getAsBoolean(),
                RefereeConfigJSONToRefereeConfig(o.get("ref-spec").getAsJsonObject()));
      } catch(Exception e) {
        throw new IllegalStateException("invalid: " + o);
      }
  }


  public static RefereeConfig RefereeConfigJSONToRefereeConfig(JsonObject o) {
    return new RefereeConfig(JStateToGameState(o.get("state0").getAsJsonObject()), o.get("quiet").getAsBoolean(),
            RefereeStateConfigJsonToRefereeStateConfig(o.get("config-s").getAsJsonObject()),
            o.get("per-turn").getAsInt(), o.get("observe").getAsBoolean());
  }

  public static ScorerConfig RefereeStateConfigJsonToRefereeStateConfig(JsonObject o) {
      return new ScorerConfig(o.get("qbo").getAsInt(), o.get("fbo").getAsInt());
  }

  /**
   * Converts a JsonElement to a JName
   * @throws IllegalArgumentException if element isn't a valid JName:
   * 1. between 1-20 characters
   * 2. satisfies "^[a-zA-Z0-9]+$"
   */
  public static String JsonToJName(String name) {
      if (name.length() > 0 && name.length() <= 20 && name.matches("^[a-zA-Z0-9]+$")) {
        return name;
      }
      else {
        throw new IllegalArgumentException("Provided a name that isn't between 1-20 chars or has invalid characters");
      }
  }

  /**
   * Converts a JState to a State
   * @throws IllegalArgumentException if players is empty
   */
  public static GameState JStateToGameState(JsonObject jState) throws IllegalArgumentException {
    if (jState.getAsJsonArray("players").size() < 1) {
      throw new IllegalArgumentException("players must be non-empty");
    }
    Deque<Tile> deck = new ArrayDeque<>();
    for (JsonElement jTile : jState.getAsJsonArray("tile*")) {
      deck.add(JTileToTile(jTile.getAsJsonObject()));
    }
    Queue<PlayerState> players = new ArrayDeque<>();
    for (JsonElement jPlayer : jState.getAsJsonArray("players")) {
      players.add(JPlayerToPlayerState(jPlayer.getAsJsonObject()));
    }

    return new GameState(players, JMapToGameBoard(jState.getAsJsonArray("map")), deck);
  }

  public static List<player> JActorsToPlayerList(JsonArray JActors) {
    if (JActors.size() < 2 || JActors.size() > 4) {
      throw new IllegalArgumentException("Only 2-4 players are allowed.");
    }
    List<player> players = new ArrayList<>();
    for (JsonElement JActorSpec : JActors) {
      players.add(JActorSpecToPlayer(JActorSpec.getAsJsonArray()));
    }
    return players;
  }

  private static player PlayerWithJExn(String name, Strategy strategy, JsonArray JActorSpec) {
    return switch (JActorSpec.get(2).getAsString()) {
      case "setup" -> new playerSetupException(name, strategy);
      case "take-turn" -> new playerTakeTurnException(name, strategy);
      case "new-tiles" -> new playerNewTilesException(name, strategy);
      case "win" -> new playerWinException(name, strategy);
      default -> throw new IllegalArgumentException("Invalid JActor with JExn");
    };
  }

  public static Strategy JStrategyToStrategy(String jStrategy) {
    return switch (jStrategy) {
      case "ldasg" -> new LdasgStrategy();
      case "dag" -> new DagStrategy();
      default -> throw new IllegalArgumentException("Strategy doesn't exist");
    };
  }

  private static Strategy JCheatToCheatStrategy(String jStrategy, String jCheat) {
    Strategy fallbackStrategy = JStrategyToStrategy(jStrategy);
    return switch (jCheat) {
      case "non-adjacent-coordinate" -> new NonAdjacentCoordinateStrategy(fallbackStrategy);
      case "tile-not-owned" -> new TileNotOwnedStrategy(fallbackStrategy);
      case "not-a-line" -> new NotALineStrategy(fallbackStrategy);
      case "bad-ask-for-tiles" -> new BadAskForTilesStrategy(fallbackStrategy);
      case "no-fit" -> new NoFitStrategy(fallbackStrategy);
      default -> throw new IllegalArgumentException("Invalid JCheat");
    };
  }

  private static player PlayerWithJCheat(String name, String strategyName, JsonArray JActorSpec) {
    if (!JActorSpec.get(2).getAsString().equals("a cheat")) {
      throw new IllegalArgumentException("invalid JActorSpecA");
    }

    return new playerImpl(name, JCheatToCheatStrategy(strategyName, JActorSpec.get(3).getAsString()));
  }

  private static player PlayerWithCountJExn(String name, String strategyName, JsonArray JActorSpec) {
    int when = JActorSpec.get(3).getAsInt();
    Strategy strategy = JStrategyToStrategy(strategyName);
    return switch(JActorSpec.get(2).getAsString()) {
      case "setup" -> new PlayerSetupInf(name, strategy, when);
      case "new-tiles" -> new playerNewTilesInf(name, strategy, when);
      case "take-turn" -> new PlayerTakeTurnInf(name, strategy, when);
      case "win" -> new PlayerWinInf(name, strategy, when);
      default -> throw new IllegalArgumentException("Invalid Player: [JName, JStrategy, JExn, Count]");
    };
  }

  private static player PlayerWithAnotherCheat(String name, String strategyName, JsonArray JActorSpec) {
    if (JActorSpec.get(2).getAsString().equals("a cheat")) {
      return PlayerWithJCheat(name, strategyName, JActorSpec);
    }
    else {
      return PlayerWithCountJExn(name, strategyName, JActorSpec);
    }
  }


  private static player JActorSpecToPlayer(JsonArray JActorSpec) throws IllegalArgumentException {
    String name = JActorSpec.get(0).getAsString();
    Strategy strategy = jsonToStrategy(JActorSpec.get(1).getAsString());

    return switch (JActorSpec.size()) {
      case 2 -> new playerImpl(name, strategy);
      case 3 -> PlayerWithJExn(name, strategy, JActorSpec);
      case 4 -> PlayerWithAnotherCheat(name, JActorSpec.get(1).getAsString(), JActorSpec);
      default ->
              throw new IllegalArgumentException("JActorSpec must contain either 2, 3, or 4 elements.");
    };
  }



  /**
   * Converts the given JPlacements to a list of Placements
   */
  public static Queue<Placement> JPlacementsToPlacements(JsonArray jPlacements) {
    Queue<Placement> listOfPlacement = new ArrayDeque<>();
    for (JsonElement e : jPlacements) {
      JsonObject onePlacement = e.getAsJsonObject();
      Tile newTile = JTileToTile(onePlacement.get("1tile").getAsJsonObject());
      Coordinate newCoordinate = Coordinate.toCoordinate(onePlacement.get("coordinate").getAsJsonObject());

      listOfPlacement.add(new Placement(newCoordinate, newTile));
    }
    return listOfPlacement;
  }

  /**
   * Returns a sorted list of Tiles and Coordinates by their Coordinate.
   * TODO use treeMap
   */
  public static List<Map.Entry<Coordinate, Tile>> sortGameboard(Map<Coordinate, Tile> tiles) {
    // convert hashmap to sorted list
    List<Map.Entry<Coordinate, Tile>> tilesAsList = new ArrayList<>(tiles.entrySet());
    Collections.sort(tilesAsList, Map.Entry.comparingByKey());
    return tilesAsList;
  }

  /**
   * Converts the given JPlayer to a PlayerState with a list of Tiles and score.
   */
  public static PlayerStateImpl JPlayerToPlayerState(JsonObject jPlayer) {
    List<Tile> playersTiles = new ArrayList<>();
    for (JsonElement tileElement : jPlayer.get("tile*").getAsJsonArray()) {
      playersTiles.add(JTileToTile(tileElement.getAsJsonObject()));
    }
    return new PlayerStateImpl(jPlayer.get("name").getAsString(), playersTiles, jPlayer.get("score").getAsInt());
  }

  /**
   * Converts the given JMap to a HashMap<Coordinate, Tile> tiles that represents the map of this game.
   */
  protected static Map<Coordinate, Tile> JMapToHashmap(JsonArray jMap) {
    Map<Coordinate, Tile> tiles = new HashMap<>();
    for (JsonElement jRowElement : jMap) {
      JsonArray jRow = jRowElement.getAsJsonArray();
      int rowIdx = jRow.get(0).getAsInt();
      for (int i = 1; i<jRow.size(); i++) {
        JsonArray jCell = jRow.get(i).getAsJsonArray();
        JsonObject jTile = jCell.get(1).getAsJsonObject();
        int colIdx = jCell.get(0).getAsInt();
        Coordinate coordinate = new Coordinate(colIdx, rowIdx);
        Tile tile = JTileToTile(jTile);
        tiles.put(coordinate, tile);
      }
    }
    return tiles;
  }

  public static GameBoard JMapToGameBoard(JsonArray jMap) {
    return new GameBoard(JMapToHashmap(jMap));
  }

  /**
   * Converts the given JPub to a PublicPlayerKnowledge
   */
  public static ActivePlayerKnowledge JPubToActivePlayerKnowledge(JsonObject jPub) {
    GameBoard gameBoard = JMapToGameBoard(jPub.getAsJsonArray("map"));
    int refTilesRemaining = jPub.get("tile*").getAsInt();
    JsonArray players = jPub.get("players").getAsJsonArray();
    PlayerStateImpl activePlayer = JPlayerToPlayerState(players.get(0).getAsJsonObject());
    Queue<PublicPlayerState> opponentStates = convertPlayersToQueueOfPublicPlayerStates(players);
    return new ActivePlayerKnowledge(opponentStates, gameBoard, refTilesRemaining, activePlayer.getHand());
  }

  /**
   * Converts a JPub's client to a Queue of opponent's PublicPlayerStates
   */
  protected static Queue<PublicPlayerState> convertPlayersToQueueOfPublicPlayerStates(JsonArray players) {
    Queue<PublicPlayerState> states = new ArrayDeque<>();
    for (int i = 1; i < players.size(); i++) { // skip active JPlayer
      states.add(new PlayerStateImpl(players.get(i).getAsString()));
    }
    return states;
  }


  /**
   * Converts the given JTile to a Tile with QColor and QShape.
   */
  public static Tile JTileToTile(JsonObject jTile) {
    QColor color = QColor.fromString(jTile.get("color").getAsString());
    QShape shape = QShape.fromString(jTile.get("shape").getAsString());
    return new Tile(color, shape);
  }


  public static Strategy jsonToStrategy(String strat) {
    return switch (strat) {
      case "ldasg" -> new LdasgStrategy();
      case "dag" -> new DagStrategy();
      default -> throw new IllegalArgumentException("Strategy doesn't exist");
    };
  }

  public static QGameCommand jChoiceToQGameCommand(JsonElement choice) {
    if(choice.isJsonArray()) {
      return new PlacementCommand(JPlacementsToPlacements(choice.getAsJsonArray()));
    }
    else if (choice.isJsonPrimitive()) {
      String exchangeOrPass = choice.getAsString();
      if (exchangeOrPass.equals("pass")) {
        return new PassCommand();
      }
      else if (exchangeOrPass.equals("replace")) {
        return new ExchangeCommand();
      }
      else {
        throw new IllegalArgumentException("Invalid JChoice string " + choice.getAsString());
      }
    }
    throw new IllegalArgumentException("Invalid JChoice: " + choice);
  }

  /**
   * Returns the Method Name from a Method Call Json
   */
  public static MName getMName(JsonElement element) {
    return MName.fromString(element.getAsJsonArray().get(0).getAsString());
  }

  public static ActivePlayerKnowledge MethodCallToActivePlayerKnowledge(JsonArray methodCall) {
    JsonObject jPub = methodCall.get(1).getAsJsonArray().get(0).getAsJsonObject();
    return JPubToActivePlayerKnowledge(jPub);
  }

  public static List<Tile> ListOfJTileToListOfTile(JsonArray jTiles) {
    List<Tile> tiles = new ArrayList<>();
    for (JsonElement jTile : jTiles) {
      tiles.add(JTileToTile(jTile.getAsJsonObject()));
    }
    return tiles;
  }

  /**
   * @param index of JTile* in the arguments array.
   */
  public static List<Tile> MethodCallToListOfTiles(JsonArray methodCall, int index) {
    JsonArray jTiles = methodCall.get(1).getAsJsonArray().get(index).getAsJsonArray();
    return ListOfJTileToListOfTile(jTiles);
  }

  public static List<Tile> SetupMethodCallToListOfTiles(JsonArray methodCall) {
    return MethodCallToListOfTiles(methodCall, 1);
  }

  public static List<Tile> NewTilesMethodCallToListOfTiles(JsonArray methodCall) {
    return MethodCallToListOfTiles(methodCall, 0);
  }

  public static boolean WinMethodCallToListOfTiles(JsonArray methodCall) {
    return methodCall.get(1).getAsJsonArray().get(0).getAsJsonPrimitive().getAsBoolean();
  }

}
