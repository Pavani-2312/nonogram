import nonogram.controller.PuzzleLoader;

public class TestLoading {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println("Starting puzzle loader...");
        
        PuzzleLoader loader = new PuzzleLoader();
        
        long end = System.currentTimeMillis();
        System.out.println("Puzzle loading took: " + (end - start) + "ms");
        System.out.println("Total puzzles: " + loader.getAllPuzzles().size());
    }
}
