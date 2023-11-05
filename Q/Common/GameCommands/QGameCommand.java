import com.google.gson.JsonElement;

/**
 * Represents a command a Player can make. Also includes functionality to check if this is valid
 * to execute on a GameState and can execute this on a GameState.
 */
public interface QGameCommand {

  /**
   * @return true if this QGameCommand is legal given the GameState and the QRuleBook.
   */
  boolean isLegal(QRuleBook rules, GameState game);

  /**
   * Executes this QGameCommand on a given GameState. It does NOT check if this GameCommand
   * is legal.
   */
  void execute(GameState game) throws IllegalStateException;

  /**
   * Scores this QGameCommand in the given GameState using the given Scorer object.
   */
  void score(GameState game, Scorer scorer);

  /**
   * @return this command represented as a JsonElement.
   */
  JsonElement toJSON();

}
