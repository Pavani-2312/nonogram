package nonogram.controller;

import nonogram.model.*;
import nonogram.view.MainFrame;

public class GameController {
    private GameBoard board;
    private GameState gameState;
    private MainFrame view;
    private PuzzleLoader puzzleLoader;
    private Puzzle currentPuzzle;
    private Difficulty currentDifficulty;
    private int currentPuzzleIndex;
    
    public GameController() {
        puzzleLoader = new PuzzleLoader();
        currentDifficulty = Difficulty.EASY;
        currentPuzzleIndex = 0;
    }
    
    public void setView(MainFrame view) {
        this.view = view;
    }
    
    public void startNewGame() {
        currentPuzzle = puzzleLoader.getPuzzle(currentDifficulty, currentPuzzleIndex);
        board = new GameBoard(currentPuzzle.getSolution());
        gameState = new GameState(board);
        view.initializeGame(board, currentPuzzle.getName());
    }
    
    public void startNewGame(Difficulty difficulty) {
        currentDifficulty = difficulty;
        currentPuzzleIndex = 0;
        startNewGame();
    }
    
    public void nextPuzzle() {
        int maxPuzzles = puzzleLoader.getPuzzleCount(currentDifficulty);
        if (maxPuzzles > 0) {
            currentPuzzleIndex = (currentPuzzleIndex + 1) % maxPuzzles;
            startNewGame();
        }
    }
    
    public void previousPuzzle() {
        int maxPuzzles = puzzleLoader.getPuzzleCount(currentDifficulty);
        if (maxPuzzles > 0) {
            currentPuzzleIndex = (currentPuzzleIndex - 1 + maxPuzzles) % maxPuzzles;
            startNewGame();
        }
    }
    
    public void handleCellClick(int row, int col) {
        if (gameState.isComplete()) {
            return; // Ignore clicks after completion
        }
        
        Cell cell = board.getCell(row, col);
        CellState newState = cell.getCurrentState().getNextState();
        
        // Use GameState to track the move for undo/redo
        gameState.makeMove(new CellPosition(row, col), newState);
        
        view.updateDisplay();
        
        if (gameState.isComplete()) {
            view.showCompletionMessage();
        }
    }
    
    public void resetPuzzle() {
        gameState.reset();
        view.updateDisplay();
    }
    
    public void undo() {
        if (gameState.canUndo()) {
            gameState.undo();
            view.updateDisplay();
        }
    }
    
    public void redo() {
        if (gameState.canRedo()) {
            gameState.redo();
            view.updateDisplay();
        }
    }
    
    public void showHint() {
        Hint hint = HintGenerator.generateHint(board);
        if (hint != null) {
            gameState.incrementHintsUsed();
            view.showHint(hint);
        } else {
            view.showMessage("No hints available at this time.");
        }
    }
    
    public boolean canUndo() {
        return gameState != null && gameState.canUndo();
    }
    
    public boolean canRedo() {
        return gameState != null && gameState.canRedo();
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }
    
    public String getCurrentPuzzleInfo() {
        int total = puzzleLoader.getPuzzleCount(currentDifficulty);
        return String.format("%s - Puzzle %d of %d", 
                           currentDifficulty.getDisplayName(), 
                           currentPuzzleIndex + 1, 
                           total);
    }
}
