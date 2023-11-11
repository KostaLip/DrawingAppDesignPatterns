package geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Line extends Shape {
	private Point startPoint;
	private Point endPoint;

	public Line() {

	}

	public Line(Point startPoint, Point endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public Line(Point startPoint, Point endPoint, boolean selected) {
		super(selected);
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Line) {
			Line pomocna = (Line) obj;
			if (this.startPoint.equals(pomocna.startPoint) && this.endPoint.equals(pomocna.endPoint))
				return true;
			else
				return false;
		} else
			return false;
	}

	public double length() {
		return this.startPoint.distance(endPoint.getX(), getEndPoint().getY());
	}

	public void draw(Graphics g) {
		g.setColor(super.getColor());
		g.drawLine(startPoint.getX(), startPoint.getY(), endPoint.getX(), this.endPoint.getY());
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(startPoint.getX() - 2, startPoint.getY() - 2, 4, 4);
			g.drawRect(endPoint.getX() - 2, endPoint.getY() - 2, 4, 4);
			g.setColor(Color.black);
		}

	}

	public void moveTo(int x, int y) {
		this.startPoint.moveTo(x, y);
		this.endPoint.moveTo(x, y);

	}

	public void moveBy(int byX, int byY) {
		this.startPoint.moveBy(byX, byY);
		this.endPoint.moveBy(byX, byY);

	}

	public int compareTo(Object obj) {
		if (obj instanceof Line) {
			Line shapeToCompare = (Line) obj;
			return (int) (this.length() - shapeToCompare.length());
		}
		return 0;
	}

	public boolean contains(int x, int y) {
		return this.startPoint.distance(x, y) + this.endPoint.distance(x, y) - length() <= 2;
	}

	public boolean contains(Point clickPoint) {
		return this.startPoint.distance(clickPoint.getX(), clickPoint.getY())
				+ this.endPoint.distance(clickPoint.getX(), clickPoint.getY()) - length() <= 2;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getStartPoint() {
		return this.startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public String toString() {
		return "Line->StartPoint:X=" + startPoint.getX() + ",Y=" + startPoint.getY() + ";"
	+ "EndPoint:X=" + endPoint.getX() + ",Y=" + endPoint.getY() + " Color:" + getColor();
	}
}
