package nonogram.view;

import javax.swing.*;
import java.awt.*;
import nonogram.model.GameBoard;
import nonogram.datastructures.MyLinkedList;

public class CluePanel extends JPanel {
    private GameBoard board;
    private boolean isRowClues;
    private static final int CELL_SIZE = 30;
    
    public CluePanel(GameBoard board, boolean isRowClues) {
        this.board = board;
        this.isRowClues = isRowClues;
        
        if (isRowClues) {
            setPreferredSize(new Dimension(80, board.getRows() * CELL_SIZE));
        } else {
            setPreferredSize(new Dimension(board.getCols() * CELL_SIZE, 60));
        }
        
        setBackground(Color.LIGHT_GRAY);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        
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
            int y = startY + row * CELL_SIZE + CELL_SIZE / 2 + 5;
            // Right-align the text
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(clueText.toString());
            int x = getWidth() - textWidth - 8;
            
            g.drawString(clueText.toString(), x, y);
        }
    }
    
    private void drawColumnClues(Graphics g) {
        for (int col = 0; col < board.getCols(); col++) {
            MyLinkedList<Integer> clues = board.getColumnClues(col);
            
            // Center horizontally with the grid column
            int x = col * CELL_SIZE + CELL_SIZE / 2;
            int y = 15;
            
            // Draw each clue number vertically
            for (int i = 0; i < clues.size(); i++) {
                String clueStr = clues.get(i).toString();
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(clueStr);
                
                // Center the text horizontally
                g.drawString(clueStr, x - textWidth / 2, y + i * 18);
            }
        }
    }
}
