package nonogram.controller;

import nonogram.model.Puzzle;
import nonogram.model.Difficulty;
import nonogram.datastructures.MyLinkedList;

public class PuzzleLoader {
    private MyLinkedList<Puzzle> puzzles;
    private MyLinkedList<Puzzle> easyPuzzles;
    private MyLinkedList<Puzzle> mediumPuzzles;
    private MyLinkedList<Puzzle> hardPuzzles;
    private MyLinkedList<Puzzle> expertPuzzles;
    
    // Pre-initialized static arrays
    private static final boolean[][] HOUSE = {
        {false,false,false,true,true,true,true,false,false,false},
        {false,false,true,true,true,true,true,true,false,false},
        {false,true,true,true,true,true,true,true,true,false},
        {false,true,false,false,false,false,false,false,true,false},
        {false,true,false,false,false,false,false,false,true,false},
        {false,true,false,false,true,true,false,false,true,false},
        {false,true,false,false,true,true,false,false,true,false},
        {false,true,false,false,true,true,false,false,true,false},
        {false,true,false,false,false,false,false,false,true,false},
        {false,true,true,true,true,true,true,true,true,false}
    };
    
    private static final boolean[][] TREE = {
        {false,false,false,false,true,true,false,false,false,false},
        {false,false,false,true,true,true,true,false,false,false},
        {false,false,true,true,true,true,true,true,false,false},
        {false,true,true,true,true,true,true,true,true,false},
        {false,false,false,false,true,true,false,false,false,false},
        {false,false,false,false,true,true,false,false,false,false},
        {false,false,false,false,true,true,false,false,false,false},
        {false,false,false,false,true,true,false,false,false,false},
        {false,false,false,false,true,true,false,false,false,false},
        {false,false,false,false,true,true,false,false,false,false}
    };
    
    private static final boolean[][] STAR = {
        {false,false,false,false,false,false,false,true,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,true,false,true,false,true,false,false,false,false,false},
        {false,false,false,false,true,false,false,true,false,false,true,false,false,false,false},
        {false,false,false,true,false,false,false,true,false,false,false,true,false,false,false},
        {false,false,true,false,false,false,false,true,false,false,false,false,true,false,false},
        {false,true,false,false,false,false,false,true,false,false,false,false,false,true,false},
        {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true},
        {false,true,false,false,false,false,false,true,false,false,false,false,false,true,false},
        {false,false,true,false,false,false,false,true,false,false,false,false,true,false,false},
        {false,false,false,true,false,false,false,true,false,false,false,true,false,false,false},
        {false,false,false,false,true,false,false,true,false,false,true,false,false,false,false},
        {false,false,false,false,false,true,false,true,false,true,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,true,false,false,false,false,false,false,false}
    };
    
    private static final boolean[][] CASTLE = {
        {false,false,false,false,false,false,true,false,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,false,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,true,true,true,true,true,true,true,false,false,false,false},
        {false,false,false,false,true,true,true,true,true,true,true,false,false,false,false},
        {false,false,false,false,true,true,true,false,true,true,true,false,false,false,false},
        {false,false,false,false,true,true,true,false,true,true,true,false,false,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false}
    };
    
    private static final boolean[][] DIAMOND = {
        {false,false,false,false,false,false,false,false,false,true,true,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,true,true,true,true,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,true,true,true,true,true,true,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,false,false,false,false,false},
        {false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,false,false,false,false},
        {false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false},
        {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true},
        {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true},
        {false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,false,false,false},
        {false,false,false,false,true,true,true,true,true,true,true,true,true,true,true,true,false,false,false,false},
        {false,false,false,false,false,true,true,true,true,true,true,true,true,true,true,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,true,true,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,true,true,true,true,true,true,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,true,true,true,true,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,true,true,false,false,false,false,false,false,false,false,false}
    };
    
    private static final boolean[][] CHECKER = {
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true},
        {true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false},
        {false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true}
    };
    
    public PuzzleLoader() {
        puzzles = new MyLinkedList<>();
        easyPuzzles = new MyLinkedList<>();
        mediumPuzzles = new MyLinkedList<>();
        hardPuzzles = new MyLinkedList<>();
        expertPuzzles = new MyLinkedList<>();
        loadDefaultPuzzles();
    }
    
    public Puzzle getDefaultPuzzle() {
        return puzzles.get(0);
    }
    
    public MyLinkedList<Puzzle> getAllPuzzles() {
        return puzzles;
    }
    
    public MyLinkedList<Puzzle> getPuzzlesForDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY: return easyPuzzles;
            case MEDIUM: return mediumPuzzles;
            case HARD: return hardPuzzles;
            case EXPERT: return expertPuzzles;
            default: return easyPuzzles;
        }
    }
    
    public int getPuzzleCount(Difficulty difficulty) {
        return getPuzzlesForDifficulty(difficulty).size();
    }
    
    public Puzzle getPuzzle(Difficulty difficulty, int index) {
        MyLinkedList<Puzzle> difficultyPuzzles = getPuzzlesForDifficulty(difficulty);
        if (index >= 0 && index < difficultyPuzzles.size()) {
            return difficultyPuzzles.get(index);
        }
        return null;
    }
    
    private void loadDefaultPuzzles() {
        loadEasyPuzzles();
        loadMediumPuzzles();
        loadHardPuzzles();
        loadExpertPuzzles();
    }
    
    private void loadEasyPuzzles() {
        // 5x5 Heart puzzle
        boolean[][] heart = {
            {false, true, false, true, false},
            {true, true, true, true, true},
            {true, true, true, true, true},
            {false, true, true, true, false},
            {false, false, true, false, false}
        };
        addPuzzle(Difficulty.EASY, new Puzzle("HEART", "Heart Shape", heart));
        
        // 5x5 Smiley Face puzzle
        boolean[][] smiley = {
            {false, true, true, true, false},
            {true, false, true, false, true},
            {true, false, false, false, true},
            {true, true, false, true, true},
            {false, true, true, true, false}
        };
        addPuzzle(Difficulty.EASY, new Puzzle("SMILEY", "Smiley Face", smiley));
        
        // 5x5 Cross puzzle
        boolean[][] cross = {
            {false, false, true, false, false},
            {false, false, true, false, false},
            {true, true, true, true, true},
            {false, false, true, false, false},
            {false, false, true, false, false}
        };
        addPuzzle(Difficulty.EASY, new Puzzle("CROSS", "Cross Shape", cross));
        
        // 5x5 Square puzzle
        boolean[][] square = {
            {true, true, true, true, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, true, true, true, true}
        };
        addPuzzle(Difficulty.EASY, new Puzzle("SQUARE", "Square Shape", square));
    }
    
    private void loadMediumPuzzles() {
        addPuzzle(Difficulty.MEDIUM, new Puzzle("HOUSE", "House", HOUSE));
        addPuzzle(Difficulty.MEDIUM, new Puzzle("TREE", "Tree", TREE));
    }
    
    private void loadHardPuzzles() {
        addPuzzle(Difficulty.HARD, new Puzzle("STAR", "Star Pattern", STAR));
        addPuzzle(Difficulty.HARD, new Puzzle("CASTLE", "Castle", CASTLE));
    }
    
    private void loadExpertPuzzles() {
        addPuzzle(Difficulty.EXPERT, new Puzzle("DIAMOND", "Diamond Shape", DIAMOND));
        addPuzzle(Difficulty.EXPERT, new Puzzle("CHECKER", "Checkerboard", CHECKER));
    }
    
    private void addPuzzle(Difficulty difficulty, Puzzle puzzle) {
        puzzles.add(puzzle);
        switch (difficulty) {
            case EASY: easyPuzzles.add(puzzle); break;
            case MEDIUM: mediumPuzzles.add(puzzle); break;
            case HARD: hardPuzzles.add(puzzle); break;
            case EXPERT: expertPuzzles.add(puzzle); break;
        }
    }
}
