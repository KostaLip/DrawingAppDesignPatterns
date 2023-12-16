package command;

import java.util.Collections;

import geometry.Shape;
import mvc.DrawingController;
import mvc.DrawingModel;

public class BringToFrontCmd implements Command {

	private DrawingModel model;
	private Shape shape;
	private int index;
	
	public BringToFrontCmd(DrawingModel model, Shape shape) {
		this.model = model;
		this.shape = shape;
	}
	
	@Override
	public void execute() {
		int oldIndex = model.getShapes().lastIndexOf(shape);
		index = model.getShapes().size() - 1 - oldIndex;
		for(int i = index, j = model.getShapes().lastIndexOf(shape); i > 0; i--, j++) {
			Collections.swap(model.getShapes(), j, j + 1);
		}
	}

	@Override
	public void unexecute() {
		for(int i = index, j = model.getShapes().size() - 1; i > 0; i--, j--) {
			Collections.swap(model.getShapes(), j, j - 1);
		}
	}

}
