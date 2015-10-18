package ch.daniel.karateseon.gridcalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Level;
import ch.daniel.karateseon.gridcalculator.util.MyLogger;

public class DrawingCalculatorTest extends TestBase {
	private final static MyLogger logger = MyLogger.getLogger(DrawingCalculatorTest.class);
	private DrawingCalculator calculator;

	@Before
	public void setup() throws IOException {
		File outputDirectory = new File(BASE_PATH, "drawing-calculator-test");
		FileUtils.deleteDirectory(outputDirectory);
		FileUtils.forceMkdir(outputDirectory);

		logger.info("Writing files to {0}", outputDirectory.getAbsolutePath());
		calculator = new DrawingCalculator(outputDirectory.getAbsolutePath());
	}

	@Test
	public void testFullListWithSimpleFilters() throws FileNotFoundException, IOException {
		List<FilterCriteria> filterCriteria = Lists.newLinkedList();
		filterCriteria.add(FilterCriteria
				.createKumite(Gender.MALE, 0, Integer.MAX_VALUE, 0, Double.MAX_VALUE, "schwer"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.FEMALE, 0, Integer.MAX_VALUE, 0, Double.MAX_VALUE,
				"schwer"));

		filterCriteria.add(FilterCriteria.createKata(Gender.MALE, 0, Integer.MAX_VALUE, Level.LOWER_STAGE));
		filterCriteria.add(FilterCriteria.createKata(Gender.MALE, 0, Integer.MAX_VALUE, Level.UPPER_STAGE));

		filterCriteria.add(FilterCriteria.createKata(Gender.FEMALE, 0, Integer.MAX_VALUE, Level.LOWER_STAGE));
		filterCriteria.add(FilterCriteria.createKata(Gender.FEMALE, 0, Integer.MAX_VALUE, Level.UPPER_STAGE));

		calculator.calculateGrid(new File(TEST_RESOURCES, "teilnehmer_mixed.xlsx"), filterCriteria);
	}

	@Test
	public void testSmallListForAllvsAllDrawing() throws FileNotFoundException, IOException {
		List<FilterCriteria> filterCriteria = Lists.newLinkedList();
		filterCriteria.add(FilterCriteria.createKata(Gender.MALE, 1999, 2004, Level.LOWER_STAGE));
		calculator.calculateGrid(new File(TEST_RESOURCES, "teilnehmer_kata_small.xlsx"), filterCriteria);
	}
}
