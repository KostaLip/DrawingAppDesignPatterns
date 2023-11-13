package mvc;

import java.awt.Color;
import java.awt.color.CMMException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

import command.AddShapeCmd;
import command.RemoveShapeCmd;
import dialogs.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgDonut;
import dialogs.DlgCircle;

public class DrawingController {

	DrawingModel model;
	DrawingFrame frame;

	private Point startPoint;
	private Point endPoint;

	Shape selectedShape;

	private String command;
	private String drawCommand;
	private ArrayList<String> tempCommands = new ArrayList<String>();
	private ArrayList<String> commands = new ArrayList<String>();
	private Stack<Shape> deletedShapes = new Stack<Shape>();
	private Stack<Shape> tempShapes = new Stack<Shape>();
	private Stack<Integer> indexs = new Stack<Integer>();
	private Stack<Shape> tempDeletedShapes = new Stack<Shape>();

	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
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
		} else if (selectedShape == null) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR", JOptionPane.ERROR_MESSAGE);
		} else {
			int option = JOptionPane.showConfirmDialog(null, "DO YOU WANT TO DELETE SELECTED SHAPE");
			if (option == JOptionPane.YES_OPTION) {
				deletedShapes.push(selectedShape);
				command = "Deleted!" + selectedShape + "\n";
				frame.commandList.append(command);
				commands.add(command);
				indexs.push(model.getShapes().indexOf(selectedShape));
				model.getShapes().remove(selectedShape);
				deselect();
				frame.repaint();
			}
		}
	}

	public void select(int x, int y) {
		boolean blank = false;
		boolean go = true;
		ArrayList<Shape> shapes = model.getShapes();
		for (int br = shapes.size() - 1; br >= 0; br--) {
			shapes.get(br).setSelected(false);
			frame.repaint();
			if (shapes.get(br).contains(x, y) && go) {
				shapes.get(br).setSelected(true);
				selectedShape = shapes.get(br);
				blank = true;
				go = false;
			}
		}
		if (!blank) {
			selectedShape = null;
		}
	}

	public void editSelectedShape() {
		setPointsToNull();
		if (model.getShapes().size() == 0) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO DRAWN SHAPES", "ERROR", JOptionPane.ERROR_MESSAGE);
		} else if (selectedShape == null) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR", JOptionPane.ERROR_MESSAGE);
		} else {
			if (selectedShape instanceof Point) {
				Point p = (Point) selectedShape;
				Color pointColor = p.getColor();
				DlgPoint dlgPoint = new DlgPoint(pointColor);
				dlgPoint.txtX.setText(Integer.toString(p.getX()));
				dlgPoint.txtY.setText(Integer.toString(p.getY()));
				dlgPoint.btnColor.setBackground(p.getColor());
				dlgPoint.setVisible(true);
				if (dlgPoint.getPoint() != null) {
					p.setX(dlgPoint.getPoint().getX());
					p.setY(dlgPoint.getPoint().getY());
					p.setColor(dlgPoint.getColor());
					frame.repaint();
					setAllFalse();
				}
			} else if (selectedShape instanceof Rectangle) {
				Rectangle r = (Rectangle) selectedShape;
				Color recColor = r.getColor();
				Color recInnerColor = r.getInnerColor();
				DlgRectangle dlgRectangle = new DlgRectangle(recColor, recInnerColor);
				dlgRectangle.txtUpperX.setText(Integer.toString(r.getUpperLeftPoint().getX()));
				dlgRectangle.txtUpperY.setText(Integer.toString(r.getUpperLeftPoint().getY()));
				dlgRectangle.txtWidth.setText(Integer.toString(r.getWidth()));
				dlgRectangle.txtHeight.setText(Integer.toString(r.getHeight()));
				dlgRectangle.btnColor.setBackground(r.getColor());
				dlgRectangle.btnInnerColor.setBackground(r.getInnerColor());
				dlgRectangle.setVisible(true);
				if (dlgRectangle.getRectangle() != null) {
					Rectangle r2 = dlgRectangle.getRectangle();
					Point p = new Point(r2.getUpperLeftPoint().getX(), r2.getUpperLeftPoint().getY());
					r.setUpperLeftPoint(p);
					r.setWidth(r2.getWidth());
					r.setHeight(r2.getHeight());
					r.setColor(dlgRectangle.getColor());
					r.setInnerColor(dlgRectangle.getInnerColor());
					frame.repaint();
					setAllFalse();
				}
			} else if (selectedShape instanceof Line) {
				Line l = (Line) selectedShape;
				Color lineColor = l.getColor();
				DlgLine dlgLine = new DlgLine(lineColor);
				dlgLine.txtStartX.setText(Integer.toString(l.getStartPoint().getX()));
				dlgLine.txtStartY.setText(Integer.toString(l.getStartPoint().getY()));
				dlgLine.txtEndX.setText(Integer.toString(l.getEndPoint().getX()));
				dlgLine.txtEndY.setText(Integer.toString(l.getEndPoint().getY()));
				dlgLine.btnColor.setBackground(lineColor);
				dlgLine.setVisible(true);
				if (dlgLine.getLine() != null) {
					Line l2 = dlgLine.getLine();
					Point startPoint = new Point(l2.getStartPoint().getX(), l2.getStartPoint().getY());
					Point endPoint = new Point(l2.getEndPoint().getX(), l2.getEndPoint().getY());
					l.setStartPoint(startPoint);
					l.setEndPoint(endPoint);
					l.setColor(dlgLine.getColor());
					frame.repaint();
					setAllFalse();
				}
			} else if (selectedShape instanceof Donut) {
				Donut d = (Donut) selectedShape;
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
				if (dlgDonut.getDonut() != null) {
					Donut d2 = dlgDonut.getDonut();
					Point p2 = new Point(d2.getCenter().getX(), d2.getCenter().getY());
					d.setCenter(p2);
					d.setRadius(d2.getRadius());
					d.setInnerRadius(d2.getInnerRadius());
					d.setColor(dlgDonut.getColor());
					d.setInnerColor(dlgDonut.getInnerColor());
					frame.repaint();
					setAllFalse();
				}
			} else if (selectedShape instanceof Circle) {
				Circle c = (Circle) selectedShape;
				Color circleColor = c.getColor();
				Color circleInnerColor = c.getInnerColor();
				DlgCircle dlgCircle = new DlgCircle(circleColor, circleInnerColor);
				dlgCircle.txtCenterX.setText(Integer.toString(c.getCenter().getX()));
				dlgCircle.txtCenterY.setText(Integer.toString(c.getCenter().getY()));
				dlgCircle.txtRadius.setText(Integer.toString(c.getRadius()));
				dlgCircle.btnColor.setBackground(circleColor);
				dlgCircle.btnInnerColor.setBackground(circleInnerColor);
				dlgCircle.setVisible(true);
				if (dlgCircle.getCircle() != null) {
					Circle c2 = dlgCircle.getCircle();
					Point p1 = new Point(c2.getCenter().getX(), c2.getCenter().getY());
					c.setCenter(p1);
					c.setRadius(c2.getRadius());
					c.setColor(dlgCircle.getColor());
					c.setInnerColor(dlgCircle.getInnerColor());
					frame.repaint();
					setAllFalse();
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
						frame.repaint();
					}
				} else if (frame.tglBtnSelect.isSelected()) {
					setPointsToNull();
					select(e.getX(), e.getY());
				}
			}
		};
	}

	public void undo() {
		if (commands.size() != 0) {
			if (readCommand().equals("Added")) {
				tempCommands.add(commands.remove(commands.size() - 1));
				removeCommandsFromList();
				AddShapeCmd asc = new AddShapeCmd(model.get(model.getShapes().size() - 1), model);
				tempShapes.push(model.get(model.getShapes().size() - 1));
				asc.unexecute();
				frame.repaint();
			} else if (readCommand().equals("Deleted")) {
				tempCommands.add(commands.remove(commands.size() - 1));
				removeCommandsFromList();
				deletedShapes.peek().setSelected(false);
				tempDeletedShapes.push(deletedShapes.peek());
				RemoveShapeCmd rsc = new RemoveShapeCmd(deletedShapes.pop(), model, indexs.pop());
				rsc.unexecute();
				frame.repaint();
			}
		}
	}

	public void redo() {
		if (tempCommands.size() != 0) {
			if (readUndoCommand().equals("Added")) {
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				removeCommandsFromList();
				AddShapeCmd asc = new AddShapeCmd(tempShapes.pop(), model);
				asc.execute();
				frame.repaint();
			} else if (readUndoCommand().equals("Deleted")) {
				commands.add(tempCommands.remove(tempCommands.size() - 1));
				removeCommandsFromList();
				deletedShapes.push(tempDeletedShapes.peek());
				indexs.push(model.getShapes().indexOf(tempDeletedShapes.peek()));
				RemoveShapeCmd rsc = new RemoveShapeCmd(tempDeletedShapes.pop(), model, 0);
				rsc.execute();
				frame.repaint();
			}
		}
	}

	private String readUndoCommand() {
		String[] parts = tempCommands.get(tempCommands.size() - 1).split("!");
		drawCommand = parts[0];
		return drawCommand;
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