package command;

import mvc.DrawingModel;

public class SelectShapeCmd implements Command {

	private DrawingModel model;
	private int index;
	
	public  SelectShapeCmd(DrawingModel model, int index) {
		this.model = model;
		this.index = index;
	}
	
	@Override
	public void execute() {
		model.getShapes().get(index).setSelected(true);
	}

	@Override
	public void unexecute() {
		model.getShapes().get(index).setSelected(false);
	}

}
