import java.awt.*;
import javax.swing.*;

/**
 * Renders a GameBoard. It receives a copy of the GameBoard
 * so updateGameBoard() needs to be called before repaint().
 */
public class GameBoardPanel extends JPanel {

  private GameBoard board;

  GameBoardPanel(GameBoard board) {
    this.board = board;
  }

  public void updateGameBoard(GameBoard board) {
    this.board = board;
  }

  @Override
  protected void paintComponent(Graphics g) {
    GameBoardPainter gbp = new GameBoardPainter(this.board.getMap(), 400, 400);
    super.paintComponent(g);
    g.drawImage(gbp.reveal(), 0, 0, null);
  }

}
