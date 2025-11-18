package nonogram.controller;

import nonogram.model.Puzzle;
import nonogram.model.Difficulty;
import nonogram.datastructures.MyLinkedList;
import nonogram.datastructures.MyHashMap;

public class PuzzleLoader {
    private MyLinkedList<Puzzle> puzzles;
    private MyHashMap<Difficulty, MyLinkedList<Puzzle>> puzzlesByDifficulty;
    
    public PuzzleLoader() {
        puzzles = new MyLinkedList<>();
        puzzlesByDifficulty = new MyHashMap<>();
        loadDefaultPuzzles();
    }
    
    public Puzzle getDefaultPuzzle() {
        return puzzles.get(0);
    }
    
    public MyLinkedList<Puzzle> getAllPuzzles() {
        return puzzles;
    }
    
    public MyLinkedList<Puzzle> getPuzzlesForDifficulty(Difficulty difficulty) {
        return puzzlesByDifficulty.get(difficulty);
    }
    
    public int getPuzzleCount(Difficulty difficulty) {
        MyLinkedList<Puzzle> difficultyPuzzles = puzzlesByDifficulty.get(difficulty);
        return difficultyPuzzles != null ? difficultyPuzzles.size() : 0;
    }
    
    public Puzzle getPuzzle(Difficulty difficulty, int index) {
        MyLinkedList<Puzzle> difficultyPuzzles = puzzlesByDifficulty.get(difficulty);
        if (difficultyPuzzles != null && index >= 0 && index < difficultyPuzzles.size()) {
            return difficultyPuzzles.get(index);
        }
        return null;
    }
    
    private void loadDefaultPuzzles() {
        // Initialize difficulty maps
        for (Difficulty diff : Difficulty.values()) {
            puzzlesByDifficulty.put(diff, new MyLinkedList<>());
        }
        
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
        // 10x10 House puzzle - pre-calculated
        boolean[][] house = {
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
        addPuzzle(Difficulty.MEDIUM, new Puzzle("HOUSE", "House", house));
        
        // 10x10 Tree puzzle - pre-calculated
        boolean[][] tree = {
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
        addPuzzle(Difficulty.MEDIUM, new Puzzle("TREE", "Tree", tree));
    }
    
    private void loadHardPuzzles() {
        // 15x15 Star puzzle - pre-calculated
        boolean[][] star = {
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
        addPuzzle(Difficulty.HARD, new Puzzle("STAR", "Star Pattern", star));
        
        // 15x15 Castle puzzle - pre-calculated
        boolean[][] castle = {
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
        addPuzzle(Difficulty.HARD, new Puzzle("CASTLE", "Castle", castle));
    }
    
    private void loadExpertPuzzles() {
        // 20x20 Diamond puzzle - pre-calculated
        boolean[][] diamond = {
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
        addPuzzle(Difficulty.EXPERT, new Puzzle("DIAMOND", "Diamond Shape", diamond));
        
        // 20x20 Checkerboard puzzle - pre-calculated
        boolean[][] checker = {
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
        addPuzzle(Difficulty.EXPERT, new Puzzle("CHECKER", "Checkerboard", checker));
    }
    
    private void addPuzzle(Difficulty difficulty, Puzzle puzzle) {
        puzzles.add(puzzle);
        puzzlesByDifficulty.get(difficulty).add(puzzle);
    }
}
