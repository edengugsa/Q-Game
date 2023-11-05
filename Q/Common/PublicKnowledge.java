package Common;

import java.util.Queue;

import Common.GameBoard.GameBoard;
import Common.State.PublicPlayerState;

/**
 * Represents information about all players
 * that a referee may wish to share with the active player.
 */
public class PublicKnowledge {
  public final Queue<PublicPlayerState> opponentStates;
  public final GameBoard board;
  public final int refTilesRemaining;

  public PublicKnowledge(Queue<PublicPlayerState> opponentStates,
                         GameBoard board, int refTilesRemaining) {
    this.opponentStates = opponentStates;
    this.board = board;
    this.refTilesRemaining = refTilesRemaining;
  }

}
