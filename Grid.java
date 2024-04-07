import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Grid
{
  private Square[][] grid;
  private int numUncovered;
  private int numMines;
  private int numRow;
  private int numCol;

  public Grid(int numRow, int numCol, int numMines) {
    //Initialize grid
    this.grid = new Square[numRow][numCol];
    this.numUncovered = 0;
    this.numRow = numRow;
    this.numCol = numCol;
    this.numMines = numMines;

    //Fill in grid with default squares
    for(int row=0; row<numRow; row++) {
      for(int col=0; col<numCol; col++) {
        grid[row][col] = new Square();
      }
    }
  }

  public void setRandomMineLocs(int numMines, int chosenIndex)
  {
    ArrayList<Integer> available = new ArrayList<Integer>();

    for(int c = 0; c < grid.length * grid[0].length; c++) {
      available.add(c);
    }
    available.remove(chosenIndex);
    
    Random rand = new Random();

    for(int m = 0; m < numMines; m++) {
      int randIndex = rand.nextInt(available.size());
      int a = available.get(randIndex);
      
      grid[a/grid[0].length][a%grid[0].length].setMine();
      available.remove(randIndex);
    }
  }
  
  public void setMineNums() {
    int count;
    for(int r=0; r<numRow; r++) {
        for(int c=0; c<numCol; c++) {
            count=0;
            for(Integer[] i : getAdjacentSquares(new Integer[]{r,c})) {
              if(grid[i[0]][i[1]].isMine()==1) {
                  count++;
              }
            }
            grid[r][c].setNumMines(count);
        }
    }
  }
  
  public ArrayList<Integer[]> getAdjacentSquares(Integer[] center) {
    ArrayList<Integer[]> adjacencyList = new ArrayList<Integer[]>();
    int row = center[0];
    int col = center[1];
    
    int lastRow = numRow-1;
    int lastCol = numCol-1;
    
    if(row!=0) {
      adjacencyList.add(new Integer[]{row-1, col});
      if(col!=0) {
        adjacencyList.add(new Integer[]{row-1, col-1});
      }
      if(col!=lastCol) {
        adjacencyList.add(new Integer[]{row-1, col+1});
      }
    }
    if(col!=0) {
      adjacencyList.add(new Integer[]{row, col-1});
      if(row!=lastRow) {
        adjacencyList.add(new Integer[]{row+1, col-1});
      }
    }
    if(row!=lastRow) {
      adjacencyList.add(new Integer[]{row+1, col});
      if(col!=lastCol) {
        adjacencyList.add(new Integer[]{row+1, col+1});
      }
    }
    if(col!=lastCol) {
      adjacencyList.add(new Integer[]{row, col+1});
    }
    
    return adjacencyList;
  }
  
  public Square[][] getGrid()
  {
    return grid;
  }

  public int getNumMines()
  {
    return numMines;
  }

  public void revealMines() {
    for(int r=0; r<grid.length;r++) {
      for(int c=0; c<grid[0].length;c++) {
        if(grid[r][c].isMine()==1) {
            grid[r][c].uncover();
        }
      }
    }
  }
  //Returns 0 if location is a mine
  //Otherwise, returns number of squares it uncovers
  public ArrayList<Integer[]> uncoverSquares(Integer[] startLoc, ArrayList<Integer[]> updatedLocs)
  {
      
    int row = startLoc[0];
    int col = startLoc[1];
    
    ArrayList<Integer[]> v = new ArrayList<Integer[]>();
    
    if(grid[row][col].isMine()==0 && grid[row][col].isCovered()) {
        grid[row][col].uncover();
        v.add(startLoc);
          
        if(grid[row][col].getNumMines()==0) {
          for(Integer[] t : getAdjacentSquares(startLoc)) {
            if(!updatedLocs.contains(t)) {
              for(Integer[] s : uncoverSquares(t, updatedLocs)) {
                updatedLocs.add(s);
                v.add(s);
              }
            }
          }
        }
    }

    return v;
  }

  public ArrayList<Integer[]> processMove(int clickNum, Integer[] loc)
  {
    ArrayList<Integer[]> updatedSquares=new ArrayList<Integer[]>();
    
    if(clickNum==3) {
      grid[loc[0]][loc[1]].changeFlag();
      updatedSquares.add(loc);
    } else if(clickNum==1) {
      updatedSquares = uncoverSquares(loc, new ArrayList<Integer[]>());
      numUncovered += updatedSquares.size();
      
      if(checkVictory()) {
        ArrayList<Integer[]> win = new ArrayList<Integer[]>();
        win.add(new Integer[]{-2,0});
        return win;
      } else if(grid[loc[0]][loc[1]].isMine()==1) {
        ArrayList<Integer[]> gameOver = new ArrayList<Integer[]>();
        gameOver.add(new Integer[]{-1,0});
        return gameOver;
      } else {
        return updatedSquares;
      }
    }
    return updatedSquares;
  }

  public boolean checkVictory() {
    return (numUncovered==numRow*numCol-numMines);
  }
}