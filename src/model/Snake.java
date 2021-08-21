package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Rafael Vera
 */
public class Snake {
  public final static byte UP = 0;
  public final static byte RIGHT = 1;
  public final static byte DOWN = 2;
  public final static byte LEFT = 3;
  
  private byte[] x;
  private byte[] y;
  private short length;
  private byte orientation;
  private final byte width;
  private final Color head;
  private final Color body;
  
  public Snake(byte orientation, byte width, short size, Color head, Color body) {
    this.orientation = orientation;
    this.x = new byte[size];
    this.y = new byte[size];
    this.width = width;
    this.head = head;
    this.body = body;
    initSnake();
  }
  
  public Snake(byte orientation, byte width) {
    this(orientation, width, (short) 725, Color.LIGHT_GRAY, Color.WHITE);
  }
  
  private void initSnake() {
    addNode((byte) 12, (byte) 10);
    addNode((byte) 11, (byte) 10);
    addNode((byte) 10, (byte) 10);
  }
  
  public void draw(Graphics2D g2) {
    int xI, yI;
    for(short i=0; i<length; i++) {
      xI = x[i] * width;
      yI = y[i] * width;
      
      g2.setColor(
        i == 0 ? head : body
      );
      g2.fillRect(xI, yI, width, width);
      g2.setColor(Color.DARK_GRAY);
      g2.setStroke(new BasicStroke(2));
      g2.drawRect(xI, yI, width, width);
      g2.setStroke(new BasicStroke(1));
    }
  }
  
  private void addNode(byte xP, byte yP) {
    x[length] = xP;
    y[length] = yP;
    length++;
  }
  
  public byte getHeadX() {
    return x[0];
  }
  
  public byte getHeadY() {
    return y[0];
  }
  
  public void advance(boolean eat) {
    byte xP = x[length-1];
    byte yP = y[length-1];
    
    for(short i = (short) (length-1); i > 0; i--) {
      x[i] = x[i-1];
      y[i] = y[i-1];
    }
    
    if(orientation == UP) {
      y[0]--;
    } else if(orientation == DOWN) {
      y[0]++;
    } else if(orientation == LEFT) {
      x[0]--;
    } else if(orientation == RIGHT) {
      x[0]++;
    }
    
    if(eat) {
      addNode(xP, yP);
    }
  }
  
  public byte getOrientation() {
    return this.orientation;
  }
  
  public void setOrientation(byte orientation) {
    this.orientation = orientation;
  }
  
  public boolean isEatingItself() {
    boolean eatItself = false;
    for(short i=1; i<length; i++) {
      if(x[0] == x[i] && y[0] == y[i]) {
        eatItself = true;
        break;
      }
    }
    return eatItself;
  }
  
  public boolean isFree(byte xP, byte yP) {
    boolean free = true;
    for(short i = 0; i<length; i++) {
      if(x[i] == xP && y[i] == yP) {
        free = false;
        break;
      }
    }
    return free;
  }
}