package ch.daniel.karateseon.gridcalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Level;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		DrawingCalculator calculator = new DrawingCalculator("F:\\development\\temp\\output");

		List<FilterCriteria> filters = createFilters();
		
		calculator.calculateGrid(new File("F:\\development\\temp\\testliste\\teilnehmer.xlsx"), filters);
	}

	private static List<FilterCriteria> createFilters() {
		List<FilterCriteria> filters = Lists.newLinkedList();
		filters.add(FilterCriteria.createKata(Gender.MALE, 1998, 2001, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2001, 2003, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2003, 2006, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 1999, 2002, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2002, 2004, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2004, 2006, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2006, 2008, Level.LOWER_STAGE));
		
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 1997, 2000, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2000, 2002, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2002, 2006, Level.UPPER_STAGE));
		
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 1998, 2002, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2002, 2004, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2004, 2005, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2005, 2007, Level.LOWER_STAGE));
		
		filters.add(FilterCriteria.createKumite(Gender.MALE, 1998, 2001, 0, 57, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 1998, 2001, 57, Double.MAX_VALUE, "schwer"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2001, 2003, 30, 42, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2001, 2003, 42, Double.MAX_VALUE, "schwer"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2003, 2005, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2005, 2008, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 1997, 2000, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2000, 2002, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2002, 2005, 0, 40, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2002, 2005, 40, Double.MAX_VALUE, "schwer"));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2005, 2007, 0, Double.MAX_VALUE, ""));
		return filters;
	}

}
