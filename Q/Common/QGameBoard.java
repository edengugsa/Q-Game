/**
 * Interface for representing the model board in The Q Game. Contains functionality
 * for placing tiles on the board at provided locations, and
 */
public interface QGameBoard extends QGameBoardState {

    /**
     * Places a tile on the board at the given coordinate, ensuring that
     * it shares a side with a pre-existing tile.
     *
     * @param c the coordinate of the tile to be placed
     * @param t the tile to place on the board
     * @throws IllegalArgumentException if the placement on the board is invalid
     */
    void extend(Coordinate c, Tile t) throws IllegalArgumentException;
}
