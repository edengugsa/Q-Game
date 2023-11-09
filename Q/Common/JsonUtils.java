package Common;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;

import java.util.*;

import Common.GameBoard.GameBoard;
import Common.RuleBook.BadAskForTilesRuleBook;
import Common.RuleBook.NoFitRuleBook;
import Common.RuleBook.NonAdjacentCoordinateRuleBook;
import Common.RuleBook.NotALineRuleBook;
import Common.RuleBook.QRuleBook;
import Common.RuleBook.TileNotOwnedRuleBook;
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
import Player.player;
import Player.playerImpl;
import Player.Strategy.DagStrategy;
import Player.Strategy.LdasgStrategy;
import Player.playerNewTilesException;
import Player.playerSetupException;
import Player.Strategy.Strategy;
import Player.playerTakeTurnException;
import Player.playerWinException;

public class JsonUtils {

  public static JsonArray NamesToJNames(List<String> names) {
    JsonArray jNames = new JsonArray();
    for (String name : names) {
      jNames.add(name);
    }
    return jNames;
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

//  public static JsonObject GameStateToJState(GameState state) {
//    JsonObject jsonObj = new JsonObject();
//    jsonObj.add("map", HashMapToJMap(state.board()));
//    jsonObj.addProperty("tile*", refTilesToJTileArray(state.getRefDeck()));
//    jsonObj.addProperty("players", playerStatesToJPlayersArray(state.players()));
//    return jsonObj;
//  }
//
//  public static JsonArray refTilesToJTileArray(Deque<Tile> tiles) {
//    JsonArray jsonArr = new JsonArray();
//
//  }
//
//  public static JsonArray playerStatesToJPlayersArray(List<PlayerState> players) {
//    JsonArray jsonArr = new JsonArray();
//
//  }


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

  public static Strategy JStrategyJCheatToStrategy(String jStrategy, String jCheat) {
    QRuleBook rules = JCheatToRuleBook(jCheat);
    return switch (jStrategy) {
      case "ldasg" -> new LdasgStrategy(rules);
      case "dag" -> new DagStrategy(rules);
      default -> throw new IllegalArgumentException("Strategy doesn't exist");
    };
  }

  private static QRuleBook JCheatToRuleBook(String jCheat) {
    return switch (jCheat) {
      case "non-adjacent-coordinate" -> new NonAdjacentCoordinateRuleBook();
      case "tile-not-owned" -> new TileNotOwnedRuleBook();
      case "not-a-line" -> new NotALineRuleBook();
      case "bad-ask-for-tiles" -> new BadAskForTilesRuleBook();
      case "no-fit" -> new NoFitRuleBook();
      default -> throw new IllegalArgumentException("Invalid JCheat");
    };
  }

  private static player PlayerWithJCheat(String name, String strategyName, JsonArray JActorSpec) {
    if (!JActorSpec.get(2).getAsString().equals("a cheat")) {
      throw new IllegalArgumentException("invalid JActorSpecA");
    }

    return new playerImpl(name, JStrategyJCheatToStrategy(strategyName, JActorSpec.get(3).getAsString()));
  }



  private static player JActorSpecToPlayer(JsonArray JActorSpec) throws IllegalArgumentException {
    String name = JActorSpec.get(0).getAsString();
    Strategy strategy = jsonToStrategy(JActorSpec.get(1).getAsString());

    switch(JActorSpec.size()) {
      case 2:
        return new playerImpl(name, strategy);
      case 3:
        return PlayerWithJExn(name, strategy, JActorSpec);
      case 4:
        return PlayerWithJCheat(name, JActorSpec.get(1).getAsString(), JActorSpec);
      default:
        throw new IllegalArgumentException("JActorSpec must contain either 2, 3, or 4 elements.");
    }
  }

  /**
   * Converts the given hashmap of Coordinates and Tiles to a JMap. Tiles is a non-empty hashmap!
   * @param tiles
   */
  public static JsonArray HashMapToJMap(Map<Coordinate, Tile> tiles) {

    List<Map.Entry<Coordinate, Tile>> tilesAsList = sortGameboard(tiles);

    JsonArray jMap = new JsonArray();
    JsonArray jRow = new JsonArray();
    int rowIdx = tilesAsList.get(0).getKey().row(); // first tile's row value
    jRow.add(rowIdx);

    for (Map.Entry<Coordinate, Tile> entry : tilesAsList) {
      if (rowIdx != entry.getKey().row()) { // encountered a new row
        jMap.add(jRow); // add the completed row to the map
        rowIdx = entry.getKey().row(); // update rowIdx
        jRow = new JsonArray(); // start the new row
        jRow.add(rowIdx);
      }
      JsonArray jCell = new JsonArray();
      jCell.add(entry.getKey().col()); // ColumnIndex
      jCell.add(TileToJTile(entry.getValue())); // JTile
      jRow.add(jCell);
    }
    jMap.add(jRow); // add the final row to the map
    return jMap;
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
    return new PlayerStateImpl(playersTiles, jPlayer.get("score").getAsInt());
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

  public static JsonObject GameStateToJState(GameState gs) {
    JsonObject jState = new JsonObject();
    jState.add("map", HashMapToJMap(gs.getGameBoard().getMap()));
    JsonArray jTiles = new JsonArray();
    for (Tile t : gs.getFromDeck(gs.tilesRemaining())) {
      jTiles.add(TileToJTile(t));
    }
    jState.add("tile*", jTiles);
    JsonArray players = new JsonArray();
    for (PlayerState p : gs.players()) {
      players.add(PlayerStateToJPlayer(p));
    }
    jState.add("players", players);
    return jState;
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
  public static JsonObject PlayerStateToJPlayer(PlayerState ps) {
    JsonObject jPlayer = new JsonObject();
    jPlayer.addProperty("score", ps.score());
    jPlayer.add("tiles", TilesToJTiles(ps.getHand()));

    return jPlayer;
  }

  public static JsonArray TilesToJTiles(List<Tile> tiles) {
    JsonArray jTiles = new JsonArray();
    for(Tile t: tiles) {
      jTiles.add(TileToJTile(t));
    }
    return jTiles;
  }

  /**
   * Converts a JPub's player to a Queue of opponent's PublicPlayerStates
   */
  protected static Queue<PublicPlayerState> convertPlayersToQueueOfPublicPlayerStates(JsonArray players) {
    Queue<PublicPlayerState> states = new ArrayDeque<>();
    for (int i = 1; i < players.size(); i++) { // skip active JPlayer
      states.add(new PlayerStateImpl(players.get(i).getAsString()));
    }
    return states;
  }

  /**
   * Converts the given Tile to a JTile.
   */
  public static JsonObject TileToJTile(Tile t) {
    JsonObject jTile = new JsonObject();
    jTile.addProperty("color", t.color().toString());
    jTile.addProperty("shape", t.shape().toString());
    return jTile;
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
}
