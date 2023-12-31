package Common.Tiles;

import com.google.gson.JsonObject;

import java.util.Objects;

/**
 * Represents a Tile placed at a Coordinate.
 */
public class Placement {
  private final Coordinate coord;
  private final Tile tile;

  /**
   * Constructs a placement with a coordinate and a tile.
   */
  public Placement(Coordinate c, Tile t) {
    this.coord = c;
    this.tile = t;
  }

  /**
   * @return this Placement's coordinate.
   */
  public Coordinate coordinate() {
    return this.coord;
  }

  /**
   * @return this Placement's tile.
   */
  public Tile tile() {
    return this.tile;
  }

  public JsonObject toJson() {
    JsonObject jPlacement = new JsonObject();
    jPlacement.add("coordinate", this.coord.toJSON());
    jPlacement.add("1tile", this.tile.toJSON());
    return jPlacement;
  }

  @Override
  public String toString() {
    return "["+this.tile.toString()+", "+this.coord.toString()+"]";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Placement other)) {
      return false;
    }
    return other.tile().equals(this.tile) && other.coordinate().equals(this.coord);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.tile, this.coord);
  }



}
