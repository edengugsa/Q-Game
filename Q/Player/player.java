import java.util.List;


public class AIPlayerImpl implements AIPlayer {

  protected final String name;
  protected List<Tile> hand;
  protected QGameBoardState board;
  private final Strategy strategy;

  public AIPlayerImpl(String name, Strategy strategy) {
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
  public void setup(QGameBoardState board, List<Tile> hand) {
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
