import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


/**
 * Represents the Exchange Action where a Player requests to exchanges all of their tiles for new ones.
 * This is possibly only if there are enough referee tiles remaining. This class has functionality
 * to check if this Command can be executed on a given GameState and to execute it on a given GameState.
 */
public class ExchangeCommand implements QGameCommand {

  @Override
  public boolean isLegal(QRuleBook rules, GameState game) {
    return rules.allows(this, game.tilesRemaining(), game.currentPlayerTiles().size());
  }

  /**
   * Exchanges all the given GameState's active player's tiles for new ones.
   * Appends the player's old tiles to the end of GameState's deck.
   */
  @Override
  public void execute(GameState game) {
      for (Tile t : game.currentPlayerTiles()) {
        game.takeTileFromActivePlayer(t);
        game.deal(1);
      }
  }

  @Override
  public void score (GameState game, Scorer scorer) {}

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ExchangeCommand other)) {
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
    return JsonParser.parseString("exchange");
  }
}
