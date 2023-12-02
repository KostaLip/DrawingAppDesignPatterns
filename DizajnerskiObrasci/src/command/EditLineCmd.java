package command;

import geometry.Point;

import geometry.Line;

public class EditLineCmd implements Command {

	private Line line = new Line(new Point(), new Point());
	private Line newState =  new Line(new Point(), new Point());
	private Line original = new Line(new Point(), new Point());
	
	public EditLineCmd(Line line, Line newState) {
		this.line = line;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		original = line.clone(original);
		line= newState.clone(line);
	}

	@Override
	public void unexecute() {
		line = original.clone(line);
	}

}
