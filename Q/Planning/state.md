# TO: Matthias Felleisen

# FROM: Clever Wolves (Hugh and Lisa)

# CC: Michael Ballantyne

# DATE: September 25th, 2023

# SUBJECT: Plan for Building The Q Game

Game state should have
* The game Board where referee will place tiles
* Each of the player’s tiles
* Spare tiles for the referee to exchange to the players

The GameState data-representation needs to include the following fields;

    GameBoard board; // the state of the game board
    Map<PlayerID, Player> players; // map from unique playerID to Players in the game, 
    ArrayList<Tile> spareTiles; // stores the spare tiles that in the referee's possession
    Map<Player, Integer> scores; // stores the scores of each individual player



public class Player {
private final ArrayList<Tile> tiles;
// name, and other personal info

    /**
     * 
     * @return 
     */
    public Map<Coordinate, Tile> makeMove() {}

    /**
     * Add the tiles received from the referee to the tiles in THIS player's hand
     * @param tilesReceived Tiles received from the Referee to be appended
     */
    public void refreshTiles(ArrayList<Tile> tilesReceived) {}

    /**
     *  Takes this player's tiles (for the referee)
     */
    public List<Tile> takeTiles() {}
}


}