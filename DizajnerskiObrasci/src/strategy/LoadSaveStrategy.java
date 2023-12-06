package strategy;

import java.lang.String;

public interface LoadSaveStrategy {

	public void saveF(String path);
	public void load(String path);
	
}
