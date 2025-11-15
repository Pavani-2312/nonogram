package nonogram.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import nonogram.controller.GameController;

public class PuzzleSelectionFrame extends JFrame {
    private GameController controller;
    
    public PuzzleSelectionFrame(GameController controller) {
        this.controller = controller;
        
        setTitle("Nonogram - Select Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Choose a Puzzle", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(titleLabel, BorderLayout.NORTH);
        
        // Puzzle buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 50, 50));
        
        // Create buttons manually
        JButton heartButton = new JButton("Heart Shape");
        heartButton.setFont(new Font("Arial", Font.BOLD, 18));
        heartButton.addActionListener(e -> startPuzzle(0));
        
        JButton smileyButton = new JButton("Smiley Face");
        smileyButton.setFont(new Font("Arial", Font.BOLD, 18));
        smileyButton.addActionListener(e -> startPuzzle(1));
        
        JButton crossButton = new JButton("Cross Shape");
        crossButton.setFont(new Font("Arial", Font.BOLD, 18));
        crossButton.addActionListener(e -> startPuzzle(2));
        
        JButton squareButton = new JButton("Square Shape");
        squareButton.setFont(new Font("Arial", Font.BOLD, 18));
        squareButton.addActionListener(e -> startPuzzle(3));
        
        buttonPanel.add(heartButton);
        buttonPanel.add(smileyButton);
        buttonPanel.add(crossButton);
        buttonPanel.add(squareButton);
        
        add(buttonPanel, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }
    
    private void startPuzzle(int puzzleIndex) {
        controller.startGameWithPuzzleIndex(puzzleIndex);
        dispose();
    }
}
