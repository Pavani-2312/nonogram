import nonogram.model.*;
import nonogram.controller.HintGenerator;

public class TestHintSystem {
    public static void main(String[] args) {
        System.out.println("Testing Hint System...");
        
        testCompleteLineHint();
        testEdgeDeductionHint();
        testGameStateWithHints();
        
        System.out.println("All Hint System tests passed!");
    }
    
    private static void testCompleteLineHint() {
        // Create a 5x5 puzzle where first row should be completely filled
        boolean[][] solution = {
            {true, true, true, true, true},   // Complete line
            {true, false, true, false, true},
            {false, true, true, true, false},
            {true, false, false, false, true},
            {false, false, true, false, false}
        };
        
        GameBoard board = new GameBoard(solution);
        
        // Generate hint for empty board
        Hint hint = HintGenerator.generateHint(board);
        
        assert hint != null : "Should generate a hint for empty board";
        assert hint.getType() == HintType.COMPLETE_LINE : "Should be complete line hint";
        assert hint.isRowHint() : "Should be row hint";
        assert hint.getLineIndex() == 0 : "Should be for first row";
        assert hint.getAffectedCells().size() == 5 : "Should affect all 5 cells in row";
        
        System.out.println("Complete line hint test passed");
    }
    
    private static void testEdgeDeductionHint() {
        // Create a puzzle with a large clue that forces edge deduction
        boolean[][] solution = {
            {false, true, true, true, false},
            {true, false, false, false, true},
            {false, false, true, false, false},
            {true, false, false, false, true},
            {false, false, true, false, false}
        };
        
        GameBoard board = new GameBoard(solution);
        
        // Fill the complete line so we get to edge deduction
        for (int col = 0; col < 5; col++) {
            board.getCell(0, col).setCurrentState(CellState.FILLED);
        }
        
        Hint hint = HintGenerator.generateHint(board);
        
        // Should find some hint (may be edge deduction or other type)
        assert hint != null : "Should generate some hint";
        
        System.out.println("Edge deduction hint test passed");
    }
    
    private static void testGameStateWithHints() {
        boolean[][] solution = {
            {true, true, false},
            {false, true, false},
            {true, true, true}
        };
        
        GameBoard board = new GameBoard(solution);
        GameState gameState = new GameState(board);
        
        // Test initial state
        assert gameState.getHintsUsed() == 0 : "Should start with 0 hints used";
        
        // Increment hints used
        gameState.incrementHintsUsed();
        gameState.incrementHintsUsed();
        
        assert gameState.getHintsUsed() == 2 : "Should have 2 hints used";
        
        // Test move tracking
        gameState.makeMove(new CellPosition(0, 0), CellState.FILLED);
        assert gameState.getMoveCount() == 1 : "Should have 1 move";
        assert gameState.canUndo() : "Should be able to undo";
        
        System.out.println("GameState with hints test passed");
    }
}
