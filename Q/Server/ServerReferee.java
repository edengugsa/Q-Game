package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import Common.QGameToJson;
import Common.RuleBook.RuleBook;
import Common.TimeUtils;
import Player.player;
import Referee.Referee;
import Referee.WinnersAndCheaters;
import Referee.observer;

public class ServerReferee {
  static final int SIGNUP_TIMEOUT = 10;
  static final int NAME_TIMEOUT = 3;
  static final int MIN_NUM_PLAYERS = 2;
  static final int MAX_NUM_PLAYERS = 4;
  ServerSocket serverSocket;
  List<ProxyPlayer> listOfPlayerProxies;

  ServerReferee(int port) throws IOException {
    this.serverSocket = new ServerSocket(port);
    this.listOfPlayerProxies = new ArrayList<>();
  }

  /**
   * Adds a new ProxyPlayer to this Server for every Player that requests to join this game and
   * provided a valid JName. This method will anywhere from 0 to the MAX_NUM_PLAYERS to join.
   */
  public int signup() {
    this.listOfPlayerProxies = new ArrayList<>();

    while (this.listOfPlayerProxies.size() < MAX_NUM_PLAYERS) {
      Socket playerSocket;
      try {
        playerSocket = this.serverSocket.accept();
        try {
          int p = TimeUtils.callWithTimeOut(() -> this.signupAPlayer(playerSocket), NAME_TIMEOUT);
          System.out.println("Players so far " + this.listOfPlayerProxies.size());
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
          // unable to sign up a player
          playerSocket.close();
          System.out.println("ServerReferee::signup 60 " + e.getMessage());
        }
      }
      catch (IOException ignored) {
      }
    }

    return 0;
  }

  /**
   *
   */
  protected void trySignup() {
    for (int i = 0; i < 2; i++) {
      try {
        System.out.println("starting iteration wait: " + (i+1));
        TimeUtils.callWithTimeOut(this::signup, SIGNUP_TIMEOUT);
      }
      catch (Exception e) {
        System.out.println("tried to sign up but surpassed 20s limit: " + e.getMessage());
      }
      // if we get enough players on the first iteration
      if (this.listOfPlayerProxies.size() >= MIN_NUM_PLAYERS) {
        System.out.println("Found " + this.listOfPlayerProxies.size() + " to join our game");
        break;
      }
    }
    System.out.println("Found " + this.listOfPlayerProxies.size() + " to join our game");
  }


  /**
   * Signs up a new Proxy Player.
   * @return 0 upon completion
   */
  protected int signupAPlayer(Socket socket) {
    ProxyPlayer player = new ProxyPlayer(socket);
    if (player.name() != null) {
      this.listOfPlayerProxies.add(player);
      System.out.println("Added new Player: " + player.name());
    }

    return 0;
  }

  public WinnersAndCheaters run() {
      this.trySignup();
      ArrayList<player> players = new ArrayList<>(this.listOfPlayerProxies);
      WinnersAndCheaters gameResults = new WinnersAndCheaters(new ArrayList<>(), new ArrayList<>());
      System.out.println("players list: " + this.listOfPlayerProxies);

      if (this.listOfPlayerProxies.size() >= MIN_NUM_PLAYERS) {
        System.out.println("Started game with " + this.listOfPlayerProxies.size());
        List<observer> observers = new ArrayList<>();
//        observers.add(new observer()); // TODO SHOULD GO SOMEHWERE ELSE
        Referee ref = new Referee(players, observers, new RuleBook());
        gameResults = ref.runGame();
        System.out.println("Game Over: " + QGameToJson.WinnersAndCheatersToJson(gameResults));
      }
      return gameResults;
  }

  public void shutDown() {
    try {
      this.serverSocket.close();
    }
    catch(IOException e) {
      System.out.println("Could not server");
    }
  }



}
