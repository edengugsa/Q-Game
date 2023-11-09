package Common.Rendering;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Common.JsonUtils;
import Referee.observer;
import javax.swing.*;

import Common.State.GameState;

/**
 * Renders a component that lets an Observer browse through GameStates and save a
 * GameState as a json file.
 */
public class RenderObserverGameStates extends JFrame {
 RenderObserverButtons buttons;
 observer observer;
 RenderGameState renderGameState; // this should take in an image that shows the entire image or its a scrollable JPane ok
  // is the image just the board or the whole state?
  // the whole state. we just gotta combine all three jpanels in renderGameState into an image
  // board, player states, ref tiles
  // 3? the board, players ok wait or we can turn the jpanel into an image. this is a quick fix tho
  // w


  public RenderObserverGameStates(observer observer) {
    this.observer = observer;
    this.setSize(1500,2000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new GridLayout(2,3, 20,0));

    this.setTitle("Observer");

    this.buttons = new RenderObserverButtons();
    this.add(this.buttons);
    this.renderGameState = new RenderGameState(observer.getCurrentGameState());
    this.add(this.renderGameState);
    this.renderGameState.setVisible(true);
  }

  public void renderNextGameState() {
    try {
      this.renderGameState(observer.next());
    }
    catch (Exception e) {
      this.noNextOrPrev(e.getMessage());
    }
  }

  public void renderPrevGameState() {
    try {
      this.renderGameState(observer.previous());
    }
    catch (Exception e) {
      this.noNextOrPrev(e.getMessage());
    }
  }

  private void renderGameState(GameState gs) {
    this.remove(renderGameState);
    renderGameState = new RenderGameState(gs);
    this.add(renderGameState);
    this.renderGameState.setVisible(true);
    this.repaint();
  }

  public void notifyGameOver() {
    JOptionPane.showMessageDialog(this, "Game is over",
            "Game over", JOptionPane.INFORMATION_MESSAGE);
  }

  private void noNextOrPrev(String msg) {
    JOptionPane.showMessageDialog(this, msg,
            "Observer Button Malfunction", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Saves the current GameState as a Json at the given path.
   */
  public void saveGameState() throws IOException {
    // ask the user for the file name?
    String fileName = JOptionPane.showInputDialog("Enter a file name to save the game state to");
    observer.save(fileName);
  }


  /**
   * Represents three buttons that an Observer can use to browse through
   * GameStates and save a GameState.
   */
  class RenderObserverButtons extends JPanel{
    private JButton next;
    private JButton prev;
    private JButton save;

    public RenderObserverButtons() {
//      GridLayout gl = new GridLayout(1, 3);
      this.setLayout(new GridLayout(1,3));

      this.next = new JButton("next");
      this.prev = new JButton("prev");
      this.save = new JButton("save");

      this.next.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          renderNextGameState();
        }
      });
      this.prev.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          renderPrevGameState();
        }
      });
      this.save.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          try {
            saveGameState();
          }
          catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        }
      });

      this.prev.setBounds(20, 20, 100, 30);
      this.next.setBounds(140, 20, 100, 30);
      this.save.setBounds(260, 20, 100, 30);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setSize(1000, 10);
      buttonPanel.add(this.prev);
      buttonPanel.add(this.next);
      buttonPanel.add(this.save); // TODO fix formating
      this.add(buttonPanel);
      this.setSize(1000,40);
      this.setVisible(true);

    }
  }
}
