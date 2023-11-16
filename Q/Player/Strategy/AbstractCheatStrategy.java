package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;

/**
 * Represents a composite strategy that will first try producing a GameCommand that violates the Q
 * Game. Else, it will use its fallback Strategy.
 *
 * Concrete classes that extend this CheatStrategy will decide which rule their GameCommand will
 * violate.
 */
public abstract class AbstractCheatStrategy implements CheatStrategy {
  Strategy fallbackStrategy;

  AbstractCheatStrategy(Strategy fallbackStrategy) {
    this.fallbackStrategy = fallbackStrategy;
  }

  /**
   * Most Cheat Strategies can always cheat.
   */
  public boolean canCheat(ActivePlayerKnowledge apk) {
    return true;
  }

  public abstract QGameCommand computeHelper(ActivePlayerKnowledge apk);

  public QGameCommand compute(ActivePlayerKnowledge apk) {
    if (canCheat(apk)) {
      return this.computeHelper(apk);
    }
    else {
      return this.fallbackStrategy.compute(apk);
    }
  }
}
