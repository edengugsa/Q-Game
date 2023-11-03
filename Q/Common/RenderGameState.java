import java.awt.*;

import javax.swing.*;

/**
 * Represents functionality to render a GameState. The image will display the GameBoard, the number
 * of remaining Referee tiles, as well as each player's score and Tiles.
 */
public class RenderGameState extends JFrame {

  GameState gamestate;
  GameBoardPanel gameboardPanel;
  PlayerStatesPanel playerStatesPanel;
  JLabel numRefTiles;

  public RenderGameState(GameState gamestate) {
    this.gamestate = gamestate;
    this.setSize(1500,1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new GridLayout(1,3, 20,0));
    this.setTitle("Q Game");
    this.gameboardPanel = new GameBoardPanel(gamestate.getGameBoard());
    this.playerStatesPanel = new PlayerStatesPanel(this.gamestate.players());
    this.numRefTiles = new JLabel();
    this.add(this.gameboardPanel);
    this.add(this.playerStatesPanel);
    this.add(numRefTiles);
    this.setVisible(true);
  }

  @Override
  public void paint(Graphics e) {
    this.playerStatesPanel.updatePlayers(this.gamestate.players());
    this.playerStatesPanel.repaint();
    this.gameboardPanel.updateGameBoard(this.gamestate.getGameBoard());
    this.gameboardPanel.repaint();
    this.numRefTiles.setText("Number of Tiles Left: " + this.gamestate.tilesRemaining());
  }

}


