package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Player.playerImpl;

/**
 * Represents a faulty player that throws an error when the takeTurn() method is called
 */
public class TakeTurnFail_AI extends playerImpl {

  public TakeTurnFail_AI(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge knowledge) {
    throw new IllegalStateException("whoops!");
  }
}
