package Common.Rendering;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import Common.State.GameState;

/**
 * Represents functionality to render a State. The image will display the GameBoard, the number
 * of remaining Referee tiles, as well as each player's score and Tiles.
 */
public class RenderGameState extends JPanel {
  GameState gamestate;
  GameBoardPanel gameboardPanel;
  JScrollPane scrollableGameBoard;
  PlayerStatesPanel playerStatesPanel;
  JLabel numRefTiles;

  public RenderGameState(GameState gamestate) {
    this.gamestate = gamestate;
    this.setSize(1500,1000);
    this.setLayout(new GridLayout(1,3, 20,0));
    this.gameboardPanel = new GameBoardPanel(gamestate.getGameBoard());
    this.scrollableGameBoard = new JScrollPane(gameboardPanel);
    this.playerStatesPanel = new PlayerStatesPanel(this.gamestate.players());
    this.numRefTiles = new JLabel();
    this.add(this.scrollableGameBoard);
    this.add(this.playerStatesPanel);
    this.add(numRefTiles);
    this.setVisible(true);
  }

  public void updateGameState(GameState gs) {
    this.gamestate = gs;
  }

  /**
   * @return an image showing this class' GameState.
   */
  public BufferedImage toPng() {
    BufferedImage gameBoardImg = new GameBoardPainter(this.gamestate.getGameBoard().getMap()).reveal();
    BufferedImage playerStatesImg = this.playerStatesPanel.toPng();
    int height = Math.max(gameBoardImg.getHeight(), playerStatesImg.getHeight());
    int width = gameBoardImg.getWidth() + 10 + playerStatesImg.getWidth();
    BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(gameBoardImg, 0, 0, null);
    g.drawImage(playerStatesImg, gameBoardImg.getWidth() + 10, 0, null);
    return combined;
  }

  @Override
  public void paintComponent(Graphics e) {
    this.playerStatesPanel.updatePlayers(this.gamestate.players());
    this.playerStatesPanel.repaint();
    this.gameboardPanel.updateGameBoard(this.gamestate.getGameBoard());
    this.gameboardPanel.repaint();
    this.numRefTiles.setText("Number of Tiles Left: " + this.gamestate.tilesRemaining());
  }

}


