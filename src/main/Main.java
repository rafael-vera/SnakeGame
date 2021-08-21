package main;

import controller.GameListener;
import view.PanelGame;
import view.PanelScore;

/**
 *
 * @author Rafael Vera
 */
public class Main {
  public static void main(String[] args) {
    PanelScore panelScore = new PanelScore();
    PanelGame panelGame = new PanelGame((byte) 29, (byte) 25);
    Frame frame = new Frame(panelScore, panelGame);
    GameListener listener = new GameListener(panelScore, panelGame);
    panelGame.addEvents(listener);
    frame.setVisible(true);
  }
}