package Player;
import java.util.List;

import Common.GameBoard.GameBoard;
import Common.GameCommands.QGameCommand;
import Common.State.ActivePlayerKnowledge;
import Common.State.GameState;
import Common.State.QGameBoardState;
import Common.Tiles.Tile;

public interface player {

    /**
     * @return this Player's name
     */
    String name();

    /**
     * Configures this player with the state of the game board and its tiles.
     */
    void setup(GameState state, List<Tile> hand);

    /**
     * @return this player's requested QGameCommand based on the given knowledge about the game.
     */
    QGameCommand takeTurn(ActivePlayerKnowledge knowledge);

    /**
     * Represents this player's reaction to winning (or losing) the game.
     * @param w true if this player won the game
     */
    void win(boolean w);

    /**
     * Tells this player what Tiles they have.
     */
    void newTiles(List<Tile> tiles);

}
