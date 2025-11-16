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
