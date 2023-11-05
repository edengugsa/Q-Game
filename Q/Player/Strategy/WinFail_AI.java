package Player.Strategy;

import Player.playerImpl;

/**
 * Represents a faulty player that throws an error when the win() method is called
 */
public class WinFail_AI extends playerImpl {

  public WinFail_AI(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void win(boolean w) {
    throw new IllegalStateException("whoops!");
  }
}
