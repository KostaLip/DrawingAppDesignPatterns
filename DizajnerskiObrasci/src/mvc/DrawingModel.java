package mvc;

import java.util.ArrayList;

import geometry.Shape;

public class DrawingModel {

	public ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	public void add(Shape sh) {
		shapes.add(sh);
	}
	
	public void remove(Shape sh) {
		shapes.remove(sh);
	}
	
	public Shape get(int index) {
		return shapes.get(index);
	}
	
	public ArrayList<Shape> getShapes() {
		return this.shapes;
	}
	
}
