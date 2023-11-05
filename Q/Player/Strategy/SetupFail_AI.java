package Player.Strategy;
import java.util.List;

import Common.GameBoard.GameBoard;
import Common.Tiles.Tile;
import Player.playerImpl;

/**
 * Represents a Player that raises an exception every time setup() is invoked.
 */
public class SetupFail_AI extends playerImpl {

  public SetupFail_AI(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void setup(GameBoard board, List<Tile> hand) {
    throw new IllegalStateException("whoops!");
  }

}
