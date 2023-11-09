package Common.GameCommands;

import com.google.gson.JsonElement;
import Common.RuleBook.QRuleBook;
import Common.Scorer.Scorer;
import Common.State.GameState;

/**
 * Represents a command a Player can make. Also includes functionality to check if this is valid
 * to execute on a State and can execute this on a State.
 */
public interface QGameCommand {

  /**
   * @return true if this QGameCommand is legal given the State and the QRuleBook.
   */
  boolean isLegal(QRuleBook rules, GameState game);

  /**
   * Executes this QGameCommand on a given State. It does NOT check if this GameCommand
   * is legal.
   */
  void execute(GameState game) throws IllegalStateException;

  /**
   * Scores this QGameCommand in the given State using the given Common.Scorer.Scorer object.
   */
  void score(GameState game, Scorer scorer);

  /**
   * Replaces the Active Player's tiles.
   */
  void renewPlayerTiles(GameState game);

  /**
   * @return this command represented as a JsonElement.
   */
  JsonElement toJSON();

}
