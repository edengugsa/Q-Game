package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;

/**
 * Represents a Strategy that will produce an invalid GameCommand if given the opportunity.
 */
public interface CheatStrategy extends Strategy {

  /**
   * Given the Active Player's Knowledge of the QGame, can this Strategy produce a GameCommand
   * that violates the rules?
   */
  boolean canCheat(ActivePlayerKnowledge apk);

  /**
   * Computes a QGameCommand that violates one of the Q Game's rules.
   */
  QGameCommand computeHelper(ActivePlayerKnowledge knowledge);
}
