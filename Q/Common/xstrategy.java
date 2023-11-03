import com.google.gson.JsonStreamParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *  Consumes a JPub object followed by a JStrategy from STDIN, computes a move, and returns it to STDOUT.
 */
public class xstrategy {

  static JsonStreamParser parser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

  public static void main(String[] args) {
    while (parser.hasNext()) {
      Strategy strategy = JsonUtils.jsonToStrategy(parser.next().getAsString());
      ActivePlayerKnowledge knowledge = JsonUtils.JPubToActivePlayerKnowledge(parser.next().getAsJsonObject());
      System.out.println((strategy.compute(knowledge).toJSON()));
    }
  }
}
