package nonogram.controller;

import nonogram.model.*;
import nonogram.datastructures.MyLinkedList;
import nonogram.datastructures.MyArrayList;

public class HintGenerator {
    
    public static Hint generateHint(GameBoard board) {
        // Try different hint strategies in order of priority
        
        // Priority 1: Complete line hints
        Hint completeLineHint = findCompleteLineHint(board);
        if (completeLineHint != null) {
            return completeLineHint;
        }
        
        // Priority 2: Edge deduction hints
        Hint edgeHint = findEdgeDeductionHint(board);
        if (edgeHint != null) {
            return edgeHint;
        }
        
        // Priority 3: Simple overlap hints
        Hint overlapHint = findOverlapHint(board);
        if (overlapHint != null) {
            return overlapHint;
        }
        
        // No hints available
        return null;
    }
    
    private static Hint findCompleteLineHint(GameBoard board) {
        // Check rows
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            if (isCompleteLinePattern(clues, board.getCols())) {
                // Check if row is not already filled
                boolean needsFilling = false;
                for (int col = 0; col < board.getCols(); col++) {
                    if (board.getCell(row, col).getCurrentState() == CellState.UNKNOWN) {
                        needsFilling = true;
                        break;
                    }
                }
                
                if (needsFilling) {
                    Hint hint = new Hint(HintType.COMPLETE_LINE, 
                                       "This entire row should be filled based on the clue.", 
                                       row, true);
                    
                    // Add all cells in the row as affected
                    for (int col = 0; col < board.getCols(); col++) {
                        hint.addAffectedCell(new CellPosition(row, col));
                    }
                    return hint;
                }
            }
        }
        
        // Check columns
        for (int col = 0; col < board.getCols(); col++) {
            MyLinkedList<Integer> clues = board.getColumnClues(col);
            if (isCompleteLinePattern(clues, board.getRows())) {
                // Check if column is not already filled
                boolean needsFilling = false;
                for (int row = 0; row < board.getRows(); row++) {
                    if (board.getCell(row, col).getCurrentState() == CellState.UNKNOWN) {
                        needsFilling = true;
                        break;
                    }
                }
                
                if (needsFilling) {
                    Hint hint = new Hint(HintType.COMPLETE_LINE, 
                                       "This entire column should be filled based on the clue.", 
                                       col, false);
                    
                    // Add all cells in the column as affected
                    for (int row = 0; row < board.getRows(); row++) {
                        hint.addAffectedCell(new CellPosition(row, col));
                    }
                    return hint;
                }
            }
        }
        
        return null;
    }
    
    private static Hint findEdgeDeductionHint(GameBoard board) {
        // Check rows for edge deductions
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            
            // Simple case: if first clue is large enough to force edge cells
            if (clues.size() > 0) {
                int firstClue = clues.get(0);
                int lineLength = board.getCols();
                
                // If clue is more than half the line length, some edge cells must be filled
                if (firstClue > lineLength / 2) {
                    // Check if leftmost forced cells are still unknown
                    int forcedStart = lineLength - firstClue;
                    for (int col = forcedStart; col < firstClue; col++) {
                        if (board.getCell(row, col).getCurrentState() == CellState.UNKNOWN) {
                            Hint hint = new Hint(HintType.EDGE_DEDUCTION,
                                               "The clue " + firstClue + " forces some cells to be filled.",
                                               row, true);
                            hint.addAffectedCell(new CellPosition(row, col));
                            return hint;
                        }
                    }
                }
            }
        }
        
        // Check columns for edge deductions
        for (int col = 0; col < board.getCols(); col++) {
            MyLinkedList<Integer> clues = board.getColumnClues(col);
            
            if (clues.size() > 0) {
                int firstClue = clues.get(0);
                int lineLength = board.getRows();
                
                if (firstClue > lineLength / 2) {
                    int forcedStart = lineLength - firstClue;
                    for (int row = forcedStart; row < firstClue; row++) {
                        if (board.getCell(row, col).getCurrentState() == CellState.UNKNOWN) {
                            Hint hint = new Hint(HintType.EDGE_DEDUCTION,
                                               "The clue " + firstClue + " forces some cells to be filled.",
                                               col, false);
                            hint.addAffectedCell(new CellPosition(row, col));
                            return hint;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    private static Hint findOverlapHint(GameBoard board) {
        // Simple overlap detection - if all possible arrangements of a clue
        // result in the same cell being filled, that cell must be filled
        
        // This is a simplified version - a full implementation would be more complex
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            
            // For single clue case
            if (clues.size() == 1 && clues.get(0) > 0) {
                int clueValue = clues.get(0);
                int lineLength = board.getCols();
                
                // Calculate overlap region
                int maxStart = lineLength - clueValue;
                if (maxStart < clueValue) {
                    // There's overlap - find first unknown cell in overlap region
                    for (int col = maxStart; col < clueValue; col++) {
                        if (board.getCell(row, col).getCurrentState() == CellState.UNKNOWN) {
                            Hint hint = new Hint(HintType.OVERLAP_ANALYSIS,
                                               "All possible arrangements of clue " + clueValue + " overlap here.",
                                               row, true);
                            hint.addAffectedCell(new CellPosition(row, col));
                            return hint;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    private static boolean isCompleteLinePattern(MyLinkedList<Integer> clues, int lineLength) {
        if (clues.size() == 1) {
            return clues.get(0) == lineLength;
        }
        return false;
    }
}
