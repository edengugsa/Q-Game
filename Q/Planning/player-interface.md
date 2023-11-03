# CS4500 The Q Game Memorandum

# TO: Matthias Felleisen

# FROM: Hugh and Lisa (Clever Wolves)

# CC: Michael Ballantyne (TA)

# DATE: October 5th, 2023

# SUBJECT: Designing the Player Interface


Here is our function "wish-list" for the Player class, specifically 
regarding the mechanics for executing game actions...

Make moves:
- public void requestsAPass()
{Requests a pass}

- public Set<Tile> exchangeTiles();
  {Exchanges this Player's tiles for a new set (of the same #) of tiles}

- public void requestsToPlaceTiles()
{requests to place tiles}

In order to make the above moves, the player needs to know this player's current set of tiles,
and the game board.

---
// map of string to integer represents a map of playerID to their respective scores 
- public int getScores(Map<String, Integer> scores)
{gets all players' current number of points from the referee}

- public getBoard(GameBoard gameBoard)
{gets a Viewable gameBoard from referee}

- public getTurns(List<Player> turns)
{gets the players' turns}

// gets this player's current set of tiles from the referee
- public getCurrentTiles(Set<Tile> tiles)

// map of string to integer represents a map of playerID to their number of remaining tiles
- public int getNumRemainingTiles(Map<String, Integer> numberOfRemainingTiles)
{gets number of remaining tiles for all players}

- public void requestsToQuit()
{requests to quit the game}