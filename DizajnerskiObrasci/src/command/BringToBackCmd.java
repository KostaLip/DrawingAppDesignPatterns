package command;

import java.util.ArrayList;
import java.util.Collections;

import geometry.Shape;
import mvc.DrawingController;
import mvc.DrawingModel;

public class BringToBackCmd implements Command {

	private Shape selectedShape;
	private int oldIndex;
	private DrawingModel model;

	public BringToBackCmd(DrawingModel model, Shape shape) {
		this.model = model;
		this.selectedShape = shape;
	}

	@Override
	public void execute() {
		oldIndex = model.getShapes().lastIndexOf(selectedShape);
		for(int i = oldIndex; i > 0; i--) {
			Collections.swap(model.getShapes(), i, i - 1);
		}
	}

	@Override
	public void unexecute() {
		for(int i = oldIndex, j = 0; i > 0; i--, j++) {
			Collections.swap(model.getShapes(), j, j + 1);
		}
	}
}
