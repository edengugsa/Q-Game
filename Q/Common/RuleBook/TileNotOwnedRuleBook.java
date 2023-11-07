package Common.RuleBook;

import java.util.List;
import java.util.Queue;

import Common.Tiles.Placement;
import Common.Tiles.Tile;

/**
 * Represents a Strategy that requests the placement of a tile that it does not own.
 */
public class TileNotOwnedRuleBook extends RuleBook {
  @Override
  protected boolean isTileInHand(List<Tile> tiles, Queue<Placement> placements) {
    return !super.isTileInHand(tiles, placements);
  }

}
