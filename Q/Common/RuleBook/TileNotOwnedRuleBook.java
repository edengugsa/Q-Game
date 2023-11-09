package Common.RuleBook;

import java.util.*;

import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

/**
 * Represents a Strategy that requests the placement of a tile that it does not own.
 */
public class TileNotOwnedRuleBook extends RuleBook {

  private Set<Tile> allTiles = allTiles();

  @Override
  protected boolean isTileInHand(List<Tile> tiles, Queue<Placement> placements) {
    return !super.isTileInHand(tiles, placements);
  }

  /**
   * Returns Tiles that aren't in this list of Tiles
   */
  @Override
  public List<Tile> getPlayerTiles(List<Tile> playerTiles) {
    List<Tile> res = new ArrayList<>();

    for (Tile t: allTiles()) {
      if (!playerTiles.contains(t)) {
        res.add(t);
      }
    }
    return res;
  }


  /**
   *  Returns a set of all possible Tiles.
   */
  private static Set<Tile> allTiles() {
    List<QColor> colors = new ArrayList<>(Arrays.asList
            (QColor.RED, QColor.GREEN, QColor.BLUE, QColor.YELLOW, QColor.ORANGE, QColor.PURPLE));
    List<QShape> shapes = new ArrayList<>(Arrays.asList
            (QShape.star, QShape.eight_star, QShape.square, QShape.circle, QShape.clover, QShape.diamond));
    Set<Tile> tiles = new HashSet<>();

    for (QShape s : shapes) {
      for (QColor c : colors) {
        tiles.add(new Tile(c, s));
      }
    }

    return tiles;
  }

}
