class Difficulty {
  private int numRow;
  private int numCol;
  private int numMines;
  private String name;

  public Difficulty(String name, int numRow, int numCol, int numMines) {
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
