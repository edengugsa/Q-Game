package Referee;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import Common.Rendering.RenderGameState;
import Common.Rendering.RenderObserverGameStates;
import Common.State.GameState;

/**
 * Represents an Observer of the Q Game. An Observer can browse through the game at different points
 * of time.
 */
public class observer {
  List<GameState> gameStates;
  GameState currentGameState;
  RenderObserverGameStates renderObserverGameStates;
  private ListIterator gameStateIterator;


  public observer() {
    this.gameStates = new ArrayList<>();
    this.gameStateIterator = gameStates.listIterator();
  }

  public GameState getCurrentGameState() {
    return currentGameState;
  }


  /**
   * Start showing the GUI. receiveState() must be called BEFORE
   */
  public void startGUI() {
    this.renderObserverGameStates = new RenderObserverGameStates(this);
    this.renderObserverGameStates.setVisible(true);
    System.out.println("set gui visible");
  }

  public void receiveState(GameState state) {
    this.gameStates.add(state);
  }

  public void gameOver() {
    System.out.println("Game over");
    this.renderObserverGameStates.notifyGameOver();
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
