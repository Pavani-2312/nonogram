# Nonogram Game - Algorithms & Solving Strategies
## Technical Specification Document

---

## 1. Overview

This document specifies the algorithms used throughout the Nonogram application, including clue generation, hint generation, validation, and solving strategies.

---

## 2. Clue Generation Algorithm

### 2.1 Purpose
Convert a boolean solution grid into row and column clue numbers.

### 2.2 Algorithm: Generate Row Clues

**Input**: boolean[] row (array of true/false values)
**Output**: MyArrayList<Integer> clues

**Pseudocode**:
```
function generateRowClue(boolean[] row):
    clues = new MyArrayList<Integer>()
    count = 0
    
    for each cell in row:
        if cell == true:
            count++
        else:
            if count > 0:
                clues.add(count)
                count = 0
    
    // Add final count if row ends with filled cells
    if count > 0:
        clues.add(count)
    
    // If no filled cells, return empty list (represents 0)
    return clues
```

**Example**:
```
Input:  [T, T, F, T, T, T, F, T]
        ■ ■ □ ■ ■ ■ □ ■

Step 1: cells 0-1 are true → count = 2
Step 2: cell 2 is false → add 2 to clues, reset count
Step 3: cells 3-5 are true → count = 3
Step 4: cell 6 is false → add 3 to clues, reset count
Step 5: cell 7 is true → count = 1
Step 6: end of row → add 1 to clues

Output: [2, 3, 1]
```

### 2.3 Algorithm: Generate All Clues

```java
public void generateAllClues(boolean[][] solution) {
    int rows = solution.length;
    int cols = solution[0].length;
    
    // Generate row clues
    for (int r = 0; r < rows; r++) {
        MyArrayList<Integer> clue = generateRowClue(solution[r]);
        rowClues.add(clue);
    }
    
    // Generate column clues
    for (int c = 0; c < cols; c++) {
        boolean[] column = extractColumn(solution, c);
        MyArrayList<Integer> clue = generateRowClue(column);
        columnClues.add(clue);
    }
}

private boolean[] extractColumn(boolean[][] grid, int colIndex) {
    boolean[] column = new boolean[grid.length];
    for (int r = 0; r < grid.length; r++) {
        column[r] = grid[r][colIndex];
    }
    return column;
}
```

---

## 3. Line Validation Algorithm

### 3.1 Purpose
Check if a row/column matches its clues.

### 3.2 Algorithm: Validate Line

**Input**: 
- MyArrayList<Cell> line (cells from row or column)
- MyArrayList<Integer> clues (expected clue numbers)

**Output**: boolean (true if valid, false otherwise)

**Pseudocode**:
```
function validateLine(cells, clues):
    // Extract filled positions from current state
    actualGroups = new MyArrayList<Integer>()
    count = 0
    
    for each cell in cells:
        if cell.getCurrentState() == FILLED:
            count++
        else:
            if count > 0:
                actualGroups.add(count)
                count = 0
    
    // Add final group
    if count > 0:
        actualGroups.add(count)
    
    // Compare actual groups to clues
    if actualGroups.size() != clues.size():
        return false
    
    for i from 0 to actualGroups.size():
        if actualGroups.get(i) != clues.get(i):
            return false
    
    return true
```

**Example**:
```
Clues: [2, 3, 1]
Player state: ■ ■ □ ■ ■ ■ □ ■ □ □

Step 1: Extract groups → [2, 3, 1]
Step 2: Compare [2, 3, 1] with clues [2, 3, 1]
Step 3: All match → return true

Player state: ■ ■ ■ □ ■ ■ □ ■ □ □

Step 1: Extract groups → [3, 2, 1]
Step 2: Compare [3, 2, 1] with clues [2, 3, 1]
Step 3: Don't match → return false
```

---

## 4. Overlap Detection Algorithm

### 4.1 Purpose
Find cells that MUST be filled based on clue overlap.

### 4.2 Algorithm: Calculate Overlap

**Input**: 
- int clueNumber (single clue value)
- int lineLength (row/column length)

**Output**: CellRange (start and end indices of overlap)

**Formula**:
```
overlapSize = (2 × clueNumber) - lineLength

If overlapSize > 0:
    overlapStart = lineLength - clueNumber
    overlapEnd = clueNumber - 1
    
    Cells from overlapStart to overlapEnd must be filled
Else:
    No overlap (no certain cells)
```

**Example 1**:
```
Clue: 7
Line length: 10

Calculation:
overlapSize = (2 × 7) - 10 = 14 - 10 = 4

Left-aligned:  ■ ■ ■ ■ ■ ■ ■ □ □ □  (positions 0-6)
Right-aligned: □ □ □ ■ ■ ■ ■ ■ ■ ■  (positions 3-9)

Overlap:       □ □ □ ■ ■ ■ ■ □ □ □
                     ↑_______↑
               positions 3-6 must be filled

overlapStart = 10 - 7 = 3
overlapEnd = 7 - 1 = 6
```

**Example 2**:
```
Clue: 3
Line length: 10

Calculation:
overlapSize = (2 × 3) - 10 = 6 - 10 = -4

No overlap (clue too small for guaranteed cells)
```

### 4.3 Implementation

```java
public class OverlapFinder {
    public static CellRange findOverlap(int clue, int length) {
        int overlapSize = (2 * clue) - length;
        
        if (overlapSize <= 0) {
            return null; // No overlap
        }
        
        int start = length - clue;
        int end = clue - 1;
        
        return new CellRange(start, end);
    }
}

class CellRange {
    int start;
    int end;
    
    CellRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
```

---

## 5. Edge Deduction Algorithm

### 5.1 Purpose
Identify cells that can be determined from edge constraints.

### 5.2 Algorithm: Left Edge Deduction

**Input**: 
- MyArrayList<Cell> line
- MyArrayList<Integer> clues

**Pseudocode**:
```
function leftEdgeDeduction(line, clues):
    certainCells = new MyArrayList<CellPosition>()
    
    // If first clue is large enough
    firstClue = clues.get(0)
    
    // Find first filled cell
    firstFilled = -1
    for i from 0 to line.size():
        if line.get(i).getCurrentState() == FILLED:
            firstFilled = i
            break
    
    if firstFilled == -1:
        return certainCells // No filled cells yet
    
    // If first filled cell is within first clue range
    if firstFilled < firstClue:
        // Cells from firstFilled to firstClue-1 must be part of first group
        for i from firstFilled to min(firstFilled + firstClue - 1, line.size()-1):
            if line.get(i).getCurrentState() == UNKNOWN:
                certainCells.add(new CellPosition(row, i))
    
    return certainCells
```

**Example**:
```
Clue: [5, 2]
Current state: □ □ ■ ? ? ? ? ? ? □

Analysis:
- First filled cell at position 2
- First clue is 5
- Cells 2-6 must be part of first group

Deduction: Fill cells 3-6
Result:        □ □ ■ ■ ■ ■ ■ ? ? □
```

### 5.3 Algorithm: Clue Satisfaction

**Input**:
- MyArrayList<Cell> line
- int clueIndex
- int clueValue

**Pseudocode**:
```
function checkClueSatisfied(line, clueIndex, clueValue):
    // Count consecutive filled cells matching this clue
    consecutiveCount = 0
    inGroup = false
    groupIndex = 0
    
    for each cell in line:
        if cell.getCurrentState() == FILLED:
            consecutiveCount++
            inGroup = true
        else:
            if inGroup:
                if groupIndex == clueIndex:
                    // This is the group we're checking
                    if consecutiveCount == clueValue:
                        return true // Clue satisfied
                    else:
                        return false // Mismatch
                groupIndex++
                consecutiveCount = 0
                inGroup = false
    
    // Check last group
    if inGroup and groupIndex == clueIndex:
        return consecutiveCount == clueValue
    
    return false
```

---

## 6. Complete Line Detection

### 6.1 Algorithm: Detect Complete Line

**Input**:
- MyArrayList<Integer> clues
- int lineLength

**Output**: boolean (true if line must be completely filled)

**Logic**:
```
function isCompleteLine(clues, lineLength):
    // Sum all clue numbers
    clueSum = sum of all numbers in clues
    
    // Calculate minimum gaps needed
    numGaps = clues.size() - 1
    minGapsSize = numGaps // At least 1 empty cell between groups
    
    // Total minimum space needed
    minSpace = clueSum + minGapsSize
    
    if minSpace == lineLength:
        return true // Exact fit, only one arrangement
    
    if clueSum == lineLength and clues.size() == 1:
        return true // Single clue filling entire line
    
    return false
```

**Example 1**:
```
Clues: [10]
Line length: 10

clueSum = 10
minGapsSize = 0 (only one clue)
minSpace = 10

minSpace == lineLength → true
All cells must be filled
```

**Example 2**:
```
Clues: [3, 2, 4]
Line length: 11

clueSum = 3 + 2 + 4 = 9
minGapsSize = 2 (two gaps needed)
minSpace = 9 + 2 = 11

minSpace == lineLength → true
Only one arrangement: ■ ■ ■ □ ■ ■ □ ■ ■ ■ ■
```

---

## 7. Hint Generation Algorithm

### 7.1 Master Hint Generator

**Pseudocode**:
```
function generateAllHints(gameBoard):
    hints = new MyQueue<Hint>()
    
    // Priority 1: Complete lines
    for each row:
        if isCompleteLine(row.clues, board.cols):
            hint = new Hint("Row " + row + " must be completely filled", 
                           COMPLETE_LINE, 1)
            hint.setLine(row, true)
            for col from 0 to board.cols:
                hint.addAffectedCell(row, col)
            hints.enqueue(hint)
    
    // Repeat for columns
    
    // Priority 2: Overlaps
    for each row:
        for each clue in row.clues:
            if clue is only clue in row:
                range = findOverlap(clue, board.cols)
                if range != null:
                    hint = new Hint("Row " + row + " has overlap", 
                                   OVERLAP, 2)
                    for col from range.start to range.end:
                        hint.addAffectedCell(row, col)
                    hints.enqueue(hint)
    
    // Priority 3: Edge deductions
    for each row:
        cells = leftEdgeDeduction(row, row.clues)
        if cells not empty:
            hint = new Hint("Row " + row + " edge deduction", 
                           EDGE_DEDUCTION, 3)
            hint.addAffectedCells(cells)
            hints.enqueue(hint)
    
    // Priority 4: Error detection
    for each row:
        if not validateLine(row):
            hint = new Hint("Row " + row + " has errors", ERROR, 5)
            hint.setLine(row, true)
            hints.enqueue(hint)
    
    return hints
```

---

## 8. Simple Boxes Algorithm

### 8.1 Purpose
Detect simple patterns where clues have limited placement options.

### 8.2 Algorithm: Find Simple Boxes

**Input**:
- MyArrayList<Cell> line
- MyArrayList<Integer> clues

**Pseudocode**:
```
function findSimpleBoxes(line, clues):
    certainCells = new MyArrayList<CellPosition>()
    
    // For single small clue
    if clues.size() == 1 and clues.get(0) <= 3:
        clue = clues.get(0)
        
        // Find marked cells (X marks)
        markedPositions = new MyArrayList<Integer>()
        for i from 0 to line.size():
            if line.get(i).getCurrentState() == MARKED:
                markedPositions.add(i)
        
        // If marked cells create small gaps
        for i from 0 to markedPositions.size()-1:
            gapSize = markedPositions.get(i+1) - markedPositions.get(i) - 1
            if gapSize == clue:
                // Gap exactly fits clue, must fill it
                for j from markedPositions.get(i)+1 to markedPositions.get(i+1)-1:
                    certainCells.add(new CellPosition(row, j))
    
    return certainCells
```

**Example**:
```
Clue: [2]
Current state: □ □ X □ □ X □ □ □ □
                   ↑ ↓ ↓ ↑
                   gap of 2

Gap between marks at positions 2 and 5:
- Gap size = 5 - 2 - 1 = 2
- Clue is 2
- Gap exactly fits clue
- Must fill positions 3-4

Result: □ □ X ■ ■ X □ □ □ □
```

---

## 9. Line Solving Algorithm (Advanced)

### 9.1 Purpose
Determine all possible valid arrangements and find common cells.

### 9.2 Algorithm: Generate All Possibilities

**Input**:
- MyArrayList<Integer> clues
- int lineLength

**Output**: MyArrayList<boolean[]> (all valid arrangements)

**Pseudocode**:
```
function generateAllArrangements(clues, length):
    arrangements = new MyArrayList<boolean[]>()
    
    // Calculate minimum space needed
    clueSum = sum of clues
    numGaps = clues.size() - 1
    minSpace = clueSum + numGaps
    
    if minSpace > length:
        return arrangements // Impossible
    
    // Generate recursively
    current = new boolean[length]
    generateRecursive(clues, 0, 0, current, arrangements, length)
    
    return arrangements

function generateRecursive(clues, clueIndex, position, 
                          current, arrangements, length):
    if clueIndex == clues.size():
        // All clues placed, add arrangement
        arrangements.add(copy of current)
        return
    
    clue = clues.get(clueIndex)
    
    // Calculate remaining space needed
    remainingClues = sum of clues from clueIndex to end
    remainingGaps = (clues.size() - clueIndex - 1)
    minRemainingSpace = remainingClues + remainingGaps
    
    // Try placing clue at each valid position
    maxStart = length - minRemainingSpace
    
    for start from position to maxStart:
        // Place clue
        for i from start to start + clue - 1:
            current[i] = true
        
        // Recurse for next clue (skip at least 1 cell for gap)
        generateRecursive(clues, clueIndex + 1, start + clue + 1, 
                         current, arrangements, length)
        
        // Backtrack
        for i from start to start + clue - 1:
            current[i] = false
```

### 9.3 Algorithm: Find Common Cells

```
function findCommonCells(arrangements):
    if arrangements.size() == 0:
        return empty list
    
    length = arrangements.get(0).length
    commonCells = new MyArrayList<CellPosition>()
    
    for i from 0 to length:
        allTrue = true
        allFalse = true
        
        for each arrangement in arrangements:
            if arrangement[i] == false:
                allTrue = false
            if arrangement[i] == true:
                allFalse = false
        
        if allTrue:
            commonCells.add(new CellPosition(row, i, FILLED))
        else if allFalse:
            commonCells.add(new CellPosition(row, i, MARKED))
    
    return commonCells
```

**Example**:
```
Clues: [2, 1]
Length: 6

Possible arrangements:
1. ■ ■ □ ■ □ □
2. ■ ■ □ □ ■ □
3. ■ ■ □ □ □ ■
4. □ ■ ■ □ ■ □
5. □ ■ ■ □ □ ■

Common analysis:
Position 0: T,T,T,F,F → not common
Position 1: T,T,T,T,T → ALWAYS true → certain FILLED
Position 2: F,F,F,T,T → not common
Position 3: T,F,F,F,F → not common
Position 4: F,T,F,T,F → not common
Position 5: F,F,T,F,T → not common

Result: Only position 1 is certain (must be FILLED)
```

---

## 10. Performance Optimization

### 10.1 Caching Strategies

**Cache Validated Lines**:
```java
private MyHashMap<Integer, Boolean> validatedLines;

public boolean isLineComplete(int lineIndex, boolean isRow) {
    Integer key = isRow ? lineIndex : lineIndex + 1000;
    Boolean cached = validatedLines.get(key);
    
    if (cached != null) {
        return cached;
    }
    
    boolean result = validateLine(...);
    validatedLines.put(key, result);
    return result;
}
```

**Cache Hint Generation**:
- Don't regenerate hints if board state unchanged
- Clear cache only when cell state changes

### 10.2 Early Termination

**In Line Validation**:
```java
// Stop checking if error found
for (int i = 0; i < groups.size(); i++) {
    if (groups.get(i) != clues.get(i)) {
        return false; // Early exit
    }
}
```

**In Possibility Generation**:
```java
// Prune impossible branches early
if (position + minRemainingSpace > length) {
    return; // Can't fit remaining clues
}
```

---

## 11. Algorithm Complexity Analysis

### 11.1 Time Complexity

| Algorithm | Best Case | Average Case | Worst Case |
|-----------|-----------|--------------|------------|
| Generate Clues | O(n) | O(n) | O(n) |
| Validate Line | O(n) | O(n) | O(n) |
| Find Overlap | O(1) | O(1) | O(1) |
| Edge Deduction | O(n) | O(n) | O(n) |
| Generate Possibilities | O(2^k) | O(2^k) | O(2^k) |

Where:
- n = line length
- k = number of clues

### 11.2 Space Complexity

| Algorithm | Space Used |
|-----------|------------|
| Generate Clues | O(k) per line |
| Validate Line | O(k) for groups |
| Find Overlap | O(1) |
| Generate Possibilities | O(2^k × n) |

---

## 12. Summary

### 12.1 Core Algorithms

1. **Clue Generation**: Convert solution → clues
2. **Line Validation**: Check if player state matches clues
3. **Overlap Detection**: Find guaranteed filled cells
4. **Edge Deduction**: Determine cells from constraints
5. **Complete Line Detection**: Identify fully determinable lines
6. **Hint Generation**: Produce helpful solving suggestions
7. **Possibility Generation**: Calculate all valid arrangements

### 12.2 Algorithm Selection

- **Simple puzzles**: Use overlap and edge deduction
- **Complex puzzles**: May need full possibility generation
- **Hints**: Start with simple algorithms, escalate if needed
- **Validation**: Always use line validation (fast and accurate)

---

## Document Revision History
- Version 1.0 - Initial specification
- Date: 2025-11-15
