import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Referee's knowledge of a Player in the Q game.
 * </p>
 * This is merely an internal representation of a PlayerState. It is not capable of
 * procuring GameActions.
 */
public class PlayerStateImpl implements PlayerState {

  private String name;
  private final List<Tile> hand;
  private int score;
  private int turnsCompleted;

  /**
   * Creates a new Player State from scratch.
   */
  public PlayerStateImpl(String name) {
    this.name = name;
    this.hand = new ArrayList<>();
    this.score = 0;
    this.turnsCompleted = 0;
  }

  /**
   * Creates a new PlayerState from an existing state. The name gets added
   * later.
   */
  public PlayerStateImpl(List<Tile> tiles, int score) {
    this.hand = new ArrayList<>(tiles);
    this.score = score;
  }

  /**
   * Creates a new PlayerState from an existing state.
   */
  public PlayerStateImpl(String name, List<Tile> tiles, int score) {
    this(tiles, score);
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public int score() {
    return this.score;
  }

  @Override
  public int tilesRemaining() {
    return this.hand.size();
  }

  @Override
  public List<Tile> getHand() {
    return new ArrayList<>(this.hand);
  }

  @Override
  public int turnsCompleted() {
    return this.turnsCompleted;
  }

  @Override
  public void incrementTurnsCompleted() {
    this.turnsCompleted++;
  }

  @Override
  public void removeTile(Tile t) throws IllegalArgumentException {
    if (!this.hand.remove(t)) {
      throw new IllegalArgumentException("Player does not possess the given tile.");
    }
  }

  @Override
  public void newTiles(List<Tile> newTiles) {
    this.hand.addAll(newTiles);
  }

  @Override
  public void reward(int points) {
    this.score += points;
  }

}
