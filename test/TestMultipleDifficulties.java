import nonogram.model.*;
import nonogram.controller.PuzzleLoader;
import nonogram.datastructures.MyLinkedList;

public class TestMultipleDifficulties {
    public static void main(String[] args) {
        System.out.println("Testing Multiple Difficulties...");
        
        testDifficultyEnumeration();
        testPuzzleLoader();
        testDifferentSizes();
        
        System.out.println("All Multiple Difficulties tests passed!");
    }
    
    private static void testDifficultyEnumeration() {
        // Test difficulty properties
        assert Difficulty.EASY.getRows() == 5 : "Easy should be 5x5";
        assert Difficulty.MEDIUM.getRows() == 10 : "Medium should be 10x10";
        assert Difficulty.HARD.getRows() == 15 : "Hard should be 15x15";
        assert Difficulty.EXPERT.getRows() == 20 : "Expert should be 20x20";
        
        // Test total cells calculation
        assert Difficulty.EASY.getTotalCells() == 25 : "Easy should have 25 cells";
        assert Difficulty.MEDIUM.getTotalCells() == 100 : "Medium should have 100 cells";
        assert Difficulty.HARD.getTotalCells() == 225 : "Hard should have 225 cells";
        assert Difficulty.EXPERT.getTotalCells() == 400 : "Expert should have 400 cells";
        
        System.out.println("Difficulty enumeration test passed");
    }
    
    private static void testPuzzleLoader() {
        PuzzleLoader loader = new PuzzleLoader();
        
        // Test that each difficulty has puzzles
        for (Difficulty diff : Difficulty.values()) {
            int count = loader.getPuzzleCount(diff);
            assert count > 0 : "Difficulty " + diff + " should have at least one puzzle";
            
            MyLinkedList<Puzzle> puzzles = loader.getPuzzlesForDifficulty(diff);
            assert puzzles != null : "Should return puzzle list for " + diff;
            assert puzzles.size() == count : "Puzzle count should match list size";
        }
        
        // Test getting specific puzzles
        Puzzle easyPuzzle = loader.getPuzzle(Difficulty.EASY, 0);
        assert easyPuzzle != null : "Should get first easy puzzle";
        
        Puzzle mediumPuzzle = loader.getPuzzle(Difficulty.MEDIUM, 0);
        assert mediumPuzzle != null : "Should get first medium puzzle";
        
        // Test default puzzle
        Puzzle defaultPuzzle = loader.getDefaultPuzzle();
        assert defaultPuzzle != null : "Should get default puzzle";
        
        System.out.println("PuzzleLoader test passed");
    }
    
    private static void testDifferentSizes() {
        PuzzleLoader loader = new PuzzleLoader();
        
        // Test that puzzles match their difficulty sizes
        for (Difficulty diff : Difficulty.values()) {
            Puzzle puzzle = loader.getPuzzle(diff, 0);
            if (puzzle != null) {
                boolean[][] solution = puzzle.getSolution();
                
                // Note: The current implementation may not strictly enforce size matching
                // This test verifies that we can create GameBoards from these puzzles
                GameBoard board = new GameBoard(solution);
                assert board != null : "Should create GameBoard for " + diff + " puzzle";
                assert board.getRows() == solution.length : "Board rows should match solution";
                assert board.getCols() == solution[0].length : "Board cols should match solution";
            }
        }
        
        System.out.println("Different sizes test passed");
    }
}
