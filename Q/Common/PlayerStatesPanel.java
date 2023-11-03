import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

/**
 * Renders information about all the Players. Renders a Player's score, name, and Tiles.
 */
public class PlayerStatesPanel extends JPanel {
  private Map<PlayerState, PlayerStatePanel> playerToPanel;
  List<PlayerState> activePlayers;

  PlayerStatesPanel(List<PlayerState> players) {
    this.playerToPanel = new HashMap<>();
    paintPlayers(players);
    int numRows = players.size();
    this.setLayout(new GridLayout(numRows, 1, 10, 0));
  }

  /**
   * @param players still in the game
   */
  public void updatePlayers(List<PlayerState> players) {
    this.activePlayers = players;
  }

  @Override
  public void paintComponent(Graphics g) {
    for (PlayerState p : this.playerToPanel.keySet()) {
      if (!this.activePlayers.contains(p)) {
        this.playerToPanel.get(p).disqualified();
        this.playerToPanel.get(p).repaint();
      }
    }
  }

  private void paintPlayers(List<PlayerState> players) {
    for (PlayerState player : players) {
      JPanel playerPanel = new PlayerStatePanel(player);
      this.playerToPanel.put(player, new PlayerStatePanel(player));
      this.add(playerPanel);
    }
  }
}
