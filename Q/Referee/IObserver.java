package Referee;

import Common.State.GameState;

/**
 * Represents an Observer than can receive GameStates and be notified that the Game it is viewing
 * is over.
 */
public interface IObserver {
   /**
    * Used to give this Observer the first GameState.
    */
   void setup(GameState gameState);

   /**
    *
    * @param state
    */
   void receiveState(GameState state);
   void gameOver();
}
