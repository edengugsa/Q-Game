package Common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

import Common.GameBoard.GameBoard;
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
import Player.playerSetupException;
import Player.Strategy.Strategy;
import Player.playerTakeTurnException;
import Player.playerWinException;
import Referee.WinnersAndCheaters;



public class QGameToJson {

  public static JsonArray NamesToJNames(List<String> names) {
    JsonArray jNames = new JsonArray();
    for (String name : names) {
      jNames.add(name);
    }
    return jNames;
  }

  /**
   *  JPub is an object with three fields:
   *
   *       { "map"      : JMap,
   *
   *         "tile*"    : Tile#,
   *
   *         "players"  : [JPlayer, Tile#, ..., Tile#] }
   * @param apk
   * @return
   */
  public static JsonObject ActivePlayerKnowledgeToJPub(ActivePlayerKnowledge apk) {
    JsonObject jPub = new JsonObject();

    jPub.add("map", HashMapToJMap(apk.getBoard().getMap()));
    jPub.addProperty("tile*", apk.getNumRefTilesRemaining());
    JsonArray players = new JsonArray();
    players.add(ActivePlayerKnowledgeToJPlayer(apk));

    for (PublicPlayerState opponentState : apk.getOpponentStates()) {
      players.add(opponentState.score());
    }
    jPub.add("players", players);
    return jPub;
  }

  public static JsonObject ActivePlayerKnowledgeToJPlayer(ActivePlayerKnowledge apk) {
    JsonObject jPlayer = new JsonObject();
    Queue<PublicPlayerState> playerStates = apk.getOpponentStates();
    jPlayer.addProperty("score", apk.getActivePlayerScore());
    jPlayer.addProperty("name", apk.getActivePlayerName());
    jPlayer.add("tile*", TilesToJTiles(apk.getActivePlayerTiles()));
    return jPlayer;
  }


  public static JsonArray ListOfTilesToJsonArray(List<Tile> tiles) {
    JsonArray jTiles = new JsonArray();
    for(Tile t : tiles) {
      jTiles.add(t.toJSON());
    }

    return jTiles;
  }

  /**
   * Converts the given WinnersAndCheaters to a JsonArray of a list of winners sorted alphabetically
   * by their name and a list of cheaters sorted by the order in which they were removed.
   */
  public static JsonArray WinnersAndCheatersToJson(WinnersAndCheaters w) {
    JsonArray res = new JsonArray();
    w.winners.sort(String::compareTo);
    JsonArray winners = new JsonArray();
    for (String winner : w.winners) {
      winners.add(winner);
    }
    JsonArray cheaters = new JsonArray();
    for (String cheater : w.cheaters) {
      cheaters.add(cheater);
    }
    res.add(winners);
    res.add(cheaters);
    return res;
  }


  /**
   * Converts the given hashmap of Coordinates and Tiles to a JMap. Tiles is a non-empty hashmap!
   * @param tiles
   */
  public static JsonArray HashMapToJMap(Map<Coordinate, Tile> tiles) {

    List<Map.Entry<Coordinate, Tile>> tilesAsList = JsonToQGame.sortGameboard(tiles);

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
      jCell.add(entry.getValue().toJSON()); // JTile
      jRow.add(jCell);
    }
    jMap.add(jRow); // add the final row to the map
    return jMap;
  }


  public static JsonObject GameStateToJState(GameState gs) {
    JsonObject jState = new JsonObject();
    jState.add("map", HashMapToJMap(gs.getGameBoard().getMap()));
    JsonArray jTiles = new JsonArray();
    for (Tile t : gs.getFromDeck(gs.tilesRemaining())) {
      jTiles.add(t.toJSON());
    }
    jState.add("tile*", jTiles);
    JsonArray players = new JsonArray();
    for (PlayerState p : gs.players()) {
      players.add(PlayerStateToJPlayer(p));
    }
    jState.add("players", players);
    return jState;
  }

  public static JsonArray TilesToJTiles(List<Tile> tiles) {
    JsonArray jTiles = new JsonArray();
    for(Tile t: tiles) {
      jTiles.add(t.toJSON());
    }
    return jTiles;
  }

  public static JsonObject WinBooleanToJsonBool(Boolean win) {
    JsonObject winJson = new JsonObject();
    winJson.addProperty("win", win.booleanValue());
    return winJson;
  }

  public static JsonObject PlayerStateToJPlayer(PlayerState ps) {
    JsonObject jPlayer = new JsonObject();
    jPlayer.addProperty("score", ps.score());
    jPlayer.addProperty("name", ps.name());
    jPlayer.add("tile*", TilesToJTiles(ps.getHand()));

    return jPlayer;
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

  public static JsonArray FormatJson(String mName, List<JsonElement> argument) {
    JsonArray formattedJson = new JsonArray();
    formattedJson.add(mName);
    JsonArray argumentsArray = new JsonArray();

    for(JsonElement element: argument) {
      argumentsArray.add(element);
    }

    formattedJson.add(argumentsArray);

    return formattedJson;
  }
}
