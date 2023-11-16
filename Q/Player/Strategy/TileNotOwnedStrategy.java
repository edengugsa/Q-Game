package Player.Strategy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import Common.GameBoard.GameBoard;
import Common.GameCommands.PlacementCommand;
import Common.GameCommands.QGameCommand;
import Common.RuleBook.RuleBook;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Placement;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

/**
 * Represents a Strategy that produces a PlacementCommand with a tile that the
 * ActivePlayerKnowledge does not own.
 */
public class TileNotOwnedStrategy extends AbstractCheatStrategy {

  public TileNotOwnedStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
  }

  /**
   * Computes a Placement Command that contains one Tile that the Player does not own at a valid
   * coordinate on the GameBoard.
   * If the Tiles the Player does not own cannot be placed on the board, it will revert to its
   * fallback strategy.
   */
  public QGameCommand computeHelper(ActivePlayerKnowledge apk) {
    List<Tile> tilesPlayerDoesNotOwn = this.getTilesPlayerDoesNotOwn(apk.getActivePlayerTiles());
    GameBoard gameboard = apk.getBoard();

    for (Tile t : tilesPlayerDoesNotOwn) {
      for(Placement p : gameboard.placementAdjacentOptions(t)) {
        if (new RuleBook().matchesNeighbors(gameboard, p)) {
          Queue<Placement> onePlacement = new ArrayDeque<>();
          onePlacement.add(p);
          return new PlacementCommand(onePlacement);
        }
      }
    }
    return this.fallbackStrategy.compute(apk);
  }


  private List<Tile> getTilesPlayerDoesNotOwn(List<Tile> playerTiles) {
    List<Tile> res = new ArrayList<>();

    for (Tile t: allTiles()) {
      if (!playerTiles.contains(t)) {
        res.add(t);
      }
    }
    return res;
  }


  /**
   *  Returns a set of all possible Tiles.
   */
  private static Set<Tile> allTiles() {
    List<QColor> colors = new ArrayList<>(Arrays.asList
            (QColor.RED, QColor.GREEN, QColor.BLUE, QColor.YELLOW, QColor.ORANGE, QColor.PURPLE));
    List<QShape> shapes = new ArrayList<>(Arrays.asList
            (QShape.star, QShape.eight_star, QShape.square, QShape.circle, QShape.clover, QShape.diamond));
    Set<Tile> tiles = new HashSet<>();

    for (QShape s : shapes) {
      for (QColor c : colors) {
        tiles.add(new Tile(c, s));
      }
    }

    return tiles;
  }
}
