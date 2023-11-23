package Common.Tiles;

import java.util.Comparator;

/**
 * Represents the possible shapes of a Q Game tile.
 */
public enum QShape {
  circle("circle", 4),
  square("square", 3),
  diamond("diamond", 6),
  star("star", 1),
  eight_star("8star", 2),
  clover("clover", 5);
  private final String shapeName;
  private final int order; // represents the order of this QShape where 1 is the lowest

  QShape(String shapeName, int order) {
    this.shapeName = shapeName;
    this.order = order;
  }

  public int getOrder() {
    return this.order;
  }

  /**
   * Converts the given color represented as a String to its corresponding QColor enum
   */
  public static QShape fromString(String shapeName) {
    for (QShape shape : QShape.values()) {
      if (shape.shapeName.equalsIgnoreCase(shapeName)) {
        return shape;
      }
    }
    throw new IllegalArgumentException("No QShape named " + shapeName);
  }

  @Override
  public String toString() {
    return this.shapeName;
  }

  /**
   * Represents a Comparator for comparing QColors
   */
  public static class QShapeComparator implements Comparator<QShape> {
    @Override
    public int compare(QShape s1, QShape s2) {
      return s1.order - s2.order;
    }
  }
}



