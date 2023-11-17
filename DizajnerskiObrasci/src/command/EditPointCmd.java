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
		original.setX(point.getX());
		original.setY(point.getY());
		original.setColor(point.getColor());
		
		point.setX(newState.getX());
		point.setY(newState.getY());
		point.setColor(newState.getColor());
	}

	@Override
	public void unexecute() {
		point.setX(original.getX());
		point.setY(original.getY());
		point.setColor(original.getColor());
	}

}
