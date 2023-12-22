package mvc;

import java.awt.Color;

import javax.swing.JFrame;

public class Application {

	public static void main(String[] args) {
		
		DrawingModel model = new DrawingModel();
		DrawingFrame frame = new DrawingFrame();
		DrawingController controller = new DrawingController(model, frame);
		
		frame.getView().setModel(model);
		frame.setController(controller);
		
		frame.setBounds(100, 100, 1200, 810);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Kosta Bjelogrlic IT31-2021");
		System.out.println("SRECNO MASTERU PARTIZAN SAMPION :)");
		frame.setVisible(true);
		
	}

}
