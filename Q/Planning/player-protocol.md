# TO: Matthias Felleisen
# FROM: Alina Geng and Hugh Hudson
# CC: Michael Ballantyne
# DATE: 10/12/23
# SUBJECT: Player-Referee Communication Protocol

## PHASE 1: LOADING GAME  

1. A Player sends a request to the Referee to join the game.
2. Referee receives request, chooses to ACCEPT or DECLINE the Player into the game.
   - If DECLINE, tell Player they were not accepted and end communication with them.
   - If ACCEPT, ask the Player for their birthday and name and weight (lbs.).
3. The accepted Player sends birthday, name, and weight (lbs.)
4. Referee tells the Player to wait until the game is ready to begin.

## PHASE 2A: GAME READY TO START

1. Referee sends each Player their 6 tiles, the current board, other Players' scores, the # if Referee Tiles,
and the order of their turn, with the message "LET THE GAMES BEGIN!"

## PHASE 2B: ACTIVE PLAYER'S TURN 

1. Referee alerts active Player it is their turn. Referee once again sends their 6 tiles, the current
   board, the score, the number of Referee tiles left and the order of Players' turns. 
   The Referee tells all other players the name of the Player who is currently making a move.
2. Player responds with:
    - Pass
    - Exchange all their tiles
    - Request placement of tiles 
3. Referee response depends on the Player's requested move's legality:
    - If move is VALID:
      - Briefly congratulate player.
      - Send all Players the updated game board, scores, and # of Referee Tiles. 
      - Send the active Player their new tiles.
      - Repeat STEPS 1-3.
    - If move is INVALID:
      - Reprimand the Player. Warn/threaten them.
      - Player is given 2 more tries to make a valid move before
        their move automatically ceases.

## PHASE 3: GAME OVER

1. Referee alerts all Players the game is over along with the final game board and everyone's score. 
   The Referee also ends communication with all the Players.



