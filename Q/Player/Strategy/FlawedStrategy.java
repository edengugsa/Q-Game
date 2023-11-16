package Player.Strategy;
import java.util.ArrayDeque;
import java.util.Queue;

import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

/**
 * A flawed strategy (for testing purposes). Returns pseudorandom placements that the player may or may not
 * be able to make.
 */
public class FlawedStrategy implements Strategy {

  @Override
  public QGameCommand compute(ActivePlayerKnowledge apk) {
    Queue<Placement> illegal = new ArrayDeque<>();
    illegal.add(new Placement(new Coordinate(1000000,10), new Tile(QColor.GREEN, QShape.circle)));
    illegal.add(new Placement(new Coordinate(10,10), new Tile(QColor.GREEN, QShape.circle)));
    return new PlacementCommand(illegal);
  }
}
