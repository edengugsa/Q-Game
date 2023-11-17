package Client;

import Player.Strategy.LdasgStrategy;
import Player.Strategy.NoFitStrategy;

public class runPlayerBananas {
  public static void main(String[] args) {
    ClientPlayer player2 = new ClientPlayer("127.0.0.1", 33331, "bananas",
            new NoFitStrategy(new LdasgStrategy()));
    player2.run();
  }
}
