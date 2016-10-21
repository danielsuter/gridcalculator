package ch.danielsuter.gridcalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.util.MyLogger;
import org.junit.Test;

import ch.danielsuter.gridcalculator.model.Gender;
import ch.danielsuter.gridcalculator.model.Grid;
import ch.danielsuter.gridcalculator.model.GridSize;
import ch.danielsuter.gridcalculator.model.Group;
import ch.danielsuter.gridcalculator.model.Level;
import ch.danielsuter.gridcalculator.model.Participant;

public class GridCalculatorTest extends TestBase {
	private final static MyLogger logger = MyLogger.getLogger(GridCalculatorTest.class);

	@Test
	public void testUC1() {
		Group group = new Group(createFilterCriteria());
		group.add(createParticipants(8, CLUB1));
		group.add(createParticipants(4, CLUB2));
		group.add(createParticipants(4, CLUB3));

		Grid grid = getAndAssertGrid(group, GridSize.SMALL);

		assertNoPairingsFromSameClub(grid);
	}



	@Test
	public void testUC2() {
		Group group = new Group(createFilterCriteria());
		group.add(createParticipants(4, CLUB1));
		group.add(createParticipants(4, CLUB2));

		Grid grid = getAndAssertGrid(group, GridSize.SMALL);

		assertMaxPairings(grid, 0);
		assertNoPairingsSameClubInRound2(grid);
	}

	@Test
	public void testUC3() {
		Group group = new Group(createFilterCriteria());
		group.add(createParticipants(10, CLUB1));
		group.add(createParticipants(2, CLUB2));
		group.add(createParticipants(3, CLUB3));

		Grid grid = getAndAssertGrid(group, GridSize.SMALL);

		int numPairsSameClub = 0;
		int numNulls = 0;
		for (int i = 0; i < GridSize.SMALL.getSize(); i += 2) {
			Participant participant1 = grid.getPosition(i);
			Participant participant2 = grid.getPosition(i + 1);
			if (participant1 != null && participant2 != null) {
				if (participant1.getClub().equals(participant2.getClub())) {
					numPairsSameClub++;
				}
			} else {
				numNulls++;
			}
		}
		assertTrue("Max. 2 pairs from the same club", numPairsSameClub < 3);
		assertEquals("Only 1 empty spot", 1, numNulls);
	}

	@Test
	public void testUC4() {
		Group group = new Group(createFilterCriteria());
		group.add(createParticipants(8, CLUB1));
		group.add(createParticipants(6, CLUB2));
		group.add(createParticipants(6, CLUB3));
		group.add(createParticipants(6, CLUB4));

		Grid grid = getAndAssertGrid(group, GridSize.LARGE);

		assertNumFilledSlots(grid, 0, 16, 13);
		assertNumFilledSlots(grid, 16, 32, 13);

		assertNoPairingsFromSameClub(grid);
	}

	@Test
	public void testUC5() {
		Group group = new Group(createFilterCriteria());
		group.add(createParticipants(6, CLUB1));
		group.add(createParticipants(4, CLUB2));
		group.add(createParticipants(4, CLUB3));
		group.add(createParticipants(3, CLUB4));

		Grid grid = getAndAssertGrid(group, GridSize.LARGE);

		assertMaxPairings(grid, 1);
		assertNoPairingsFromSameClub(grid);
	}

	@Test
	public void testUC6() {
		Group group = new Group(createFilterCriteria());
		group.add(createParticipants(2, CLUB1));
		group.add(createParticipants(2, CLUB2));

		Grid grid = getAndAssertGrid(group, GridSize.SMALL);

		assertMaxPairings(grid, 0);
		assertNoPairingsFromSameClub(grid);
	}
	
	@Test
	public void testUC7() {
		Group group = new Group(createFilterCriteria());
		group.add(createParticipants(3, CLUB1));
		
		getAndAssertGrid(group, GridSize.TINY);
	}

	private void assertMaxPairings(Grid grid, int maxPairings) {
		int numNonWildcardsFound = 0;
		for (int positionIndex = 0; positionIndex < grid.getSize().getSize(); positionIndex += 2) {
			Participant participant1 = grid.getPosition(positionIndex);
			Participant participant2 = grid.getPosition(positionIndex + 1);
			if (participant1 != null && participant2 != null) {
				numNonWildcardsFound++;
			}
		}
		assertTrue("Only one pairing expected", numNonWildcardsFound < maxPairings + 1);
	}

	private Participant getParticipantFromPair(Grid grid, int pairIndex) {
		int startIndex = pairIndex;
		Participant participant1 = grid.getPosition(startIndex);
		if (participant1 != null) {
			return participant1;
		}
		Participant participant2 = grid.getPosition(startIndex + 1);
		if (participant2 != null) {
			return participant2;
		}

		throw new RuntimeException("Both positions are empty");
	}

	private void assertNoPairingsFromSameClub(Grid grid) {
		for (int i = 0; i < grid.getSize().getSize(); i += 2) {
			Participant participant1 = grid.getPosition(i);
			Participant participant2 = grid.getPosition(i + 1);

			if (participant1 != null && participant2 != null) {
				assertTrue("Clubs must differ", !participant1.getClub().equals(participant2.getClub()));
			}
		}
	}

	private void assertNumFilledSlots(Grid grid, int startPosition, int endPosition, int... expected) {
		int numFilledSlots = 0;
		for (int i = startPosition; i < endPosition; i++) {
			if (grid.getPosition(i) != null) {
				numFilledSlots++;
			}
		}

		List<Integer> expectedAsList = new LinkedList<Integer>();
		for (int expectedItem : expected) {
			expectedAsList.add(expectedItem);
		}
		assertTrue("Participants must be divided equally. Found " + numFilledSlots + " Expected: " + expected,
				expectedAsList.contains(numFilledSlots));

	}

	private void assertNoPairingsSameClubInRound2(Grid grid) {
		for (int i = 0; i < grid.getSize().getSize() / 2; i += 2) {
			Participant participant1 = getParticipantFromPair(grid, i);
			Participant participant2 = getParticipantFromPair(grid, i + 1);

			assertTrue("Clubs must differ", !participant1.getClub().equals(participant2.getClub()));
		}
	}

	private Grid getAndAssertGrid(Group group, GridSize gridSize) {
		GridCalculator calculator = new GridCalculator();
		Grid grid = calculator.calculateGrid(group);
		logger.debug(grid);
		assertEquals("Assert " + gridSize + " grid", gridSize, grid.getSize());

		return grid;
	}
	
	private FilterCriteria createFilterCriteria() {
		return FilterCriteria.createKata(Gender.MALE, 0, 1, Level.LOWER_STAGE);
	}
}
