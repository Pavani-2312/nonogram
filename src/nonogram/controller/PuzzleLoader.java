package nonogram.controller;

import nonogram.model.Puzzle;
import nonogram.datastructures.MyLinkedList;

public class PuzzleLoader {
    private MyLinkedList<Puzzle> puzzles;
    
    public PuzzleLoader() {
        puzzles = new MyLinkedList<>();
        loadDefaultPuzzles();
    }
    
    public Puzzle getDefaultPuzzle() {
        return puzzles.get(0);
    }
    
    private void loadDefaultPuzzles() {
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
        puzzles.add(new Puzzle("SMILEY", "Smiley Face", smiley));
        
        // 5x5 Heart puzzle
        boolean[][] heart = {
            {false, true, false, true, false},
            {true, true, true, true, true},
            {true, true, true, true, true},
            {false, true, true, true, false},
            {false, false, true, false, false}
        };
        puzzles.add(new Puzzle("HEART", "Heart Shape", heart));
        
        // 5x5 Cross puzzle
        boolean[][] cross = {
            {false, false, true, false, false},
            {false, false, true, false, false},
            {true, true, true, true, true},
            {false, false, true, false, false},
            {false, false, true, false, false}
        };
        puzzles.add(new Puzzle("CROSS", "Cross Shape", cross));
        
        // 5x5 Square puzzle
        boolean[][] square = {
            {true, true, true, true, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, false, false, false, true},
            {true, true, true, true, true}
        };
        puzzles.add(new Puzzle("SQUARE", "Square Shape", square));
    }
}
