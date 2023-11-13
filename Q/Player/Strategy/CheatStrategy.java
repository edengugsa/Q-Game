package Player.Strategy;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;

public interface CheatStrategy extends Strategy {
  boolean canCheat();

  QGameCommand computeHelper(ActivePlayerKnowledge knowledge);
}
