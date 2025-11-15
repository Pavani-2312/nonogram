package nonogram;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        
        SwingUtilities.invokeLater(() -> {
            JFrame selectionFrame = new JFrame("Nonogram - Select Puzzle");
            selectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            selectionFrame.setSize(600, 500);
            selectionFrame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
            
            JLabel title = new JLabel("Choose a Puzzle");
            title.setFont(new Font("Arial", Font.BOLD, 32));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(title);
            
            panel.add(Box.createVerticalStrut(50));
            
            String[] puzzles = {"Heart Shape", "Smiley Face", "Cross Shape", "Square Shape"};
            
            for (int i = 0; i < puzzles.length; i++) {
                final int index = i;
                JButton button = new JButton(puzzles[i]);
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setAlignmentX(Component.CENTER_ALIGNMENT);
                button.setMaximumSize(new Dimension(300, 60));
                
                button.addActionListener(e -> {
                    selectionFrame.dispose();
                    // Only create GameController when puzzle is selected
                    SwingUtilities.invokeLater(() -> {
                        try {
                            Class<?> controllerClass = Class.forName("nonogram.controller.GameController");
                            Object controller = controllerClass.newInstance();
                            controllerClass.getMethod("startGameWithPuzzleIndex", int.class).invoke(controller, index);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                });
                
                panel.add(button);
                panel.add(Box.createVerticalStrut(20));
            }
            
            selectionFrame.add(panel);
            selectionFrame.setVisible(true);
        });
    }
}
