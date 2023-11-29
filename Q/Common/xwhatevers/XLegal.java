import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Reads in a JPlacements and JPub from STDIN and checks if the requested Placements are legal.
 * If they are, returns a new JMap of placed Tiles, otherwise returns false.
 */
public class XLegal {

  public static void main(String[] args) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    JsonStreamParser jsonStreamParser = new JsonStreamParser(reader);

    while (jsonStreamParser.hasNext()) {
      JsonObject jPub = jsonStreamParser.next().getAsJsonObject();
      JsonArray jPlacements = jsonStreamParser.next().getAsJsonArray();
      try {
        JsonArray jmap = placeOnMap(jPub, jPlacements);
        System.out.println(jmap);
      }
      catch(Exception e) {
        System.out.println("false");
      }
    }
    reader.close();
  }


  /**
   * Places the given JPlacements on the gameboard.
   * @throws IllegalArgumentException if the JPlacements are invalid
   */
  protected static JsonArray placeOnMap(JsonObject jpub, JsonArray jPlacements) throws IllegalArgumentException {
    JsonArray map = jpub.get("map").getAsJsonArray();
    JsonObject activePlayer = jpub.get("players").getAsJsonArray().get(0).getAsJsonObject();

    // Extract and Parse JPlayer, JMap, and JPlacements
    GameBoard gameboard = JsonUtils.JMapToGameBoard(map);
    Queue<Placement> placements = JsonUtils.JPlacementsToPlacements(jPlacements);

    Queue<PlayerState> playerList = new ArrayDeque<>();
    PlayerState player = JsonUtils.JPlayerToPlayerState(activePlayer);
    playerList.add(player);
    playerList.add(new PlayerStateImpl("dummy", new ArrayList<>(), 3)); // dummy client
    PlacementCommand pc = new PlacementCommand(placements);

    if (new RuleBook().allows(pc, gameboard)) {
      GameState gs = new GameState(playerList, gameboard);
      gs.execute(pc);
      return JsonUtils.HashMapToJMap(gs.getGameBoard().getMap());
    }
    else {
      throw new IllegalArgumentException("FALSE");
    }
  }

}


