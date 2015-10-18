package ch.daniel.karateseon.gridcalculator.persistence;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.google.common.collect.Iterables;

import ch.daniel.karateseon.gridcalculator.GridCalculator;
import ch.daniel.karateseon.gridcalculator.TestBase;
import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Grid;
import ch.daniel.karateseon.gridcalculator.model.Group;
import ch.daniel.karateseon.gridcalculator.model.Level;

public class GridDaoTest extends TestBase {

	private GridDao gridDao = new GridDao();

	@Test
	public void saveAndLoadGrd() throws IOException {
		Grid grid = createGrid();

		File testFile = new File("target/grid1.xml");
		gridDao.save(testFile, Arrays.asList(grid));
		Iterable<Grid> loadedGrids = gridDao.load(testFile);

		assertEquals("Only one grid", 1, Iterables.size(loadedGrids));
		Grid loadedGrid = loadedGrids.iterator().next();
		assertEquals("Grid is the same", grid, loadedGrid);
	}

	private Grid createGrid() {
		FilterCriteria kataFilter = FilterCriteria.createKata(Gender.MALE, 0, Integer.MAX_VALUE, Level.LOWER_STAGE);
		Group group = new Group(kataFilter);
		group.add(createParticipants(4, CLUB1));
		group.add(createParticipants(2, CLUB2));
		group.add(createParticipants(2, CLUB3));
		group.add(createParticipants(1, CLUB4));

		GridCalculator gridCalculator = new GridCalculator();
		Grid grid = gridCalculator.calculateGrid(group);
		return grid;
	}
}
