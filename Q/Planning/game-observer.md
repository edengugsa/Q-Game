# TO: Matthias Felleisen
# FROM: Alina Geng and Hugh Hudson
# CC: Michael Ballantyne
# DATE: 10/26/23
# SUBJECT: Game-Observer Design

Task: Design an interactive game-observer mechanism. The design should describe the interface 
of the component; how it interacts with the existing system; and how a single 
person may wish to interact with the observer’s view.—As always, a mix of prose, 
code snippets, and diagrams is appropriate. Keep it under two pages.

An observer of the Q Game does not have any stakes in the game, so they have permission to view (but not modify)
the entire game state. This includes viewing:
- all the Players' Tiles
- all the Players' scores
- The Tiles on the GameBoard
- The number of referee tiles left
- The Player's names

The Observers of the Q-Game will be stored in Referee, in a similar format to how Players are stored.
When the GameState changes, the referee updates Observers with the changes. When a single person joins the game 
(before it starts), they can opt to play with an AI, or observe the game. If they choose to observe, 
we map them to a new Observer object, so that they may receive the "omniscient" game view. 

A person may wish to view the GameState they were given with the render() method.


interface QObserver

void update(GameState game) --> Updates this Observer with the new GameState.

void results(List winners) --> Informs this Observer the game has ended and who the winners are.

void render() --> Render this Observer's most current GameState as an image


