package nonogram.view;

import javax.swing.*;
import java.awt.*;
import nonogram.model.GameBoard;
import nonogram.datastructures.MyLinkedList;

public class CluePanel extends JPanel {
    private GameBoard board;
    private boolean isRowClues;
    private int cellSize;
    
    public CluePanel(GameBoard board, boolean isRowClues, int cellSize) {
        this.board = board;
        this.isRowClues = isRowClues;
        this.cellSize = cellSize;
        
        if (isRowClues) {
            setPreferredSize(new Dimension(cellSize * 2, board.getRows() * cellSize));
        } else {
            setPreferredSize(new Dimension(board.getCols() * cellSize, cellSize * 2));
        }
        
        setBackground(Color.WHITE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        int fontSize = Math.max(cellSize / 3, 12);
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        
        if (isRowClues) {
            drawRowClues(g);
        } else {
            drawColumnClues(g);
        }
    }
    
    private void drawRowClues(Graphics g) {
        // Calculate offset to align with grid (account for any top padding)
        int startY = 0; // Start from the actual top of this panel
        
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            StringBuilder clueText = new StringBuilder();
            
            for (int i = 0; i < clues.size(); i++) {
                if (i > 0) clueText.append(" ");
                clueText.append(clues.get(i));
            }
            
            // Align exactly with the grid row center
            int y = startY + row * cellSize + cellSize / 2 + (cellSize / 8);
            // Right-align the text
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(clueText.toString());
            int x = getWidth() - textWidth - (cellSize / 8);
            
            g.drawString(clueText.toString(), x, y);
        }
    }
    
    private void drawColumnClues(Graphics g) {
        for (int col = 0; col < board.getCols(); col++) {
            MyLinkedList<Integer> clues = board.getColumnClues(col);
            
            // Center horizontally with the grid column
            int x = col * cellSize + cellSize / 2;
            int y = Math.max(cellSize / 4, 15);
            int lineSpacing = Math.max(cellSize / 3, 18);
            
            // Draw each clue number vertically
            for (int i = 0; i < clues.size(); i++) {
                String clueStr = clues.get(i).toString();
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(clueStr);
                
                // Center the text horizontally
                g.drawString(clueStr, x - textWidth / 2, y + i * lineSpacing);
            }
        }
    }
}
