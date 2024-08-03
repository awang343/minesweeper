import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Game {
  GUIFrame frame;
  ArrayList<Difficulty> difficultyList;
  
  public Game() {
    this.difficultyList = new ArrayList<Difficulty>();
    difficultyList.add(new Difficulty("Baby",4,4,1));
    difficultyList.add(new Difficulty("Easy",8,10,10));
    difficultyList.add(new Difficulty("Medium",14,18,40));
    difficultyList.add(new Difficulty("Hard",20,24,99));
    difficultyList.add(new Difficulty("Insane",25,25,623));
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

