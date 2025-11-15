# Nonogram Game - Controller Layer Architecture
## Implementation Specification

---

## 1. Overview

This document specifies the Controller layer of the Nonogram application following the Model-View-Controller (MVC) architectural pattern. The Controller layer manages user interactions, coordinates between Model and View, and handles game flow logic.

---

## 2. Controller Layer Principles

### 2.1 Design Constraints
- **MVC Mediation**: Coordinates between Model and View layers
- **Event Processing**: Handles all user input events
- **Game Logic**: Manages game state transitions
- **No Direct UI**: Does not contain Swing components

### 2.2 Layer Responsibilities
- Process user input from View layer
- Update Model based on user actions
- Coordinate View updates after Model changes
- Manage game flow and state transitions

---

## 3. Core Controller Classes

### 3.1 Class Overview

| Class | Purpose | Implementation Priority |
|-------|---------|------------------------|
| GameController | Main game coordination | Phase 1 - Essential |
| PuzzleLoader | Load puzzle data | Phase 1 - Essential |
| HintGenerator | Generate hints | Phase 2 - Enhancement |

---

## 4. GameController Class

### 4.1 Purpose
Central coordinator that manages all game interactions and state changes.

### 4.2 Basic Structure

```java
public class GameController {
    private GameBoard board;
    private MainFrame view;
    private MyStack<Move> undoStack;  // Phase 2
    private PuzzleLoader puzzleLoader;
    private HintGenerator hintGenerator;  // Phase 2
    
    public GameController() {
        puzzleLoader = new PuzzleLoader();
        undoStack = new MyStack<>();
        hintGenerator = new HintGenerator();
    }
}
```

### 4.3 Essential Methods (Phase 1)

**Game Initialization**:
```java
public void startNewGame()  // Initialize new puzzle
public void loadPuzzle(String puzzleId)  // Load specific puzzle
public void setView(MainFrame view)  // Connect to view layer
```

**User Input Processing**:
```java
public void handleCellClick(int row, int col)  // Process cell clicks
public void resetPuzzle()  // Reset current puzzle
public void checkWinCondition()  // Check if puzzle solved
```

**View Updates**:
```java
public void updateView()  // Refresh view display
private void notifyViewUpdate()  // Trigger view refresh
```

### 4.4 Enhanced Methods (Phase 2)

**Undo System**:
```java
public void undoMove()  // Undo last move
public void redoMove()  // Redo undone move
private void recordMove(Move move)  // Save move to stack
```

**Hint System**:
```java
public void requestHint()  // Generate and display hint
private void showHint(String hintMessage)  // Display hint to user
```

### 4.5 Core Game Logic

**Cell Click Processing**:
```java
public void handleCellClick(int row, int col) {
    // Get current cell
    Cell cell = board.getCell(row, col);
    
    // Record current state for undo (Phase 2)
    CellState oldState = cell.getCurrentState();
    
    // Cycle cell state
    cell.cycleState();
    
    // Record move for undo (Phase 2)
    Move move = new Move(row, col, oldState, cell.getCurrentState());
    recordMove(move);
    
    // Check win condition
    if (board.isPuzzleComplete()) {
        handlePuzzleComplete();
    }
    
    // Update view
    updateView();
}
```

**Win Condition Handling**:
```java
private void handlePuzzleComplete() {
    // Show completion message
    view.showCompletionDialog("Puzzle Complete!");
    
    // Disable further input
    view.setInputEnabled(false);
    
    // Offer new game option
    view.showNewGameOption();
}
```

---

## 5. PuzzleLoader Class

### 5.1 Purpose
Handles loading puzzle data from various sources.

### 5.2 Basic Structure

```java
public class PuzzleLoader {
    private MyArrayList<Puzzle> availablePuzzles;
    
    public PuzzleLoader() {
        availablePuzzles = new MyArrayList<>();
        loadDefaultPuzzles();
    }
}
```

### 5.3 Essential Methods

**Puzzle Loading**:
```java
public Puzzle loadPuzzle(String puzzleId)  // Load by ID
public MyArrayList<Puzzle> getAvailablePuzzles()  // Get puzzle list
private void loadDefaultPuzzles()  // Load hardcoded puzzles
```

**Puzzle Creation**:
```java
public Puzzle createPuzzleFromSolution(boolean[][] solution)  // Create from array
private void generateClues(boolean[][] solution)  // Generate clue numbers
```

### 5.4 Default Puzzle Data

**Hardcoded Puzzles for Basic Implementation**:
```java
private void loadDefaultPuzzles() {
    // 5x5 Heart puzzle
    boolean[][] heart = {
        {false, true, false, true, false},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {false, true, true, true, false},
        {false, false, true, false, false}
    };
    availablePuzzles.add(new Puzzle("HEART", "Heart Shape", heart));
    
    // 5x5 Cross puzzle
    boolean[][] cross = {
        {false, false, true, false, false},
        {false, false, true, false, false},
        {true, true, true, true, true},
        {false, false, true, false, false},
        {false, false, true, false, false}
    };
    availablePuzzles.add(new Puzzle("CROSS", "Cross Shape", cross));
}
```

---

## 6. HintGenerator Class (Phase 2)

### 6.1 Purpose
Generates helpful hints for players based on current board state.

### 6.2 Basic Structure

```java
public class HintGenerator {
    public String generateHint(GameBoard board) {
        // Check for complete lines first
        String completeLineHint = checkCompleteLines(board);
        if (completeLineHint != null) {
            return completeLineHint;
        }
        
        // Check for obvious patterns
        String patternHint = checkObviousPatterns(board);
        if (patternHint != null) {
            return patternHint;
        }
        
        // Default strategic hint
        return "Look for the largest clue numbers first";
    }
}
```

### 6.3 Hint Generation Methods

**Complete Line Detection**:
```java
private String checkCompleteLines(GameBoard board) {
    for (int row = 0; row < board.getRows(); row++) {
        MyArrayList<Integer> clues = board.getRowClues(row);
        int clueSum = calculateClueSum(clues);
        
        if (clueSum == board.getCols()) {
            return "Row " + (row + 1) + " can be filled completely";
        }
    }
    
    for (int col = 0; col < board.getCols(); col++) {
        MyArrayList<Integer> clues = board.getColumnClues(col);
        int clueSum = calculateClueSum(clues);
        
        if (clueSum == board.getRows()) {
            return "Column " + (col + 1) + " can be filled completely";
        }
    }
    
    return null;
}
```

**Pattern Recognition**:
```java
private String checkObviousPatterns(GameBoard board) {
    // Check for large single clues that create overlaps
    for (int row = 0; row < board.getRows(); row++) {
        MyArrayList<Integer> clues = board.getRowClues(row);
        if (clues.size() == 1) {
            int clueValue = clues.get(0);
            if (clueValue > board.getCols() / 2) {
                return "Row " + (row + 1) + " has obvious overlap cells";
            }
        }
    }
    
    return null;
}
```

---

## 7. Move Class (Phase 2)

### 7.1 Purpose
Records individual player moves for undo/redo functionality.

### 7.2 Basic Structure

```java
public class Move {
    private int row;
    private int col;
    private CellState previousState;
    private CellState newState;
    
    public Move(int row, int col, CellState previousState, CellState newState) {
        this.row = row;
        this.col = col;
        this.previousState = previousState;
        this.newState = newState;
    }
}
```

### 7.3 Essential Methods

```java
public void undo(GameBoard board)  // Reverse this move
public void redo(GameBoard board)  // Reapply this move
public int getRow()
public int getCol()
public CellState getPreviousState()
public CellState getNewState()
```

---

## 8. Game Flow Management

### 8.1 Application Startup

**Initialization Sequence**:
```java
public static void main(String[] args) {
    // Create MVC components
    GameController controller = new GameController();
    MainFrame view = new MainFrame();
    
    // Connect components
    controller.setView(view);
    view.setController(controller);
    
    // Start first game
    controller.startNewGame();
    
    // Show window
    view.setVisible(true);
}
```

### 8.2 Game State Transitions

**State Flow**:
```
INITIALIZING → PLAYING → COMPLETED
     ↓            ↓         ↓
   Load Puzzle → Handle → Show Win
                 Input    Message
                   ↓         ↓
                Update → New Game
                 View    Option
```

### 8.3 Event Processing Flow

**User Action Processing**:
```
User clicks cell
       ↓
View captures mouse event
       ↓
View calls controller.handleCellClick()
       ↓
Controller updates Model (GameBoard)
       ↓
Controller checks win condition
       ↓
Controller calls view.updateDisplay()
       ↓
View repaints affected components
```

---

## 9. Error Handling

### 9.1 Input Validation

**Cell Click Validation**:
```java
public void handleCellClick(int row, int col) {
    // Validate coordinates
    if (row < 0 || row >= board.getRows() || 
        col < 0 || col >= board.getCols()) {
        return;  // Ignore invalid clicks
    }
    
    // Check if game is still active
    if (board.isPuzzleComplete()) {
        return;  // Ignore clicks after completion
    }
    
    // Process valid click
    processValidCellClick(row, col);
}
```

### 9.2 Exception Handling

**Puzzle Loading Errors**:
```java
public boolean loadPuzzle(String puzzleId) {
    try {
        Puzzle puzzle = puzzleLoader.loadPuzzle(puzzleId);
        if (puzzle == null) {
            view.showErrorMessage("Puzzle not found: " + puzzleId);
            return false;
        }
        
        board = new GameBoard(puzzle.getSolution());
        updateView();
        return true;
        
    } catch (Exception e) {
        view.showErrorMessage("Error loading puzzle: " + e.getMessage());
        return false;
    }
}
```

---

## 10. Implementation Priority

### 10.1 Phase 1 - Essential Features
1. **GameController** - Basic game coordination
2. **PuzzleLoader** - Hardcoded puzzle loading
3. **Cell click handling** - Core interaction
4. **Win detection** - Basic completion check
5. **View updates** - Display synchronization

### 10.2 Phase 2 - Enhanced Features
1. **Move class** - Undo/redo support
2. **HintGenerator** - Player assistance
3. **Error handling** - Robust input validation
4. **Multiple puzzles** - Puzzle selection

---

## 11. Testing Considerations

### 11.1 Unit Testing
- Test cell click processing with various states
- Verify win condition detection accuracy
- Test puzzle loading with valid/invalid data
- Validate undo/redo functionality

### 11.2 Integration Testing
- Test Model-View-Controller communication
- Verify event flow from user input to display update
- Test game state transitions
- Validate error handling across layers

---

## Document Revision History
- Version 1.0 - Initial specification
- Date: 2025-11-15
