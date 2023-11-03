import java.awt.*;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

/**
 * Renders a Player's name, score, and the Tiles they have.
 */
public class PlayerStatePanel extends JPanel {

  private PlayerState playerState;
  private JLabel nameScoreTilesRemaining;
  private GameBoardPanel tiles;
  private boolean disqualified;

  public PlayerStatePanel(PlayerState playerState) {
    this.playerState = playerState;
    this.setLayout(new GridLayout(2, 1, 0, 0));
    nameScoreTilesRemaining = new JLabel();
    tiles = new GameBoardPanel(this.createGameBoardFromTiles(this.playerState.getHand()));
    this.add(nameScoreTilesRemaining);
    this.add(tiles);
    this.disqualified = false;
    this.setVisible(true);
  }

  public void disqualified() {
    this.disqualified = true;
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (!disqualified) {
      this.nameScoreTilesRemaining.setText("Name: " + playerState.name() + "   Score: " + playerState.score() +
              "   Tiles Remaining: " + playerState.tilesRemaining());
      this.tiles.updateGameBoard(this.createGameBoardFromTiles(playerState.getHand()));
      this.tiles.repaint();
    } else {
      this.nameScoreTilesRemaining.setText("DISQUALIFIED");
    }
  }

  /**
   * Creates a GameBoard from a list of Tiles. The Tiles will be in a row starting from (0,0)
   * and go in the right direction.
   */
  private GameBoard createGameBoardFromTiles(List<Tile> tiles) {
    HashMap<Coordinate, Tile> tilesHashmap = new HashMap<Coordinate,Tile>();
    for (int i = 0; i < tiles.size(); i++) {
      tilesHashmap.put(new Coordinate(i, 0), tiles.get(i));
    }
    return new GameBoard(tilesHashmap);
  }
}
