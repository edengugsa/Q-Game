import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A flawed strategy (for testing purposes). Returns pseudorandom placements that the player may or may not
 * be able to make.
 */
public class FlawedStrategy implements Strategy {

  @Override
  public QGameCommand compute(ActivePlayerKnowledge knowledge) {
    Queue<Placement> illegal = new ArrayDeque<>();
    illegal.add(new Placement(new Coordinate(1000000,10), new Tile(QColor.GREEN, QShape.circle)));
    illegal.add(new Placement(new Coordinate(10,10), new Tile(QColor.GREEN, QShape.circle)));
    return new PlacementCommand(illegal);
  }
}