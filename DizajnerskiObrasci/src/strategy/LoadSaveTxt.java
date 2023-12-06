package strategy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import mvc.DrawingFrame;

public class LoadSaveTxt implements LoadSaveStrategy {

	private DrawingFrame frame;
	
	public LoadSaveTxt(DrawingFrame frame) {
		this.frame = frame;
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
		// TODO Auto-generated method stub
		
	}

}
