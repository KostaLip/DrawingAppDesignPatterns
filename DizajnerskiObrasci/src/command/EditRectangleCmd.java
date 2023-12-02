package command;

import geometry.Point;
import geometry.Rectangle;

public class EditRectangleCmd implements Command {

	private Rectangle rectangle;
	private Rectangle newState;
	private Rectangle original = new Rectangle(new Point(), 0, 0);
	
	public EditRectangleCmd(Rectangle rectangle, Rectangle newState) {
		this.rectangle = rectangle;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		original = rectangle.clone(original);
		rectangle = newState.clone(rectangle);
	}

	@Override
	public void unexecute() {
		rectangle = original.clone(rectangle);
	}

}
