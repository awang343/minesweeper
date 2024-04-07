import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Game {
  GUIFrame frame;
  ArrayList<difficulty> difficultyList;
  
  public Game() {
    this.difficultyList = new ArrayList<difficulty>();
    difficultyList.add(new difficulty("Baby",4,4,1));
    difficultyList.add(new difficulty("Easy",8,10,10));
    difficultyList.add(new difficulty("Medium",14,18,40));
    difficultyList.add(new difficulty("Hard",20,24,99));
    difficultyList.add(new difficulty("Insane",25,25,623));
  }

  public void runGame()
  {
    EventQueue.invokeLater(new Runnable(){
        public void run()
        {
          frame = new GUIFrame(difficultyList);
        }
    });
  }
}

class difficulty {
  private int numRow;
  private int numCol;
  private int numMines;
  private String name;

  public difficulty(String name, int numRow, int numCol, int numMines) {
    this.name = name;
    this.numRow = numRow;
    this.numCol = numCol;
    this.numMines = numMines;
  }

  public String getName() {
    return name;
  }
  public int getNR() {
    return numRow;
  }
  public int getNC() {
    return numCol;
  }
  public int getNM() {
    return numMines;
  }
}