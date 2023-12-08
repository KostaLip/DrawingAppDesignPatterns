package command;

import java.util.ArrayList;

import geometry.Shape;
import mvc.DrawingModel;

public class DeselectShapeCmd implements Command {

	private Shape shape;
	private ArrayList<Shape> selectedShapeList = new ArrayList<Shape>();
	
	public DeselectShapeCmd(Shape shape, ArrayList<Shape> selectedShapeList) {
		this.shape = shape;
		this.selectedShapeList = selectedShapeList;
	}
	
	@Override
	public void execute() {
		shape.setSelected(false);
		selectedShapeList.remove(shape);
	}

	@Override
	public void unexecute() {
		shape.setSelected(true);
		selectedShapeList.add(shape);
	}

}
