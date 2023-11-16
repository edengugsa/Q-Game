package Player.Strategy;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import Common.GameBoard.GameBoard;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.RuleBook.RuleBook;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

/**
 *  Represents a Strategy that produces a PlacementCommand with a tile that does not match its
 *  adjacent tiles on the ActivePlayerKnowledge's GameBoard.
 */
public class NoFitStrategy extends AbstractCheatStrategy {
  public NoFitStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
  }

  /**
   * Computes a Placement Command of a Placement with a Tile does not match its adjacent neighbors
   * on the apk's GameBoard.
   *
   * If such a Placement is not possible, it will revert to its
   * fallback strategy.
   */
  @Override
  public QGameCommand computeHelper(ActivePlayerKnowledge apk) {
    List<Tile> playerTiles = apk.getActivePlayerTiles();
    GameBoard gameboard = apk.getBoard();

    for(Tile t : playerTiles) {
      for(Placement p : gameboard.placementAdjacentOptions(t)) {
        if(!new RuleBook().matchesNeighbors(gameboard, p)) {
          Queue<Placement> onePlacement = new ArrayDeque<>();
          onePlacement.add(p);
          return new PlacementCommand(onePlacement);
        }
      }
    }
    return this.fallbackStrategy.compute(apk);
  }
}
