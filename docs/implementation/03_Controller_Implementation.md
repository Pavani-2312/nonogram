# Controller Implementation Guide

## Overview

This guide provides detailed implementation specifications for the Controller layer classes. The Controller layer handles user interactions, manages game flow, and coordinates between the Model and View layers.

## Controller Architecture

### MVC Pattern Compliance
- **Model Access**: Controllers interact with Model classes
- **View Communication**: Controllers receive events from View components
- **Business Logic**: Controllers contain game flow logic
- **State Management**: Controllers manage application state transitions

### Layer Dependencies
- **Depends On**: Model layer, algorithms layer
- **Used By**: View layer
- **Independence**: No direct View dependencies in Controller logic

## Implementation Order

1. **ValidationController** - Core validation logic
2. **PuzzleController** - Puzzle management
3. **GameController** - Main game flow
4. **HintController** - Hint system management
5. **StatisticsController** - Performance tracking
6. **FileController** - Save/load operations

## Core Controller Implementations

### ValidationController Class

#### Purpose
Manages solution validation, error detection, and correctness checking.

#### Essential Fields
- **gameBoard**: Reference to current game board (GameBoard)
- **validationEngine**: Algorithm for validation (ValidationEngine)
- **errorCells**: List of incorrect cells (MyArrayList<CellPosition>)
- **lastValidationResult**: Cache of last validation (boolean)

#### Constructor Requirements
- **ValidationController(GameBoard gameBoard)**
- **Initialization**: Create validation engine, initialize error tracking

#### Core Methods Implementation

##### Validation Operations
- **validateSolution()**: Check complete puzzle solution
  - Use ValidationEngine to check all cells
  - Update errorCells list
  - Return boolean result
  - Cache result for performance

- **validateRow(int rowIndex)**: Check specific row
  - Validate row against clues
  - Return validation result
  - Update error tracking for row

- **validateColumn(int colIndex)**: Check specific column
  - Validate column against clues
  - Return validation result
  - Update error tracking for column

- **validateCell(int row, int col)**: Check single cell
  - Compare cell state with solution
  - Return correctness result

##### Error Management
- **getErrorCells()**: Return list of incorrect cells
- **hasErrors()**: Return true if errors exist
- **clearErrors()**: Clear error tracking
- **getErrorCount()**: Return number of errors

##### Progress Tracking
- **getCompletionPercentage()**: Calculate completion percentage
- **isComplete()**: Check if puzzle is solved
- **getCorrectCells()**: Count correctly filled cells

#### Integration Points
- **Model Integration**: Access GameBoard and Cell states
- **Algorithm Integration**: Use ValidationEngine for logic
- **View Integration**: Provide error information for display

### PuzzleController Class

#### Purpose
Manages puzzle loading, selection, and puzzle library operations.

#### Essential Fields
- **puzzleLibrary**: Map of available puzzles (MyHashMap<String, Puzzle>)
- **currentPuzzle**: Currently loaded puzzle (Puzzle)
- **puzzleStats**: Statistics for puzzles (MyHashMap<String, PuzzleStats>)
- **fileController**: File operations handler (FileController)

#### Constructor Requirements
- **PuzzleController()**
- **Initialization**: Initialize puzzle library, load default puzzles

#### Core Methods Implementation

##### Puzzle Management
- **loadPuzzle(String puzzleId)**: Load specific puzzle
  - Retrieve puzzle from library
  - Create new GameBoard from puzzle
  - Initialize game state
  - Return success status

- **loadRandomPuzzle(Difficulty difficulty)**: Load random puzzle
  - Filter puzzles by difficulty
  - Select random puzzle from filtered list
  - Load selected puzzle

- **getCurrentPuzzle()**: Return currently loaded puzzle
- **getPuzzleLibrary()**: Return available puzzles
- **addPuzzle(Puzzle puzzle)**: Add new puzzle to library

##### Puzzle Library Operations
- **loadPuzzleLibrary()**: Load puzzles from files
  - Read puzzle files from resources
  - Parse puzzle data
  - Populate puzzle library

- **getPuzzlesByDifficulty(Difficulty difficulty)**: Filter puzzles
  - Return MyArrayList of puzzles matching difficulty

- **searchPuzzles(String searchTerm)**: Find puzzles by name
  - Search puzzle names and descriptions
  - Return matching puzzles

##### Statistics Integration
- **getPuzzleStats(String puzzleId)**: Get performance statistics
- **updatePuzzleStats(String puzzleId, GameState gameState)**: Update statistics
- **resetPuzzleStats(String puzzleId)**: Clear statistics

#### File Integration
- **savePuzzleLibrary()**: Save library to file
- **loadCustomPuzzle(String filename)**: Load user-created puzzle

### GameController Class

#### Purpose
Main game flow control, user action handling, and game state management.

#### Essential Fields
- **gameBoard**: Current game board (GameBoard)
- **gameState**: Current game session (GameState)
- **puzzleController**: Puzzle management (PuzzleController)
- **validationController**: Validation logic (ValidationController)
- **hintController**: Hint system (HintController)
- **undoStack**: Move history for undo (MyStack<Move>)
- **redoStack**: Move history for redo (MyStack<Move>)
- **gameListeners**: Event listeners (MyArrayList<GameListener>)

#### Constructor Requirements
- **GameController()**
- **Initialization**: Create sub-controllers, initialize stacks

#### Core Methods Implementation

##### Game Flow Control
- **startNewGame(String puzzleId)**: Initialize new game
  - Load puzzle via PuzzleController
  - Create new GameBoard and GameState
  - Clear undo/redo stacks
  - Start timer
  - Notify listeners

- **pauseGame()**: Pause current game
  - Pause timer in GameState
  - Set pause flag
  - Notify listeners

- **resumeGame()**: Resume paused game
  - Resume timer
  - Clear pause flag
  - Notify listeners

- **endGame()**: Complete current game
  - Stop timer
  - Calculate final score
  - Update statistics
  - Notify listeners

##### User Action Handling
- **handleCellClick(int row, int col, boolean isRightClick)**: Process cell interaction
  - Get current cell state
  - Determine new state based on click type
  - Create Move object
  - Apply state change
  - Push to undo stack
  - Clear redo stack
  - Update game state
  - Check for completion

- **handleUndo()**: Undo last move
  - Check if undo stack not empty
  - Pop move from undo stack
  - Apply undo to game board
  - Push move to redo stack
  - Update game state

- **handleRedo()**: Redo last undone move
  - Check if redo stack not empty
  - Pop move from redo stack
  - Apply redo to game board
  - Push move to undo stack
  - Update game state

##### Game State Queries
- **canUndo()**: Return !undoStack.isEmpty()
- **canRedo()**: Return !redoStack.isEmpty()
- **isGameActive()**: Return game in progress
- **isGameComplete()**: Return puzzle solved
- **getGameState()**: Return current game state

##### Event Management
- **addGameListener(GameListener listener)**: Add event listener
- **removeGameListener(GameListener listener)**: Remove event listener
- **notifyGameStateChanged()**: Notify all listeners of state change

#### Integration Coordination
- **checkSolution()**: Coordinate with ValidationController
- **requestHint()**: Coordinate with HintController
- **saveGame()**: Coordinate with FileController

### HintController Class

#### Purpose
Manages hint generation, display, and hint system state.

#### Essential Fields
- **hintGenerator**: Algorithm for generating hints (HintGenerator)
- **hintQueue**: Queue of available hints (MyQueue<Hint>)
- **gameBoard**: Reference to game board (GameBoard)
- **hintsUsed**: Count of hints used in session (int)
- **hintCooldown**: Time between hints (long)
- **lastHintTime**: Timestamp of last hint (long)

#### Constructor Requirements
- **HintController(GameBoard gameBoard)**
- **Initialization**: Create hint generator, initialize queue

#### Core Methods Implementation

##### Hint Generation
- **generateHints()**: Create hints for current board state
  - Analyze current game board
  - Use HintGenerator to create hints
  - Sort hints by priority
  - Clear and populate hint queue

- **getNextHint()**: Get next available hint
  - Check hint cooldown
  - If queue empty, generate new hints
  - Dequeue highest priority hint
  - Increment hints used counter
  - Update last hint time
  - Return hint

##### Hint Management
- **hasAvailableHints()**: Return !hintQueue.isEmpty()
- **getHintCount()**: Return hints used in session
- **clearHints()**: Clear hint queue
- **resetHintCounter()**: Reset session hint count

##### Hint Display Support
- **getHintMessage(Hint hint)**: Format hint for display
- **getAffectedCells(Hint hint)**: Get cells to highlight
- **applyHintPenalty()**: Apply scoring penalty for hint use

#### Algorithm Integration
- **analyzeBoard()**: Use algorithms to analyze current state
- **detectErrors()**: Find contradictions in current state
- **findObviousMoves()**: Identify certain moves

### StatisticsController Class

#### Purpose
Manages performance tracking, statistics calculation, and data persistence.

#### Essential Fields
- **puzzleStats**: Per-puzzle statistics (MyHashMap<String, PuzzleStats>)
- **globalStats**: Overall performance data (GlobalStats)
- **sessionStats**: Current session data (SessionStats)
- **fileController**: File operations (FileController)

#### Constructor Requirements
- **StatisticsController()**
- **Initialization**: Load existing statistics, initialize tracking

#### Core Methods Implementation

##### Statistics Recording
- **recordGameStart(String puzzleId)**: Record game attempt
  - Get or create PuzzleStats
  - Increment attempts counter
  - Update global statistics

- **recordGameCompletion(String puzzleId, GameState gameState)**: Record completion
  - Update puzzle statistics with performance data
  - Update global completion statistics
  - Calculate and store scores

- **recordGameAbandonment(String puzzleId)**: Record give-up
  - Update abandonment statistics
  - Track incomplete attempts

##### Statistics Queries
- **getPuzzleStats(String puzzleId)**: Get puzzle-specific statistics
- **getGlobalStats()**: Get overall performance statistics
- **getSessionStats()**: Get current session statistics
- **getLeaderboard(Difficulty difficulty)**: Get top scores

##### Statistics Calculations
- **calculateAverageTime(String puzzleId)**: Calculate average completion time
- **calculateSolveRate(String puzzleId)**: Calculate success percentage
- **calculateEfficiency(String puzzleId)**: Calculate move efficiency
- **calculateOverallRating()**: Calculate player skill rating

##### Data Persistence
- **saveStatistics()**: Save all statistics to file
- **loadStatistics()**: Load statistics from file
- **exportStatistics(String filename)**: Export to external file
- **resetStatistics()**: Clear all statistics

### FileController Class

#### Purpose
Handles file operations for save/load, puzzle import/export, and data persistence.

#### Essential Fields
- **saveDirectory**: Directory for save files (String)
- **puzzleDirectory**: Directory for puzzle files (String)
- **statisticsFile**: Statistics data file (String)
- **fileUtils**: File operation utilities (FileUtils)

#### Constructor Requirements
- **FileController()**
- **Initialization**: Set up directories, verify file access

#### Core Methods Implementation

##### Game Save/Load
- **saveGame(GameState gameState, GameBoard gameBoard, String filename)**: Save current game
  - Serialize game state and board state
  - Write to save file
  - Return success status

- **loadGame(String filename)**: Load saved game
  - Read save file
  - Deserialize game state and board
  - Return loaded game data

- **getSaveFiles()**: List available save files
- **deleteSaveFile(String filename)**: Remove save file

##### Puzzle File Operations
- **loadPuzzleFromFile(String filename)**: Load puzzle from file
  - Parse puzzle file format
  - Create Puzzle object
  - Validate puzzle data

- **savePuzzleToFile(Puzzle puzzle, String filename)**: Save puzzle to file
  - Serialize puzzle data
  - Write to puzzle file

- **importPuzzle(String filename)**: Import external puzzle
- **exportPuzzle(Puzzle puzzle, String filename)**: Export puzzle

##### Statistics Persistence
- **saveStatistics(MyHashMap<String, PuzzleStats> stats)**: Save statistics
- **loadStatistics()**: Load statistics from file
- **backupStatistics()**: Create statistics backup

##### File Management
- **ensureDirectoriesExist()**: Create necessary directories
- **validateFileAccess()**: Check read/write permissions
- **cleanupOldFiles()**: Remove old temporary files

## Controller Integration Patterns

### Event Flow Pattern
1. **View Event**: User interaction in View
2. **Controller Method**: View calls Controller method
3. **Model Update**: Controller updates Model
4. **Validation**: Controller validates changes
5. **State Update**: Controller updates game state
6. **View Notification**: Controller notifies View of changes

### Coordination Pattern
- **Primary Controller**: GameController coordinates other controllers
- **Specialized Controllers**: Handle specific domains
- **Shared Resources**: Controllers share Model objects
- **Event Broadcasting**: Controllers notify interested parties

### Error Handling Pattern
- **Validation**: Controllers validate all inputs
- **Exception Handling**: Controllers catch and handle exceptions
- **Error Reporting**: Controllers report errors to View
- **Recovery**: Controllers implement error recovery strategies

## Testing Strategy

### Unit Testing
- **Mock Dependencies**: Use mock objects for Model classes
- **Test Methods**: Test each controller method independently
- **Edge Cases**: Test boundary conditions and error cases
- **State Verification**: Verify controller state changes

### Integration Testing
- **Controller Coordination**: Test controller interactions
- **Model Integration**: Test Model layer integration
- **Event Flow**: Test complete event handling flow
- **Error Scenarios**: Test error handling across controllers

### Performance Testing
- **Response Time**: Measure controller method execution time
- **Memory Usage**: Monitor memory consumption
- **Scalability**: Test with large datasets
- **Concurrency**: Test thread safety if applicable

## Implementation Guidelines

### Design Principles
- **Single Responsibility**: Each controller has focused purpose
- **Loose Coupling**: Minimize dependencies between controllers
- **High Cohesion**: Related functionality grouped together
- **Interface Segregation**: Use specific interfaces for communication

### Error Handling
- **Input Validation**: Validate all parameters
- **Exception Management**: Handle exceptions appropriately
- **Error Recovery**: Implement recovery mechanisms
- **User Feedback**: Provide meaningful error messages

### Performance Optimization
- **Caching**: Cache frequently accessed data
- **Lazy Loading**: Load data only when needed
- **Efficient Algorithms**: Use optimal algorithms
- **Resource Management**: Manage resources efficiently

## Validation Checklist

### Functionality Verification
- [ ] All user interactions handled correctly
- [ ] Game flow logic implemented properly
- [ ] Error handling comprehensive
- [ ] Integration with Model layer working

### Design Verification
- [ ] MVC pattern followed correctly
- [ ] Controller responsibilities well-defined
- [ ] Dependencies properly managed
- [ ] Event handling implemented correctly

### Quality Verification
- [ ] Performance requirements met
- [ ] Memory usage optimized
- [ ] Error recovery mechanisms working
- [ ] Testing coverage adequate

---

**Next Step**: After implementing the Controller layer, proceed to [View Implementation](04_View_Implementation.md) to create the user interface components.
