import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Represents information known by a Player about a Q game's GameState.
 */
public class ActivePlayerKnowledge {
  private final Queue<PublicPlayerState> opponentStates;
  private final QGameBoardState board;
  private final int refTilesRemaining;
  private final List<Tile> activePlayerTiles;

  public ActivePlayerKnowledge(Queue<PublicPlayerState> opponentStates,
                               QGameBoardState board,
                               int refTilesRemaining, List<Tile> tiles)  {
    this.opponentStates = opponentStates;
    this.board = board;
    this.refTilesRemaining = refTilesRemaining;
    this.activePlayerTiles = tiles;
  }

  public QGameBoardState getBoard() { return new GameBoard(this.board.getMap()); }
  public List<Tile> getActivePlayerTiles() { return new ArrayList<>(this.activePlayerTiles); }
  public int getNumRefTilesRemaining() { return this.refTilesRemaining; }
  public int getNumPlayerTiles() { return this.activePlayerTiles.size(); }

  public ActivePlayerKnowledge(PublicKnowledge pk, List<Tile> activePlayerTiles) {
    this.opponentStates = pk.opponentStates;
    this.board = pk.board;
    this.refTilesRemaining = pk.refTilesRemaining;
    this.activePlayerTiles = activePlayerTiles;
  }

}
