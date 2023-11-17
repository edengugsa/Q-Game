package Player;
import java.util.List;

import Common.GameBoard.GameBoard;
import Common.State.ActivePlayerKnowledge;
import Common.State.GameState;
import Common.Tiles.Tile;
import Player.Strategy.Strategy;

/**
 * Represents a Player that raises an exception every time setup() is invoked.
 */
public class playerSetupException extends playerImpl {

  public playerSetupException(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void setup(ActivePlayerKnowledge apk, List<Tile> hand) {
    throw new IllegalStateException("Setup exc");
  }

}
