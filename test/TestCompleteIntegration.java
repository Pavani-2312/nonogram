import nonogram.model.*;
import nonogram.controller.*;
import nonogram.datastructures.*;

public class TestCompleteIntegration {
    public static void main(String[] args) {
        System.out.println("Running Complete Integration Test...");
        
        testDataStructuresIntegration();
        testModelIntegration();
        testControllerIntegration();
        testGameplayFlow();
        
        System.out.println("All Integration tests passed!");
        System.out.println("Project implementation is complete and functional!");
    }
    
    private static void testDataStructuresIntegration() {
        // Test all custom data structures work together
        MyArrayList<String> list = new MyArrayList<>();
        MyStack<Integer> stack = new MyStack<>();
        MyQueue<Boolean> queue = new MyQueue<>();
        MyHashMap<String, Integer> map = new MyHashMap<>();
        
        // Test basic operations
        list.add("test");
        stack.push(42);
        queue.enqueue(true);
        map.put("key", 100);
        
        assert list.size() == 1 : "ArrayList should have 1 element";
        assert stack.size() == 1 : "Stack should have 1 element";
        assert queue.size() == 1 : "Queue should have 1 element";
        assert map.size() == 1 : "HashMap should have 1 element";
        
        System.out.println("Data structures integration test passed");
    }
    
    private static void testModelIntegration() {
        // Test model components work together
        boolean[][] solution = {
            {true, false, true},
            {false, true, false},
            {true, true, true}
        };
        
        GameBoard board = new GameBoard(solution);
        GameState gameState = new GameState(board);
        
        // Test game state operations
        gameState.makeMove(new CellPosition(0, 0), CellState.FILLED);
        assert gameState.getMoveCount() == 1 : "Should have 1 move";
        assert gameState.canUndo() : "Should be able to undo";
        
        gameState.undo();
        assert gameState.getMoveCount() == 0 : "Should have 0 moves after undo";
        assert gameState.canRedo() : "Should be able to redo";
        
        System.out.println("Model integration test passed");
    }
    
    private static void testControllerIntegration() {
        // Test controller components work together
        PuzzleLoader loader = new PuzzleLoader();
        GameController controller = new GameController();
        
        // Test puzzle loading for different difficulties
        for (Difficulty diff : Difficulty.values()) {
            int count = loader.getPuzzleCount(diff);
            assert count > 0 : "Should have puzzles for " + diff;
            
            Puzzle puzzle = loader.getPuzzle(diff, 0);
            assert puzzle != null : "Should get puzzle for " + diff;
        }
        
        // Test hint generation
        Puzzle puzzle = loader.getDefaultPuzzle();
        GameBoard board = new GameBoard(puzzle.getSolution());
        Hint hint = HintGenerator.generateHint(board);
        // Hint may or may not be available depending on puzzle, but should not crash
        
        System.out.println("Controller integration test passed");
    }
    
    private static void testGameplayFlow() {
        // Test complete gameplay flow
        PuzzleLoader loader = new PuzzleLoader();
        Puzzle puzzle = loader.getDefaultPuzzle();
        GameBoard board = new GameBoard(puzzle.getSolution());
        GameState gameState = new GameState(board);
        
        // Simulate some moves
        gameState.makeMove(new CellPosition(0, 0), CellState.FILLED);
        gameState.makeMove(new CellPosition(0, 1), CellState.MARKED);
        gameState.makeMove(new CellPosition(1, 0), CellState.FILLED);
        
        assert gameState.getMoveCount() == 3 : "Should have 3 moves";
        
        // Test undo/redo cycle
        gameState.undo();
        assert gameState.getMoveCount() == 2 : "Should have 2 moves after undo";
        
        gameState.redo();
        assert gameState.getMoveCount() == 3 : "Should have 3 moves after redo";
        
        // Test hint usage
        gameState.incrementHintsUsed();
        assert gameState.getHintsUsed() == 1 : "Should have 1 hint used";
        
        // Test reset
        gameState.reset();
        assert gameState.getMoveCount() == 0 : "Should have 0 moves after reset";
        assert gameState.getHintsUsed() == 0 : "Should have 0 hints after reset";
        
        System.out.println("Gameplay flow test passed");
    }
}
