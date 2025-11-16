package nonogram.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import nonogram.controller.GameController;
import nonogram.model.*;

public class MainFrame extends JFrame {
    private GamePanel gamePanel;
    private GameController controller;
    private JMenuItem undoItem;
    private JMenuItem redoItem;
    private JMenuItem nextPuzzleItem;
    private JMenuItem previousPuzzleItem;
    
    public MainFrame() {
        setTitle("Nonogram Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());
        setSize(1000, 800);
        createMenuBar();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Game Menu
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.setMnemonic(KeyEvent.VK_N);
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        
        JMenuItem resetPuzzle = new JMenuItem("Reset Puzzle");
        resetPuzzle.setMnemonic(KeyEvent.VK_R);
        resetPuzzle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        
        previousPuzzleItem = new JMenuItem("Previous Puzzle");
        previousPuzzleItem.setMnemonic(KeyEvent.VK_P);
        previousPuzzleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
        
        nextPuzzleItem = new JMenuItem("Next Puzzle");
        nextPuzzleItem.setMnemonic(KeyEvent.VK_X);
        nextPuzzleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_E);
        
        newGame.addActionListener(e -> {
            if (controller != null) {
                controller.startNewGame();
                updateMenuStates();
            }
        });
        
        resetPuzzle.addActionListener(e -> {
            if (controller != null) {
                controller.resetPuzzle();
                updateMenuStates();
            }
        });
        
        previousPuzzleItem.addActionListener(e -> {
            if (controller != null) {
                controller.previousPuzzle();
                updateMenuStates();
            }
        });
        
        nextPuzzleItem.addActionListener(e -> {
            if (controller != null) {
                controller.nextPuzzle();
                updateMenuStates();
            }
        });
        
        exit.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGame);
        gameMenu.add(resetPuzzle);
        gameMenu.addSeparator();
        gameMenu.add(previousPuzzleItem);
        gameMenu.add(nextPuzzleItem);
        gameMenu.addSeparator();
        
        // Difficulty submenu
        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.setMnemonic(KeyEvent.VK_D);
        
        ButtonGroup difficultyGroup = new ButtonGroup();
        for (Difficulty diff : Difficulty.values()) {
            JRadioButtonMenuItem diffItem = new JRadioButtonMenuItem(diff.getDisplayName());
            diffItem.setSelected(diff == Difficulty.EASY); // Default selection
            difficultyGroup.add(diffItem);
            diffItem.addActionListener(e -> {
                if (controller != null) {
                    controller.setDifficulty(diff);
                    updateMenuStates();
                }
            });
            difficultyMenu.add(diffItem);
        }
        
        gameMenu.add(difficultyMenu);
        gameMenu.addSeparator();
        gameMenu.add(exit);
        
        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        
        redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_D);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        
        JMenuItem clearAll = new JMenuItem("Clear All");
        clearAll.setMnemonic(KeyEvent.VK_C);
        
        JMenuItem autoFill = new JMenuItem("Auto Fill");
        autoFill.setMnemonic(KeyEvent.VK_A);
        
        JMenuItem getHint = new JMenuItem("Get Hint");
        getHint.setMnemonic(KeyEvent.VK_H);
        getHint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        
        undoItem.addActionListener(e -> {
            if (controller != null) {
                controller.undo();
                updateMenuStates();
            }
        });
        
        redoItem.addActionListener(e -> {
            if (controller != null) {
                controller.redo();
                updateMenuStates();
            }
        });
        
        clearAll.addActionListener(e -> {
            if (controller != null) {
                controller.resetPuzzle();
                updateMenuStates();
            }
        });
        
        autoFill.addActionListener(e -> {
            if (gamePanel != null && gamePanel.getBoard() != null) {
                gamePanel.getBoard().autoFillMarks();
                updateDisplay();
            }
        });
        
        getHint.addActionListener(e -> {
            if (controller != null) {
                controller.getHint();
            }
        });
        
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(clearAll);
        editMenu.add(autoFill);
        editMenu.addSeparator();
        editMenu.add(getHint);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem howToPlay = new JMenuItem("How to Play");
        JMenuItem about = new JMenuItem("About");
        
        howToPlay.addActionListener(e -> showHowToPlay());
        about.addActionListener(e -> showAbout());
        
        helpMenu.add(howToPlay);
        helpMenu.add(about);
        
        menuBar.add(gameMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        updateMenuStates();
    }
    
    private void updateMenuStates() {
        if (controller != null) {
            undoItem.setEnabled(controller.canUndo());
            redoItem.setEnabled(controller.canRedo());
            nextPuzzleItem.setEnabled(controller.hasNextPuzzle());
            previousPuzzleItem.setEnabled(controller.hasPreviousPuzzle());
        }
    }
    
    private void showHowToPlay() {
        String message = "How to Play Nonogram:\n\n" +
                "1. Click cells to cycle through states:\n" +
                "   • Empty → Filled (Black) → Marked (X) → Empty\n\n" +
                "2. Numbers show consecutive filled cells in each row/column\n\n" +
                "3. Fill cells to match the clues and reveal the picture\n\n" +
                "4. Cross marks (X) automatically fill when clues are satisfied\n\n" +
                "Keyboard Shortcuts:\n" +
                "• Ctrl+Z: Undo\n" +
                "• Ctrl+Y: Redo\n" +
                "• Ctrl+H: Get Hint\n" +
                "• Ctrl+Left/Right: Previous/Next Puzzle";
        
        JOptionPane.showMessageDialog(this, message, "How to Play", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAbout() {
        String message = "Nonogram Puzzle Game\n\n" +
                "A Java implementation using MVC architecture\n" +
                "with custom data structures.\n\n" +
                "Features:\n" +
                "• Interactive grid gameplay\n" +
                "• Undo/Redo functionality\n" +
                "• Auto-fill cross marks\n" +
                "• Hint system\n" +
                "• Multiple built-in puzzles\n" +
                "• Puzzle navigation\n" +
                "• Win detection";
        
        JOptionPane.showMessageDialog(this, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void setController(GameController controller) {
        this.controller = controller;
        updateMenuStates();
    }
    
    public void initializeGame(GameBoard board, String puzzleName) {
        if (gamePanel != null) {
            remove(gamePanel);
        }
        
        gamePanel = new GamePanel(board, controller, puzzleName);
        add(gamePanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        updateMenuStates();
    }
    
    public void updateDisplay() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
        updateMenuStates();
    }
    
    public void showCompletionMessage() {
        GameState gameState = controller.getGameState();
        String message = "Puzzle Complete!\n\n" +
                "Time: " + (gameState.getElapsedTime() / 1000) + " seconds\n" +
                "Moves: " + gameState.getMoveCount() + "\n" +
                "Hints used: " + gameState.getHintsUsed();
        
        JOptionPane.showMessageDialog(this, message, "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        if (controller != null) {
            controller.resetPuzzle();
        }
    }
    
    public void showHint(Hint hint) {
        String message = hint.getFullMessage();
        if (hint.getAffectedCells().size() > 0) {
            message += "\n\nAffected cells: ";
            for (int i = 0; i < hint.getAffectedCells().size(); i++) {
                CellPosition pos = hint.getAffectedCells().get(i);
                message += "(" + (pos.getRow() + 1) + "," + (pos.getCol() + 1) + ")";
                if (i < hint.getAffectedCells().size() - 1) {
                    message += ", ";
                }
            }
        }
        
        JOptionPane.showMessageDialog(this, message, "Hint", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showNoHintMessage() {
        JOptionPane.showMessageDialog(this, "No hints available at this time.", "No Hint", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void updateXButton(boolean xMode) {
        if (gamePanel != null) {
            gamePanel.updateXButton(xMode);
        }
    }
    
    public void showWrongMove(int row, int col) {
        if (gamePanel != null) {
            gamePanel.showWrongMove(row, col);
        }
    }
    
    public void updateLivesDisplay(int lives) {
        if (gamePanel != null) {
            gamePanel.updateLivesDisplay(lives);
        }
    }
    
    public void showGameOver() {
        JOptionPane.showMessageDialog(this, "Game Over! No lives remaining.", "Game Over", JOptionPane.ERROR_MESSAGE);
        if (controller != null) {
            controller.resetPuzzle();
        }
    }
}
