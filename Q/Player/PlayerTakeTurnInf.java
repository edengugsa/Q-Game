package Player;

import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Player.Strategy.Strategy;

public class PlayerTakeTurnInf extends abstractInfPlayer {

  public PlayerTakeTurnInf(String name, Strategy strategy, int when) {
    super(name, strategy, when);
  }

  // have a generic supplier. it either returns an int because it is inf looping
  // or an actual gameCommand
  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge apk) {
    if (this.when > 1) {
      this.when--;
      return super.takeTurn(apk);
    }
    else {
      super.infLoop();
    }
    // should never happen
    throw new IllegalArgumentException("could not take turn and exited inf loop.");
  }
}
