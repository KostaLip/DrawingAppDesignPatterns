package observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import mvc.DrawingFrame;
import mvc.DrawingModel;

public class ButtonEnableUpdate implements PropertyChangeListener {

	private int shapeListSize = 0;
	private int selectedShapeListSize = 0;
	private int redoListSize = 0;
	private int undoListSize = 0;
	private int position = 0;
	private DrawingFrame frame;
	private DrawingModel model;

	public ButtonEnableUpdate(DrawingFrame frame, DrawingModel model) {
		this.frame = frame;
		this.model = model;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("addShape")) {
			this.shapeListSize = (int) evt.getNewValue();
			checkBtnEnable();
			checkBtnSelectedEnable();
			checkBtnRedo();
			checkBtnUndo();
		} else if (evt.getPropertyName().equals("selectedShape")) {
			this.selectedShapeListSize = (int) evt.getNewValue();
			checkBtnEnable();
			checkBtnSelectedEnable();
			checkBtnRedo();
			checkBtnUndo();
		} else if (evt.getPropertyName().equals("redo")) {
			this.redoListSize = (int) evt.getNewValue();
			checkBtnEnable();
			checkBtnSelectedEnable();
			checkBtnRedo();
			checkBtnUndo();
		} else if (evt.getPropertyName().equals("undo")) {
			this.undoListSize = (int) evt.getNewValue();
			checkBtnEnable();
			checkBtnSelectedEnable();
			checkBtnRedo();
			checkBtnUndo();
		}
	}

	private void checkBtnUndo() {
		if (undoListSize == 0) {
			frame.btnUndo.setEnabled(false);
		} else if (undoListSize > 0) {
			frame.btnUndo.setEnabled(true);
		}
	}

	private void checkBtnRedo() {
		if (redoListSize == 0) {
			frame.btnRedo.setEnabled(false);
		} else if (redoListSize > 0) {
			frame.btnRedo.setEnabled(true);
		}
	}

	private void checkBtnSelectedEnable() {
		if (selectedShapeListSize == 0) {
			frame.btnDelete.setEnabled(false);
			frame.btnEdit.setEnabled(false);
			frame.btnBringToBack.setEnabled(false);
			frame.btnBringToFront.setEnabled(false);
			frame.btnToBack.setEnabled(false);
			frame.btnToFront.setEnabled(false);
		} else if (selectedShapeListSize == 1) {
			frame.btnDelete.setEnabled(true);
			frame.btnEdit.setEnabled(true);
			frame.btnUndo.setEnabled(true);
			frame.btnRedo.setEnabled(true);
			if(shapeListSize > 1) {
				frame.btnToBack.setEnabled(true);
				frame.btnToFront.setEnabled(true);
				frame.btnBringToBack.setEnabled(true);
				frame.btnBringToFront.setEnabled(true);
			}
		} else if (selectedShapeListSize > 1) {
			frame.btnDelete.setEnabled(true);
			frame.btnEdit.setEnabled(false);
			frame.btnBringToBack.setEnabled(false);
			frame.btnBringToFront.setEnabled(false);
			frame.btnUndo.setEnabled(true);
			frame.btnRedo.setEnabled(true);
			frame.btnToBack.setEnabled(false);
			frame.btnToFront.setEnabled(false);
		}
	}

	private void checkBtnEnable() {
		if (shapeListSize == 0) {
			frame.btnDelete.setEnabled(false);
			frame.btnEdit.setEnabled(false);
			frame.tglBtnSelect.setEnabled(false);
			frame.tglBtnSelect.setSelected(false);
			frame.btnBringToBack.setEnabled(false);
			frame.btnBringToFront.setEnabled(false);
			frame.btnUndo.setEnabled(false);
			frame.btnRedo.setEnabled(false);
			frame.btnToBack.setEnabled(false);
			frame.btnToFront.setEnabled(false);
		} else if (shapeListSize > 0) {
			frame.btnDelete.setEnabled(true);
			frame.btnEdit.setEnabled(true);
			frame.tglBtnSelect.setEnabled(true);
			frame.btnUndo.setEnabled(true);
			frame.btnRedo.setEnabled(true);
			if (shapeListSize > 1) {
				frame.btnToBack.setEnabled(true);
				frame.btnToFront.setEnabled(true);
				frame.btnBringToBack.setEnabled(true);
				frame.btnBringToFront.setEnabled(true);
			}
		}
	}

}
