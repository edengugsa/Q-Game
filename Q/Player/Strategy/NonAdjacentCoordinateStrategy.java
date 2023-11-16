package Player.Strategy;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import Common.GameBoard.GameBoard;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

/**
 *  Represents a Strategy that produces a PlacementCommand with a tile that is not adjacent to
 *  a placed tile in the ActivePlayerKnowledge's GameBoard.
 */
public class NonAdjacentCoordinateStrategy extends AbstractCheatStrategy {

  public NonAdjacentCoordinateStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
  }


  /**
   * @return A PlacementCommand of an ActivePlayerKnowledge's Tile at a Coordinate
   * that is not adjacent to a placed Tile on the GameBoard.
   */
  public QGameCommand compute(ActivePlayerKnowledge knowledge) {
    Tile t = knowledge.getActivePlayerTiles().get(0);
    Queue<Placement> onePlacement = new ArrayDeque<>();
    onePlacement.add(new Placement(getNonAdjacentCoordinate(t, knowledge.getBoard()), t));
    return new PlacementCommand(onePlacement);
  }

  /**
   * @return A Coordinate for the given Tile that is not adjacent to a Tile on the board.
   */
  private Coordinate getNonAdjacentCoordinate(Tile t, GameBoard board) {
    List<Placement> placementAdjacentOptions =  board.placementAdjacentOptions(t);

    for (Placement p : placementAdjacentOptions) {
      for(Coordinate neighbor: p.coordinate().getNeighbors()) {
        if (!board.containsCoord(neighbor)
                && board.getNumFreeNeighbors(neighbor) == 4) {
          return neighbor;
        }
      }
    }
    //shouldn't reach here
    throw new IllegalArgumentException("Cannot find a non-adjacent coordinate");
  }
}
