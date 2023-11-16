package Common.RuleBook;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

import static org.junit.Assert.assertFalse;

public class testRuleBook {
  @Test
  public void testContiguous() {
    Queue<Placement> placements = new ArrayDeque<>();
    placements.add(new Placement(new Coordinate(0,0), new Tile(QColor.GREEN, QShape.square)));
    placements.add(new Placement(new Coordinate(1,0), new Tile(QColor.RED, QShape.square)));
    placements.add(new Placement(new Coordinate(0,1), new Tile(QColor.BLUE, QShape.square)));

    assertFalse(new RuleBook().contiguous(placements));
  }
}
