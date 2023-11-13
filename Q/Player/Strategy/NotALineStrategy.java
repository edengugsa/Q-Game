package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;

public class NotALineStrategy extends AbstractCheatStrategy {

  Strategy fallbackStrategy;

  public NotALineStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
  }

  @Override
  public boolean canCheat() {
    return false;
  }


  @Override
  public QGameCommand computeHelper(ActivePlayerKnowledge knowledge) {

  }

}
