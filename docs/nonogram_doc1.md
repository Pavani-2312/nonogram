# Nonogram Game - Data Structures Architecture
## Technical Specification Document

---

## 1. Executive Summary

This document specifies the custom data structures required for implementing a Nonogram puzzle game. The implementation strictly prohibits the use of Java Collections Framework, requiring custom implementations of ArrayList, Stack, Queue, and HashMap. Each data structure serves specific functional requirements within the game architecture.

---

## 2. Data Structure Overview

### 2.1 Required Custom Implementations

| Data Structure | Primary Use Case | Key Operations | Complexity |
|---------------|------------------|----------------|------------|
| MyLinkedList<E> | Clue storage, dynamic sequences | add, get, size | O(1) add, O(n) get |
| MyStack<E> | Undo/redo mechanism | push, pop, peek | O(1) |

### 2.2 Implementation Rationale

**MyLinkedList vs MyArrayList**:
- Better memory efficiency for variable-length clue sequences
- No need for array resizing operations
- Optimal for frequent additions (clue generation)
- Acceptable O(n) access time for small clue lists

**MyStack with Linked Nodes**:
- Pure implementation without using pre-defined structures
- Proper LIFO behavior with linked nodes
- Memory efficient for undo operations

---

## 3. MyLinkedList<E> - Linked List Implementation

### 3.1 Functional Role in Nonogram

MyLinkedList serves as the foundational data structure for storing variable-length sequences throughout the application.

#### 3.1.1 Clue Number Storage
**Purpose**: Each row and column in a Nonogram puzzle contains a sequence of clue numbers indicating consecutive filled cells.

**Implementation Requirement**:
- Row 1 clue "3" → MyLinkedList containing single Integer: [3]
- Row 2 clue "2 1 3" → MyLinkedList containing: [2, 1, 3]
- Row 3 clue "1 1 1 1" → MyLinkedList containing: [1, 1, 1, 1]

**Data Structure Nesting**:
```
MyLinkedList<MyLinkedList<Integer>> rowClues
├─ Index 0: MyLinkedList [2, 3]      // Row 0 clues
├─ Index 1: MyLinkedList [1, 1]      // Row 1 clues
├─ Index 2: MyLinkedList [5]         // Row 2 clues
└─ Index 3: MyLinkedList [2, 1, 2]   // Row 3 clues

MyLinkedList<MyLinkedList<Integer>> columnClues
├─ Index 0: MyLinkedList [3, 1]      // Column 0 clues
├─ Index 1: MyLinkedList [2, 2]      // Column 1 clues
└─ Index 2: MyLinkedList [1, 1, 1]   // Column 2 clues
```

**Rationale**: Variable-length sequences require dynamic storage. Fixed arrays would waste memory for short clues and limit long clues.

#### 3.1.2 Hint Management
**Purpose**: Store multiple generated hints for sequential display to the player.

**Structure**:
```
MyArrayList<Hint> availableHints
├─ Index 0: Hint("Row 5 complete overlap detected")
├─ Index 1: Hint("Column 2 has definite cells")
└─ Index 2: Hint("Edge deduction possible in Row 7")
```

**Operations Required**:
- `add(Hint)` - Append newly generated hint
- `get(int index)` - Retrieve specific hint for display
- `remove(int index)` - Remove displayed hint
- `size()` - Track remaining hints
- `clear()` - Reset hint list for new puzzle

#### 3.1.3 Cell Position Tracking
**Purpose**: Track multiple cell coordinates for highlighting, error display, and hint visualization.

**Use Cases**:
1. **Hint Highlighting**: Store cells to highlight when displaying hint
   ```
   MyArrayList<CellPosition> hintCells
   ├─ CellPosition(5, 3)  // Row 5, Column 3
   ├─ CellPosition(5, 4)
   ├─ CellPosition(5, 5)
   └─ CellPosition(5, 6)
   ```

2. **Error Marking**: Track incorrect cells during validation
   ```
   MyArrayList<CellPosition> errorCells
   ├─ CellPosition(3, 4)  // Player marked incorrectly
   └─ CellPosition(7, 2)  // Player filled incorrectly
   ```

3. **Completion Tracking**: Store indices of completed rows/columns
   ```
   MyArrayList<Integer> completedRows
   ├─ 0  // Row 0 complete
   ├─ 2  // Row 2 complete
   └─ 5  // Row 5 complete
   ```

#### 3.1.4 Puzzle Library Management
**Purpose**: Maintain catalog of all available puzzles with metadata.

**Structure**:
```
MyArrayList<Puzzle> puzzleLibrary
├─ Puzzle(id="EASY_HEART", size=5, difficulty=EASY)
├─ Puzzle(id="EASY_STAR", size=5, difficulty=EASY)
├─ Puzzle(id="MEDIUM_TREE", size=10, difficulty=MEDIUM)
├─ Puzzle(id="MEDIUM_CAT", size=10, difficulty=MEDIUM)
└─ Puzzle(id="HARD_DRAGON", size=15, difficulty=HARD)
```

**Operations Required**:
- `add(Puzzle)` - Add new puzzle to library
- `get(int index)` - Retrieve puzzle by position
- `size()` - Count available puzzles
- **Iteration** - Filter puzzles by difficulty, search by attributes

**Filtering Example**:
```
To get all EASY puzzles:
1. Create new MyArrayList<Puzzle> easyPuzzles
2. Iterate through puzzleLibrary
3. For each puzzle, if difficulty == EASY, add to easyPuzzles
4. Return easyPuzzles
```

### 3.2 Essential Methods

**Core Operations**:
- `add(E element)` - Append element to end, O(1)
- `get(int index)` - Retrieve element by position, O(n)
- `size()` - Get element count, O(1)
- `isEmpty()` - Check if empty, O(1)

**Implementation Structure**:
```java
public class MyLinkedList<E> {
    private Node<E> head;
    private int size;
    
    private static class Node<E> {
        E data;
        Node<E> next;
    }
    
    public void add(E element) { /* append to end */ }
    public E get(int index) { /* traverse to index */ }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}
```

**Performance Characteristics**:
- Add operation: O(1) - always append to end
- Get operation: O(n) - must traverse from head
- Memory efficient: no unused array slots
- Suitable for clue storage where access is infrequent

---

## 4. MyStack<E> - LIFO Stack Implementation

### 4.1 Functional Role in Nonogram

MyStack implements Last-In-First-Out behavior essential for reversible actions.

#### 4.1.1 Undo Mechanism
**Purpose**: Enable players to reverse their moves in reverse chronological order.

**Move Recording**:
Each cell state change creates a Move object containing:
- Row coordinate (int)
- Column coordinate (int)
- Previous state (CellState enum: UNKNOWN, FILLED, MARKED)
- New state (CellState enum: UNKNOWN, FILLED, MARKED)
- Timestamp (long, milliseconds since game start)

**Example Move Sequence**:
```
Player actions:
1. Click cell (3,5): UNKNOWN → FILLED
2. Click cell (3,6): UNKNOWN → MARKED
3. Click cell (2,8): UNKNOWN → FILLED
4. Click cell (3,5) again: FILLED → MARKED

Stack state after these moves:
TOP → Move(3,5, FILLED, MARKED, t=145000)
      Move(2,8, UNKNOWN, FILLED, t=127000)
      Move(3,6, UNKNOWN, MARKED, t=98000)
BASE → Move(3,5, UNKNOWN, FILLED, t=67000)
```

**Undo Operation Flow**:
```
Player presses Undo button:
1. Pop from undoStack → gets Move(3,5, FILLED, MARKED, t=145000)
2. Restore cell (3,5) to FILLED (previous state)
3. Push Move to redoStack for potential redo
4. Decrement move counter
5. Update UI to reflect change
```

**Implementation Details**:
```
MyStack<Move> undoStack = new MyStack<>();

// When player clicks cell:
Move move = new Move(row, col, oldState, newState, timestamp);
undoStack.push(move);
redoStack.clear();  // Clear redo stack on new move

// When player clicks Undo:
if (!undoStack.isEmpty()) {
    Move lastMove = undoStack.pop();
    gameBoard.getCell(lastMove.row, lastMove.col).setState(lastMove.previousState);
    redoStack.push(lastMove);
    moveCount--;
}
```

#### 4.1.2 Redo Mechanism
**Purpose**: Allow players to reapply undone moves.

**Redo Operation Flow**:
```
Player presses Redo button:
1. Pop from redoStack → gets most recently undone Move
2. Reapply cell state change (set to newState)
3. Push Move back to undoStack
4. Increment move counter
5. Update UI
```

**Implementation Details**:
```
MyStack<Move> redoStack = new MyStack<>();

// When player clicks Redo:
if (!redoStack.isEmpty()) {
    Move moveToRedo = redoStack.pop();
    gameBoard.getCell(moveToRedo.row, moveToRedo.col).setState(moveToRedo.newState);
    undoStack.push(moveToRedo);
    moveCount++;
}
```

**Critical Behavior**: New moves clear redo stack
```
When player makes ANY new move after undo:
- Clear entire redoStack
- Reason: Prevents inconsistent game state
- Example: Undo 3 moves, make new move → cannot redo those 3
```

### 4.2 Essential Methods

**Core Operations**:
- `push(E element)` - Add element to top, O(1)
- `pop()` - Remove and return top element, O(1)
- `peek()` - View top element without removal, O(1)
- `isEmpty()` - Check if stack empty, O(1)
- `size()` - Get element count, O(1)

**Implementation Structure**:
```java
public class MyStack<E> {
    private Node<E> top;
    private int size;
    
    private static class Node<E> {
        E data;
        Node<E> next;
    }
    
    public void push(E element) { /* add to top */ }
    public E pop() { /* remove from top */ }
    public E peek() { /* view top */ }
    public boolean isEmpty() { return top == null; }
}
```

**Pure Implementation**:
- No dependency on other data structures
- Direct linked node implementation
- Proper LIFO behavior
- Memory efficient for undo operations

---

## 5. MyQueue<E> - FIFO Queue Implementation

### 5.1 Functional Role in Nonogram

MyQueue implements First-In-First-Out behavior for ordered sequential processing.

#### 5.1.1 Hint Display System
**Purpose**: Present hints to players in logical difficulty order.

**Hint Priority System**:
Hints are generated and queued based on increasing complexity:

**Priority 1: Complete Line Hints**
- Easiest to understand
- Example: "Row 5 must be completely filled (clue: 10)"
- Detection: Row/column clue sum equals grid size

**Priority 2: Large Overlap Hints**
- Moderate difficulty
- Example: "Row 3 cells 4-8 must be filled (overlap detected)"
- Detection: Clue numbers large enough to force certain cells

**Priority 3: Edge Deduction Hints**
- Requires logical reasoning
- Example: "Column 2 has 3 cells filled, mark remaining 7 cells empty"
- Detection: Count filled cells, compare to clue sum

**Priority 4: Error Detection Hints**
- Points out mistakes
- Example: "Row 7 contradiction: too many filled cells"
- Detection: Validation check fails

**Priority 5: Strategic Hints**
- High-level guidance
- Example: "Start with Row 1, it has the largest single number"
- Detection: Analysis of clue patterns

**Queue Structure**:
```
Front → Hint(priority=1, "Row 5 complete fill")
        Hint(priority=1, "Column 3 complete fill")
        Hint(priority=2, "Row 2 overlap cells 3-6")
        Hint(priority=2, "Column 7 overlap cells 1-4")
        Hint(priority=3, "Row 8 edge deduction")
Rear  → Hint(priority=5, "Strategic: start Row 1")
```

**Hint Display Flow**:
```
1. Hint generation phase:
   - Analyze game board state
   - Generate all applicable hints
   - Sort by priority (1 to 5)
   - Enqueue in priority order

2. Player clicks "Show Hint":
   - Dequeue front hint (highest priority)
   - Display hint message in dialog
   - Highlight affected cells on grid
   - Increment hints_used counter
   - Apply hint penalty to score

3. Player clicks "Next Hint":
   - Dequeue next hint from queue
   - Repeat display process
   - Continue until queue empty
```

**Implementation Details**:
```
MyQueue<Hint> hintQueue = new MyQueue<>();

// During hint generation:
MyArrayList<Hint> allHints = generateAllHints(gameBoard);
sortHintsByPriority(allHints);  // Priority 1 first
for (int i = 0; i < allHints.size(); i++) {
    hintQueue.enqueue(allHints.get(i));
}

// When player requests hint:
if (!hintQueue.isEmpty()) {
    Hint currentHint = hintQueue.dequeue();
    displayHintDialog(currentHint);
    highlightCells(currentHint.getAffectedCells());
    gameState.incrementHintsUsed();
    gameState.applyHintPenalty();
}
```

#### 5.1.2 Auto-Fill Animation Queue
**Purpose**: Apply certain cell fills with visual animation delay.

**Use Case**: When auto-fill feature identifies definitively certain cells, apply them sequentially rather than instantly for visual clarity.

**Queue Structure**:
```
Front → CellPosition(2, 5, FILLED)
        CellPosition(2, 6, FILLED)
        CellPosition(2, 7, FILLED)
        CellPosition(4, 3, MARKED)
Rear  → CellPosition(7, 9, MARKED)
```

**Animation Flow**:
```
1. Auto-fill detection finds 15 certain cells
2. Enqueue all CellPosition objects
3. Start timer (100ms interval)
4. Each timer tick:
   - Dequeue one CellPosition
   - Apply cell state change
   - Show fill animation
   - Pause for visual effect
5. Continue until queue empty
6. Display completion message
```

#### 5.1.3 Tutorial Step Queue
**Purpose**: Guide new players through ordered tutorial steps.

**Queue Structure**:
```
Front → TutorialStep("Welcome to Nonogram!")
        TutorialStep("Click cells to fill them")
        TutorialStep("Right-click to mark empty")
        TutorialStep("Use clues to solve puzzle")
Rear  → TutorialStep("Try solving this puzzle!")
```

### 5.2 Essential Methods

**Core Operations**:
- `enqueue(E element)` - Add element to rear, O(1)
- `dequeue()` - Remove and return front element, O(1), throws NoSuchElementException if empty
- `peek()` - View front element without removal, O(1), throws NoSuchElementException if empty
- `isEmpty()` - Check if queue empty, O(1)
- `size()` - Get element count, O(1)
- `clear()` - Remove all elements, O(n)

**Internal Implementation**:
Circular array implementation with front and rear pointers, or linked node implementation.

---

## 6. MyHashMap<K, V> - Key-Value Store Implementation

### 6.1 Functional Role in Nonogram

MyHashMap provides constant-time lookup for key-value associations.

#### 6.1.1 Puzzle Lookup by Identifier
**Purpose**: Instant access to puzzle objects by unique string ID.

**Problem**: 
- Puzzle library contains 100+ puzzles
- Player selects "MEDIUM_TREE" from menu
- Linear search through MyArrayList would be O(n)
- HashMap provides O(1) lookup

**Structure**:
```
MyHashMap<String, Puzzle> puzzleMap

Key: "EASY_HEART"    → Value: Puzzle(solution=5×5 heart pattern, clues...)
Key: "EASY_STAR"     → Value: Puzzle(solution=5×5 star pattern, clues...)
Key: "MEDIUM_TREE"   → Value: Puzzle(solution=10×10 tree pattern, clues...)
Key: "MEDIUM_CAT"    → Value: Puzzle(solution=10×10 cat pattern, clues...)
Key: "HARD_DRAGON"   → Value: Puzzle(solution=15×15 dragon pattern, clues...)
```

**Operations**:
```
// Loading puzzle library:
puzzleMap.put("EASY_HEART", heartPuzzle);
puzzleMap.put("MEDIUM_TREE", treePuzzle);

// Player selects puzzle:
String selectedId = "MEDIUM_TREE";
Puzzle puzzle = puzzleMap.get(selectedId);  // O(1) lookup
loadPuzzle(puzzle);
```

#### 6.1.2 Statistics Storage per Puzzle
**Purpose**: Track performance metrics for each puzzle independently.

**Structure**:
```
MyHashMap<String, PuzzleStats> statisticsMap

Key: "EASY_HEART" → Value: PuzzleStats {
    timesAttempted: 5,
    timesSolved: 4,
    bestTime: 143 seconds,
    averageTime: 178 seconds,
    bestMoves: 42,
    averageMoves: 56,
    hintsUsed: 2
}

Key: "MEDIUM_TREE" → Value: PuzzleStats {
    timesAttempted: 3,
    timesSolved: 2,
    bestTime: 567 seconds,
    averageTime: 612 seconds,
    bestMoves: 134,
    averageMoves: 156,
    hintsUsed: 8
}
```

**Operations**:
```
// After completing puzzle:
PuzzleStats stats = statisticsMap.get(puzzleId);
if (stats == null) {
    stats = new PuzzleStats(puzzleId);
}
stats.recordSolution(elapsedTime, moveCount, hintsUsed);
if (elapsedTime < stats.bestTime) {
    stats.bestTime = elapsedTime;
}
statisticsMap.put(puzzleId, stats);

// Displaying statistics:
PuzzleStats stats = statisticsMap.get(currentPuzzleId);
displayStats(stats.bestTime, stats.averageTime, stats.timesAttempted);
```

#### 6.1.3 Cell State Fast Lookup
**Purpose**: Quick verification if specific cell has been modified during validation.

**Structure**:
```
MyHashMap<String, CellState> modifiedCells

Key: "3_5"  → Value: FILLED    // Row 3, Column 5
Key: "3_6"  → Value: MARKED    // Row 3, Column 6
Key: "7_2"  → Value: FILLED    // Row 7, Column 2
Key: "9_9"  → Value: MARKED    // Row 9, Column 9
```

**Key Format**: "row_column" (e.g., "3_5")

**Operations**:
```
// Recording cell modification:
String key = row + "_" + col;
modifiedCells.put(key, newState);

// Checking during validation:
String key = row + "_" + col;
CellState playerState = modifiedCells.get(key);
if (playerState == null) {
    playerState = UNKNOWN;  // Not modified yet
}
boolean correct = validateCell(playerState, actualSolution[row][col]);
```

#### 6.1.4 Keyboard Shortcut Mapping
**Purpose**: Map keyboard input to game actions.

**Structure**:
```
MyHashMap<Character, GameAction> keyBindings

Key: 'Z' → Value: UNDO_ACTION
Key: 'Y' → Value: REDO_ACTION
Key: 'H' → Value: SHOW_HINT_ACTION
Key: 'R' → Value: RESET_PUZZLE_ACTION
Key: 'N' → Value: NEW_GAME_ACTION
Key: 'C' → Value: CHECK_SOLUTION_ACTION
Key: 'Q' → Value: QUIT_GAME_ACTION
```

**Operations**:
```
// Setting up key bindings:
keyBindings.put('Z', GameAction.UNDO);
keyBindings.put('Y', GameAction.REDO);
keyBindings.put('H', GameAction.HINT);

// Handling key press:
char keyPressed = event.getKeyChar();
GameAction action = keyBindings.get(keyPressed);
if (action != null) {
    executeAction(action);
}
```

#### 6.1.5 Theme Color Configuration
**Purpose**: Store UI color scheme for different themes (Light, Dark, High Contrast).

**Structure**:
```
MyHashMap<String, Color> currentTheme

Key: "background"        → Value: Color(255, 255, 255)  // White
Key: "gridLines"         → Value: Color(0, 0, 0)        // Black
Key: "filledCell"        → Value: Color(0, 0, 0)        // Black
Key: "markedCell"        → Value: Color(200, 200, 200)  // Light gray
Key: "unknownCell"       → Value: Color(255, 255, 255)  // White
Key: "highlightColor"    → Value: Color(255, 255, 0)    // Yellow
Key: "errorColor"        → Value: Color(255, 0, 0)      // Red
Key: "completeColor"     → Value: Color(0, 255, 0)      // Green
Key: "clueText"          → Value: Color(0, 0, 0)        // Black
```

**Operations**:
```
// Loading theme:
loadTheme("DARK_MODE");
currentTheme.put("background", new Color(30, 30, 30));
currentTheme.put("filledCell", new Color(200, 200, 200));
currentTheme.put("gridLines", new Color(100, 100, 100));

// Using colors during painting:
Color bgColor = currentTheme.get("background");
graphics.setColor(bgColor);
graphics.fillRect(0, 0, width, height);
```

### 6.2 Essential Methods

**Core Operations**:
- `put(K key, V value)` - Insert/update entry, O(1) average
- `get(K key)` - Retrieve value, O(1) average, returns null if not found
- `remove(K key)` - Delete entry, O(1) average
- `containsKey(K key)` - Check key existence, O(1) average
- `containsValue(V value)` - Check value existence, O(n)
- `size()` - Get entry count, O(1)
- `isEmpty()` - Check if empty, O(1)
- `clear()` - Remove all entries, O(n)
- `keySet()` - Get all keys as MyArrayList<K>, O(n)
- `values()` - Get all values as MyArrayList<V>, O(n)

**Internal Implementation Requirements**:
- Hash table with separate chaining (linked list/array per bucket)
- Default capacity: 16 buckets
- Load factor: 0.75 (rehash when size > capacity * 0.75)
- Hash function: key.hashCode() % capacity
- Collision resolution: chaining with linked nodes

---

## 7. Data Structure Selection Decision Matrix

### 7.1 Selection Criteria

| Requirement | MyArrayList | MyStack | MyQueue | MyHashMap |
|-------------|-------------|---------|---------|-----------|
| Order preservation | ✓ | ✓ | ✓ | ✗ |
| Fast indexed access | ✓ | ✗ | ✗ | ✗ |
| Fast key lookup | ✗ | ✗ | ✗ | ✓ |
| LIFO behavior | ✗ | ✓ | ✗ | ✗ |
| FIFO behavior | ✗ | ✗ | ✓ | ✗ |
| Variable size | ✓ | ✓ | ✓ | ✓ |
| Duplicate elements | ✓ | ✓ | ✓ | Keys: ✗, Values: ✓ |
| Iteration | ✓ | ✓ | ✓ | ✓ |

### 7.2 Usage Recommendations

**Use MyArrayList when:**
- Ordered collection required
- Index-based access needed
- Frequent iteration over elements
- Size unknown or variable
- Examples: clues, puzzle lists, cell positions

**Use MyStack when:**
- LIFO behavior required
- Most recent item must be processed first
- Undo/redo functionality needed
- Examples: move history, navigation

**Use MyQueue when:**
- FIFO behavior required
- Processing order important
- Items consumed in received order
- Examples: hints, animations, tutorials

**Use MyHashMap when:**
- Fast lookup by unique identifier required
- Key-value associations needed
- Frequent searches without iteration
- No ordering required
- Examples: puzzle lookup, statistics, configurations

---

## 8. Integration Architecture

### 8.1 Data Structure Relationships

```
GameController
    └── MyStack<Move> undoStack

Puzzle
    ├── MyLinkedList<MyLinkedList<Integer>> rowClues
    └── MyLinkedList<MyLinkedList<Integer>> columnClues

GameBoard
    ├── MyLinkedList<MyLinkedList<Integer>> rowClues
    └── MyLinkedList<MyLinkedList<Integer>> columnClues

PuzzleLoader
    └── MyLinkedList<Puzzle> puzzleLibrary
```

### 8.2 Memory Efficiency Considerations

**Clue Storage**:
- 15×15 grid worst case: 15 rows + 15 columns = 30 MyLinkedList instances
- Each clue list: ~5 integers average = 30 × 5 = 150 integers total
- Linked list overhead: ~16 bytes per node
- Memory: ~2.4KB + overhead (acceptable)

**Move History**:
- 15×15 grid = 225 cells maximum
- Each Move object: ~40 bytes
- Stack nodes: ~16 bytes each
- Full undo stack: 225 × 56 = 12.6KB (acceptable)

**Performance Trade-offs**:
- MyLinkedList: O(1) add, O(n) get - optimal for clue generation, acceptable for small lists
- MyStack: O(1) all operations - optimal for undo functionality

---

## 9. Implementation Priority Order

**Phase 1: Core Data Structures**
1. MyLinkedList - Foundation for all clue storage
2. MyStack - Essential for undo functionality

**Phase 2: Integration**
1. Integrate MyLinkedList into GameBoard for clues
2. Integrate MyStack into GameController for undo/redo
3. Test with game components

**Phase 3: Testing**
1. Unit test each data structure independently
2. Integration test with game components
3. Performance test with various puzzle sizes
4. Memory test with extensive move history

---

## 10. Performance Requirements

**MyArrayList**:
- add() must complete in < 1ms for sizes up to 1000 elements
- get() must complete in < 0.1ms
- Growth strategy must minimize copy operations

**MyStack**:
- push() and pop() must complete in < 0.1ms
- Support minimum 500 moves without performance degradation

**MyQueue**:
- enqueue() and dequeue() must complete in < 0.1ms
- Support minimum 100 queued items

**MyHashMap**:
- put() and get() must complete in < 0.5ms for 1000 entries
- Collision rate should be < 20%
- Rehashing must complete in < 10ms

---

## Document Revision History
- Version 1.0 - Initial specification
- Date: 2025-11-12