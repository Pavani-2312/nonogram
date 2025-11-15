# Project Structure Documentation

## Complete Directory Layout

```
nonogram/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── datastructures/
│   │       │   ├── MyArrayList.java
│   │       │   ├── MyStack.java
│   │       │   ├── MyQueue.java
│   │       │   └── MyHashMap.java
│   │       ├── model/
│   │       │   ├── CellState.java (enum)
│   │       │   ├── Cell.java
│   │       │   ├── GameBoard.java
│   │       │   ├── Clue.java
│   │       │   ├── Puzzle.java
│   │       │   ├── Difficulty.java (enum)
│   │       │   ├── Move.java
│   │       │   ├── GameState.java
│   │       │   ├── Hint.java
│   │       │   ├── HintType.java (enum)
│   │       │   ├── PuzzleStats.java
│   │       │   └── CellPosition.java
│   │       ├── view/
│   │       │   ├── GameWindow.java
│   │       │   ├── GamePanel.java
│   │       │   ├── GridPanel.java
│   │       │   ├── CluePanel.java
│   │       │   ├── ControlPanel.java
│   │       │   ├── MenuBar.java
│   │       │   ├── HintDialog.java
│   │       │   ├── StatisticsDialog.java
│   │       │   └── SettingsDialog.java
│   │       ├── controller/
│   │       │   ├── GameController.java
│   │       │   ├── PuzzleController.java
│   │       │   ├── HintController.java
│   │       │   ├── StatisticsController.java
│   │       │   ├── FileController.java
│   │       │   └── ValidationController.java
│   │       ├── algorithms/
│   │       │   ├── SolvingAlgorithms.java
│   │       │   ├── HintGenerator.java
│   │       │   ├── ValidationEngine.java
│   │       │   ├── ClueGenerator.java
│   │       │   └── PuzzleAnalyzer.java
│   │       └── utils/
│   │           ├── FileUtils.java
│   │           ├── TimeUtils.java
│   │           ├── ColorUtils.java
│   │           └── Constants.java
│   └── test/
│       └── java/
│           ├── datastructures/
│           │   ├── MyArrayListTest.java
│           │   ├── MyStackTest.java
│           │   ├── MyQueueTest.java
│           │   └── MyHashMapTest.java
│           ├── model/
│           │   ├── CellTest.java
│           │   ├── GameBoardTest.java
│           │   ├── PuzzleTest.java
│           │   ├── GameStateTest.java
│           │   └── MoveTest.java
│           ├── controller/
│           │   ├── GameControllerTest.java
│           │   ├── PuzzleControllerTest.java
│           │   └── ValidationControllerTest.java
│           ├── algorithms/
│           │   ├── SolvingAlgorithmsTest.java
│           │   ├── HintGeneratorTest.java
│           │   └── ValidationEngineTest.java
│           └── integration/
│               ├── GameFlowTest.java
│               ├── PuzzleLoadingTest.java
│               └── StatisticsTest.java
├── resources/
│   ├── puzzles/
│   │   ├── easy/
│   │   │   ├── heart.puzzle
│   │   │   ├── star.puzzle
│   │   │   └── cross.puzzle
│   │   ├── medium/
│   │   │   ├── tree.puzzle
│   │   │   ├── cat.puzzle
│   │   │   └── flower.puzzle
│   │   ├── hard/
│   │   │   ├── dragon.puzzle
│   │   │   ├── castle.puzzle
│   │   │   └── portrait.puzzle
│   │   └── expert/
│   │       ├── cityscape.puzzle
│   │       └── detailed_face.puzzle
│   ├── images/
│   │   ├── icons/
│   │   │   ├── undo.png
│   │   │   ├── redo.png
│   │   │   ├── hint.png
│   │   │   └── check.png
│   │   └── backgrounds/
│   │       ├── light_theme.png
│   │       └── dark_theme.png
│   └── config/
│       ├── default_settings.properties
│       ├── themes.properties
│       └── keybindings.properties
├── docs/
│   └── (existing and new documentation files)
├── build/
│   ├── classes/
│   │   ├── main/
│   │   └── test/
│   ├── resources/
│   └── nonogram.jar (final executable)
└── saves/
    ├── game_saves/
    └── statistics/
```

## Package Organization

### Core Packages

#### `datastructures` Package
**Purpose**: Custom data structure implementations
**Dependencies**: None (foundational package)
**Key Classes**:
- MyArrayList: Dynamic array implementation
- MyStack: LIFO stack implementation  
- MyQueue: FIFO queue implementation
- MyHashMap: Hash table implementation

#### `model` Package
**Purpose**: Game logic and data representation
**Dependencies**: datastructures
**Key Classes**:
- Cell: Individual grid cell with state
- GameBoard: Complete puzzle grid
- Puzzle: Puzzle definition with solution
- GameState: Game session tracking
- Move: Action recording for undo/redo

#### `algorithms` Package
**Purpose**: Game logic algorithms
**Dependencies**: model, datastructures
**Key Classes**:
- SolvingAlgorithms: Player solving strategies
- HintGenerator: Intelligent hint creation
- ValidationEngine: Solution validation
- ClueGenerator: Solution to clue conversion

#### `controller` Package
**Purpose**: User interaction and game flow
**Dependencies**: model, algorithms, datastructures
**Key Classes**:
- GameController: Main game flow control
- PuzzleController: Puzzle loading and management
- HintController: Hint system management

#### `view` Package
**Purpose**: User interface components
**Dependencies**: model, controller
**Key Classes**:
- GameWindow: Main application window
- GamePanel: Central game display
- GridPanel: Puzzle grid visualization
- ControlPanel: Game controls interface

#### `utils` Package
**Purpose**: Helper utilities and constants
**Dependencies**: None
**Key Classes**:
- Constants: Application-wide constants
- FileUtils: File operation helpers
- TimeUtils: Time formatting utilities

## File Naming Conventions

### Class Files
- **Classes**: PascalCase (GameBoard.java)
- **Enums**: PascalCase (CellState.java)
- **Interfaces**: PascalCase with descriptive names
- **Test Classes**: ClassNameTest.java format

### Resource Files
- **Puzzle Files**: lowercase with .puzzle extension
- **Image Files**: lowercase with underscores
- **Configuration**: lowercase with .properties extension

### Directory Names
- **Packages**: lowercase, single words when possible
- **Resource Directories**: lowercase, descriptive names
- **Build Directories**: lowercase, standard names

## Dependency Hierarchy

### Layer Dependencies (Top to Bottom)
1. **View Layer** → Controller Layer
2. **Controller Layer** → Model Layer + Algorithms Layer
3. **Algorithms Layer** → Model Layer
4. **Model Layer** → Data Structures Layer
5. **Data Structures Layer** → No dependencies

### Dependency Rules
- **No Circular Dependencies**: Higher layers cannot depend on lower layers
- **Interface Separation**: View should not directly access Model
- **Utility Access**: All layers can access utils package
- **Test Independence**: Test classes only depend on classes they test

## Build Output Structure

### Compilation Output
- **Main Classes**: `build/classes/main/`
- **Test Classes**: `build/classes/test/`
- **Resources**: `build/resources/`

### Packaging Structure
- **JAR File**: `build/nonogram.jar`
- **Manifest**: Includes main class and classpath
- **Resources**: Embedded in JAR for distribution

## Resource Management

### Puzzle Files
- **Format**: Custom text format with .puzzle extension
- **Organization**: Grouped by difficulty level
- **Naming**: Descriptive names (heart.puzzle, dragon.puzzle)

### Image Resources
- **Icons**: Small UI elements (16x16, 24x24 pixels)
- **Backgrounds**: Theme-related images
- **Format**: PNG for transparency support

### Configuration Files
- **Settings**: User preferences and defaults
- **Themes**: Color schemes and UI customization
- **Key Bindings**: Keyboard shortcut mappings

## Development Workflow Structure

### Source Control Organization
- **Main Branch**: Stable, working code
- **Feature Branches**: Individual component development
- **Ignore Patterns**: Build outputs, IDE files, temporary files

### Testing Structure
- **Unit Tests**: One test class per main class
- **Integration Tests**: Cross-component testing
- **Test Resources**: Separate test data and configurations

### Documentation Integration
- **Implementation Guides**: Linked to specific packages
- **API Documentation**: Generated from source comments
- **Architecture Diagrams**: Visual representation of structure

## Configuration Management

### Build Configuration
- **Source Paths**: Clearly defined source directories
- **Classpath**: Proper dependency resolution
- **Output Paths**: Organized build artifacts

### Runtime Configuration
- **Resource Loading**: Proper resource path resolution
- **File Locations**: Consistent file access patterns
- **Settings Management**: Centralized configuration handling

## Scalability Considerations

### Package Extensibility
- **Plugin Architecture**: Potential for additional algorithms
- **Theme System**: Expandable UI customization
- **Puzzle Formats**: Support for multiple puzzle file formats

### Maintenance Structure
- **Clear Separation**: Easy to modify individual components
- **Test Coverage**: Comprehensive testing for safe refactoring
- **Documentation**: Up-to-date implementation guides

## Verification Checklist

### Structure Verification
- [ ] All package directories created
- [ ] Proper directory hierarchy established
- [ ] Resource directories organized
- [ ] Build directories prepared

### Naming Verification
- [ ] Consistent naming conventions applied
- [ ] No naming conflicts between packages
- [ ] Clear, descriptive file names
- [ ] Proper extension usage

### Dependency Verification
- [ ] No circular dependencies planned
- [ ] Clear dependency hierarchy
- [ ] Proper separation of concerns
- [ ] Interface boundaries defined

---

**Next Step**: Review [Implementation Order](Implementation_Order.md) to understand the development sequence for this structure.
