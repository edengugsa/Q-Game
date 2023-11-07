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
  private ListIterator<GameState> gameStateIterator;


  public observer() {
    this.gameStates = new ArrayList<>();
    this.gameStateIterator = gameStates.listIterator();
  }

  public GameState getCurrentGameState() {
    return currentGameState;
  }

  public void setup(GameState gameState) {
    this.currentGameState = gameState;
    this.receiveState(gameState);
    this.startGUI();
  }

  /**
   * Start showing the GUI. receiveState() must be called BEFORE
   */
  private void startGUI() {
    this.renderObserverGameStates = new RenderObserverGameStates(this);
    this.renderObserverGameStates.setVisible(true);
  }

  /**
   * Receive a new GameState from the Referee
   */
  public void receiveState(GameState state) {
    this.gameStateIterator.add(state);
  }

  public void gameOver() {
    System.out.println("Game over");
    this.renderObserverGameStates.notifyGameOver();
  }

  public GameState next() {

//    List<GameState> gameStatesCopy = new ArrayList<>(this.gameStates);
//    ListIterator<GameState> iterator = gameStatesCopy.listIterator();

    if(gameStateIterator.hasNext()) {
      GameState next = gameStateIterator.next();
      currentGameState = next;
      return currentGameState;
    }
    else {
      throw new IllegalArgumentException("game has no next state");
    }
  }

  public GameState previous() {
//    List<GameState> gameStatesCopy = new ArrayList<>(this.gameStates);
//    ListIterator<GameState> iterator = gameStatesCopy.listIterator();
//    int i = gameStatesCopy.indexOf(currentGameState);
//    iterator.
    if(this.gameStateIterator.hasPrevious()) {
      GameState prev = gameStateIterator.previous();
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
