package ch.danielsuter.gridcalculator.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Dao<T> {
	public void save(File destination, T objectToSave) throws IOException;
	
	public T load(File input) throws FileNotFoundException;
}
