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
    private JButton xButton;
    
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
        
        // Create X button
        xButton = new JButton("X");
        xButton.setPreferredSize(new Dimension(25, 25));
        xButton.setFont(new Font("Arial", Font.BOLD, 12));
        xButton.setMargin(new Insets(0, 0, 0, 0));
        xButton.addActionListener(e -> controller.toggleXMode());
        
        // Create main game panel with proper alignment
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        
        // Create grid and column clues panel
        JPanel gridAndColumnPanel = new JPanel(new BorderLayout());
        gridAndColumnPanel.add(gridPanel, BorderLayout.CENTER);
        gridAndColumnPanel.add(columnCluePanel, BorderLayout.SOUTH);
        
        // Create left panel with X button and row clues
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel xButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        xButtonPanel.add(xButton);
        leftPanel.add(xButtonPanel, BorderLayout.WEST);
        leftPanel.add(rowCluePanel, BorderLayout.CENTER);
        
        // Add components with proper alignment
        mainGamePanel.add(leftPanel, BorderLayout.WEST);
        mainGamePanel.add(gridAndColumnPanel, BorderLayout.CENTER);
        
        // Center the entire game
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.add(mainGamePanel);
        
        add(centerWrapper, BorderLayout.CENTER);
        setBackground(Color.WHITE);
    }
    
    public GameBoard getBoard() {
        return board;
    }
    
    public void updateXButton(boolean xMode) {
        if (xMode) {
            xButton.setBackground(Color.LIGHT_GRAY);
            xButton.setText("X");
        } else {
            xButton.setBackground(null);
            xButton.setText("X");
        }
    }
}
