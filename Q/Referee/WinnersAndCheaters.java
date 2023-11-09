package Referee;

import java.util.List;

/**
 * Represents a list of winners' names and cheaters' names.
 */
public class WinnersAndCheaters {
  public List<String> winners;
  List<String> cheaters;

  public WinnersAndCheaters(List<String> winners, List<String> cheaters) {
    this.winners = winners;
    this.cheaters = cheaters;
  }
}
