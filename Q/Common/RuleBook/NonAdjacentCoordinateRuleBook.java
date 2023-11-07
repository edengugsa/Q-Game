package Common.RuleBook;

import Common.GameBoard.GameBoard;
import Common.GameCommands.PlacementCommand;

public class NonAdjacentCoordinateRuleBook extends RuleBook {

//  @Override
//  public boolean allows(PlacementCommand cmd, GameBoard board) {
//    this.mock = new GameBoard(board.getMap());
//    if (!contiguous(cmd.getPlacements())) {return false;}
//    for (Placement p : cmd.getPlacements()) {
//      if (!this.matchesNeighbors(p)) {return false;}
//      try {this.mock.extend(p.coordinate(), p.tile());}
//      catch (Exception e){return false;}
//    }
//    return true;
//  }
}
