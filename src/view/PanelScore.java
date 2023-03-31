package view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Rafael Vera
 */
public class PanelScore extends JPanel {
  private final JLabel labelScoreTxt;
  private final JLabel labelScore;
  
  private short score;
  
  public PanelScore() {
    this.labelScoreTxt = new JLabel();
    this.labelScore = new JLabel();
    this.score = 0;
    initComponents();
  }
  
  private void initComponents() {
    setBackground(Color.BLACK);
    
    Font font = new Font("Consolas", 0, 36);
    
    labelScoreTxt.setFont(font);
    labelScoreTxt.setForeground(Color.WHITE);
    labelScoreTxt.setText("Score:");
    add(labelScoreTxt);
    
    labelScore.setFont(font);
    labelScore.setForeground(Color.WHITE);
    labelScore.setText(String.valueOf(score));
    add(labelScore);
  }
  
  public void increaseScore(short points) {
    this.score += points;
    this.labelScore.setText(String.valueOf(score));
  }
  
  public void restartScore() {
    this.score = 0;
    this.labelScore.setText(String.valueOf(score));
  }
}