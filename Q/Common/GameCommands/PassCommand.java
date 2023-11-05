package Common.GameCommands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import Common.RuleBook.QRuleBook;
import Common.Scorer.Scorer;
import Common.State.GameState;

/**
 * Represents the Pass Action where the active PlayerState chooses to do nothing.
 */
public class PassCommand implements QGameCommand {

  @Override
  public boolean isLegal(QRuleBook rules, GameState game) {
    return rules.allows(this);
  }

  @Override
  public void execute(GameState game) throws IllegalArgumentException {
  }

  @Override
  public void score (GameState game, Scorer scorer) {}

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PassCommand other)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 1;
  }

  @Override
  public JsonElement toJSON() {
    return JsonParser.parseString("pass");
  }

}
