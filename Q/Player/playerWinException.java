package Player;

import Player.Strategy.Strategy;

/**
 * Represents a faulty client that throws an error when the win() method is called
 */
public class playerWinException extends playerImpl {

  public playerWinException(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void win(boolean w) {
    throw new IllegalStateException("win exception");
  }
}
