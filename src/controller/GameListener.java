package controller;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import model.Fruit;
import model.Snake;
import view.PanelGame;
import view.PanelScore;

/**
 *
 * @author Rafael Vera
 */
public class GameListener extends WindowAdapter implements KeyListener {
  private final PanelScore panelScore;
  private final PanelGame panelGame;
  private final Snake snake;
  private final Fruit fruit;
  private ThreadGame thread;
  private final Queue<Byte> moves;
  private Clip background;
  private Clip eating;
  private boolean mute;
  
  public GameListener(PanelScore panelScore, PanelGame panelGame) {
    this.panelScore = panelScore;
    this.panelGame = panelGame;
    this.snake = panelGame.getSnake();
    this.fruit = panelGame.getFruit();
    this.thread = new ThreadGame();
    this.moves = new LinkedList<>();
    this.mute = false;
    loadAudio();
  }
  
  private void loadAudio() {
    try {
      background = AudioSystem.getClip();
      eating = AudioSystem.getClip();
      background.open(
        AudioSystem.getAudioInputStream(
          new BufferedInputStream(
            getClass()
              .getResourceAsStream("/audio/Background-8-bit.wav")
          )
        )
      );
      eating.open(
        AudioSystem.getAudioInputStream(
          new BufferedInputStream(
            getClass()
              .getResourceAsStream("/audio/eat-sfx.wav")
          )
        )
      );
    } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
      System.err.println("Error: "+ex.getMessage());
      System.exit(-1);
    }
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_UP ->  {
        changeSnakeOrientation(Snake.UP);
      }
      case KeyEvent.VK_DOWN ->  {
        changeSnakeOrientation(Snake.DOWN);
      }
      case KeyEvent.VK_RIGHT ->  {
        changeSnakeOrientation(Snake.RIGHT);
      }
      case KeyEvent.VK_LEFT ->  {
        changeSnakeOrientation(Snake.LEFT);
      }
      case KeyEvent.VK_ESCAPE ->  {
        if(panelGame.getGameStatus() == PanelGame.PLAYING) {
          panelGame.setGameStatus(PanelGame.PAUSE);
        } else if(panelGame.getGameStatus() == PanelGame.PAUSE) {
          thread = new ThreadGame();
          startGame();
        }
      }
      case KeyEvent.VK_M ->  {
        mute = !mute;
        if(mute) {
          background.stop();
        } else {
          startBackground();
        }
      }
      case KeyEvent.VK_SPACE ->  {
        restartGame();
      }
    }
  }
  
  private void changeSnakeOrientation(byte newOrientation) {
    if(panelGame.getGameStatus() == PanelGame.PLAYING
      || panelGame.getGameStatus() == PanelGame.WAITING) {
      if(!moves.isEmpty()) {
        updateOrientation(moves.element(), newOrientation);
      } else {
        updateOrientation(snake.getOrientation(), newOrientation);
      }
    }
    startGame();
  }
  
  private void updateOrientation(byte oldOrientation, byte newOrientation) {
    if(newOrientation == Snake.UP || newOrientation == Snake.DOWN) {
      if(oldOrientation == Snake.RIGHT || oldOrientation == Snake.LEFT) {
        moves.add(newOrientation);
      }
    } else {
      if(oldOrientation == Snake.UP || oldOrientation == Snake.DOWN) {
        moves.add(newOrientation);
      }
    }
  }
  
  private void startGame() {
    if(thread.getState().equals(Thread.State.NEW)) {
      panelGame.setGameStatus(PanelGame.PLAYING);
      startBackground();
      thread.start();
    }
  }
  
  private void startBackground() {
    if(!background.isRunning() && !mute) {
      background.loop(Clip.LOOP_CONTINUOUSLY);
    }
  }
  
  private void startEating() {
    if(!eating.isRunning() && !mute) {
      eating.setFramePosition(0);
      eating.start();
    }
  }
  
  private void restartGame() {
    if(thread.getState().equals(Thread.State.TERMINATED) || panelGame.getGameStatus() == PanelGame.PAUSE) {
      panelGame.setGameStatus(PanelGame.WAITING);
      thread = new ThreadGame();
      panelScore.restartScore();
      snake.restartSnake();
      snake.setOrientation(Snake.RIGHT);
      panelGame.updateFruit();
      panelGame.repaint();
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
  @Override
  public void keyReleased(KeyEvent e) {
  }
  
  @Override
  public void windowClosing(WindowEvent e) {
    background.close();
    eating.close();
    super.windowClosing(e);
  }
  
  private class ThreadGame extends Thread {
    @Override
    public void run() {
      while(panelGame.getGameStatus() == PanelGame.PLAYING) {
        byte x = snake.getHeadX();
        byte y = snake.getHeadY();
        
        if(!moves.isEmpty()) {
          snake.setOrientation(moves.poll());
        }
        
        if(x >= 0 && x < panelGame.getMaxHorizontalCells()
          && y >= 0 && y < panelGame.getMaxVerticalCells()) {
          
          if(x == fruit.getX() && y == fruit.getY()) {
            snake.advance(true);
            startEating();
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
      if(background.isRunning()) {
        background.stop();
      }
      if(panelGame.getGameStatus() == PanelGame.GAME_OVER) {
        background.setFramePosition(0);
      }
    }
  }
}