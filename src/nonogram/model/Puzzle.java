package nonogram.model;

public class Puzzle {
    private String puzzleId;
    private String name;
    private boolean[][] solution;
    
    public Puzzle(String puzzleId, String name, boolean[][] solution) {
        this.puzzleId = puzzleId;
        this.name = name;
        this.solution = copyArray(solution);
    }
    
    public String getPuzzleId() {
        return puzzleId;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean[][] getSolution() {
        return copyArray(solution);
    }
    
    public int getSize() {
        return solution.length;
    }
    
    private boolean[][] copyArray(boolean[][] original) {
        boolean[][] copy = new boolean[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }
}
