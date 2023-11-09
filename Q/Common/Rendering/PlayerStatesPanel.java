package Common.Rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import Common.State.PlayerState;

/**
 * Renders information about all the Players. Renders a Player's score, name, and Tiles in order
 * from least to greatest.
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
        this.playerToPanel.get(p).disqualified(); // TODO disqualified doesn't show
        this.playerToPanel.get(p).repaint();
      }
    }
  }

  public BufferedImage toPng() {
    int width = 50 * 6;
    int height = this.playerToPanel.size() * (70+50 + 10);

    BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();

    int i = 0;
    for (PlayerStatePanel ps : this.playerToPanel.values()) {
      g.drawImage(ps.toPng(), 0, i * (70+50 + 10), null);
    }

    return combined;
  }

  private void paintPlayers(List<PlayerState> players) {
    for (PlayerState player : players) {
      JPanel playerPanel = new PlayerStatePanel(player);
      this.playerToPanel.put(player, new PlayerStatePanel(player));
      this.add(playerPanel);
    }
  }
}
