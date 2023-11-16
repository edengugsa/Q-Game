package Common.xwhatevers;

import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Common.JsonUtils;
import Common.State.GameState;
import Player.player;
import Referee.Referee;
import Referee.observer;
import Referee.WinnersAndCheaters;

/**
 * Testing harness for running a Q Game with an optional Observer and with JCheat Players.
 * Takes in a GameState and JActorSpecA from stdin, runs a complete game, and prints out the names of
 * winners and cheaters.
 */
public class XGamesWithObservers {

  private static final JsonStreamParser parser
          = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

  public static void main(String[] args) {
    GameState gamestate = JsonUtils.JStateToGameState(parser.next().getAsJsonObject());
    List<player> players = JsonUtils.JActorsToPlayerList(parser.next().getAsJsonArray());

    List<observer> observers = new ArrayList<>();
    if (args.length > 0 && (args[0].equals("–show") || args[0].equals("-show"))) {
      observers.add(new observer());
    }

    WinnersAndCheaters results = new Referee(players, observers, gamestate).runGame();
    System.out.println(JsonUtils.WinnersAndCheatersToJson(results));
  }
}
