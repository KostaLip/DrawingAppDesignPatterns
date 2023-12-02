package geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Circle extends Shape {
	private Point center = new Point();
	protected int radius;

	public Circle() {

	}

	public Circle(Point center, int radius) {
		this.center = center;
		this.radius = radius;
	}

	public Circle(Point center, int radius, boolean selected) {
		this(center, radius);
		setSelected(selected);
	}

	public boolean equals(Object obj) {
		if (obj instanceof Circle) {
			Circle pomocni = (Circle) obj;
			if (this.center.equals(pomocni.center) && this.radius == pomocni.radius) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void moveTo(int x, int y) {
		this.center.moveTo(x, y);
	}

	public void moveBy(int byX, int byY) {
		this.center.moveBy(byX, byY);

	}

	public int compareTo(Object obj) {
		if (obj instanceof Circle) {
			Circle shapeToCompare = (Circle) obj;
			return (int) (this.area() - shapeToCompare.area());
		}
		return 0;
	}

	public double area() {
		return radius * getRadius() * Math.PI;
	}

	public double circumference() {
		return 2 * radius * Math.PI;
	}

	public boolean contains(int x, int y) {
		return center.distance(x, y) <= radius;
	}

	public void draw(Graphics g) {
		g.setColor(getInnerColor());
		g.fillOval(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);
		g.setColor(super.getColor());
		g.drawOval(center.getX() - radius, center.getY() - radius, 2 * radius, 2 * radius);

		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(center.getX() - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() - radius - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() + radius - 2, center.getY() - 2, 4, 4);
			g.drawRect(center.getX() - 2, center.getY() - radius - 2, 4, 4);
			g.drawRect(center.getX() - 2, center.getY() + radius - 2, 4, 4);
			g.setColor(Color.black);
		}

	}

	public boolean contains(Point clickPoint) {
		return center.distance(clickPoint.getX(), clickPoint.getY()) <= radius;
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public int getRadius() {
		return this.radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public Circle clone(Circle circle) {
		
		circle.getCenter().setX(this.getCenter().getX());
		circle.getCenter().setY(this.getCenter().getY());
		circle.setRadius(this.getRadius());
		circle.setColor(this.getColor());
		circle.setInnerColor(this.getInnerColor());
		
		return circle;
		
	}

	public String toString() {
		return "Circle->Center:" + "X=" + center.getX() + ",Y=" + center.getY() + ",radius=" + radius
				+ " Color:" + getColor() + ",InnerColor:" + getInnerColor();
	}
}
