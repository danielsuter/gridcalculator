package ch.danielsuter.gridcalculator.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;

public class FilterDao implements Dao<Iterable<FilterCriteria>> {
	private XStreamHelper xstreamHelper;
	
	public FilterDao() {
		xstreamHelper = new XStreamHelper();
	}
	
	public void save(File destination, Iterable<FilterCriteria> filterCriteria) throws IOException {
		xstreamHelper.save(destination, filterCriteria);
	}

	@SuppressWarnings("unchecked")
	public Iterable<FilterCriteria> load(File input) throws FileNotFoundException {
		return (Iterable<FilterCriteria>) xstreamHelper.load(input);
	}
}
