/**
 * Represents a faulty AIPlayer that throws an error when the takeTurn() method is called
 */
public class TakeTurnFail_AI extends AIPlayerImpl {

  public TakeTurnFail_AI(String name, Strategy strategy) {
    super(name, strategy);
  }

  @Override
  public QGameCommand takeTurn(ActivePlayerKnowledge knowledge) {
    throw new IllegalStateException("whoops!");
  }
}
