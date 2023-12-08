package strategy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import mvc.DrawingController;
import mvc.DrawingFrame;

public class LoadSaveTxt implements LoadSaveStrategy {

	private DrawingFrame frame;
	private DrawingController controller;
	
	public LoadSaveTxt(DrawingFrame frame, DrawingController controller) {
		this.frame = frame;
		this.controller = controller;
	}
	
	@Override
	public void saveF(String path) {
		try(BufferedWriter bwriter = new BufferedWriter(new FileWriter(path))) {
			String text = frame.commandList.getText();
			bwriter.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void load(String path) {
		controller.loadTxt();
	}

}
