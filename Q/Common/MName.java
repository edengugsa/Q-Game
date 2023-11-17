package Common;

import Common.Tiles.QColor;

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

  public static MName fromString(String mName) {
    for (MName mNameString : MName.values()) {
      if (mNameString.string.equalsIgnoreCase(mName)) {
        return mNameString;
      }
    }
    throw new IllegalArgumentException("No MName named " + mName);
  }

}
