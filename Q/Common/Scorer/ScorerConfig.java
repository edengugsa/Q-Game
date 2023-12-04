package Common.Scorer;

/**
 * A ScorerConfig determines how many points are awarded for finishing a game or completing a Q.
 */
public class ScorerConfig {
  final private int qBonus;
  final private int finishBonus;

  // Constructor
  public ScorerConfig(int qbo, int fbo) {
    this.qBonus = qbo;
    this.finishBonus = fbo;
  }

  public int getFinishBonus() {
    return this.finishBonus;
  }

  public int getQBonus() {
    return this.qBonus;
  }
}

