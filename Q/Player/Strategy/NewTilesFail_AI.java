import java.util.List;

/**
 * Represents a faulty AIPlayer that throws an error when the newTiles() method is called
 */
public class NewTilesFail_AI extends AIPlayerImpl {

  public NewTilesFail_AI(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public void newTiles(List<Tile> tiles) {
    throw new IllegalStateException("whoops!");
  }
}
