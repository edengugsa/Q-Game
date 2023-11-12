package Referee;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import Common.JsonUtils;
import Common.Rendering.RenderGameState;
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

  /**
   * Receive a new GameState from the Referee and saves it as a PNG file in Tmp/
   */
  public void receiveState(GameState state) {
    this.gameStates.add(state);
    this.saveGameStateAsPng(state, this.gameStates.size() - 1);
  }

  /**
   * Saves the given GameState at [idx].png in Tmp/
   */
  private void saveGameStateAsPng(GameState state, int idx) {
    String fileName = "Tmp/" + idx + ".png";
    try {
      ImageIO.write(new RenderGameState(state).GameStateToPng(state), "png", new File(fileName));
    }
    catch(Exception e) {
//      System.out.println("Could not save game state " + fileName + ": " + e);
    }
  }


  public void gameOver() {
//    System.out.println("Observer: Game over");
    this.renderObserverGameStates.notifyGameOver();
  }

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
    try (FileWriter fileWriter = new FileWriter(fileName + ".json")) {
      new Gson().toJson(stateToSave, fileWriter);
      fileWriter.flush();
    }
  }
}
