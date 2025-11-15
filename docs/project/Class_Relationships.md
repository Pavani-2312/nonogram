# Class Relationships Documentation

## Overview

This document describes the relationships, dependencies, and interactions between all classes in the Nonogram game project. Understanding these relationships is crucial for proper implementation and maintenance.

## Architecture Overview

### Layered Architecture
```
┌─────────────────────────────────────────┐
│                View Layer               │
│  GameWindow, GamePanel, GridPanel, etc. │
└─────────────────┬───────────────────────┘
                  │ depends on
┌─────────────────▼───────────────────────┐
│             Controller Layer            │
│ GameController, PuzzleController, etc.  │
└─────────────────┬───────────────────────┘
                  │ depends on
┌─────────────────▼───────────────────────┐
│              Model Layer                │
│   Cell, GameBoard, Puzzle, etc.        │
└─────────────────┬───────────────────────┘
                  │ depends on
┌─────────────────▼───────────────────────┐
│           Data Structures               │
│ MyArrayList, MyStack, MyQueue, etc.    │
└─────────────────────────────────────────┘
```

### Cross-Cutting Concerns
```
┌─────────────────────────────────────────┐
│            Algorithm Layer              │
│  SolvingAlgorithms, HintGenerator, etc. │
└─────────────────┬───────────────────────┘
                  │ used by
┌─────────────────▼───────────────────────┐
│             Utility Layer               │
│    FileUtils, TimeUtils, etc.          │
└─────────────────────────────────────────┘
```

## Data Structure Layer Relationships

### MyArrayList Relationships
```
MyArrayList<E>
├── Used by: GameBoard (for clue storage)
├── Used by: Puzzle (for clue management)
├── Used by: HintController (for hint storage)
├── Used by: StatisticsController (for data collections)
└── Used by: ValidationController (for error lists)
```

**Key Relationships**:
- **GameBoard.rowClues**: `MyArrayList<MyArrayList<Integer>>`
- **GameBoard.columnClues**: `MyArrayList<MyArrayList<Integer>>`
- **GameBoard.completedRows**: `MyArrayList<Integer>`
- **Hint.affectedCells**: `MyArrayList<CellPosition>`

### MyStack Relationships
```
MyStack<E>
├── Used by: GameController (for undo operations)
│   ├── undoStack: MyStack<Move>
│   └── redoStack: MyStack<Move>
└── Used by: SolvingAlgorithms (for backtracking)
```

**Key Relationships**:
- **GameController.undoStack**: Stores moves for undo functionality
- **GameController.redoStack**: Stores moves for redo functionality
- **Move objects**: Stored in stacks for state management

### MyQueue Relationships
```
MyQueue<E>
├── Used by: HintController (for hint sequencing)
│   └── hintQueue: MyQueue<Hint>
└── Used by: GameController (for event processing)
```

**Key Relationships**:
- **HintController.hintQueue**: Manages hint display order
- **Hint objects**: Queued by priority for sequential display

### MyHashMap Relationships
```
MyHashMap<K,V>
├── Used by: PuzzleController (for puzzle library)
│   └── puzzleLibrary: MyHashMap<String, Puzzle>
├── Used by: StatisticsController (for statistics)
│   └── puzzleStats: MyHashMap<String, PuzzleStats>
└── Used by: GameController (for configuration)
    └── settings: MyHashMap<String, Object>
```

**Key Relationships**:
- **String keys**: Puzzle IDs, setting names, statistic keys
- **Object values**: Puzzles, statistics, configuration values

## Model Layer Relationships

### Core Model Relationships
```
Puzzle
├── contains: boolean[][] solution
├── contains: MyArrayList<MyArrayList<Integer>> rowClues
├── contains: MyArrayList<MyArrayList<Integer>> columnClues
└── creates: GameBoard (via constructor)

GameBoard
├── contains: Cell[][] cells
├── references: Puzzle (for solution validation)
├── manages: MyArrayList<Integer> completedRows
└── manages: MyArrayList<Integer> completedColumns

Cell
├── has: CellState currentState
├── has: boolean actualValue
├── has: int row, col (position)
└── used by: GameBoard (in 2D array)
```

### State Management Relationships
```
GameState
├── tracks: int elapsedSeconds
├── tracks: int moveCount
├── tracks: int hintsUsed
├── references: GameBoard (for progress calculation)
└── used by: GameController (for state management)

Move
├── records: int row, col
├── records: CellState previousState, newState
├── records: long timestamp
├── used by: MyStack<Move> (in GameController)
└── operates on: Cell (via GameBoard)
```

### Statistics Relationships
```
PuzzleStats
├── tracks: String puzzleId
├── tracks: performance metrics
├── stored in: MyHashMap<String, PuzzleStats>
└── managed by: StatisticsController

Hint
├── contains: String message
├── contains: HintType type
├── contains: MyArrayList<CellPosition> affectedCells
├── stored in: MyQueue<Hint>
└── managed by: HintController
```

## Controller Layer Relationships

### Controller Hierarchy
```
GameController (Main Controller)
├── manages: PuzzleController
├── manages: ValidationController
├── manages: HintController
├── manages: StatisticsController
├── manages: FileController
├── references: GameBoard
├── references: GameState
└── communicates with: View Layer
```

### Controller Dependencies
```
PuzzleController
├── depends on: FileController (for puzzle loading)
├── manages: MyHashMap<String, Puzzle> puzzleLibrary
├── creates: GameBoard (from Puzzle)
└── used by: GameController

ValidationController
├── depends on: ValidationEngine (algorithm)
├── references: GameBoard
├── manages: MyArrayList<CellPosition> errorCells
└── used by: GameController

HintController
├── depends on: HintGenerator (algorithm)
├── manages: MyQueue<Hint> hintQueue
├── references: GameBoard
└── used by: GameController

StatisticsController
├── depends on: FileController (for persistence)
├── manages: MyHashMap<String, PuzzleStats>
├── references: GameState
└── used by: GameController

FileController
├── handles: file I/O operations
├── used by: PuzzleController
├── used by: StatisticsController
└── used by: GameController (for save/load)
```

## Algorithm Layer Relationships

### Algorithm Dependencies
```
SolvingAlgorithms
├── analyzes: GameBoard
├── uses: Cell state information
├── produces: solving suggestions
└── used by: HintGenerator

HintGenerator
├── depends on: SolvingAlgorithms
├── analyzes: GameBoard
├── produces: Hint objects
├── manages: hint prioritization
└── used by: HintController

ValidationEngine
├── analyzes: GameBoard
├── compares with: Puzzle solution
├── produces: validation results
├── identifies: error locations
└── used by: ValidationController

ClueGenerator
├── analyzes: boolean[][] solution
├── produces: clue numbers
├── used by: Puzzle (during creation)
└── used by: PuzzleController
```

### Algorithm Integration
```
PuzzleAnalyzer
├── uses: SolvingAlgorithms
├── uses: ValidationEngine
├── assesses: puzzle difficulty
├── evaluates: puzzle quality
└── used by: PuzzleController
```

## View Layer Relationships

### View Hierarchy
```
GameWindow (Main Window)
├── contains: MenuBar
├── contains: GamePanel
├── contains: StatusBar
└── manages: Dialog windows

GamePanel (Central Panel)
├── contains: GridPanel
├── contains: CluePanel (row clues)
├── contains: CluePanel (column clues)
└── contains: ControlPanel

GridPanel
├── displays: GameBoard cells
├── handles: mouse interactions
├── references: GameController
└── updates: based on Cell states

CluePanel
├── displays: puzzle clues
├── shows: completion status
├── references: GameBoard
└── updates: based on progress

ControlPanel
├── contains: control buttons
├── displays: game statistics
├── references: GameController
└── triggers: controller actions
```

### Dialog Relationships
```
HintDialog
├── displays: Hint information
├── references: HintController
└── managed by: GameWindow

StatisticsDialog
├── displays: PuzzleStats information
├── references: StatisticsController
└── managed by: GameWindow

SettingsDialog
├── manages: application settings
├── references: GameController
└── managed by: GameWindow
```

## Cross-Layer Interactions

### Event Flow Relationships
```
User Interaction Flow:
GridPanel (mouse click)
    ↓ event
GameController (handleCellClick)
    ↓ update
GameBoard (cell state change)
    ↓ create
Move (action record)
    ↓ store
MyStack<Move> (undo history)
    ↓ notify
GridPanel (visual update)
```

### Data Flow Relationships
```
Puzzle Loading Flow:
FileController (load puzzle file)
    ↓ parse
Puzzle (create object)
    ↓ initialize
GameBoard (create from solution)
    ↓ generate
ClueGenerator (create clues)
    ↓ display
CluePanel (show clues)
```

### Validation Flow Relationships
```
Solution Validation Flow:
GameController (check solution)
    ↓ request
ValidationController (validate)
    ↓ analyze
ValidationEngine (check correctness)
    ↓ compare
GameBoard vs Puzzle.solution
    ↓ result
MyArrayList<CellPosition> (errors)
    ↓ display
GridPanel (highlight errors)
```

## Dependency Management

### Dependency Injection Patterns
```
Constructor Injection:
- Controllers receive dependencies via constructors
- View components receive controller references
- Algorithms receive model references

Factory Pattern:
- PuzzleController creates GameBoard from Puzzle
- HintGenerator creates Hint objects
- ClueGenerator creates clue arrays

Observer Pattern:
- View components observe controller state
- Controllers observe model changes
- Statistics observe game completion
```

### Circular Dependency Prevention
```
Avoided Circular Dependencies:
✗ GameBoard ↔ GameController
✓ GameController → GameBoard (one-way)

✗ View ↔ Controller
✓ View → Controller (one-way)

✗ Model ↔ Algorithm
✓ Algorithm → Model (one-way)
```

## Interface Definitions

### Key Interfaces
```
GameListener (Observer Interface)
├── onGameStateChanged()
├── onPuzzleCompleted()
├── onErrorOccurred()
└── implemented by: View components

Validatable (Validation Interface)
├── isValid()
├── getErrors()
└── implemented by: GameBoard, Puzzle

Serializable (Persistence Interface)
├── serialize()
├── deserialize()
└── implemented by: GameState, PuzzleStats
```

## Memory Management Relationships

### Object Lifecycle
```
Application Startup:
1. Create data structures (MyArrayList, etc.)
2. Initialize controllers
3. Create view components
4. Load initial data

Game Session:
1. Create GameBoard and GameState
2. Create Move objects during play
3. Create Hint objects as needed
4. Update statistics continuously

Application Shutdown:
1. Save current state
2. Persist statistics
3. Clean up resources
4. Dispose view components
```

### Reference Management
```
Strong References:
- GameController → GameBoard
- GameBoard → Cell[][]
- PuzzleController → Puzzle library

Weak References:
- View → Controller (event listeners)
- Temporary algorithm results
- Cache entries
```

## Performance Relationships

### Performance Critical Paths
```
High Frequency Operations:
- Cell state changes (GridPanel → GameController → GameBoard)
- Visual updates (GameBoard → GridPanel)
- Move recording (GameController → MyStack<Move>)

Medium Frequency Operations:
- Hint generation (HintController → HintGenerator → GameBoard)
- Validation (ValidationController → ValidationEngine → GameBoard)
- Statistics updates (StatisticsController → PuzzleStats)

Low Frequency Operations:
- Puzzle loading (PuzzleController → FileController)
- Save/Load operations (GameController → FileController)
- Settings changes (SettingsDialog → GameController)
```

### Optimization Relationships
```
Caching Relationships:
- ValidationController caches validation results
- HintGenerator caches analysis results
- PuzzleController caches loaded puzzles

Lazy Loading:
- Puzzle library loads on demand
- Statistics load when accessed
- Hints generate when requested
```

## Testing Relationships

### Test Dependencies
```
Unit Test Dependencies:
- DataStructureTests → Data Structure classes
- ModelTests → Model classes + Data Structures
- ControllerTests → Controller + Model + Data Structures
- AlgorithmTests → Algorithm + Model classes

Integration Test Dependencies:
- Component Integration → Multiple related classes
- Layer Integration → Entire architectural layers
- System Integration → Complete application
```

### Mock Relationships
```
Mock Objects for Testing:
- MockGameBoard for algorithm testing
- MockController for view testing
- MockFileSystem for persistence testing
- MockUserInput for interaction testing
```

## Documentation Relationships

### Documentation Structure
```
Implementation Guides:
├── Data Structures → Model → Controller → View
├── Algorithms → Integration
└── Testing → All components

API Documentation:
├── Public interfaces
├── Method signatures
└── Usage examples

Architecture Documentation:
├── Class relationships (this document)
├── Design patterns
└── System overview
```

## Validation Checklist

### Relationship Verification
- [ ] All dependencies are one-way (no circular dependencies)
- [ ] Interface contracts are well-defined
- [ ] Memory management relationships are clear
- [ ] Performance critical paths are identified

### Implementation Verification
- [ ] All relationships can be implemented as designed
- [ ] Dependencies can be satisfied during construction
- [ ] Interface implementations are feasible
- [ ] Testing relationships support comprehensive testing

### Maintenance Verification
- [ ] Relationships support system evolution
- [ ] Changes can be made without breaking dependencies
- [ ] New features can be added within existing relationships
- [ ] System can be extended and modified safely

---

**Summary**: This class relationship documentation provides the foundation for understanding how all components of the Nonogram game work together. Use this as a reference during implementation to ensure proper component interactions and system architecture.
