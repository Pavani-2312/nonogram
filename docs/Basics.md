# Nonogram Game - Complete Rules & Mechanics Documentation

## Table of Contents
1. [What is Nonogram?](#1-what-is-nonogram)
2. [Game Rules in Detail](#2-game-rules-in-detail)
3. [Game Mechanics](#3-game-mechanics)
4. [Solving Strategies](#4-solving-strategies-for-players)
5. [Puzzle Library Structure](#5-puzzle-library-structure)
6. [Game Flow](#6-game-flow-diagram)
7. [Detailed Game Features](#7-detailed-game-features)
8. [Advanced Features](#8-advanced-features)
9. [Error Handling & Edge Cases](#9-error-handling--edge-cases)

---

## 1. What is Nonogram?

Nonogram (also known as **Picross**, **Griddlers**, or **Picture Cross**) is a picture logic puzzle where you fill cells in a grid according to numerical clues to reveal a hidden picture.

### Core Concept

- You have a blank grid (like graph paper)
- Numbers on the edges tell you how many consecutive cells to fill in each row/column
- When completed correctly, filled cells form a picture

### Example of a Simple 5×5 Nonogram

```
        2   1   3   1   2
      ┌───┬───┬───┬───┬───┐
    2 │ ■ │ ■ │   │   │   │  ← Row clue: 2 means "2 consecutive filled cells"
      ├───┼───┼───┼───┼───┤
  1 1 │ ■ │   │   │ ■ │   │  ← Row clue: 1 1 means "1 filled, gap, 1 filled"
      ├───┼───┼───┼───┼───┤
    3 │ ■ │ ■ │ ■ │   │   │  ← Row clue: 3 means "3 consecutive filled cells"
      ├───┼───┼───┼───┼───┤
    1 │   │   │ ■ │   │   │  ← Row clue: 1 means "1 filled cell"
      ├───┼───┼───┼───┼───┤
  2 1 │ ■ │ ■ │   │ ■ │   │  ← Row clue: 2 1 means "2 filled, gap, 1 filled"
      └───┴───┴───┴───┴───┘
        ↑   ↑   ↑   ↑   ↑
      Col Col Col Col Col
      clues
```

**Result:** This reveals the picture of a HEART ❤️

---
## 2. Game Rules in Detail

### 2.1 The Grid

#### Grid Structure

- **Square grid** with N × N cells (typically 5×5, 10×10, 15×15, or 20×20)
- Each cell can be in one of **three states**:
  - **Empty/Unknown** (white) - Not yet determined
  - **Filled** (black) - Player believes this should be filled  
  - **Marked with X** (grey with X) - Player believes this should be empty

#### Grid Sizes and Difficulty

| Grid Size | Difficulty | Time to Solve | Best For |
|-----------|------------|---------------|----------|
| **5×5**   | Easy       | 2-5 minutes   | Beginners, quick games |
| **10×10** | Medium     | 10-20 minutes | Regular players |
| **15×15** | Hard       | 20-40 minutes | Experienced players |
| **20×20** | Expert     | 40-90 minutes | Advanced puzzles |

### 2.2 Understanding Clues

#### What Clues Represent

**Row Clues (Left side of grid):**
- Numbers indicate groups of consecutive filled cells in that row
- Numbers are listed from left to right

**Column Clues (Top of grid):**
- Numbers indicate groups of consecutive filled cells in that column  
- Numbers are listed from top to bottom

#### Clue Examples

##### Example 1: Single Number

**Row clue:** `3`

Possible solutions for a 5-cell row:
```
■ ■ ■ □ □  ✓ Valid
□ ■ ■ ■ □  ✓ Valid  
□ □ ■ ■ ■  ✓ Valid
■ ■ □ ■ □  ✗ Invalid (not consecutive)
```

##### Example 2: Multiple Numbers

**Row clue:** `2 1`

Possible solutions for a 5-cell row:
```
■ ■ □ ■ □  ✓ Valid (2 filled, gap, 1 filled)
■ ■ □ □ ■  ✓ Valid (2 filled, gaps, 1 filled)
□ ■ ■ □ ■  ✓ Valid (gap, 2 filled, gap, 1 filled)
■ ■ ■ □ □  ✗ Invalid (3 consecutive, not 2 and 1)
■ ■ ■ □ ■  ✗ Invalid (3 consecutive, not 2 and 1)
```

##### Example 3: Zero (All Empty)

**Row clue:** `0` OR (no numbers shown)

Only solution for any row:
```
□ □ □ □ □  ✓ Valid (all cells empty)
```

##### Example 4: Complete Fill

**Row clue:** `5` (for a 5-cell row)

Only solution:
```
■ ■ ■ ■ ■  ✓ Valid (entire row filled)
```

#### Critical Clue Rules

1. **Minimum Gap Rule**
   - Between different number groups, there MUST be at least 1 empty cell
   - Example: "2 3" means 2 filled, at least 1 empty, then 3 filled

2. **Order Rule**
   - Numbers appear in the order the groups appear
   - "2 1" means the group of 2 comes before the group of 1

3. **Complete Coverage Rule**
   - All filled cells in a row/column must be accounted for in the clues
   - No extra filled cells allowed

4. **Multiple Solutions Check**
   - Clues must lead to ONLY ONE correct solution
   - Good puzzle design ensures uniqueness

### 2.3 How Clues Are Generated

#### Algorithm: From Picture to Clues

Let's say we have this 5×5 picture (solution):

```
Row 0:  ■ ■ □ □ □
Row 1:  ■ □ ■ ■ □
Row 2:  ■ ■ ■ □ □
Row 3:  □ □ ■ □ □
Row 4:  ■ ■ □ ■ □
```

#### Step 1: Generate Row Clues

```java
For each row:
    count = 0
    clues = []
    
    For each cell in row:
        if cell is filled:
            count++
        else:
            if count > 0:
                clues.add(count)
                count = 0
    
    if count > 0:
        clues.add(count)
    
    if clues is empty:
        display "0" or leave blank
```

**Applying to our picture:**

```
Row 0: ■ ■ □ □ □ → Clue: 2
       (2 consecutive, then empty)

Row 1: ■ □ ■ ■ □ → Clue: 1 2
       (1 filled, empty, 2 filled)

Row 2: ■ ■ ■ □ □ → Clue: 3
       (3 consecutive)

Row 3: □ □ ■ □ □ → Clue: 1
       (1 filled)

Row 4: ■ ■ □ ■ □ → Clue: 2 1
       (2 filled, empty, 1 filled)
```

#### Step 2: Generate Column Clues

Same algorithm, but process columns vertically:

```
Col 0:  Col 1:  Col 2:  Col 3:  Col 4:
  ■       ■       □       □       □
  ■       □       ■       ■       □
  ■       ■       ■       □       □
  □       □       ■       □       □
  ■       ■       □       ■       □

Clue:   Clue:   Clue:   Clue:   Clue:
3 1     2 1     3       1 1     0
```

#### Final Clue Display:

```
        3   2   3   1   0
        1   1       1
      ┌───┬───┬───┬───┬───┐
    2 │   │   │   │   │   │
      ├───┼───┼───┼───┼───┤
  1 2 │   │   │   │   │   │
      ├───┼───┼───┼───┼───┤
    3 │   │   │   │   │   │
      ├───┼───┼───┼───┼───┤
    1 │   │   │   │   │   │
      ├───┼───┼───┼───┼───┤
  2 1 │   │   │   │   │   │
      └───┴───┴───┴───┴───┘
```

---

### 2.4 Player Interactions

#### Cell States and Actions

**State 1: Unknown (Initial)**
- **Appearance:** White/Empty cell
- **Meaning:** Player hasn't decided yet
- **Action:** Click to fill

**State 2: Filled**
- **Appearance:** Black/Dark cell
- **Meaning:** Player believes this cell should be filled
- **Action:** Click again to mark as empty

**State 3: Marked as Empty (X)**
- **Appearance:** Grey cell with red X
- **Meaning:** Player is certain this cell should remain empty
- **Action:** Click again to return to unknown

#### Click Cycle
```
Unknown (□) → Left Click → Filled (■) → Left Click → Marked (X) → Left Click → Unknown (□)
     ↑                                                                                ↓
     └────────────────────────────────────────────────────────────────────────────────┘
```

#### Mouse Controls

**Left Click:**
- On Unknown cell → Fill it (make it black)
- On Filled cell → Mark it with X (grey with X)
- On Marked cell → Clear it (back to unknown)

**Right Click (Alternative control):**
- Directly toggle between Unknown and Marked
- Skip the Filled state

**Drag to Fill (Advanced):**
- Hold left button and drag → Fill multiple cells
- Useful for filling obvious consecutive cells

### 2.5 Validation System & Error Handling

#### Real-Time Solution Validation
The game continuously compares player choices against the predetermined solution to provide immediate feedback and enforce game rules. Each cell placement is instantly validated against the correct answer stored in memory.

#### Solution Comparison Process
When a player clicks a cell, the system checks whether the new state matches what should actually be in that position:
- If player fills a cell that should be empty → Error detected
- If player marks a cell that should be filled → Error detected  
- If player's choice matches the solution → Valid move accepted

#### Three-Strike Error System

**Error Tracking Mechanism:**
Players begin each puzzle with exactly 3 allowed mistakes. The system maintains an error counter that increments with each incorrect cell placement. Upon reaching the 4th error, the game terminates immediately.

**Error Classification:**
The system distinguishes between actual errors and acceptable actions:

**Counted as Errors:**
- Filling a cell black when it should remain empty
- Marking a cell with X when it should be filled black

**Not Counted as Errors:**
- Changing an unknown cell to the correct state
- Reverting an incorrect cell back to unknown
- Using the undo function to correct mistakes
- Clicking the same cell multiple times without changing state

**Progressive Warning System:**
The game provides escalating feedback as errors accumulate:

**First Error Response:**
- Cell briefly flashes red to indicate mistake
- Error counter displays "Errors: 1/3"
- Subtle warning sound plays
- Game continues normally

**Second Error Response:**
- Red flash animation on incorrect cell
- Counter shows "Errors: 2/3" with yellow warning color
- System displays cautionary message about remaining chances

**Third Error Response:**
- Prominent red flash on cell
- Counter displays "Errors: 3/3" in critical red color
- Final warning appears: "One more error will end the game"
- Interface elements briefly pulse red for emphasis

**Fourth Error (Game Termination):**
- Immediate game over upon error detection
- All player interactions disabled instantly
- Timer stops at current time
- Game over screen appears with final statistics

#### Game Completion Conditions

**Victory Requirements:**
The puzzle is considered solved when all cells match their intended solution state and the error limit hasn't been exceeded. The system verifies that every cell requiring a fill is marked as filled, while ensuring no incorrect fills exist.

**Defeat Condition:**
The game ends in failure when the player makes their fourth error, regardless of puzzle completion percentage. This creates a strategic element where players must balance speed with accuracy.

**End Game Statistics:**
Upon completion (victory or defeat), the system presents comprehensive performance metrics including time elapsed, total moves made, error count, completion percentage, and calculated score based on efficiency and accuracy.

## 3. Game Mechanics

### 3.1 Starting a New Game

#### Step 1: Choose Difficulty
```
Easy (5×5):     [Select]
  - 25 cells
  - Simple patterns
  - 2-5 minute solve time

Medium (10×10): [Select]
  - 100 cells
  - Moderate complexity
  - 10-20 minute solve time

Hard (15×15):   [Select]
  - 225 cells
  - Complex patterns
  - 30-60 minute solve time
```

#### Step 2: Puzzle Loading

**Option A: Random from Library**
```java
puzzleLibrary.selectRandom(difficulty)
```

**Option B: Specific Puzzle**
```java
puzzleLibrary.loadPuzzle("EASY_HEART")
```

#### Step 3: Initialize Game State
```java
1. Create empty grid (all cells UNKNOWN)
2. Generate clues from solution
3. Hide actual solution from player
4. Start timer
5. Reset move counter
6. Clear undo stack
```

### 3.2 During Gameplay

#### Information Display

**Top Panel:**
```
┌─────────────────────────────────────┐
│ Timer: 05:23                        │
│ Moves: 47                           │
│ Completion: 67%                     │
│ [Undo] [Redo] [Hint] [Check]      │
└─────────────────────────────────────┘
```

**Timer:**
- Starts when first cell is clicked
- Pauses when hint is shown
- Stops when puzzle is solved

**Move Counter:**
- Increments on every cell click
- Used to track efficiency
- Lower is better

**Completion Percentage:**
```java
correctCells = count of cells matching solution
totalCells = rows × cols
percentage = (correctCells / totalCells) × 100
```

#### Visual Feedback

**Clue Highlighting (Advanced Feature):**
```
When hovering over row/column:
✓ Highlight corresponding clues
✓ Dim other clues
✓ Show cell numbers (optional)
```

**Cell Highlighting:**
```
When clicking cell at (row, col):
✓ Highlight entire row
✓ Highlight entire column
✓ Show cross-hair effect
```

**Progress Indicators:**
```
Row/Column Complete:
✓ Change clue color to green
✓ Draw checkmark next to clue
✓ Slight celebration animation
```

### 3.3 Hint System

#### How Hints Work

The hint system analyzes the current board state and provides strategic guidance without directly revealing the solution. It employs logical deduction algorithms to identify cells that can be determined with certainty based on current information.

#### Hint Generation Strategy

The system prioritizes hints based on solving efficiency and learning value:

**Priority Level 1: Immediate Certainties**
The system first identifies rows or columns where clues can be satisfied in only one way given the current grid state. These represent the most valuable hints as they provide definitive answers.

**Priority Level 2: Overlap Analysis**  
For clues with large numbers relative to row/column length, the system calculates positional overlaps. When a clue is large enough, certain cells must be filled regardless of the exact positioning of the consecutive block.

**Priority Level 3: Elimination Logic**
The system identifies cells that cannot possibly be filled based on already-satisfied clues or spatial constraints. These elimination hints help players mark cells with confidence.

**Priority Level 4: Cross-Reference Opportunities**
Advanced hints analyze the interaction between row and column constraints, identifying cells where both horizontal and vertical clues provide converging evidence.

**Priority Level 5: Error Detection**
The system can identify contradictions in the current grid state, alerting players to mistakes that violate the fundamental rules of nonogram solving.

#### Hint Delivery Methods

**Textual Guidance:**
Hints are presented as natural language explanations that teach solving techniques rather than simply providing answers. This educational approach helps players develop independent solving skills.

**Visual Highlighting:**
When appropriate, hints can include visual emphasis on relevant grid areas, helping players understand the spatial relationships being discussed.

**Progressive Disclosure:**
The system can provide increasingly specific guidance, starting with general strategy suggestions and progressing to more detailed analysis if needed.

#### Hint Cost Mechanism

To maintain challenge integrity, the hint system implements usage limitations:
- Initial hints may be provided freely to encourage learning
- Subsequent hints incur penalties to final scoring or timing
- The cost structure encourages strategic hint usage rather than over-reliance

#### **Hint Delivery**

**Method 1: Text Popup**
```
┌─────────────────────────────────┐
│           HINT                  │
├─────────────────────────────────┤
│ Row 5 has clue "7". In a 10-   │
│ cell row, this creates overlap. │
│ Cells 4-7 MUST be filled.       │
│                                 │
│ [OK] [Show Me] [Next Hint]     │
└─────────────────────────────────┘
```

**Method 2: Visual Highlight**
```
If user clicks "Show Me":
✓ Highlight suggested row/column
✓ Pulse the cells mentioned
✓ Temporarily show which cells are certain
```

**Hint Cost:**
```
- First hint: Free
- Subsequent hints: 30 seconds added to timer
- Or: Hints reduce final score
```

---

### **3.4 Check Solution Feature**

#### **Instant Check**
```
[Check Solution] button clicked:

Step 1: Validate all cells
Step 2: Show result

Result A - Perfect:
  "🎉 Congratulations! Puzzle solved correctly!"
  "Time: 12:34"
  "Moves: 156"

Result B - Has Errors:
  "❌ Not quite right. 3 errors found."
  "Completion: 87%"
  
  Options:
  [Show Errors] - Highlight wrong cells in red
  [Continue]    - Keep trying
  [Give Up]     - Show solution

Result C - Incomplete:
  "⏸ Puzzle not complete. 23 cells remaining."
  "Current accuracy: 94%"
```

#### **Error Highlighting**
```
When "Show Errors" is clicked:

Wrong Filled Cell:
  ■ → ⚠ (Red cell with warning icon)

Wrong Marked Cell:
  X → ⚠ (Red X with warning)

Correct cells:
  ■ → ✓ (Green check or no change)

3.5 Undo/Redo System
Undo Functionality

What is Recorded:
java

class Move {
    int row;
    int col;
    CellState previousState;  // What it was before
    CellState newState;       // What it became
    long timestamp;           // When it happened
}
```

**Undo Operation:**
```
User clicks [Undo]:

1. Pop latest move from undo stack
2. Restore cell to previous state
3. Push move to redo stack
4. Decrement move counter
5. Update display
```

**Undo Limits:**
```
Maximum undo depth: 50 moves
Reason: Balance between usability and memory

If 51st move is made:
  - Oldest move is forgotten
  - Can only undo last 50 moves
```

#### **Redo Functionality**

**Redo Operation:**
```
User clicks [Redo]:

1. Pop latest move from redo stack
2. Reapply cell state change
3. Push move back to undo stack
4. Increment move counter
5. Update display
```

**Redo is Cleared When:**
```
- User makes a new move (not undo/redo)
- User clicks "New Game"
- User clicks "Reset"
```

#### **Visual Feedback**
```
[Undo] button states:
  Enabled:  Dark button, clickable
  Disabled: Grey button, not clickable (no moves to undo)

[Redo] button states:
  Enabled:  Dark button, clickable
  Disabled: Grey button, not clickable (no moves to redo)
```

---

### **3.6 Reset and Give Up**

#### **Reset Puzzle**
```
[Reset] button clicked:

Confirmation dialog:
  "Reset puzzle? All progress will be lost."
  [Cancel] [Reset]

If confirmed:
  1. Clear all cell states (back to UNKNOWN)
  2. Keep same puzzle/clues
  3. Reset timer to 0
  4. Reset move counter to 0
  5. Clear undo/redo stacks
  6. Keep puzzle loaded
```

#### **Give Up / Show Solution**
```
[Show Solution] button clicked:

Confirmation dialog:
  "Show solution? This will mark the puzzle as incomplete."
  [Cancel] [Show Solution]

If confirmed:
  1. Reveal all filled cells
  2. Mark all empty cells with light grey
  3. Stop timer
  4. Disable all interactions
  5. Show statistics:
     "Time: 08:45"
     "Moves: 234"
     "Completion: 67%"
     "Status: Gave Up"
```

## 4. Solving Strategies (For Players)

### 4.1 Basic Strategies

#### Strategy 1: Start with Large Numbers
```
Example: 10-cell row with clue "9"

Possible positions:
■ ■ ■ ■ ■ ■ ■ ■ ■ □  (position 1)
□ ■ ■ ■ ■ ■ ■ ■ ■ ■  (position 2)

Overlap (must be filled):
□ ■ ■ ■ ■ ■ ■ ■ ■ □
  ↑_______________↑
  Cells 2-9 must be filled!
```

**Rule of Thumb:**
```
If clue number > half of row/column length:
  → There WILL be overlap
  → Start here first!
```

#### Strategy 2: Complete Rows/Columns
```
If clue sum == row length:
  → Fill entire row

Example: 10-cell row with clue "10"
  ■ ■ ■ ■ ■ ■ ■ ■ ■ ■  (100% certain)

Example: 10-cell row with clue "3 2 5"
  3 + 2 + 5 = 10 cells
  Minimum gaps: 2 gaps × 1 cell = 2 cells
  Total needed: 10 + 2 = 12 cells
  Available: 10 cells
  → IMPOSSIBLE! (Puzzle error)
  
  BUT if clue is "3 2 4":
  3 + 2 + 4 = 9 cells
  Minimum gaps: 2 × 1 = 2 cells
  Total needed: 9 + 2 = 11 cells
  Available: 10 cells
  → Still impossible!

  BUT if clue is "3 2 3":
  3 + 2 + 3 = 8 cells
  Minimum gaps: 2 × 1 = 2 cells
  Total needed: 8 + 2 = 10 cells
  Available: 10 cells
  → Only ONE arrangement:
     ■ ■ ■ □ ■ ■ □ ■ ■ ■
```

#### Strategy 3: Edge Deduction
```
10-cell row with clue "7"

Left-aligned:  ■ ■ ■ ■ ■ ■ ■ □ □ □
Right-aligned: □ □ □ ■ ■ ■ ■ ■ ■ ■

Overlap:       □ □ □ ■ ■ ■ ■ □ □ □
                     ↑_____↑
                 Cells 4-7 certain!

Fill these cells immediately!
```

**Formula:**
```
If clue = N and row length = L:
  Overlap size = N - (L - N)
  Overlap size = 2N - L

Only works if: N > L/2

Overlap starts at: L - N + 1
Overlap ends at:   N
```

#### Strategy 4: Mark Impossible Cells
```
10-cell row with clue "3"
Already filled: cells 1-3

Current state: ■ ■ ■ □ □ □ □ □ □ □
                    ↑_______________↑
                    All these MUST be empty!

Mark with X: ■ ■ ■ X X X X X X X
```

### 4.2 Advanced Strategies

#### Strategy 5: Cross-Referencing
```
Grid state:
     C1  C2  C3  C4  C5
R1   □   ■   □   □   □    Clue: 1
R2   □   ■   □   □   □    Clue: 1
R3   □   ■   □   □   □    Clue: 1
R4   □   □   □   □   □    Clue: 0
R5   □   □   □   □   □    Clue: 0

Column 2 clue: 3

Analysis:
- Rows 1-3 each have exactly 1 filled cell
- It's in column 2
- That's 3 filled cells in column 2
- Column 2 clue is "3"
- Column 2 is COMPLETE!
- R4-C2 and R5-C2 must be empty

Result:
R4   □   X   □   □   □
R5   □   X   □   □   □
```

#### Strategy 6: Split Analysis
```
10-cell row with clue "2 2"

Possible arrangements:
■ ■ □ ■ ■ □ □ □ □ □  (position 1)
■ ■ □ □ ■ ■ □ □ □ □  (position 2)
■ ■ □ □ □ ■ ■ □ □ □  (position 3)
... many more ...
□ □ □ □ □ ■ ■ □ ■ ■  (position N)

Analyze each position:
- What cells are ALWAYS filled?
- What cells are NEVER filled?

Result:
■ ■ □ ? ? ? ? ? ? ?  (Cells 1-2 always filled)
? ? ? ? ? ? ? ? □ □  (Cells 9-10 never filled)
```

#### Strategy 7: Recursive Solving
```
Row clue: 3 2

Step 1: Try leftmost position
  ■ ■ ■ □ ■ ■ □ □ □ □

Step 2: Check column constraints
  If Column 1 already has enough filled cells:
    Position 1 is INVALID
    Try next position

Step 3: Recurse until valid arrangement found
```

---

## 5. Puzzle Library Structure & Generation

### 5.1 Dual Puzzle System

The game employs a hybrid approach combining pre-designed puzzles with dynamic generation capabilities.

#### Fixed Puzzle Collection
A curated library contains hand-crafted puzzles designed for optimal solving experience:

**Easy Puzzles (5×5 grids):**
- Simple recognizable shapes (heart, star, cross)
- Clear, unambiguous clue patterns
- Designed for learning basic solving techniques
- Guaranteed unique solutions with minimal complexity

**Medium Puzzles (10×10 grids):**
- More detailed images (tree, cat, flower)
- Multiple solving strategies required
- Balanced difficulty progression
- Intermediate pattern recognition challenges

**Hard Puzzles (15×15 grids):**
- Complex artistic designs
- Advanced logical deduction required
- Multiple interconnected solving paths
- High-detail imagery when completed

**Expert Puzzles (20×20 grids):**
- Intricate detailed artwork
- Master-level solving techniques needed
- Extended solving sessions (45+ minutes)
- Professional-quality final images

#### Dynamic Puzzle Generation

**Random Grid Creation Process:**
The system generates new puzzles by creating random filled patterns and deriving clues mathematically:

1. **Pattern Generation:** Random placement of filled cells following aesthetic guidelines
2. **Clue Derivation:** Automatic calculation of row and column number sequences
3. **Solution Validation:** Verification that generated clues produce unique solutions
4. **Difficulty Assessment:** Analysis of solving complexity and required techniques

**Clue Generation Algorithm:**
For each completed grid pattern, the system analyzes consecutive filled cells:
- Scans each row left-to-right counting consecutive filled blocks
- Scans each column top-to-bottom counting consecutive filled blocks  
- Generates number sequences representing these consecutive groups
- Ensures minimum gaps between groups for valid puzzle structure

**Quality Assurance Process:**
Generated puzzles undergo automated testing:
- Unique solution verification using constraint satisfaction
- Difficulty rating based on required solving techniques
- Aesthetic evaluation of final revealed image
- Rejection of trivial or overly complex patterns

### 5.2 Puzzle Storage & Access

#### Memory-Based Storage
New randomly generated puzzles exist only during active gameplay sessions. They are not permanently stored, ensuring fresh challenges while maintaining system efficiency.

#### Fixed Puzzle Persistence
Pre-designed puzzles are stored in structured data files containing:
- Grid solution patterns
- Pre-calculated clue sequences
- Metadata (difficulty, theme, estimated solve time)
- Quality ratings and player feedback integration

#### Selection Mechanism
Players can choose between:
- **Specific Selection:** Browse and select from the fixed puzzle library
- **Random Selection:** System automatically chooses from appropriate difficulty level
- **Generated Challenge:** Request a newly created random puzzle
- **Progressive Mode:** Systematic advancement through increasing difficulty levels

---

### 5.3 Clue Generation Process

#### Mathematical Derivation from Solution Grid

The system automatically generates row and column clues by analyzing the completed puzzle solution through systematic scanning algorithms.

**Row Analysis Process:**
Each horizontal row is examined from left to right, identifying consecutive sequences of filled cells. The system counts each unbroken chain of filled cells and records the length. When an empty cell interrupts a sequence, the current count is stored and a new count begins for the next filled sequence.

**Column Analysis Process:**  
Similarly, each vertical column is scanned from top to bottom using the same consecutive-counting methodology. This ensures that clues accurately represent the vertical patterns that players must deduce.

**Clue Sequence Formation:**
The resulting number sequences become the clues displayed to players. For example, if a row contains two separate groups of filled cells - one with 3 consecutive cells and another with 2 consecutive cells - the clue becomes "3 2".

**Validation and Quality Control:**
Generated clues undergo verification to ensure they produce exactly one valid solution. The system employs constraint satisfaction algorithms to confirm that the clue combinations lead to a unique grid configuration, preventing ambiguous puzzles that could frustrate players.

6. Game Flow Diagram

START
  ↓
┌─────────────────┐
│  Main Menu      │
│                 │
│ [New Game]      │ ──→ Select Difficulty ──→ Load Puzzle ──┐
│ [Continue]      │ ──→ Resume Saved Game ─────────────────┤
│ [Puzzle List]   │ ──→ Choose Specific ───────────────────┤
│ [Settings]      │                                         │
│ [Exit]          │                                         │
└─────────────────┘                                         │
                                                            ↓
                                              ┌─────────────────────┐
                                              │  GAMEPLAY           │
                                              │                     │
                                              │  Grid + Clues       │
                                              │  Timer              │
                                              │  Move Counter       │
                                              │                     │
                                              │  [Undo] [Hint]     │
                                              │  [Check] [Reset]

Continue

                                              │                     │
                                              └─────────────────────┘
                                                        │
                                      ┌─────────────────┼─────────────────┐
                                      │                 │                 │
                                   Player            Player           Player
                                   Clicks            Clicks           Clicks
                                   Cell              [Hint]           [Check]
                                      │                 │                 │
                                      ↓                 ↓                 ↓
                              ┌──────────────┐   ┌──────────────┐  ┌──────────────┐
                              │ Update Cell  │   │ Show Hint    │  │ Validate     │
                              │ State        │   │ Dialog       │  │ Solution     │
                              │              │   │              │  │              │
                              │ Unknown→Fill │   │ Queue.poll() │  │ Check all    │
                              │ Fill→Mark(X) │   │ Display text │  │ cells        │
                              │ Mark→Unknown │   │              │  │              │
                              │              │   │              │  │              │
                              │ Push to      │   │ Pause timer  │  │              │
                              │ Undo Stack   │   │              │  │              │
                              └──────────────┘   └──────────────┘  └──────────────┘
                                      │                 │                 │
                                      ↓                 ↓                 ↓
                              ┌──────────────┐   ┌──────────────┐  ┌──────────────┐
                              │ Increment    │   │ Resume play  │  │ All Correct? │
                              │ Move Counter │   │              │  │              │
                              └──────────────┘   └──────────────┘  │   Yes   No   │
                                      │                 │           │    ↓     ↓   │
                                      ↓                 ↓           │   WIN  Error │
                              ┌──────────────┐   ┌──────────────┐  │              │
                              │ Check if     │   │ [Continue]   │  └──────────────┘
                              │ Solved?      │   │ [Next Hint]  │         ↓
                              │              │   │ [Give Up]    │    Show Errors
                              │  Yes    No   │   └──────────────┘    [Continue]
                              │   ↓      ↓   │                        [Give Up]
                              │  WIN  Continue                             ↓
                              └──────────────┘                        Back to Game
                                      │
                                      ↓
                              ┌──────────────────┐
                              │  PUZZLE SOLVED!  │
                              │                  │
                              │  🎉 Success! 🎉  │
                              │                  │
                              │  Time: 12:34     │
                              │  Moves: 156      │
                              │  Score: 8520     │
                              │                  │
                              │  [New Puzzle]    │
                              │  [Same Diff]     │
                              │  [Main Menu]     │
                              └──────────────────┘
                                      │
                                      ↓
                              Save Statistics
                              Update Leaderboard
                                      │
                                      ↓
                              Back to Main Menu
                                      │
                                      ↓
                                    LOOP

7. Detailed Game Features
7.1 Timer System
Timer States

STOPPED (Initial):
  Display: 00:00
  Status: Waiting for first move

RUNNING (Active):
  Display: MM:SS (e.g., 05:23)
  Status: Counting up
  Update: Every 1 second

PAUSED (Hint shown):
  Display: MM:SS (frozen)
  Status: Hint dialog open
  Resume: When dialog closed

FINISHED (Solved):
  Display: Final time
  Status: Puzzle complete
  Action: Stop and save

Timer Implementation
java

class GameTimer {
    private int seconds;
    private boolean running;
    private Thread timerThread;
    
    public void start() {
        running = true;
        timerThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);  // 1 second
                    seconds++;
                    updateDisplay();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        timerThread.start();
    }
    
    public void pause() {
        running = false;
    }
    
    public void resume() {
        running = true;
    }
    
    public void stop() {
        running = false;
        if (timerThread != null) {
            timerThread.interrupt();
        }
    }
    
    public String getFormattedTime() {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}
```

#### **Timer Events**
```
Event: First cell clicked
  → Start timer

Event: Hint button clicked
  → Pause timer
  → Show hint dialog
  → Resume when closed

Event: Check button clicked
  → Keep running (no pause)
  → Stop only if solved

Event: Puzzle solved
  → Stop timer
  → Save final time

Event: Give Up clicked
  → Stop timer
  → Mark as DNF (Did Not Finish)

Event: New Game clicked
  → Reset timer to 00:00
```

---

### **7.2 Move Counter**

#### **What Counts as a Move**
```
✅ Counts as Move:
- Cell clicked (state changes)
- Drag fills multiple cells (each cell = 1 move)

❌ Does NOT Count:
- Clicking same cell twice quickly (no state change)
- Undo/Redo actions
- Hovering over cells
- Opening/closing dialogs
```

#### **Move Quality Scoring**
```
Efficient Player:
  Final moves ≈ Number of filled cells
  Example: 5×5 puzzle with 13 filled cells
           Ideal moves: ~13-20
           Score: Excellent

Average Player:
  Final moves = 1.5× to 2× number of cells
  Example: 5×5 puzzle (25 cells)
           Moves: 40-50
           Score: Good

Inefficient Player:
  Final moves > 3× number of cells
  Example: 5×5 puzzle (25 cells)
           Moves: 100+
           Score: Needs improvement
```

#### **Move Display**
```
┌──────────────────────┐
│ Moves: 47            │ ← Current count
│ Minimum: 25          │ ← Theoretical minimum
│ Efficiency: 53%      │ ← min/current ratio
└──────────────────────┘

7.3 Completion Percentage
Calculation Methods

Method 1: Correct Cells
java

int totalCells = rows × cols;
int correctCells = 0;

for each cell:
    if (cell.playerState == FILLED && cell.actualValue == true)
        correctCells++;
    if (cell.playerState == MARKED && cell.actualValue == false)
        correctCells++;
    if (cell.playerState == UNKNOWN && cell.actualValue == false)
        correctCells++;  // Unknown empty is technically correct

percentage = (correctCells × 100) / totalCells;

Method 2: Filled Cells Only (More Strict)
java

int totalFilledNeeded = count of all true cells in solution;
int correctFilled = 0;

for each cell where actualValue == true:
    if (cell.playerState == FILLED)
        correctFilled++;

percentage = (correctFilled × 100) / totalFilledNeeded;
```

#### **Display Styles**

**Text Display:**
```
Completion: 67% (17/25 cells correct)
```

**Progress Bar:**
```
[████████████░░░░░░░] 67%
```

**Color-Coded:**
```
0-33%:   Red    "Just started"
34-66%:  Yellow "Making progress"
67-99%:  Orange "Almost there!"
100%:    Green  "Perfect!"
```

---

### **7.4 Scoring System**

#### **Score Components**
```
Base Score = 1000 points

Time Bonus:
  Fast solve (< average time): +500
  Average solve: +0
  Slow solve: -200

Move Efficiency Bonus:
  Excellent (moves < 1.5× min): +300
  Good (moves < 2× min): +100
  Average: +0
  Poor: -100

Hint Penalty:
  Each hint used: -50 points

Error Penalty:
  Each error in final check: -25 points

Difficulty Multiplier:
  Easy (5×5): ×1.0
  Medium (10×10): ×1.5
  Hard (15×15): ×2.0
  Expert (20×20): ×3.0

Final Score = (Base + Time + Efficiency - Hints - Errors) × Difficulty
```

#### **Example Calculation**
```
Player solves Medium (10×10) puzzle:
  Time: 8 minutes (average is 12 minutes) → +500
  Moves: 85 (minimum is 50) → 85/50 = 1.7 ratio → +100
  Hints used: 2 → -100
  Errors: 0 → -0
  
  Calculation:
  (1000 + 500 + 100 - 100 - 0) × 1.5
  = 1500 × 1.5
  = 2250 points

7.5 Statistics Tracking
Per-Puzzle Statistics
java

class PuzzleStats {
    String puzzleId;
    
    // Completion
    int timesAttempted;
    int timesSolved;
    int timesGivenUp;
    
    // Performance
    int bestTime;          // In seconds
    int averageTime;
    int bestMoves;
    int averageMoves;
    int bestScore;
    
    // Efficiency
    double averageCompletion;  // When given up
    int totalHintsUsed;
    int totalErrorsMade;
    
    // Dates
    long firstAttempted;
    long lastAttempted;
    long lastSolved;
}

Global Statistics
java

class GlobalStats {
    // Overall
    int totalPuzzlesSolved;
    int totalPuzzlesAttempted;
    double solveRate;  // solved/attempted
    
    // Time
    int totalPlayTime;  // In seconds
    int averageSolveTime;
    int fastestSolve;
    String fastestPuzzleId;
    
    // Performance
    int totalMoves;
    int totalHintsUsed;
    int totalErrorsMade;
    int bestScore;
    
    // Difficulty breakdown
    int easySolved;
    int mediumSolved;
    int hardSolved;
    int expertSolved;
    
    // Streaks
    int currentStreak;  // Consecutive solves
    int longestStreak;
    int currentDailyStreak;  // Days in a row
}
```

#### **Statistics Display**
```
┌────────────────────────────────────┐
│        YOUR STATISTICS             │
├────────────────────────────────────┤
│                                    │
│ Puzzles Solved: 47 / 63 (75%)     │
│                                    │
│ Total Play Time: 12h 34m           │
│ Average Solve Time: 8m 23s         │
│                                    │
│ Best Score: 3,450                  │
│ Total Hints Used: 23               │
│                                    │
│ By Difficulty:                     │
│   Easy:   ████████████ 15/15       │
│   Medium: ████████░░░░ 20/25       │
│   Hard:   ████░░░░░░░░ 10/20       │
│   Expert: ██░░░░░░░░░░  2/15       │
│                                    │
│ Current Streak: 5 puzzles 🔥       │
│                                    │
│ [View Detailed Stats]              │
│ [Export to File]                   │
└────────────────────────────────────┘
```

---

## **8. Advanced Features**

### **8.1 Auto-Fill Feature (Optional)**

#### **What is Auto-Fill?**

Automatically fills cells that are 100% certain based on current board state and clues.

#### **When to Auto-Fill**
```
Scenario 1: Complete Row/Column
  Clue: 10 (in 10-cell row)
  Action: Fill entire row automatically
  
Scenario 2: Obvious Overlap
  Clue: 8 (in 10-cell row)
  Overlap: Cells 3-8 must be filled
  Action: Fill cells 3-8 automatically
  
Scenario 3: Clue Satisfied
  Row clue: 3
  Current: ■ ■ ■ □ □ □ □ □ □ □
  Action: Mark remaining cells with X automatically

Implementation
java

class AutoFillHelper {
    
    public MyArrayList<CellPosition> findCertainCells(GameBoard board) {
        MyArrayList<CellPosition> certainCells = new MyArrayList<>();
        
        // Check each row
        for (int row = 0; row < board.getRows(); row++) {
            certainCells.addAll(analyzeRow(board, row));
        }
        
        // Check each column
        for (int col = 0; col < board.getCols(); col++) {
            certainCells.addAll(analyzeColumn(board, col));
        }
        
        return certainCells;
    }
    
    private MyArrayList<CellPosition> analyzeRow(GameBoard board, int row) {
        MyArrayList<CellPosition> cells = new MyArrayList<>();
        Clue clue = board.getRowClues().get(row);
        
        // Check for complete fill
        if (clueSum(clue) == board.getCols()) {
            for (int col = 0; col < board.getCols(); col++) {
                cells.add(new CellPosition(row, col, CellState.FILLED));
            }
            return cells;
        }
        
        // Check for overlap
        if (clue.getNumbers().size() == 1) {
            int num = clue.getNumbers().get(0);
            int overlapSize = 2 * num - board.getCols();
            
            if (overlapSize > 0) {
                int start = board.getCols() - num;
                int end = num - 1;
                
                for (int col = start; col <= end; col++) {
                    if (board.getCell(row, col).getCurrentState() == CellState.UNKNOWN) {
                        cells.add(new CellPosition(row, col, CellState.FILLED));
                    }
                }
            }
        }
        
        return cells;
    }
}
```

#### **User Control**
```
Settings:
  [✓] Enable Auto-Fill
  [✓] Show Auto-Fill Suggestions (highlight in blue)
  [ ] Apply Auto-Fill Automatically
  
If "Show Suggestions" only:
  → Highlight certain cells in light blue
  → User must click [Apply Auto-Fill] button
  → Gives user control
  
If "Apply Automatically":
  → Fills certain cells instantly
  → User can still undo
  → More aggressive assistance
```

---

### **8.2 Color Themes**

#### **Theme Options**

**Classic (Default)**
```
Background:    White
Filled Cell:   Black
Marked Cell:   Light Grey + Red X
Grid Lines:    Black
Clues:         Black
```

**Dark Mode**
```
Background:    Dark Grey (#2b2b2b)
Filled Cell:   White
Marked Cell:   Dark Grey + White X
Grid Lines:    Light Grey
Clues:         White
```

**Colorful**
```
Background:    Light Blue
Filled Cell:   Deep Blue
Marked Cell:   Pink + Red X
Grid Lines:    Navy Blue
Clues:         Dark Blue
```

**High Contrast (Accessibility)**
```
Background:    White
Filled Cell:   Bright Yellow
Marked Cell:   Bright Red + Black X
Grid Lines:    Thick Black (3px)
Clues:         Large Bold Black
```

#### **Custom Colors**
```
┌────────────────────────────────┐
│      CUSTOMIZE THEME           │
├────────────────────────────────┤
│                                │
│ Background:    [🎨 White  ▼]  │
│ Filled Cells:  [🎨 Black  ▼]  │
│ Marked Cells:  [🎨 Grey   ▼]  │
│ Grid Lines:    [🎨 Black  ▼]  │
│ Clue Text:     [🎨 Black  ▼]  │
│                                │
│ Cell Size:     [40px     ▼]   │
│ Grid Thickness:[2px      ▼]   │
│                                │
│ [Preview] [Save] [Reset]       │
└────────────────────────────────┘

8.3 Save/Load Game State
What is Saved
java

class SavedGame {
    // Puzzle Info
    String puzzleId;
    int difficulty;
    int gridSize;
    
    // Current State
    CellState[][] currentGrid;  // Player's current progress
    
    // Game Progress
    int elapsedSeconds;
    int moveCount;
    
    // History
    MyStack<Move> undoStack;
    MyStack<Move> redoStack;
    
    // Statistics
    int hintsUsed;
    int checksPerformed;
    
    // Timestamp
    long savedAt;
}

Save Format (JSON-like)
json

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
    "UUUUUUUUUU",
    ...
  ],
  "undoStack": [
    {"row":3,"col":4,"prevState":"UNKNOWN","newState":"FILLED"},
    {"row":3,"col":5,"prevState":"UNKNOWN","newState":"FILLED"},
    ...
  ]
}

Legend:
  F = FILLED
  M = MARKED
  U = UNKNOWN
```

#### **Save/Load Operations**

**Save Game:**
```
User clicks [Save Game]:

1. Serialize current game state
2. Write to file: saves/game_TIMESTAMP.sav
3. Show confirmation: "Game saved successfully!"
4. Continue playing

Auto-save (optional):
  → Every 5 minutes
  → On app close
  → File: saves/autosave.sav (overwrite)
```

**Load Game:**
```
User clicks [Continue] or [Load Game]:

1. Show list of saved games:
   ┌────────────────────────────────┐
   │ MEDIUM_TREE                    │
   │ Progress: 67%                  │
   │ Time: 8:43                     │
   │ Saved: 2 hours ago             │
   │ [Load] [Delete]                │
   ├────────────────────────────────┤
   │ HARD_DRAGON                    │
   │ Progress: 34%                  │
   │ Time: 15:22                    │
   │ Saved: Yesterday               │
   │ [Load] [Delete]                │
   └────────────────────────────────┘

2. User selects game
3. Deserialize state
4. Restore grid, timer, moves, undo stack
5. Resume gameplay
```

---

### **8.4 Keyboard Shortcuts**

#### **Available Shortcuts**
```
GAMEPLAY:
  Space        → Toggle cell state (if cell selected)
  X            → Mark cell as empty
  Z            → Undo last move
  Ctrl+Z       → Undo last move
  Ctrl+Y       → Redo move
  H            → Show hint
  C            → Check solution
  R            → Reset puzzle
  
NAVIGATION:
  Arrow Keys   → Move cell selection
  Tab          → Next cell
  Shift+Tab    → Previous cell
  Home         → First cell in row
  End          → Last cell in row
  Page Up      → First row
  Page Down    → Last row
  
ZOOM:
  Ctrl + +     → Zoom in
  Ctrl + -     → Zoom out
  Ctrl + 0     → Reset zoom
  
GENERAL:
  Ctrl+N       → New game
  Ctrl+S       → Save game
  Ctrl+O       → Load game
  Esc          → Close dialog / Return to menu
  F1           → Help
```

#### **Cell Selection Visual**
```
When cell is selected with keyboard:
  ┌───────┐
  │       │ ← Thick yellow border
  │   ■   │
  │       │
  └───────┘
  
Arrow key navigation:
  Current: (3, 4)
  Right Arrow → (3, 5)
  Down Arrow  → (4, 4)
  
  Wraps at edges:
  At (9, 9), Right Arrow → (9, 0)

8.5 Hint System Deep Dive
Hint Priority Queue
java

class HintSystem {
    private MyQueue<Hint> hintQueue;
    private GameBoard board;
    
    public Hint getNextHint() {
        if (hintQueue.isEmpty()) {
            generateHints();
        }
        return hintQueue.dequeue();
    }
    
    private void generateHints() {
        // Priority 1: Immediate certainties
        findCompletableLines();
        
        // Priority 2: Large overlaps
        findOverlapOpportunities();
        
        // Priority 3: Edge deductions
        findEdgeCases();
        
        // Priority 4: Cross-reference opportunities
        findCrossReferences();
        
        // Priority 5: Contradiction detection
        findErrors();
        
        // Priority 6: General strategy
        if (hintQueue.isEmpty()) {
            addGeneralHints();
        }
    }
}
```

#### **Hint Types in Detail**

**Type 1: Completable Line**
```
Detection:
  Row 5 clue: 10
  Grid width: 10
  → Entire row must be filled

Hint Message:
  "Row 5 must be completely filled! 
   The clue is 10 and the row has 10 cells."

Visual Aid:
  → Highlight Row 5
  → Pulse animation
```

**Type 2: Large Overlap**
```
Detection:
  Row 3 clue: 8
  Grid width: 10
  Overlap calculation: 2×8 - 10 = 6 cells
  Overlap range: cells 3-8

Hint Message:
  "Row 3 has overlap! With a clue of 8 in a 
   10-cell row, cells 3 through 8 must be filled."

Visual Aid:
  → Highlight cells 3-8 in Row 3
  → Show left/right alignment diagrams
```

**Type 3: Edge Deduction**
```
Detection:
  Column 7 clue: 3
  Already filled: cells (2,7), (3,7), (4,7)
  → 3 consecutive cells already filled
  → Clue satisfied
  → All other cells in column must be empty

Hint Message:
  "Column 7 already has 3 filled cells in a row.
   The clue is 3, so it's complete! Mark the 
   remaining cells with X."

Visual Aid:
  → Highlight the 3 filled cells in green
  → Highlight remaining cells with suggestion marks
```

**Type 4: Contradiction**
```
Detection:
  Row 2 clue: 2 2
  Current state: ■ ■ ■ □ ■ ■ □ □ □ □
  → Has 3 consecutive, but clue says "2 2"
  → Contradiction detected

Hint Message:
  "⚠️ Row 2 has an error! You have 3 consecutive 
   filled cells, but the clue is '2 2', which 
   means two separate groups of 2."

Visual Aid:
  → Highlight error cells in red
  → Flash animation
```

**Type 5: Starting Strategy**
```
Detection:
  Board is mostly empty
  Find largest single-number clue
  Example: Row 7 has clue "12" (in 15-cell row)

Hint Message:
  "Good starting point: Row 7 has the biggest 
   number (12). This creates a large overlap, 
   making it easier to fill."

Visual Aid:
  → Highlight Row 7 clue
  → Show recommended starting cells
```

**Type 6: Cross-Reference**
```
Detection:
  Row 4 has 7 filled cells
  Row 4 clue is "7"
  → Row complete, but cells not marked
  
  OR
  
  Column 3 clue is "5"
  Already have 5 filled in Column 3
  → Remaining cells must be marked

Hint Message:
  "Row 4 is complete! It has exactly 7 filled 
   cells, matching the clue. You can mark the 
   remaining empty cells with X."

Visual Aid:
  → Green checkmark next to Row 4 clue
  → Highlight cells to be marked
```

#### **Hint Cost System**
```
First hint:    Free
Second hint:   +30 seconds penalty OR -50 points
Third hint:    +60 seconds penalty OR -100 points
Fourth+ hint:  +90 seconds penalty OR -150 points

Option 1: Time Penalty
  → Adds time to final solve time
  → Affects time-based scoring
  → Shows in statistics

Option 2: Point Penalty
  → Directly reduces score
  → Shown in hint dialog
  → More transparent

Option 3: Hint Tokens
  → Start with 3 free hints
  → Can earn more by solving puzzles
  → Gamification element
```

#### **Hint Dialog Design**
```
┌──────────────────────────────────────┐
│              💡 HINT                 │
├──────────────────────────────────────┤
│                                      │
│  Row 5 has overlap! With a clue of 8 │
│  in a 10-cell row, cells 3-8 must    │
│  be filled.                          │
│                                      │
│  ┌────────────────────────────────┐  │
│  │ Explanation:                   │  │
│  │ Left-align:  ■■■■■■■■□□        │  │
│  │ Right-align: □□■■■■■■■■        │  │
│  │ Overlap:     □□■■■■■■□□        │  │
│  └────────────────────────────────┘  │
│                                      │
│  [Show Me] [Got It] [Next Hint]     │
│                                      │
│  Hints used: 2/∞  Penalty: -100pts  │
└──────────────────────────────────────┘

9. Error Handling & Edge Cases
9.1 Invalid Puzzles
Puzzle Validation on Load
java

class PuzzleValidator {
    
    public ValidationResult validate(boolean[][] solution, 
                                     List<Clue> rowClues, 
                                     List<Clue> colClues) {
        
        // Check 1: Grid is rectangular
        if (!isRectangular(solution)) {
            return ValidationResult.error("Grid is not rectangular");
        }
        
        // Check 2: Row clues match solution
        for (int row = 0; row < solution.length; row++) {
            if (!validateRowClue(solution[row], rowClues.get(row))) {
                return ValidationResult.error("Row " + row + " clue mismatch");
            }
        }
        
        // Check 3: Column clues match solution
        for (int col = 0; col < solution[0].length; col++) {
            if (!validateColumnClue(solution, col, colClues.get(col))) {
                return ValidationResult.error("Column " + col + " clue mismatch");
            }
        }
        
        // Check 4: Puzzle has unique solution (advanced)
        if (!hasUniqueSolution(rowClues, colClues)) {
            return ValidationResult.warning("Puzzle may have multiple solutions");
        }
        
        return ValidationResult.success();
    }
    
    private boolean validateRowClue(boolean[] row, Clue clue) {
        List<Integer> actualGroups = new MyArrayList<>();
        int count = 0;
        
        for (boolean cell : row) {
            if (cell) {
                count++;
            } else {
                if (count > 0) {
                    actualGroups.add(count);
                    count = 0;
                }
            }
        }
        if (count > 0) {
            actualGroups.add(count);
        }
        
        // Compare actual groups with clue
        if (actualGroups.size() != clue.getNumbers().size()) {
            return false;
        }
        
        for (int i = 0; i < actualGroups.size(); i++) {
            if (!actualGroups.get(i).equals(clue.getNumbers().get(i))) {
                return false;
            }
        }
        
        return true;
    }
}
```

#### **Error Messages**
```
If puzzle file is corrupted:
  ┌─────────────────────────────────┐
  │  ❌ Error Loading Puzzle         │
  ├─────────────────────────────────┤
  │  The puzzle file is corrupted   │
  │  or invalid.                    │
  │                                 │
  │  Error: Row 5 clue mismatch     │
  │  Expected: 2 3 1                │
  │  Got: 2 2 1                     │
  │                                 │
  │  [Try Another Puzzle] [Report]  │
  └─────────────────────────────────┘

If puzzle has no solution:
  ┌─────────────────────────────────┐
  │  ⚠️ Warning                      │
  ├─────────────────────────────────┤
  │  This puzzle appears to have    │
  │  no valid solution.             │
  │                                 │
  │  Continue anyway?               │
  │                                 │
  │  [Yes] [Choose Different]       │
  └─────────────────────────────────┘

9.2 User Input Edge Cases
Rapid Clicking

Problem:
  User double-clicks cell very quickly
  → First click: UNKNOWN → FILLED
  → Second click: FILLED → MARKED
  → User intended single click

Solution:
  Implement debounce timer (200ms)
  
Code:
  long lastClickTime = 0;
  static final long DEBOUN


