package command;

import java.util.ArrayList;

import geometry.Shape;
import mvc.DrawingModel;

public class DeselectShapeCmd implements Command {

	private DrawingModel model;
	private int index;
	
	public DeselectShapeCmd(DrawingModel model, int index) {
		this.model = model;
		this.index = index;
	}
	
	@Override
	public void execute() {
		model.getShapes().get(index).setSelected(false);
	}

	@Override
	public void unexecute() {
		model.getShapes().get(index).setSelected(true);
	}

}
