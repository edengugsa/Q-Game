import java.util.List;

public interface AIPlayer {

    /**
     * @return this Player's name
     */
    String name();

    /**
     * Configures this AIPlayer with the state of the game board and its tiles.
     */
    void setup(QGameBoardState board, List<Tile> hand);

    /**
     * @return this AIPlayer's requested QGameCommand based on the given knowledge about the game.
     */
    QGameCommand takeTurn(ActivePlayerKnowledge knowledge);

    /**
     * Represents this AIPlayer's reaction to winning (or losing) the game.
     * @param w true if this AIPlayer won the game
     */
    void win(boolean w);

    /**
     * Tells this AIPlayer what Tiles they have.
     */
    void newTiles(List<Tile> tiles);

}
