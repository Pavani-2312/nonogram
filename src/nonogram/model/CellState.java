package nonogram.model;

public enum CellState {
    UNKNOWN,    // Initial state
    FILLED,     // Player believes cell should be black
    MARKED      // Player believes cell should be empty (X mark)
}
