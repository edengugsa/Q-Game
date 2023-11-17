package Common.Tiles;

import com.google.gson.JsonElement;

import java.awt.*;
import java.util.Comparator;
import java.util.Objects;

import Common.JsonToQGame;
import Common.QGameToJson;

/**
 * Represents a tile in The Q Game. A tile has two attributes: Color and QShape.
 */
public record Tile(QColor color, QShape shape) {

  /**
   * Represents a Comparator for comparing Tiles.
   *
   * t1 is less than t2 if t1’s shape is less than t2’s.
   * If t1’s shape is identical to t2’s, t1 is below t2 if t1’s color is below t2’s.
   */
  public static final Comparator<? super Tile> TileComparator = (Comparator<Tile>) (t1, t2) -> {
    QShape.QShapeComparator shapeComparator = new QShape.QShapeComparator();
    int shapeCompare = shapeComparator.compare(t1.shape, t2.shape);

    if (shapeCompare == 0) {
      QColor.QColorComparator colorComparator = new QColor.QColorComparator();
      return colorComparator.compare(t1.color, t2.color);
    }
    return shapeCompare;
  };

  public Color toColor() {
    return this.color.toColor();
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Tile other)) {
      return false;
    }
    return other.color().toString().equals(this.color.toString()) &&
            other.shape().toString().equals(this.shape.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color, this.shape);
  }


  public JsonElement toJSON() {
    return QGameToJson.TileToJTile(this);
  }
}
