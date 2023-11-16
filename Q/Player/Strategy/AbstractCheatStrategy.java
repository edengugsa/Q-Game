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
public abstract class AbstractCheatStrategy implements Strategy {
  protected Strategy fallbackStrategy;

  AbstractCheatStrategy(Strategy fallbackStrategy) {
    this.fallbackStrategy = fallbackStrategy;
  }

}
