package nonogram.controller;

import nonogram.model.*;
import nonogram.view.MainFrame;
import nonogram.datastructures.MyLinkedList;

public class GameController {
    private GameBoard board;
    private GameState gameState;
    private MainFrame view;
    private PuzzleLoader puzzleLoader;
    private Puzzle currentPuzzle;
    private int currentPuzzleIndex;
    
    public GameController() {
        puzzleLoader = new PuzzleLoader();
        currentPuzzleIndex = 0;
    }
    
    public void setView(MainFrame view) {
        this.view = view;
    }
    
    public void startNewGame() {
        currentPuzzle = puzzleLoader.getDefaultPuzzle();
        currentPuzzleIndex = 0;
        initializeGame();
    }
    
    public void startGameWithPuzzle(Puzzle puzzle) {
        currentPuzzle = puzzle;
        initializeGame();
    }
    
    public void startGameWithPuzzleIndex(int index) {
        MyLinkedList<Puzzle> puzzles = puzzleLoader.getAllPuzzles();
        if (index >= 0 && index < puzzles.size()) {
            currentPuzzleIndex = index;
            currentPuzzle = puzzles.get(index);
            initializeGame();
        }
    }
    
    private void initializeGame() {
        board = new GameBoard(currentPuzzle.getSolution());
        gameState = new GameState(board);
        if (view != null) {
            view.initializeGame(board, currentPuzzle.getName());
        }
    }
    
    public void handleCellClick(int row, int col) {
        if (gameState.isComplete()) {
            return;
        }
        
        Cell cell = board.getCell(row, col);
        CellState oldState = cell.getCurrentState();
        CellState newState = oldState.getNextState();
        
        gameState.makeMove(new CellPosition(row, col), newState);
        board.autoFillMarks();
        view.updateDisplay();
        
        if (gameState.isComplete()) {
            view.showCompletionMessage();
        }
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
            board.autoFillMarks();
            view.updateDisplay();
        }
    }
    
    public void getHint() {
        if (!gameState.isComplete()) {
            Hint hint = HintGenerator.generateHint(board);
            if (hint != null) {
                gameState.incrementHintsUsed();
                view.showHint(hint);
            } else {
                view.showNoHintMessage();
            }
        }
    }
    
    public void nextPuzzle() {
        MyLinkedList<Puzzle> puzzles = puzzleLoader.getAllPuzzles();
        if (currentPuzzleIndex < puzzles.size() - 1) {
            currentPuzzleIndex++;
            startGameWithPuzzleIndex(currentPuzzleIndex);
        }
    }
    
    public void previousPuzzle() {
        if (currentPuzzleIndex > 0) {
            currentPuzzleIndex--;
            startGameWithPuzzleIndex(currentPuzzleIndex);
        }
    }
    
    public void resetPuzzle() {
        gameState.reset();
        view.updateDisplay();
    }
    
    public boolean canUndo() {
        return gameState != null && gameState.canUndo();
    }
    
    public boolean canRedo() {
        return gameState != null && gameState.canRedo();
    }
    
    public boolean hasNextPuzzle() {
        return currentPuzzleIndex < puzzleLoader.getAllPuzzles().size() - 1;
    }
    
    public boolean hasPreviousPuzzle() {
        return currentPuzzleIndex > 0;
    }
    
    public GameState getGameState() {
        return gameState;
    }
}
