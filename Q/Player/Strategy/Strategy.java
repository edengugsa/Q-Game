package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.RuleBook.QRuleBook;
import Common.RuleBook.RuleBook;
import Common.State.ActivePlayerKnowledge;

/**
 * Represents a strategy for selecting a GameCommand for the Q Game. The GameCommand is selected
 * based on a Player's tiles and the public knowledge about the game.
 */
public interface Strategy {

  /**
   * @param apk the knowledge about the game
   * @return QGameAction this strategy's selected action based on the active player's knowledge about the game.
   */
  QGameCommand compute(ActivePlayerKnowledge apk);

  void computeOnePlacement() throws IllegalStateException;


}
