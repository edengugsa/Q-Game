package Client;

import Player.Strategy.LdasgStrategy;

public class runPlayerApples {
  public static void main(String[] args) {
    ClientPlayer player1 = new ClientPlayer("127.0.0.1", 33331, "apples", new LdasgStrategy());
    player1.run();
  }
}
