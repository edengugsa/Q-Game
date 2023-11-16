package Player.Strategy;

import java.util.ArrayDeque;
import java.util.Deque;

import Common.GameBoard.GameBoard;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.RuleBook.RuleBook;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

/**
 *  Represents a Strategy that produces a PlacementCommand of two Placements that are not in the same
 *  row or column.
 */
public class NotALineStrategy extends AbstractCheatStrategy {

  public NotALineStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
  }

  /**
   * Does the Player have at least two Tiles?
   */
  @Override
  public QGameCommand compute(ActivePlayerKnowledge apk) {
    if (apk.getNumPlayerTiles() > 1) {
      return this.computeHelper(apk);
    }
    else {
      return this.fallbackStrategy.compute(apk);
    }
  }

  /**
   * Returns a PlacementCommand with exactly two Placements that are not in the same row or col.
   * If that is not possible, it will revert to its fallback strategy.
   */
  private QGameCommand computeHelper(ActivePlayerKnowledge apk) {
    GameBoard board = apk.getBoard();
    Deque<Placement> notInLinePlacements = new ArrayDeque<>();
    RuleBook rulebook = new RuleBook();

    for (Tile t : apk.getActivePlayerTiles()) {
      for (Placement p : board.placementAdjacentOptions(t)) {
        if (rulebook.matchesNeighbors(board, p)) {
          notInLinePlacements.add(p);
          if (notInLinePlacements.size() < 2) {
            break;
          }
          else {
            if (rulebook.contiguous(notInLinePlacements)) {
              notInLinePlacements.removeLast();
            }
            else {
              return new PlacementCommand(notInLinePlacements);
            }
          }
        }
      }
    }
    return this.fallbackStrategy.compute(apk);
  }

}
