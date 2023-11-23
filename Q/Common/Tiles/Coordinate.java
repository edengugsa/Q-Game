package Common.Tiles;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Objects;

/**
 * Represents a 2-D coordinate at a row and column.
 */
public class Coordinate implements Comparable {

  private final int col;
  private final int row;

  /**
   * Instantiates this coordinate with a row and col.
   */
  public Coordinate(int col, int row){
    this.col = col;
    this.row = row;
  }

  /**
   * Returns the coordinates directly above, below, left, and right of this coordinate in that
   * order.
   */
  public Coordinate[] getNeighbors() {
    return new Coordinate[]{
            new Coordinate(col, row + 1), // down
            new Coordinate(col, row - 1), // up
            new Coordinate(col - 1, row),// left
            new Coordinate(col + 1, row) // right
    };
  }

  /**
   * Adds the given Coordinate to this Coordinate. Performs Vector addition on the two
   * coordinates.
   */
  public Coordinate add(Coordinate other) {
    return new Coordinate(this.col + other.col, this.row + other.row);
  }

  /**
   * @return Coordinate representing the left direction
   */
  public static Coordinate left() {
    return new Coordinate( - 1, 0);
  }

  /**
   * @return Coordinate representing the right direction
   */
  public static Coordinate right() {
    return new Coordinate( 1, 0);
  }

  /**
   * @return Coordinate representing the up direction
   */
  public static Coordinate up() {
    return new Coordinate(0,  -1);
  }

  /**
   * @return Coordinate representing the down direction
   */
  public static Coordinate down() {
    return new Coordinate(0,  1);
  }

  public int col() {
    return col;
  }

  public int row() {
    return row;
  }

  @Override
  public String toString() {
    return "(col: " + col + ", row: " + row + ")";
  }

  public String toStringXY() {
    return "(" + col + ", " + row + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Coordinate other)) {
      return false;
    }
    return other.col() == this.col && other.row() == this.row;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.col, this.row);
  }

  /**
   * Converts the given JCoordinate to a Coordinate.
   */
  public static Coordinate toCoordinate(JsonObject jCoordinate) {
    int y = jCoordinate.get("row").getAsInt();
    int x = jCoordinate.get("column").getAsInt();

    return new Coordinate(x, y);
  }

  public JsonElement toJSON() {
    JsonObject obj = new JsonObject();
    obj.add("row", JsonParser.parseString(String.valueOf(this.row)));
    obj.add("column", JsonParser.parseString(String.valueOf(this.col)));
    return obj;
  }

  @Override
  public int compareTo(Object o) {
    if (!(o instanceof Coordinate c2)) {
      throw new IllegalArgumentException("I refuse to compare myself to other objects.");
    }
    if (this.row == c2.row()) {
      return this.col - c2.col();
    }
    else {
      return this.row - c2.row();
    }
  }
}
