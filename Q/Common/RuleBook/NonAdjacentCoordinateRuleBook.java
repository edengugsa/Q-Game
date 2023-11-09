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

public class NonAdjacentCoordinateRuleBook extends RuleBook {

  /**
   * Gets non-adjacent Placements for the given tile on the given board
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
        }
      }
    }
    return new ArrayList<>(res);
  }

  /**
   * @return true if the given Placements is allowed on the given GameState
   * 1. Does the player own the tiles?
   * 2. are the placements in the same row or col?
   * 3. do the placements match its neighbors?
   * 4. are the placements adjacent to placed tiles?
   */
  @Override
  public boolean allows(PlacementCommand cmd, GameBoard board, List<Tile> playerTiles) {
    GameBoard mock = new GameBoard(board.getMap());
    if (!contiguous(cmd.getPlacements()) ||
            !this.isTileInHand(playerTiles, cmd.getPlacements())) {
      return false;
    }
    for (Placement p : cmd.getPlacements()) {
      if (!mock.isEmptyAndAdjacent(p)) {
        return true;
      }
      try {mock.extend(p.coordinate(), p.tile());}
      catch (Exception e) {return false;}
    }
    return false;
  }

}
