package nonogram;

import nonogram.controller.GameController;
import nonogram.view.MainFrame;

import javax.swing.UIManager;

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
        
        controller.startNewGame();
        view.setVisible(true);
    }
}
