import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a set of rules for the Q Game.
 *
 * It checks if a proposed sequence of Placements is allowed, if a Player is allowed to Exchange,
 * and determines the winners of a game.
 *
 * </p>
 * The requested placements must all be in the same row or column.
 * Each placement must match its neighbor above/below and to the left/right
 * by the same quality (shape or color).
 */
public class RuleBook implements QRuleBook {

  private GameBoard mock;

  /**
   * @return true if the given placements on the given map follow the
   * rules of this RuleBook. See class header for placement rules.
   */
  @Override
  public boolean allows(PlacementCommand cmd, QGameBoardState board) {
    this.mock = new GameBoard(board.getMap());
    if (!contiguous(cmd.placements)) {return false;}
    for (Placement p : cmd.placements) {
      if (!this.matchesNeighbors(p)) {return false;}
      try {this.mock.extend(p.coordinate(), p.tile());}
      catch (Exception e){return false;}
    }
    return true;
  }

  @Override
  public boolean allows(ExchangeCommand cmd, int numRefTiles, int numPlayerTiles) {
    return numPlayerTiles <= numRefTiles;
  }

  @Override
  public boolean allows(PassCommand cmd) {
    return true;
  }

  @Override
  public boolean gameOver(GameState game) {
    return game.numPlayers() == 0 || this.roundWithoutPlacements(game) || game.playerPlacedAllTiles;
  }

  @Override
  public List<String> determineWinners(List<PlayerState> players) {
    List<String> winners = new ArrayList<>();
    int max = -1;
    for (PlayerState p : players) {
      if (p.score() > max) {
        max = p.score();
        winners = new ArrayList<>();
        winners.add(p.name());
      }
      else if (p.score() == max) {
        winners.add(p.name());
      }
    }
    return winners;
  }

  @Override
  public int tilesPerPlayer() {
    return 6;
  }

  /**
   * @return true if all remaining players passed or replaced their tiles this round
   */
  private boolean roundWithoutPlacements(GameState game) {
    if (this.roundComplete(game) && game.roundHistory.size() > 0) {
      for (QGameCommand cmd : game.roundHistory) {
        if (cmd instanceof PlacementCommand)  {
          game.roundHistory.clear();
          return false;
        }
      }
      game.roundHistory.clear();
      return true;
    }
    return false; // if not at the end of a round
  }

  @Override
  public boolean roundComplete(GameState game) {
    int numRounds = game.currentPlayer().turnsCompleted();
    if (numRounds == 0) {
      return false;
    }
    for (PlayerState p : game.players()) {
      if (p.turnsCompleted() != numRounds) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines if a given placement on the map satisfies the matching rules
   * of the Q-Game.
   */
  private boolean matchesNeighbors(Placement p) {
    Coordinate[] neighbors = p.coordinate().getNeighbors(); //
    // [0] = @below, [1] = @above, [2] = @left, [3] = @right
    return matchesShapeOrColor(p.tile(), neighbors[0], neighbors[1])
            && matchesShapeOrColor(p.tile(), neighbors[2], neighbors[3]);
  }

  /**
   * @return true if each coordinate in the set of placements is contiguous.
   * A set of coordinates are contiguous if they are all in either the same row
   * or the same column.
   */
  private boolean contiguous(Queue<Placement> placements) {
    List<Coordinate> coords = new ArrayList<>();
    for (Placement p : placements) {
      coords.add(p.coordinate());
    }
    int col = coords.get(0).col();
    int row = coords.get(0).row();
    boolean sameRow = true;
    boolean sameCol = true;
    for (int i = 1; i < coords.size(); i++) {
      if (coords.get(i).col() != col) {
        sameCol = false;
      }
      if (coords.get(i).row() != row) {
        sameRow = false;
      }
    }
    return sameRow || sameCol;
  }

  /**
   * Does the given Tile have the same color as the tiles at c1 and c2 or the same shape?
   * At least c1 and c2 must have a tile.
   */
  private boolean matchesShapeOrColor(Tile tile, Coordinate c1, Coordinate c2) {
    boolean matchesTile1Shape = true, matchesTile1Color = true;
    boolean matchesTile2Shape = true, matchesTile2Color = true;
    if (this.mock.containsCoord(c1)) {
      Tile tile1 = this.mock.getTileAt(c1);
      matchesTile1Shape = tile.shape().equals(tile1.shape());
      matchesTile1Color = tile.color().equals(tile1.color());
    }
    if (this.mock.containsCoord(c2)) {
      Tile tile2 = this.mock.getTileAt(c2);
      matchesTile2Shape = tile.shape().equals(tile2.shape());
      matchesTile2Color = tile.color().equals(tile2.color());
    }
    return (matchesTile1Color && matchesTile2Color) || (matchesTile1Shape && matchesTile2Shape);
  }
}
