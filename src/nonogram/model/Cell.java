package nonogram.model;

public class Cell {
    private int row;
    private int col;
    private CellState currentState;
    private boolean actualValue;
    
    public Cell(int row, int col, boolean actualValue) {
        this.row = row;
        this.col = col;
        this.actualValue = actualValue;
        this.currentState = CellState.UNKNOWN;
    }
    
    public CellState getCurrentState() {
        return currentState;
    }
    
    public void setState(CellState newState) {
        this.currentState = newState;
    }
    
    public void cycleState() {
        switch (currentState) {
            case UNKNOWN:
                currentState = CellState.FILLED;
                break;
            case FILLED:
                currentState = CellState.MARKED;
                break;
            case MARKED:
                currentState = CellState.UNKNOWN;
                break;
        }
    }
    
    public boolean getActualValue() {
        return actualValue;
    }
    
    public boolean isCorrect() {
        switch (currentState) {
            case UNKNOWN:
                return false;
            case FILLED:
                return actualValue == true;
            case MARKED:
                return actualValue == false;
            default:
                return false;
        }
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public void reset() {
        currentState = CellState.UNKNOWN;
    }
}
