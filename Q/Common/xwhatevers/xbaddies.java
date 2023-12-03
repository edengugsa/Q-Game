package Common.xwhatevers;

import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Common.JsonToQGame;
import Common.QGameToJson;
import Common.State.GameState;
import Player.player;
import Referee.WinnersAndCheaters;
import Referee.Referee;
import Referee.observer;

/**
 * Testing script for Players that take too long to complete an API call.
 * Reads in a GameState and List of Players from stdin, runs a complete game, and prints out
 * the winners and cheaters.
 * Players that take too long will be disqualified.
 */
public class xbaddies {

  public static void main(String[] args) throws IOException {
    JsonStreamParser parser = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

    GameState gamestate = JsonToQGame.JStateToGameState(parser.next().getAsJsonObject());
    List<player> players = JsonToQGame.JActorsToPlayerList(parser.next().getAsJsonArray());

    WinnersAndCheaters results = new Referee(players, gamestate).runGame();

    System.out.println(QGameToJson.WinnersAndCheatersToJson(results));
    System.exit(0);
  }

}
