package nonogram.view;

import javax.swing.*;
import java.awt.*;
import nonogram.controller.GameController;
import nonogram.model.GameBoard;

public class GamePanel extends JPanel {
    private GridPanel gridPanel;
    private GameBoard board;
    private String puzzleName;
    private JButton xButton;
    private JLabel livesLabel;
    private GameController controller;

    public GamePanel(GameBoard board, GameController controller, String puzzleName) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setupGame(board, puzzleName);
    }

    public void updateGame(GameBoard board, String puzzleName) {
        removeAll();
        setupGame(board, puzzleName);
        revalidate();
        repaint();
    }

    private void setupGame(GameBoard board, String puzzleName) {
        this.board = board;
        this.puzzleName = puzzleName;

        gridPanel = new GridPanel(board, controller);

        // Create X button
        xButton = new JButton("X");
        xButton.setPreferredSize(new Dimension(50, 50));
        xButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        xButton.setMargin(new Insets(0, 0, 0, 0));
        xButton.setFocusPainted(false);
        xButton.setBorder(BorderFactory.createRaisedBevelBorder());
        xButton.addActionListener(e -> controller.toggleXMode());

        // Create main game panel with overlay
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        mainGamePanel.add(gridPanel, BorderLayout.CENTER);
        
        // Create overlay panel for floating button
        JPanel overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setLayout(null);
        overlayPanel.add(xButton);
        xButton.setBounds(10, 10, 50, 50);
        
        // Use layered pane to overlay button
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());
        layeredPane.add(mainGamePanel, BorderLayout.CENTER);
        layeredPane.add(overlayPanel, BorderLayout.CENTER);
        layeredPane.setLayer(mainGamePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.setLayer(overlayPanel, JLayeredPane.PALETTE_LAYER);
        
        // Add resize listener
        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                mainGamePanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                overlayPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
            }
        });

        add(layeredPane, BorderLayout.CENTER);
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
    
    public void showWrongMove(int row, int col) {
        gridPanel.showWrongMove(row, col);
    }
    
    public void updateLivesDisplay(int lives) {
        // Lives display is handled in MainFrame menu bar
    }
}
