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
    private JLabel livesLabel;
    public GamePanel(GameBoard board, GameController controller, String puzzleName) {
        this.board = board;
        this.puzzleName = puzzleName;
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(puzzleName, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        livesLabel = new JLabel("Lives: 3", JLabel.RIGHT);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        livesLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(livesLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        gridPanel = new GridPanel(board, controller);
        int cellSize = gridPanel.getCellSize();
        rowCluePanel = new CluePanel(board, true, cellSize);
        columnCluePanel = new CluePanel(board, false, cellSize);
        xButton = new JButton("X");
        int buttonSize = Math.max(cellSize - 5, 25);
        xButton.setPreferredSize(new Dimension(buttonSize, buttonSize));
        xButton.setFont(new Font("Arial", Font.BOLD, 12));
        xButton.setMargin(new Insets(0, 0, 0, 0));
        xButton.addActionListener(e -> controller.toggleXMode());
        JPanel mainGamePanel = new JPanel(new BorderLayout());
        mainGamePanel.setBackground(Color.WHITE);
        JPanel gridAndColumnPanel = new JPanel(new BorderLayout());
        gridAndColumnPanel.setBackground(Color.WHITE);
        gridAndColumnPanel.add(gridPanel, BorderLayout.CENTER);
        gridAndColumnPanel.add(columnCluePanel, BorderLayout.SOUTH);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        JPanel xButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        xButtonPanel.setBackground(Color.WHITE);
        xButtonPanel.add(xButton);
        leftPanel.add(xButtonPanel, BorderLayout.WEST);
        leftPanel.add(rowCluePanel, BorderLayout.CENTER);
        mainGamePanel.add(leftPanel, BorderLayout.WEST);
        mainGamePanel.add(gridAndColumnPanel, BorderLayout.CENTER);
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(mainGamePanel, gbc);
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
    public void showWrongMove(int row, int col) {
        gridPanel.showWrongMove(row, col);
    }
    public void updateLivesDisplay(int lives) {
        livesLabel.setText("Lives: " + lives);
        if (lives <= 1) {
            livesLabel.setForeground(Color.RED);
        } else {
            livesLabel.setForeground(Color.BLACK);
        }
    }
}
