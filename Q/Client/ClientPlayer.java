package Client;

import java.net.Socket;


public class ClientPlayer {
  Socket server;

  ClientPlayer(String ip, int port) {
    connectToSever(ip, port);

  }

  private void connectToSever(String ip, int port) {
    try {
      this.server = new Socket(ip, port);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Could not join the server");
    }
  }



}
