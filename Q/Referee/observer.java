package Referee;

import Common.Rendering.RenderObserverGameStates;
import Common.State.GameState;

/**
 * Represents an Observer of the Q Game. An Observer can browse through the game at different points
 * of time.
 */
public class observer {
//  List<GameState> gameStates;
  GameState currentGameState;
  RenderObserverGameStates renderObserverGameStates;
//  private ListIterator<GameState> gameStateIterator;

  GameStateList dbll;
  GameStateNode gsIterator;


  public observer() {
    dbll = new GameStateList();
//    this.gameStates = new ArrayList<>();
//    this.gameStateIterator = gameStates.listIterator();
  }

  public GameState getCurrentGameState() {
    return currentGameState;
  }

  public void setup(GameState gameState) {
//    gsIterator = new GameStateIterator(gameState);
    dbll.insertAtEnd(gameState);
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
    dbll.insertAtEnd(state);
//    this.gameStateIterator.add(state);
  }

  public void gameOver() {
    System.out.println("Game over");
    this.renderObserverGameStates.notifyGameOver();
  }

  public GameState next() {
    dbll.getForward();
//    return dbll.

//    List<GameState> gameStatesCopy = new ArrayList<>(this.gameStates);
//    ListIterator<GameState> iterator = gameStatesCopy.listIterator();
//
//    if(iterator.hasNext()) {
//      GameState next = iterator.next();
//      currentGameState = next;
//      return currentGameState;
//    }
//    else {
//      throw new IllegalArgumentException("game has no next state");
//    }
  }

  public GameState previous() {
//    List<GameState> gameStatesCopy = new ArrayList<>(this.gameStates);
//    ListIterator<GameState> iterator = gameStatesCopy.listIterator();
////    int i = gameStatesCopy.indexOf(currentGameState);
////    iterator.
//    if(iterator.hasPrevious()) {
//      GameState prev = iterator.previous();
//      currentGameState = prev;
//      return currentGameState;
//    }
//    else {
//      throw new IllegalArgumentException("game has no previous state");
//    }
  }

  public void save(String fileName) {
    // save gameState
  }

}
