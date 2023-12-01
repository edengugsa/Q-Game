package Client;

import com.google.gson.JsonPrimitive;
import java.net.Socket;

import Common.DebugUtil;


/**
 * Represents a client that can join and play in a remote QGame. This client uses a client
 * with a strategy to respond to the Referee's requests and take turns.
 */
public class client {
  Socket socket;
  public Player.player player;
  referee referee;
  boolean isQuiet = false; // do we want debug output on standard error?
  boolean isConnected = false;

  /**
   * Creates a client that joins a Q Game on the given ip address and port number and makes
   * moves according to its player.
   * @param ip address of server
   * @param port number server
   * @param player with a strategy that can take turns.
   */
  public client(String ip, int port, Player.player player) {
    connectToServer(ip, port);
    this.player = player;
  }

  /**
   * Creates a client that joins a Q Game on the given ip address and port number and makes
   * moves according to its client.
   * @param ip address of server
   * @param port number server
   * @param player with a strategy that can take turns.
   * @param isQuiet do we want debug output on standard error?
   */
  public client(String ip, int port, Player.player player, boolean isQuiet) {
    this(ip, port, player);
    this.isQuiet = isQuiet;
  }

  /**
   * Starts running this Client Player.
   * 1. Creates a new referee for this client.
   * 2. Sends its name to the Server.
   * 3. Starts listening for method calls from the Server's Referee.
   */
  public void run() {

  }

  public void sendName() {
    try {
      this.referee = new referee(this.socket, this.player);
      DebugUtil.debug(isQuiet, this.player.name() + " joined the server");
    }
    catch (Exception e) {
      DebugUtil.debug(isQuiet, "Could not create Proxy Referee " + e.getMessage());
    }
    try {
      this.referee.sendJsonToReferee(new JsonPrimitive(this.player.name()));
      DebugUtil.debug(isQuiet, this.player.name() + " sent their name");
    }
    catch (Exception e) {
      DebugUtil.debug(isQuiet, "could not send name: " + e.getMessage());
    }
  }

  public void proxyRefereeReceiveInfoFromServer() {
    try {
      this.referee.receiveInformationFromServer();
    }
    catch (Exception e) {
      DebugUtil.debug(isQuiet, this.player.name() + " could not receive information from the server: " + e.getMessage());
    }
  }

  public boolean isConnected() {
    return this.isConnected;
  }

  /**
   * Tries to connect to the Server until it succeeds or encounters an exception
   * EFFECT: sets this.socket when this client successfully connects to a socket
   */
  private void connectToServer(String ip, int port) {
    while (!this.isConnected) {
      try {
        this.socket = new Socket(ip, port);
        this.isConnected = true;
      }
      catch (Exception ignored) {
//        DebugUtil.debug(isQuiet, "Could not join the server");
      }
    }
  }
  
}
