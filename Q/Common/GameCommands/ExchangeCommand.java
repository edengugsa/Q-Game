package Common.GameCommands;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import Common.RuleBook.QRuleBook;
import Common.Scorer.Scorer;
import Common.State.GameState;
import Common.Tiles.Tile;


/**
 * Represents the Exchange Action where a Player requests to exchanges all of their tiles for new ones.
 * This is possibly only if there are enough referee tiles remaining. This class has functionality
 * to check if this Command can be executed on a given State and to execute it on a given State.
 */
public class ExchangeCommand implements QGameCommand {

  @Override
  public boolean isLegal(QRuleBook rules, GameState game) {
    return rules.allows(this, game.tilesRemaining(), game.currentPlayerTiles().size());
  }

  /**
   * Exchanges all the given State's active player's tiles for new ones.
   * Appends the player's old tiles to the end of State's deck.
   */
  @Override
  public void execute(GameState game) {
      for (Tile t : game.currentPlayerTiles()) {
        game.takeTileFromActivePlayer(t);
        game.deal(1);
      }
  }

  @Override
  public void score(GameState game, Scorer scorer) {}

  @Override
  public void renewPlayerTiles(GameState game) {
    // nothing
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ExchangeCommand)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 2;
  }

  @Override
  public JsonElement toJSON() {
    return JsonParser.parseString("replace");
  }

  @Override
  public boolean doesPlayerGetNewTiles() {
    return true;
  }
}
