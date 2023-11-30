package command;

import mvc.DrawingController;

public class BringToFrontCmd implements Command {

	private DrawingController controller;
	
	public BringToFrontCmd(DrawingController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute() {
		controller.bringToFront(true);
	}

	@Override
	public void unexecute() {
		controller.bringToBack(true);
	}

}
