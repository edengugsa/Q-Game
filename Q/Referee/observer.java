package Referee;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Common.JsonUtils;
import Common.Rendering.GameBoardPainter;
import Common.Rendering.RenderObserverGameStates;
import Common.State.GameState;

/**
 * Represents an Observer of the Q Game. An Observer can browse through the game at different points
 * of time.
 */
public class observer {
  private List<GameState> gameStates;
  private int currentGameStateIdx;
  private RenderObserverGameStates renderObserverGameStates;

  public observer() {
    this.gameStates = new ArrayList<>();
    this.currentGameStateIdx = 0;
  }

  /**
   * @return the current GameState the observer sees.
   */
  public GameState getCurrentGameState() {
    return gameStates.get(currentGameStateIdx);
  }

  public void setup(GameState gameState) {
    this.currentGameStateIdx = 0;
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
  // i think our receiveState should call a method thats like renderGameStateAsPNG
  // and then our JPanel just shows that image instead of having JPanels. Like all the JPanels
  // should be combined into an image
  // its for the first functionality but the second functionality's gui happens to use it
  // im confused

  /**
   * Receive a new GameState from the Referee
   */
  public void receiveState(GameState state) {
    this.gameStates.add(state);
  }

  /**
   * @return An Image representing the given GameState.
   */
//  public BufferedImage GameStateToPng(GameState gs) {
//    BufferedImage gameboard = new GameBoardPainter(gs.board()).reveal();
//    BufferedImage playerStates =
//
//    BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//
//    return res;
//  }

  public void gameOver() {
    System.out.println("Observer: Game over");
    this.renderObserverGameStates.notifyGameOver();
  }
// lets make it a method first GameStateToPng

  public GameState next() {
    if (this.currentGameStateIdx + 1 < this.gameStates.size()) {
      this.currentGameStateIdx += 1;
      return gameStates.get(this.currentGameStateIdx);
    }
    else {
      throw new IllegalArgumentException("game has no next state");
    }
  }

  public GameState previous() {
    if(this.currentGameStateIdx - 1 >= 0) {
      this.currentGameStateIdx -= 1;
      return gameStates.get(this.currentGameStateIdx);
    }
    else {
      throw new IllegalArgumentException("game has no previous state");
    }
  }

  /**
   * Saves the current GameState to a Json file. the .json extension is automatically appended.
   */
  public void save(String fileName) throws IOException {
    JsonObject stateToSave = JsonUtils.GameStateToJState(gameStates.get(this.currentGameStateIdx));
    new Gson().toJson(stateToSave, new FileWriter(fileName + ".json"));
  }
}
