package nonogram.model;
public class CellPosition {
    private final int row;
    private final int col;
    public CellPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CellPosition that = (CellPosition) obj;
        return row == that.row && col == that.col;
    }
    @Override
    public int hashCode() {
        return row * 31 + col;
    }
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
    public boolean isValid(int maxRows, int maxCols) {
        return row >= 0 && row < maxRows && col >= 0 && col < maxCols;
    }
}
