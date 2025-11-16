package nonogram.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import nonogram.controller.GameController;
import nonogram.model.GameBoard;
import nonogram.model.Hint;
import nonogram.model.Difficulty;

public class MainFrame extends JFrame {
    private GamePanel gamePanel;
    private GameController controller;
    private JMenuBar menuBar;
    
    public MainFrame() {
        setTitle("Nonogram Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());
        setSize(600, 700);
        
        createMenuBar();
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // Game menu
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.setMnemonic(KeyEvent.VK_N);
        newGameItem.addActionListener(e -> {
            if (controller != null) {
                controller.startNewGame();
            }
        });
        
        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.setMnemonic(KeyEvent.VK_R);
        resetItem.addActionListener(e -> {
            if (controller != null) {
                controller.resetPuzzle();
            }
        });
        
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        
        JMenuItem nextPuzzleItem = new JMenuItem("Next Puzzle");
        nextPuzzleItem.setMnemonic(KeyEvent.VK_X);
        nextPuzzleItem.addActionListener(e -> {
            if (controller != null) {
                controller.nextPuzzle();
            }
        });
        
        JMenuItem prevPuzzleItem = new JMenuItem("Previous Puzzle");
        prevPuzzleItem.setMnemonic(KeyEvent.VK_P);
        prevPuzzleItem.addActionListener(e -> {
            if (controller != null) {
                controller.previousPuzzle();
            }
        });
        
        gameMenu.add(nextPuzzleItem);
        gameMenu.add(prevPuzzleItem);
        gameMenu.addSeparator();
        gameMenu.add(resetItem);
        
        // Difficulty submenu
        JMenu difficultyMenu = new JMenu("Difficulty");
        difficultyMenu.setMnemonic(KeyEvent.VK_D);
        
        for (Difficulty diff : Difficulty.values()) {
            JMenuItem diffItem = new JMenuItem(diff.getDisplayName() + " (" + diff.getRows() + "x" + diff.getCols() + ")");
            diffItem.addActionListener(e -> {
                if (controller != null) {
                    controller.startNewGame(diff);
                }
            });
            difficultyMenu.add(diffItem);
        }
        
        gameMenu.add(difficultyMenu);
        
        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undoItem.addActionListener(e -> {
            if (controller != null) {
                controller.undo();
            }
        });
        
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_R);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        redoItem.addActionListener(e -> {
            if (controller != null) {
                controller.redo();
            }
        });
        
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem hintItem = new JMenuItem("Show Hint");
        hintItem.setMnemonic(KeyEvent.VK_H);
        hintItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        hintItem.addActionListener(e -> {
            if (controller != null) {
                controller.showHint();
            }
        });
        
        helpMenu.add(hintItem);
        
        menuBar.add(gameMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    public void setController(GameController controller) {
        this.controller = controller;
    }
    
    public void initializeGame(GameBoard board, String puzzleName) {
        if (gamePanel != null) {
            remove(gamePanel);
        }
        
        gamePanel = new GamePanel(board, controller, puzzleName);
        add(gamePanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    public void updateDisplay() {
        if (gamePanel != null) {
            gamePanel.repaint();
        }
    }
    
    public void showCompletionMessage() {
        if (controller != null && controller.getGameState() != null) {
            long timeMs = controller.getGameState().getElapsedTime();
            int moves = controller.getGameState().getMoveCount();
            int hints = controller.getGameState().getHintsUsed();
            
            String message = String.format(
                "Puzzle Complete!\n\nTime: %.1f seconds\nMoves: %d\nHints used: %d",
                timeMs / 1000.0, moves, hints
            );
            
            JOptionPane.showMessageDialog(this, message, "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Puzzle Complete!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showHint(Hint hint) {
        String message = hint.getFullMessage();
        JOptionPane.showMessageDialog(this, message, "Hint", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
