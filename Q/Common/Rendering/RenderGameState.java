package Common.Rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import Common.State.GameState;
import Common.Tiles.Coordinate;
import Common.Tiles.Tile;

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
    BufferedImage refTilesImg = this.refTilesImg();
    int height = Math.max(gameBoardImg.getHeight(), Math.max(playerStatesImg.getHeight(), refTilesImg.getHeight()));
    int width = gameBoardImg.getWidth() + playerStatesImg.getWidth() + refTilesImg().getWidth() + 20; // 20 for padding
    BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
    g.setColor(Color.PINK);
    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
    g.drawImage(gameBoardImg, 0, 0, null);
    g.drawImage(playerStatesImg, gameBoardImg.getWidth() + 10, 0, null);
    g.drawImage(refTilesImg, gameBoardImg.getWidth() + 20 + playerStatesImg.getWidth(), 0, null );
    return combined;
  }

  /**
   * @return an image showing the number of ref tiles remaining as well as a preview of the first 20 ref tiles
   */
  private BufferedImage refTilesImg() {
    Deque<Tile> refTiles = this.gamestate.getDeck();
    Map<Coordinate, Tile> refTilesMap = new HashMap<>();
    for (int i = 0; i < Math.min(this.gamestate.tilesRemaining(), 20); i++ ) {
      refTilesMap.put(new Coordinate(i % 4, Math.floorDiv(i, 4)), refTiles.pop());
    }
    BufferedImage refTilesImg= new GameBoardPainter(refTilesMap).reveal();
    BufferedImage combined = new BufferedImage(200, refTilesImg.getHeight() + 70, BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
    g.setColor(Color.PINK);
    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
    g.drawImage(refTilesImg, 0, 70, null);
    g.setColor(Color.BLACK);
    g.drawString("Number of Tiles Left: " + this.gamestate.tilesRemaining(), 10 , 50);
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


