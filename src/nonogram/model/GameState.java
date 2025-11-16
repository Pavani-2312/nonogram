package nonogram.model;

import nonogram.datastructures.MyStack;

public class GameState {
    private final GameBoard board;
    private final MyStack<Move> moveHistory;
    private final MyStack<Move> redoStack;
    private boolean isComplete;
    private long startTime;
    private long endTime;
    private int hintsUsed;
    private int lives;
    
    public GameState(GameBoard board) {
        this.board = board;
        this.moveHistory = new MyStack<>();
        this.redoStack = new MyStack<>();
        this.isComplete = false;
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
        this.hintsUsed = 0;
        this.lives = 3;
    }
    
    public GameBoard getBoard() {
        return board;
    }
    
    public void makeMove(CellPosition position, CellState newState) {
        Cell cell = board.getCell(position.getRow(), position.getCol());
        CellState oldState = cell.getCurrentState();
        
        if (oldState != newState) {
            Move move = new Move(position, oldState, newState);
            moveHistory.push(move);
            redoStack.clear(); // Clear redo stack when new move is made
            
            cell.setCurrentState(newState);
            checkCompletion();
        }
    }
    
    public boolean canUndo() {
        return !moveHistory.isEmpty();
    }
    
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    public void undo() {
        if (canUndo()) {
            Move move = moveHistory.pop();
            redoStack.push(move);
            
            Cell cell = board.getCell(move.getPosition().getRow(), move.getPosition().getCol());
            cell.setCurrentState(move.getOldState());
            
            // If game was complete, it's no longer complete after undo
            if (isComplete) {
                isComplete = false;
                endTime = 0;
            }
        }
    }
    
    public void redo() {
        if (canRedo()) {
            Move move = redoStack.pop();
            moveHistory.push(move);
            
            Cell cell = board.getCell(move.getPosition().getRow(), move.getPosition().getCol());
            cell.setCurrentState(move.getNewState());
            checkCompletion();
        }
    }
    
    public boolean isComplete() {
        return isComplete;
    }
    
    public long getElapsedTime() {
        if (endTime > 0) {
            return endTime - startTime;
        }
        return System.currentTimeMillis() - startTime;
    }
    
    public int getMoveCount() {
        return moveHistory.size();
    }
    
    public int getHintsUsed() {
        return hintsUsed;
    }
    
    public void incrementHintsUsed() {
        hintsUsed++;
    }
    
    public int getLives() {
        return lives;
    }
    
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }
    
    public boolean hasLives() {
        return lives > 0;
    }
    
    private void checkCompletion() {
        if (board.isSolved()) {
            isComplete = true;
            endTime = System.currentTimeMillis();
        }
    }
    
    public void reset() {
        moveHistory.clear();
        redoStack.clear();
        isComplete = false;
        startTime = System.currentTimeMillis();
        endTime = 0;
        hintsUsed = 0;
        lives = 3;
        
        // Reset all cells to unknown state
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                board.getCell(row, col).setCurrentState(CellState.UNKNOWN);
            }
        }
    }
}
