package Referee;

/**
 * Represents a scoring configuration for Referee that determines how many points are awarded to a
 * player when they place all their tiles and when they provide a PlacementCommand that forms a Q.
 */
public class RefereeStateConfig {
  final private int qBonus;
  final private int finishBonus;

  // Constructor
  public RefereeStateConfig(int qbo, int fbo) {
    this.qBonus = qbo;
    this.finishBonus = fbo;
  }

  public int getFinishBonus() {
    return this.finishBonus;
  }

  public int getQBonus(int fbo) {
    return this.qBonus;
  }
}

