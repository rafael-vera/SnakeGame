package controller;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import model.Fruit;
import model.Snake;
import view.PanelGame;
import view.PanelScore;

/**
 *
 * @author Rafael Vera
 */
public class GameListener extends KeyAdapter {
  private final PanelScore panelScore;
  private final PanelGame panelGame;
  private final Snake snake;
  private final Fruit fruit;
  private ThreadGame thread;
  
  public GameListener(PanelScore panelScore, PanelGame panelGame) {
    this.panelScore = panelScore;
    this.panelGame = panelGame;
    this.snake = panelGame.getSnake();
    this.fruit = panelGame.getFruit();
    this.thread = new ThreadGame();
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_UP ->  {
        if(snake.getOrientation() == Snake.RIGHT || snake.getOrientation() == Snake.LEFT) {
          snake.setOrientation(Snake.UP);
        }
      }
      case KeyEvent.VK_DOWN ->  {
        if(snake.getOrientation() == Snake.RIGHT || snake.getOrientation() == Snake.LEFT) {
          snake.setOrientation(Snake.DOWN);
        }
      }
      case KeyEvent.VK_RIGHT ->  {
        if(snake.getOrientation() == Snake.UP || snake.getOrientation() == Snake.DOWN) {
          snake.setOrientation(Snake.RIGHT);
        }
      }
      case KeyEvent.VK_LEFT ->  {
        if(snake.getOrientation() == Snake.UP || snake.getOrientation() == Snake.DOWN) {
          snake.setOrientation(Snake.LEFT);
        }
      }
      case KeyEvent.VK_ESCAPE ->  {
        if(panelGame.getGameStatus() == PanelGame.PLAYING) {
          panelGame.setGameStatus(PanelGame.PAUSE);
        } else if(panelGame.getGameStatus() == PanelGame.PAUSE) {
          panelGame.setGameStatus(PanelGame.PLAYING);
          thread = new ThreadGame();
        }
        break;
      }
    }
    if(panelGame.getGameStatus() == PanelGame.WAITING) {
      panelGame.setGameStatus(PanelGame.PLAYING);
    }
    if(thread.getState().equals(Thread.State.NEW)) {
      thread.start();
    }
  }
  
  private class ThreadGame extends Thread {
    @Override
    public void run() {
      while(panelGame.getGameStatus() == PanelGame.PLAYING) {
        byte x = snake.getHeadX();
        byte y = snake.getHeadY();
        
        if(x >= 0 && x < panelGame.getMaxHorizontalCells()
          && y >= 0 && y < panelGame.getMaxVerticalCells()) {
          
          if(x == fruit.getX() && y == fruit.getY()) {
            snake.advance(true);
            panelGame.updateFruit();
            panelScore.increaseScore((short) 25);
          } else if(snake.isEatingItself()) {
            panelGame.setGameStatus(PanelGame.GAME_OVER);
          } else {
            snake.advance(false);
          }
          
        } else {
          panelGame.setGameStatus(PanelGame.GAME_OVER);
        }
        
        try {
          sleep(100);
        } catch (InterruptedException ex) {
          System.err.println("Error: "+ex.getMessage());
          System.exit(-1);
        }
        
        panelGame.repaint();
        Toolkit.getDefaultToolkit().sync();
      }
    }
  }
}