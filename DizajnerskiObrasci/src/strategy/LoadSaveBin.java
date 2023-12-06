package strategy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import geometry.Shape;
import mvc.DrawingModel;

public class LoadSaveBin implements LoadSaveStrategy {

	DrawingModel model;
	
	public void SaveToBin(DrawingModel model) {
		this.model = model;
	}

	@Override
	public void saveF(String path) {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            ArrayList<Shape> shapes = model.getShapes();
            outputStream.writeObject(shapes);
            System.out.println("Crtez je uspešno sačuvan u datoteku kao binarni fajl.");
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
	        	System.out.println(shape);
	            model.getShapes().add(shape);
	        }
	        System.out.println("Ucitano iz binarnog fajla: " + path);
	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}

}
