package Common.Scorer;
import org.junit.Test;

import java.util.*;

import Common.GameBoard.GameBoard;
import Common.Scorer.Scorer;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

import static org.junit.Assert.assertEquals;


public class testScorer {

  @Test
  public void testScoreKeeperPtsForQEight() {
    Map<Coordinate, Tile> map = new HashMap<>();
    map.put(new Coordinate(0,0), new Tile(QColor.GREEN, QShape.diamond));
    map.put(new Coordinate(1,0), new Tile(QColor.ORANGE, QShape.diamond));
    map.put(new Coordinate(2,0), new Tile(QColor.RED, QShape.diamond));
    map.put(new Coordinate(3,0), new Tile(QColor.YELLOW, QShape.diamond));
    map.put(new Coordinate(4,0), new Tile(QColor.PURPLE, QShape.diamond));

    Queue<Placement> placements = new ArrayDeque<>();
    placements.add(new Placement(new Coordinate(5,0), new Tile(QColor.BLUE, QShape.diamond)));
    placements.add(new Placement(new Coordinate(6,0), new Tile(QColor.GREEN, QShape.diamond)));

    Scorer scorer = new Scorer();
    scorer.setQ_PTS(8);
    scorer.scorePlacement(placements, new GameBoard(map), false);
    assertEquals(8, scorer.ptsForQ());
  }

  @Test
  public void testScoreKeeperPtsForQNineMillion() {
    Map<Coordinate, Tile> map = new HashMap<>();
    map.put(new Coordinate(0,0), new Tile(QColor.GREEN, QShape.diamond));
    map.put(new Coordinate(1,0), new Tile(QColor.ORANGE, QShape.diamond));
    map.put(new Coordinate(2,0), new Tile(QColor.RED, QShape.diamond));
    map.put(new Coordinate(3,0), new Tile(QColor.YELLOW, QShape.diamond));
    map.put(new Coordinate(4,0), new Tile(QColor.PURPLE, QShape.diamond));

    Queue<Placement> placements = new ArrayDeque<>();
    placements.add(new Placement(new Coordinate(5,0), new Tile(QColor.BLUE, QShape.diamond)));
    placements.add(new Placement(new Coordinate(6,0), new Tile(QColor.GREEN, QShape.diamond)));

    Scorer scorer = new Scorer();
    scorer.setPTS_FOR_PLACING_ALL(99910924);
    scorer.scorePlacement(placements, new GameBoard(map), true);
    assertEquals(99910924, scorer.ptsForPlacingAll());
  }

  @Test
  public void testScoreKeeperPtsForContigTwo() {
    Map<Coordinate, Tile> map = new HashMap<>();
    map.put(new Coordinate(0,0), new Tile(QColor.GREEN, QShape.diamond));
    map.put(new Coordinate(1,0), new Tile(QColor.ORANGE, QShape.diamond));
    map.put(new Coordinate(2,0), new Tile(QColor.RED, QShape.diamond));
    map.put(new Coordinate(3,0), new Tile(QColor.YELLOW, QShape.diamond));
    map.put(new Coordinate(4,0), new Tile(QColor.PURPLE, QShape.diamond));

    Queue<Placement> placements = new ArrayDeque<>();
    placements.add(new Placement(new Coordinate(5,0), new Tile(QColor.GREEN, QShape.diamond)));
    placements.add(new Placement(new Coordinate(6,0), new Tile(QColor.GREEN, QShape.diamond)));

    Scorer scorer = new Scorer();
    scorer.setPTS_PER_CONTIG_TILE(2);
    scorer.scorePlacement(placements, new GameBoard(map), false);
    assertEquals(0, scorer.ptsForContiguity());
  }

  @Test
  public void testScoreKeeperPtsForContig() {
    Map<Coordinate, Tile> map = new HashMap<>();
    map.put(new Coordinate(0,0), new Tile(QColor.GREEN, QShape.diamond));
    map.put(new Coordinate(1,0), new Tile(QColor.ORANGE, QShape.diamond));
    map.put(new Coordinate(2,0), new Tile(QColor.RED, QShape.diamond));
    map.put(new Coordinate(3,0), new Tile(QColor.YELLOW, QShape.diamond));
    map.put(new Coordinate(4,0), new Tile(QColor.PURPLE, QShape.diamond));

    Queue<Placement> placements = new ArrayDeque<>();
    placements.add(new Placement(new Coordinate(5,0), new Tile(QColor.GREEN, QShape.diamond)));
    placements.add(new Placement(new Coordinate(6,0), new Tile(QColor.GREEN, QShape.diamond)));

    Scorer scorer = new Scorer();
    scorer.scorePlacement(placements, new GameBoard(map), false);
    assertEquals(7, scorer.ptsForContiguity());
  }

  @Test
  public void testScoreKeeperPtsForQ() {
    Map<Coordinate, Tile> map = new HashMap<>();
    map.put(new Coordinate(0,0), new Tile(QColor.GREEN, QShape.diamond));
    map.put(new Coordinate(1,0), new Tile(QColor.ORANGE, QShape.diamond));
    map.put(new Coordinate(2,0), new Tile(QColor.RED, QShape.diamond));
    map.put(new Coordinate(3,0), new Tile(QColor.YELLOW, QShape.diamond));
    map.put(new Coordinate(4,0), new Tile(QColor.PURPLE, QShape.diamond));
    map.put(new Coordinate(5,0), new Tile(QColor.BLUE, QShape.diamond));
    map.put(new Coordinate(6,0), new Tile(QColor.GREEN, QShape.diamond));

    Queue<Placement> placements = new ArrayDeque<>();
    placements.add(new Placement(new Coordinate(5,0), new Tile(QColor.BLUE, QShape.diamond)));
    placements.add(new Placement(new Coordinate(6,0), new Tile(QColor.GREEN, QShape.diamond)));

    Scorer scorer = new Scorer();
    scorer.scorePlacement(placements, new GameBoard(map), false);
    assertEquals(6, scorer.ptsForQ());
  }
}





