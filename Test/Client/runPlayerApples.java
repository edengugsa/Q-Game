package Client;

import Player.Strategy.LdasgStrategy;

public class runPlayerApples {
  public static void main(String[] args) {
    client client1 = new client("127.0.0.1", 33331, "apples", new LdasgStrategy());
    client1.run();
  }
}
