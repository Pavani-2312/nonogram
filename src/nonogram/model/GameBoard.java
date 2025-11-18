package nonogram.model;

import nonogram.datastructures.MyLinkedList;

public class GameBoard {
    private int rows;
    private int cols;
    private Cell[][] cells;
    private MyLinkedList<MyLinkedList<Integer>> rowClues;
    private MyLinkedList<MyLinkedList<Integer>> columnClues;
    
    public GameBoard(boolean[][] solution) {
        this.rows = solution.length;
        this.cols = solution[0].length;
        this.cells = new Cell[rows][cols];
        this.rowClues = new MyLinkedList<>();
        this.columnClues = new MyLinkedList<>();
        
        // Initialize cells
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col, solution[row][col]);
            }
        }
        
        // Generate clues
        generateCluesFromSolution(solution);
    }
    
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid cell position");
        }
        return cells[row][col];
    }
    
    public MyLinkedList<Integer> getRowClues(int rowIndex) {
        return rowClues.get(rowIndex);
    }
    
    public MyLinkedList<Integer> getColumnClues(int colIndex) {
        return columnClues.get(colIndex);
    }
    
    public boolean isPuzzleComplete() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isCorrect()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isSolved() {
        return isPuzzleComplete();
    }
    
    public void reset() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col].reset();
            }
        }
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public void autoFillMarks() {
        for (int row = 0; row < rows; row++) {
            autoFillRowMarks(row);
        }
        for (int col = 0; col < cols; col++) {
            autoFillColumnMarks(col);
        }
        
        // Auto-fill remaining cells if all blacks are correctly filled
        autoFillRemainingCells();
    }
    
    private void autoFillRemainingCells() {
        // Check if all black cells are correctly filled
        boolean allBlacksFilled = true;
        for (int row = 0; row < rows && allBlacksFilled; row++) {
            for (int col = 0; col < cols && allBlacksFilled; col++) {
                Cell cell = cells[row][col];
                if (cell.getActualValue() && cell.getCurrentState() != CellState.FILLED) {
                    allBlacksFilled = false;
                }
            }
        }
        
        // If all blacks are filled, mark remaining unknowns as MARKED
        if (allBlacksFilled) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Cell cell = cells[row][col];
                    if (cell.getCurrentState() == CellState.UNKNOWN) {
                        cell.setState(CellState.MARKED);
                    }
                }
            }
        }
    }
    
    private void autoFillRowMarks(int row) {
        MyLinkedList<Integer> clues = rowClues.get(row);
        if (isRowCluesSatisfied(row, clues)) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].getCurrentState() == CellState.UNKNOWN) {
                    cells[row][col].setState(CellState.MARKED);
                }
            }
        }
    }
    
    private void autoFillColumnMarks(int col) {
        MyLinkedList<Integer> clues = columnClues.get(col);
        if (isColumnCluesSatisfied(col, clues)) {
            for (int row = 0; row < rows; row++) {
                if (cells[row][col].getCurrentState() == CellState.UNKNOWN) {
                    cells[row][col].setState(CellState.MARKED);
                }
            }
        }
    }
    
    public boolean isRowCluesSatisfied(int row, MyLinkedList<Integer> clues) {
        // Check if filled cells match the solution pattern
        for (int col = 0; col < cols; col++) {
            if (cells[row][col].getCurrentState() == CellState.FILLED) {
                if (!cells[row][col].getActualValue()) {
                    return false; // Filled cell doesn't match solution
                }
            }
        }
        
        // Check if current filled pattern matches clues
        MyLinkedList<Integer> currentClues = new MyLinkedList<>();
        int count = 0;
        
        for (int col = 0; col < cols; col++) {
            if (cells[row][col].getCurrentState() == CellState.FILLED) {
                count++;
            } else {
                if (count > 0) {
                    currentClues.add(count);
                    count = 0;
                }
            }
        }
        if (count > 0) {
            currentClues.add(count);
        }
        
        return cluesMatch(currentClues, clues);
    }
    
    public boolean isColumnCluesSatisfied(int col, MyLinkedList<Integer> clues) {
        // Check if filled cells match the solution pattern
        for (int row = 0; row < rows; row++) {
            if (cells[row][col].getCurrentState() == CellState.FILLED) {
                if (!cells[row][col].getActualValue()) {
                    return false; // Filled cell doesn't match solution
                }
            }
        }
        
        // Check if current filled pattern matches clues
        MyLinkedList<Integer> currentClues = new MyLinkedList<>();
        int count = 0;
        
        for (int row = 0; row < rows; row++) {
            if (cells[row][col].getCurrentState() == CellState.FILLED) {
                count++;
            } else {
                if (count > 0) {
                    currentClues.add(count);
                    count = 0;
                }
            }
        }
        if (count > 0) {
            currentClues.add(count);
        }
        
        return cluesMatch(currentClues, clues);
    }
    
    private boolean cluesMatch(MyLinkedList<Integer> current, MyLinkedList<Integer> target) {
        if (current.size() != target.size()) {
            return false;
        }
        for (int i = 0; i < current.size(); i++) {
            if (!current.get(i).equals(target.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private void generateCluesFromSolution(boolean[][] solution) {
        // Generate row clues
        for (int row = 0; row < rows; row++) {
            MyLinkedList<Integer> clues = new MyLinkedList<>();
            int count = 0;
            
            for (int col = 0; col < cols; col++) {
                if (solution[row][col]) {
                    count++;
                } else {
                    if (count > 0) {
                        clues.add(count);
                        count = 0;
                    }
                }
            }
            if (count > 0) {
                clues.add(count);
            }
            if (clues.isEmpty()) {
                clues.add(0);
            }
            rowClues.add(clues);
        }
        
        // Generate column clues
        for (int col = 0; col < cols; col++) {
            MyLinkedList<Integer> clues = new MyLinkedList<>();
            int count = 0;
            
            for (int row = 0; row < rows; row++) {
                if (solution[row][col]) {
                    count++;
                } else {
                    if (count > 0) {
                        clues.add(count);
                        count = 0;
                    }
                }
            }
            if (count > 0) {
                clues.add(count);
            }
            if (clues.isEmpty()) {
                clues.add(0);
            }
            columnClues.add(clues);
        }
    }
}
