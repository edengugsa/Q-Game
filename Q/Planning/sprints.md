# CS4500 The O Game Memorandum

# TO:   Matthias Felleisen

# FROM: Hugh and Lisa (Clever Wolves)

# CC:   Michael Ballantyne (TA)

# DATE: September 13th, 2023

# SUBJECT:  Program Planning


**Sprint #1 Goals**

Design the Player component.
- Retrieves information about the name, location, etc.
- Retrieves information about the Player's current tiles.

Design the Referee component.
- Responsible for setting up the game by
instantiating the board (model) and waiting
for Players to "sign-up"
- Referee will store the board for the board game(the model)
- Referee will store each Player(that contains info of the player when it signs up and its respective card state)
- Referee will be in charge of updating the model(both the Player and the board)
  and is responsible for sending the "truth" to all players for each turn
- Referee will also be in charge of validating each turn for the players and checking
  if the game is over
 
**Sprint #2 Goals**


**Sprint #3 Goals**

Develop the client-server communication
Implement Methods for 













# TO: Matthias Felleisen

# FROM: Clever Wolves (Hugh and Lisa)

# CC: Michael Ballantyne

# DATE: September 14th, 2023

# SUBJECT: Plan for Building The Q Game

# High-level idea:
- Referee will store the “truth” of the entire game state -- including the board itself and the cards that each client/player has for each turn
- Referee will be in charge of updating the model after receiving the moves from each player

# Sprint #1 Goals:

Goal #1: Determine a data representation of the GameState.
- Must separate the data representation of the public board from the data representation of the tiles, which a player can either put on the board or exchange the whole deck
- Determine the data representation for the tiles each player has in his or her
possession. 
- Must remain as private information.
- Determine how to store the spare deck that the referee can hand out in exchange for a player’s 6 cards

Goal #2: Write the basic functionality for the game board.
- An external method for expanding the board and placing tiles on the game board -- mutating the game state with a given move
- An internal method for mutating the tiles for the respective client with a given move
- An external method for scoring a placement of tiles.

# Sprint 2 Goals: 
Establish the Referee and its necessary methods for setting up and carrying out The Q Game.

Referee must be able to...
- Determine which client’s turn it is and send a respective client request when it’s their turn
- Validate a player’s response
- Referee should be able to query and alter the GameState by providing the GameState a move with the previously written methods.
- Check for end game, report updated scores
- Broadcast the GameState to all Players and Observers
Repeat
IF a client crashes/quits, game should continue.

# Sprint 3 Goals: 
Complete the Referee-Player Interface and a Player implementation to validate it.
- Server initializes the game when enough players sign up
AIPlayerImpl should be able to...
- Sign up to a server
- Compute a move on the tile or exchange their current tiles or pass
- Know their scores
- Know when they win
