package nonogram;
import nonogram.controller.GameController;
import nonogram.view.MainFrame;
public class Main {
    public static void main(String[] args) {
        GameController controller = new GameController();
        MainFrame view = new MainFrame();
        controller.setView(view);
        view.setController(controller);
        controller.startNewGame();
        view.setVisible(true);
    }
}
