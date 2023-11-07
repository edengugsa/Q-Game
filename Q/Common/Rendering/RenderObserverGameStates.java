package Common.Rendering;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Referee.observer;

import javax.swing.*;

import Common.State.GameState;
import Referee.observer;

/**
 * Renders a component that lets an Observer browse through GameStates and save a
 * GameState as a json file.
 */
public class RenderObserverGameStates extends JFrame {
 RenderObserverButtons buttons;
 observer observer;
 RenderGameState renderGameState;


  public RenderObserverGameStates(observer observer) {
    this.observer = observer;
    this.setSize(1500,2000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new GridLayout(2,1, 20,0));
    this.setTitle("Observer");

    this.buttons = new RenderObserverButtons();
    this.add(this.buttons);
    this.buttons.setVisible(true);
    this.renderGameState = new RenderGameState(observer.getCurrentGameState());
    this.renderGameState.setVisible(true);
  }

  public void renderNextGameState() {
    try {
      GameState gs =  observer.next();
      renderGameState = new RenderGameState(gs);
      this.repaint();
    }
    catch (Exception e) {
      this.noNextOrPrev(e.getMessage());
    }
  }

  public void notifyGameOver() {
//    JOptionPane.showMessageDialog(this, "Game is over",
//            "Game over", JOptionPane.INFORMATION_MESSAGE);
  }

  private void noNextOrPrev(String msg) {
    JOptionPane.showMessageDialog(this, msg,
            "Observer Button Malfunction", JOptionPane.ERROR_MESSAGE);
  }

  public void renderPrevGameState() {
    try {
      GameState gs =  observer.previous();
      renderGameState = new RenderGameState(gs);
      this.repaint();
    }
    catch (Exception e) {
      this.noNextOrPrev(e.getMessage());
    }
  }

  public void saveGameState() {
    throw new UnsupportedOperationException("what");
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
      this.setLayout(new GridLayout(3,1));
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
          saveGameState();
        }
      });
    }
  }
}
