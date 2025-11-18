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
        
        // EASY (5x5) puzzles
        loadEasyPuzzles();
        
        // MEDIUM (10x10) puzzles  
        loadMediumPuzzles();
        
        // HARD (15x15) puzzles
        loadHardPuzzles();
        
        // EXPERT (20x20) puzzles
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
        // 10x10 House puzzle
        boolean[][] house = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                house[i][j] = (i == 0 && j >= 3 && j <= 6) || 
                             (i == 1 && j >= 2 && j <= 7) ||
                             (i == 2 && j >= 1 && j <= 8) ||
                             (i >= 3 && i <= 8 && (j == 1 || j == 8)) || 
                             (i == 9 && j >= 1 && j <= 8) || 
                             (i >= 5 && i <= 7 && j >= 4 && j <= 5);
            }
        }
        addPuzzle(Difficulty.MEDIUM, new Puzzle("HOUSE", "House", house));
        
        // 10x10 Tree puzzle
        boolean[][] tree = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tree[i][j] = (i <= 6 && j >= 4 && j <= 5) || // trunk
                           (i >= 0 && i <= 3 && Math.abs(j - 4.5) <= i + 1); // leaves
            }
        }
        addPuzzle(Difficulty.MEDIUM, new Puzzle("TREE", "Tree", tree));
        
        // 10x10 Car puzzle
        boolean[][] car = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                car[i][j] = (i >= 3 && i <= 6 && j >= 1 && j <= 8) || // body
                          (i == 7 && (j == 2 || j == 7)) || // wheels
                          (i == 2 && j >= 2 && j <= 7); // roof
            }
        }
        addPuzzle(Difficulty.MEDIUM, new Puzzle("CAR", "Car", car));
        
        // 10x10 Flower puzzle
        boolean[][] flower = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                flower[i][j] = (i == 4 && j == 4) || // center
                             (i == 3 && j >= 3 && j <= 5) || // petals
                             (i == 5 && j >= 3 && j <= 5) ||
                             (j == 3 && i >= 3 && i <= 5) ||
                             (j == 5 && i >= 3 && i <= 5) ||
                             (i >= 6 && i <= 8 && j == 4); // stem
            }
        }
        addPuzzle(Difficulty.MEDIUM, new Puzzle("FLOWER", "Flower", flower));
        
        // 10x10 Fish puzzle
        boolean[][] fish = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                fish[i][j] = (i >= 3 && i <= 6 && j >= 2 && j <= 6) || // body
                           (i == 4 && j == 7) || (i == 5 && j == 7) || // tail
                           (i == 1 && j >= 4 && j <= 5) || // top fin
                           (i == 8 && j >= 4 && j <= 5); // bottom fin
            }
        }
        addPuzzle(Difficulty.MEDIUM, new Puzzle("FISH", "Fish", fish));
    }
    
    private void loadHardPuzzles() {
        // 15x15 Star puzzle
        boolean[][] star = new boolean[15][15];
        int center = 7;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                star[i][j] = (i == center) || (j == center) || 
                           (Math.abs(i - center) == Math.abs(j - center));
            }
        }
        addPuzzle(Difficulty.HARD, new Puzzle("STAR", "Star Pattern", star));
        
        // 15x15 Castle puzzle
        boolean[][] castle = new boolean[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                castle[i][j] = (i >= 10 && i <= 14 && j >= 2 && j <= 12) || // base
                             (i >= 6 && i <= 9 && j >= 4 && j <= 10) || // middle
                             (i >= 2 && i <= 5 && j >= 6 && j <= 8) || // tower
                             (i == 1 && (j == 6 || j == 8)) || // flags
                             (i >= 8 && i <= 10 && j >= 6 && j <= 8); // gate
            }
        }
        addPuzzle(Difficulty.HARD, new Puzzle("CASTLE", "Castle", castle));
        
        // 15x15 Butterfly puzzle
        boolean[][] butterfly = new boolean[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                butterfly[i][j] = (j == 7) || // body
                                (i >= 2 && i <= 6 && ((j >= 2 && j <= 6) || (j >= 8 && j <= 12))) || // wings
                                (i >= 8 && i <= 12 && ((j >= 2 && j <= 6) || (j >= 8 && j <= 12)));
            }
        }
        addPuzzle(Difficulty.HARD, new Puzzle("BUTTERFLY", "Butterfly", butterfly));
        
        // 15x15 Ship puzzle
        boolean[][] ship = new boolean[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                ship[i][j] = (i >= 10 && i <= 12 && j >= 3 && j <= 11) || // hull
                           (i >= 6 && i <= 9 && j == 7) || // mast
                           (i == 6 && j >= 5 && j <= 9) || // sail
                           (i >= 7 && i <= 8 && j >= 4 && j <= 10);
            }
        }
        addPuzzle(Difficulty.HARD, new Puzzle("SHIP", "Ship", ship));
        
        // 15x15 Mountain puzzle
        boolean[][] mountain = new boolean[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                mountain[i][j] = (i >= 14 - j && i >= j - 0 && i <= 14) || // left slope
                               (i >= j - 14 && i >= 28 - j && i <= 14); // right slope
            }
        }
        addPuzzle(Difficulty.HARD, new Puzzle("MOUNTAIN", "Mountain", mountain));
    }
    
    private void loadExpertPuzzles() {
        // 20x20 Diamond puzzle
        boolean[][] diamond = new boolean[20][20];
        int center = 9;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                diamond[i][j] = (Math.abs(i - center) + Math.abs(j - center)) <= center;
            }
        }
        addPuzzle(Difficulty.EXPERT, new Puzzle("DIAMOND", "Diamond Shape", diamond));
        
        // 20x20 Spiral puzzle
        boolean[][] spiral = new boolean[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                int layer = Math.min(Math.min(i, j), Math.min(19-i, 19-j));
                spiral[i][j] = (layer % 2 == 0);
            }
        }
        addPuzzle(Difficulty.EXPERT, new Puzzle("SPIRAL", "Spiral Pattern", spiral));
        
        // 20x20 Checkerboard puzzle
        boolean[][] checker = new boolean[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                checker[i][j] = (i + j) % 2 == 0;
            }
        }
        addPuzzle(Difficulty.EXPERT, new Puzzle("CHECKER", "Checkerboard", checker));
        
        // 20x20 Circle puzzle
        boolean[][] circle = new boolean[20][20];
        center = 9;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                double dist = Math.sqrt((i-center)*(i-center) + (j-center)*(j-center));
                circle[i][j] = (dist >= 6 && dist <= 9);
            }
        }
        addPuzzle(Difficulty.EXPERT, new Puzzle("CIRCLE", "Circle Ring", circle));
        
        // 20x20 Maze puzzle
        boolean[][] maze = new boolean[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                maze[i][j] = (i % 4 == 0 || j % 4 == 0) && 
                           !((i % 4 == 0 && j % 4 == 0) || 
                             (i % 8 == 2 && j % 4 == 0) ||
                             (i % 4 == 0 && j % 8 == 2));
            }
        }
        addPuzzle(Difficulty.EXPERT, new Puzzle("MAZE", "Maze Pattern", maze));
    }
    
    private void addPuzzle(Difficulty difficulty, Puzzle puzzle) {
        puzzles.add(puzzle);
        puzzlesByDifficulty.get(difficulty).add(puzzle);
    }
}
