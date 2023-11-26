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
		original.setCenterX(hexagon.getCenterX());
		original.setCenterY(hexagon.getCenterY());
		original.setRadius(hexagon.getRadius());
		original.setColor(hexagon.getColor());
		original.setInnerColor(hexagon.getInnerColor());
		
		hexagon.setCenterX(newState.getCenterX());
		hexagon.setCenterY(newState.getCenterY());
		hexagon.setRadius(newState.getRadius());
		hexagon.setColor(newState.getColor());
		hexagon.setInnerColor(newState.getInnerColor());
	}

	@Override
	public void unexecute() {
		hexagon.setCenterX(original.getCenterX());
		hexagon.setCenterY(original.getCenterY());
		hexagon.setRadius(original.getRadius());
		hexagon.setColor(original.getColor());
		hexagon.setInnerColor(original.getInnerColor());

	}

}
