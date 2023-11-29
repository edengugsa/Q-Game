package Common.Scorer;

import java.util.*;

import Common.GameBoard.GameBoard;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

/**
 * Represents the rules and functionality for scoring a PlacementCommand.
 * </p>
 * 1 point is awarded per tile placed.
 * 1 point is awarded for each tile in a contiguous row and/or column of tiles that includes a placed tile.
 * 6 points are awarded for each completion of a "Q";
 * A "Q" is defined as...
 *    A contiguous sequence of tiles where each tile is distinct either by shape or by color,
 *    and all available colors and shapes are represented exactly once.
 * </p>
 * Assumes a given PlacementAction has been validated and enacted on the game board.
 * Does NOT update PlayerState scores.
 */
public class Scorer {

  private int PTS_PER_TILE;
  private int PTS_PER_CONTIG_TILE;
  private int Q_PTS;
  private int PTS_FOR_PLACING_ALL;

  // set of Placements for one turn
  private Queue<Placement> placements;
  private GameBoard board;
  // did the client who made this placement place ALL their tiles?
  private boolean placedAll;

  /**
   * Default constructor. Sets the number of points rewarded to default values.
   */
  public Scorer() {
    this.Q_PTS = 8;
    this.PTS_PER_CONTIG_TILE = 1;
    this.PTS_PER_TILE = 1;
    this.PTS_FOR_PLACING_ALL = 4;
  }

  /**
   * Updates this Common.Scorer.Scorer's number of points rewarded for placing a single tile.
   */
  public void setPTS_PER_TILE(int amt) {
    this.PTS_PER_TILE = amt;
  }

  /**
   * Updates this Common.Scorer.Scorer's number of points rewarded for placing a contiguous tile.
   */
  public void setPTS_PER_CONTIG_TILE(int amt) {
    this.PTS_PER_CONTIG_TILE = amt;
  }

  /**
   * Updates this Common.Scorer.Scorer's number of points rewarded for completing a Q
   */
  public void setQ_PTS(int amt) {
    this.Q_PTS = amt;
  }

  /**
   * Updates this Common.Scorer.Scorer's number of points rewarded for placing all tiles in a hand.
   */
  public void setPTS_FOR_PLACING_ALL(int amt) {
    this.PTS_FOR_PLACING_ALL = amt;
  }

  /**
   * @return the number of points this Common.Scorer.Scorer rewards for a given placement
   */
  public int scorePlacement(Queue<Placement> placements, GameBoard board, boolean placedAll) {
    this.placements = placements;
    this.board = board;
    this.placedAll = placedAll;
    return this.tally();
  }

  /**
   * @return the number of points this ScoreKeeper has recorded for the given placements.
   */
  public int tally() {
    return this.ptsPerTile() + this.ptsForContiguity() + this.ptsForQ() + this.ptsForPlacingAll();
  }

  public int ptsForPlacingAll() {
    if (placedAll) {
      return PTS_FOR_PLACING_ALL;
    }
    return 0;
  }

  /**
   * 1 point is rewarded for each tile placed.
   * @return the number of tiles placed
   */
  protected int ptsPerTile() {
    return this.placements.size() * PTS_PER_TILE;
  }

  /**
   * Updates this ScoreKeeper with the points earned for contiguous tiles from this list of
   * placements.
   * @return the updated ScoreKeeper with points earned
   */
  protected int ptsForContiguity() {
    Set<Placement> visitedInRow = new HashSet<>();
    Set<Placement> visitedInCol = new HashSet<>();
    for (Placement placement : this.placements) {
      visitedInRow.addAll(this.board.getContiguousTilesInRow(placement));
      visitedInCol.addAll(this.board.getContiguousTilesInCol(placement));
    }
    return (visitedInRow.size() + visitedInCol.size()) * PTS_PER_CONTIG_TILE;
  }

  /**
   * Updates this ScoreKeeper with the points earned for earning a Q.
   * @return the updated ScoreKeeper with points earned
   */
  protected int ptsForQ() {
    Set<Set<Placement>> visitedQs = new HashSet<>();
    for (Placement placement : this.placements) {
      Set<Placement> contiguousTilesInRow = this.board.getContiguousTilesInRow(placement);
      Set<Placement> contiguousTilesInCol = this.board.getContiguousTilesInCol(placement);
      if (isQ(contiguousTilesInRow)) {
        visitedQs.add(contiguousTilesInRow);
      }
      if (isQ(contiguousTilesInCol)) {
        visitedQs.add(contiguousTilesInCol);
      }
    }
    return visitedQs.size()*Q_PTS;
  }

  /**
   * @return true if the given sequence of tiles completes a Q.
   * See class definition for what qualifies as a Q.
   */
  protected boolean isQ(Set<Placement> tiles) {
    if (tiles.size() == QColor.values().length) {
      List<QColor> colors = new ArrayList<>();
      List<QShape> shapes = new ArrayList<>();
      for (Placement p : tiles) {
        Tile t = p.tile();
        colors.add(t.color());
        shapes.add(t.shape());
      }
      return new HashSet(colors).size() == tiles.size()
              || new HashSet(shapes).size() == tiles.size();
    }
    return false;
  }

}
