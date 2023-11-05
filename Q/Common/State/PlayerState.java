package Common.State;

import java.util.List;

import Common.Tiles.Tile;

/**
 * Represents the Referee's knowledge of a Player in the Q game.
 */
public interface PlayerState extends PublicPlayerState {

    void setName(String name);

    /**
     * Adds tiles to this PlayerState's hand.
     */
    void newTiles(List<Tile> tiles);

    /**
     * Rewards this Player the given amount of points.
     */
    void reward(int points);

    /**
     * @return this PlayerState's hand
     */
    List<Tile> getHand();

    /**
     * @return the number of turns this PlayerState has completed
     */
    int turnsCompleted();

    /**
     * Increases the number of turns this PlayerState has completed by 1.
     */
    void incrementTurnsCompleted();

    /**
     * @param t the tile to remove from this PlayerState
     */
    void removeTile(Tile t);

}
