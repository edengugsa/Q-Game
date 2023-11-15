package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;


/**
 * Represents an AbstractCheat Strategy that will produce a GameCommand that violates the Q Game
 * rules if given the opportunity. Else it will use its fallback Strategy.
 */
public abstract class AbstractCheatStrategy implements CheatStrategy {
  Strategy fallbackStrategy;

  AbstractCheatStrategy(Strategy fallbackStrategy) {
    this.fallbackStrategy = fallbackStrategy;
  }

  /**
   * Given the Active Player's Knowledge of the QGame, can this Strategy produce a GameCommand
   * that violates the rules?
   */
  public boolean canCheat(ActivePlayerKnowledge apk) {
    return true;
  }



//  public Placement computeOnePlacement() {
//    return new Placement(new Coordinate(0,0), new Tile(QColor.YELLOW, QShape.square));
//  }

  public QGameCommand compute(ActivePlayerKnowledge apk) {
    if (canCheat(apk)) {
      return this.compute(apk);
    }
    else {
      return this.fallbackStrategy.compute(apk);
    }
  }

}
