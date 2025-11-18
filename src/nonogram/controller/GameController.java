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
    private Difficulty currentDifficulty;
    private boolean xMode = false;
    
    public GameController() {
        puzzleLoader = new PuzzleLoader();
        currentDifficulty = Difficulty.EASY;
        currentPuzzleIndex = 0;
    }
    
    public void setView(MainFrame view) {
        this.view = view;
    }
    
    public void startNewGame() {
        currentPuzzle = puzzleLoader.getPuzzle(currentDifficulty, 0);
        if (currentPuzzle == null) {
            currentPuzzle = puzzleLoader.getDefaultPuzzle();
        }
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
        if (gameState.isComplete() || !gameState.hasLives()) {
            return;
        }
        
        Cell cell = board.getCell(row, col);
        CellState oldState = cell.getCurrentState();
        
        // Don't lose life if cell is already correctly filled
        if (cell.isCorrect()) {
            return;
        }
        
        CellState newState;
        
        if (xMode) {
            newState = CellState.MARKED;
            xMode = false;
            view.updateXButton(false);
        } else {
            newState = oldState.getNextState();
        }
        
        // Check if the move would be wrong
        CellState tempState = cell.getCurrentState();
        cell.setCurrentState(newState);
        boolean isWrongMove = cell.isWrong();
        cell.setCurrentState(tempState); // Restore original state
        
        if (isWrongMove) {
            gameState.loseLife();
            view.showWrongMove(row, col);
            view.updateLivesDisplay(gameState.getLives());
            
            if (!gameState.hasLives()) {
                view.showGameOver();
                return;
            }
        } else {
            gameState.makeMove(new CellPosition(row, col), newState);
            board.autoFillMarks();
            view.updateDisplay();
            
            // Check completion after auto-fill
            if (board.isPuzzleComplete()) {
                view.showCompletionMessage();
            }
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
        MyLinkedList<Puzzle> currentDifficultyPuzzles = puzzleLoader.getPuzzlesForDifficulty(currentDifficulty);
        if (currentDifficultyPuzzles != null && currentPuzzleIndex < currentDifficultyPuzzles.size() - 1) {
            currentPuzzleIndex++;
            currentPuzzle = currentDifficultyPuzzles.get(currentPuzzleIndex);
            initializeGame();
        }
    }
    
    public void previousPuzzle() {
        if (currentPuzzleIndex > 0) {
            currentPuzzleIndex--;
            MyLinkedList<Puzzle> currentDifficultyPuzzles = puzzleLoader.getPuzzlesForDifficulty(currentDifficulty);
            if (currentDifficultyPuzzles != null) {
                currentPuzzle = currentDifficultyPuzzles.get(currentPuzzleIndex);
                initializeGame();
            }
        }
    }
    
    public void resetPuzzle() {
        gameState.reset();
        view.updateDisplay();
        view.updateLivesDisplay(gameState.getLives());
    }
    
    public boolean canUndo() {
        return gameState != null && gameState.canUndo();
    }
    
    public boolean canRedo() {
        return gameState != null && gameState.canRedo();
    }
    
    public boolean hasNextPuzzle() {
        MyLinkedList<Puzzle> currentDifficultyPuzzles = puzzleLoader.getPuzzlesForDifficulty(currentDifficulty);
        return currentDifficultyPuzzles != null && currentPuzzleIndex < currentDifficultyPuzzles.size() - 1;
    }
    
    public boolean hasPreviousPuzzle() {
        return currentPuzzleIndex > 0;
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    public void toggleXMode() {
        xMode = !xMode;
        view.updateXButton(xMode);
    }
    
    public void setDifficulty(Difficulty difficulty) {
        currentDifficulty = difficulty;
        currentPuzzleIndex = 0;
        currentPuzzle = puzzleLoader.getPuzzle(difficulty, 0);
        if (currentPuzzle != null) {
            initializeGame();
        }
    }
    
    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }
}
