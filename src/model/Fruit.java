package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Rafael Vera
 */
public class Fruit {
  private byte x;
  private byte y;
  private final byte width;
  private final Color color;
  
  public Fruit(byte x, byte y, byte width, Color color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.color = color;
  }
  
  public void draw(Graphics2D g2) {
    g2.setColor(color);
    g2.fillOval(x * width, y * width, width, width);
    g2.setColor(Color.DARK_GRAY);
    g2.setStroke(new BasicStroke(2));
    g2.drawOval(x, y, width, width);
    g2.setStroke(new BasicStroke(1));
  }
  
  public void setLocation(byte x, byte y) {
    this.x = x;
    this.y = y;
  }
  
  public byte getX() {
    return x;
  }
  
  public byte getY() {
    return y;
  }
}