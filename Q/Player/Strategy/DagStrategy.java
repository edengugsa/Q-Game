package Player.Strategy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Common.RuleBook.QRuleBook;
import Common.Tiles.Placement;

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

  public DagStrategy() {
    super();
  }


  /**
   * @return a list of Placements sorted by this DagStrategy's Placement comparator
   */
  protected List<Placement> sort(List<Placement> placements) {
    Collections.sort(placements, this.DagComparator);
    return placements;
  }

  /**
   * Represents this DagStrategy's comparator for sorting Placements. Placement p1 is less than Placement p2 if p1's Coordinate
   * is less than p2's Coordinate.
   */
  private final Comparator<? super Placement>
          DagComparator = Comparator.comparing(Placement::coordinate);
}
