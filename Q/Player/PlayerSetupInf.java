package Player;

import java.util.List;

import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Player.Strategy.Strategy;

public class PlayerSetupInf extends abstractInfPlayer {

  public PlayerSetupInf(String name, Strategy strategy, int when) {
    super(name, strategy, when);
  }

  // have a generic supplier. it either returns an int because it is inf looping
  // or an actual gameCommand
  @Override
  public void setup(ActivePlayerKnowledge apk, List<Tile> hand) {
    if (this.when > 0) {
      this.when--;
      super.setup(apk, hand);
    }
    else {
      super.infLoop();
    }
  }
}
