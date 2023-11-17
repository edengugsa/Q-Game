package Common.GameCommands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayDeque;
import java.util.Queue;

import Common.RuleBook.QRuleBook;
import Common.Scorer.Scorer;
import Common.State.GameState;
import Common.Tiles.Placement;

/**
 * A QGameCommand for validating and executing the placement of tiles on the board.
 */
public class PlacementCommand implements QGameCommand {

  final Queue<Placement> placements;

  public Queue<Placement> getPlacements() {
    return new ArrayDeque<>(this.placements);
  }

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
    return ruleBook.allows(this, game.getGameBoard(), game.currentPlayerTiles());
  }

  @Override
  public void execute(GameState game) {
      game.place(this.placements);
  }

  @Override
  public void score(GameState game, Scorer scorer) {
    game.updateScore(scorer.scorePlacement(this.placements, game.getGameBoard(), game.currentPlayerTiles().isEmpty()));
  }

  @Override
  public void renewPlayerTiles(GameState game) {
    game.renewPlayerTiles(this.placements.size());
  }

  @Override
  public JsonElement toJSON() {
    JsonArray jPlacements = new JsonArray();
    for (Placement p : this.placements) {
      jPlacements.add(p.toJson());
    }
    return jPlacements;
//    JsonObject obj = new JsonObject();
//    obj.add("coordinate", first().coordinate().toJSON());
//    obj.add("1tile", first().tile().toJSON());
//    return obj;
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

  public boolean doesPlayerGetNewTiles() {
    return true;
  }
}

