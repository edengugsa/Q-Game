package Referee;

import Common.State.GameState;

public class GameStateList {
  GameStateNode current;
  GameStateNode tail;

  public GameStateList() {
    this.current = null;
    this.tail = null;
  }

  public GameState getForward() {
    if(this.current.next != null) {
      GameState temp = current.next.gameState;
      this.current = current.next;
      return temp;
    }
    else {
      throw new IllegalStateException("Cannot go forward");
    }

//    GameStateNode current = this.current;
//    while (current != null) {
//      System.out.print(current.gameState + " ");
//      current = current.next;
//    }
  }


  // Traversing from tail to the head
  public GameState goBackward() {

    if(this.current.prev != null) {
      GameState temp = current.prev.gameState;
      this.current = current.prev;
      return temp;
    }
    else {
      throw new IllegalStateException("Cannot go backward");
    }
//    GameStateNode current = tail;
//    while (current != null) {
//      System.out.print(current.gameState + " ");
//      current = current.prev;
//    }
  }


  public void insertAtEnd(GameState gameState) {
    GameStateNode iterator = current;
    GameStateNode newNode = new GameStateNode(gameState);

    while (iterator != null) {
      GameStateNode temp = iterator;
      iterator = iterator.next;

      if (iterator == null) {
        temp.next = newNode;
        newNode.prev = temp;
        return;
      }
    }
  }


}
