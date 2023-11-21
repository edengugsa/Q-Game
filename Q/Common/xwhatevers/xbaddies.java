package Common.xwhatevers;

import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
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

public class xbaddies {
  private static final JsonStreamParser parser
          = new JsonStreamParser(new BufferedReader(new InputStreamReader(System.in)));

  public static void main(String[] args) {
    GameState gamestate = JsonToQGame.JStateToGameState(parser.next().getAsJsonObject());

    List<player> players = JsonToQGame.JActorsToPlayerList(parser.next().getAsJsonArray());

      // TODO REMOVE OBSERVER
      List<observer> observers = new ArrayList<>();
      observers.add(new observer());

    WinnersAndCheaters results = new Referee(players, observers, gamestate).runGame();
    System.out.println(QGameToJson.WinnersAndCheatersToJson(results));
  }
}
