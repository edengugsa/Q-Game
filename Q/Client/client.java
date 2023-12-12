package Client;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import Client.ClientConfig;

import Common.DebugUtil;


/**
 * Represents a client that can join and play in a remote QGame. This client uses a client
 * with a strategy to respond to the Referee's requests and take turns.
 */
public class client {
  Socket socket;
  public Player.player player;
  referee referee;
  boolean isQuiet;
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
    this.isQuiet = true;
  }

  /**
   * Creates a client that joins a Q Game on the given ip address and port number and makes
   * moves according to its client.
   * @param cc to configure this client with parameters like having debug output, and what address
   *           the server is located at.
   * @param player with a strategy that can take turns.
   */
  public client(ClientConfig cc, Player.player player) {
    this(cc.getHost(), cc.getPort(), player);
    this.isQuiet = cc.isQuiet();
  }

  public void run() {
    this.sendName();
    this.proxyRefereeReceiveInfoFromServer();
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
      catch (IOException ignored) {
        // keep waiting for the server to start
      }
    }
  }

  public void sendName() {
    this.createRef();
    try {
      this.referee.sendJsonToReferee(new JsonPrimitive(this.player.name()));
      DebugUtil.debug(isQuiet, this.player.name() + " sent their name");
    }
    catch (Exception e) {
      DebugUtil.debug(isQuiet, "could not send name: " + e.getMessage());
    }
  }

  private void createRef() {
    try {
      this.referee = new referee(
              new JsonStreamParser(new BufferedReader(new InputStreamReader(socket.getInputStream()))),
              new JsonWriter(new OutputStreamWriter(socket.getOutputStream())), this.player, this.isQuiet);
      DebugUtil.debug(isQuiet, this.player.name() + " joined the server");
    }
    catch (Exception e) {
      DebugUtil.debug(isQuiet, "Could not create Proxy Referee " + e.getMessage());
    }
  }

  public void proxyRefereeReceiveInfoFromServer() {
    try {
      this.referee.receiveInformationFromServer();
    }
    catch (Exception e) {
      try {
        DebugUtil.debug(isQuiet, this.player.name() + " received invalid Json from the Server: " + e.getMessage());
        this.socket.close();
      }
      catch (IOException exc) {
        DebugUtil.debug(isQuiet, this.player.name() + " could not receive information from the server: " + exc.getMessage());
      }
    }
  }
}
