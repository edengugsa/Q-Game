package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

/**
 * Represents an AbstractCheat Strategy that will produce a GameCommand that violates the Q Game
 * rules if given the opportunity. Else it will use its fallback Strategy.
 */
public abstract class AbstractCheatStrategy implements CheatStrategy {
  Strategy fallbackStrategy;

  AbstractCheatStrategy(Strategy fallbackStrategy) {
    this.fallbackStrategy = fallbackStrategy;
  }

  abstract public boolean canCheat();


  public Placement computeOnePlacement() {
    return new Placement(new Coordinate(0,0), new Tile(QColor.YELLOW, QShape.square));
  }

  public QGameCommand compute(ActivePlayerKnowledge apk) {
    if (canCheat()) {
      return this.compute(apk);
    }
    else {
      return this.fallbackStrategy.compute(apk);
    }
  }

}
