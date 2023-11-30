package command;

import mvc.DrawingController;

public class BringToBackCmd implements Command {

	private DrawingController controller;
	
	public BringToBackCmd(DrawingController controller) {
		this.controller = controller;
	}
	
	@Override
	public void execute() {
		controller.bringToBack(true);
	}

	@Override
	public void unexecute() {
		controller.bringToFront(true);
	}

}
