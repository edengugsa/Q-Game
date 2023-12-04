package Player.Strategy;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Common.RuleBook.QRuleBook;
import Common.Tiles.Coordinate;


/**
 * <p>
 * Represents a strategy for choosing a GameCommand for The Q Game.
 * </p>
 * This strategy first tries to find as many possible tile placements on the map, starting with the
 * "smallest" tile in the client's hand. This Strategy ranks the "smallest" Tile's possible
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
   *
   */
  protected void sort(List<Coordinate> coordinates) {
    Collections.sort(coordinates, this.neighborsComparator);
  }

  /**
   * Represents this LdasgStrategy's comparator used for sorting placements.
   * Sorts Placements first by the most existing number of neighbors, and ties are broken using the 
   * row-column order of coordinates.
   */
  private  Comparator<Coordinate> neighborsComparator = Comparator
          .comparing((Coordinate c) -> this.mockBoard.getNumFreeNeighbors(c))
          .thenComparing(CoordinateComparator);
}
