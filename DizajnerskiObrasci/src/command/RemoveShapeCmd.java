package command;

import geometry.Shape;
import mvc.DrawingModel;

public class RemoveShapeCmd implements Command {

	private Shape shape;
	private DrawingModel model;
	private int index;
	
	public RemoveShapeCmd(Shape shape, DrawingModel model, int index) {
		this.shape = shape;
		this.model = model;
		this.index = index;
	}

	@Override
	public void execute() {
		model.remove(shape);
	}

	@Override
	public void unexecute() {
		model.getShapes().add(index,shape);
	}
	
}
