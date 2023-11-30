package Client;

import java.io.IOException;
import java.net.Socket;

import Player.Strategy.LdasgStrategy;
import Player.playerImpl;
import Server.player;

public class runPlayerApples {
  public static void main(String[] args) throws IOException {
//    client client1 = new client("127.0.0.1", 33331,
//            new playerImpl("apples", new LdasgStrategy()));
    client client1 = new client("127.0.0.1", 33331,
            new player(new Socket("127.0.0.1", 33331)));
    client1.run();
  }
}
