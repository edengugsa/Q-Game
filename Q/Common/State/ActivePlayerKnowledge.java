package Common.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import Common.GameBoard.GameBoard;
import Common.PublicKnowledge;
import Common.Tiles.Tile;

/**
 * Represents information known by a Player about a Q game's State.
 */
public class ActivePlayerKnowledge {
  private final Queue<PublicPlayerState> opponentStates;
  private final GameBoard board;
  private final int refTilesRemaining;
  private final List<Tile> activePlayerTiles;

  public ActivePlayerKnowledge(Queue<PublicPlayerState> opponentStates,
                               GameBoard board,
                               int refTilesRemaining, List<Tile> tiles)  {
    this.opponentStates = opponentStates;
    this.board = board;
    this.refTilesRemaining = refTilesRemaining;
    this.activePlayerTiles = tiles;
  }

  public ActivePlayerKnowledge(PublicKnowledge pk, List<Tile> activePlayerTiles) {
    this.opponentStates = pk.opponentStates;
    this.board = pk.board;
    this.refTilesRemaining = pk.refTilesRemaining;
    this.activePlayerTiles = activePlayerTiles;
  }

  public GameBoard getBoard() { return new GameBoard(this.board.getMap()); }
  public List<Tile> getActivePlayerTiles() { return new ArrayList<>(this.activePlayerTiles); }
  public int getNumRefTilesRemaining() { return this.refTilesRemaining; }
  public int getNumPlayerTiles() { return this.activePlayerTiles.size(); }



}
