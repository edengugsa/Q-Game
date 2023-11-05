package Common.State;

public interface PublicPlayerState {

    /**
     * @return this Player's name.
     */
    String name();

    /**
     * @return the current number of points this Player has.
     */
    int score();

    /**
     * @return the number of tiles this Player's hand.
     */
    int tilesRemaining();


}
