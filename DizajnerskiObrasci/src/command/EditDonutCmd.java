package command;

import geometry.Donut;
import geometry.Point;

public class EditDonutCmd implements Command {

	private Donut donut;
	private Donut newState;
	private Donut original = new Donut(new Point(), 0, 0);
	
	public EditDonutCmd(Donut donut, Donut newState) {
		this.donut = donut;
		this.newState = newState;
	}
	
	@Override
	public void execute() {
		original = donut.clone(original);
		donut = newState.clone(donut);
	}

	@Override
	public void unexecute() {
		donut = original.clone(donut);
	}

}
