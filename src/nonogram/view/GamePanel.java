package nonogram.view;

import javax.swing.*;
import java.awt.*;
import nonogram.controller.GameController;
import nonogram.model.GameBoard;

public class GamePanel extends JPanel {
    private GridPanel gridPanel;
    private CluePanel rowCluePanel;
    private CluePanel columnCluePanel;
    private GameBoard board;
    private String puzzleName;
    
    public GamePanel(GameBoard board, GameController controller, String puzzleName) {
        this.board = board;
        this.puzzleName = puzzleName;
        setLayout(new BorderLayout());
        
        // Add puzzle title at the top
        JLabel titleLabel = new JLabel(puzzleName, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);
        
        gridPanel = new GridPanel(board, controller);
        rowCluePanel = new CluePanel(board, true);
        columnCluePanel = new CluePanel(board, false);
        
        // Create main game panel with proper alignment
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        
        // Create grid and column clues panel
        JPanel gridAndColumnPanel = new JPanel(new BorderLayout());
        gridAndColumnPanel.add(gridPanel, BorderLayout.CENTER);
        gridAndColumnPanel.add(columnCluePanel, BorderLayout.SOUTH);
        
        // Add components with proper alignment
        mainGamePanel.add(rowCluePanel, BorderLayout.WEST);
        mainGamePanel.add(gridAndColumnPanel, BorderLayout.CENTER);
        
        // Center the entire game
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.add(mainGamePanel);
        
        add(centerWrapper, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }
}
