package Player;
import java.util.List;

import Common.GameBoard.GameBoard;
import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;
import Player.Strategy.Strategy;

public class playerImpl implements player {

  protected final String name;
  protected List<Tile> hand;
  protected GameBoard board;
  private final Strategy strategy;

  public playerImpl(String name, Strategy strategy) {
    this.name = name;
    this.strategy = strategy;
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    this.hand = tiles;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public void setup(GameBoard board, List<Tile> hand) {
    this.board = board;
    this.hand = hand;
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge apk) {
    return strategy.compute(apk);
  }


  @Override
  public void win(boolean w) {
    if (w) {
//      System.out.println(name + ": Winnner");
    }
    else {
//      System.out.println(name + ": Loser");
    }
  }
}
