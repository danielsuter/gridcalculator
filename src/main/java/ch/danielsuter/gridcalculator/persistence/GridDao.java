package ch.danielsuter.gridcalculator.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.danielsuter.gridcalculator.model.Grid;

public class GridDao implements Dao<Iterable<Grid>> {
	private XStreamHelper xstreamHelper = new XStreamHelper();

	public void save(File destination, Iterable<Grid> grids) throws IOException {
		xstreamHelper.save(destination, grids);
	}

	@SuppressWarnings("unchecked")
	public Iterable<Grid> load(File input) throws FileNotFoundException {
		return (Iterable<Grid>) xstreamHelper.load(input);
	}

}
