package Player;

import java.util.List;
import Common.Tiles.Tile;
import Player.Strategy.Strategy;

public class playerNewTilesInf extends abstractInfPlayer {

  public playerNewTilesInf(String name, Strategy strategy, int when) {
    super(name, strategy, when);
  }
  
  @Override
  public void newTiles(List<Tile> hand) {
//    System.out.println("when: " + when + " new Tiles called on " + this.name());
    if (this.when > 1) {
      super.newTiles(hand);
      this.when--;
    }
    else {
      super.infLoop();
    }
  }
}
