package Common.RuleBook;

import Common.GameCommands.ExchangeCommand;
import Common.GameCommands.PlacementCommand;
import Common.State.ActivePlayerKnowledge;


/**
 * Represents a RuleBook that will allow exchanges even where there aren't enough referee
 * tiles remaining.
 *
 */
public class BadAskForTilesRuleBook extends RuleBook {

  @Override
  public boolean allows(ExchangeCommand cmd, int numRefTiles, int numPlayerTiles) {
    return !super.allows(cmd, numRefTiles, numPlayerTiles);
  }

  @Override
  public boolean allows(PlacementCommand cmd, ActivePlayerKnowledge apk) {
    if (this.allows(new ExchangeCommand(), apk.getNumRefTilesRemaining(), apk.getNumPlayerTiles())) {
      return false;
    }
    else{
      return super.allows(cmd, apk);
    }
  }

}
