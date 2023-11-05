package Common.Tiles;

import java.awt.*;
import java.util.Comparator;

import java.util.Objects;

/**
 * Enumeration representing the shape on a Q Game tile.
 */
public enum QColor {
  RED("red", 1),
  GREEN("green", 2),
  BLUE("blue",3),
  YELLOW("yellow",4),
  ORANGE("orange",5),
  PURPLE("purple",6);

  private final String colorName;
  private final int order; // represents the order of this QColor where 1 is the lowest

  QColor(String colorName, int order) {
    this.colorName = colorName;
    this.order = order;
  }

  /**
   * Converts the given color represented as a String to its corresponding QColor enum
   */
  public static QColor fromString(String colorName) {
    for (QColor color : QColor.values()) {
      if (color.colorName.equalsIgnoreCase(colorName)) {
        return color;
      }
    }
    throw new IllegalArgumentException("No QColor named " + colorName);
  }

  public Color toColor() {
    return switch (colorName) {
      case "red" -> new Color(234, 51, 35);
      case "green" -> new Color(117, 251, 76);
      case "blue" -> new Color(0, 0, 245);
      case "yellow" -> new Color(255, 255, 84);
      case "orange" -> new Color(242, 169, 59);
      case "purple" -> new Color(147, 44, 231);
      default -> Color.white;
    };
  }

  @Override
  public String toString() {
    return this.colorName;
  }

  /**
   * Represents a Comparator for comparing QColors
   */
  public static class QColorComparator implements Comparator<QColor> {
    @Override
    public int compare(QColor c1, QColor c2) {
      return c1.order - c2.order;
    }
  }
}
