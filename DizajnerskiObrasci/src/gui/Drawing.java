package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import gui.Paint;

public class Drawing extends JPanel {

	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	int br;
	private Shape selectedShape;
	private Color boja;

	public Drawing() {
		setBackground(Color.WHITE);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Iterator<Shape> it = shapes.iterator();
		while (it.hasNext()) {
			Shape sh = it.next();
			sh.draw(g);
		}
	}

	public void addShape(Shape sh) {
		shapes.add(sh);
		repaint();
	}

	public ArrayList<Shape> getShapes() {
		return shapes;
	}

	public void select(int x, int y) {
		boolean blank = false;
		boolean go = true;
		for (br = shapes.size() - 1; br >= 0; br--) {
			shapes.get(br).setSelected(false);
			repaint();
			if (shapes.get(br).contains(x, y) && go) {
				shapes.get(br).setSelected(true);
				selectedShape = shapes.get(br);
				blank = true;
				go = false;
				repaint();
			}
		}
		if (!blank) {
			selectedShape = null;
		}
	}

	public void setAllFalse() {
		for (br = shapes.size() - 1; br >= 0; br--) {
			shapes.get(br).setSelected(false);
			repaint();
		}
		selectedShape = null;
	}

	public Shape getSelectedShape() {
		return selectedShape;
	}

	public void setSelectedShape() {
		this.selectedShape = null;
	}

	public boolean isShapesEmpty() {
		return shapes.isEmpty();
	}

	public void setColor(Color r) {
		this.boja = r;
	}
}
