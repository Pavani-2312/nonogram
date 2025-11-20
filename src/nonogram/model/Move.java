package nonogram.model;
public class Move {
    private final CellPosition position;
    private final CellState oldState;
    private final CellState newState;
    private final long timestamp;
    public Move(CellPosition position, CellState oldState, CellState newState) {
        this.position = position;
        this.oldState = oldState;
        this.newState = newState;
        this.timestamp = System.currentTimeMillis();
    }
    public CellPosition getPosition() {
        return position;
    }
    public CellState getOldState() {
        return oldState;
    }
    public CellState getNewState() {
        return newState;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public Move getReverse() {
        return new Move(position, newState, oldState);
    }
    @Override
    public String toString() {
        return "Move{" + position + ": " + oldState + " -> " + newState + "}";
    }
}
