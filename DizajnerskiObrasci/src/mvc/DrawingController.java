package mvc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import dialogs.DlgRectangle;
import geometry.Line;
import geometry.Point;
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
				}
			}
		};
	}
	
}
