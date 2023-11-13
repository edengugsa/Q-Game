package Player.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.QColor;
import Common.Tiles.QShape;
import Common.Tiles.Tile;

public class TileNotOwnedStrategy extends AbstractCheatStrategy {
  Strategy fallbackStrategy;

  public TileNotOwnedStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
    this.fallbackStrategy = fallbackStrategy;
  }

  @Override
  public boolean canCheat() {
    return true;
  }

  @Override
  public QGameCommand compute(ActivePlayerKnowledge apk) {
    if (canCheat()) {
      return this.computeHelper(apk);
    }
    else {
      return this.fallbackStrategy.compute(apk);
    }
  }

  private QGameCommand computeHelper(ActivePlayerKnowledge apk) {
    ActivePlayerKnowledge newAPK = new ActivePlayerKnowledge(apk.getOpponentStates(),
            apk.getBoard(), apk.getNumRefTilesRemaining(), getPlayerTiles(apk.getActivePlayerTiles()));

    return this.fallbackStrategy.computeOnePlacement(newAPK);
  }


  public List<Tile> getPlayerTiles(List<Tile> playerTiles) {
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
