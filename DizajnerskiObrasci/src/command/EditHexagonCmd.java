package command;

import adapter.HexagonAdapter;
import hexagon.Hexagon;

public class EditHexagonCmd implements Command {

	private HexagonAdapter hexagon;
	private HexagonAdapter newState;
	private HexagonAdapter original = new HexagonAdapter(new Hexagon(0, 0, 0));
	
	public EditHexagonCmd(HexagonAdapter hexagon, HexagonAdapter newState) {
		this.hexagon = hexagon;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original = hexagon.clone(original);
		hexagon = newState.clone(hexagon);
	}

	@Override
	public void unexecute() {
		hexagon = original.clone(hexagon);

	}

}
