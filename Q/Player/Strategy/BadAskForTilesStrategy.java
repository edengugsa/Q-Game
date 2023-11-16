package Player.Strategy;

import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;

/**
 *  Represents a Strategy that requests an ExchangeCommand when the ActivePlayer's knowledge's
 *  hand has more Tiles than the referee owns.
 */
public class BadAskForTilesStrategy extends AbstractCheatStrategy {

  public BadAskForTilesStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
  }

  @Override
  public QGameCommand compute(ActivePlayerKnowledge apk) {
    if (apk.getNumRefTilesRemaining() < apk.getNumPlayerTiles()) {
      return new ExchangeCommand();
    }
    else {
      return this.fallbackStrategy.compute(apk);
    }
  }
}
