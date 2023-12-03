package Player;

import Player.Strategy.Strategy;

public class PlayerWinInf extends abstractInfPlayer {

  public PlayerWinInf(String name, Strategy strategy, int when) {
    super(name, strategy, when);
  }

  // have a generic supplier. it either returns an int because it is inf looping
  // or an actual gameCommand
  @Override
  public void win(boolean w) {
    if (this.when > 1) {
      this.when--;
      super.win(w);
    }
    else {
      super.infLoop();
    }
  }
}
