# Nonogram Game - View Layer Architecture
## Implementation Specification

---

## 1. Overview

This document specifies the View layer of the Nonogram application following the Model-View-Controller (MVC) architectural pattern. The View layer handles all user interface components and visual representation while maintaining separation from business logic.

---

## 2. View Layer Principles

### 2.1 Design Constraints
- **Swing Framework**: Uses Java Swing for GUI components
- **MVC Separation**: No direct model manipulation from view
- **Event Delegation**: All user actions passed to controller
- **Basic Appearance**: Functional UI without complex styling

### 2.2 Layer Responsibilities
- Display game grid and clues
- Handle user input events
- Provide visual feedback
- Update display based on model changes

---

## 3. Core View Components

### 3.1 Component Overview

| Component | Purpose | Implementation Priority |
|-----------|---------|------------------------|
| MainFrame | Application window | Phase 1 - Essential |
| GamePanel | Main game display | Phase 1 - Essential |
| GridPanel | Cell grid display | Phase 1 - Essential |
| CluePanel | Row/column clues | Phase 1 - Essential |
| ButtonPanel | Game controls | Phase 2 - Enhancement |

---

## 4. MainFrame Class

### 4.1 Purpose
Primary application window that contains all other UI components.

### 4.2 Basic Structure

```java
public class MainFrame extends JFrame {
    private GamePanel gamePanel;
    private JMenuBar menuBar;  // Phase 2
    
    public MainFrame() {
        setTitle("Nonogram Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initializeComponents();
        pack();
        setLocationRelativeTo(null);
    }
}
```

### 4.3 Essential Methods

```java
private void initializeComponents()  // Setup UI layout
public void setController(GameController controller)  // Connect to controller
public void updateDisplay()  // Refresh entire display
```

---

## 5. GamePanel Class

### 5.1 Purpose
Main container that organizes the game layout with grid and clues.

### 5.2 Layout Structure

```
┌─────────────────────────────────┐
│          Game Panel             │
│  ┌─────────┬─────────────────┐  │
│  │ Row     │                 │  │
│  │ Clues   │    Grid Panel   │  │
│  │         │                 │  │
│  └─────────┼─────────────────┤  │
│            │   Column Clues  │  │
│            └─────────────────┘  │
│  ┌─────────────────────────────┐ │
│  │      Button Panel           │ │ Phase 2
│  └─────────────────────────────┘ │
└─────────────────────────────────┘
```

### 5.3 Implementation

```java
public class GamePanel extends JPanel {
    private GridPanel gridPanel;
    private CluePanel rowCluePanel;
    private CluePanel columnCluePanel;
    private ButtonPanel buttonPanel;  // Phase 2
    
    public GamePanel(GameBoard board) {
        setLayout(new BorderLayout());
        initializePanels(board);
        layoutComponents();
    }
}
```

---

## 6. GridPanel Class

### 6.1 Purpose
Displays the interactive cell grid where players make moves.

### 6.2 Basic Structure

```java
public class GridPanel extends JPanel {
    private GameBoard board;
    private GameController controller;
    private int cellSize = 30;  // Pixels per cell
    private Color[] cellColors = {
        Color.WHITE,      // UNKNOWN
        Color.BLACK,      // FILLED  
        Color.LIGHT_GRAY  // MARKED
    };
}
```

### 6.3 Essential Methods

**Display Methods**:
```java
protected void paintComponent(Graphics g)  // Draw the grid
private void drawGrid(Graphics g)  // Draw grid lines
private void drawCells(Graphics g)  // Draw cell contents
private void drawCell(Graphics g, int row, int col)  // Draw single cell
```

**Input Handling**:
```java
private void addMouseListener()  // Setup click detection
private void handleCellClick(int row, int col)  // Process cell clicks
private Point getCellFromPixel(int x, int y)  // Convert mouse to grid coords
```

### 6.4 Cell Rendering

**Cell States Visual Mapping**:
```java
private void renderCell(Graphics g, Cell cell, int x, int y) {
    switch (cell.getCurrentState()) {
        case UNKNOWN:
            g.setColor(Color.WHITE);
            g.fillRect(x, y, cellSize, cellSize);
            break;
        case FILLED:
            g.setColor(Color.BLACK);
            g.fillRect(x, y, cellSize, cellSize);
            break;
        case MARKED:
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, y, cellSize, cellSize);
            g.setColor(Color.RED);
            drawX(g, x, y);  // Draw X mark
            break;
    }
}
```

---

## 7. CluePanel Class

### 7.1 Purpose
Displays numerical clues for rows and columns.

### 7.2 Basic Structure

```java
public class CluePanel extends JPanel {
    private MyArrayList<MyArrayList<Integer>> clues;
    private boolean isRowClues;  // true for rows, false for columns
    private int cellSize = 30;
    
    public CluePanel(MyArrayList<MyArrayList<Integer>> clues, boolean isRowClues) {
        this.clues = clues;
        this.isRowClues = isRowClues;
        setPreferredSize(calculateSize());
    }
}
```

### 7.3 Essential Methods

```java
protected void paintComponent(Graphics g)  // Draw clues
private void drawClueNumbers(Graphics g)  // Render clue text
private Dimension calculateSize()  // Determine panel size
public void updateClues(MyArrayList<MyArrayList<Integer>> newClues)  // Refresh display
```

### 7.4 Clue Rendering

**Row Clues Layout**:
```
Row 0: [2, 1]  →  "2 1"
Row 1: [3]     →  "3"
Row 2: [1, 1]  →  "1 1"
```

**Column Clues Layout**:
```
Col 0: [2]     Col 1: [1, 2]
  "2"            "1"
                 "2"
```

---

## 8. ButtonPanel Class (Phase 2)

### 8.1 Purpose
Provides game control buttons for enhanced functionality.

### 8.2 Basic Structure

```java
public class ButtonPanel extends JPanel {
    private JButton hintButton;
    private JButton undoButton;
    private JButton resetButton;
    private JButton newGameButton;
    
    public ButtonPanel() {
        setLayout(new FlowLayout());
        initializeButtons();
        addButtons();
    }
}
```

### 8.3 Button Configuration

```java
private void initializeButtons() {
    hintButton = new JButton("Hint");
    undoButton = new JButton("Undo");
    resetButton = new JButton("Reset");
    newGameButton = new JButton("New Game");
    
    // Add action listeners that delegate to controller
    hintButton.addActionListener(e -> controller.requestHint());
    undoButton.addActionListener(e -> controller.undoMove());
    resetButton.addActionListener(e -> controller.resetPuzzle());
    newGameButton.addActionListener(e -> controller.startNewGame());
}
```

---

## 9. Event Handling

### 9.1 Mouse Events

**Cell Click Processing**:
```java
public class GridPanel extends JPanel implements MouseListener {
    public void mouseClicked(MouseEvent e) {
        Point cellCoords = getCellFromPixel(e.getX(), e.getY());
        if (cellCoords != null) {
            controller.handleCellClick(cellCoords.x, cellCoords.y);
        }
    }
    
    // Convert pixel coordinates to grid coordinates
    private Point getCellFromPixel(int x, int y) {
        int row = y / cellSize;
        int col = x / cellSize;
        
        if (row >= 0 && row < board.getRows() && 
            col >= 0 && col < board.getCols()) {
            return new Point(row, col);
        }
        return null;
    }
}
```

### 9.2 Controller Integration

**Event Delegation Pattern**:
```java
// View never modifies model directly
// All actions go through controller

gridPanel.setController(controller);
buttonPanel.setController(controller);

// Controller updates model, then tells view to refresh
controller.addView(this);  // Observer pattern
```

---

## 10. Visual Feedback

### 10.1 Basic Feedback (Phase 1)

**Cell State Changes**:
- Immediate visual update when cell clicked
- Clear distinction between UNKNOWN/FILLED/MARKED states

**Grid Lines**:
- Clear borders between cells
- Consistent spacing and alignment

### 10.2 Enhanced Feedback (Phase 2)

**Completion Indicators**:
```java
private void highlightCompletedLines(Graphics g) {
    MyArrayList<Integer> completedRows = board.getCompletedRows();
    for (int i = 0; i < completedRows.size(); i++) {
        int row = completedRows.get(i);
        highlightRowClue(g, row, Color.GREEN);
    }
}
```

**Error Highlighting**:
```java
private void showErrors(Graphics g) {
    MyArrayList<Cell> errorCells = board.getIncorrectCells();
    for (int i = 0; i < errorCells.size(); i++) {
        Cell cell = errorCells.get(i);
        highlightCell(g, cell.getRow(), cell.getCol(), Color.RED);
    }
}
```

---

## 11. Layout Management

### 11.1 Basic Layout

**BorderLayout Structure**:
```java
gamePanel.setLayout(new BorderLayout());
gamePanel.add(rowCluePanel, BorderLayout.WEST);
gamePanel.add(gridPanel, BorderLayout.CENTER);
gamePanel.add(columnCluePanel, BorderLayout.SOUTH);
gamePanel.add(buttonPanel, BorderLayout.NORTH);  // Phase 2
```

### 11.2 Size Calculations

**Dynamic Sizing**:
```java
private Dimension calculateGridSize() {
    int width = board.getCols() * cellSize;
    int height = board.getRows() * cellSize;
    return new Dimension(width, height);
}

private Dimension calculateClueSize() {
    // Calculate based on maximum clue length and font metrics
    FontMetrics fm = getFontMetrics(getFont());
    int maxClueWidth = calculateMaxClueWidth(fm);
    return new Dimension(maxClueWidth, board.getRows() * cellSize);
}
```

---

## 12. Implementation Priority

### 12.1 Phase 1 - Essential Components
1. **MainFrame** - Basic window setup
2. **GamePanel** - Layout container
3. **GridPanel** - Interactive cell grid
4. **CluePanel** - Display clues
5. **Mouse handling** - Cell click detection

### 12.2 Phase 2 - Enhanced Features
1. **ButtonPanel** - Game controls
2. **Visual feedback** - Completion highlighting
3. **Error display** - Show incorrect cells
4. **Menu system** - File operations

---

## Document Revision History
- Version 1.0 - Initial specification
- Date: 2025-11-15
