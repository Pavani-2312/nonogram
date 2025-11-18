package nonogram;

import nonogram.controller.GameController;
import nonogram.view.MainFrame;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        GameController controller = new GameController();
        MainFrame view = new MainFrame();
        
        controller.setView(view);
        view.setController(controller);
        
        view.setVisible(true);
        
        // Ensure UI is fully rendered before starting game
        SwingUtilities.invokeLater(() -> {
            controller.startNewGame();
            // Simulate clicking on Game menu to trigger UI refresh
            view.getJMenuBar().getMenu(0).doClick();
        });
    }
}
