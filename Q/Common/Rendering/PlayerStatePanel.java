//package Common.Rendering;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.util.HashMap;
//import java.util.List;
//
//import javax.swing.*;
//
//import Common.GameBoard.GameBoard;
//import Common.State.PlayerState;
//import Common.Tiles.Coordinate;
//import Common.Tiles.Tile;
//
///**
// * Renders a Player's name, score, and the Tiles they have.
// */
//public class PlayerStatePanel extends JPanel {
//
//  private PlayerState playerState;
//  private JLabel nameScoreTilesRemaining;
//  private GameBoardPanel tiles;
//  private boolean disqualified;
//
//  private static int WIDTH = 400;
//  private static int HEIGHT = 400;
//  private static int TEXT_HEIGHT = 70;
//
//  public PlayerStatePanel(PlayerState playerState) {
//    this.playerState = playerState;
//    this.setLayout(new GridLayout(2, 1, 0, 0));
//    nameScoreTilesRemaining = new JLabel();
//    this.tiles = new GameBoardPanel(this.createGameBoardFromTiles(this.playerState.getHand()), WIDTH, HEIGHT);
//    this.add(nameScoreTilesRemaining);
//    this.add(tiles);
//    this.disqualified = false;
//    this.setVisible(true);
//  }
//
//  public void disqualified() {
//    this.disqualified = true;
//  }
//
//  /**
//   * Renders this PlayerState as an image. It displays the player's name, score, tiles remaining,
//   * and their tiles.
//   */
//  public BufferedImage toPng() {
//    BufferedImage playerTilesImage = new GameBoardPainter(this.createGameBoardFromTiles(this.playerState.getHand()).getMap()).reveal();
//    BufferedImage combined = new BufferedImage(50*6+20, playerTilesImage.getHeight() + TEXT_HEIGHT, BufferedImage.TYPE_INT_RGB);
//    Graphics g = combined.getGraphics();
//    g.setColor(Color.PINK);
//    g.fillRect(0, 0, combined.getWidth(), combined.getHeight());
//    g.setColor(Color.BLACK);
//    g.drawString("Name: " + playerState.name() + "   Score: " + playerState.score() +
//            "   Tiles Remaining: " + playerState.tilesRemaining(), 10, 50);
//    g.drawImage(playerTilesImage, 0, TEXT_HEIGHT, null);
//    return combined;
//  }
//
//  @Override
//  protected void paintComponent(Graphics g) {
//    if (!disqualified) {
//      this.nameScoreTilesRemaining.setText("Name: " + playerState.name() + "   Score: " + playerState.score() +
//              "   Tiles Remaining: " + playerState.tilesRemaining());
//      this.tiles.updateGameBoard(this.createGameBoardFromTiles(playerState.getHand()));
//      this.tiles.repaint();
//    } else {
//      this.nameScoreTilesRemaining.setText("DISQUALIFIED");
//    }
//  }
//
//  /**
//   * Creates a GameBoard from a list of Tiles. The Tiles will be in a row starting from (0,0)
//   * and go in the right direction.
//   */
//  private GameBoard createGameBoardFromTiles(List<Tile> tiles) {
//    tiles.sort(Tile.TileComparator);
//    HashMap<Coordinate, Tile> tilesHashmap = new HashMap<Coordinate,Tile>();
//    for (int i = 0; i < tiles.size(); i++) {
//      tilesHashmap.put(new Coordinate(i, 0), tiles.get(i));
//    }
//    return new GameBoard(tilesHashmap);
//  }
//
//
//}
