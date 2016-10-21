package ch.danielsuter.gridcalculator.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Gender;
import ch.danielsuter.gridcalculator.model.Level;
import ch.danielsuter.gridcalculator.model.Participant;

public class GridCalculatorFormatterTest {

	@Test
	public void formatForGroupSheetHeaderKata() {
		String result = GridCalculatorFormatter.formatForGroupSheetHeader(FilterCriteria.createKata(Gender.MALE, 2000,
				2003, Level.LOWER_STAGE));
		assertEquals("Kata Knaben Unterstufe 2000-2002", result);
	}

	@Test
	public void formatForGroupSheetHeaderKumite() {
		String result = GridCalculatorFormatter.formatForGroupSheetHeader(FilterCriteria.createKumite(Gender.MALE,
				2000, 2003, 0, 0, "leicht"));
		assertEquals("Kumite Knaben 2000-2002 leicht", result);

	}
	
	@Test
	public void formatForGroupSheetHeaderWithoutWeightString() {
		String result = GridCalculatorFormatter.formatForGroupSheetHeader(FilterCriteria.createKumite(Gender.FEMALE,
				2000, 2003, 0, 0, null));
		assertEquals("Kumite Mädchen 2000-2002", result);
	}

	@Test
	public void formatForGrid() {
		String result = GridCalculatorFormatter.formatForGrid(new Participant("Sepp", "Meier", "Club 1"));
		assertEquals("Meier, Sepp", result);
	}

	@Test
	public void formatForFilenameGroupSheet() {
		String result = GridCalculatorFormatter.formatForFilename(
				FilterCriteria.createKata(Gender.MALE, 2000, 2003, Level.LOWER_STAGE), "group");
		assertEquals("kata-male-2000_2002-unterstufe-group", result);
	}

	@Test
	public void formatForFilenameDrawing() {
		String result = GridCalculatorFormatter.formatForFilename(
				FilterCriteria.createKumite(Gender.FEMALE, 1999, 2003, 0, 0, "schwer"), "drawing");
		assertEquals("kumite-female-1999_2002-schwer-drawing", result);
	}

	@Test
	public void formatForFilenameDrawingWithoutWeightString() {
		String result = GridCalculatorFormatter.formatForFilename(
				FilterCriteria.createKumite(Gender.FEMALE, 1999, 2003, 0, 0, null), "drawing");
		assertEquals("kumite-female-1999_2002-drawing", result);
	}
}
