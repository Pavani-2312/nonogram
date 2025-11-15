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
    private static final int CELL_SIZE = 30;
    
    public GridPanel(GameBoard board, GameController controller) {
        this.board = board;
        this.controller = controller;
        
        int width = board.getCols() * CELL_SIZE;
        int height = board.getRows() * CELL_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = e.getY() / CELL_SIZE;
                int col = e.getX() / CELL_SIZE;
                
                if (row >= 0 && row < board.getRows() && col >= 0 && col < board.getCols()) {
                    controller.handleCellClick(row, col);
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw cells
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                drawCell(g, row, col);
            }
        }
        
        // Draw grid lines
        g.setColor(Color.BLACK);
        // Horizontal lines
        for (int i = 0; i <= board.getRows(); i++) {
            g.drawLine(0, i * CELL_SIZE, board.getCols() * CELL_SIZE, i * CELL_SIZE);
        }
        // Vertical lines
        for (int i = 0; i <= board.getCols(); i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, board.getRows() * CELL_SIZE);
        }
    }
    
    private void drawCell(Graphics g, int row, int col) {
        Cell cell = board.getCell(row, col);
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;
        
        switch (cell.getCurrentState()) {
            case UNKNOWN:
                g.setColor(Color.WHITE);
                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                break;
            case FILLED:
                g.setColor(Color.BLACK);
                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                break;
            case MARKED:
                g.setColor(Color.WHITE);
                g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                g.setColor(Color.RED);
                // Draw X mark
                g.drawLine(x + 8, y + 8, x + CELL_SIZE - 8, y + CELL_SIZE - 8);
                g.drawLine(x + 8, y + CELL_SIZE - 8, x + CELL_SIZE - 8, y + 8);
                break;
        }
    }
}
