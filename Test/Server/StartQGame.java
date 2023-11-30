package Server;

import Referee.WinnersAndCheaters;

/**
 * Runs the Q Game on localhost:33331
 */
public class StartQGame {
  public static void main(String[] args) {
    try {
      server server = new server(33331);
      WinnersAndCheaters res = server.run();
      System.out.println(res);
    }
    catch (Exception e) {
      System.out.println("Start QGame server failed");
    }
  }
}
