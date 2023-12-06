package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public abstract class Shape implements Moveble, Comparable, Serializable {
	private boolean selected;
	private Color color = Color.BLACK;
	private Color innerColor = Color.WHITE;

	public Shape() {

	}

	public Shape(boolean selected) {
		this.selected = selected;
	}

	public abstract boolean contains(int x, int y);

	public abstract void draw(Graphics g);

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Color getInnerColor() {
		return this.innerColor;
	}

	public void setInnerColor(Color c) {
		this.innerColor = c;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
