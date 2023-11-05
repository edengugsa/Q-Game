import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A QGameCommand for validating and executing the placement of tiles on the board.
 */
public class PlacementCommand implements QGameCommand {

  final Queue<Placement> placements;

  /**
   * Constructs this action with a list of placements.
   */
  public PlacementCommand(Queue<Placement> placements) {
    if (placements == null || placements.size() == 0) {
      throw new IllegalArgumentException("Please ensure there is at least one (1) placement.");
    }
    this.placements = new ArrayDeque<>(placements);
  }

  @Override
  public boolean isLegal(QRuleBook ruleBook, GameState game) {
    for (Placement p : this.placements) {
      if (!game.currentPlayerTiles().contains(p.tile())) {
        return false;
      }
    }
    return ruleBook.allows(this, game.getGameBoard());
  }

  @Override
  public void execute(GameState game) {
      game.place(this.placements);
      game.renewPlayerTiles(this.placements.size());
  }

  @Override
  public void score(GameState game, Scorer scorer) {
    game.updateScore(scorer.scorePlacement(this.placements, game.getGameBoard(), game.playerPlacedAllTiles));
  }

  @Override
  public JsonElement toJSON() {
    JsonObject obj = new JsonObject();
    obj.add("coordinate", first().coordinate().toJSON());
    obj.add("1tile", first().tile().toJSON());
    return obj;
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    for (Placement p : this.placements) {
      out.append(p.toString()).append("\n");
    }
    return out.toString();
  }

  private Placement first() {
    return this.placements.peek();
  }
}
