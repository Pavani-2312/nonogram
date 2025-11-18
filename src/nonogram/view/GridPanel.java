package nonogram.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import nonogram.controller.GameController;
import nonogram.model.GameBoard;
import nonogram.model.Cell;
import nonogram.model.CellState;
import nonogram.datastructures.MyLinkedList;

public class GridPanel extends JPanel {
    private GameBoard board;
    private GameController controller;
    private int calculatedCellSize = 30;
    private int calculatedXOffset = 0;
    private int calculatedYOffset = 0;
    private int wrongRow = -1;
    private int wrongCol = -1;
    private int clueAreaWidth = 60;
    private int clueAreaHeight = 40;
    
    public GridPanel(GameBoard board, GameController controller) {
        this.board = board;
        this.controller = controller;
        setBackground(Color.WHITE);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = (e.getX() - calculatedXOffset) / calculatedCellSize;
                int row = (e.getY() - calculatedYOffset) / calculatedCellSize;
                
                if (row >= 0 && row < board.getRows() && col >= 0 && col < board.getCols()) {
                    controller.handleCellClick(row, col);
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        if (panelWidth == 0 || panelHeight == 0) return;
        
        // Calculate grid size with space for clues
        int availableWidth = panelWidth - clueAreaWidth;
        int availableHeight = panelHeight - clueAreaHeight;
        
        int cellSizeX = availableWidth / board.getCols();
        int cellSizeY = availableHeight / board.getRows();
        calculatedCellSize = (int)(Math.min(cellSizeX, cellSizeY) * 0.9); // Reduce by 10%
        
        int gridWidth = board.getCols() * calculatedCellSize;
        int gridHeight = board.getRows() * calculatedCellSize;
        calculatedXOffset = clueAreaWidth + (availableWidth - gridWidth) / 2;
        calculatedYOffset = clueAreaHeight + (availableHeight - gridHeight) / 2;
        
        // Draw clues
        drawRowClues(g);
        drawColumnClues(g);
        
        // Draw grid
        drawGrid(g);
    }
    
    private void drawRowClues(Graphics g) {
        int fontSize = Math.max(8, calculatedCellSize / 4);
        g.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        
        for (int row = 0; row < board.getRows(); row++) {
            MyLinkedList<Integer> clues = board.getRowClues(row);
            g.setColor(board.isRowCluesSatisfied(row, clues) ? Color.GRAY : Color.BLACK);
            
            StringBuilder clueText = new StringBuilder();
            for (int i = 0; i < clues.size(); i++) {
                if (i > 0) clueText.append(" ");
                clueText.append(clues.get(i));
            }
            
            FontMetrics fm = g.getFontMetrics();
            int x = calculatedXOffset - 5 - fm.stringWidth(clueText.toString());
            int y = calculatedYOffset + row * calculatedCellSize + calculatedCellSize / 2 + fm.getAscent() / 2;
            
            g.drawString(clueText.toString(), x, y);
        }
    }
    
    private void drawColumnClues(Graphics g) {
        int fontSize = Math.max(8, calculatedCellSize / 4);
        g.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        
        for (int col = 0; col < board.getCols(); col++) {
            MyLinkedList<Integer> clues = board.getColumnClues(col);
            g.setColor(board.isColumnCluesSatisfied(col, clues) ? Color.GRAY : Color.BLACK);
            
            int x = calculatedXOffset + col * calculatedCellSize + calculatedCellSize / 2;
            FontMetrics fm = g.getFontMetrics();
            int y = calculatedYOffset - 5;
            
            for (int i = clues.size() - 1; i >= 0; i--) {
                String clueStr = clues.get(i).toString();
                int textWidth = fm.stringWidth(clueStr);
                g.drawString(clueStr, x - textWidth / 2, y);
                y -= fm.getHeight();
            }
        }
    }
    
    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw cells
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                drawCell(g, row, col);
            }
        }
        
        // Draw grid lines
        g.setColor(Color.BLACK);
        Stroke defaultStroke = g2d.getStroke();
        Stroke thickStroke = new BasicStroke(2.0f);
        
        int gridWidth = board.getCols() * calculatedCellSize;
        int gridHeight = board.getRows() * calculatedCellSize;
        
        // Horizontal lines
        for (int i = 0; i <= board.getRows(); i++) {
            if (i % 5 == 0) g2d.setStroke(thickStroke);
            else g2d.setStroke(defaultStroke);
            g.drawLine(calculatedXOffset, calculatedYOffset + i * calculatedCellSize, 
                      calculatedXOffset + gridWidth, calculatedYOffset + i * calculatedCellSize);
        }
        
        // Vertical lines
        for (int i = 0; i <= board.getCols(); i++) {
            if (i % 5 == 0) g2d.setStroke(thickStroke);
            else g2d.setStroke(defaultStroke);
            g.drawLine(calculatedXOffset + i * calculatedCellSize, calculatedYOffset, 
                      calculatedXOffset + i * calculatedCellSize, calculatedYOffset + gridHeight);
        }
        g2d.setStroke(defaultStroke);
    }
    
    private void drawCell(Graphics g, int row, int col) {
        Cell cell = board.getCell(row, col);
        int x = calculatedXOffset + col * calculatedCellSize;
        int y = calculatedYOffset + row * calculatedCellSize;
        
        if (row == wrongRow && col == wrongCol) {
            g.setColor(Color.RED);
            g.fillRect(x + 1, y + 1, calculatedCellSize - 2, calculatedCellSize - 2);
            return;
        }
        
        switch (cell.getCurrentState()) {
            case UNKNOWN:
                g.setColor(Color.WHITE);
                g.fillRect(x + 1, y + 1, calculatedCellSize - 2, calculatedCellSize - 2);
                break;
            case FILLED:
                g.setColor(Color.BLACK);
                g.fillRect(x + 1, y + 1, calculatedCellSize - 2, calculatedCellSize - 2);
                break;
            case MARKED:
                g.setColor(Color.WHITE);
                g.fillRect(x + 1, y + 1, calculatedCellSize - 2, calculatedCellSize - 2);
                g.setColor(Color.RED);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setStroke(new BasicStroke(2.0f));
                int offset = calculatedCellSize / 4;
                g2d.drawLine(x + offset, y + offset, x + calculatedCellSize - offset, y + calculatedCellSize - offset);
                g2d.drawLine(x + offset, y + calculatedCellSize - offset, x + calculatedCellSize - offset, y + offset);
                g2d.dispose();
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
        return calculatedCellSize;
    }
    
    public int getXOffset() {
        return calculatedXOffset;
    }
    
    public int getYOffset() {
        return calculatedYOffset;
    }
}
