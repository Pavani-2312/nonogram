package nonogram.controller;

import nonogram.model.Puzzle;
import nonogram.model.Difficulty;
import nonogram.datastructures.MyLinkedList;
import nonogram.datastructures.MyHashMap;

public class PuzzleLoader {
    private MyHashMap<Difficulty, MyLinkedList<Puzzle>> puzzlesByDifficulty;
    
    public PuzzleLoader() {
        puzzlesByDifficulty = new MyHashMap<>();
        loadDefaultPuzzles();
    }
    
    public Puzzle getDefaultPuzzle() {
        return getPuzzle(Difficulty.EASY, 0);
    }
    
    public Puzzle getPuzzle(Difficulty difficulty, int index) {
        MyLinkedList<Puzzle> puzzles = puzzlesByDifficulty.get(difficulty);
        if (puzzles != null && index >= 0 && index < puzzles.size()) {
            return puzzles.get(index);
        }
        return getDefaultPuzzle();
    }
    
    public MyLinkedList<Puzzle> getPuzzlesForDifficulty(Difficulty difficulty) {
        return puzzlesByDifficulty.get(difficulty);
    }
    
    public int getPuzzleCount(Difficulty difficulty) {
        MyLinkedList<Puzzle> puzzles = puzzlesByDifficulty.get(difficulty);
        return puzzles != null ? puzzles.size() : 0;
    }
    
    private void loadDefaultPuzzles() {
        // Initialize lists for each difficulty
        for (Difficulty diff : Difficulty.values()) {
            puzzlesByDifficulty.put(diff, new MyLinkedList<>());
        }
        
        loadEasyPuzzles();
        loadMediumPuzzles();
        loadHardPuzzles();
        loadExpertPuzzles();
    }
    
    private void loadEasyPuzzles() {
        MyLinkedList<Puzzle> easyPuzzles = puzzlesByDifficulty.get(Difficulty.EASY);
        
        // 5x5 Heart puzzle
        boolean[][] heart = {
            {false, true, false, true, false},
            {true, true, true, true, true},
            {true, true, true, true, true},
            {false, true, true, true, false},
            {false, false, true, false, false}
        };
        easyPuzzles.add(new Puzzle("HEART", "Heart Shape", heart));
        
        // 5x5 Cross puzzle
        boolean[][] cross = {
            {false, false, true, false, false},
            {false, false, true, false, false},
            {true, true, true, true, true},
            {false, false, true, false, false},
            {false, false, true, false, false}
        };
        easyPuzzles.add(new Puzzle("CROSS", "Cross Shape", cross));
        
        // 5x5 Square puzzle
        boolean[][] square = {
            {true, true, true, true, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, true, true, true, true}
        };
        easyPuzzles.add(new Puzzle("SQUARE", "Square Shape", square));
    }
    
    private void loadMediumPuzzles() {
        MyLinkedList<Puzzle> mediumPuzzles = puzzlesByDifficulty.get(Difficulty.MEDIUM);
        
        // 10x10 Smiley Face puzzle
        boolean[][] smiley = {
            {false, false, true, true, true, true, true, true, false, false},
            {false, true, false, false, false, false, false, false, true, false},
            {true, false, true, false, false, false, false, true, false, true},
            {true, false, false, false, false, false, false, false, false, true},
            {true, false, true, false, false, false, false, true, false, true},
            {true, false, false, false, true, true, false, false, false, true},
            {true, false, false, true, false, false, true, false, false, true},
            {true, false, false, false, true, true, false, false, false, true},
            {false, true, false, false, false, false, false, false, true, false},
            {false, false, true, true, true, true, true, true, false, false}
        };
        mediumPuzzles.add(new Puzzle("SMILEY", "Smiley Face", smiley));
        
        // 10x10 House puzzle
        boolean[][] house = {
            {false, false, false, false, true, true, false, false, false, false},
            {false, false, false, true, false, false, true, false, false, false},
            {false, false, true, false, false, false, false, true, false, false},
            {false, true, false, false, false, false, false, false, true, false},
            {true, true, true, true, true, true, true, true, true, true},
            {true, false, true, false, false, false, false, true, false, true},
            {true, false, true, false, false, false, false, true, false, true},
            {true, false, false, true, true, true, true, false, false, true},
            {true, false, false, true, false, false, true, false, false, true},
            {true, true, true, true, true, true, true, true, true, true}
        };
        mediumPuzzles.add(new Puzzle("HOUSE", "House", house));
    }
    
    private void loadHardPuzzles() {
        MyLinkedList<Puzzle> hardPuzzles = puzzlesByDifficulty.get(Difficulty.HARD);
        
        // 15x15 Star puzzle (simplified for demonstration)
        boolean[][] star = new boolean[15][15];
        // Create a simple star pattern
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                // Simple star pattern - center vertical and horizontal lines plus diagonals
                if (i == 7 || j == 7 || i == j || i + j == 14) {
                    star[i][j] = true;
                }
            }
        }
        hardPuzzles.add(new Puzzle("STAR", "Star Pattern", star));
    }
    
    private void loadExpertPuzzles() {
        MyLinkedList<Puzzle> expertPuzzles = puzzlesByDifficulty.get(Difficulty.EXPERT);
        
        // 20x20 Complex pattern (simplified for demonstration)
        boolean[][] complex = new boolean[20][20];
        // Create a complex pattern with multiple shapes
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                // Create a pattern with borders and internal structure
                if (i == 0 || i == 19 || j == 0 || j == 19 || // Border
                    (i > 5 && i < 14 && j > 5 && j < 14 && (i + j) % 3 == 0)) { // Internal pattern
                    complex[i][j] = true;
                }
            }
        }
        expertPuzzles.add(new Puzzle("COMPLEX", "Complex Pattern", complex));
    }
}
