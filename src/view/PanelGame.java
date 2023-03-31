package view;

import controller.GameListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import javax.swing.JPanel;
import model.Fruit;
import model.Snake;

/**
 *
 * @author Rafael Vera
 */
public class PanelGame extends JPanel {
  public final static byte WAITING = 0;
  public final static byte PLAYING = 1;
  public final static byte PAUSE = 2;
  public final static byte GAME_OVER = 3;
  
  private final Snake snake;
  private final Fruit fruit;
  private final byte maxHorizontalCells;
  private final byte maxVerticalCells;
  
  private byte gameStatus;
  
  public PanelGame(byte maxHorizontalCells, byte maxVerticalCells) {
    this.snake = new Snake(Snake.RIGHT, (byte) 20);
    this.fruit = new Fruit((byte) 0, (byte) 0, (byte) 20, Color.GREEN);
    this.maxHorizontalCells = maxHorizontalCells;
    this.maxVerticalCells = maxVerticalCells;
    this.gameStatus = WAITING;
    initComponents();
  }
  
  private void initComponents() {
    this.setBackground(Color.DARK_GRAY);
    updateFruit();
  }
  
  public void addEvents(GameListener listener) {
    this.setFocusable(true);
    this.addKeyListener(listener);
  }
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    fruit.draw(g2);
    snake.draw(g2);
    if(gameStatus != PLAYING) {
      g2.setColor(Color.RED);
      g2.setFont(new Font("Consolas", 3, 50));
      if(gameStatus == PAUSE) {
        g2.drawString("PAUSE", 225, 250);
      } else if(gameStatus == GAME_OVER) {
        g2.drawString("GAME OVER", 175, 250);
      }
    }
  }
  
  public void updateFruit() {
    byte x;
    byte y;
    while(true) {
      x = (byte) (Math.random() * 29);
      y = (byte) (Math.random() * 25);
      if(snake.isFree(x, y)) {
        fruit.setLocation(x, y);
        break;
      }
    }
  }
  
  public Fruit getFruit() {
    return this.fruit;
  }
  
  public Snake getSnake() {
    return this.snake;
  }
  
  public byte getMaxHorizontalCells() {
    return maxHorizontalCells;
  }
  
  public byte getMaxVerticalCells() {
    return maxVerticalCells;
  }
  
  public void setGameStatus(byte gameStatus) {
    this.gameStatus = gameStatus;
  }
  
  public byte getGameStatus() {
    return gameStatus;
  }
}