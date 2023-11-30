package command;

import mvc.DrawingController;

public class ToFrontCmd implements Command {

	private DrawingController controller;
	
	public ToFrontCmd(DrawingController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute() {
		controller.toFront(true);
	}

	@Override
	public void unexecute() {
		controller.toBack(true);
	}

}
