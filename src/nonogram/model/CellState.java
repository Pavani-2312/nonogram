package nonogram.model;
public enum CellState {
    UNKNOWN,    
    FILLED,     
    MARKED;     
    public char getDisplaySymbol() {
        switch (this) {
            case FILLED: return '■';
            case MARKED: return 'X';
            case UNKNOWN: 
            default: return ' ';
        }
    }
    public CellState getNextState() {
        switch (this) {
            case UNKNOWN: return FILLED;
            case FILLED: return MARKED;
            case MARKED: return UNKNOWN;
            default: return UNKNOWN;
        }
    }
    public boolean isAnswered() {
        return this != UNKNOWN;
    }
}
