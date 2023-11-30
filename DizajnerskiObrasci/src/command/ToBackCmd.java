package command;

import mvc.DrawingController;

public class ToBackCmd implements Command {

	private DrawingController controller;
	
	public ToBackCmd(DrawingController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute() {
		controller.toBack(true);
	}

	@Override
	public void unexecute() {
		controller.toFront(true);
	}

}
