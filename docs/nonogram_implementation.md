# Nonogram Complete Implementation Documentation
## Java Implementation with Custom Data Structures

---

## Table of Contents
1. [Architecture Design](#1-architecture-design)
2. [Data Structures Specification](#2-data-structures-specification)
3. [Core Components](#3-core-components)
4. [Game Logic](#4-game-logic)
5. [Hint System](#5-hint-system)
6. [UI Integration](#6-ui-integration)
7. [Puzzle Management](#7-puzzle-management)
8. [Implementation Checklist](#8-implementation-checklist)

---

## 1. Architecture Design

### 1.1 System Components

```
┌─────────────────────────────────────────────────┐
│            USER INTERFACE LAYER                 │
│  GamePanel, MenuPanel, DialogHelper             │
└──────────────────┬──────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────┐
│          GAME CONTROLLER                        │
│  - handleCellClick()                            │
│  - handleUndo/Redo()                            │
│  - handleHint()                                 │
│  - handleCheck()                                │
└──┬────────┬────────┬────────┬────────┬─────────┘
   │        │        │        │        │
┌──▼───┐ ┌─▼────┐ ┌─▼─────┐ ┌▼──────┐ ┌▼────────┐
│Board │ │State │ │Logic  │ │Hint   │ │Storage  │
│      │ │      │ │       │ │System │ │         │
│Grid  │ │Timer │ │Valid  │ │Queue  │ │Puzzles  │
│Clues │ │Moves │ │Check  │ │       │ │Save/Load│
└──────┘ └──────┘ └───────┘ └───────┘ └─────────┘
```

### 1.2 Class Structure

```
nonogram/
├── datastructures/
│   ├── MyArrayList.java      (Dynamic array)
│   ├── MyStack.java           (LIFO for undo/redo)
│   └── MyQueue.java           (FIFO for hints)
│
├── model/
│   ├── Cell.java              (Grid cell)
│   ├── Clue.java              (Row/column clue)
│   ├── Move.java              (For undo/redo)
│   ├── Puzzle.java            (Puzzle definition)
│   ├── GameBoard.java         (Grid + clues)
│   └── GameState.java         (Timer, moves, stats)
│
├── enums/
│   ├── CellState.java         (UNKNOWN/FILLED/MARKED)
│   └── Difficulty.java        (EASY/MEDIUM/HARD/EXPERT)
│
├── logic/
│   ├── ClueGenerator.java     (Generate clues from solution)
│   ├── ValidationEngine.java  (Check correctness)
│   ├── HintSystem.java        (Generate hints)
│   └── SolutionChecker.java   (Win condition)
│
├── manager/
│   ├── UndoRedoManager.java   (Manage undo/redo stacks)
│   ├── GameTimer.java         (Time tracking)
│   └── StatisticsManager.java (Track stats)
│
├── storage/
│   ├── PuzzleLibrary.java     (Load puzzles)
│   └── GameSaver.java         (Save/load game state)
│
├── controller/
│   └── GameController.java    (Main coordinator)
│
└── ui/
    ├── MainFrame.java         (Main window)
    ├── GamePanel.java         (Game display)
    └── MenuPanel.java         (Menu screens)
```

### 1.3 Key Data Flow

**Cell Click Flow:**
```
User clicks cell (row, col)
    ↓
GameController.handleCellClick(row, col)
    ↓
1. Get current state from GameBoard
2. Create Move object (for undo)
3. Push Move to UndoManager
4. Cycle cell state in GameBoard
5. Clear redo stack
6. Increment move counter
7. Check if solved
8. Update UI
```

**Undo Flow:**
```
User clicks Undo
    ↓
GameController.handleUndo()
    ↓
1. Pop Move from undo stack
2. Restore previous cell state
3. Push Move to redo stack
4. Decrement move counter
5. Update UI
```

**Hint Flow:**
```
User clicks Hint
    ↓
GameController.handleHint()
    ↓
1. Pause timer
2. HintSystem.generateHints()
3. Get next hint from queue
4. Display hint dialog
5. Resume timer
6. Increment hint counter
```

---

## 2. Data Structures Specification

### 2.1 MyArrayList<T>

**Purpose:** Store dynamic collections (clues, cells, hints)

**Core Operations:**
```
+ MyArrayList()                    // Initialize with capacity 10
+ void add(T element)              // Add to end, O(1) amortized
+ T get(int index)                 // Get at index, O(1)
+ void set(int index, T element)   // Set at index, O(1)
+ int size()                       // Get size, O(1)
+ boolean isEmpty()                // Check empty, O(1)
+ void addAll(MyArrayList<T> other) // Add all from other list
+ void clear()                     // Remove all elements
```

**Internal Structure:**
```
- Object[] data        // Internal array
- int size             // Current elements
- int capacity         // Total slots
```

**Growth Strategy:**
- Initial capacity: 10
- When full: double capacity
- Copy all elements to new array

**Usage in Nonogram:**
```
// Store clue numbers
MyArrayList<Integer> clueNumbers = new MyArrayList<>();
clueNumbers.add(2);
clueNumbers.add(3);
clueNumbers.add(1);

// Store all row clues
MyArrayList<Clue> rowClues = new MyArrayList<>();
for (int i = 0; i < gridSize; i++) {
    rowClues.add(generateClue(i));
}
```

### 2.2 MyStack<T>

**Purpose:** Implement undo/redo functionality (LIFO)

**Core Operations:**
```
+ MyStack()                  // Default max size 50
+ MyStack(int maxSize)       // Custom max size
+ void push(T element)       // Add to top, O(1)
+ T pop()                    // Remove from top, O(1)
+ T peek()                   // View top, O(1)
+ boolean isEmpty()          // Check empty, O(1)
+ int size()                 // Get size, O(1)
+ void clear()               // Remove all
```

**Internal Structure:**
```
- MyArrayList<T> elements  // Underlying storage
- int maxSize              // Capacity limit
```

**Capacity Management:**
- Default max: 50 moves
- When full: remove oldest (bottom) element before adding new
- Prevents unlimited memory growth

**Usage in Nonogram:**
```
// Undo stack
MyStack<Move> undoStack = new MyStack<>(50);
undoStack.push(new Move(row, col, prevState, newState));

// Undo operation
if (!undoStack.isEmpty()) {
    Move lastMove = undoStack.pop();
    restoreState(lastMove);
    redoStack.push(lastMove);
}
```

### 2.3 MyQueue<T>

**Purpose:** Manage hint queue (FIFO)

**Core Operations:**
```
+ MyQueue()                  // Initialize empty queue
+ void enqueue(T element)    // Add to rear, O(1)
+ T dequeue()                // Remove from front, O(1)
+ T peek()                   // View front, O(1)
+ boolean isEmpty()          // Check empty, O(1)
+ int size()                 // Get size, O(1)
+ void clear()               // Remove all
```

**Internal Structure (Linked List):**
```
- Node<T> front   // First node
- Node<T> rear    // Last node
- int size        // Element count

Node<T>:
  - T data
  - Node<T> next
```

**Usage in Nonogram:**
```
// Hint queue
MyQueue<Hint> hintQueue = new MyQueue<>();

// Add hints by priority
hintQueue.enqueue(new Hint("Row 5 complete", Priority.HIGH));
hintQueue.enqueue(new Hint("Check Column 3", Priority.MEDIUM));

// Get next hint
if (!hintQueue.isEmpty()) {
    Hint nextHint = hintQueue.dequeue();
    displayHint(nextHint);
}
```

---

## 3. Core Components

### 3.1 Enums

**CellState.java**
```
UNKNOWN  - Empty/white cell, not yet determined
FILLED   - Black cell, player thinks should be filled
MARKED   - Grey with X, player thinks should be empty

State Transitions:
UNKNOWN → (click) → FILLED → (click) → MARKED → (click) → UNKNOWN
```

**Difficulty.java**
```
EASY   - 5×5 grid,   2-5 min solve time
MEDIUM - 10×10 grid, 10-20 min solve time
HARD   - 15×15 grid, 30-60 min solve time
EXPERT - 20×20 grid, 40-90 min solve time
```

### 3.2 Cell Class

**Responsibilities:**
- Represent one grid cell
- Track current state (player's choice)
- Store actual solution value
- Provide state cycling

**Key Attributes:**
```
- CellState currentState     // Player's current choice
- boolean actualValue        // True = should be filled
- int row, col               // Position in grid
```

**Key Methods:**
```
+ void cycleState()          // UNKNOWN→FILLED→MARKED→UNKNOWN
+ boolean isCorrect()        // Compare currentState with actualValue
+ CellState getCurrentState()
+ void setCurrentState(CellState state)
+ boolean getActualValue()
```

**Correctness Logic:**
```
isCorrect():
  if currentState == FILLED && actualValue == true  → CORRECT
  if currentState == MARKED && actualValue == false → CORRECT
  if currentState == UNKNOWN                        → NEUTRAL
  otherwise                                         → INCORRECT
```

### 3.3 Clue Class

**Responsibilities:**
- Store numbers for a row/column
- Calculate space requirements
- Validate against line

**Key Attributes:**
```
- MyArrayList<Integer> numbers  // List of group sizes
```

**Key Methods:**
```
+ void addNumber(int num)
+ MyArrayList<Integer> getNumbers()
+ int getSum()                      // Sum of all numbers
+ int getMinimumSpaceNeeded()       // Sum + (groups-1) gaps
+ boolean isEmpty()                 // No groups or [0]
+ String toString()                 // "2 3 1" format
```

**Examples:**
```
Clue [3]:       "3" → 3 consecutive filled cells
Clue [2, 1]:    "2 1" → 2 filled, gap, 1 filled
Clue [1, 1, 1]: "1 1 1" → 1, gap, 1, gap, 1
Clue [0] or []: All empty
```

**Space Calculation:**
```
Clue [3, 2, 1] in 10-cell row:
  Sum: 3 + 2 + 1 = 6 cells
  Gaps: (3 groups - 1) = 2 minimum gaps
  Total needed: 6 + 2 = 8 cells minimum
```

### 3.4 Move Class

**Responsibilities:**
- Record a single cell state change
- Enable undo/redo

**Key Attributes:**
```
- int row, col
- CellState previousState
- CellState newState
- long timestamp
```

**Usage:**
```
// Recording a move
Move move = new Move(row, col, 
                     cell.getCurrentState(),  // before
                     nextState);               // after
undoStack.push(move);

// Undoing a move
Move lastMove = undoStack.pop();
board.setCellState(lastMove.row, lastMove.col, 
                   lastMove.previousState);
```

### 3.5 GameBoard Class

**Responsibilities:**
- Manage grid of cells
- Store clues
- Provide cell access/manipulation

**Key Attributes:**
```
- Cell[][] grid
- int rows, cols
- MyArrayList<Clue> rowClues
- MyArrayList<Clue> colClues
- boolean[][] solution  // Hidden from player
```

**Key Methods:**
```
+ Cell getCell(int row, int col)
+ void setCellState(int row, int col, CellState state)
+ void cycleCell(int row, int col)
+ Clue getRowClue(int row)
+ Clue getColClue(int col)
+ boolean isComplete()
+ void reset()
```

**Initialization:**
```
1. Create Cell[][] grid with all cells UNKNOWN
2. Generate row clues from solution
3. Generate column clues from solution
4. Hide solution from player view
```

### 3.6 GameState Class

**Responsibilities:**
- Track game progress
- Manage timer
- Count moves and hints

**Key Attributes:**
```
- GameTimer timer
- int moveCount
- int hintsUsed
- boolean isPaused
- boolean isSolved
- Difficulty difficulty
- long startTime
```

**Key Methods:**
```
+ void startTimer()
+ void pauseTimer()
+ void resumeTimer()
+ void incrementMoves()
+ void incrementHints()
+ int getElapsedSeconds()
+ void reset()
```

---

## 4. Game Logic

### 4.1 Clue Generation

**Algorithm: From Solution to Clues**
```
For each row:
  1. count = 0
  2. clues = []
  3. For each cell in row:
     a. If cell is filled: count++
     b. If cell is empty:
        - If count > 0: add count to clues, reset count = 0
  4. If count > 0 at end: add to clues
  5. If clues empty: add 0

For each column:
  Same algorithm, process vertically
```

**Example:**
```
Row: [■][■][□][■][□]
     2 filled, empty, 1 filled, empty
Result: Clue [2, 1]

Column: [■][■][■][□][□]
        3 consecutive filled
Result: Clue [3]
```

### 4.2 Solution Validation

**Check if Puzzle is Solved:**
```
For each cell in grid:
  if cell.currentState == FILLED:
    if cell.actualValue != true → INCORRECT
  if cell.currentState == MARKED:
    if cell.actualValue != false → INCORRECT

All checks pass → SOLVED
```

**Partial Validation (Progress):**
```
correctCells = 0
totalCells = rows × cols

For each cell:
  if cell.isCorrect():
    correctCells++

progress = (correctCells / totalCells) × 100%
```

### 4.3 Error Detection

**Find Specific Errors:**
```
errorCells = []

For each cell in grid:
  if cell.currentState == FILLED && cell.actualValue == false:
    errorCells.add(cell) // Wrong fill
  if cell.currentState == MARKED && cell.actualValue == true:
    errorCells.add(cell) // Wrong mark

return errorCells
```

**Line Validation:**
```
Check if row/column matches its clue:

1. Extract filled cell positions
2. Group consecutive fills
3. Compare groups with clue numbers
4. If mismatch → contradiction found
```

---

## 5. Hint System

### 5.1 Hint Types

**Type 1: Complete Line**
```
Detection:
  Row clue sum == row length
  → Must fill entire row

Example:
  10-cell row, clue [10]
  → All 10 cells must be filled

Hint: "Row X must be completely filled!"
```

**Type 2: Large Overlap**
```
Detection:
  Single-number clue > half the line length

Calculation:
  Row length = L, clue = N
  Overlap size = 2N - L
  Overlap start = L - N + 1
  Overlap end = N

Example:
  10-cell row, clue [8]
  Left-align:  [■■■■■■■■□□]
  Right-align: [□□■■■■■■■■]
  Overlap:     [□□■■■■■■□□]
              Cells 3-8 must be filled

Hint: "Row X has overlap! Cells Y-Z must be filled."
```

**Type 3: Clue Satisfied**
```
Detection:
  Count filled cells in row/column
  If count == clue sum → complete

Example:
  Row clue [3, 2], already has 3 + 2 = 5 filled
  → All other cells must be empty

Hint: "Row X is complete! Mark remaining cells."
```

**Type 4: Contradiction**
```
Detection:
  Filled cells don't match clue pattern

Example:
  Row clue [2, 2]
  Current: [■■■□...] (3 consecutive)
  → Violates "2, 2" requirement

Hint: "Row X has an error! Check your filled cells."
```

**Type 5: Edge Deduction**
```
Detection:
  Cells at edges can be determined

Example:
  Row clue [7], 10-cell row
  If first cell is filled:
    → Must extend at least 6 more cells right
  If last cell is filled:
    → Must extend at least 6 more cells left

Hint: "If cell X is filled, then cells Y-Z must also be filled."
```

### 5.2 Hint Generation Algorithm

```
generateHints():
  hintQueue.clear()
  
  // Priority 1: Complete lines
  for each row:
    if clueSum == rowLength:
      hintQueue.enqueue(completeLineHint(row))
  
  // Priority 2: Large overlaps
  for each row:
    if hasSingleClue and clue > rowLength/2:
      overlap = calculateOverlap(row, clue)
      if overlap.size() > 0:
        hintQueue.enqueue(overlapHint(row, overlap))
  
  // Priority 3: Satisfied clues
  for each row:
    if isClueComplete(row):
      hintQueue.enqueue(satisfiedHint(row))
  
  // Priority 4: Contradictions
  for each row:
    if hasContradiction(row):
      hintQueue.enqueue(errorHint(row))
  
  // Priority 5: General strategy
  if hintQueue.isEmpty():
    largestClue = findLargestClue()
    hintQueue.enqueue(strategyHint(largestClue))
```

### 5.3 Hint Display

```
Hint Dialog Format:
┌────────────────────────────────────┐
│         💡 HINT                    │
├────────────────────────────────────┤
│                                    │
│ [Hint Message]                     │
│                                    │
│ ┌────────────────────────────────┐ │
│ │ Explanation:                   │ │
│ │ [Visual or text explanation]   │ │
│ └────────────────────────────────┘ │
│                                    │
│ [Got It] [Show Me] [Next Hint]    │
│                                    │
│ Hints used: 2  Penalty: -100pts   │
└────────────────────────────────────┘
```

---

## 6. UI Integration

### 6.1 Game Panel Layout

```
┌────────────────────────────────────────────┐
│  NONOGRAM - Medium (10×10)                 │
├────────────────────────────────────────────┤
│ Time: 05:23    Moves: 47    Progress: 67% │
│ [Undo] [Redo] [Hint] [Check] [Reset]      │
├────────────────────────────────────────────┤
│                                            │
│        2  1  3  2  1  4  2  3  1  2       │
│        1  2  1  1  3  1  1  1  2  1       │
│     ┌──┬──┬──┬──┬──┬──┬──┬──┬──┬──┐      │
│   2 │  │■ │  │  │  │  │  │  │  │  │      │
│ 1 1 │■ │  │  │■ │  │  │  │  │  │  │      │
│   3 │■ │■ │■ │  │  │  │  │  │  │  │      │
│   1 │  │  │■ │  │  │  │  │  │  │  │      │
│ 2 1 │■ │■ │  │■ │  │  │  │  │  │  │      │
│   4 │  │  │  │  │■ │■ │■ │■ │  │  │      │
│   2 │  │  │  │  │  │  │■ │■ │  │  │      │
│   3 │  │  │  │  │  │  │  │■ │■ │■ │      │
│ 1 1 │  │  │  │  │  │  │■ │  │  │■ │      │
│   2 │  │  │  │  │  │  │  │  │■ │■ │      │
│     └──┴──┴──┴──┴──┴──┴──┴──┴──┴──┘      │
│                                            │
└────────────────────────────────────────────┘
```

### 6.2 Event Handling

**Cell Click Handler:**
```
onCellClick(row, col):
  1. Get current cell state
  2. Create Move object
  3. Push to undo stack
  4. Cycle cell state
  5. Clear redo stack
  6. Increment move counter
  7. Check if solved
  8. Repaint cell
```

**Undo Button Handler:**
```
onUndoClick():
  if undoStack.isEmpty():
    show message "Nothing to undo"
    return
  
  Move lastMove = undoStack.pop()
  board.setCellState(lastMove.row, lastMove.col, 
                     lastMove.previousState)
  redoStack.push(lastMove)
  moveCount--
  repaint()
```

**Hint Button Handler:**
```
onHintClick():
  timer.pause()
  
  if hintQueue.isEmpty():
    hintSystem.generateHints()
  
  if hintQueue.isEmpty():
    showDialog("No hints available. You're on the right track!")
  else:
    Hint hint = hintQueue.dequeue()
    showHintDialog(hint)
    hintsUsed++
  
  timer.resume()
```

**Check Button Handler:**
```
onCheckClick():
  result = validationEngine.checkSolution()
  
  if result.isComplete() and result.isCorrect():
    timer.stop()
    score = calculateScore()
    showVictoryDialog(score, time, moves)
  else if result.hasErrors():
    showErrorDialog(result.getErrorCount())
    // Option to highlight errors
  else:
    showProgressDialog(result.getProgress())
```

### 6.3 Visual Feedback

**Cell Rendering:**
```
UNKNOWN state:
  - Background: White
  - Border: Grey
  
FILLED state:
  - Background: Black
  - Border: Black
  
MARKED state:
  - Background: Light grey
  - Draw red X in center
  
Mouse hover:
  - Highlight row and column
  - Show cell coordinates (optional)
```

**Clue Rendering:**
```
Incomplete clue:
  - Color: Black
  - Font: Normal
  
Complete clue:
  - Color: Green
  - Font: Bold
  - Optional: ✓ checkmark
  
Current line hover:
  - Highlight corresponding clue
  - Increase font size slightly
```

---

## 7. Puzzle Management

### 7.1 Puzzle File Format

**Text Format (puzzles.txt):**
```
PUZZLE_ID=EASY_HEART
NAME=Heart Shape
DIFFICULTY=EASY
SIZE=5
GRID=
01100
11111
11111
01110
00100
END

PUZZLE_ID=MEDIUM_TREE
NAME=Christmas Tree
DIFFICULTY=MEDIUM
SIZE=10
GRID=
0000110000
0001111000
0011111100
0111111110
0001111000
0011111100
0111111110
1111111111
0001111000
0000110000
END
```

### 7.2 Loading Puzzles

**PuzzleLibrary.loadPuzzle():**
```
1. Open puzzle file
2. Search for PUZZLE_ID
3. Read puzzle metadata (name, difficulty, size)
4. Read GRID lines (size × size lines)
5. Convert '1' to true, '0' to false
6. Create boolean[][] solution
7. Generate clues using ClueGenerator
8. Return Puzzle object
```

### 7.3 Save/Load Game State

**Save Format (JSON-like):**
```
{
  "puzzleId": "MEDIUM_TREE",
  "difficulty": "MEDIUM",
  "gridSize": 10,
  "elapsedSeconds": 523,
  "moveCount": 87,
  "hintsUsed": 2,
  "savedAt": 1699876543210,
  "currentGrid": [
    "FFUUMMUUFF",
    "FFFFFFFUUU",
    ...
  ]
}

Legend:
F = FILLED
M = MARKED
U = UNKNOWN
```

**Save Operation:**
```
1. Serialize GameState
2. Serialize GameBoard (current cell states only)
3. Write to file: saves/game_TIMESTAMP.sav
4. Show confirmation message
```

**Load Operation:**
```
1. Read save file
2. Load puzzle by puzzleId
3. Restore cell states from saved grid
4. Restore timer, moves, hints
5. Resume game
```

---

## 8. Implementation Checklist

### 8.1 Phase 1: Core Data Structures
- [ ] Implement MyArrayList<T>
  - [ ] Constructor, add(), get(), set()
  - [ ] size(), isEmpty()
  - [ ] ensureCapacity() for growth
  - [ ] addAll(), clear()
- [ ] Implement MyStack<T>
  - [ ] Constructor with max size
  - [ ] push(), pop(), peek()
  - [ ] Handle capacity limit
- [ ] Implement MyQueue<T>
  - [ ] Node class
  - [ ] enqueue(), dequeue(), peek()
  - [ ] size(), isEmpty()

### 8.2 Phase 2: Core Models
- [ ] Create Enums
  - [ ] CellState (UNKNOWN, FILLED, MARKED)
  - [ ] Difficulty (EASY, MEDIUM, HARD, EXPERT)
- [ ] Implement Cell class
  - [ ] Attributes: currentState, actualValue, row, col
  - [ ] cycleState() method
  - [ ] isCorrect() method
- [ ] Implement Clue class
  - [ ] MyArrayList<Integer> numbers
  - [ ] getSum(), getMinimumSpaceNeeded()
- [ ] Implement Move class
  - [ ] Store row, col, previousState, newState
- [ ] Implement GameBoard class
  - [ ] Cell[][] grid
  - [ ] rowClues, colClues
  - [ ] getCell(), setCellState(), cycleCell()
- [ ] Implement GameState class
  - [ ] Timer, moveCount, hintsUsed
  - [ ] Start/pause/resume methods

### 8.3 Phase 3: Game Logic
- [ ] Implement ClueGenerator
  - [ ] generateRowClues(solution)
  - [ ] generateColumnClues(solution)
  - [ ] validateClues()
- [ ] Implement ValidationEngine
  - [ ] checkSolution() - full validation
  - [ ] calculateProgress() - partial check
  - [ ] findErrors() - locate wrong cells
- [ ] Implement SolutionChecker
  - [ ] isComplete() - all cells determined
  - [ ] isCorrect() - matches solution

### 8.4 Phase 4: Advanced Features
- [ ] Implement UndoRedoManager
  - [ ] MyStack<Move> undoStack, redoStack
  - [ ] undo(), redo()
  - [ ] recordMove()
- [ ] Implement HintSystem
  - [ ] MyQueue<Hint> hintQueue
  - [ ] generateHints() - all hint types
  - [ ] Overlap calculation
  - [ ] Contradiction detection
- [ ] Implement GameTimer
  - [ ] Start, pause, resume, stop
  - [ ] Thread-based timing
  - [ ] Format time display

### 8.5 Phase 5: Persistence
- [ ] Implement PuzzleLibrary
  - [ ] Load puzzles from file
  - [ ] Parse puzzle format
  - [ ] Store in MyArrayList
- [ ] Implement GameSaver
  - [ ] Save game state to file
  - [ ] Load game state from file
  - [ ] Serialize/deserialize grid

### 8.6 Phase 6: UI Integration
- [ ] Create GameController
  - [ ] handleCellClick()
  - [ ] handleUndo/Redo()
  - [ ] handleHint()
  - [ ] handleCheck()
- [ ] Create GamePanel
  - [ ] Draw grid
  - [ ] Draw clues
  - [ ] Handle mouse clicks
  - [ ] Update display
- [ ] Create MenuPanel
  - [ ] Difficulty selection
  - [ ] New game
  - [ ] Load game
  - [ ] Statistics
- [ ] Create Dialogs
  - [ ] Hint dialog
  - [ ] Victory dialog
  - [ ] Error dialog

### 8.7 Phase 7: Testing
- [ ] Test data structures
  - [ ] MyArrayList operations
  - [ ] MyStack undo/redo
  - [ ] MyQueue hint order
- [ ] Test game logic
  - [ ] Clue generation correctness
  - [ ] Validation accuracy
  - [ ] Win condition detection
- [ ] Test UI
  - [ ] Cell clicking cycles correctly
  - [ ] Undo/redo works properly
  - [ ] Hints display correctly
- [ ] Test edge cases
  - [ ] Empty rows (clue 0)
  - [ ] Full rows
  - [ ] Rapid clicking
  - [ ] Stack overflow (50+ undos)

### 8.8 Phase 8: Polish
- [ ] Add animations
  - [ ] Cell state transitions
  - [ ] Victory celebration
- [ ] Add sound effects (optional)
- [ ] Add statistics tracking
  - [ ] Best time
  - [ ] Total solved
  - [ ] Success rate
- [ ] Add themes/colors
- [ ] Performance optimization

---

## Quick Reference

### Key Algorithms

**Clue Generation:**
```java
public Clue generateRowClue(boolean[] row) {
    MyArrayList<Integer> numbers = new MyArrayList<>();
    int count = 0;
    
    for (boolean cell : row) {
        if (cell) {
            count++;
        } else {
            if (count > 0) {
                numbers.add(count);
                count = 0;
            }
        }
    }
    if (count > 0) numbers.add(count);
    
    return new Clue(numbers);
}
```

**Overlap Calculation:**
```java
public int[] calculateOverlap(int lineLength, int clueNumber) {
    if (clueNumber <= lineLength / 2) return new int[0];
    
    int overlapSize = 2 * clueNumber - lineLength;
    int start = lineLength - clueNumber;
    int end = clueNumber - 1;
    
    int[] overlap = new int[overlapSize];
    for (int i = 0; i < overlapSize; i++) {
        overlap[i] = start + i;
    }
    return overlap;
}
```

**Solution Validation:**
```java
public boolean isCorrect(Cell[][] grid, boolean[][] solution) {
    for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
            Cell cell = grid[i][j];
            boolean actual = solution[i][j];
            
            if (cell.getCurrentState() == CellState.FILLED && !actual) return false;
            if (cell.getCurrentState() == CellState.MARKED && actual) return false;
        }
    }
    return true;
}
```

### Code Templates

**Game Controller:**
```java
public class GameController {
    private GameBoard board;
    private GameState state;
    private UndoRedoManager undoManager;
    private HintSystem hintSystem;
    
    public void handleCellClick(int row, int col) {
        Cell cell = board.getCell(row, col);
        CellState prevState = cell.getCurrentState();
        
        Move move = new Move(row, col, prevState, getNextState(prevState));
        undoManager.recordMove(move);
        
        cell.cycleState();
        state.incrementMoves();
        
        if (isGameComplete()) {
            handleVictory();
        }
    }
    
    private CellState getNextState(CellState current) {
        switch (current) {
            case UNKNOWN: return CellState.FILLED;
            case FILLED: return CellState.MARKED;
            case MARKED: return CellState.UNKNOWN;
            default: return CellState.UNKNOWN;
        }
    }
}
```

**Puzzle Loading:**
```java
public class PuzzleLibrary {
    public Puzzle loadPuzzle(String puzzleId) {
        boolean[][] solution = parsePuzzleFile(puzzleId);
        
        MyArrayList<Clue> rowClues = new MyArrayList<>();
        MyArrayList<Clue> colClues = new MyArrayList<>();
        
        for (int i = 0; i < solution.length; i++) {
            rowClues.add(generateRowClue(solution[i]));
        }
        
        for (int j = 0; j < solution[0].length; j++) {
            boolean[] column = extractColumn(solution, j);
            colClues.add(generateRowClue(column));
        }
        
        return new Puzzle(puzzleId, solution, rowClues, colClues);
    }
}
```

### Performance Specifications

**Memory Usage:**
- MyArrayList: O(n) space, doubles when full
- MyStack: Limited to 50 moves
- MyQueue: Linked list implementation

**Time Complexity:**
- Cell click: O(1)
- Hint generation: O(n²)
- Solution validation: O(n²)
- Undo/Redo: O(1)

### Edge Cases

**Clue Generation:**
- Empty rows generate clue [0] or empty clue
- Single cells generate clue [1]
- Full rows generate clue [n] where n is row length

**State Management:**
- Clear redo stack when new move is made
- Pause timer during hint display
- Handle rapid clicking with debounce

**UI Synchronization:**
- Update move counter after each click
- Refresh progress percentage regularly
- Validate input bounds for grid access

### Minimal Implementation

```java
public class MinimalNonogram {
    public static void main(String[] args) {
        boolean[][] solution = {
            {false, true, true, false, false},
            {true, true, true, true, true},
            {true, true, true, true, true},
            {false, true, true, true, false},
            {false, false, true, false, false}
        };
        
        GameBoard board = new GameBoard(solution);
        GameController controller = new GameController(board);
        
        Scanner scanner = new Scanner(System.in);
        while (!controller.isComplete()) {
            board.display();
            System.out.print("Enter row col (0-4): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            controller.handleCellClick(row, col);
        }
        
        System.out.println("Puzzle solved!");
    }
}
```

### Implementation Priority

**Phase 1 - Core (Required):**
- MyArrayList, MyStack, MyQueue
- Cell, Clue, GameBoard classes
- Click handling and state cycling
- Clue generation from solution
- Win condition checking

**Phase 2 - Enhanced (Recommended):**
- Undo/Redo system
- Basic hint system (overlap detection)
- Progress tracking
- Save/Load functionality

**Phase 3 - Polish (Optional):**
- Advanced hints (contradiction detection)
- Statistics tracking
- Multiple difficulty levels
- Visual themes and animations