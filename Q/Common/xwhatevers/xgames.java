package Common.xwhatevers;
import com.google.gson.JsonStreamParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import Common.JsonToQGame;
import Common.QGameToJson;
import Common.State.GameState;
import Player.player;
import Referee.Referee;
import Referee.WinnersAndCheaters;

/**
 * Reads in a JState and JActors, runs a complete game of Q, and then outputs the winners and cheaters.
 */
public class xgames {
  private static final JsonStreamParser parser
          = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

  public static void main(String[] args) {
      GameState gamestate = JsonToQGame.JStateToGameState(parser.next().getAsJsonObject());

      List<player> players = JsonToQGame.JActorsToPlayerList(parser.next().getAsJsonArray());

//      // TODO REMOVE OBSERVER
//      List<observer> observers = new ArrayList<>();
//      observers.add(new observer());

      WinnersAndCheaters results = new Referee(players, gamestate).runGame();
      System.out.println(QGameToJson.WinnersAndCheatersToJson(results));
  }
}
