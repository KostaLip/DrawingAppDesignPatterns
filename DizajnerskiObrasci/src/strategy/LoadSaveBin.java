package strategy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import geometry.Shape;
import mvc.DrawingController;
import mvc.DrawingModel;

public class LoadSaveBin implements LoadSaveStrategy {

	DrawingModel model;
	DrawingController controller;
	
	public LoadSaveBin(DrawingModel model, DrawingController controller) {
		this.model = model;
		this.controller = controller;
	}

	@Override
	public void saveF(String path) {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))){
            ArrayList<Shape> shapes = model.getShapes();
            outputStream.writeObject(shapes);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void load(String path) {
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path))) {
	        @SuppressWarnings("unchecked")
			ArrayList<Shape> shapes = (ArrayList<Shape>) inputStream.readObject();
	        model.getShapes().clear();
	        for (Shape shape : shapes) {
	        	if(shape.isSelected()) {
	        		controller.selectedShapesList.add(shape);
	        	}
	            model.getShapes().add(shape);
	        }
	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}

}
