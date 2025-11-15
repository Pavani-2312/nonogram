package nonogram.view;

import javax.swing.*;
import java.awt.*;
import nonogram.controller.GameController;
import nonogram.model.GameBoard;

public class MainFrame extends JFrame {
    private GamePanel gamePanel;
    private GameController controller;
    
    public MainFrame() {
        setTitle("Nonogram Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLayout(new BorderLayout());
        setSize(600, 700);
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
        JOptionPane.showMessageDialog(this, "Puzzle Complete!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
    }
}
