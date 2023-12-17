package geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Shape {
	private Point upperLeftPoint = new Point(); 
	private int width;
	private int height;
	private Color innerColor = Color.WHITE;

	public Rectangle() {
	}

	public Rectangle(Point upperLeftPoint, int width, int height) {
		this.upperLeftPoint = upperLeftPoint;
		this.width = width;
		this.height = height;
	}

	public Rectangle(Point upperLeftPoint, int width, int height, boolean selected) {

		this(upperLeftPoint, width, height);
		setSelected(selected);
	}

	public boolean equals(Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle pomocna = (Rectangle) obj;
			if (this.upperLeftPoint.equals(pomocna.upperLeftPoint) && this.width == pomocna.width
					&& this.height == pomocna.height && this.getColor().equals(pomocna.getColor()) && this.getInnerColor().equals(pomocna.getInnerColor()))
				return true;
			else
				return false;
		} else
			return false;
	}

	public void draw(Graphics g) {
		g.setColor(getInnerColor());
		g.fillRect(upperLeftPoint.getX(), upperLeftPoint.getY(), width, height);
		g.setColor(super.getColor());
		g.drawRect(upperLeftPoint.getX(), upperLeftPoint.getY(), width, height);

		if (isSelected()) {
			g.setColor(Color.blue);
			g.drawRect(upperLeftPoint.getX() - 2, upperLeftPoint.getY() - 2, 4, 4);
			g.drawRect(upperLeftPoint.getX() + width - 2, upperLeftPoint.getY() - 2, 4, 4);
			g.drawRect(upperLeftPoint.getX() - 2, upperLeftPoint.getY() + height - 2, 4, 4);
			g.drawRect(upperLeftPoint.getX() + width - 2, upperLeftPoint.getY() + height - 2, 4, 4);
		}

	}

	public void moveTo(int x, int y) {
		this.upperLeftPoint.moveTo(x, y);
	}

	public void moveBy(int byX, int byY) {
		this.upperLeftPoint.moveBy(byX, byY);

	}

	public int compareTo(Object obj) {
		if (obj instanceof Rectangle) {
			Rectangle shapeToCompare = (Rectangle) obj;
			return (this.area() - shapeToCompare.area());
		}
		return 0;
	}

	public int area() {
		return width * height;
	}

	public int circumference() {
		return 2 * (width + height);
	}

	public boolean contains(int x, int y) {
		return x >= upperLeftPoint.getX() && x <= this.upperLeftPoint.getX() + width && y >= upperLeftPoint.getY()
				&& y <= upperLeftPoint.getY() + height;
	}

	public boolean contains(Point clickPoint) {
		return clickPoint.getX() >= upperLeftPoint.getX() && clickPoint.getX() <= this.upperLeftPoint.getX() + width
				&& clickPoint.getY() >= upperLeftPoint.getY() && clickPoint.getY() <= upperLeftPoint.getY() + height;
	}

	public Color getInnerColor() {
		return innerColor;
	}

	public void setInnerColor(Color innerColor) {
		this.innerColor = innerColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setUpperLeftPoint(Point upperLeftPoint) {
		this.upperLeftPoint = upperLeftPoint;
	}

	public Point getUpperLeftPoint() {
		return upperLeftPoint;
	}
	
	public Rectangle clone(Rectangle rectangle) {
		
		rectangle.getUpperLeftPoint().setX(this.getUpperLeftPoint().getX());
		rectangle.getUpperLeftPoint().setY(this.getUpperLeftPoint().getY());
		rectangle.setHeight(this.getHeight());
		rectangle.setWidth(this.getWidth());
		rectangle.setColor(this.getColor());
		rectangle.setInnerColor(this.getInnerColor());
		
		return rectangle;
		
	}

	public String toString() {
		return "Rectangle->UpperLeftPoint:X=" + upperLeftPoint.getX() + ",Y=" + upperLeftPoint.getY() + 
				",width=" + width + ",height=" + height + " Color:" + getColor() + ",InnerColor:" + getInnerColor();
	}
}
