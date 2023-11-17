package Common;

/**
 * Represents possible MNames.
 */
public enum MName {
  SETUP("setup"),
  TAKE_TURN("take-turn"),
  NEW_TILES("new-tiles"),
  WIN("win");

  private final String string;

  MName(String string) {
    this.string = string;
  }




}
