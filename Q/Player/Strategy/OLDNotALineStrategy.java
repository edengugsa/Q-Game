package Player.Strategy;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

public class OLDNotALineStrategy implements Strategy{
  @Override
  public QGameCommand compute(ActivePlayerKnowledge apk) {
    List<Tile> tiles = apk.getActivePlayerTiles();
    Collections.sort(apk.getActivePlayerTiles(), Tile.TileComparator);

    Queue<Placement> res = new ArrayDeque<>();
    for (Tile t : tiles) {
      List<Placement> options = apk.getBoard().placementAdjacentOptions(t);
      if (!options.isEmpty()) {
        res.add(options.get(0));
      }
    }



    return null;
  }
}
