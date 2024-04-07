public class Square
{
  private int minesAround;
  private boolean covered;
  private boolean flagged;
  private int mine;

  public Square()
  {
    this.minesAround = 0;
    this.covered = true;
    this.flagged = false;
    this.mine = 0;
  }

  public int getNumMines() {
    return minesAround;
  }
  public void setNumMines(int num) {
    this.minesAround = num;
  }

  public boolean isCovered() {
    return covered;
  }
  public void uncover() {
    this.covered = false;
  }

  public int isMine() {
    return mine;
  }

  public void setMine() {
    mine = 1;
  }

  public boolean isFlagged() {
    return flagged;
  }

  public boolean changeFlag() {
    if(!covered) {
      return false;
    }
    else {
      this.flagged = !flagged;
      return true;
    }
  }
}