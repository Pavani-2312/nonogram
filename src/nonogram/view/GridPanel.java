package nonogram.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import nonogram.controller.GameController;
import nonogram.model.GameBoard;
import nonogram.model.Cell;
import nonogram.model.CellState;
public class GridPanel extends JPanel {
    private GameBoard board;
    private GameController controller;
    private int cellSize;
    private int wrongRow = -1;
    private int wrongCol = -1;
    public GridPanel(GameBoard board, GameController controller) {
        this.board = board;
        this.controller = controller;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = (int)(screenSize.width * 0.6); 
        int maxHeight = (int)(screenSize.height * 0.7); 
        int cellSizeByWidth = maxWidth / board.getCols();
        int cellSizeByHeight = maxHeight / board.getRows();
        this.cellSize = Math.min(cellSizeByWidth, cellSizeByHeight);
        this.cellSize = Math.max(this.cellSize, 30); 
        int width = board.getCols() * cellSize;
        int height = board.getRows() * cellSize;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / cellSize;
                int col = e.getX() / cellSize;
                if (row >= 0 && row < board.getRows() && col >= 0 && col < board.getCols()) {
                    controller.handleCellClick(row, col);
                }
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                drawCell(g, row, col);
            }
        }
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2)); 
        for (int i = 0; i <= board.getRows(); i++) {
            g2d.drawLine(0, i * cellSize, board.getCols() * cellSize, i * cellSize);
        }
        for (int i = 0; i <= board.getCols(); i++) {
            g2d.drawLine(i * cellSize, 0, i * cellSize, board.getRows() * cellSize);
        }
    }
    private void drawCell(Graphics g, int row, int col) {
        Cell cell = board.getCell(row, col);
        int x = col * cellSize;
        int y = row * cellSize;
        if (row == wrongRow && col == wrongCol) {
            g.setColor(Color.RED);
            g.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
            return;
        }
        switch (cell.getCurrentState()) {
            case UNKNOWN:
                g.setColor(Color.WHITE);
                g.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
                break;
            case FILLED:
                g.setColor(Color.BLACK);
                g.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
                break;
            case MARKED:
                g.setColor(Color.WHITE);
                g.fillRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
                g.setColor(Color.RED);
                int margin = cellSize / 6;
                g.drawLine(x + margin, y + margin, x + cellSize - margin, y + cellSize - margin);
                g.drawLine(x + margin, y + cellSize - margin, x + cellSize - margin, y + margin);
                break;
        }
    }
    public void showWrongMove(int row, int col) {
        wrongRow = row;
        wrongCol = col;
        repaint();
        Timer timer = new Timer(1000, e -> {
            wrongRow = -1;
            wrongCol = -1;
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
    public int getCellSize() {
        return cellSize;
    }
}
