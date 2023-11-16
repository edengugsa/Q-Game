package Common.RuleBook;

import java.util.List;
import java.util.Queue;

import Common.GameBoard.GameBoard;
import Common.GameCommands.PlacementCommand;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

public class NotALineRuleBook extends RuleBook {
  @Override
  public boolean contiguous(Queue<Placement> placements) {
    if (placements.size() == 1) {
      return false;
    }
    else {
      return super.contiguous(placements);
    }
  }
  @Override
  public boolean allows(PlacementCommand cmd, GameBoard board, List<Tile> playerTiles) {
    this.mock = new GameBoard(board.getMap());
    if (contiguous(cmd.getPlacements())) {
      return false;
    }
    for (Placement p : cmd.getPlacements()) {
      if (!this.matchesNeighbors(p) || !mock.isEmptyAndAdjacent(p)) {
        return false;
      }
      try {this.mock.extend(p.coordinate(), p.tile());}
      catch (Exception e) {return false;}
    }
    return this.isTileInHand(playerTiles, cmd.getPlacements());
  }

  @Override
  public int maxPlacementSize() {
    return 2;
  }
}
