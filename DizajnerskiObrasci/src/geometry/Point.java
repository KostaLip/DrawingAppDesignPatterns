package geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Point extends Shape {
	private int x;
	private int y;

	public Point() {

	}

	public Point(int x, int y) {
		this.x = x;
		setY(y);
	}

	public Point(int x, int y, boolean selected) {
		this(x, y);
		setSelected(selected);
	}

	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point pomocna = (Point) obj;
			if (this.x == pomocna.x && this.y == pomocna.y) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(int x, int y) {
		return this.distance(x, y) <= 10;
	}

	public boolean contains(Point clickPoint) {
		return this.distance(clickPoint.x, clickPoint.y) <= 2;
	}

	public double distance(int xPoint2, int yPoint2) {
		double dx = this.x - xPoint2;
		double dy = this.y - yPoint2;
		double d = Math.sqrt(dx * dx + dy * dy);
		return d;
	}

	public void draw(Graphics g) {
		g.setColor(super.getColor());
		g.drawLine(x - 2, y, x + 2, y);
		g.drawLine(x, y - 2, x, y + 2);

		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(x - 2, y - 2, 4, 4);
			g.setColor(Color.black);
		}

	}

	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public void moveBy(int byX, int byY) {
		this.x += byX;
		this.y += byY;
	}

	public int compareTo(Object obj) {
		if (obj instanceof Point) {
			Point shapeToCompare = (Point) obj;
			return (int) (this.distance(0, 0) - shapeToCompare.distance(0, 0));
		}
		return 0;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Point clone() {
		Point point = new Point();
		
		point.setX(this.getX());
		point.setY(this.getY());
		point.setColor(this.getColor());
		
		return point;
	}

	public String toString() {
		return "Point->X=" + x + ",Y=" + y + " Color:" + getColor();
	}

}
