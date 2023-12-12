package Referee;

import Common.Scorer.ScorerConfig;
import Common.State.GameState;

/**
 * Represents a configuration to initialize and run a Referee.
 */
public class RefereeConfig {
  private final GameState gameState;
  private final boolean quiet;
  private final ScorerConfig scorerConfig; // configures # of pts awarded per move
  private final int perTurn;
  private final boolean observe;

  public RefereeConfig(GameState gameState, boolean quiet, ScorerConfig scorerConfig, int perTurn, boolean observe){
    this.gameState = gameState;
    this.quiet = quiet;
    this.scorerConfig = scorerConfig;
    this.perTurn = perTurn;
    this.observe = observe;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  public boolean isQuiet() {
    return this.quiet;
  }

  public ScorerConfig getRefereeStateConfig() {
    return this.scorerConfig;
  }

  public int getPerTurn() {
    return this.perTurn;
  }

  public boolean isObserve() {
    return this.observe;
  }
}
