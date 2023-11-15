package Common.Rendering;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
 RenderGameState renderGameState;

  public RenderObserverGameStates(observer observer) {
    this.observer = observer;

    this.setSize(1500,2000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Observer");
    this.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    this.buttons = new RenderObserverButtons();
    c.weightx = 0.5;
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    this.add(this.buttons, c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.0;
    c.gridx = 0;
    c.gridy = 1;

    this.renderGameState = new RenderGameState(this.observer.getCurrentGameState());
    this.add(this.renderGameState, c);
  }

  private void addGameStateImg(GameState gs) {
    this.renderGameState.updateGameState(gs);
    this.renderGameState.repaint();
    this.repaint();
  }


  public void renderNextGameState() {
    try {
      this.addGameStateImg(observer.next());
    }
    catch (Exception e) {
      this.noNextOrPrev(e.getMessage());
    }
  }

  public void renderPrevGameState() {
    try {
      this.addGameStateImg(observer.previous());
    }
    catch (Exception e) {
      this.noNextOrPrev(e.getMessage());
    }
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

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    int option = fileChooser.showSaveDialog(this);
    if(option == JFileChooser.APPROVE_OPTION){
      observer.save(fileChooser.getSelectedFile().getName());
//      File file = fileChooser.getSelectedFile();
    }

//    String fileName = JOptionPane.showInputDialog("Enter a file name to save the game state to");
//    observer.save(fileName);
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

//      this.prev.setBounds(20, 20, 100, 30);
//      this.next.setBounds(140, 20, 100, 30);
//      this.save.setBounds(260, 20, 100, 30);
      JPanel buttonPanel = new JPanel();
//      buttonPanel.setSize(1000, 10);
      buttonPanel.add(this.prev);
      buttonPanel.add(this.next);
      buttonPanel.add(this.save);
      this.add(buttonPanel);
//      this.setSize(1000,40);
      this.setVisible(true);
    }
  }
}
