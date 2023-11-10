package Common.RuleBook;

import java.util.ArrayList;
import java.util.List;

import Common.GameBoard.GameBoard;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

public class NoFitRuleBook extends RuleBook {

  public NoFitRuleBook() {
  }

  @Override
  public int maxPlacementSize() {
    return 1;
  }

  @Override
  protected boolean matchesNeighbors(Placement p) {
    return !super.matchesNeighbors(p);
  }

  @Override
  public List<Coordinate> getMatchingCoordinates(List<Placement> placements, GameBoard board) {
    List<Coordinate> res = new ArrayList<>();
    for (Placement p : placements) {
      res.add(p.coordinate());
    }
    return res;
  }

}
