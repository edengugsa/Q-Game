package Common.RuleBook;

import Common.GameCommands.ExchangeCommand;

public class BadAskForTilesRuleBook extends RuleBook {

  @Override
  public boolean allows(ExchangeCommand cmd, int numRefTiles, int numPlayerTiles) {
    return !super.allows(cmd, numRefTiles, numPlayerTiles);
  }
}
