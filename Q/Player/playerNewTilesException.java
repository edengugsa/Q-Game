package Player;
import java.util.List;

import Common.Tiles.Tile;
import Player.Strategy.Strategy;

/**
 * Represents a faulty client that throws an error when the newTiles() method is called
 */
public class playerNewTilesException extends playerImpl {

  public playerNewTilesException(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    throw new IllegalStateException("whoops!");
  }
}
