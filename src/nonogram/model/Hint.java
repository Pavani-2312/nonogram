package nonogram.model;

import nonogram.datastructures.MyArrayList;

public class Hint {
    private final HintType type;
    private final String message;
    private final MyArrayList<CellPosition> affectedCells;
    private final boolean isRowHint;
    private final int lineIndex;
    
    public Hint(HintType type, String message, int lineIndex, boolean isRowHint) {
        this.type = type;
        this.message = message;
        this.lineIndex = lineIndex;
        this.isRowHint = isRowHint;
        this.affectedCells = new MyArrayList<>();
    }
    
    public HintType getType() {
        return type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public MyArrayList<CellPosition> getAffectedCells() {
        return affectedCells;
    }
    
    public void addAffectedCell(CellPosition position) {
        affectedCells.add(position);
    }
    
    public boolean isRowHint() {
        return isRowHint;
    }
    
    public int getLineIndex() {
        return lineIndex;
    }
    
    public String getFullMessage() {
        String lineType = isRowHint ? "Row" : "Column";
        return type.getName() + " - " + lineType + " " + (lineIndex + 1) + ": " + message;
    }
}
