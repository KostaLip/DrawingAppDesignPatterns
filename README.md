# Java 2D Paint Application

A feature-rich desktop drawing application built with Java Swing, demonstrating advanced Object-Oriented Programming principles and multiple design patterns. This project showcases a complete MVC architecture with undo/redo functionality, shape persistence, and interactive drawing capabilities.

## Project Overview

A professional-grade 2D graphics editor that allows users to create, modify, and manage geometric shapes with full command history and state management. Built as an academic project for Advanced Design Patterns course, extending the original Object-Oriented IT course project with enterprise-level features.

**Key Achievement:** Implemented 6+ design patterns (MVC, Command, Adapter, Strategy, Observer, Prototype) in a cohesive, production-ready application.

## Technologies Used

- **Java SE** - Core application development
- **Java Swing** - GUI framework
- **Java AWT** - 2D graphics rendering
- **Java Serialization** - Object persistence
- **hexagon.jar** - External library (integrated via Adapter pattern)

## Core Features

### Drawing & Graphics
- **Multiple Shape Types:** Point, Line, Rectangle, Circle, Donut (circle with hole), Hexagon
- **Color Management:** 
  - Separate edge and fill colors using JColorChooser
  - Visual color indicators with click-to-change functionality
  - Transparent inner area for donut shapes (using Graphics2D, Shape, Area, Ellipse2D)
- **Interactive Drawing:** Click-based shape creation with real-time preview

### Shape Manipulation
- **Selection:** Single and multi-select capabilities
- **Modification:** Edit shape properties (position, size, colors)
- **Deletion:** Remove selected shapes
- **Z-Axis Management:**
  - **To Front** - Move up one layer at a time
  - **To Back** - Move down one layer at a time
  - **Bring To Front** - Move to topmost layer instantly
  - **Bring To Back** - Move to bottom layer instantly

### Advanced Functionality
- **Undo/Redo:** Full command history with Command + Prototype patterns
- **Command Log:** Real-time display of all executed operations
- **Persistence:**
  - Save complete drawing (Serialization)
  - Export command log to text file
  - Load drawing from serialized file
  - Replay command log step-by-step
- **Smart UI:** Context-aware buttons using Observer pattern
  - Delete button enabled only when shapes are selected
  - Modify button enabled only with single shape selected
  - Undo/Redo buttons enabled based on command stack state

## Architecture & Design Patterns

### 1. MVC (Model-View-Controller)
**Purpose:** Separation of concerns and maintainability

- **Model:** `DrawingModel` - Manages shape collection and business logic
- **View:** `DrawingPanel` (JPanel) - Renders shapes and handles display
- **Controller:** `DrawingController` - Coordinates user interactions and model updates

### 2. Command Pattern
**Purpose:** Undo/Redo functionality and command logging

**Implementation:**
- All user actions encapsulated as Command objects
- Command stack for undo operations
- Separate stack for redo operations
- Each command implements `execute()` and `unexecute()`

```java
public interface Command {
    void execute();
    void unexecute();
}

public class AddShapeCommand implements Command {
    private DrawingModel model;
    private Shape shape;
    
    @Override
    public void execute() {
        model.addShape(shape);
    }
    
    @Override
    public void unexecute() {
        model.removeShape(shape);
    }
}
```

**Commands Implemented:**
- `AddShapeCommand` - Add new shapes
- `RemoveShapeCommand` - Delete shapes
- `ModifyShapeCommand` - Edit shape properties
- `SelectShapeCommand` - Select/deselect shapes
- `ToFrontCommand` - Z-axis reordering
- `ToBackCommand` - Z-axis reordering
- `BringToFrontCommand` - Z-axis reordering
- `BringToBackCommand` - Z-axis reordering

### 3. Prototype Pattern
**Purpose:** Deep cloning for undo/redo state preservation

**Implementation:**
- Each shape implements `Cloneable`
- Deep copy for undo operations
- Preserves complete shape state including colors

```java
public abstract class Shape implements Cloneable {
    @Override
    public Shape clone() {
        try {
            return (Shape) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
```

### 4. Adapter Pattern
**Purpose:** Integration of external hexagon.jar library

**Implementation:**
- Adapter class wraps external Hexagon class
- Converts interface to match application's shape hierarchy
- Enables seamless CRUD operations on hexagons

```java
public class HexagonAdapter extends Shape {
    private hexagon.Hexagon hexagon; // External library
    
    public HexagonAdapter(int x, int y, int radius) {
        this.hexagon = new hexagon.Hexagon(x, y, radius);
    }
    
    @Override
    public void draw(Graphics g) {
        hexagon.paint(g); // Delegate to external library
    }
    
    @Override
    public boolean contains(Point p) {
        return hexagon.doesContain(p.x, p.y);
    }
}
```

### 5. Strategy Pattern
**Purpose:** Flexible save/load mechanisms

**Implementation:**
- Different strategies for saving (Serialization vs. Log Export)
- Interchangeable at runtime
- Clean separation of persistence logic

```java
public interface SaveStrategy {
    void save(DrawingModel model, String path);
}

public class SerializationStrategy implements SaveStrategy {
    @Override
    public void save(DrawingModel model, String path) {
        // Serialize entire drawing
    }
}

public class LogExportStrategy implements SaveStrategy {
    @Override
    public void save(DrawingModel model, String path) {
        // Export command log as text
    }
}
```

### 6. Observer Pattern
**Purpose:** Context-sensitive UI updates

**Implementation:**
- Model notifies observers when selection changes
- Buttons automatically enable/disable based on state
- Decoupled UI updates

```java
public class DrawingModel extends Observable {
    public void setSelectedShapes(List<Shape> shapes) {
        this.selectedShapes = shapes;
        setChanged();
        notifyObservers();
    }
}

public class DeleteButton extends JButton implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        DrawingModel model = (DrawingModel) o;
        setEnabled(!model.getSelectedShapes().isEmpty());
    }
}
```

## Command Logging System

### Features
- **Real-time Log Display:** All actions shown in scrollable text area
- **Persistent Storage:** Export log to `.txt` file
- **Command Replay:** Load and execute commands step-by-step
- **Comprehensive Logging:** Captures all operations including:
  - Shape creation (with all properties)
  - Shape modification (before/after states)
  - Shape deletion
  - Selection/deselection
  - Z-axis reordering
  - Undo/redo operations

### Log Format Example
```
ADD: Point(100, 150) - Edge Color: RGB(255,0,0)
SELECT: Rectangle(50, 50, 100, 80)
MODIFY: Circle(200, 200, 50) -> Circle(200, 200, 75)
TO FRONT: Line from (10,10) to (50,50)
UNDO: MODIFY Circle
DELETE: Point(100, 150)
REDO: MODIFY Circle
```

## Persistence Mechanisms

### 1. Complete Drawing Serialization
- Saves entire drawing state as binary file
- Preserves all shapes with properties
- Quick load for complete restoration

### 2. Command Log Export
- Text-based format for human readability
- Version control friendly
- Enables command replay functionality

### 3. Command Replay
- Load log file
- Execute commands one-by-one with user confirmation
- Recreate drawing from scratch based on history

## Shape Hierarchy

```
Shape (abstract)
├── Point
├── Line
├── SurfaceShape (abstract)
│   ├── Rectangle
│   ├── Circle
│   └── Donut (Circle with transparent hole)
└── HexagonAdapter (wraps external hexagon.jar)
```

**Shape Properties:**
- Position (x, y coordinates)
- Edge color
- Fill color (for surface shapes)
- Selected state
- Drawing order (z-index)

## User Interaction Flow

### Drawing Mode
1. Select shape type from toolbar
2. Choose edge and fill colors
3. Click on canvas to place shape
4. Shape appears with selected properties

### Modification Mode
1. Select single shape by clicking
2. Click "Modify" button
3. Dialog opens with current properties
4. Change values and confirm
5. Shape updates while maintaining selection

### Z-Axis Reordering
1. Select shape(s)
2. Use Z-axis buttons:
   - **To Front/Back:** Incremental movement
   - **Bring To Front/Back:** Instant placement

### Undo/Redo
1. Undo reverts last action
2. Redo restores undone action
3. New action clears redo stack

## Getting Started

### Prerequisites
- Java JDK 8 or higher
- hexagon.jar (included in `/lib` folder)

### Running the Application

### Using an IDE (Eclipse/IntelliJ)
1. Import project
2. Add hexagon.jar to build path
3. Run `Main.java`

### Course Requirements Met
- MVC Architecture
- 6 Design Patterns Implemented
- Java Swing GUI
- External Library Integration (Adapter)
- Complete Undo/Redo System
- Command Logging & Replay
- Serialization & Persistence
- Observer-based UI Updates
- Git Workflow with Meaningful Commits

### Development Process
- **Version Control:** GitHub with incremental commits
- **Code Quality:** English naming conventions throughout
- **Architecture:** Strict MVC separation
- **Testing:** Manual testing of all features
- **Documentation:** Comprehensive inline comments

## Key Learning Points

- **Design Patterns:** Practical application of 6+ patterns in a cohesive system
- **MVC Architecture:** Clean separation of concerns without Singleton anti-pattern
- **Java Swing:** Complex GUI development with custom painting
- **Command Pattern:** Implementing undo/redo with command stacks
- **Serialization:** Object persistence and state management
- **Adapter Pattern:** Integrating external libraries seamlessly
- **Observer Pattern:** Event-driven programming for UI updates
- **Git Workflow:** Professional version control practices

## Technical Highlights

### Transparent Donut Shape
```java
public class Donut extends Circle {
    private int innerRadius;
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Create outer circle
        Ellipse2D outer = new Ellipse2D.Double(
            x - radius, y - radius, 
            2 * radius, 2 * radius
        );
        
        // Create inner circle (hole)
        Ellipse2D inner = new Ellipse2D.Double(
            x - innerRadius, y - innerRadius,
            2 * innerRadius, 2 * innerRadius
        );
        
        // Subtract inner from outer for transparency
        Area area = new Area(outer);
        area.subtract(new Area(inner));
        
        // Fill and draw
        g2d.setColor(fillColor);
        g2d.fill(area);
        g2d.setColor(edgeColor);
        g2d.draw(area);
    }
}
```

### Smart Button Management (Observer)
```java
public void updateButtons() {
    int selectedCount = model.getSelectedShapes().size();
    
    deleteButton.setEnabled(selectedCount > 0);
    modifyButton.setEnabled(selectedCount == 1);
    toFrontButton.setEnabled(selectedCount > 0);
    toBackButton.setEnabled(selectedCount > 0);
    
    undoButton.setEnabled(!commandStack.isEmpty());
    redoButton.setEnabled(!redoStack.isEmpty());
}
```

## Future Enhancements

- Add text shapes with font customization
- Implement layer system for complex drawings
- Add zoom and pan functionality
- Export to image formats (PNG, JPG)
- Add shape grouping functionality
- Implement snapping to grid
- Add keyboard shortcuts
- Create plugin system for custom shapes
