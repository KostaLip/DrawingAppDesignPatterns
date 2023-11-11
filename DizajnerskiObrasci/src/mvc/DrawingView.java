package mvc;

import java.awt.Color;
import java.awt.Graphics;
import geometry.Shape;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

public class DrawingView extends JPanel {

	DrawingModel model = new DrawingModel();
	
	public DrawingView() {
		setBackground(Color.WHITE);
	}
	
	public void setModel(DrawingModel model) {
		this.model = model;
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