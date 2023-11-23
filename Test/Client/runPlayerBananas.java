package Client;

import Player.PlayerTakeTurnInf;
import Player.Strategy.LdasgStrategy;
import Player.player;

public class runPlayerBananas {
  public static void main(String[] args) {
    player player = new PlayerTakeTurnInf("bananas", new LdasgStrategy(), 4);
    ClientPlayer player2 = new ClientPlayer("127.0.0.1", 33331, player);
    player2.run();
  }
}
