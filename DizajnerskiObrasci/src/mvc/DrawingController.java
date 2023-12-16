package mvc;

import java.awt.Color;
import java.awt.color.CMMException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.standard.Media;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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
import strategy.LoadSaveBin;
import strategy.LoadSaveTxt;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgDonut;
import dialogs.DlgHexagon;
import dialogs.DlgCircle;

public class DrawingController {

	private static final Object[] String = null;
	private ButtonEnable btnEnable;
	private ButtonEnableUpdate btnEnableUpdate;

	private ToFrontCmd tfc = new ToFrontCmd(this);
	private ToBackCmd tbc = new ToBackCmd(this);
	//private BringToFrontCmd btfc = new BringToFrontCmd(this);
	//private BringToBackCmd btbc = new BringToBackCmd(this);
	private Stack<Command> brings = new Stack<Command>(); 
	private Stack<Command> tempBrings = new Stack<Command>();

	DrawingModel model;
	DrawingFrame frame;

	private Point startPoint;
	private Point endPoint;

	Shape selectedShape;

	// UNDO I REDO ZA EDIT :)
	private Stack<Command> editCommands = new Stack<Command>();
	private Stack<Command> tempEditCommands = new Stack<Command>();

	private Stack<Command> selectCommands = new Stack<Command>();
	private Stack<Command> tempSelectCommands = new Stack<Command>();

	private String command;
	private String drawCommand;
	private ArrayList<String> tempCommands = new ArrayList<String>();
	private ArrayList<String> commands = new ArrayList<String>();
	private Stack<Shape> deletedShapes = new Stack<Shape>();
	private Stack<Shape> tempShapes = new Stack<Shape>();
	private Stack<Integer> indexs = new Stack<Integer>();
	private Stack<Shape> tempDeletedShapes = new Stack<Shape>();

	private ArrayList<Shape> deselectedShapesList = new ArrayList<Shape>();
	public ArrayList<Shape> selectedShapesList = new ArrayList<Shape>();
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
				tempCommands.clear();
			}
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
					SelectShapeCmd ssc = new SelectShapeCmd(shapes.get(br), selectedShapesList);
					ssc.execute();
					selectCommands.push(ssc);
					selectedShape = shapes.get(br);
					blank = true;
					tempCommands.clear();
					btnEnable.addShapeInList(model.getShapes().size());
					btnEnable.addShapeInSelectedList(selectedShapesList.size());
					btnEnable.addRedoList(tempCommands.size());
					btnEnable.addUndoList(commands.size());
				} else {
					Shape deselectedShape = shapes.get(br);
					DeselectShapeCmd dsc = new DeselectShapeCmd(shapes.get(br), selectedShapesList);
					dsc.execute();
					selectCommands.push(dsc);
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
			commands.add("SELECTED!" + selectedShape + "\n");
			frame.commandList.append("SELECTED!" + selectedShape);
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
					commands.add("EDITED!" + oldState + "/" + newState + "\n");
					frame.commandList.append(commands.get(commands.size() - 1));
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
					commands.add("EDITED!" + oldState + "/" + newState + "\n");
					frame.commandList.append(commands.get(commands.size() - 1));
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
					commands.add("EDITED!" + oldState + "/" + newState + "\n");
					frame.commandList.append(commands.get(commands.size() - 1));
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
					commands.add("EDITED!" + oldState + "/" + newState + "\n");
					frame.commandList.append(commands.get(commands.size() - 1));
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
					commands.add("EDITED!" + oldState + "/" + newState + "\n");
					frame.commandList.append(commands.get(commands.size() - 1));
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
					commands.add("EDITED!" + oldState + "/" + newState + "\n");
					frame.commandList.append(commands.get(commands.size() - 1));
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
			if (selectedShapesList.size() == 1
					&& model.getShapes().indexOf(selectedShapesList.get(0)) != model.getShapes().size() - 1) {
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
		brings.push(new BringToFrontCmd(model, selectedShapesList.get(0)));
		brings.peek().execute();
		command = "BringToFront!" + selectedShape;
		commands.add(command);
		frame.commandList.append(command);
		frame.repaint();
		btnEnable.addRedoList(tempCommands.size());
		btnEnable.addUndoList(commands.size());
		/*if (!undoOrRedo) {
			if (selectedShapesList.size() == 1
					&& model.getShapes().indexOf(selectedShapesList.get(0)) != model.getShapes().size() - 1) {
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
		}*/
	}

	public void bringToBack(boolean undoOrRedo) {
		brings.push(new BringToBackCmd(model, selectedShapesList.get(0)));
		brings.peek().execute();
		command = "BringToBack!" + selectedShape;
		commands.add(command);
		frame.commandList.append(command);
		frame.repaint();
		btnEnable.addRedoList(tempCommands.size());
		btnEnable.addUndoList(commands.size());
		/*if (!undoOrRedo) {
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
		}*/
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
				brings.peek().unexecute();
				tempBrings.push(brings.pop());
				frame.repaint();
			} else if (readCommand().equals("BringToBack")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				brings.peek().unexecute();
				tempBrings.push(brings.pop());
				frame.repaint();
			} else if (readCommand().equals("EDITED")) {
				if (readEditShape().equals("Point")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Line")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Rectangle")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Circle")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Donut")) {
					frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Hexagon")) {
					frame.commandList.append("UNDO" + commands.get(commands.size() - 1));
					tempCommands.add(commands.remove(commands.size() - 1));
					editCommands.peek().unexecute();
					tempEditCommands.push(editCommands.pop());
					frame.repaint();
				}
			} else if (readCommand().equals("SELECTED")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				selectCommands.peek().unexecute();
				tempSelectCommands.push(selectCommands.pop());
				frame.repaint();
			} else if (readCommand().equals("DESELECTED")) {
				frame.commandList.append("UNDO!" + commands.get(commands.size() - 1));
				tempCommands.add(commands.remove(commands.size() - 1));
				selectCommands.peek().unexecute();
				tempSelectCommands.push(selectCommands.pop());
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
				selectedShapesList.remove(selectedShapesList.size() - 1);
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
				tempBrings.peek().execute();
				brings.push(tempBrings.pop());
				frame.repaint();
			} else if (readUndoCommand().equals("BringToBack")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				tempBrings.peek().execute();
				brings.push(tempBrings.pop());
				frame.repaint();
			} else if (readUndoCommand().equals("EDITED")) {
				if (readEditShape().equals("Point") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Line") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Rectangle") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Circle") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Donut") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				} else if (readEditShape().equals("Hexagon") && !tempEditCommands.isEmpty()) {
					frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
					commands.add(tempCommands.remove(tempCommands.size() - 1));
					tempEditCommands.peek().execute();
					editCommands.push(tempEditCommands.pop());
					frame.repaint();
				}
			} else if (readUndoCommand().equals("SELECTED")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				tempSelectCommands.peek().execute();
				selectCommands.push(tempSelectCommands.pop());
				frame.repaint();
			} else if (readUndoCommand().equals("DESELECTED")) {
				frame.commandList.append("REDO!" + tempCommands.get(tempCommands.size() - 1));
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				tempSelectCommands.peek().execute();
				selectCommands.push(tempSelectCommands.pop());
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
		JFileChooser fileChooser = new JFileChooser();
		int userSelection = fileChooser.showSaveDialog(frame);
		fileChooser.setDialogTitle("Save as");
		fileChooser.setVisible(true);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String path = file.getAbsolutePath();
			LoadSaveBin lsb = new LoadSaveBin(model, this);
			lsb.saveF(path + ".bin");
			LoadSaveTxt lst = new LoadSaveTxt(frame, this);
			lst.saveF(path + ".txt");
		}
	}

	public void loadBinFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open");
		fileChooser.setVisible(true);

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary Files", "bin");
		fileChooser.setFileFilter(filter);

		int userSelection = fileChooser.showOpenDialog(frame);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String filePath = file.getAbsolutePath();
			LoadSaveBin lsb = new LoadSaveBin(model, this);
			lsb.load(filePath);
			frame.repaint();
			btnEnable.addShapeInList(model.getShapes().size());
			btnEnable.addShapeInSelectedList(selectedShapesList.size());
			btnEnable.addRedoList(tempCommands.size());
			btnEnable.addUndoList(commands.size());
		}
	}

	public void loadTxt() {

	}

	public void loadTxtFile() {
		model.getShapes().clear();
		selectedShapesList.clear();
		indexs.clear();
		commands.clear();
		tempCommands.clear();
		frame.commandList.setText("");
		frame.repaint();
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home")+"/Desktop");
	    fileChooser.setDialogTitle("Open");
	    fileChooser.setVisible(true);

	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
	    fileChooser.setFileFilter(filter);
	    int userSelection = fileChooser.showOpenDialog(frame);
	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        String filePath = file.getAbsolutePath();
			LoadSaveTxt lst = new LoadSaveTxt(frame, this);
			String[]string2 = null;
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(file));
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	int option = JOptionPane.showConfirmDialog(null, "DO YOU WANT TO EXECUTE THIS COMMAND:" + "\n" + line);
	    			if (option == JOptionPane.YES_OPTION) {
	    				String command = readTxtCommand(line);
	    				if(command.equals("Added")) {
	    					Shape shape = readShape(line);
	    					model.add(shape);
    						frame.commandList.append(line + "\n");
    						commands.add(line);
    						frame.repaint();
	    				} else if(command.equals("Deleted")) {
	    					Shape shape = readShape(line);
	    					if(checkShapeInSelectedList(shape) != null) {
	    						deletedShapes.push(checkShapeInSelectedList(shape));
	    						frame.commandList.append(line + "\n");
	    						commands.add(command);
	    						indexs.push(model.getShapes().indexOf(checkShapeInSelectedList(shape)));
	    						model.getShapes().remove(checkShapeInSelectedList(shape));
	    						selectedShapesList.remove(checkShapeInSelectedList(shape));
	    						frame.repaint();
	    					}
	    				} else if(command.equals("SELECTED")) {
	    					Shape shape = readShape(line);
	    					if(checkIsSelected(shape) != null) {
    							SelectShapeCmd ssc = new SelectShapeCmd(checkIsSelected(shape), selectedShapesList);
    							ssc.execute();
    							selectCommands.push(ssc);
    							selectedShape = checkIsSelected(shape);
    							frame.commandList.append(line + "\n");
	    						commands.add(line);
	    						frame.repaint();
    						}
	    				} else if(command.equals("DESELECTED")) {
	    					Shape shape = readShape(line);
	    					if(checkShapeInSelectedList(shape) != null) {
	    						Shape deselectedShape = checkShapeInSelectedList(shape);
	    						DeselectShapeCmd dsc = new DeselectShapeCmd(deselectedShape, selectedShapesList);
	    						dsc.execute();
	    						selectCommands.push(dsc);
	    						frame.commandList.append(line + "\n");
	    						commands.add(line);
	    						selectedShapesList.remove(deselectedShape);
	    						frame.repaint();
	    					}
	    				} else if(command.equals("EDITED")) {
	    					Shape[] shapes = readNewState(line);
	    					Shape oldShape = shapes[0];
	    					Shape newShape = shapes[1];
	    					if(checkShapeInSelectedList(oldShape) != null && selectedShapesList.size() == 1) {
	    						if(oldShape instanceof Point) {
	    							Point oldPoint = (Point)oldShape;
	    							Point newPoint = (Point)newShape;
	    							System.out.println(oldPoint + " " + newPoint);
	    							editCommands.push(new EditPointCmd(oldPoint, newPoint));
	    							editCommands.peek().execute();
	    							commands.add(line);
	    							frame.commandList.append(line + "\n");
	    							frame.repaint();
	    						} else if(oldShape instanceof Line) {
	    							Line oldLine = (Line)oldShape;
	    							Line newLine = (Line)newShape;
	    							System.out.println(oldLine + " " + newLine);
	    							Line linee = new Line(new Point(), new Point());
	    							linee.getStartPoint().setX(newLine.getStartPoint().getX());
	    							linee.getStartPoint().setY(newLine.getStartPoint().getY());
	    							linee.getEndPoint().setX(newLine.getEndPoint().getX());
	    							linee.getEndPoint().setY(newLine.getEndPoint().getY());
	    							linee.setColor(newLine.getColor());
	    							editCommands.push(new EditLineCmd(oldLine, linee));
	    							editCommands.peek().execute();
	    							readNewState(line);
	    							commands.add(line);
	    							frame.commandList.append(line + "\n");
	    							frame.repaint();
	    						} else if(oldShape instanceof Rectangle) {
	    							Rectangle oldRectangle = (Rectangle)oldShape;
	    							Rectangle newRectangle = (Rectangle)newShape;
	    							System.out.println(oldRectangle + " " + newRectangle);
	    							editCommands.push(new EditRectangleCmd(oldRectangle, newRectangle));
	    							editCommands.peek().execute();
	    							readNewState(line);
	    							commands.add(line);
	    							frame.commandList.append(line + "\n");
	    							frame.repaint();
	    						} else if(oldShape instanceof Circle) {
	    							Circle oldCircle = (Circle)oldShape;
	    							Circle newCircle = (Circle)newShape;
	    							System.out.println(oldCircle + " " + newCircle);
	    							editCommands.push(new EditCircleCmd(oldCircle, newCircle));
	    							editCommands.peek().execute();
	    							readNewState(line);
	    							commands.add(line);
	    							frame.commandList.append(line + "\n");
	    							frame.repaint();
	    						}
	    					}
	    				}
	    			}
	            }
	        }catch (IOException e) {
	        	e.printStackTrace();
	        }
			frame.repaint();
	        btnEnable.addShapeInList(model.getShapes().size());
			btnEnable.addShapeInSelectedList(selectedShapesList.size());
			btnEnable.addRedoList(tempCommands.size());
			btnEnable.addUndoList(commands.size());
	    }

	}
	
	private Shape checkShapeInSelectedList(Shape shape) {
		for(int i = 0; i <= selectedShapesList.size() - 1; i++) {
			System.out.println("Vrti me u selektovanoj listi");
			if(shape.equals(selectedShapesList.get(i))) {
				return selectedShapesList.get(i);
			}
		}
		return null;
	}
	
	private Shape[] readNewState(String line) {
		Shape[] shapes = new Shape[2];
		String[] shape = line.split("/");
		Shape oldShape = readShape(shape[0]);
		Shape newShape = readShape("EDITED!" + shape[1]);
		shapes[0] = oldShape;
		shapes[1] = newShape;
		return shapes;
	}
	
	private Shape readShape(String line) {
		String shape = readTxtShape(line);
		if(shape.equals("Point")) {
			String[] newPoint = readPoint(line);
			int x = Integer.parseInt(newPoint[0]);
			int y = Integer.parseInt(newPoint[1]);
			int r = Integer.parseInt(newPoint[2]);
			int g = Integer.parseInt(newPoint[3]);
			int b = Integer.parseInt(newPoint[4]);
			Shape point = new Point(x, y);
			point.setColor(new Color(r, g, b));
			return point;
		} else if(shape.equals("Line")) {
			String[] newLine = readLine(line);
			int startX = Integer.parseInt(newLine[0]);
			int startY = Integer.parseInt(newLine[1]);
			int endX = Integer.parseInt(newLine[2]);
			int endY = Integer.parseInt(newLine[3]);
			int r = Integer.parseInt(newLine[4]);
			int g = Integer.parseInt(newLine[5]);
			int b = Integer.parseInt(newLine[6]);
			Point startPoint = new Point(startX, startY);
			Point endPoint = new Point(endX, endY);
			Shape nLine = new Line(startPoint, endPoint);
			nLine.setColor(new Color(r, g, b));
			return nLine;
			
		} else if(shape.equals("Rectangle")) {
			String[] newRectangle = readRectangle(line);
			int upperX = Integer.parseInt(newRectangle[0]);
			int upperY = Integer.parseInt(newRectangle[1]);
			int width = Integer.parseInt(newRectangle[2]);
			int height = Integer.parseInt(newRectangle[3]);
			int colorR = Integer.parseInt(newRectangle[4]);
			int colorG = Integer.parseInt(newRectangle[5]);
			int colorB = Integer.parseInt(newRectangle[6]);
			int innerR = Integer.parseInt(newRectangle[7]);
			int innerG = Integer.parseInt(newRectangle[8]);
			int innerB = Integer.parseInt(newRectangle[9]);
			Point upperLeftPoint = new Point(upperX, upperY);
			Shape rectangle = new Rectangle(upperLeftPoint, width, height);
			rectangle.setColor(new Color(colorR, colorG, colorB));
			rectangle.setInnerColor(new Color(innerR, innerG, innerB));
			return rectangle;
		} else if(shape.equals("Circle")) {
			String[] newCircle = readCircle(line);
			int centerX = Integer.parseInt(newCircle[0]);
			int centerY = Integer.parseInt(newCircle[1]);
			int radius = Integer.parseInt(newCircle[2]);
			int colorR = Integer.parseInt(newCircle[3]);
			int colorG = Integer.parseInt(newCircle[4]);
			int colorB = Integer.parseInt(newCircle[5]);
			int innerR = Integer.parseInt(newCircle[6]);
			int innerG = Integer.parseInt(newCircle[7]);
			int innerB = Integer.parseInt(newCircle[8]);
			Point center = new Point(centerX, centerY);
			Shape circle = new Circle(center, radius);
			circle.setColor(new Color(colorR, colorG, colorB));
			circle.setInnerColor(new Color(innerR, innerG, innerB));
			return circle;
		} else if(shape.equals("Donut")) {
			String[] newDonut = readDonut(line);
			int centerX = Integer.parseInt(newDonut[0]);
			int centerY = Integer.parseInt(newDonut[1]);
			int radius = Integer.parseInt(newDonut[2]);
			int innerRadius = Integer.parseInt(newDonut[9]);
			int colorR = Integer.parseInt(newDonut[3]);
			int colorG = Integer.parseInt(newDonut[4]);
			int colorB = Integer.parseInt(newDonut[5]);
			int innerR = Integer.parseInt(newDonut[6]);
			int innerG = Integer.parseInt(newDonut[7]);
			int innerB = Integer.parseInt(newDonut[8]);
			Point center = new Point(centerX, centerY);
			Shape donut = new Donut(center, radius, innerRadius);
			donut.setColor(new Color(colorR, colorG, colorB));
			donut.setInnerColor(new Color(innerR, innerG, innerB));
			return donut;
		} else if(shape.equals("Hexagon")) {
			String[] newHexagon = readCircle(line);
			int centerX = Integer.parseInt(newHexagon[0]);
			int centerY = Integer.parseInt(newHexagon[1]);
			int radius = Integer.parseInt(newHexagon[2]);
			int colorR = Integer.parseInt(newHexagon[3]);
			int colorG = Integer.parseInt(newHexagon[4]);
			int colorB = Integer.parseInt(newHexagon[5]);
			int innerR = Integer.parseInt(newHexagon[6]);
			int innerG = Integer.parseInt(newHexagon[7]);
			int innerB = Integer.parseInt(newHexagon[8]);
			HexagonAdapter hexagon = new HexagonAdapter(new Hexagon(centerX, centerY, radius));
			hexagon.setColor(new Color(colorR, colorG, colorB));
			hexagon.setInnerColor(new Color(innerR, innerG, innerB));
			return hexagon;
		}
		return null;
	}
	
	private Shape checkIsSelected(Shape shape) {
		for(int i = 0; i <= model.getShapes().size() - 1; i++) {
			if(model.get(i).equals(shape)) {
				return model.get(i);
			}
		}
		return null;
	}

	private String[] readDonut(String line) {
		String[] newDonut = new String[10];
		//String[] parts = line.split("!");
		String[] shape = line.split(" ");
		String[] donut = shape[0].split("->");
		String[] donutCoordinates = donut[1].split(":");
		String[] coordinates = donutCoordinates[1].split(",");
		String[] xCoordinates = coordinates[0].split("=");
		String[] yCoordinates = coordinates[1].split("=");
		String[] radius = coordinates[2].split("=");
		String[] innerRadius = coordinates[3].split("=");
		Pattern pattern = Pattern.compile(
				"Color:java.awt.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\],InnerColor:java.awt.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\]");
		Matcher matcher = pattern.matcher(shape[1]);
		newDonut[0] = xCoordinates[1];
		newDonut[1] = yCoordinates[1];
		newDonut[2] = radius[1];
		if (matcher.find()) {
			newDonut[3] = matcher.group(1);
			newDonut[4] = matcher.group(2);
			newDonut[5] = matcher.group(3);
			newDonut[6] = matcher.group(4);
			newDonut[7] = matcher.group(5);
			newDonut[8] = matcher.group(6);
		}
		newDonut[9] = innerRadius[1];
		return newDonut;
	}

	private String[] readCircle(String line) {
		String[] newCircle = new String[10];
		//String[] parts = line.split("!");
		String[] shape = line.split(" ");
		String[] circle = shape[0].split("->");
		String[] circleCoordinates = circle[1].split(":");
		String[] coordinates = circleCoordinates[1].split(",");
		String[] xCoordinates = coordinates[0].split("=");
		String[] yCoordinates = coordinates[1].split("=");
		String[] radius = coordinates[2].split("=");
		Pattern pattern = Pattern.compile(
				"Color:java.awt.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\],InnerColor:java.awt.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\]");
		Matcher matcher = pattern.matcher(shape[1]);
		newCircle[0] = xCoordinates[1];
		newCircle[1] = yCoordinates[1];
		newCircle[2] = radius[1];
		if (matcher.find()) {
			newCircle[3] = matcher.group(1);
			newCircle[4] = matcher.group(2);
			newCircle[5] = matcher.group(3);
			newCircle[6] = matcher.group(4);
			newCircle[7] = matcher.group(5);
			newCircle[8] = matcher.group(6);
		}
		return newCircle;
	}

	private String[] readRectangle(String line) {
		String[] newRectangle = new String[10];
		//String[] parts = line.split("!");
		String[] shape = line.split(" ");
		String[] rectangle = shape[0].split("->");
		String[] recCoordinates = rectangle[1].split(":");
		String[] coordinates = recCoordinates[1].split(",");
		String[] xCoordinates = coordinates[0].split("=");
		String[] yCoordinates = coordinates[1].split("=");
		String[] width = coordinates[2].split("=");
		String[] height = coordinates[3].split("=");
		Pattern pattern = Pattern.compile(
				"Color:java.awt.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\],InnerColor:java.awt.Color\\[r=(\\d+),g=(\\d+),b=(\\d+)\\]");
		Matcher matcher = pattern.matcher(shape[1]);
		newRectangle[0] = xCoordinates[1];
		newRectangle[1] = yCoordinates[1];
		newRectangle[2] = width[1];
		newRectangle[3] = height[1];
		if (matcher.find()) {
			newRectangle[4] = matcher.group(1);
			newRectangle[5] = matcher.group(2);
			newRectangle[6] = matcher.group(3);
			newRectangle[7] = matcher.group(4);
			newRectangle[8] = matcher.group(5);
			newRectangle[9] = matcher.group(6);
		}
		return newRectangle;
	}

	private String[] readLine(String linee) {
		String[] newLine = new String[10];
		//String[] parts = linee.split("!");
		String[] shape = linee.split("->");
		String[] line = shape[1].split(" ");
		String[] points = line[0].split(";");
		String[] startCoordinates = points[0].split(":");
		String[] endCoordinates = points[1].split(":");
		String[] startPointCoordinates = startCoordinates[1].split(",");
		String[] endPointCoordinates = endCoordinates[1].split(",");
		String[] startX = startPointCoordinates[0].split("=");
		String[] startY = startPointCoordinates[1].split("=");
		String[] endX = endPointCoordinates[0].split("=");
		String[] endY = endPointCoordinates[1].split("=");
		String regex = "r=(\\d+),g=(\\d+),b=(\\d+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line[1]);
		newLine[0] = startX[1];
		newLine[1] = startY[1];
		newLine[2] = endX[1];
		newLine[3] = endY[1];
		if (matcher.find()) {
			newLine[4] = matcher.group(1);
			newLine[5] = matcher.group(2);
			newLine[6] = matcher.group(3);
		}
		return newLine;
	}

	private String[] readPoint(String line) {
		String[] newPoint = new String[10];
		//String[] parts = line.split("!");
		String[] shape = line.split("->");
		String[] point = shape[1].split(" ");
		String[] coordinates = point[0].split(",");
		String[] xCoordinates = coordinates[0].split("=");
		String[] yCoordinates = coordinates[1].split("=");
		String regex = "r=(\\d+),g=(\\d+),b=(\\d+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(point[1]);
		newPoint[0] = xCoordinates[1];
		newPoint[1] = yCoordinates[1];
		if (matcher.find()) {
			newPoint[2] = matcher.group(1);
			newPoint[3] = matcher.group(2);
			newPoint[4] = matcher.group(3);
		}
		return newPoint;
	}
	
	private String readTxtShape(String line) {
		String[] parts = line.split("!");
		String[] shape = parts[1].split("->");
		return shape[0];
	}

	private String readTxtCommand(String line) {
		String[] parts = line.split("!");
		return parts[0];
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