# Nonogram Puzzle Game

A Java implementation of the classic Nonogram puzzle game using MVC architecture and custom data structures.

## Project Structure

```
nonogram/
├── src/nonogram/           # Source code
│   ├── Main.java          # Application entry point
│   ├── datastructures/    # Custom data structures
│   │   ├── MyLinkedList.java
│   │   ├── MyStack.java
│   │   ├── MyArrayList.java
│   │   ├── MyQueue.java
│   │   └── MyHashMap.java
│   ├── model/             # Game logic and data
│   │   ├── CellState.java
│   │   ├── Cell.java
│   │   ├── CellPosition.java
│   │   ├── GameBoard.java
│   │   ├── GameState.java
│   │   ├── Puzzle.java
│   │   ├── Difficulty.java
│   │   ├── Move.java
│   │   ├── Hint.java
│   │   └── HintType.java
│   ├── view/              # User interface
│   │   ├── MainFrame.java
│   │   ├── GamePanel.java
│   │   ├── GridPanel.java
│   │   └── CluePanel.java
│   └── controller/        # Game coordination
│       ├── GameController.java
│       ├── PuzzleLoader.java
│       └── HintGenerator.java
├── bin/                   # Compiled classes
├── docs/                  # Documentation
├── compile.sh            # Compilation script
└── README.md             # This file
```

## How to Build and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher

### Compilation
```bash
./compile.sh
```

### Running the Game

**Windows:**
```cmd
run.bat
```

**Manual execution:**
```bash
# Compile first
./compile.sh

# Run with display compatibility flags (required for WSL/Linux environments):
java -Djava.awt.headless=false -Dsun.java2d.xrender=false -Dsun.java2d.pmoffscreen=false -Dsun.java2d.d3d=false -Dsun.java2d.opengl=false -Dswing.defaultlaf=javax.swing.plaf.metal.MetalLookAndFeel -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -Dsun.java2d.noddraw=true -cp bin nonogram.Main
```

## How to Play

1. **Objective**: Fill cells in the grid to reveal a hidden picture
2. **Clues**: Numbers on the left (rows) and top (columns) indicate consecutive filled cells
3. **Controls**: 
   - Left click to cycle through cell states: Empty → Filled → Marked → Empty
   - Filled cells are black
   - Marked cells have an X (indicating they should stay empty)
4. **Win**: Complete the puzzle when all cells match the solution

## Features Implemented

- ✅ Custom data structures (MyLinkedList, MyStack, MyArrayList, MyQueue, MyHashMap)
- ✅ MVC architecture
- ✅ Interactive grid with mouse controls
- ✅ Clue display for rows and columns
- ✅ Win detection
- ✅ Multiple built-in puzzles (Heart, Cross, Square)
- ✅ Cell state cycling (Unknown → Filled → Marked)
- ✅ Hint system with multiple hint types
- ✅ Game state management and move tracking
- ✅ Multiple difficulty levels
- ✅ Cell position tracking

## Game Architecture

### Model Layer
- **Cell**: Individual grid cell with state and solution
- **CellPosition**: Represents cell coordinates (row, column)
- **GameBoard**: Complete puzzle grid with clues
- **GameState**: Manages current game state and move history
- **Puzzle**: Puzzle definition with solution and difficulty
- **Move**: Represents player moves for undo functionality
- **Hint**: Hint data structure with type and target
- **HintType**: Enumeration of available hint types
- **Difficulty**: Puzzle difficulty levels

### View Layer
- **MainFrame**: Main application window
- **GamePanel**: Game layout container
- **GridPanel**: Interactive cell grid
- **CluePanel**: Displays row/column clues

### Controller Layer
- **GameController**: Coordinates game logic and UI
- **PuzzleLoader**: Manages puzzle data
- **HintGenerator**: Generates hints for players

### Custom Data Structures
- **MyLinkedList**: Dynamic linked list for clue storage
- **MyStack**: Proper stack implementation with linked nodes (ready for undo functionality)
- **MyArrayList**: Dynamic array implementation
- **MyQueue**: Queue data structure for game processing
- **MyHashMap**: Hash map for efficient data storage

## Default Puzzles

The game includes three 5×5 puzzles:
1. **Heart**: Classic heart shape
2. **Cross**: Plus sign pattern
3. **Square**: Hollow square outline

## Development Notes

- No external dependencies (pure Java Swing)
- Custom data structures replace Java Collections
- MyLinkedList for efficient clue storage
- MyStack with proper linked node implementation
- Follows MVC design pattern
- Extensible architecture for adding features
