package Client;

import Player.PlayerTakeTurnInf;
import Player.Strategy.LdasgStrategy;

public class runPlayerBananas {
  public static void main(String[] args) {
    Player.player player = new PlayerTakeTurnInf("B", new LdasgStrategy(), 1);
    client client2 = new client("127.0.0.1", 33331, player);
//    client2.run();
  }
}
