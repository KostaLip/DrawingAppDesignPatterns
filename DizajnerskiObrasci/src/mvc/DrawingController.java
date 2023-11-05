package mvc;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import dialogs.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgDonut;
import dialogs.DlgCircle;

public class DrawingController {

	DrawingModel model;
	DrawingFrame frame;
	
	private Point startPoint;
	private Point endPoint;
	
	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
	}
	
	public void mouseClicked(MouseEvent e) {
		Point point = new Point(e.getX(), e.getY());
		model.add(point);
		frame.repaint();
	}
	
	public void setPointsToNull() {
		this.startPoint = null;
		this.endPoint = null;
	}
	
	public void deselect() {
		frame.getView().setAllFalse();
		frame.repaint();
	}
	
	public void deleteSelectedShape() {
		setPointsToNull();
		if (model.getShapes().size() == 0) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO DRAWN SHAPES", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		} else if (frame.getView().selectedShape == null) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		} else {
			int option = JOptionPane.showConfirmDialog(null, "DO YOU WANT TO DELETE SELECTED SHAPE");
			if (option == JOptionPane.YES_OPTION) {
				model.getShapes().remove(frame.view.selectedShape);
				deselect();
				frame.repaint();
			}
		}
	}
	
	public void editSelectedShape() {
		setPointsToNull();
		if (model.getShapes().size() == 0) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO DRAWN SHAPES", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		} else if (frame.getView().selectedShape == null) {
			JOptionPane.showMessageDialog(null, "THERE ARE NO SELECTED SHAPES", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		} else {
			if (frame.getView().selectedShape instanceof Point) {
				Point p = (Point) frame.getView().selectedShape;
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
					frame.getView().setAllFalse();
				}
			} else if (frame.getView().selectedShape instanceof Rectangle) {
				Rectangle r = (Rectangle) frame.getView().selectedShape;
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
					frame.getView().setAllFalse();
				}
			} else if (frame.getView().selectedShape instanceof Line) {
				Line l = (Line) frame.getView().selectedShape;
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
					frame.getView().setAllFalse();
				}
			} else if (frame.getView().selectedShape instanceof Donut) {
				Donut d = (Donut) frame.getView().selectedShape;
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
					frame.getView().setAllFalse();
				}
			} else if (frame.getView().selectedShape instanceof Circle) {
				Circle c = (Circle) frame.getView().selectedShape;
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
					frame.getView().setAllFalse();
				}
			}
		}
	}
	
	public MouseAdapter viewMouseListener() {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(frame.tglBtnPoint.isSelected()) {
					setPointsToNull();
					Point clickPoint = new Point(e.getX(), e.getY());
					clickPoint.setColor(frame.color);
					model.add(clickPoint);
					frame.repaint();
				} else if(frame.tglBtnLine.isSelected()) {
					if(startPoint == null && endPoint == null) {
						startPoint = new Point(e.getX(), e.getY());
					} else if (startPoint != null && endPoint == null) {
						endPoint = new Point(e.getX(), e.getY());
						Line line = new Line(startPoint, endPoint);
						line.setColor(frame.color);
						model.add(line);
						startPoint = null;
						endPoint = null;
						frame.repaint();
					}
				} else if(frame.tglBtnRectangle.isSelected()) {
					setPointsToNull();
					DlgRectangle dlgRectangle = new DlgRectangle(frame.color, frame.innerColor);
					dlgRectangle.txtUpperX.setText(Integer.toString(e.getX()));
					dlgRectangle.txtUpperY.setText(Integer.toString(e.getY()));
					dlgRectangle.setVisible(true);
					if (dlgRectangle.getRectangle() != null) {
						model.add(dlgRectangle.getRectangle());
						frame.repaint();
					}
				} else if(frame.tglBtnCircle.isSelected()) {
					setPointsToNull();
					DlgCircle dlgCircle = new DlgCircle(frame.color, frame.innerColor);
					dlgCircle.txtCenterX.setText(Integer.toString(e.getX()));
					dlgCircle.txtCenterY.setText(Integer.toString(e.getY()));
					dlgCircle.setVisible(true);
					if (dlgCircle.getCircle() != null) {
						model.add(dlgCircle.getCircle());
						frame.repaint();
					}
				} else if(frame.tglBtnDonut.isSelected()) {
					setPointsToNull();
					DlgDonut dlgDonut = new DlgDonut(frame.color, frame.innerColor);
					dlgDonut.txtCentarX.setText(Integer.toString(e.getX()));
					dlgDonut.txtCentarY.setText(Integer.toString(e.getY()));
					dlgDonut.setVisible(true);
					if (dlgDonut.getDonut() != null) {
						model.add(dlgDonut.getDonut());
						frame.repaint();
					}
				} else if(frame.tglBtnSelect.isSelected()) {
					setPointsToNull();
					frame.view.select(e.getX(), e.getY());
				}
			}
		};
	}
	
}