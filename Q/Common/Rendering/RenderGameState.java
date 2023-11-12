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
  GameState gs;
  JScrollPane scrollabeGameState;
  JLabel gameStateImg;

  public RenderGameState(GameState gamestate) {
    this.gs = gs;
    this.gameStateImg = new JLabel(new ImageIcon(this.GameStateToPng(gamestate)));
    this.scrollabeGameState = new JScrollPane(this.gameStateImg);
    this.scrollabeGameState.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.scrollabeGameState.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(scrollabeGameState);
    this.setVisible(true);
  }

  public void updateGameState(GameState gs) {
    this.gameStateImg.setIcon(new ImageIcon(this.GameStateToPng(gs)));
    this.scrollabeGameState.revalidate();
    this.scrollabeGameState.repaint();
  }

  /**
   * @return an image showing this class' GameState.
   */
  public BufferedImage GameStateToPng(GameState gs) {
    BufferedImage gameBoardImg = new GameBoardPainter(gs.getGameBoard().getMap()).reveal();
    BufferedImage playerStatesImg = new PlayerStatesAndNumRefTilesPanel(gs.players(), gs.tilesRemaining()).toPng();
    BufferedImage refTilesImg = this.refTilesImg(gs);
    int height = Math.max(gameBoardImg.getHeight(), Math.max(playerStatesImg.getHeight(), refTilesImg.getHeight()));
    int width = gameBoardImg.getWidth() + playerStatesImg.getWidth() + refTilesImg.getWidth() + 20; // 20 for padding
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
  private static BufferedImage refTilesImg(GameState gs) {
    Deque<Tile> refTiles = gs.getDeck();
    Map<Coordinate, Tile> refTilesMap = new HashMap<>();
    for (int i = 0; i < Math.min(gs.tilesRemaining(), 20); i++ ) {
      refTilesMap.put(new Coordinate(i % 4, Math.floorDiv(i, 4)), refTiles.pop());
    }
    BufferedImage refTilesImg= new GameBoardPainter(refTilesMap).reveal();
    BufferedImage combined = new BufferedImage(200, refTilesImg.getHeight() + 70, BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
    g.setColor(Color.PINK);
    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
    g.drawImage(refTilesImg, 0, 70, null);
    g.setColor(Color.BLACK);
    g.drawString("Number of Tiles Left: " + gs.tilesRemaining(), 10 , 50);
    return combined;
  }

}


