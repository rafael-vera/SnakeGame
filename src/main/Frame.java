package main;

import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import view.PanelGame;
import view.PanelScore;

/**
 *
 * @author Rafael Vera
 */
public class Frame extends JFrame {
  private final PanelScore panelScore;
  private final PanelGame panelGame;
  
  public Frame(PanelScore panelScore, PanelGame panelGame) {
    this.panelScore = panelScore;
    this.panelGame = panelGame;
    initComponents();
    setSize(596, 592);
    setLocationRelativeTo(null);
  }
  
  private void initComponents() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Snake Game");
    setResizable(false);
    add(panelScore, "North");
    add(panelGame, "Center");
    pack();
  }
  
  public void addEvents(WindowListener listener) {
    addWindowListener(listener);
  }
}