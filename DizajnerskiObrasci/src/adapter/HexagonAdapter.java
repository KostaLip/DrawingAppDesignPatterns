package adapter;

import java.awt.Color;
import java.awt.Graphics;

import geometry.Circle;
import geometry.Shape;
import hexagon.Hexagon;

public class HexagonAdapter extends Shape {

	private Hexagon hexagon;
	
	public HexagonAdapter(Hexagon hexagon) {
		this.hexagon = hexagon;
	}
	
	public Color getColor() {
		return hexagon.getBorderColor();
	}
	
	public Color getInnerColor() {
		return hexagon.getAreaColor();
	}
	
	public void setColor(Color color) {
		this.hexagon.setBorderColor(color);
	}
	
	public void setInnerColor(Color color) {
		this.hexagon.setAreaColor(color);
	}
	
	public int getCenterX() {
		return hexagon.getX();
	}
	
	public int getCenterY() {
		return hexagon.getY();
	}
	
	public int getRadius() {
		return hexagon.getR();
	}
	
	public void setCenterX(int x) {
		this.hexagon.setX(x);
	}
	
	public void setCenterY(int y) {
		this.hexagon.setY(y);
	}
	
	public void setRadius(int r) {
		this.hexagon.setR(r);
	}
	
	public HexagonAdapter clone(HexagonAdapter hexagon) {
		
		hexagon.setCenterX(this.getCenterX());
		hexagon.setCenterY(this.getCenterY());
		hexagon.setColor(this.getColor());
		hexagon.setInnerColor(this.getInnerColor());
		hexagon.setRadius(this.getRadius());
		
		return hexagon;
		
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof HexagonAdapter) {
			HexagonAdapter pomocni = (HexagonAdapter) obj;
			if (this.getCenterX() == pomocni.getCenterX() && this.getRadius() == pomocni.getRadius()
					&& this.getCenterY() == pomocni.getCenterY() && this.getColor().equals(pomocni.getColor())
					&& this.getInnerColor().equals(pomocni.getInnerColor())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "Hexagon->Center:" + "X=" + getCenterX() + ",Y=" + getCenterY() + ",radius=" + getRadius()
				+ " Color:" + getColor() + ",InnerColor:" + getInnerColor();
	}
	
	@Override
	public void moveTo(int x, int y) {
		
	}

	@Override
	public void moveBy(int byX, int byY) {
		
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
		
		if(this.isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(getCenterX() - 2, getCenterY() - 2, 4, 4);
			g.drawRect(getCenterX() - getRadius() - 2, getCenterY() - 2, 4, 4);
			g.drawRect(getCenterX() + getRadius() - 2, getCenterY() - 2, 4, 4);
			g.drawRect(getCenterX() - 2, (int) (getCenterY() - (getRadius() / 2 * Math.sqrt(3)) - 2), 4, 4);
			g.drawRect(getCenterX() - 2, (int) (getCenterY() + (getRadius() / 2 * Math.sqrt(3)) - 2), 4, 4);
			g.setColor(Color.BLACK);
		}
	}

}
