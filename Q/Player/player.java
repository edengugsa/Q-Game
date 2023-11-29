package Player;
import java.util.List;

import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.Tiles.Tile;

public interface player {

    /**
     * @return this Player's name
     */
    String name();

    /**
     * Configures this client with the state of the game board and its tiles.
     */
    void setup(ActivePlayerKnowledge apk, List<Tile> hand);

    /**
     * @return this client's requested QGameCommand based on the given knowledge about the game.
     */
    QGameCommand takeTurn(ActivePlayerKnowledge knowledge);

    /**
     * Represents this client's reaction to winning (or losing) the game.
     * @param w true if this client won the game
     */
    void win(boolean w);

    /**
     * Tells this client what Tiles they have.
     */
    void newTiles(List<Tile> tiles);


}
