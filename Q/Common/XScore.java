import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Queue;

/**
 * Consumes two JSON inputs from STDIN: a JMAP object and a JPlacements array. Computes the score (a natural
 * number) that this set of placements gathers.
 */
public class XScore {

    private static final JsonUtils utils = new JsonUtils(); // utility class for transforming JSON objects to game representations
    public static void main(String[] args) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    JsonStreamParser jsonStreamParser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

    while (jsonStreamParser.hasNext()) {
      try {
        Queue<Placement> placements = utils.JPlacementsToPlacements(jsonStreamParser.next().getAsJsonArray());
        Map<Coordinate, Tile> map = utils.JMapToHashmap(jsonStreamParser.next().getAsJsonArray());
        System.out.println(new Scorer().scorePlacement(placements, new GameBoard(map), false));
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    reader.close();
  }
}
