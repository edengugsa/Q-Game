# TO: Matthias Felleisen
# FROM: Alina Geng and Hugh Hudson
# CC: Michael Ballantyne
# DATE: 10/19/23
# SUBJECT: Referee-GameState Communication Protocol And GameState Interface

Our game-state component represents the knowledge a Referee needs to know in order to run the Q Game. It has the following methods: <br>
// instantiate a Game with Players and a board<br>
GameState(Queue<Players>, GameBoard)<br>
// moves onto the next Player<br>
void bump()<br>
// extracts and returns all the information the active Player needs to know to make a move<br>
ActivePlayerKnowledge getActivePlayerKnowledge()<br>
// extracts and returns all the information an inactive Player needs to know about this game<br>
InactivePlayerKnowledge getInactivePlayerKnowledge()<br>
// returns a new GameState with the given GameAction performed on this GameState <br>
GameState executeAction(GameAction)<br>
// update the active Player's score with the given amount<br>
void updateScore(int points) <br>
// is the game over?<br>
boolean isGameOver()<br>
// get a list of all the Player's scores<br>
List<Integer> getScores()<br>
The referee will interact with game-state as follows:
## PHASE 1: START GAME
1. Referee instantiates GameState by calling GameState() with a queue of players ordered by
birthday
2. Referee calls getActivePlayerKnowledge() followed by bump() for all Players 
## PHASE 2: TAKE TURNS
3. Referee calls getActivePlayerKnowledge() for the active Player
4. Referee calls getInactivePlayerKnowldge() for all inactive Players
5. Referee calls executeAction() with the GameAction the active Player gave
6. Referee calls updateScore() to update the score of the Player that just took a turn
7. Referee calls isGameOver() 
   - if false, repeat Steps 3-7
   - if true, call getScores(). end the protocol
