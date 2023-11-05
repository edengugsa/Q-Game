package Common.Rendering;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import Common.Tiles.Coordinate;
import Common.Tiles.Tile;

/**
 * Contains the rendering functionality required for displaying a game-board as a viewable image.
 */
public class GameBoardPainter {
  private int TILE_SIZE;
  private int maxWidth;
  private int maxHeight;
  private Graphics2D graphics;
  private BufferedImage image;
  private Map<Coordinate, Tile> gameboard;

  /**
   * Instantiates this GameBoardPainter object to render a Q-Game board as a PNG image.
   * @param gameboard the game board to render
   */
  public GameBoardPainter(Map<Coordinate, Tile> gameboard, int maxWidth, int maxHeight) {
    this.maxHeight = maxHeight;
    this.maxWidth = maxWidth;
    this.gameboard = new HashMap<>(gameboard);
    this.buildCanvas();
    this.paintImage();
  }

  /**
   * @return a BufferedImage representing this GameBoardPainter's game-board.
   */
  public BufferedImage reveal() {
    return this.image;
  }

  /**
   * @param imageFormat the type of image (e.g. PNG)
   * @param pathname the output file name
   */
  public void saveImage(String imageFormat, String pathname) {
    try {
      ImageIO.write(this.reveal(), imageFormat, new File(pathname + "." + imageFormat));
    } catch (IOException e) {
      throw new RuntimeException("Could not save image.");
    }
  }

  /**
   * Creates a new BufferedImage "canvas" based on the size of this
   * GameBoardPainter's game-board.
   */
  protected void buildCanvas() {
    ArrayList<Integer> dimensions = getBoardDimensions();
    int tilesWide = (Math.abs(dimensions.get(2) - dimensions.get(0)) + 1);
    int tilesHigh = (Math.abs(dimensions.get(3) - dimensions.get(1)) + 1);

    TILE_SIZE = Math.min(maxWidth, maxHeight) / Math.max(tilesHigh, tilesWide);

    this.image = new BufferedImage(TILE_SIZE*tilesWide,
            TILE_SIZE*tilesHigh, BufferedImage.TYPE_INT_ARGB);
    this.graphics = this.image.createGraphics();
    this.graphics.setColor(Color.white);
    graphics.fillRect(0,0, TILE_SIZE*tilesWide, TILE_SIZE*tilesHigh);
  }


  /**
   *  Paints all the tiles in this GameBoardPainter's game-board on the "canvas" at their
   *  respective coordinates.
   */
  protected void paintImage() {
    ArrayList<Integer> dimensions =  this.getBoardDimensions();
    int minX = dimensions.get(0);
    int minY = dimensions.get(1);

    for (Coordinate coord : this.gameboard.keySet()) {
      // translate all negative coordinates so they are positive
      Coordinate shiftedCoord = new Coordinate(coord.col() + Math.abs(minX), coord.row() + Math.abs(minY));
      this.drawTileAtCoordinate(coord, shiftedCoord, this.gameboard.get(coord));
    }
  }

  /**
   * Draws the given tile at the given coordinate on the correct spot of the canvas.
   * @param newCoord the coordinate of the tile on the game board
   * @param tile the tile
   */
  private void drawTileAtCoordinate(Coordinate oldCoord, Coordinate newCoord, Tile tile) {
    this.graphics.setColor(tile.toColor());
    int offsetX = newCoord.col() * TILE_SIZE; // calculate the relative x-location on the canvas
    int offsetY = newCoord.row() * TILE_SIZE; // calculate the relative y-location on the canvas
    switch(tile.shape()) {
      case star -> drawStar(offsetX, offsetY);
      case circle -> drawCircle(offsetX, offsetY);
      case square -> drawSquare(offsetX, offsetY);
      case diamond -> drawDiamond(offsetX, offsetY);
      case eight_star -> drawEightStar(offsetX, offsetY);
      case clover -> drawClover(offsetX, offsetY);
    }

    // draw coordinate onto tile
    int halfTileSize = TILE_SIZE / 2;
    this.graphics.setColor(Color.BLACK);
    this.graphics.drawString(oldCoord.toStringXY(), offsetX + halfTileSize - 15, offsetY + halfTileSize);
  }

  private void drawClover(int offsetX, int offsetY) {
    Shape circleUp = new Ellipse2D.Double
            (offsetX + (TILE_SIZE /4), offsetY + (TILE_SIZE /8), TILE_SIZE /2, TILE_SIZE /2);

    Shape circleDown = new Ellipse2D.Double
            (offsetX + (TILE_SIZE /4), offsetY + (TILE_SIZE /2), TILE_SIZE /2, TILE_SIZE /2);

    Shape circleRight = new Ellipse2D.Double
            (offsetX + (TILE_SIZE /2), offsetY + (TILE_SIZE /3),  TILE_SIZE /2, TILE_SIZE /2);

    Shape circleLeft = new Ellipse2D.Double
            (offsetX, offsetY + (TILE_SIZE /3),  TILE_SIZE /2, TILE_SIZE /2);

    graphics.fill(circleUp);
    graphics.fill(circleDown);
    graphics.fill(circleRight);
    graphics.fill(circleLeft);
  }

  private void drawDiamond(int offsetX, int offsetY) {
    int[] xCoords = new int[] {
            offsetX + TILE_SIZE /2, offsetX + TILE_SIZE, offsetX + TILE_SIZE /2, offsetX
    };
    int[] yCoords = new int[] {offsetY, offsetY + TILE_SIZE /2, offsetY + TILE_SIZE, offsetY + TILE_SIZE /2};

    graphics.fillPolygon(xCoords, yCoords, 4);
  }

  private void drawStar(int offsetX, int offsetY) {
    int[] xCoords = new int[] {
            offsetX, offsetX + TILE_SIZE /2, offsetX + TILE_SIZE, offsetX + TILE_SIZE *3/4,
            offsetX + TILE_SIZE, offsetX + TILE_SIZE /2, offsetX, offsetX + TILE_SIZE /4
    };
    int[] yCoords = new int[] {
            offsetY, offsetY + TILE_SIZE /4, offsetY, offsetY + TILE_SIZE /2, offsetY + TILE_SIZE, offsetY + TILE_SIZE *3/4, offsetY + TILE_SIZE, offsetY + TILE_SIZE /2};

    graphics.fillPolygon(xCoords, yCoords, 8);
  }

  private void drawSquare(int offsetX, int offsetY) {
    graphics.fillRect(offsetX, offsetY, TILE_SIZE, TILE_SIZE);
  }

  private void drawCircle(int offsetX, int offsetY) {
    Shape circle = new Ellipse2D.Double
            (offsetX, offsetY, TILE_SIZE, TILE_SIZE);
    graphics.fill(circle);
  }

  private void drawEightStar(int offsetX, int offsetY) {
    int[] xCoords = new int[]
            {offsetX + TILE_SIZE /2, offsetX + TILE_SIZE *5/8, offsetX + TILE_SIZE *7/8, offsetX + TILE_SIZE *3/4,
                    offsetX + TILE_SIZE, offsetX + TILE_SIZE *3/4, offsetX + TILE_SIZE *7/8, offsetX + TILE_SIZE *5/8,
                    offsetX + TILE_SIZE /2, offsetX + TILE_SIZE *3/8, offsetX + TILE_SIZE /8, offsetX + TILE_SIZE /4,
                    offsetX, offsetX + TILE_SIZE /4, offsetX + TILE_SIZE /8, offsetX + TILE_SIZE *3/8
            };
    int[] yCoords = new int[]
            {offsetY, offsetY + TILE_SIZE /4, offsetY + TILE_SIZE /8, offsetY + TILE_SIZE *3/8, offsetY + TILE_SIZE /2, offsetY + TILE_SIZE *5/8,
                    offsetY + TILE_SIZE *7/8, offsetY + TILE_SIZE *3/4, offsetY + TILE_SIZE, offsetY + TILE_SIZE *3/4, offsetY + TILE_SIZE *7/8,
                    offsetY + TILE_SIZE *5/8, offsetY + TILE_SIZE /2, offsetY + TILE_SIZE *3/8, offsetY + TILE_SIZE /8, offsetY + TILE_SIZE /4
            };
    graphics.fillPolygon(xCoords, yCoords, 16);
  }

  /**
   * @return the dimensions of the game board in the following order:
   * [minX, minY, maxX, maxY]
   */
  private ArrayList<Integer> getBoardDimensions() {
    ArrayList<Integer> output = new ArrayList<Integer>();
    int minX = 0, minY = 0, maxX = 0, maxY = 0;
    for (Coordinate coordinate : this.gameboard.keySet()) {
      minX = Math.min(coordinate.col(), minX);
      minY = Math.min(coordinate.row(), minY);
      maxX = Math.max(coordinate.col(), maxX);
      maxY = Math.max(coordinate.row(), maxY);
    }
    output.add(minX);
    output.add(minY);
    output.add(maxX);
    output.add(maxY);
    return output;
  }

}
