package Referee;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import Common.Rendering.RenderGameState;
import Common.State.GameState;

/**
 * Represents an Observer of the Q Game. An Observer can browse through the game at different points
 * of time.
 */
public class observer {
  List<GameState> gameStates;
  GameState currentGameState;
  RenderGameState renderGameState;
  private ListIterator gameStateIterator;


  public observer() {
    gameStates = new ArrayList<>();
    gameStateIterator = gameStates.listIterator();
  }


  /**
   * Start running the GUI. receiveState() must be called BEFORE
   */
  public void startGUI() {
    // render game start
    this.renderGameState = new RenderGameState(currentGameState);
    this.renderGameState.setVisible(true);
  }

  public void receiveState(GameState state) {
    gameStates.add(state);
  }

  public void gameOver() {
    // render game over for observer
  }

  public GameState next() {
    if(gameStateIterator.hasNext()) {
      GameState next = (GameState) gameStateIterator.next();
      currentGameState = next;
      return currentGameState;
    }
    else {
      throw new IllegalArgumentException("game has no next state");
    }
  }

  public GameState previous() {
    if(gameStateIterator.hasPrevious()) {
      GameState prev = (GameState) gameStateIterator.previous();
      currentGameState = prev;
      return currentGameState;
    }
    else {
      throw new IllegalArgumentException("game has no previous state");
    }
  }

  public void save(String fileName) {
    // save gameState
  }

}
