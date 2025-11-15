# Model Implementation Guide

## Overview

This guide provides detailed implementation specifications for all Model layer classes. The Model layer contains the core game logic, data representation, and business rules for the Nonogram game.

## Implementation Dependencies

**Prerequisites**: Custom data structures (MyArrayList, MyStack, MyQueue, MyHashMap) must be completed first.

**Layer Independence**: Model layer must not depend on View or Controller layers.

## Implementation Order

1. **Enumerations** (CellState, Difficulty, HintType)
2. **Basic Classes** (CellPosition, Cell)
3. **Core Game Classes** (Clue, GameBoard)
4. **Game Management** (Puzzle, Move, GameState)
5. **Advanced Features** (Hint, PuzzleStats)

## Enumeration Implementations

### CellState Enumeration

#### Purpose
Represents the three possible states of a cell in the game grid.

#### Implementation Requirements
- **UNKNOWN**: Initial state, player hasn't determined
- **FILLED**: Player believes cell should be black/filled
- **MARKED**: Player believes cell should be empty (X mark)

#### State Transition Logic
- UNKNOWN → FILLED (left click)
- FILLED → MARKED (left click)
- MARKED → UNKNOWN (left click)
- UNKNOWN → MARKED (right click)
- MARKED → UNKNOWN (right click)

#### Additional Methods
- **getDisplaySymbol()**: Return character representation
- **getNextState()**: Return next state in cycle
- **isAnswered()**: Return true if not UNKNOWN

### Difficulty Enumeration

#### Purpose
Categorizes puzzle complexity levels.

#### Implementation Requirements
- **EASY**: 5×5 to 8×8 grids, simple patterns
- **MEDIUM**: 10×10 to 12×12 grids, moderate complexity
- **HARD**: 15×15 to 18×18 grids, complex patterns
- **EXPERT**: 20×20 to 25×25 grids, very complex

#### Additional Methods
- **getMinSize()**: Minimum grid size for difficulty
- **getMaxSize()**: Maximum grid size for difficulty
- **getScoreMultiplier()**: Scoring multiplier for difficulty

### HintType Enumeration

#### Purpose
Categorizes different types of hints for the hint system.

#### Implementation Requirements
- **COMPLETE_LINE**: Entire row/column can be filled
- **OVERLAP**: Cells forced by clue overlap
- **EDGE_DEDUCTION**: Cells determinable by edge reasoning
- **SIMPLE_BOXES**: Small isolated groups
- **ERROR**: Contradiction detected
- **STRATEGY**: General solving approach

#### Additional Methods
- **getPriority()**: Return hint priority (1-5)
- **getDescription()**: Return hint type description

## Basic Class Implementations

### CellPosition Class

#### Purpose
Simple data structure to represent cell coordinates.

#### Essential Fields
- **row**: Row coordinate (int)
- **col**: Column coordinate (int)

#### Constructor Requirements
- **CellPosition(int row, int col)**: Initialize coordinates
- **Validation**: Accept any integer values (validation by caller)

#### Core Methods
- **getRow()**: Return row coordinate
- **getCol()**: Return column coordinate
- **equals(Object obj)**: Compare positions for equality
- **hashCode()**: Generate hash for HashMap usage
- **toString()**: Format as "(row, col)"

#### Implementation Notes
- Make fields final for immutability
- Override equals and hashCode properly
- Handle null in equals method

### Cell Class

#### Purpose
Represents a single cell in the game grid with state and solution.

#### Essential Fields
- **row**: Row position (int, final)
- **col**: Column position (int, final)
- **currentState**: Player's current answer (CellState)
- **actualValue**: True solution (boolean, final)

#### Constructor Requirements
- **Cell(int row, int col, boolean actualValue)**
- **Validation**: row >= 0, col >= 0
- **Initialization**: currentState = UNKNOWN

#### Core Methods Implementation

##### State Management
- **getCurrentState()**: Return player's current state
- **setState(CellState newState)**: Set player's state
  - Validate newState not null
  - Update currentState field
- **cycleState()**: Advance to next state in cycle
  - Use CellState.getNextState() or implement cycling logic

##### Solution Access
- **getActualValue()**: Return true solution value
- **isCorrect()**: Check if player's state matches solution
  - UNKNOWN: return false (not answered)
  - FILLED: return actualValue == true
  - MARKED: return actualValue == false

##### Position Access
- **getRow()**: Return row coordinate
- **getCol()**: Return column coordinate

##### Utility Methods
- **reset()**: Set currentState back to UNKNOWN
- **toString()**: Format for debugging

#### Validation Logic
- Implement comprehensive state validation
- Handle null state assignments
- Ensure immutable position after construction

## Core Game Class Implementations

### Clue Class

#### Purpose
Represents sequence of numbers for a single row or column.

#### Essential Fields
- **numbers**: Sequence of clue numbers (MyArrayList<Integer>)
- **lineIndex**: Which row/column (int)
- **isRow**: true for row, false for column (boolean)

#### Constructor Requirements
- **Clue(int lineIndex, boolean isRow)**
- **Initialization**: Create empty numbers list
- **Validation**: lineIndex >= 0

#### Core Methods Implementation

##### Number Management
- **addNumber(int value)**: Append clue number
  - Validate value > 0
  - Add to numbers list
- **getNumbers()**: Return complete sequence
  - Consider returning copy for immutability
- **getNumber(int index)**: Return specific clue number
  - Validate index bounds
- **getCount()**: Return numbers.size()

##### Calculations
- **getTotalFilledCells()**: Sum all clue numbers
- **getMinimumLength()**: Calculate minimum space needed
  - Formula: sum of numbers + (count - 1) gaps
- **isValid(int lineLength)**: Check if clue fits in line
  - Return getMinimumLength() <= lineLength

##### Utility Methods
- **toString()**: Format as space-separated numbers
- **isEmpty()**: Return numbers.size() == 0
- **clear()**: Remove all numbers

### GameBoard Class

#### Purpose
Manages complete grid of cells, clues, and puzzle progress.

#### Essential Fields
- **rows**: Grid height (int, final)
- **cols**: Grid width (int, final)
- **cells**: 2D array of cells (Cell[][])
- **rowClues**: Clues for each row (MyArrayList<MyArrayList<Integer>>)
- **columnClues**: Clues for each column (MyArrayList<MyArrayList<Integer>>)
- **completedRows**: Indices of solved rows (MyArrayList<Integer>)
- **completedColumns**: Indices of solved columns (MyArrayList<Integer>)

#### Constructor Requirements

##### Primary Constructor
- **GameBoard(int rows, int cols)**
- **Validation**: 5 <= rows,cols <= 25
- **Initialization**: 
  - Create cells array with default actualValue = false
  - Initialize empty clue lists
  - Initialize empty completed lists

##### Solution Constructor
- **GameBoard(boolean[][] solution)**
- **Validation**: Rectangular array, valid dimensions
- **Initialization**: Create cells with actualValue from solution

#### Core Methods Implementation

##### Cell Access
- **getCell(int row, int col)**: Retrieve specific cell
  - Validate coordinates
  - Return cell reference
- **setCell(int row, int col, Cell cell)**: Replace cell
  - Validate coordinates and cell not null
- **getRow(int rowIndex)**: Return all cells in row
  - Create MyArrayList with row cells
- **getColumn(int colIndex)**: Return all cells in column
  - Create MyArrayList with column cells

##### Clue Management
- **getRowClues(int rowIndex)**: Return row clue numbers
- **getColumnClues(int colIndex)**: Return column clue numbers
- **setRowClues(int rowIndex, MyArrayList<Integer> clues)**: Set row clues
- **setColumnClues(int colIndex, MyArrayList<Integer> clues)**: Set column clues
- **generateCluesFromSolution()**: Analyze solution and create clues
  - Iterate through each row and column
  - Count consecutive filled cells
  - Create clue numbers from counts

##### Validation Methods
- **isRowComplete(int rowIndex)**: Check if row correctly solved
  - Verify all cells in row are correct
- **isColumnComplete(int colIndex)**: Check if column correctly solved
- **validateRow(int rowIndex)**: Comprehensive row validation
  - Compare player pattern against clues
- **validateColumn(int colIndex)**: Comprehensive column validation
- **isPuzzleComplete()**: Check if entire puzzle solved
  - Verify all cells are correct
- **getIncorrectCells()**: Find all incorrect cells
  - Return MyArrayList of incorrect Cell references

##### Progress Tracking
- **updateCompletedLines()**: Update completed row/column lists
  - Clear existing lists
  - Check each row/column for completion
  - Add completed indices to lists
- **getCompletedRows()**: Return completed row indices
- **getCompletedColumns()**: Return completed column indices
- **getCompletionPercentage()**: Calculate percentage complete
  - Formula: (correctCells / totalCells) × 100

##### Board Manipulation
- **reset()**: Set all cells to UNKNOWN state
- **clear()**: Same as reset
- **copy()**: Create deep copy of board

##### Dimension Access
- **getRows()**: Return grid height
- **getCols()**: Return grid width

## Game Management Class Implementations

### Puzzle Class

#### Purpose
Complete puzzle definition with solution, clues, and metadata.

#### Essential Fields
- **puzzleId**: Unique identifier (String, final)
- **name**: Display name (String, final)
- **difficulty**: Difficulty level (Difficulty, final)
- **size**: Grid dimension (int, final)
- **solution**: True solution grid (boolean[][], final)
- **rowClues**: Row clue lists (MyArrayList<MyArrayList<Integer>>)
- **columnClues**: Column clue lists (MyArrayList<MyArrayList<Integer>>)
- **author**: Puzzle creator (String)
- **description**: Optional description (String)
- **createdDate**: Creation timestamp (long)

#### Constructor Requirements

##### Full Constructor
- **Puzzle(String puzzleId, String name, Difficulty difficulty, boolean[][] solution)**
- **Validation**: All parameters non-null, solution rectangular
- **Initialization**: 
  - Deep copy solution array
  - Generate clues from solution
  - Set createdDate to current time

##### Clue Constructor
- **Puzzle(String puzzleId, String name, Difficulty difficulty, int size, rowClues, columnClues)**
- **Usage**: Loading from file with pre-generated clues

#### Core Methods Implementation

##### Metadata Access
- **getPuzzleId()**: Return unique identifier
- **getName()**: Return display name
- **getDifficulty()**: Return difficulty level
- **getSize()**: Return grid dimension
- **getAuthor()**: Return creator name
- **getDescription()**: Return description
- **getCreatedDate()**: Return creation timestamp

##### Solution Access
- **getSolution()**: Return deep copy of solution
- **getSolutionValue(int row, int col)**: Return single cell value
  - Validate coordinates

##### Clue Access
- **getRowClues()**: Return all row clues
- **getColumnClues()**: Return all column clues
- **getRowClue(int rowIndex)**: Return specific row clues
- **getColumnClue(int colIndex)**: Return specific column clues

##### Puzzle Generation
- **generateClues()**: Create clues from solution
  - Implement clue generation algorithm
  - Handle empty rows/columns (clue = 0)

##### Validation
- **isValid()**: Validate puzzle is well-formed
  - Check solution dimensions
  - Verify clue validity
  - Ensure solvability

##### Serialization
- **toFileFormat()**: Convert to saveable string
- **fromFileFormat(String data)**: Parse from saved format (static)

### Move Class

#### Purpose
Records single player action for undo/redo functionality.

#### Essential Fields
- **row**: Cell row position (int, final)
- **col**: Cell column position (int, final)
- **previousState**: State before move (CellState, final)
- **newState**: State after move (CellState, final)
- **timestamp**: When move occurred (long, final)

#### Constructor Requirements
- **Move(int row, int col, CellState previousState, CellState newState, long timestamp)**
- **Validation**: All parameters valid, states not null

#### Core Methods Implementation

##### Accessors
- **getRow()**: Return row position
- **getCol()**: Return column position
- **getPreviousState()**: Return previous state
- **getNewState()**: Return new state
- **getTimestamp()**: Return timestamp

##### Undo/Redo Operations
- **undo(GameBoard board)**: Reverse move on board
  - Get cell at position
  - Set state to previousState
- **redo(GameBoard board)**: Reapply move on board
  - Get cell at position
  - Set state to newState

##### Utility Methods
- **isStateChange()**: Return previousState != newState
- **toString()**: Format for debugging

### GameState Class

#### Purpose
Tracks game progress, timing, scoring, and statistics.

#### Essential Fields
- **elapsedSeconds**: Total time elapsed (int)
- **moveCount**: Total moves made (int)
- **hintsUsed**: Number of hints requested (int)
- **errorsFound**: Number of incorrect cells (int)
- **score**: Calculated score (int)
- **completionPercentage**: Puzzle completion 0-100 (double)
- **isPaused**: Timer paused flag (boolean)
- **isComplete**: Puzzle solved flag (boolean)
- **startTime**: Session start timestamp (long)
- **pauseStartTime**: When pause began (long)
- **totalPausedTime**: Accumulated pause time (long)

#### Constructor Requirements
- **GameState()**: Initialize all counters to zero

#### Core Methods Implementation

##### Timer Management
- **startTimer()**: Record session start time
- **pauseTimer()**: Pause time tracking
- **resumeTimer()**: Resume after pause, exclude pause time
- **stopTimer()**: Finalize timing
- **getElapsedSeconds()**: Calculate current elapsed time

##### Statistics Management
- **incrementMoves()**: Increment move counter
- **incrementHints()**: Increment hint counter
- **setErrors(int count)**: Record incorrect cell count
- **updateCompletion(double percentage)**: Update completion percentage

##### Score Calculation
- **calculateScore()**: Compute final score
  - Base score + bonuses - penalties
  - Apply difficulty multiplier
- **getScore()**: Return calculated score

##### State Queries
- **getMoveCount()**: Return move count
- **getHintsUsed()**: Return hint count
- **getErrorsFound()**: Return error count
- **getCompletionPercentage()**: Return completion percentage
- **isPaused()**: Return pause state
- **isComplete()**: Return completion state

##### Reset Methods
- **reset()**: Clear all statistics
- **resetTimer()**: Reset only time-related fields

## Advanced Feature Implementations

### Hint Class

#### Purpose
Represents single hint with message, priority, and affected cells.

#### Essential Fields
- **message**: Hint text for display (String)
- **type**: Category of hint (HintType)
- **priority**: 1=highest, 5=lowest (int)
- **affectedCells**: Cells to highlight (MyArrayList<CellPosition>)
- **lineIndex**: Row/column index, -1 if not applicable (int)
- **isRow**: true for row, false for column (boolean)

#### Constructor Requirements
- **Hint(String message, HintType type, int priority)**
- **Validation**: message non-empty, priority 1-5

#### Core Methods Implementation

##### Cell Management
- **addAffectedCell(int row, int col)**: Add cell to highlight
- **addAffectedCells(MyArrayList<CellPosition> cells)**: Add multiple cells
- **getAffectedCells()**: Return cells to highlight

##### Line Association
- **setLine(int lineIndex, boolean isRow)**: Associate with row/column

##### Accessors
- **getMessage()**: Return hint text
- **getType()**: Return hint type
- **getPriority()**: Return priority level
- **getLineIndex()**: Return line index
- **isRow()**: Return row flag
- **hasLine()**: Return lineIndex >= 0

##### Comparison
- **compareTo(Hint other)**: Compare by priority for sorting

### PuzzleStats Class

#### Purpose
Tracks performance statistics for individual puzzles.

#### Essential Fields
- **puzzleId**: Associated puzzle identifier (String, final)
- **timesAttempted**: Total attempts (int)
- **timesSolved**: Total completions (int)
- **timesGivenUp**: Total abandonments (int)
- **bestTime**: Fastest completion in seconds (int)
- **averageTime**: Average completion time (int)
- **bestMoves**: Fewest moves to solve (int)
- **averageMoves**: Average moves per completion (int)
- **bestScore**: Highest score achieved (int)
- **totalHintsUsed**: Cumulative hints (int)
- **firstAttemptedDate**: First play timestamp (long)
- **lastSolvedDate**: Most recent completion (long)
- **solveHistory**: List of completion times (MyArrayList<Integer>)

#### Constructor Requirements
- **PuzzleStats(String puzzleId)**
- **Initialization**: Set defaults, create empty history

#### Core Methods Implementation

##### Recording Methods
- **recordAttempt()**: Increment attempt counter
- **recordSolution(int time, int moves, int hints, int score)**: Record completion
  - Update all relevant statistics
  - Recalculate averages
- **recordGiveUp()**: Increment give-up counter

##### Query Methods
- **getSolveRate()**: Calculate completion percentage
- **getAverageHintsPerSolve()**: Calculate hints per completion
- **hasBeenSolved()**: Return timesSolved > 0
- **getDaysSinceLastSolved()**: Calculate days since last completion
- **getPerformanceRating()**: Categorize performance level

##### Reset Methods
- **resetStats()**: Clear all statistics

## Implementation Guidelines

### Error Handling Strategy
- **Validation**: Validate all inputs at method entry
- **Exceptions**: Use appropriate exception types
- **Null Handling**: Define null policies consistently
- **Bounds Checking**: Always validate array/list access

### Memory Management
- **Object Creation**: Minimize unnecessary object creation
- **Reference Cleanup**: Clear references when removing elements
- **Deep Copying**: Implement proper deep copy methods
- **Resource Management**: Clean up resources properly

### Performance Considerations
- **Algorithm Efficiency**: Use efficient algorithms
- **Data Structure Choice**: Choose appropriate structures
- **Caching**: Cache calculated values when appropriate
- **Lazy Initialization**: Initialize expensive objects only when needed

### Testing Strategy
- **Unit Tests**: Test each class independently
- **Edge Cases**: Test boundary conditions
- **Error Conditions**: Test exception handling
- **Integration**: Test class interactions

## Validation Checklist

### Functionality Verification
- [ ] All required methods implemented
- [ ] Proper state management
- [ ] Correct validation logic
- [ ] Exception handling implemented

### Design Verification
- [ ] Proper encapsulation maintained
- [ ] No circular dependencies
- [ ] Clear separation of concerns
- [ ] Consistent naming conventions

### Quality Verification
- [ ] Comprehensive error handling
- [ ] Memory efficient implementation
- [ ] Performance requirements met
- [ ] Code is maintainable and readable

---

**Next Step**: After implementing and testing the Model layer, proceed to [Controller Implementation](03_Controller_Implementation.md) to handle user interactions and game flow.
