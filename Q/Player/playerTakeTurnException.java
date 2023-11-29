package Player;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Player.Strategy.Strategy;

/**
 * Represents a faulty client that throws an error when the takeTurn() method is called
 */
public class playerTakeTurnException extends playerImpl {

  public playerTakeTurnException(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge knowledge) {
    throw new IllegalStateException("whoops!");
  }
}
