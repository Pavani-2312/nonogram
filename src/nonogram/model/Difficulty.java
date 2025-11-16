package nonogram.model;

public enum Difficulty {
    EASY(5, 5, "Easy"),
    MEDIUM(10, 10, "Medium"),
    HARD(15, 15, "Hard"),
    EXPERT(20, 20, "Expert");
    
    private final int rows;
    private final int cols;
    private final String displayName;
    
    Difficulty(int rows, int cols, String displayName) {
        this.rows = rows;
        this.cols = cols;
        this.displayName = displayName;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getTotalCells() {
        return rows * cols;
    }
}
