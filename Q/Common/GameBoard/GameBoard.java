package Common.GameBoard;
import java.util.*;
import Common.Tiles.Coordinate;
import Common.Tiles.Tile;
import Common.Tiles.Placement;


/**
 * Represents an instance of The Q Game board implementing the QGameBoardState interface.
 * Utilizes a Map to chart Tiles to a unique (x,y) coordinate. Coordinates have no mapping bounds in this implementation.
 */
public class GameBoard {

  private final Map<Coordinate, Tile> board;

  /**
   * Creates the game board given a starting tile, placing it at (0,0)
   *
   * @param startTile the game's starting tile provided by referee
   */
  public GameBoard(Tile startTile) {
    this.board = new HashMap<>();
    this.board.put(new Coordinate(0,0), startTile);
  }

  public GameBoard(Map<Coordinate, Tile> map) {
    this.board = new HashMap<>(map);
  }

  /**
   * Returns all the Placements on this GameBoard where the given Tile is adjacent.
   */

  public List<Placement> placementAdjacentOptions(Tile tile) {
    Set<Coordinate> allEmptySpots = this.getEmptySpots();
    ArrayList<Placement> options = new ArrayList<>();
    for (Coordinate emptySpot : allEmptySpots) {
      if (this.isEmptyAndAdjacent(new Placement(emptySpot, tile))) {
        options.add(new Placement(emptySpot, tile));
      }
    }
    return options;
  }



  public boolean containsCoord(Coordinate c) {
    return this.board.containsKey(c);
  }

  public Tile getTileAt(Coordinate c) {
    return this.board.get(c);
  }

  public Map<Coordinate, Tile> getMap() {
    return new HashMap<>(this.board); // returns a new copy, not the actual instance.
  }

  public int getNumNeighbors(Coordinate coordinate) {
    int count = 0;
    for (Coordinate c : coordinate.getNeighbors()) {
      if (this.containsCoord(c)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Tries to extend this board with a tile at the given coordinate
   * @throws RuntimeException if a tile cannot be placed at the coordinate
   */
  public void extend(Coordinate c, Tile t) throws RuntimeException {
    if (this.checkAdjacent(c)) {
      this.board.put(c, t);
    } else {
      throw new RuntimeException("Tile cannot be placed. " +
              "No adjacent tiles.");
    }
  }

  /**
   * Given a tile and a coordinate, determines whether the tile can be
   * placed on the board at the coordinate without breaking any placement
   * rules described in The Q Game. The rules are as follows:
   * --> Tiles placed must have an existing tile adjacent it.
   * --> All adjacent tiles must match the new tile either by shape or by color.
   *
   * @param p the placement under consideration
   * @return true if the tile can be legally placed, false otherwise
   */
  public boolean isEmptyAndAdjacent(Placement p) {
    return !board.containsKey(p.coordinate()) && checkAdjacent(p.coordinate());
  }

  /**
   * Gets all the contiguous tiles in a row for the given placement.
   */
  public Set<Placement> getContiguousTilesInRow(Placement p) {
    Set<Placement> allTileInRow = new HashSet<>();
    allTileInRow.addAll(getTilesInDirection(p.coordinate(), Coordinate.left()));
    allTileInRow.addAll(getTilesInDirection(p.coordinate(), Coordinate.right()));
    if (allTileInRow.size() > 0) {
      allTileInRow.add(p);
    }
    return allTileInRow;
  }

  /**
   * Gets all the contiguous tiles in a column starting from the given placement.
   */
  public Set<Placement> getContiguousTilesInCol(Placement p) {
    Set<Placement> allTileInCol = new HashSet<>();
    allTileInCol.addAll(getTilesInDirection(p.coordinate(), Coordinate.up()));
    allTileInCol.addAll(getTilesInDirection(p.coordinate(), Coordinate.down()));
    if (allTileInCol.size() > 0) { // if the placement has neighbors, include it in the column
      allTileInCol.add(p);
    }
    return allTileInCol;
  }

  /**
   * Gets all the continuous tiles in a given direction from a starting Coordinate, excluding the Tile
   * at the given coordinate.
   * @param coordinate the starting coordinate
   * @param direction left (0,-1), right (0,1), above (-1,0), or below (1,0)
   */
  private Set<Placement> getTilesInDirection(Coordinate coordinate, Coordinate direction) {
    Set<Placement> placedTiles = new HashSet<>();
    coordinate = coordinate.add(direction);
    while (board.containsKey(coordinate)) {
      placedTiles.add(new Placement(coordinate, board.get(coordinate)));
      coordinate = coordinate.add(direction);
    }
    return placedTiles;
  }

  /**
   * @return true if their exists a tile on this board adjacent to the given coordinate
   */
  private boolean checkAdjacent(Coordinate c) {
    for (Coordinate neighbor : c.getNeighbors()) {
      if (board.containsKey((neighbor))) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return the set of all empty spots for a tile on this board
   */
  private Set<Coordinate> getEmptySpots() {
    Set<Coordinate> emptySpots = new HashSet<>();
    for (Coordinate spot: this.board.keySet()) { // all the tiles on the board
      for (Coordinate neighbor: spot.getNeighbors()) {
        if (!board.containsKey(neighbor)) {
          emptySpots.add(neighbor);
        }
      }
    }
    return emptySpots;
  }
}
