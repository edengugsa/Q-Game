package Common.RuleBook;

import Common.Tiles.Coordinate;
import Common.Tiles.Tile;

public class NoFitRuleBook extends RuleBook {

  public NoFitRuleBook() {
  }

  @Override
  protected boolean matchesShapeOrColor(Tile tile, Coordinate c1, Coordinate c2) {
    return !super.matchesShapeOrColor(tile, c1, c2);
  }
}
