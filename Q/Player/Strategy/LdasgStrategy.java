package Player.Strategy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Common.RuleBook.QRuleBook;
import Common.Tiles.Placement;


/**
 * <p>
 * Represents a strategy for choosing a GameCommand for The Q Game.
 * </p>
 * This strategy first tries to find as many possible tile placements on the map, starting with the
 * "smallest" tile in the player's hand. This Strategy ranks the "smallest" Tile's possible
 * placements based on the number of neighboring. Tiles it has. The more neighbors the better.
 */
public class LdasgStrategy extends LexicoStrategy {

  /**
   * Constructs a LdasgStrategy that computes GameCommands.
   */
  public LdasgStrategy(QRuleBook rulebook) {
    super(rulebook);
  }

  /**
   * Constructs a Ldasg Strategy that computes moves based on the default rulebook
   */
  public LdasgStrategy() {
    super();
  }

  /**
   * @return a list of placements sorted by this LdasgStrategy's comparator. 
   */
  protected List<Placement> sort(List<Placement> placements) {
    Collections.sort(placements, this.neighborsComparator);
    return placements;
  }

  /**
   * Represents this LdasgStrategy's comparator used for sorting placements.
   * Sorts Placements first by the most existing number of neighbors, and ties are broken using the 
   * row-column order of coordinates.
   */
  private Comparator<Placement> neighborsComparator = Comparator
          .comparing((Placement p) -> this.currentState.getBoard().getNumNeighbors(p.coordinate())).reversed()
          .thenComparing(Placement::coordinate);
}
