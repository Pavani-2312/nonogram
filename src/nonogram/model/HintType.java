package nonogram.model;

public enum HintType {
    COMPLETE_LINE("Complete Line", "This line can be completely filled based on its clue."),
    EDGE_DEDUCTION("Edge Deduction", "Cells at the edges can be determined from the clues."),
    OVERLAP_ANALYSIS("Overlap Analysis", "Multiple arrangements of clues overlap at certain cells."),
    SIMPLE_PATTERN("Simple Pattern", "A basic pattern can be identified in this line."),
    ERROR_DETECTION("Error Detection", "There's an error in your current solution.");
    
    private final String name;
    private final String description;
    
    HintType(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}
