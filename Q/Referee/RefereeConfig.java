package Referee;

import Common.State.GameState;

/**
 * A RefereeConfig is an object with 5 fields:,
 *
 *        { "state0"__ : JState,
 *
 *        { "quiet"___ : Boolean,
 *
 *        { "config-s" : RefereeStateConfig,
 *
 *        { "per-turn" : Natural (less than or equal to 6),
 *
 *        { "observe"_ : Boolean }
 */
public class RefereeConfig {
  final GameState gameState;
  final boolean quiet;
  final RefereeStateConfig refereeStateConfig;
  final int perTurn;
  final boolean observe;

  public RefereeConfig(GameState gameState, boolean quiet, RefereeStateConfig refereeStateConfig, int perTurn, boolean observe){
    this.gameState = gameState;
    this.quiet = quiet;
    this.refereeStateConfig = refereeStateConfig;
    this.perTurn = perTurn;
    this.observe = observe;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  public boolean isQuiet() {
    return this.quiet;
  }

  public RefereeStateConfig getRefereeStateConfig() {
    return this.refereeStateConfig;
  }

  public int getPerTurn() {
    return this.perTurn;
  }

  public boolean isObserve() {
    return this.observe;
  }
}
