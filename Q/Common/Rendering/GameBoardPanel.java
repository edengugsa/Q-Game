package Common.Rendering;

import java.awt.*;
import javax.swing.*;

import Common.GameBoard.GameBoard;

/**
 * Renders a GameBoard. It receives a copy of the GameBoard
 * so updateGameBoard() needs to be called before repaint().
 */
public class GameBoardPanel extends JPanel {

  private GameBoard board;
  private int maxWidth;
  private int maxHeight;

  public GameBoardPanel(GameBoard board) {
    this.board = board;
    this.maxHeight = 400;
    this.maxWidth = 400;
  }
  public GameBoardPanel(GameBoard board, int maxWidth, int maxHeight) {
    this.board = board;
    this.maxHeight = maxHeight;
    this.maxWidth = maxWidth;
  }


  public void updateGameBoard(GameBoard board) {
    this.board = board;
  }

  @Override
  protected void paintComponent(Graphics g) {
    GameBoardPainter gbp = new GameBoardPainter(this.board.getMap(), this.maxWidth, this.maxHeight);
    super.paintComponent(g);
    g.drawImage(gbp.reveal(), 0, 0, null);
  }

}
