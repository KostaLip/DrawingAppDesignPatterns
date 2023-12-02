package command;

import geometry.Point;
import geometry.Shape;

public class EditPointCmd implements Command {

	private Point point;
	private Point newState;
	private Point original = new Point();
	
	public EditPointCmd() {
		
	}
	
	public EditPointCmd(Point point, Point newState) {
		this.point = point;
		this.newState = newState;
		
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	
	public void setNewState(Point newState) {
		this.newState = newState;
	}
	
	public void setOriginal(Point original) {
		this.original = original;
	}
	
	@Override
	public void execute() {
		original = point.clone(original);
		point = newState.clone(point);
	}

	@Override
	public void unexecute() {
		point = original.clone(point);
	}

}
