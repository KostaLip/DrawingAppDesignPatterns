package observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ButtonEnable {

	private PropertyChangeSupport propertyChangeSupport;
	private int shapeListSize = 0;
	private int selectedShapeListSize = 0;
	private int redoListSize = 0;
	private int undoListSize = 0;
	
	public ButtonEnable() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addShapeInList(int size) {
		propertyChangeSupport.firePropertyChange("addShape", this.shapeListSize, size);
		this.shapeListSize = size;
	}
	
	public void addShapeInSelectedList(int size) {
		propertyChangeSupport.firePropertyChange("selectedShape", this.selectedShapeListSize, size);
		this.selectedShapeListSize = size;
	}
	
	public void addRedoList(int size) {
		propertyChangeSupport.firePropertyChange("redo", this.redoListSize, size);
		this.redoListSize = size;
	}
	
	public void addUndoList(int size) {
		propertyChangeSupport.firePropertyChange("undo", this.undoListSize, size);
		this.undoListSize = size;
	}
	
	public void addListener(PropertyChangeListener propertyChangeListener) {
		this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}
	
	public void removeListener(PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
	}
	
}
