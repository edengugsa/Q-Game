package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import Common.DebugUtil;
import Common.RuleBook.RuleBook;
import Common.TimeUtils;
import Referee.Referee;
import Referee.WinnersAndCheaters;
import Referee.RefereeConfig;

/**
 * Represents a Server to run the QGame on a specified port. This Server waits for enough players
 * to start a game and then creates a Referee to run the game to completion.
 *
 * For every remote client that joins the game, this class makes a new player to convert our
 * data representations into their JSON representation to send over to a client.
 */
public class server {
  private int SIGNUP_TIMEOUT = 20;
  private int NAME_TIMEOUT = 3;
  private int MAX_NUM_SIGNUPS = 2;
  private int MIN_NUM_PLAYERS = 2;
  private int MAX_NUM_PLAYERS = 4;
  private ServerSocket serverSocket;
  private List<player> listOfPlayerProxies;
  private boolean isQuiet = true;

  private RefereeConfig refereeConfig;

  server(int port) {
    this.createServer(port);
    this.listOfPlayerProxies = new ArrayList<>();
  }

  public server(int port, ServerConfig config) {
    this(port);
    this.SIGNUP_TIMEOUT = config.serverWait;
    this.MAX_NUM_SIGNUPS = config.serverTries;
    this.NAME_TIMEOUT = config.waitForSignup;
    this.isQuiet = config.quiet;
    this.refereeConfig = config.refereeConfig;
  }

  /**
   * EFFECT sets this.serverSocket to a ServerSocket running on the given port.
   */
  private void createServer(int port) {
    try {
      this.serverSocket = new ServerSocket(port);
    }
    catch(Exception ignored) {
      DebugUtil.debug(this.isQuiet, "Could not create server socket");
    }
  }

  /**
   * Run an entire game to completion.
   * @return the game results
   */
  public WinnersAndCheaters run() {
    this.runSignup();
    ArrayList<Player.player> players = new ArrayList<>(this.listOfPlayerProxies);
    WinnersAndCheaters gameResults = new WinnersAndCheaters(new ArrayList<>(), new ArrayList<>());

    if (this.listOfPlayerProxies.size() >= MIN_NUM_PLAYERS) {
      Referee ref = this.createReferee(players);
      gameResults = ref.runGame();
    }

    this.shutDown();
    return gameResults;
  }

  private Referee createReferee(ArrayList<Player.player> players) {
    if (this.refereeConfig == null) {
      return new Referee(players);
    }
    else {
      return new Referee(players, this.refereeConfig);
    }
  }

  /**
   * Waits SIGNUP_TIMEOUT seconds at most MAX_NUM_SIGNUPS number of times for a valid number
   * of clients to join the game.
   */
  protected void runSignup() {
    for (int i = 0; i < MAX_NUM_SIGNUPS; i++) {
      try {
        TimeUtils.callWithTimeOut(this::signupPlayers, SIGNUP_TIMEOUT);
      }
      catch (Exception ignored) {
      }
      // if we get enough players on the first iteration
      if (this.listOfPlayerProxies.size() >= MIN_NUM_PLAYERS) {
        break;
      }
    }
  }

  /**
   * EFFECT: adds a new player to this.listOfPlayerProxies for every client that connects and
   * provides a valid JName within NAME_TIMEOUT seconds. This method will add anywhere from
   * 0 to the MAX_NUM_PLAYERS ProxyPlayers.
   * @return 0 upon completion
   */
  protected int signupPlayers() {
    this.listOfPlayerProxies = new ArrayList<>();

    while (this.listOfPlayerProxies.size() < MAX_NUM_PLAYERS) {
      Socket playerSocket;
      try {
        playerSocket = this.serverSocket.accept();
        try {
          TimeUtils.callWithTimeOut(() -> this.signupAPlayer(playerSocket), NAME_TIMEOUT);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
          // unable to sign up a client
          playerSocket.close();
        }
      }
      catch (IOException ignored) {
      }
    }

    return 0;
  }


  /**
   * Creates a new player for a client that connected to this server and provided a valid JName.
   * @param socket to communicate with the client
   * @return 0 upon completion
   */
  protected int signupAPlayer(Socket socket) {
    player player = new player(socket);
    if (player.name() != null) {
      this.listOfPlayerProxies.add(player);
      System.out.println("Added new Player: " + player.name());
    }
    return 0;
  }



  protected void shutDown() {
    try {
      this.serverSocket.close();
    }
    catch(IOException e) {
      System.out.println("Could not server");
    }
  }

}
