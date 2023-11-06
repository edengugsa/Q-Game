package Common.RuleBook;

import java.util.Queue;

import Common.Tiles.Placement;

public class NotALineRuleBook extends RuleBook {
  @Override
  protected boolean contiguous(Queue<Placement> placements) {
    return !super.contiguous(placements);
  }
}
