import nonogram.model.*;
import nonogram.datastructures.MyArrayList;

public class TestModelEnhancements {
    public static void main(String[] args) {
        System.out.println("Testing Model Enhancements...");
        
        testCellState();
        testDifficulty();
        testHintType();
        testCellPosition();
        testMove();
        testHint();
        
        System.out.println("All Model Enhancement tests passed!");
    }
    
    private static void testCellState() {
        // Test display symbols
        assert CellState.UNKNOWN.getDisplaySymbol() == ' ' : "Unknown should display as space";
        assert CellState.FILLED.getDisplaySymbol() == '■' : "Filled should display as filled block";
        assert CellState.MARKED.getDisplaySymbol() == 'X' : "Marked should display as X";
        
        // Test state transitions
        assert CellState.UNKNOWN.getNextState() == CellState.FILLED : "Unknown -> Filled";
        assert CellState.FILLED.getNextState() == CellState.MARKED : "Filled -> Marked";
        assert CellState.MARKED.getNextState() == CellState.UNKNOWN : "Marked -> Unknown";
        
        // Test isAnswered
        assert !CellState.UNKNOWN.isAnswered() : "Unknown should not be answered";
        assert CellState.FILLED.isAnswered() : "Filled should be answered";
        assert CellState.MARKED.isAnswered() : "Marked should be answered";
        
        System.out.println("CellState test passed");
    }
    
    private static void testDifficulty() {
        // Test difficulty properties
        assert Difficulty.EASY.getRows() == 5 : "Easy should have 5 rows";
        assert Difficulty.EASY.getCols() == 5 : "Easy should have 5 cols";
        assert Difficulty.EASY.getTotalCells() == 25 : "Easy should have 25 total cells";
        assert "Easy".equals(Difficulty.EASY.getDisplayName()) : "Easy display name should be 'Easy'";
        
        assert Difficulty.EXPERT.getRows() == 20 : "Expert should have 20 rows";
        assert Difficulty.EXPERT.getCols() == 20 : "Expert should have 20 cols";
        assert Difficulty.EXPERT.getTotalCells() == 400 : "Expert should have 400 total cells";
        
        System.out.println("Difficulty test passed");
    }
    
    private static void testHintType() {
        HintType type = HintType.COMPLETE_LINE;
        assert "Complete Line".equals(type.getName()) : "Complete line name should match";
        assert type.getDescription().contains("completely filled") : "Description should mention completely filled";
        
        System.out.println("HintType test passed");
    }
    
    private static void testCellPosition() {
        CellPosition pos1 = new CellPosition(2, 3);
        CellPosition pos2 = new CellPosition(2, 3);
        CellPosition pos3 = new CellPosition(1, 3);
        
        // Test getters
        assert pos1.getRow() == 2 : "Row should be 2";
        assert pos1.getCol() == 3 : "Col should be 3";
        
        // Test equals
        assert pos1.equals(pos2) : "Same positions should be equal";
        assert !pos1.equals(pos3) : "Different positions should not be equal";
        
        // Test hashCode consistency
        assert pos1.hashCode() == pos2.hashCode() : "Equal objects should have same hash code";
        
        // Test validity
        assert pos1.isValid(5, 5) : "Position (2,3) should be valid in 5x5 grid";
        assert !pos1.isValid(2, 2) : "Position (2,3) should not be valid in 2x2 grid";
        
        System.out.println("CellPosition test passed");
    }
    
    private static void testMove() {
        CellPosition pos = new CellPosition(1, 2);
        Move move = new Move(pos, CellState.UNKNOWN, CellState.FILLED);
        
        // Test getters
        assert move.getPosition().equals(pos) : "Position should match";
        assert move.getOldState() == CellState.UNKNOWN : "Old state should be UNKNOWN";
        assert move.getNewState() == CellState.FILLED : "New state should be FILLED";
        assert move.getTimestamp() > 0 : "Timestamp should be positive";
        
        // Test reverse
        Move reverse = move.getReverse();
        assert reverse.getOldState() == CellState.FILLED : "Reverse old state should be FILLED";
        assert reverse.getNewState() == CellState.UNKNOWN : "Reverse new state should be UNKNOWN";
        
        System.out.println("Move test passed");
    }
    
    private static void testHint() {
        Hint hint = new Hint(HintType.COMPLETE_LINE, "Fill entire row", 2, true);
        
        // Test basic properties
        assert hint.getType() == HintType.COMPLETE_LINE : "Type should match";
        assert "Fill entire row".equals(hint.getMessage()) : "Message should match";
        assert hint.getLineIndex() == 2 : "Line index should be 2";
        assert hint.isRowHint() : "Should be row hint";
        
        // Test affected cells
        CellPosition pos = new CellPosition(2, 1);
        hint.addAffectedCell(pos);
        assert hint.getAffectedCells().size() == 1 : "Should have 1 affected cell";
        assert hint.getAffectedCells().get(0).equals(pos) : "Affected cell should match";
        
        // Test full message
        String fullMessage = hint.getFullMessage();
        assert fullMessage.contains("Row 3") : "Full message should contain 'Row 3'";
        assert fullMessage.contains("Complete Line") : "Full message should contain hint type";
        
        System.out.println("Hint test passed");
    }
}
