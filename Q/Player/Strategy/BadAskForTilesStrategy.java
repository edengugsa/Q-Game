package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;

/**
 *  Represents a Strategy that requests an ExchangeCommand when the ActivePlayer's knowledge's
 *  hand has more Tiles than the referee owns.
 */
public class BadAskForTilesStrategy extends AbstractCheatStrategy {

  BadAskForTilesStrategy(Strategy fallbackStrategy) {
    super(fallbackStrategy);
  }

  /**
   * Does the Player have more tiles than the Referee?
   */
  @Override
  public boolean canCheat(ActivePlayerKnowledge apk) {
    return apk.getNumRefTilesRemaining() < apk.getNumPlayerTiles();
  }

  @Override
  public QGameCommand computeHelper(ActivePlayerKnowledge knowledge) {
    return null;
  }
}
