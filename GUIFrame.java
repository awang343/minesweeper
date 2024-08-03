import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
 
public class GUIFrame extends JFrame
{
  private ArrayList<Difficulty> difficultyList;
  private JTextField statusBar;
  
  private GamePanel panel;
  private diffPanel bottomPanel;
  private JButton topPanel;
  
  private instructListener iListener;

  private GUIFrame self;

  Color red = new Color(252, 111, 111);
  Color darkgreen = new Color(162, 209, 73);
  Color lightgreen = new Color(170,215,81);
  Color brown = new Color(215, 184, 153);
  Color blue = new Color(116, 230, 242);
  Color yellow = new Color(243, 245, 113);

  public GUIFrame(ArrayList<Difficulty> difficultyList)
  {
    self = this;

    setLayout(new BorderLayout());

    this.difficultyList = difficultyList;
    
    panel = new GamePanel();
    add(panel, BorderLayout.CENTER);
    
    bottomPanel = new diffPanel(difficultyList);
    add(bottomPanel, BorderLayout.LINE_START);
    
    iListener = new instructListener();
    
    topPanel = new JButton();
    topPanel.setText("Instructions");
    topPanel.addActionListener(iListener);
    add(topPanel, BorderLayout.NORTH);
  
    statusBar=new JTextField("Left-click to uncover and right-click to flag.");
    statusBar.setEditable(false);
    add(statusBar,BorderLayout.SOUTH);
  
    setTitle("Minesweeper");
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    setUndecorated(true);
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    
    setVisible(true);
  }
  
  public GamePanel getPanel() {
    return panel;
  }
  
  class instructListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(null,"The numbers on the board represent how many bombs are adjacent to a square.\nFor example, if a square has a \"3\" on it, then there are 3 bombs next to that square.\nThe bombs could be above, below, right, left, or diagonal to the square.\nFlags can be used to help you remember where the mines are by marking them in yellow.\nAvoid all the bombs and expose all the empty spaces to win Minesweeper.\nIf you click on a mine you lose. \n M stands for Mine, NM stands for Not Mine, and F stands for Flag.");
    }
  }
  
  class diffPanel extends JPanel
  {
    private diffListener dListener;
    private JButton[] diffButtons;

    public diffPanel(ArrayList<Difficulty> difficultyList)
    {
      dListener = new diffListener();
      diffButtons = new JButton[difficultyList.size()];
      setLayout(new GridLayout(difficultyList.size(),1));

      for(int i=0; i<difficultyList.size();i++) {
        diffButtons[i] = new JButton();

        diffButtons[i].putClientProperty("INDEX", i);
        diffButtons[i].addActionListener(dListener);
        diffButtons[i].setText(difficultyList.get(i).getName());
        add(diffButtons[i]);
      }
    }

    class diffListener implements ActionListener
    {
      public void actionPerformed(ActionEvent e) {
        JButton b = (JButton)e.getSource();
        int index = (int) b.getClientProperty("INDEX");
        Difficulty selected = difficultyList.get(index);
        
        self.remove(panel);
        
        panel.reset(selected.getNR(), selected.getNC(), selected.getNM());
      }
    }
  }

  class GamePanel extends JPanel
  {
    private GameListener listener;
    private JButton[][] buttons;
    private int numRow;
    private int numCol;
    private int numMines;
    private boolean firstMoveMade;
    
    private Grid grid;

    public GamePanel()
    {
      listener = new GameListener();
    }

    public void reset(int newNR, int newNC, int newNM)
    {
      this.numRow = newNR;
      this.numCol = newNC;
      this.numMines = newNM;
      this.firstMoveMade = false;
      this.grid = new Grid(numRow, numCol, numMines);
      
      self.remove(this);
      this.removeAll();
      
      setLayout(new GridLayout(numRow,numCol));
      
      buttons = new JButton[numRow][numCol];

      for(int i=0;i<numRow;i++) {
        for(int j=0;j<numCol;j++) {
          buttons[i][j] = new JButton();
          buttons[i][j].putClientProperty("INDEX", new Integer[]{i,j});

          buttons[i][j].addMouseListener(listener);
          buttons[i][j].setForeground(yellow);
          buttons[i][j].setFont(new Font("Calibri", Font.BOLD, 20));

          buttons[i][j].setEnabled(true);
          buttons[i][j].setText("");
          if((i+j)%2==0) {
            buttons[i][j].setBackground(lightgreen);
          }
          else {
            buttons[i][j].setBackground(darkgreen);
          }
          panel.add(buttons[i][j]);
        }
      }
      
      self.add(panel);
      self.revalidate();
      self.repaint();
    }

    public void updatePanel(Integer[] index) {
      JButton b;
      b = buttons[index[0]][index[1]];
      
      if(grid.getGrid()[index[0]][index[1]].isFlagged()) {
          b.setBackground(yellow);
          b.setText("F");
          b.setEnabled(false);
      }
      else if(grid.getGrid()[index[0]][index[1]].isCovered()) {
          b.setBackground(lightgreen);
          b.setText("");
          b.setEnabled(true);
      }
      else {
        if (grid.getGrid()[index[0]][index[1]].isMine()==1) {
          b.setBackground(red);
          b.setText("M");
          b.setEnabled(false);
        }
        else {
          b.setBackground(brown);
          b.setText(grid.getGrid()[index[0]][index[1]].getNumMines()+"");
          b.setEnabled(false);
        }
      }
    }
    public void updateSelectedPanels(ArrayList<Integer[]> indexes) {
        for(Integer[] loc : indexes) {
           updatePanel(loc); 
        }
    }
    
    public void finalReveal() {
        grid.revealMines();
        for(int r=0; r<numRow; r++) {
            for(int c=0; c<numCol; c++) {
                updatePanel(new Integer[]{r,c});
                if(grid.getGrid()[r][c].isMine()==0 && grid.getGrid()[r][c].isFlagged()) {
                    buttons[r][c].setBackground(blue);
                    buttons[r][c].setText("NM");
                }
            }
        }
    }
    
    class GameListener extends MouseAdapter
    {
      public void mouseReleased(MouseEvent e)
      {
        int resultCode = 1;
        
        JButton b = (JButton)e.getSource();
        Integer[] index = (Integer[]) b.getClientProperty("INDEX");
        
        if(!firstMoveMade) {
            if(e.getButton()==3) {
                return;
            }
            grid.setRandomMineLocs(numMines,index[0]*numCol+index[1]);
            grid.setMineNums();
            
            firstMoveMade=true;
        }
        
        ArrayList<Integer[]> updatedSquares = grid.processMove(e.getButton(), index);

        if(updatedSquares.size()>0) {
            if(updatedSquares.get(0)[0]==-2) {
              finalReveal();
              JOptionPane.showMessageDialog(null, "Congratulations! You win!");
              reset(numRow, numCol, numMines);
              return;
            }
            if(updatedSquares.get(0)[0]==-1) {
              finalReveal();
              JOptionPane.showMessageDialog(null, "Game over!");
              reset(numRow, numCol, numMines);
              return;
            }
            updateSelectedPanels(updatedSquares);
        }
        
        
      }
    }
  }
}
