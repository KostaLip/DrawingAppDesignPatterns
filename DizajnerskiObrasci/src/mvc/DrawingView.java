package mvc;

import java.awt.Color;
import java.awt.Graphics;
import geometry.Shape;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

public class DrawingView extends JPanel {

	DrawingModel model = new DrawingModel();
	Shape selectedShape;
	
	public DrawingView() {
		setBackground(Color.WHITE);
	}
	
	public void setModel(DrawingModel model) {
		this.model = model;
	}
	
	public void select(int x, int y) {
		boolean blank = false;
		boolean go = true;
		ArrayList<Shape> shapes = model.getShapes();
		for (int br = shapes.size() - 1; br >= 0; br--) {
			shapes.get(br).setSelected(false);
			repaint();
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
	
	public void setAllFalse() {
		ArrayList<Shape> shapes = model.getShapes();
		for (int br = shapes.size() - 1; br >= 0; br--) {
			shapes.get(br).setSelected(false);
			repaint();
		}
		selectedShape = null;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Iterator<Shape> it = model.getShapes().iterator();
		while(it.hasNext()) {
			Shape sh = it.next();
			sh.draw(g);
		}
	}
	
}