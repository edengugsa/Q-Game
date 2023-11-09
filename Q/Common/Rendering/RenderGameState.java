package Common.Rendering;

import java.awt.*;

import javax.swing.*;

import Common.State.GameState;

/**
 * Represents functionality to render a State. The image will display the GameBoard, the number
 * of remaining Referee tiles, as well as each player's score and Tiles.
 */
public class RenderGameState extends JPanel {
  GameState gamestate;
  GameBoardPanel gameboardPanel; // TODO Make this a scrollable panel
  PlayerStatesPanel playerStatesPanel;
  JLabel numRefTiles;

  public RenderGameState(GameState gamestate) {
    this.gamestate = gamestate;
    this.setSize(1500,1000);
    this.setLayout(new GridLayout(1,3, 20,0));
    this.gameboardPanel = new GameBoardPanel(gamestate.getGameBoard());
    this.playerStatesPanel = new PlayerStatesPanel(this.gamestate.players());
    this.numRefTiles = new JLabel();
    this.add(this.gameboardPanel);
    this.add(this.playerStatesPanel);
    this.add(numRefTiles);
    this.setVisible(true);
  }

  public void updateGameState(GameState gs) {
    this.gamestate = gs;
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


