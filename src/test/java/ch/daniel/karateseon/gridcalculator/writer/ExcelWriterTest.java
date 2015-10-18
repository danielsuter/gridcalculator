package ch.daniel.karateseon.gridcalculator.writer;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import ch.daniel.karateseon.gridcalculator.GridCalculator;
import ch.daniel.karateseon.gridcalculator.TestBase;
import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Grid;
import ch.daniel.karateseon.gridcalculator.model.GridSize;
import ch.daniel.karateseon.gridcalculator.model.Group;
import ch.daniel.karateseon.gridcalculator.model.Level;
import ch.daniel.karateseon.gridcalculator.model.Participant;
import ch.daniel.karateseon.gridcalculator.util.MyLogger;

public class ExcelWriterTest extends TestBase {
	private final static MyLogger logger = MyLogger.getLogger(GridCalculator.class);

	private ExcelWriter writer = new ExcelWriter();

	@Test
	public void simpleWrite() {
		Grid grid = createFullGrid(GridSize.SMALL);
		writeGrid(grid, "simple-write.xlsx");
	}

	@Test
	public void simpleWriteLarge() {
		Grid grid = createFullGrid(GridSize.LARGE);
		writeGrid(grid, "simple-write-large.xlsx");
	}

	private void writeGrid(Grid grid, String name) {
		File destination = new File(BASE_PATH, name);
		writer.write(grid, FilterCriteria.createKata(Gender.MALE, 1999, 2002, Level.UPPER_STAGE), destination, null);
		logger.info("Wrote target file to {0}", destination.getAbsolutePath());
	}

	private Grid createFullGrid(GridSize gridSize) {
		Participant[] participants = createParticipants(gridSize.getSize(), CLUB1);
		Grid grid = new Grid(gridSize, new Group());
		for (int positionIndex = 0; positionIndex < gridSize.getSize(); positionIndex++) {
			grid.setPosition(positionIndex, participants[positionIndex]);
		}
		return grid;
	}

	@Test
	public void testWriteParticipants() {
		Participant[] participants = createParticipants(9, CLUB1);
		File destination = new File(BASE_PATH, "group-sheet.xlsx");
		writer.writeGroupSheet(Arrays.asList(participants),
				FilterCriteria.createKumite(Gender.MALE, 1999, 2000, 45.5, 51.5, "Schwer"), destination);
		logger.info("Wrote target file to {0}", destination.getAbsolutePath());

	}
}
