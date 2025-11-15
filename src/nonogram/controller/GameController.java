package nonogram.controller;

import nonogram.model.GameBoard;
import nonogram.model.Puzzle;
import nonogram.view.MainFrame;

public class GameController {
    private GameBoard board;
    private MainFrame view;
    private PuzzleLoader puzzleLoader;
    private Puzzle currentPuzzle;
    
    public GameController() {
        puzzleLoader = new PuzzleLoader();
    }
    
    public void setView(MainFrame view) {
        this.view = view;
    }
    
    public void startNewGame() {
        currentPuzzle = puzzleLoader.getDefaultPuzzle();
        board = new GameBoard(currentPuzzle.getSolution());
        view.initializeGame(board, currentPuzzle.getName());
    }
    
    public void startGameWithPuzzle(Puzzle puzzle) {
        currentPuzzle = puzzle;
        board = new GameBoard(currentPuzzle.getSolution());
        if (view == null) {
            view = new MainFrame();
            view.setController(this);
            view.setVisible(true);
        }
        view.initializeGame(board, currentPuzzle.getName());
    }
    
    public void startGameWithPuzzleIndex(int index) {
        currentPuzzle = puzzleLoader.getAllPuzzles().get(index);
        board = new GameBoard(currentPuzzle.getSolution());
        
        view = new MainFrame();
        view.setController(this);
        view.initializeGame(board, currentPuzzle.getName());
        view.setVisible(true);
    }
    
    public void handleCellClick(int row, int col) {
        if (board.isPuzzleComplete()) {
            return;
        }
        
        board.getCell(row, col).cycleState();
        view.updateDisplay();
        
        if (board.isPuzzleComplete()) {
            view.showCompletionMessage();
        }
    }
    
    public void resetPuzzle() {
        board.reset();
        view.updateDisplay();
    }
}
