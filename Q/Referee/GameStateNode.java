package Referee;
import Common.State.GameState;

public class GameStateNode {
  GameState gameState;
  GameStateNode prev;
  GameStateNode next;

  public GameStateNode(GameState gameState) {
    this.gameState = gameState;
    this.prev = null;
    this.next = null;
  }

}

