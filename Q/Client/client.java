package Client;

import com.google.gson.JsonPrimitive;
import java.net.Socket;


/**
 * Represents a client that can join and play in a remote QGame. This client uses a client
 * with a strategy to respond to the Referee's requests and take turns.
 */
public class client {
  Socket socket;
  Player.player player;
  referee referee;
  boolean isQuiet = false; // do we want debug output on standard error?

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
    try {
      this.referee = new referee(this.socket, this.player);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Could not create Proxy Referee " + e.getMessage());
    }
    try {
      this.referee.sendJsonToReferee(new JsonPrimitive(this.player.name()));
    }
    catch (Exception e) {
      throw new IllegalArgumentException("could not send name: " + e.getMessage());
    }
    try {
      this.referee.receiveInformationFromReferee();
    }
    catch (Exception e) {
      throw new IllegalArgumentException("could not receive information from the ref: " + e.getMessage());
    }
  }

  /**
   * Tries to connect to the Server until it succeeds or encounters an exception
   * EFFECT: sets this.socket when this client successfully connects to a socket
   */
  private void connectToServer(String ip, int port) {
    while (this.socket == null) {
      try {
        this.socket = new Socket(ip, port);
      }
      catch (Exception ignored) {
        throw new IllegalArgumentException("Could not join the server");
      }
    }
  }
  
}
