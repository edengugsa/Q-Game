package Common.State;

import java.util.List;
import java.util.Map;

import Common.Tiles.Coordinate;
import Common.Tiles.Placement;
import Common.Tiles.Tile;

/**
 * Interface representing the status of The Q Game board. Contains methods
 * providing important sizing, rendering, and in-game suggestion information.
 */
public interface QGameBoardState {

    /**
     * @return a copy of this QGameBoardState's map
     */
    Map<Coordinate, Tile> getMap();

    /**
     * @return the number of tiles surrounding the given coordinate on this QGameBoardState.
     */
    int getNumNeighbors(Coordinate c);

    /**
     * Delivers an ArrayList of coordinates specifying the locations
     * where a selected tile could be properly placed on this current QGameBoardState.
     *
     * @param tile the selected tile
     * @return the coordinates where the tile could potentially be placed
     */
    List<Placement> placementOptions(Tile tile);

    /**
     * Does this board have a Tile at the given Coordinate?
     */
    boolean containsCoord(Coordinate c);

    /**
     * Gets the Tile at the given Coordinate. This is called after containsCoord()!!!!
     */
    Tile getTileAt(Coordinate c);

}
