package nonogram.controller;
import nonogram.model.*;
import nonogram.datastructures.MyLinkedList;
import nonogram.datastructures.MyArrayList;
public class HintGenerator {
    public static Hint generateHint(GameBoard board) {
        Hint completeLineHint = findCompleteLineHint(board);
        if (completeLineHint != null) {
            return completeLineHint;
        }
        Hint edgeHint = findEdgeDeductionHint(board);
        if (edgeHint != null) {
            return edgeHint;
        }
        Hint overlapHint = findOverlapHint(board);
        if (overlapHint != null) {
            return overlapHint;
        }
        return null;
    }
    private static Hint findCompleteLineHint(GameBoard board) {
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            if (isCompleteLinePattern(clues, board.getCols())) {
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
                    for (int col = 0; col < board.getCols(); col++) {
                        hint.addAffectedCell(new CellPosition(row, col));
                    }
                    return hint;
                }
            }
        }
        for (int col = 0; col < board.getCols(); col++) {
            MyLinkedList<Integer> clues = board.getColumnClues(col);
            if (isCompleteLinePattern(clues, board.getRows())) {
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
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            if (clues.size() > 0) {
                int firstClue = clues.get(0);
                int lineLength = board.getCols();
                if (firstClue > lineLength / 2) {
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
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            if (clues.size() == 1 && clues.get(0) > 0) {
                int clueValue = clues.get(0);
                int lineLength = board.getCols();
                int maxStart = lineLength - clueValue;
                if (maxStart < clueValue) {
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
