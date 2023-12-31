package Player.Strategy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Common.RuleBook.QRuleBook;
import Common.Tiles.Coordinate;

/**
 * Represents the Dag Strategy for choosing Placements. This Strategy will sort a Tile's
 * possible Placements by the Placement's Coordinate using row column order. It will pick the smallest
 * Placement until no more Placements can be made.
 */
public class DagStrategy extends LexicoStrategy  {

  /**
   * Constructs a DagStrategy that computes GameCommands that adhere to the given RuleBook.
   */
  public DagStrategy(QRuleBook rulebook) {
    super(rulebook);
  }

  /**
   *    * Constructs a DagStrategy that computes GameCommands that adhere to the default Rules.
   */
  public DagStrategy() {
    super();
  }


  /**
   *
   */
  protected void sort(List<Coordinate> coordinate) {
    Collections.sort(coordinate, this.DagComparator);
  }

  /**
   * Represents this DagStrategy's comparator for sorting Placements. Placement p1 is less than Placement p2 if p1's Coordinate
   * is less than p2's Coordinate.
   */
  private final Comparator<? super Coordinate>
          DagComparator = CoordinateComparator;
}
