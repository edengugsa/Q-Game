import java.util.List;

/**
 * Represents a Player that raises an exception every time setup() is invoked.
 */
public class SetupFail_AI extends AIPlayerImpl {

  public SetupFail_AI(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void setup(QGameBoardState board, List<Tile> hand) {
    throw new IllegalStateException("whoops!");
  }

}
