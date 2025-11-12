# Nonogram Game - Model Layer Architecture
## Technical Specification Document

---

## 1. Overview

This document specifies the Model layer of the Nonogram application following the Model-View-Controller (MVC) architectural pattern. The Model layer is responsible for all game logic, data structures, and business rules. It maintains complete independence from UI components and contains no Swing or AWT classes.

---

## 2. Model Layer Principles

### 2.1 Design Constraints
- **Pure Java**: No UI framework dependencies
- **Data Encapsulation**: All fields private with accessor methods
- **Immutability Where Applicable**: Puzzle solutions immutable after creation
- **State Management**: Clear state transitions with validation
- **No Collections Framework**: Uses only custom data structures

### 2.2 Layer Responsibilities
- Store game state (grid, cells, clues, timer)
- Enforce game rules (valid moves, win conditions)
- Manage puzzle data (solutions, clues, metadata)
- Track statistics (time, moves, score)
- Provide business logic API for Controller layer

---

## 3. CellState Enumeration

### 3.1 Purpose
Defines the three possible states of a cell in the game grid.

### 3.2 Enumeration Definition

```java
public enum CellState {
    UNKNOWN,    // Initial state, player hasn't determined
    FILLED,     // Player believes cell should be black/filled
    MARKED      // Player believes cell should be empty (X mark)
}
```

### 3.3 State Transition Rules

**Valid Transitions**:
```
UNKNOWN → FILLED    (Left click on empty cell)
FILLED → MARKED     (Left click on filled cell)
MARKED → UNKNOWN    (Left click on marked cell)

Alternative (Right-click):
UNKNOWN → MARKED    (Right-click on empty cell)
MARKED → UNKNOWN    (Right-click on marked cell)
```

**Transition Diagram**:
```
    ┌──────────┐
    │ UNKNOWN  │
    └────┬─────┘
         │ ↓ (click)
    ┌────┴─────┐
    │  FILLED  │
    └────┬─────┘
         │ ↓ (click)
    ┌────┴─────┐
    │  MARKED  │
    └────┬─────┘
         │ ↓ (click)
    └────→ UNKNOWN
```

### 3.4 Visual Representation Mapping

| State | Visual | Color | Symbol |
|-------|--------|-------|--------|
| UNKNOWN | Empty white cell | White/Light gray | None |
| FILLED | Solid filled cell | Black/Dark gray | ■ |
| MARKED | Cell with X mark | Light gray | X |

---

## 4. Cell Class

### 4.1 Purpose
Represents a single cell in the Nonogram grid, maintaining both player's current state and the actual solution value.

### 4.2 Class Structure

**Fields**:
```java
private int row;                    // Row position (0-indexed)
private int col;                    // Column position (0-indexed)
private CellState currentState;     // Player's current answer
private boolean actualValue;        // True solution (true=filled, false=empty)
```

### 4.3 Constructor Specifications

**Primary Constructor**:
```java
public Cell(int row, int col, boolean actualValue)
```
- **Parameters**:
  - `row`: Row index (must be >= 0)
  - `col`: Column index (must be >= 0)
  - `actualValue`: True if cell should be filled in solution, false if empty
- **Initialization**:
  - Sets position fields
  - Sets `actualValue` from parameter
  - Initializes `currentState` to `UNKNOWN`
- **Validation**: Throws IllegalArgumentException if row or col < 0

### 4.4 Core Methods

**State Management**:

```java
public CellState getCurrentState()
```
- Returns player's current state for this cell
- Used by View layer for rendering

```java
public void setState(CellState newState)
```
- Sets player's state for this cell
- **Parameters**: newState must not be null
- **Validation**: Throws IllegalArgumentException if newState is null
- Triggers validation check internally

```java
public void cycleState()
```
- Advances state according to transition rules: UNKNOWN → FILLED → MARKED → UNKNOWN
- Used when player clicks cell without specifying target state

**Solution Access**:

```java
public boolean getActualValue()
```
- Returns the true solution value for this cell
- Used during validation and hint generation
- **Should not be exposed to UI layer directly** (prevents cheating)

```java
public boolean isCorrect()
```
- Validates if player's current state matches solution
- **Logic**:
  - If `currentState == UNKNOWN`: return false (not answered)
  - If `currentState == FILLED`: return `actualValue == true`
  - If `currentState == MARKED`: return `actualValue == false`
- **Returns**: true if player's answer is correct, false otherwise

**Position Access**:

```java
public int getRow()
public int getCol()
```
- Return cell's grid coordinates
- Immutable after construction

**Reset**:

```java
public void reset()
```
- Sets `currentState` back to `UNKNOWN`
- Does NOT change `actualValue` (solution remains)
- Used when resetting puzzle

### 4.5 Usage Examples

**Scenario 1: Player Fills Cell**
```
Cell cell = new Cell(3, 5, true);  // Position (3,5), should be filled
// Initial state: currentState = UNKNOWN

player clicks cell
→ cell.setState(FILLED)
→ cell.isCorrect() returns true (matches actualValue)
```

**Scenario 2: Player Marks Cell Incorrectly**
```
Cell cell = new Cell(2, 7, true);  // Should be filled
// Initial state: currentState = UNKNOWN

player marks cell as empty
→ cell.setState(MARKED)
→ cell.isCorrect() returns false (actualValue is true, not false)
```

**Scenario 3: State Cycling**
```
Cell cell = new Cell(1, 1, false);
cell.cycleState();  // UNKNOWN → FILLED
cell.cycleState();  // FILLED → MARKED
cell.cycleState();  // MARKED → UNKNOWN
```

---

## 5. GameBoard Class

### 5.1 Purpose
Manages the complete grid of cells, provides access to rows/columns, stores clues, and tracks puzzle progress.

### 5.2 Class Structure

**Fields**:
```java
private int rows;                                           // Grid height
private int cols;                                           // Grid width
private Cell[][] cells;                                     // 2D array of cells
private MyArrayList<MyArrayList<Integer>> rowClues;        // Clues for each row
private MyArrayList<MyArrayList<Integer>> columnClues;     // Clues for each column
private MyArrayList<Integer> completedRows;                // Indices of solved rows
private MyArrayList<Integer> completedColumns;             // Indices of solved columns
```

### 5.3 Constructor Specifications

**Primary Constructor**:
```java
public GameBoard(int rows, int cols)
```
- **Parameters**:
  - `rows`: Number of rows (must be >= 5 and <= 25)
  - `cols`: Number of columns (must be >= 5 and <= 25)
- **Initialization**:
  - Creates `rows × cols` Cell array
  - Initializes all cells with default actualValue = false
  - Creates empty rowClues and columnClues lists
  - Initializes completedRows and completedColumns as empty lists
- **Validation**: Throws IllegalArgumentException if dimensions invalid

**Constructor from Solution**:
```java
public GameBoard(boolean[][] solution)
```
- **Parameters**: 2D boolean array representing puzzle solution
- **Initialization**:
  - Determines rows/cols from array dimensions
  - Creates Cell objects with actualValue from solution array
  - Generates clues automatically by analyzing solution
- **Validation**: Validates solution array is rectangular and within size limits

### 5.4 Cell Access Methods

```java
public Cell getCell(int row, int col)
```
- Retrieves specific cell by coordinates
- **Validation**: Throws IndexOutOfBoundsException if coordinates invalid
- **Returns**: Cell object at specified position

```java
public void setCell(int row, int col, Cell cell)
```
- Replaces cell at specified position
- Used during puzzle initialization
- **Validation**: Checks coordinates and cell not null

```java
public MyArrayList<Cell> getRow(int rowIndex)
```
- Returns all cells in specified row
- Creates new MyArrayList containing cells[rowIndex][0] through cells[rowIndex][cols-1]
- Used for row validation and hint generation

```java
public MyArrayList<Cell> getColumn(int colIndex)
```
- Returns all cells in specified column
- Creates new MyArrayList containing cells[0][colIndex] through cells[rows-1][colIndex]
- Used for column validation and hint generation

### 5.5 Clue Management Methods

```java
public MyArrayList<Integer> getRowClues(int rowIndex)
```
- Returns clue numbers for specified row
- **Example**: Row with pattern "■■ ■■■ ■" returns [2, 3, 1]

```java
public MyArrayList<Integer> getColumnClues(int colIndex)
```
- Returns clue numbers for specified column

```java
public void setRowClues(int rowIndex, MyArrayList<Integer> clues)
```
- Sets clue numbers for specified row
- Used during puzzle initialization
- **Validation**: Validates clues are valid for row length

```java
public void setColumnClues(int colIndex, MyArrayList<Integer> clues)
```
- Sets clue numbers for specified column

```java
public void generateCluesFromSolution()
```
- Analyzes solution pattern in cells array
- Generates appropriate clue numbers for all rows and columns
- **Algorithm** (for each row):
  1. Iterate through row cells
  2. Count consecutive filled cells (actualValue == true)
  3. Add count to clue list when gap or end reached
  4. Repeat for all rows and columns

**Clue Generation Example**:
```
Row pattern: ■■ _ ■■■ _ ■
             ↓     ↓     ↓
Clues:      [2,   3,    1]

Column pattern: ■ _ ■ ■ ■ _
                ↓   ↓ ↓ ↓
Clues:         [1, 3]
```

### 5.6 Validation Methods

```java
public boolean isRowComplete(int rowIndex)
```
- Checks if all cells in row are correctly solved
- **Logic**:
  1. Get all cells in row
  2. For each cell, check if isCorrect() returns true
  3. Return true only if ALL cells correct
- **Returns**: true if row completely and correctly filled

```java
public boolean isColumnComplete(int colIndex)
```
- Checks if all cells in column are correctly solved
- Logic identical to row validation

```java
public boolean validateRow(int rowIndex)
```
- Comprehensive row validation including clue matching
- **Logic**:
  1. Get all cells in row
  2. Count consecutive FILLED cells to form player's pattern
  3. Compare player's pattern against rowClues
  4. Return true if patterns match exactly
- **Example**:
  ```
  Clues: [2, 3, 1]
  Player pattern: ■■ _ ■■■ _ ■  → Valid
  Player pattern: ■■■ _ ■■ _ ■  → Invalid (doesn't match clues)
  ```

```java
public boolean validateColumn(int colIndex)
```
- Comprehensive column validation including clue matching

```java
public boolean isPuzzleComplete()
```
- Checks if entire puzzle is correctly solved
- **Logic**:
  1. Iterate through ALL cells
  2. Check each cell.isCorrect()
  3. Return true only if ALL cells correct
- Used to determine win condition

```java
public MyArrayList<Cell> getIncorrectCells()
```
- Finds all cells where player's state doesn't match solution
- Returns MyArrayList of Cell references for error highlighting
- Used by validation system to show mistakes

### 5.7 Progress Tracking Methods

```java
public void updateCompletedLines()
```
- Scans all rows and columns
- Updates completedRows and completedColumns lists
- **Logic**:
  1. Clear existing completed lists
  2. For each row, if isRowComplete(), add row index to completedRows
  3. For each column, if isColumnComplete(), add col index to completedColumns
- Called after each player move

```java
public MyArrayList<Integer> getCompletedRows()
```
- Returns list of row indices that are completely solved
- Used by View to display green checkmarks

```java
public MyArrayList<Integer> getCompletedColumns()
```
- Returns list of column indices that are completely solved

```java
public double getCompletionPercentage()
```
- Calculates percentage of correctly filled cells
- **Formula**: (correctCells / totalCells) × 100
- **Returns**: double between 0.0 and 100.0

### 5.8 Board Manipulation Methods

```java
public void reset()
```
- Resets all cells to UNKNOWN state
- Does NOT change solution values
- Clears completedRows and completedColumns
- Used when player restarts puzzle

```java
public void clear()
```
- Same as reset() - sets all cells to UNKNOWN

```java
public GameBoard copy()
```
- Creates deep copy of entire board
- Used for save states, undo snapshots
- Copies all cells, clues, and metadata

### 5.9 Dimension Access

```java
public int getRows()
public int getCols()
```
- Return grid dimensions
- Immutable after construction

---

## 6. Clue Class

### 6.1 Purpose
Represents the sequence of numbers for a single row or column that indicate groups of consecutive filled cells.

### 6.2 Class Structure

**Fields**:
```java
private MyArrayList<Integer> numbers;     // Sequence of clue numbers
private int lineIndex;                     // Which row/column (0-indexed)
private boolean isRow;                     // true=row, false=column
```

### 6.3 Constructor

```java
public Clue(int lineIndex, boolean isRow)
```
- **Parameters**:
  - `lineIndex`: Row or column index
  - `isRow`: true for row clue, false for column clue
- **Initialization**: Creates empty numbers list

### 6.4 Core Methods

```java
public void addNumber(int value)
```
- Appends clue number to sequence
- **Validation**: Throws IllegalArgumentException if value <= 0

```java
public MyArrayList<Integer> getNumbers()
```
- Returns complete clue sequence
- **Returns**: Reference to internal list (consider returning copy for immutability)

```java
public int getNumber(int index)
```
- Returns specific clue number by position
- **Validation**: Throws IndexOutOfBoundsException if invalid index

```java
public int getCount()
```
- Returns number of clue values in sequence
- **Returns**: numbers.size()

```java
public int getTotalFilledCells()
```
- Calculates sum of all clue numbers
- **Example**: Clue [2, 3, 1] → returns 6
- **Returns**: Total cells that must be filled

```java
public int getMinimumLength()
```
- Calculates minimum space needed for this clue
- **Formula**: sum of numbers + (count - 1) for mandatory gaps
- **Example**: Clue [2, 3, 1] → 2+3+1 + 2 gaps = 8 minimum
- Used for validation and hint generation

```java
public boolean isValid(int lineLength)
```
- Checks if clue can fit in line of given length
- **Logic**: Returns getMinimumLength() <= lineLength
- **Example**: Clue [2, 3, 1] needs 8, valid for line length 10, invalid for line length 7

```java
public String toString()
```
- Formats clue for display
- **Format**: Numbers separated by spaces
- **Example**: [2, 3, 1] → "2 3 1"

### 6.5 Accessors

```java
public int getLineIndex()
public boolean isRow()
```
- Return clue metadata

---

## 7. Puzzle Class

### 7.1 Purpose
Complete puzzle definition including solution, clues, metadata, and difficulty rating.

### 7.2 Class Structure

**Fields**:
```java
private String puzzleId;              // Unique identifier (e.g., "EASY_HEART")
private String name;                  // Display name (e.g., "Heart Shape")
private Difficulty difficulty;        // EASY, MEDIUM, HARD, EXPERT
private int size;                     // Grid dimension (5, 10, 15, 20, 25)
private boolean[][] solution;         // True solution grid
private MyArrayList<MyArrayList<Integer>> rowClues;
private MyArrayList<MyArrayList<Integer>> columnClues;
private String author;                // Puzzle creator
private String description;           // Optional description
private long createdDate;             // Creation timestamp
```

### 7.3 Difficulty Enumeration

```java
public enum Difficulty {
    EASY,       // 5×5 to 8×8, simple patterns
    MEDIUM,     // 10×10 to 12×12, moderate complexity
    HARD,       // 15×15 to 18×18, complex patterns
    EXPERT      // 20×20 to 25×25, very complex
}
```

### 7.4 Constructor Specifications

**Full Constructor**:
```java
public Puzzle(String puzzleId, String name, Difficulty difficulty, 
              boolean[][] solution)
```
- **Parameters**:
  - `puzzleId`: Unique identifier (required, non-empty)
  - `name`: Display name (required, non-empty)
  - `difficulty`: Difficulty level (required, not null)
  - `solution`: 2D boolean array (required, must be rectangular)
- **Initialization**:
  - Validates all parameters
  - Copies solution array (deep copy for immutability)
  - Determines size from solution dimensions
  - Generates clues automatically from solution
  - Sets createdDate to current timestamp
- **Validation**: Throws IllegalArgumentException for invalid parameters

**Constructor with Explicit Clues**:
```java
public Puzzle(String puzzleId, String name, Difficulty difficulty,
              int size, MyArrayList<MyArrayList<Integer>> rowClues,
              MyArrayList<MyArrayList<Integer>> columnClues)
```
- Used when loading puzzle from file with pre-generated clues
- Does NOT have solution array (solution derived from clues during play)

### 7.5 Core Methods

**Metadata Access**:

```java
public String getPuzzleId()
public String getName()
public Difficulty getDifficulty()
public int getSize()
public String getAuthor()
public String getDescription()
public long getCreatedDate()
```
- Standard getters for puzzle metadata

**Solution Access**:

```java
public boolean[][] getSolution()
```
- Returns deep copy of solution array
- **Important**: Must return COPY to prevent external modification
- Used to initialize GameBoard

```java
public boolean getSolutionValue(int row, int col)
```
- Returns single cell value from solution
- **Validation**: Throws IndexOutOfBoundsException for invalid coordinates

**Clue Access**:

```java
public MyArrayList<MyArrayList<Integer>> getRowClues()
public MyArrayList<MyArrayList<Integer>> getColumnClues()
```
- Returns all clues for rows/columns
- Consider returning copies for immutability

```java
public MyArrayList<Integer> getRowClue(int rowIndex)
public MyArrayList<Integer> getColumnClue(int colIndex)
```
- Returns clues for specific line

**Puzzle Generation**:

```java
public void generateClues()
```
- Analyzes solution array
- Generates appropriate clue numbers for all rows and columns
- **Algorithm**:
  ```
  For each row:
    1. Initialize empty clue list
    2. Initialize consecutiveCount = 0
    3. For each column in row:
       a. If solution[row][col] == true:
          - Increment consecutiveCount
       b. Else if consecutiveCount > 0:
          - Add consecutiveCount to clue list
          - Reset consecutiveCount = 0
    4. If consecutiveCount > 0 at end:
       - Add final count to clue list
    5. If clue list empty:
       - Add single 0 (means no filled cells)
  
  Repeat for columns (iterate rows within each column)
  ```

**Validation**:

```java
public boolean isValid()
```
- Validates puzzle is solvable and well-formed
- **Checks**:
  1. Solution dimensions match declared size
  2. All row clues valid for size
  3. All column clues valid for size
  4. Sum of row clues equals total filled cells
  5. Sum of column clues equals total filled cells
  6. Puzzle has at least one filled cell
- **Returns**: true if puzzle passes all checks

**Serialization Support**:

```java
public String toFileFormat()
```
- Converts puzzle to saveable string format
- Used by PuzzleSaver
- **Format**: Custom text format with sections for metadata, solution, clues

```java
public static Puzzle fromFileFormat(String data)
```
- Parses puzzle from saved format
- Static factory method
- Returns new Puzzle instance

### 7.6 Usage Example

```
Creating puzzle programmatically:

boolean[][] heartPattern = {
    {false, true, false, true, false},
    {true, true, true, true, true},
    {true, true, true, true, true},
    {false, true, true, true, false},
    {false, false, true, false, false}
};

Puzzle heart = new Puzzle(
    "EASY_HEART",
    "Heart Shape",
    Difficulty.EASY,
    heartPattern
);

// Clues generated automatically:
// Row clues: [1,1], [5], [5], [3], [1]
// Column clues: [2,1], [4], [5], [4], [2,1]
```

---

## 8. Move Class

### 8.1 Purpose
Records a single player action for undo/redo functionality, capturing complete state change information.

### 8.2 Class Structure

**Fields**:
```java
private int row;                      // Cell row position
private int col;                      // Cell column position
private CellState previousState;      // State before move
private CellState newState;           // State after move
private long timestamp;               // When move occurred (milliseconds)
```

### 8.3 Constructor

```java
public Move(int row, int col, CellState previousState, 
            CellState newState, long timestamp)
```
- **Parameters**: All fields required
- **Validation**:
  - row, col must be >= 0
  - States must not be null
  - timestamp must be >= 0

### 8.4 Core Methods

**Accessors**:

```java
public int getRow()
public int getCol()
public CellState getPreviousState()
public CellState getNewState()
public long getTimestamp()
```
- Standard getters
- All fields immutable after construction

**Undo/Redo Operations**:

```java
public void undo(GameBoard board)
```
- Reverses this move on the board
- **Logic**:
  1. Get cell at (row, col) from board
  2. Set cell state to previousState
  3. Update board's completed lines
- Used when popping from undo stack

```java
public void redo(GameBoard board)
```
- Reapplies this move on the board
- **Logic**:
  1. Get cell at (row, col) from board
  2. Set cell state to newState
  3. Update board's completed lines
- Used when popping from redo stack

**Utility Methods**:

```java
public boolean isStateChange()
```
- Returns true if previousState != newState
- Used to filter out redundant moves

```java
public String toString()
```
- Format: "Move[(row,col): previousState → newState @ timestamp]"
- Used for debugging and logging

### 8.5 Usage in Undo/Redo System

**Recording Move**:
```
Player clicks cell (3, 5):
  Before click: cell.getCurrentState() = UNKNOWN
  Player action: Change to FILLED
  After click: cell.getCurrentState() = FILLED

Create Move:
  Move move = new Move(3, 5, UNKNOWN, FILLED, currentTimeMillis());
  undoStack.push(move);
  redoStack.clear();  // New move invalidates redo history
```

**Undo Operation**:
```
Player presses Undo:
  Move lastMove = undoStack.pop();
  lastMove.undo(gameBoard);
  redoStack.push(lastMove);
```

**Redo Operation**:
```
Player presses Redo:
  Move moveToRedo = redoStack.pop();
  moveToRedo.redo(gameBoard);
  undoStack.push(moveToRedo);
```

---

## 9. GameState Class

### 9.1 Purpose
Tracks overall game progress, timing, scoring, and session statistics.

### 9.2 Class Structure

**Fields**:
```java
private int elapsedSeconds;           // Total time elapsed
private int moveCount;                // Total moves made
private int hintsUsed;                // Number of hints requested
private int errorsFound;              // Number of incorrect cells
private int score;                    // Calculated score
private double completionPercentage;  // Puzzle completion (0-100)
private boolean isPaused;             // Timer paused flag
private boolean isComplete;           // Puzzle solved flag
private long startTime;               // Session start timestamp
private long pauseStartTime;          // When pause began
private long totalPausedTime;         // Accumulated pause time
```

### 9.3 Constructor

```java
public GameState()
```
- Initializes all counters to zero
- Sets flags to false
- Does NOT start timer (call startTimer() explicitly)

### 9.4 Timer Management Methods

```java
public void startTimer()
```
- Records session start time
- **Logic**:
  1. If already started, do nothing
  2. Set startTime = System.currentTimeMillis()
  3. Set isPaused = false
- Called when puzzle begins

```java
public void pauseTimer()
```
- Pauses time tracking
- **Logic**:
  1. If already paused, do nothing
  2. Set pauseStartTime = System.currentTimeMillis()
  3. Set isPaused = true
- Used when player pauses game or views menus

```java
public void resumeTimer()
```
- Resumes time tracking after pause
- **Logic**:
  1. If not paused, do nothing
  2. Calculate pauseDuration = System.currentTimeMillis() - pauseStartTime
  3. Add pauseDuration to totalPausedTime
  4. Set isPaused = false
- Excludes pause time from elapsed calculation

```java
public void stopTimer()
```
- Finalizes timing (puzzle complete or abandoned)
- **Logic**:
  1. Calculate final elapsedSeconds
  2. Set isComplete flag if puzzle solved
- Called when puzzle ends

```java
public int getElapsedSeconds()
```
- Returns current elapsed time
- **Formula**: (currentTime - startTime - totalPausedTime) / 1000
- Excludes pause time from calculation
- Updates elapsedSeconds field and returns value

### 9.5 Statistics Management

```java
public void incrementMoves()
```
- Increments move counter
- Called after each cell state change

```java
public void incrementHints()
```
- Increments hint counter
- Applies score penalty for hint usage
- Called when player requests hint

```java
public void setErrors(int count)
```
- Records number of incorrect cells found
- Called during validation check
- Affects score calculation

```java
public void updateCompletion(double percentage)
```
- Updates completion percentage
- **Parameters**: percentage between 0.0 and 100.0
- **Validation**: Clamps to [0, 100] range
- Called after each move to update progress

### 9.6 Score Calculation

```java
public void calculateScore()
```
- Computes final score based on performance metrics
- **Formula**:
  ```
  baseScore = 1000
  
  timeBonus = max(0, 500 - elapsedSeconds)  // Faster = higher bonus
  efficiencyBonus = max(0, 300 - moveCount) // Fewer moves = higher bonus
  hintPenalty = hintsUsed * 50              // -50 per hint
  errorPenalty = errorsFound * 25           // -25 per error
  
  difficultyMultiplier = based on puzzle size:
    5×5:   1.0
    10×10: 1.5
    15×15: 2.0
    20×20: 2.5
    25×25: 3.0
  
  score = (baseScore + timeBonus + efficiencyBonus - hintPenalty - errorPenalty) 
          × difficultyMultiplier
  
  Minimum score: 0 (no negative scores)
  ```

```java
public int getScore()
```
- Returns calculated score
- Call calculateScore() first to ensure current value

### 9.7 State Query Methods

```java
public int getMoveCount()
public int getHintsUsed()
public int getErrorsFound()
public double getCompletionPercentage()
public boolean isPaused()
public boolean isComplete()
```
- Standard getters for state fields

### 9.8 Reset Methods

```java
public void reset()
```
- Clears all statistics
- Resets counters to zero
- Resets flags to false
- Does NOT preserve any data
- Used when starting new puzzle

```java
public void resetTimer()
```
- Resets only time-related fields
- Preserves move count and other statistics
- Used when restarting same puzzle

---

## 10. Hint Class

### 10.1 Purpose
Represents a single hint with message, priority, and affected cells for visual highlighting.

### 10.2 Class Structure

**Fields**:
```java
private String message;                           // Hint text for display
private HintType type;                            // Category of hint
private int priority;                             // 1=highest, 5=lowest
private MyArrayList<CellPosition> affectedCells; // Cells to highlight
private int lineIndex;                            // Row/column index (-1 if not applicable)
private boolean isRow;                            // true=row, false=column
```

### 10.3 HintType Enumeration

```java
public enum HintType {
    COMPLETE_LINE,      // Entire row/column can be filled
    OVERLAP,            // Cells forced by clue overlap
    EDGE_DEDUCTION,     // Cells at edges determinable
    SIMPLE_BOXES,       // Small isolated groups
    ERROR,              // Contradiction detected
    STRATEGY            // General solving approach
}
```

### 10.4 Priority System

| Priority | Type | Description | Example |
|----------|------|-------------|---------|
| 1 | COMPLETE_LINE | Most obvious | "Row 5: clue is 10, fill all cells" |
| 2 | OVERLAP | Clear deductions | "Row 3: cells 4-7 overlap, must be filled" |
| 3 | EDGE_DEDUCTION | Edge reasoning | "Column 2: 3 filled + clue 3, mark rest empty" |
| 4 | SIMPLE_BOXES | Pattern recognition | "Row 7: single clue 2 fits only positions 1-2" |
| 5 | ERROR | Mistake indicator | "Row 9: too many filled cells for clues" |

### 10.5 Constructor

```java
public Hint(String message, HintType type, int priority)
```
- **Parameters**:
  - `message`: Descriptive hint text (required, non-empty)
  - `type`: Hint category (required, not null)
  - `priority`: 1-5, where 1 is highest priority (required)
- **Initialization**:
  - Creates empty affectedCells list
  - Sets lineIndex to -1 (not applicable by default)
- **Validation**: Validates priority in range [1, 5]

### 10.6 Core Methods

**Cell Management**:

```java
public void addAffectedCell(int row, int col)
```
- Adds cell position to highlight list
- **Parameters**: Cell coordinates
- Used during hint generation to mark relevant cells

```java
public void addAffectedCells(MyArrayList<CellPosition> cells)
```
- Adds multiple cell positions at once
- **Parameters**: List of cell positions to highlight

```java
public MyArrayList<CellPosition> getAffectedCells()
```
- Returns list of cells to highlight when showing hint
- Used by View layer for visual emphasis

**Line Association**:

```java
public void setLine(int lineIndex, boolean isRow)
```
- Associates hint with specific row or column
- **Parameters**:
  - `lineIndex`: Row or column index
  - `isRow`: true for row, false for column
- Used when hint applies to entire line

**Accessors**:

```java
public String getMessage()
public HintType getType()
public int getPriority()
public int getLineIndex()
public boolean isRow()
public boolean hasLine()
```
- Standard getters
- `hasLine()` returns true if lineIndex >= 0

**Comparison**:

```java
public int compareTo(Hint other)
```
- Compares hints by priority for sorting
- **Returns**: 
  - Negative if this hint higher priority (lower number)
  - Positive if this hint lower priority
  - 0 if equal priority
- Used to sort hints before enqueueing

### 10.7 Hint Generation Examples

**Example 1: Complete Line Hint**
```
Row 5 has clue [10] in 10×10 grid
→ All 10 cells must be filled

Hint hint = new Hint(
    "Row 5 must be completely filled (clue: 10)",
    HintType.COMPLETE_LINE,
    1  // Highest priority
);
hint.setLine(5, true);
for (int col = 0; col < 10; col++) {
    hint.addAffectedCell(5, col);
}
```

**Example 2: Overlap Hint**
```
Row 3 has clue [7] in 10-cell row
Minimum positions: cells 0-6 or 3-9
Overlap: cells 3-6 must be filled

Hint hint = new Hint(
    "Row 3 cells 3-6 must be filled (clue overlap)",
    HintType.OVERLAP,
    2  // High priority
);
hint.setLine(3, true);
hint.addAffectedCell(3, 3);
hint.addAffectedCell(3, 4);
hint.addAffectedCell(3, 5);
hint.addAffectedCell(3, 6);
```

**Example 3: Error Detection Hint**
```
Row 7 has 6 cells filled but clue sum is only 5

Hint hint = new Hint(
    "Row 7 has too many filled cells (clue: 2 1 2)",
    HintType.ERROR,
    5  // Lower priority (shown after constructive hints)
);
hint.setLine(7, true);
// Add cells that exceed clue requirements
```

---

## 11. PuzzleStats Class

### 11.1 Purpose
Tracks performance statistics for individual puzzles across multiple play sessions.

### 11.2 Class Structure

**Fields**:
```java
private String puzzleId;              // Associated puzzle identifier
private int timesAttempted;           // Total times puzzle started
private int timesSolved;              // Total times successfully completed
private int timesGivenUp;             // Total times player quit/reset
private int bestTime;                 // Fastest completion (seconds)
private int averageTime;              // Average completion time
private int bestMoves;                // Fewest moves to solve
private int averageMoves;             // Average moves per completion
private int bestScore;                // Highest score achieved
private int totalHintsUsed;           // Cumulative hints across all attempts
private long firstAttemptedDate;      // First play timestamp
private long lastSolvedDate;          // Most recent completion timestamp
private MyArrayList<Integer> solveHistory;  // List of completion times
```

### 11.3 Constructor

```java
public PuzzleStats(String puzzleId)
```
- **Parameters**: puzzleId (required, non-empty)
- **Initialization**:
  - Sets puzzleId
  - Initializes all counters to 0
  - Sets bestTime to Integer.MAX_VALUE (no best yet)
  - Sets bestMoves to Integer.MAX_VALUE
  - Sets bestScore to 0
  - Creates empty solveHistory list
  - Sets firstAttemptedDate to current time

### 11.4 Recording Methods

**Attempt Tracking**:

```java
public void recordAttempt()
```
- Increments timesAttempted counter
- Called when puzzle is started
- Updates firstAttemptedDate if first attempt

```java
public void recordSolution(int elapsedTime, int moves, int hints, int score)
```
- Records successful puzzle completion
- **Parameters**: Performance metrics from completed game
- **Actions**:
  1. Increment timesSolved
  2. Add elapsedTime to solveHistory
  3. Update bestTime if elapsedTime < bestTime
  4. Update bestMoves if moves < bestMoves
  5. Update bestScore if score > bestScore
  6. Add hints to totalHintsUsed
  7. Recalculate averageTime and averageMoves
  8. Update lastSolvedDate to current time

```java
public void recordGiveUp()
```
- Records puzzle abandonment
- Increments timesGivenUp counter
- Called when player quits or resets to different puzzle

**Average Calculation**:

```java
private void calculateAverages()
```
- Recalculates average time and moves
- **Logic**:
  ```
  averageTime = sum of all times in solveHistory / solveHistory.size()
  averageMoves = similar calculation for moves
  ```
- Called automatically after recordSolution()

### 11.5 Query Methods

**Statistics Access**:

```java
public String getPuzzleId()
public int getTimesAttempted()
public int getTimesSolved()
public int getTimesGivenUp()
public int getBestTime()
public int getAverageTime()
public int getBestMoves()
public int getAverageMoves()
public int getBestScore()
public int getTotalHintsUsed()
public long getFirstAttemptedDate()
public long getLastSolvedDate()
```
- Standard getters for all fields

**Derived Statistics**:

```java
public double getSolveRate()
```
- Calculates percentage of attempts that resulted in completion
- **Formula**: (timesSolved / timesAttempted) × 100
- **Returns**: double between 0.0 and 100.0
- **Handles**: Returns 0.0 if timesAttempted == 0

```java
public double getAverageHintsPerSolve()
```
- Calculates average hints used per successful completion
- **Formula**: totalHintsUsed / timesSolved
- **Returns**: double, or 0.0 if timesSolved == 0

```java
public boolean hasBeenSolved()
```
- Quick check if puzzle ever completed
- **Returns**: timesSolved > 0

```java
public int getDaysSinceLastSolved()
```
- Calculates days elapsed since last completion
- **Formula**: (currentTime - lastSolvedDate) / (1000 × 60 × 60 × 24)
- **Returns**: Number of days, or -1 if never solved

**Performance Rating**:

```java
public String getPerformanceRating()
```
- Categorizes player's performance on this puzzle
- **Logic**:
  ```
  if (solveRate >= 90 && averageHintsPerSolve < 2):
      return "EXPERT"
  else if (solveRate >= 70 && averageHintsPerSolve < 5):
      return "PROFICIENT"
  else if (solveRate >= 40):
      return "COMPETENT"
  else:
      return "LEARNING"
  ```

### 11.6 Reset Methods

```java
public void resetStats()
```
- Clears all statistics
- Preserves puzzleId
- Sets counters to 0, best values to defaults
- Clears solveHistory
- Used for fresh start on puzzle

### 11.7 Usage Example

```
Player completes "MEDIUM_TREE" puzzle:

PuzzleStats stats = statisticsMap.get("MEDIUM_TREE");
if (stats == null) {
    stats = new PuzzleStats("MEDIUM_TREE");
}

stats.recordSolution(
    elapsedTime = 567,    // 9 minutes 27 seconds
    moves = 142,
    hints = 3,
    score = 1850
);

statisticsMap.put("MEDIUM_TREE", stats);

// Later retrieval:
stats = statisticsMap.get("MEDIUM_TREE");
System.out.println("Best time: " + stats.getBestTime() + " seconds");
System.out.println("Solve rate: " + stats.getSolveRate() + "%");
System.out.println("Rating: " + stats.getPerformanceRating());
```

---

## 12. CellPosition Helper Class

### 12.1 Purpose
Simple data structure to represent a cell's coordinates, used throughout the system for position tracking.

### 12.2 Class Structure

**Fields**:
```java
private int row;
private int col;
```

### 12.3 Constructor

```java
public CellPosition(int row, int col)
```
- **Parameters**: Row and column coordinates
- **Validation**: Accepts any integer values (validation done by caller)

### 12.4 Methods

```java
public int getRow()
public int getCol()
```
- Standard getters
- Fields immutable after construction

```java
public boolean equals(Object obj)
```
- Compares positions for equality
- **Logic**: Two CellPosition objects equal if row and col match
- Overrides Object.equals()

```java
public int hashCode()
```
- Generates hash code for HashMap use
- **Formula**: 31 × row + col
- Overrides Object.hashCode()

```java
public String toString()
```
- Format: "(row, col)"
- Example: "(3, 5)"

### 12.5 Usage Example

```
MyArrayList<CellPosition> highlightCells = new MyArrayList<>();
highlightCells.add(new CellPosition(3, 4));
highlightCells.add(new CellPosition(3, 5));
highlightCells.add(new CellPosition(3, 6));

// Later in View layer:
for (int i = 0; i < highlightCells.size(); i++) {
    CellPosition pos = highlightCells.get(i);
    drawHighlight(graphics, pos.getRow(), pos.getCol());
}
```

---

## 13. Model Layer Integration

### 13.1 Class Dependencies

```
Puzzle
    ↓ contains
GameBoard
    ↓ contains
Cell (with CellState)
    
GameState ← tracks → GameBoard progress
    
Move ← records → Cell state changes
    
Hint ← references → CellPosition
                 ↓ highlights
                Cell locations

PuzzleStats ← tracks → Puzzle performance
```

### 13.2 Data Flow Examples

**Scenario 1: Loading Puzzle**
```
1. PuzzleLoader reads Puzzle from file
2. GameController creates GameBoard from Puzzle.getSolution()
3. GameBoard creates Cell[][] array with actualValues from solution
4. GameBoard generates/receives clues from Puzzle
5. GameState initialized with reset counters
6. View displays GameBoard grid and clues
```

**Scenario 2: Player Makes Move**
```
1. Player clicks cell at (3, 5)
2. Controller gets Cell from GameBoard.getCell(3, 5)
3. Store current state: CellState oldState = cell.getCurrentState()
4. Controller calls cell.cycleState() or cell.setState(FILLED)
5. Create Move(3, 5, oldState, newState, timestamp)
6. Push Move to undoStack
7. Clear redoStack
8. GameState.incrementMoves()
9. GameBoard.updateCompletedLines()
10. GameState.updateCompletion(board.getCompletionPercentage())
11. View repaints affected cell
```

**Scenario 3: Undo Operation**
```
1. Player clicks Undo button
2. Controller checks if undoStack.isEmpty()
3. Pop Move from undoStack
4. Call move.undo(gameBoard)
5. Push Move to redoStack
6. GameState decrements moveCount
7. GameBoard.updateCompletedLines()
8. View repaints affected cell
```

**Scenario 4: Hint Request**
```
1. Player clicks Hint button
2. HintController generates hints from current GameBoard state
3. Sort hints by priority
4. Enqueue hints into hintQueue
5. Dequeue first hint
6. GameState.incrementHints()
7. View displays hint.getMessage()
8. View highlights hint.getAffectedCells()
```

**Scenario 5: Puzzle Completion**
```
1. After player move, Controller calls GameBoard.isPuzzleComplete()
2. If true:
   a. GameState.stopTimer()
   b. GameState.calculateScore()
   c. Retrieve/create PuzzleStats for current puzzleId
   d. stats.recordSolution(time, moves, hints, score)
   e. Save stats to statisticsMap
   f. View displays completion dialog with statistics
   g. Update high scores if applicable
```

### 13.3 Validation Flow

**Cell Validation**:
```
Cell.isCorrect() checks:
    currentState matches actualValue pattern
    FILLED → actualValue must be true
    MARKED → actualValue must be false
    UNKNOWN → considered incorrect (not answered)
```

**Line Validation**:
```
GameBoard.validateRow(rowIndex):
    1. Get all cells in row
    2. Extract filled cell positions (where currentState == FILLED)
    3. Generate clue pattern from positions
    4. Compare generated pattern to rowClues[rowIndex]
    5. Return true if patterns match exactly
```

**Complete Puzzle Validation**:
```
GameBoard.isPuzzleComplete():
    1. Iterate through all cells
    2. For each cell, call cell.isCorrect()
    3. Return true only if ALL cells correct
    4. Otherwise return false
```

---

## 14. Model Layer Design Patterns

### 14.1 Encapsulation Pattern

**All fields private with accessor methods**:
```
✓ Correct:
    private int row;
    public int getRow() { return row; }

✗ Incorrect:
    public int row;  // Direct field access violates encapsulation
```

### 14.2 Immutability Pattern

**Immutable fields after construction**:
```
Puzzle.puzzleId - set once in constructor, never changed
Puzzle.solution - deep copied, original not exposed
Cell.row, Cell.col - position fixed after creation
Move fields - all final, cannot be modified after creation
```

### 14.3 Builder Pattern (Optional Enhancement)

**For complex Puzzle creation**:
```
Puzzle puzzle = new PuzzleBuilder()
    .withId("CUSTOM_01")
    .withName("Custom Design")
    .withDifficulty(Difficulty.MEDIUM)
    .withSolution(solutionArray)
    .withAuthor("Player Name")
    .withDescription("My custom puzzle")
    .build();
```

### 14.4 Observer Pattern (for future enhancement)

**Model notifies View of changes**:
```
interface GameStateListener {
    void onMoveCountChanged(int newCount);
    void onTimerTick(int seconds);
    void onCompletionChanged(double percentage);
}

GameState class maintains list of listeners and notifies on changes.
```

---

## 15. Model Layer Testing Considerations

### 15.1 Unit Test Scenarios

**Cell Tests**:
- Test state transitions (UNKNOWN → FILLED → MARKED → UNKNOWN)
- Test isCorrect() for all state combinations
- Test reset() functionality
- Test invalid state assignment (null)

**GameBoard Tests**:
- Test cell access (valid and invalid coordinates)
- Test clue generation from solution
- Test row/column validation
- Test completion detection
- Test progress percentage calculation

**Move Tests**:
- Test undo/redo operations
- Test move recording with different state transitions
- Test timestamp recording

**GameState Tests**:
- Test timer start/pause/resume/stop
- Test move counting
- Test score calculation with various parameters
- Test statistics accumulation

**Puzzle Tests**:
- Test puzzle creation from solution
- Test clue generation accuracy
- Test puzzle validation
- Test invalid puzzle detection

**PuzzleStats Tests**:
- Test statistics recording
- Test average calculations
- Test solve rate computation
- Test performance rating logic

### 15.2 Integration Test Scenarios

**Complete Game Flow**:
1. Load puzzle
2. Make series of moves
3. Undo some moves
4. Redo moves
5. Request hints
6. Complete puzzle
7. Verify statistics recorded correctly

**Edge Cases**:
- Empty puzzles (all zeros)
- Full puzzles (completely filled)
- Single row/column puzzles
- Maximum size puzzles (25×25)
- Extremely long move histories (1000+ moves)

---

## 16. Model Layer Error Handling

### 16.1 Exception Types

**IllegalArgumentException**:
- Used for invalid constructor parameters
- Used for invalid method arguments
- Example: Negative dimensions, null required fields

**IndexOutOfBoundsException**:
- Used for invalid array/list access
- Example: Cell coordinates outside grid bounds

**IllegalStateException**:
- Used for invalid operation in current state
- Example: Calling stopTimer() when timer not started

### 16.2 Validation Strategy

**Constructor Validation**:
```
public GameBoard(int rows, int cols) {
    if (rows < 5 || rows > 25) {
        throw new IllegalArgumentException(
            "Rows must be between 5 and 25: " + rows);
    }
    if (cols < 5 || cols > 25) {
        throw new IllegalArgumentException(
            "Columns must be between 5 and 25: " + cols);
    }
    // Initialize fields...
}
```

**Method Validation**:
```
public Cell getCell(int row, int col) {
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
        throw new IndexOutOfBoundsException(
            "Invalid cell position: (" + row + "," + col + ")");
    }
    return cells[row][col];
}
```

---

## 17. Model Layer Performance Considerations

### 17.1 Memory Optimization

**Cell Array Storage**:
- 25×25 grid = 625 Cell objects
- Each Cell ≈ 24 bytes (4 int + 1 boolean + 1 enum reference)
- Total: 625 × 24 = 15KB (acceptable)

**Move History Storage**:
- Maximum realistic moves: ~1000 per puzzle
- Each Move ≈ 40 bytes
- Total: 40KB (acceptable)
- Consider limiting undo history to 500 moves

**Clue Storage**:
- Maximum clues per line: 12 (for 25-cell line)
- 25 rows + 25 columns = 50 lines
- 50 × 12 = 600 integers maximum
- Total: ~2.4KB (negligible)

### 17.2 Performance Optimization

**Avoid Repeated Validation**:
- Cache completed lines instead of recalculating
- Update only affected lines after move

**Efficient Clue Generation**:
- Generate clues once during puzzle creation
- Store in Puzzle object, don't regenerate

**Lazy Calculation**:
- Calculate score only when requested, not after every move
- Calculate completion percentage only when needed

---

## 18. Summary

### 18.1 Model Layer Components

| Component | Purpose | Key Responsibility |
|-----------|---------|-------------------|
| CellState | Enumeration | Define cell states |
| Cell | Single grid cell | Store state and solution |
| GameBoard | Grid container | Manage cells and clues |
| Clue | Line hints | Store clue numbers |
| Puzzle | Complete puzzle | Define solution and metadata |
| Move | Action record | Enable undo/redo |
| GameState | Session tracking | Track time and statistics |
| Hint | Player assistance | Provide solving guidance |
| PuzzleStats | Performance history | Track puzzle statistics |
| CellPosition | Position holder | Represent coordinates |

### 18.2 Design Principles Applied

- **Single Responsibility**: Each class has one clear purpose
- **Encapsulation**: All data private with controlled access
- **Immutability**: Fixed data cannot be modified
- **Validation**: All inputs validated before use
- **Separation of Concerns**: Model independent of View/Controller

### 18.3 Next Steps

After implementing Model layer:
1. Implement custom data structures (Document 1)
2. Implement View layer (Document 3)
3. Implement Controller layer (Document 4)
4. Implement algorithms (Document 5)
5. Integration and testing

---

## Document Revision History
- Version 1.0 - Initial specification
- Date: 2025-11-12