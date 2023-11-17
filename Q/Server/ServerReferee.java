package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Common.RuleBook.RuleBook;
import Player.player;
import Referee.Referee;

public class ServerReferee {
  ServerSocket serverSocket;
  List<ProxyPlayer> proxyPlayerList;


  ServerReferee(int port) throws IOException {
    this.serverSocket = new ServerSocket(port);
  }

  /**
   * The server waits for TCP sign-ups for some time 20s for a minimum number (here, 2) of remote
   * clients to connect and sign up. As long as there isn’t this minimum number of clients signed
   * up at the end of a waiting period, the server re-enters the waiting state (just once) and for
   * the same amount of time. Either waiting period also ends when the server has accepted a maximal
   * number (here, 4) of client sign ups. If at most one player signs up by the end of the second
   * waiting period, the server doesn’t run a game and instead delivers a simple default result:
   * [ [], [] ].
   */
  /**
   * Listens for 20 seconds on its socket for clients to connect. Returns a list of ProxyPlayer for
   * every remove client that connected and sent a valid JName.
   */
  List<player> signup() {
    List<player> proxyPlayersList = new ArrayList<>();

    while(proxyPlayersList.size() < 4) {
      try {
        Socket playerSocket = this.serverSocket.accept();
        ProxyPlayer p = new ProxyPlayer(playerSocket);
        this.tryToGetName(p);
        proxyPlayersList.add(p);
        System.out.println("Added new Player: " + p.name());
      }
      catch(Exception e) {
        System.out.println("Server Referee signup threw");
        System.out.println(e.getMessage());
      }
    }
    return proxyPlayersList;
  }

  private void tryToGetName(PlayerProxy p) {
    p.name();
  }

  public void run() {
    try {
      List<player> players = this.signup();
      Referee ref = new Referee(players);
      ref.runGame();
    }
    catch (Exception e) {
      System.out.println("Server Referee run threw");
    }
  }


}
