package Common.RuleBook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Common.GameBoard.GameBoard;
import Common.GameCommands.PlacementCommand;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

/**
 * Represents a RuleBook that allows computing a Placement of a Tile that is not adjacent to a
 * placed Tile on the board.
 */
public class NonAdjacentCoordinateRuleBook extends RuleBook {

  /**
   * Returns the first Tile.
   */
  @Override
  public List<Tile> getPlayerTiles(List<Tile> playerTiles) {
    List<Tile> res = new ArrayList<>();
    res.add(playerTiles.get(0));

    return res;
  }

  /**
   * Returns a List of one Placement that is empty but not adjacent to a placed tile.
   */
  @Override
  public List<Placement> getPlacementOptions(Tile t, GameBoard board) {
    Set<Placement> res = new HashSet<>();
    List<Placement> plmt = board.placementAdjacentOptions(t);
    for (Placement p : plmt) {
      for(Coordinate neighbor: p.coordinate().getNeighbors()) {
        if (!board.containsCoord(neighbor)
                && !board.containsCoord(p.coordinate())
                && board.getNumFreeNeighbors(neighbor) == 4) {
          res.add(new Placement(neighbor, t));
          return new ArrayList<>(res);
        }
      }
    }
    return new ArrayList<>(res);
  }

  /**
   * @return true if the given Placement is not adjacent to any tile on the board.
   */
  @Override
  public boolean allows(PlacementCommand cmd, GameBoard board, List<Tile> playerTiles) {
    GameBoard mock = new GameBoard(board.getMap());
    return !mock.isEmptyAndAdjacent(cmd.getPlacements().remove()) &&
            this.isTileInHand(playerTiles, cmd.getPlacements());
  }

}
