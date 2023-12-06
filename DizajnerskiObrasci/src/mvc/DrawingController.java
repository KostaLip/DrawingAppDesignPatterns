package mvc;

import java.awt.Color;
import java.awt.color.CMMException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import adapter.HexagonAdapter;
import command.AddShapeCmd;
import command.BringToBackCmd;
import command.BringToFrontCmd;
import command.Command;
import command.DeselectShapeCmd;
import command.EditCircleCmd;
import command.EditDonutCmd;
import command.EditHexagonCmd;
import command.EditLineCmd;
import command.EditPointCmd;
import command.EditRectangleCmd;
import command.RemoveShapeCmd;
import command.SelectShapeCmd;
import command.ToBackCmd;
import command.ToFrontCmd;
import dialogs.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import hexagon.Hexagon;
import observer.ButtonEnable;
import observer.ButtonEnableUpdate;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgDonut;
import dialogs.DlgHexagon;
import dialogs.DlgCircle;

public class DrawingController {

	private ButtonEnable btnEnable;
	private ButtonEnableUpdate btnEnableUpdate;

	private ToFrontCmd tfc = new ToFrontCmd(this);
	private ToBackCmd tbc = new ToBackCmd(this);
	private BringToFrontCmd btfc = new BringToFrontCmd(this);
	private BringToBackCmd btbc = new BringToBackCmd(this);

	DrawingModel model;
	DrawingFrame frame;

	private Point startPoint;
	private Point endPoint;

	Shape selectedShape;

	// UNDO I REDO ZA EDIT :)
	private Stack<Command> editCommands = new Stack<Command>();
	private Stack<Command> tempEditCommands = new Stack<Command>();

	private String command;
	private String drawCommand;
	private ArrayList<String> tempCommands = new ArrayList<String>();
	private ArrayList<String> commands = new ArrayList<String>();
	private Stack<Shape> deletedShapes = new Stack<Shape>();
	private Stack<Shape> tempShapes = new Stack<Shape>();
	private Stack<Integer> indexs = new Stack<Integer>();
	private Stack<Shape> tempDeletedShapes = new Stack<Shape>();
	
	private ArrayList<Shape> deselectedShapesList = new ArrayList<Shape>();
	private ArrayList<Shape> selectedShapesList = new ArrayList<Shape>();
	private Map<String, Integer> indexOfSelected = new HashMap<String, Integer>();

	private Stack<Shape> oldStates = new Stack<Shape>();
	private Stack<Shape> newStates = new Stack<Shape>();

	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
		this.btnEnable = new ButtonEnable();
		this.btnEnableUpdate = new ButtonEnableUpdate(this.frame, this.model);
		btnEnable.addListener(btnEnableUpdate);
	}

	public void setPointsToNull() {
		this.startPoint = null;
		this.endPoint = null;
	}

	public void deselect() {
		setAllFalse();
		frame.repaint();
	}

	public void setAllFalse() {
		ArrayList<Shape> shapes = model.getShapes();
		for (int br = shapes.size() - 1; br >= 0; br--) {
			shapes.get(br).setSelected(false);
			frame.repaint();
		}
		selectedShape = null;
	}

	public void deleteSelectedShape() {
		setPointsToNull();
		if (model.getShapes().size() == 0) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO DRAWN SHAPES", "ERROR", JOptionPane.ERROR_MESSAGE);
		} else if (selectedShapesList.size() == 0) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR", JOptionPane.ERROR_MESSAGE);
		} else {
			int option = JOptionPane.showConfirmDialog(null, "DO YOU WANT TO DELETE SELECTED SHAPES");
			if (option == JOptionPane.YES_OPTION) {
				for (int i = selectedShapesList.size() - 1; i >= 0; i--) {
					deletedShapes.push(selectedShapesList.get(i));
					command = "Deleted!" + selectedShapesList.get(i) + "\n";
					frame.commandList.append(command);
					commands.add(command);
					indexs.push(model.getShapes().indexOf(selectedShapesList.get(i)));
					model.getShapes().remove(selectedShapesList.get(i));
					selectedShapesList.remove(selectedShapesList.size() - 1);
				}
				frame.repaint();
			}
			tempCommands.clear();
			btnEnable.addShapeInList(model.getShapes().size());
			btnEnable.addShapeInSelectedList(selectedShapesList.size());
			btnEnable.addRedoList(tempCommands.size());
			btnEnable.addUndoList(commands.size());
		}
	}

	public void select(int x, int y) {
		boolean blank = false;
		boolean go = true;
		ArrayList<Shape> shapes = model.getShapes();
		for (int br = shapes.size() - 1; br >= 0; br--) {
			frame.repaint();
			if (shapes.get(br).contains(x, y) && go) {
				if (!selectedShapesList.contains(shapes.get(br))) {
					shapes.get(br).setSelected(true);
					selectedShapesList.add(shapes.get(br));
					indexOfSelected.put(shapes.get(br).toString(), selectedShapesList.indexOf(shapes.get(br)));
					selectedShape = shapes.get(br);
					blank = true;
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
				} else {
					Shape deselectedShape = shapes.get(br);
					deselectedShape.setSelected(false);
					deselectedShapesList.add(deselectedShape);
					frame.commandList.append("DESELECTED!" + deselectedShape + "\n");
					commands.add("DESELECTED!" + deselectedShape);
					selectedShapesList.remove(deselectedShape);
					frame.repaint();
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
				}
				go = false;
				frame.repaint();
			}
		}
		/*
		 * if (!blank && selectedShape != null) { for (int i = shapes.size() - 1; i >=
		 * 0; i--) { shapes.get(i).setSelected(false); } for (int i =
		 * selectedShapesList.size() - 1; i >= 0; i--) { // commands.add("DESELECTED!" +
		 * selectedShapesList.get(i)); frame.commandList.append("DESELECTED!" +
		 * selectedShapesList.get(i) + "\n"); } selectedShapesList.clear(); }
		 */
		if (blank) {
			commands.add("SELECTED!" + selectedShape);
			frame.commandList.append("SELECTED!" + selectedShape + "\n");
		}
		frame.repaint();
	}

	public void editSelectedShape() {
		setPointsToNull();
		if (model.getShapes().size() == 0) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO DRAWN SHAPES", "ERROR", JOptionPane.ERROR_MESSAGE);
		} else if (selectedShapesList.size() == 0) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR", JOptionPane.ERROR_MESSAGE);
		} else if (selectedShapesList.size() == 1) {
			Shape selectedShape = selectedShapesList.get(0);
			if (selectedShape instanceof Point) {
				Point p = (Point) selectedShape;
				Point newPoint = new Point();
				Color pointColor = p.getColor();
				DlgPoint dlgPoint = new DlgPoint(pointColor);
				dlgPoint.txtX.setText(Integer.toString(p.getX()));
				dlgPoint.txtY.setText(Integer.toString(p.getY()));
				dlgPoint.btnColor.setBackground(p.getColor());
				String oldState = p.toString();
				oldStates.push(p);
				dlgPoint.setVisible(true);
				if (dlgPoint.getPoint() != null) {
					newPoint.setX(dlgPoint.getPoint().getX());
					newPoint.setY(dlgPoint.getPoint().getY());
					newPoint.setColor(dlgPoint.getColor());
					String newState = newPoint.toString();
					editCommands.push(new EditPointCmd(p, newPoint));
					editCommands.peek().execute();
					commands.add("EDITED!" + oldState + "?" + newState);
					frame.commandList.append(commands.get(commands.size() - 1) + "\n");
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
					frame.repaint();
				}
			} else if (selectedShape instanceof Rectangle) {
				Rectangle r = (Rectangle) selectedShape;
				Color recColor = r.getColor();
				Color recInnerColor = r.getInnerColor();
				Rectangle newRectangle = new Rectangle();
				DlgRectangle dlgRectangle = new DlgRectangle(recColor, recInnerColor);
				dlgRectangle.txtUpperX.setText(Integer.toString(r.getUpperLeftPoint().getX()));
				dlgRectangle.txtUpperY.setText(Integer.toString(r.getUpperLeftPoint().getY()));
				dlgRectangle.txtWidth.setText(Integer.toString(r.getWidth()));
				dlgRectangle.txtHeight.setText(Integer.toString(r.getHeight()));
				dlgRectangle.btnColor.setBackground(r.getColor());
				dlgRectangle.btnInnerColor.setBackground(r.getInnerColor());
				dlgRectangle.setVisible(true);
				String oldState = r.toString();
				if (dlgRectangle.getRectangle() != null) {
					Rectangle r2 = dlgRectangle.getRectangle();
					Point p = new Point(r2.getUpperLeftPoint().getX(), r2.getUpperLeftPoint().getY());
					newRectangle.setUpperLeftPoint(p);
					newRectangle.setWidth(r2.getWidth());
					newRectangle.setHeight(r2.getHeight());
					newRectangle.setColor(dlgRectangle.getColor());
					newRectangle.setInnerColor(dlgRectangle.getInnerColor());
					String newState = newRectangle.toString();
					editCommands.push(new EditRectangleCmd(r, newRectangle));
					editCommands.peek().execute();
					commands.add("EDITED!" + oldState + "?" + newState);
					frame.commandList.append(commands.get(commands.size() - 1) + "\n");
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
					frame.repaint();
				}
			} else if (selectedShape instanceof Line) {
				Line l = (Line) selectedShape;
				Line newLine = new Line(new Point(), new Point());
				Color lineColor = l.getColor();
				DlgLine dlgLine = new DlgLine(lineColor);
				dlgLine.txtStartX.setText(Integer.toString(l.getStartPoint().getX()));
				dlgLine.txtStartY.setText(Integer.toString(l.getStartPoint().getY()));
				dlgLine.txtEndX.setText(Integer.toString(l.getEndPoint().getX()));
				dlgLine.txtEndY.setText(Integer.toString(l.getEndPoint().getY()));
				dlgLine.btnColor.setBackground(lineColor);
				dlgLine.setVisible(true);
				String oldState = l.toString();
				if (dlgLine.getLine() != null) {
					newLine.getStartPoint().setX(dlgLine.getLine().getStartPoint().getX());
					newLine.getStartPoint().setY(dlgLine.getLine().getStartPoint().getY());
					newLine.getEndPoint().setX(dlgLine.getLine().getEndPoint().getX());
					newLine.getEndPoint().setY(dlgLine.getLine().getEndPoint().getY());
					newLine.setColor(dlgLine.getLine().getColor());
					String newState = newLine.toString();
					commands.add("EDITED!" + oldState + "?" + newState);
					frame.commandList.append(commands.get(commands.size() - 1) + "\n");
					editCommands.push(new EditLineCmd(l, newLine));
					editCommands.peek().execute();
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
					frame.repaint();
				}
			} else if (selectedShape instanceof Donut) {
				Donut d = (Donut) selectedShape;
				Donut newDonut = new Donut();
				Color donutColor = d.getColor();
				Color donutInnerColor = d.getInnerColor();
				DlgDonut dlgDonut = new DlgDonut(donutColor, donutInnerColor);
				dlgDonut.txtCentarX.setText(Integer.toString(d.getCenter().getX()));
				dlgDonut.txtCentarY.setText(Integer.toString(d.getCenter().getY()));
				dlgDonut.txtRadius.setText(Integer.toString(d.getRadius()));
				dlgDonut.txtInnerRadius.setText(Integer.toString(d.getInnerRadius()));
				dlgDonut.btnColor.setBackground(donutColor);
				dlgDonut.btnInnerColor.setBackground(donutInnerColor);
				dlgDonut.setVisible(true);
				String oldState = d.toString();
				if (dlgDonut.getDonut() != null) {
					Donut d2 = dlgDonut.getDonut();
					Point p2 = new Point(d2.getCenter().getX(), d2.getCenter().getY());
					newDonut.setCenter(p2);
					newDonut.setRadius(d2.getRadius());
					newDonut.setInnerRadius(d2.getInnerRadius());
					newDonut.setColor(dlgDonut.getColor());
					newDonut.setInnerColor(dlgDonut.getInnerColor());
					String newState = newDonut.toString();
					editCommands.push(new EditDonutCmd(d, newDonut));
					editCommands.peek().execute();
					commands.add("EDITED!" + oldState + "?" + newState);
					frame.commandList.append(commands.get(commands.size() - 1) + "\n");
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
					frame.repaint();
				}
			} else if (selectedShape instanceof Circle) {
				Circle c = (Circle) selectedShape;
				Circle newCircle = new Circle();
				Color circleColor = c.getColor();
				Color circleInnerColor = c.getInnerColor();
				DlgCircle dlgCircle = new DlgCircle(circleColor, circleInnerColor);
				dlgCircle.txtCenterX.setText(Integer.toString(c.getCenter().getX()));
				dlgCircle.txtCenterY.setText(Integer.toString(c.getCenter().getY()));
				dlgCircle.txtRadius.setText(Integer.toString(c.getRadius()));
				dlgCircle.btnColor.setBackground(circleColor);
				dlgCircle.btnInnerColor.setBackground(circleInnerColor);
				dlgCircle.setVisible(true);
				String oldState = c.toString();
				if (dlgCircle.getCircle() != null) {
					Circle c2 = dlgCircle.getCircle();
					Point p1 = new Point(c2.getCenter().getX(), c2.getCenter().getY());
					newCircle.setCenter(p1);
					newCircle.setRadius(c2.getRadius());
					newCircle.setColor(dlgCircle.getColor());
					newCircle.setInnerColor(dlgCircle.getInnerColor());
					String newState = newCircle.toString();
					editCommands.push(new EditCircleCmd(c, newCircle));
					editCommands.peek().execute();
					commands.add("EDITED!" + oldState + "?" + newState);
					frame.commandList.append(commands.get(commands.size() - 1) + "\n");
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
					frame.repaint();
				}
			} else if (selectedShape instanceof HexagonAdapter) {
				HexagonAdapter hexagon = (HexagonAdapter) selectedShape;
				HexagonAdapter newHexagon = new HexagonAdapter(new Hexagon(0, 0, 0));
				Color hexagonColor = hexagon.getColor();
				Color hexagonInnerColor = hexagon.getInnerColor();
				DlgHexagon dlgHexagon = new DlgHexagon(hexagonColor, hexagonInnerColor);
				dlgHexagon.txtCenterX.setText(Integer.toString(hexagon.getCenterX()));
				dlgHexagon.txtCenterY.setText(Integer.toString(hexagon.getCenterY()));
				dlgHexagon.txtRadius.setText(Integer.toString(hexagon.getRadius()));
				dlgHexagon.btnColor.setBackground(hexagonColor);
				dlgHexagon.btnInnerColor.setBackground(hexagonInnerColor);
				dlgHexagon.setVisible(true);
				String oldState = hexagon.toString();
				if (dlgHexagon.getHexagon() != null) {
					HexagonAdapter hexagon2 = dlgHexagon.getHexagon();
					newHexagon.setCenterX(hexagon2.getCenterX());
					newHexagon.setCenterY(hexagon2.getCenterY());
					newHexagon.setRadius(hexagon2.getRadius());
					newHexagon.setColor(hexagon2.getColor());
					newHexagon.setInnerColor(hexagon2.getInnerColor());
					String newState = newHexagon.toString();
					editCommands.push(new EditHexagonCmd(hexagon, newHexagon));
					editCommands.peek().execute();
					commands.add("EDITED!" + oldState + "?" + newState);
					frame.commandList.append(commands.get(commands.size() - 1) + "\n");
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
					frame.repaint();
				}
			}
		}

	}

	public MouseAdapter viewMouseListener() {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (frame.tglBtnPoint.isSelected()) {
					setPointsToNull();
					Point clickPoint = new Point(e.getX(), e.getY());
					clickPoint.setColor(frame.color);
					model.add(clickPoint);
					command = "Added!" + clickPoint + "\n";
					frame.commandList.append(command);
					commands.add(command);
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
					frame.repaint();
				} else if (frame.tglBtnLine.isSelected()) {
					if (startPoint == null && endPoint == null) {
						startPoint = new Point(e.getX(), e.getY());
					} else if (startPoint != null && endPoint == null) {
						endPoint = new Point(e.getX(), e.getY());
						Line line = new Line(startPoint, endPoint);
						line.setColor(frame.color);
						command = "Added!" + line + "\n";
						frame.commandList.append(command);
						commands.add(command);
						model.add(line);
						startPoint = null;
						endPoint = null;
						tempCommands.clear();
						btnEnable.addShapeInList(model.getShapes().size());
						btnEnable.addShapeInSelectedList(selectedShapesList.size());
						btnEnable.addRedoList(tempCommands.size());
						btnEnable.addUndoList(commands.size());
						frame.repaint();
					}
				} else if (frame.tglBtnRectangle.isSelected()) {
					setPointsToNull();
					DlgRectangle dlgRectangle = new DlgRectangle(frame.color, frame.innerColor);
					dlgRectangle.txtUpperX.setText(Integer.toString(e.getX()));
					dlgRectangle.txtUpperY.setText(Integer.toString(e.getY()));
					dlgRectangle.setVisible(true);
					if (dlgRectangle.getRectangle() != null) {
						command = "Added!" + dlgRectangle.getRectangle() + "\n";
						frame.commandList.append(command);
						commands.add(command);
						model.add(dlgRectangle.getRectangle());
						tempCommands.clear();
						btnEnable.addShapeInList(model.getShapes().size());
						btnEnable.addShapeInSelectedList(selectedShapesList.size());
						btnEnable.addRedoList(tempCommands.size());
						btnEnable.addUndoList(commands.size());
						frame.repaint();
					}
				} else if (frame.tglBtnCircle.isSelected()) {
					setPointsToNull();
					DlgCircle dlgCircle = new DlgCircle(frame.color, frame.innerColor);
					dlgCircle.txtCenterX.setText(Integer.toString(e.getX()));
					dlgCircle.txtCenterY.setText(Integer.toString(e.getY()));
					dlgCircle.setVisible(true);
					if (dlgCircle.getCircle() != null) {
						command = "Added!" + dlgCircle.getCircle() + "\n";
						frame.commandList.append(command);
						commands.add(command);
						model.add(dlgCircle.getCircle());
						tempCommands.clear();
						btnEnable.addShapeInList(model.getShapes().size());
						btnEnable.addShapeInSelectedList(selectedShapesList.size());
						btnEnable.addRedoList(tempCommands.size());
						btnEnable.addUndoList(commands.size());
						frame.repaint();
					}
				} else if (frame.tglBtnDonut.isSelected()) {
					setPointsToNull();
					DlgDonut dlgDonut = new DlgDonut(frame.color, frame.innerColor);
					dlgDonut.txtCentarX.setText(Integer.toString(e.getX()));
					dlgDonut.txtCentarY.setText(Integer.toString(e.getY()));
					dlgDonut.setVisible(true);
					if (dlgDonut.getDonut() != null) {
						command = "Added!" + dlgDonut.getDonut() + "\n";
						frame.commandList.append(command);
						commands.add(command);
						model.add(dlgDonut.getDonut());
						tempCommands.clear();
						btnEnable.addShapeInList(model.getShapes().size());
						btnEnable.addShapeInSelectedList(selectedShapesList.size());
						btnEnable.addRedoList(tempCommands.size());
						btnEnable.addUndoList(commands.size());
						frame.repaint();
					}
				} else if (frame.tglBtnSelect.isSelected()) {
					setPointsToNull();
					select(e.getX(), e.getY());
				} else if (frame.tglBtnHexagon.isSelected()) {
					setPointsToNull();
					DlgHexagon dlgHexagon = new DlgHexagon(frame.color, frame.innerColor);
					dlgHexagon.txtCenterX.setText(Integer.toString(e.getX()));
					dlgHexagon.txtCenterY.setText(Integer.toString(e.getY()));
					dlgHexagon.setVisible(true);
					if (dlgHexagon.getHexagon() != null) {
						command = "Added!" + dlgHexagon.getHexagon() + "\n";
						frame.commandList.append(command);
						commands.add(command);
						model.add(dlgHexagon.getHexagon());
						tempCommands.clear();
						btnEnable.addShapeInList(model.getShapes().size());
						btnEnable.addShapeInSelectedList(selectedShapesList.size());
						btnEnable.addRedoList(tempCommands.size());
						btnEnable.addUndoList(commands.size());
						frame.repaint();
					}
				}
			}
		};
	}

	public void toFront(boolean undoOrRedo) {
		if (!undoOrRedo) {
			if (selectedShapesList.size() == 1 && model.getShapes().indexOf(selectedShapesList.get(0)) != model.getShapes().size() - 1) {
				Shape selectedShape = selectedShapesList.get(0);
				if (model.getShapes().indexOf(selectedShape) != model.getShapes().size() - 1) {
					Collections.swap(model.getShapes(), model.getShapes().indexOf(selectedShape),
							model.getShapes().indexOf(selectedShape) + 1);
					command = "ToFront!" + selectedShape + "\n";
					commands.add(command);
					frame.commandList.append(command);
					frame.repaint();
				}
				btnEnable.addRedoList(tempCommands.size());
				btnEnable.addUndoList(commands.size());
			}
		} else {
			Shape selectedShape = selectedShapesList.get(0);
			if (model.getShapes().indexOf(selectedShape) != model.getShapes().size() - 1) {
				Collections.swap(model.getShapes(), model.getShapes().indexOf(selectedShape),
						model.getShapes().indexOf(selectedShape) + 1);
				frame.repaint();
			}
			btnEnable.addRedoList(tempCommands.size());
			btnEnable.addUndoList(commands.size());
		}
	}

	public void toBack(boolean undoOrRedo) {
		if (!undoOrRedo) {
			if (selectedShapesList.size() == 1 && model.getShapes().indexOf(selectedShapesList.get(0)) != 0) {
				Shape selectedShape = selectedShapesList.get(0);
				if (model.getShapes().indexOf(selectedShape) != 0) {
					Collections.swap(model.getShapes(), model.getShapes().indexOf(selectedShape),
							model.getShapes().indexOf(selectedShape) - 1);
					command = "ToBack!" + selectedShape + "\n";
					commands.add(command);
					frame.commandList.append(command);
					frame.repaint();
				}
				btnEnable.addRedoList(tempCommands.size());
				btnEnable.addUndoList(commands.size());
			}
		} else {
			if (selectedShapesList.size() == 1) {
				Shape selectedShape = selectedShapesList.get(0);
				if (model.getShapes().indexOf(selectedShape) != 0) {
					Collections.swap(model.getShapes(), model.getShapes().indexOf(selectedShape),
							model.getShapes().indexOf(selectedShape) - 1);
					frame.repaint();
				}
				btnEnable.addRedoList(tempCommands.size());
				btnEnable.addUndoList(commands.size());
			}
		}
	}

	public void bringToFront(boolean undoOrRedo) {
		if (!undoOrRedo) {
			if (selectedShapesList.size() == 1 && model.getShapes().indexOf(selectedShapesList.get(0)) != model.getShapes().size() - 1) {
				Shape selectedShape = selectedShapesList.get(0);
				if (model.getShapes().size() >= 2) {
					ArrayList<Shape> tempList = new ArrayList<Shape>();
					tempList.addAll(model.getShapes());
					int index = model.getShapes().indexOf(selectedShape);
					model.getShapes().clear();
					for (int i = 0; i <= tempList.size() - 1; i++) {
						if (i == index) {
							continue;
						}
						model.getShapes().add(tempList.get(i));
					}
					model.getShapes().add(tempList.get(index));
					command = "BringToFront!" + selectedShape + "\n";
					commands.add(command);
					frame.commandList.append(command);
					frame.repaint();
					tempList.clear();
				}
				btnEnable.addRedoList(tempCommands.size());
				btnEnable.addUndoList(commands.size());
			}
		} else {
			Shape selectedShape = selectedShapesList.get(0);
			if (model.getShapes().size() >= 2) {
				ArrayList<Shape> tempList = new ArrayList<Shape>();
				tempList.addAll(model.getShapes());
				int index = model.getShapes().indexOf(selectedShape);
				model.getShapes().clear();
				for (int i = 0; i <= tempList.size() - 1; i++) {
					if (i == index) {
						continue;
					}
					model.getShapes().add(tempList.get(i));
				}
				model.getShapes().add(tempList.get(index));
				btnEnable.addRedoList(tempCommands.size());
				btnEnable.addUndoList(commands.size());
			}
		}
	}

	public void bringToBack(boolean undoOrRedo) {
		if (!undoOrRedo) {
			if (selectedShapesList.size() == 1 && model.getShapes().indexOf(selectedShapesList.get(0)) != 0) {
				Shape selectedShape = selectedShapesList.get(0);
				if (model.getShapes().size() >= 2) {
					ArrayList<Shape> tempList = new ArrayList<Shape>();
					tempList.addAll(model.getShapes());
					int index = model.getShapes().indexOf(selectedShape);
					model.getShapes().clear();
					model.getShapes().add(tempList.get(index));
					for (int i = 0; i <= tempList.size() - 1; i++) {
						if (i == index) {
							continue;
						}
						model.getShapes().add(tempList.get(i));
					}
					command = "BringToBack!" + selectedShape + "\n";
					commands.add(command);
					frame.commandList.append(command);
					frame.repaint();
					tempList.clear();
				}
				btnEnable.addRedoList(tempCommands.size());
				btnEnable.addUndoList(commands.size());
			}
		} else {
			Shape selectedShape = selectedShapesList.get(0);
			if (model.getShapes().size() >= 2) {
				ArrayList<Shape> tempList = new ArrayList<Shape>();
				tempList.addAll(model.getShapes());
				int index = model.getShapes().indexOf(selectedShape);
				model.getShapes().clear();
				model.getShapes().add(tempList.get(index));
				for (int i = 0; i <= tempList.size() - 1; i++) {
					if (i == index) {
						continue;
					}
					model.getShapes().add(tempList.get(i));
				}
				btnEnable.addRedoList(tempCommands.size());
				btnEnable.addUndoList(commands.size());
			}
		}
	}

	public void undo() {
		if (commands.size() != 0) {
			if (readCommand().equals("Added")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				AddShapeCmd asc = new AddShapeCmd(model.get(model.getShapes().size() - 1), model);
				tempShapes.push(model.get(model.getShapes().size() - 1));
				asc.unexecute();
				frame.repaint();
			} else if (readCommand().equals("Deleted")) {
				try {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
					tempCommands.add(commands.remove(commands.size() - 1));
					deletedShapes.peek().setSelected(true);
					selectedShapesList.add(deletedShapes.peek());
					tempDeletedShapes.push(deletedShapes.peek());
					RemoveShapeCmd rsc = new RemoveShapeCmd(deletedShapes.pop(), model, indexs.pop());
					rsc.unexecute();
					frame.repaint();
				} catch (IndexOutOfBoundsException e) {

				}
			} else if (readCommand().equals("ToFront")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				tfc.unexecute();
				frame.repaint();
			} else if (readCommand().equals("ToBack")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				tbc.unexecute();
				frame.repaint();
			} else if (readCommand().equals("BringToFront")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				btfc.unexecute();
				frame.repaint();
			} else if (readCommand().equals("BringToBack")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				btbc.unexecute();
				frame.repaint();
			} else if (readCommand().equals("EDITED")) {
				if (readEditShape().equals("Point")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1) + "\n");
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Line")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1) + "\n");
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Rectangle")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1) + "\n");
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Circle")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1) + "\n");
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Donut")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1) + "\n");
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Hexagon")) {
					frame.commandList.append("UNDO" + commands.get(commands.size() - 1) + "\n");
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				}
			} else if(readCommand().equals("SELECTED")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1) + "\n");
				tempCommands.add(commands.remove(commands.size() - 1));
				deselectedShapesList.add(selectedShapesList.remove(selectedShapesList.size() - 1));
				SelectShapeCmd ssc = new SelectShapeCmd(model, model.getShapes().indexOf(deselectedShapesList.get(deselectedShapesList.size() - 1)));
				ssc.unexecute();
				frame.repaint();
			} else if(readCommand().equals("DESELECTED")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1) + "\n");
				tempCommands.add(commands.remove(commands.size() - 1));
				selectedShapesList.add(deselectedShapesList.remove(deselectedShapesList.size() - 1));
				DeselectShapeCmd dsc = new DeselectShapeCmd(model, model.getShapes().indexOf(selectedShapesList.get(selectedShapesList.size() - 1)));
				dsc.unexecute();
				frame.repaint();
			}
			btnEnable.addShapeInList(model.getShapes().size());
			btnEnable.addShapeInSelectedList(selectedShapesList.size());
			btnEnable.addRedoList(tempCommands.size());
			btnEnable.addUndoList(commands.size());
			/*
			 * else if (readCommand().equals("SELECTED")) { frame.commandList.append("UNDO!"
			 * + commands.get(commands.size() - 1) + "\n");
			 * tempCommands.add(commands.remove(commands.size() - 1));
			 * tempSelectedShapes.push(selectedShapes.peek()); SelectShapeCmd ssc = new
			 * SelectShapeCmd(model, model.getShapes().indexOf(selectedShapes.pop()));
			 * ssc.unexecute(); frame.repaint(); } else if
			 * (readCommand().equals("DESELECTED")) { frame.commandList.append("UNDO!" +
			 * commands.get(commands.size() - 1) + "\n");
			 * tempCommands.add(commands.remove(commands.size() - 1)); DeselectShapeCmd dsc
			 * = new DeselectShapeCmd(model,
			 * model.getShapes().indexOf(selectedShapesForDeselect.pop())); dsc.unexecute();
			 * frame.repaint(); }
			 */
		}
	}

	public void redo() {
		if (tempCommands.size() != 0) {
			if (readUndoCommand().equals("Added")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				AddShapeCmd asc = new AddShapeCmd(tempShapes.pop(), model);
				asc.execute();
				frame.repaint();
			} else if (readUndoCommand().equals("Deleted")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				deletedShapes.push(tempDeletedShapes.peek());
				indexs.push(model.getShapes().indexOf(tempDeletedShapes.peek()));
				RemoveShapeCmd rsc = new RemoveShapeCmd(tempDeletedShapes.pop(), model, 0);
				rsc.execute();
				frame.repaint();
			} else if (readUndoCommand().equals("ToFront")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				tfc.execute();
				frame.repaint();
			} else if (readUndoCommand().equals("ToBack")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				tbc.execute();
				frame.repaint();
			} else if (readUndoCommand().equals("BringToFront")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				btfc.execute();
				frame.repaint();
			} else if (readUndoCommand().equals("BringToBack")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				btbc.execute();
				frame.repaint();
			} else if (readUndoCommand().equals("EDITED")) {
				if (readEditShape().equals("Point") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Line") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Rectangle") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Circle") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Donut") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Hexagon") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				}
			} else if(readUndoCommand().equals("SELECTED")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				selectedShapesList.add(deselectedShapesList.remove(deselectedShapesList.size() - 1));
				SelectShapeCmd ssc = new SelectShapeCmd(model, model.getShapes().indexOf(selectedShapesList.get(selectedShapesList.size() - 1)));
				ssc.execute();
				frame.repaint();
			} else if(readUndoCommand().equals("DESELECTED")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1) + "\n");
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				deselectedShapesList.add(selectedShapesList.remove(selectedShapesList.size() - 1));
				DeselectShapeCmd dsc = new DeselectShapeCmd(model, model.getShapes().indexOf(deselectedShapesList.get(deselectedShapesList.size() - 1)));
				dsc.execute();
				frame.repaint();
			}
			btnEnable.addShapeInList(model.getShapes().size());
			btnEnable.addShapeInSelectedList(selectedShapesList.size());
			btnEnable.addRedoList(tempCommands.size());
			btnEnable.addUndoList(commands.size());
			/*
			 * else if (readUndoCommand().equals("SELECTED")) {
			 * frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1)
			 * + "\n"); commands.add(tempCommands.remove(tempCommands.size() - 1));
			 * selectedShapes.push(tempSelectedShapes.peek()); SelectShapeCmd ssc = new
			 * SelectShapeCmd(model, model.getShapes().indexOf(tempSelectedShapes.pop()));
			 * ssc.execute(); frame.repaint(); } else if
			 * (readUndoCommand().equals("DESELECT")) { frame.commandList.append("REDO!" +
			 * tempCommands.get(tempCommands.size() - 1) + "\n");
			 * commands.add(tempCommands.remove(tempCommands.size() - 1)); DeselectShapeCmd
			 * dsc = new DeselectShapeCmd(model, 0); dsc.execute(); frame.repaint(); }
			 */
		}
	}
	
	public void saveFile() {
		System.out.println("uso sam");
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home")+"/Desktop");
		fileChooser.setDialogTitle("Save as");
		fileChooser.setVisible(true);
	}

	private String readUndoCommand() {
		String[] parts = tempCommands.get(tempCommands.size() - 1).split("!");
		drawCommand = parts[0];
		return drawCommand;
	}

	private String readEditShape() {
		String[] parts = commands.get(commands.size() - 1).split("!");
		String[] shapeCommand = parts[1].split("->");
		return shapeCommand[0];
	}

	private void removeCommandsFromList() {
		StringBuilder sb = new StringBuilder();
		for (String command : commands) {
			sb.append(command);
		}
		frame.commandList.setText(sb.toString());
	}

	private String readCommand() {
		String[] parts = commands.get(commands.size() - 1).split("!");
		drawCommand = parts[0];
		return drawCommand;
	}
}