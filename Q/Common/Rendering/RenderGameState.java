package Common.Rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.swing.*;

import Common.GameBoard.GameBoard;
import Common.State.GameState;
import Common.State.PlayerState;
import Common.Tiles.Coordinate;
import Common.Tiles.Tile;

/**
 * Represents functionality to render a State. The image will display the GameBoard, the number
 * of remaining Referee tiles, as well as each client's score and Tiles.
 */
public class RenderGameState extends JPanel {
  JScrollPane scrollableGameState;
  JLabel gameStateImg;
  private int PADDING = 20;
  private int TILE_SIZE = 50;
  private int REF_TILES_PER_ROW = 4;
  private int TILES_PER_PLAYER = 6;
  private static int TEXT_HEIGHT = 70;

  public RenderGameState(GameState gamestate) {
    this.gameStateImg = new JLabel(new ImageIcon(this.GameStateToImage(gamestate)));
    this.scrollableGameState = new JScrollPane(this.gameStateImg);
    this.scrollableGameState.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    this.scrollableGameState.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(scrollableGameState);
    this.setVisible(true);
  }

  public void updateGameState(GameState gs) {
    this.gameStateImg.setIcon(new ImageIcon(this.GameStateToImage(gs)));
    this.scrollableGameState.revalidate();
    this.scrollableGameState.repaint();
  }

  /**
   * @return an image showing this class' GameState.
   */
  public BufferedImage GameStateToImage(GameState gs) {
    BufferedImage gameBoardImg = new GameBoardPainter(gs.getGameBoard().getMap()).reveal();
    BufferedImage playerStatesImg = ListOfPlayerStatestoImage(gs.players());
    BufferedImage refTilesImg = this.refTilesImg(gs);
    int height = Math.max(gameBoardImg.getHeight(), Math.max(playerStatesImg.getHeight(), refTilesImg.getHeight()));
    int width = gameBoardImg.getWidth() + playerStatesImg.getWidth() + refTilesImg.getWidth() + PADDING; // 20 for padding
    BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
    g.setColor(Color.PINK);
    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
    g.drawImage(gameBoardImg, 0, 0, null);
    g.drawImage(playerStatesImg, gameBoardImg.getWidth() + PADDING/2, 0, null);
    g.drawImage(refTilesImg, gameBoardImg.getWidth() + PADDING + playerStatesImg.getWidth(), 0, null );
    return combined;
  }

  private BufferedImage ListOfPlayerStatestoImage(List<PlayerState> playerStates) {
    int width = TILE_SIZE * 6;
    int height = playerStates.size() * (TILE_SIZE + 4 * PADDING);

    BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
    g.setColor(Color.PINK);
    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());

    int i = 0;
    for (PlayerState ps : playerStates) {
      g.drawImage(PlayerStateToImage(ps), 0, i * (TEXT_HEIGHT + PADDING/2 + TILE_SIZE), null);
      i++;
    }

    return combined;
  }


  /**
   * Renders this PlayerState as an image. It displays the client's name, score, tiles remaining,
   * and their tiles.
   */
  private BufferedImage PlayerStateToImage(PlayerState ps) {
    BufferedImage playerTilesImage = new GameBoardPainter(this.createGameBoardFromTiles(ps.getHand()).getMap()).reveal();
    BufferedImage combined = new BufferedImage(TILE_SIZE*TILES_PER_PLAYER+PADDING, playerTilesImage.getHeight() + TEXT_HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
    g.setColor(Color.PINK);
    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
    g.setColor(Color.BLACK);
    g.drawString("Name: " + ps.name() + "   Score: " + ps.score() +
            "   Tiles Remaining: " + ps.tilesRemaining(), 10, 50);
    g.drawImage(playerTilesImage, 0, TEXT_HEIGHT, null);
    return combined;
  }


  /**
   * @return an image showing the number of ref tiles remaining as well as a preview of the first 20 ref tiles
   */
  private BufferedImage refTilesImg(GameState gs) {
    Deque<Tile> refTiles = gs.getDeck();
    Map<Coordinate, Tile> refTilesMap = new HashMap<>();
    for (int i = 0; i < Math.min(gs.tilesRemaining(), PADDING); i++ ) {
      refTilesMap.put(new Coordinate(i % REF_TILES_PER_ROW, Math.floorDiv(i, REF_TILES_PER_ROW)), refTiles.pop());
    }
    BufferedImage refTilesImg= new GameBoardPainter(refTilesMap).reveal();
    BufferedImage combined = new BufferedImage(REF_TILES_PER_ROW * TILE_SIZE, refTilesImg.getHeight() + TILE_SIZE + PADDING, BufferedImage.TYPE_INT_RGB);
    Graphics g = combined.getGraphics();
    g.setColor(Color.PINK);
    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
    g.drawImage(refTilesImg, 0, TILE_SIZE + PADDING, null);
    g.setColor(Color.BLACK);
    g.drawString("Number of Tiles Left: " + gs.tilesRemaining(), PADDING/2 , TILE_SIZE);
    return combined;
  }

  /**
   * Creates a GameBoard from a list of Tiles. The Tiles will be in a row starting from (0,0)
   * and go in the right direction.
   */
  private GameBoard createGameBoardFromTiles(List<Tile> tiles) {
    tiles.sort(Tile.TileComparator);
    HashMap<Coordinate, Tile> tilesHashmap = new HashMap<Coordinate,Tile>();
    for (int i = 0; i < tiles.size(); i++) {
      tilesHashmap.put(new Coordinate(i, 0), tiles.get(i));
    }
    return new GameBoard(tilesHashmap);
  }

}


