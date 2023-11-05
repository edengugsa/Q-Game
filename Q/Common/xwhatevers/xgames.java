import com.google.gson.JsonArray;
import com.google.gson.JsonStreamParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Reads in a JState and JActors, runs a complete game of Q, and then outputs the winners and cheaters.
 */
public class xgames {
  private static final JsonStreamParser parser
          = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

  public static void main(String[] args) {
      GameState gamestate = JsonUtils.JStateToGameState(parser.next().getAsJsonObject());
      List<AIPlayer> players = JsonUtils.JActorsToAIPlayerList(parser.next().getAsJsonArray());
      List<List<String>> results = new Referee(players, gamestate).runGame();
      System.out.println("["+JsonUtils.NamesToJNames(results.get(0)) + ", " + JsonUtils.NamesToJNames(results.get(1)) + "]");
  }
}
