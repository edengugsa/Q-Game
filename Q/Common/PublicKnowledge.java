import java.util.Queue;

/**
 * Represents information about all players
 * that a referee may wish to share with the active player.
 */
public class PublicKnowledge {
  public final Queue<PublicPlayerState> opponentStates;
  public final QGameBoardState board;
  public final int refTilesRemaining;

  public PublicKnowledge(Queue<PublicPlayerState> opponentStates,
                         QGameBoardState board, int refTilesRemaining) {
    this.opponentStates = opponentStates;
    this.board = board;
    this.refTilesRemaining = refTilesRemaining;
  }

}
