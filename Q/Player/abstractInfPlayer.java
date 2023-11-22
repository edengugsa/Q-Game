package Player;

import java.util.function.Supplier;

import Player.Strategy.Strategy;

public class abstractInfPlayer extends playerImpl {
  int when;

  public abstractInfPlayer(String name, Strategy strategy, int when) {
    super(name, strategy);
    this.when = when;
  }

  protected void infLoop() {
    int i = 1;
    while (true) {
//      System.out.println("inf looping" + i);
      i++;
    }
  }

}
