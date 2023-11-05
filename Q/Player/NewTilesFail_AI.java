package Player;
import java.util.List;

import Common.Tiles.Tile;
import Player.Strategy.Strategy;

/**
 * Represents a faulty player that throws an error when the newTiles() method is called
 */
public class NewTilesFail_AI extends playerImpl {

  public NewTilesFail_AI(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    throw new IllegalStateException("whoops!");
  }
}
