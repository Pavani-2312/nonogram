package nonogram.view;

import javax.swing.*;
import java.awt.*;
import nonogram.model.GameBoard;
import nonogram.datastructures.MyLinkedList;

public class CluePanel extends JPanel {
    private GameBoard board;
    private boolean isRowClues;
    private int gridCellSize = 30;
    
    public CluePanel(GameBoard board, boolean isRowClues) {
        this.board = board;
        this.isRowClues = isRowClues;
        setBackground(Color.LIGHT_GRAY);
    }

    public void setGridMetrics(int cellSize, int xOffset, int yOffset) {
        this.gridCellSize = cellSize;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (isRowClues) {
            return new Dimension(80, board.getRows() * gridCellSize);
        } else {
            return new Dimension(board.getCols() * gridCellSize, 50);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (isRowClues) {
            drawRowClues(g);
        } else {
            drawColumnClues(g);
        }
    }
    
    private void drawRowClues(Graphics g) {
        int fontSize = Math.max(10, gridCellSize / 3);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));

        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            g.setColor(board.isRowCluesSatisfied(row, clues) ? Color.GRAY : Color.BLACK);
            
            StringBuilder clueText = new StringBuilder();
            for (int i = 0; i < clues.size(); i++) {
                if (i > 0) clueText.append(" ");
                clueText.append(clues.get(i));
            }
            
            FontMetrics fm = g.getFontMetrics();
            int x = getWidth() - fm.stringWidth(clueText.toString()) - 10;
            int y = row * gridCellSize + gridCellSize / 2 + fm.getAscent() / 2;
            
            g.drawString(clueText.toString(), x, y);
        }
    }
    
    private void drawColumnClues(Graphics g) {
        int fontSize = Math.max(10, gridCellSize / 3);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));

        for (int col = 0; col < board.getCols(); col++) {
            MyLinkedList<Integer> clues = board.getColumnClues(col);
            g.setColor(board.isColumnCluesSatisfied(col, clues) ? Color.GRAY : Color.BLACK);
            
            int x = col * gridCellSize + gridCellSize / 2;
            FontMetrics fm = g.getFontMetrics();
            int y = getHeight() - 10;

            for (int i = clues.size() - 1; i >= 0; i--) {
                String clueStr = clues.get(i).toString();
                int textWidth = fm.stringWidth(clueStr);
                g.drawString(clueStr, x - textWidth / 2, y);
                y -= fm.getHeight();
            }
        }
    }
}
