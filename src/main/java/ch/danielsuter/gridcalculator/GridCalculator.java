package ch.danielsuter.gridcalculator;

import java.util.Arrays;

import ch.danielsuter.gridcalculator.model.Grid;
import ch.danielsuter.gridcalculator.model.GridSize;
import ch.danielsuter.gridcalculator.util.MyLogger;
import com.google.common.base.Preconditions;

import ch.danielsuter.gridcalculator.model.Group;

public class GridCalculator {
	private final static MyLogger logger = MyLogger.getLogger(GridCalculator.class);

	public Grid calculateGrid(Group group) {
		logger.debug("Criteria: {0}", group.getFilterCriteria());
		int originalGroupSize = group.getSize();
		Preconditions.checkArgument(originalGroupSize > 2, "Groups must have more than 2 participants but was "
				+ originalGroupSize);

		GridSize gridSize = determineGridSize(originalGroupSize);
		Grid grid = new Grid(gridSize, group.clone());

		if (gridSize == GridSize.TINY) {
			for (int positionIndex = 0; positionIndex < GridSize.TINY.getSize(); positionIndex++) {
				grid.setPosition(positionIndex, group.removeParticipantFromLargestClub());
			}
		} else if (gridSize == GridSize.SMALL) {
			// Split the groups until the sizes are <= 4
			Group[] groups = group.divideEqually();
			Group[] fourGroups = concatAll(groups[0].divideEqually(), groups[1].divideEqually());

			fillGrid(grid, fourGroups);

		} else if (gridSize == GridSize.LARGE) {
			// split once and use same procedure as small grid
			Group[] groups = group.divideEqually();

			Group[] fourGroups = concatAll(groups[0].divideEqually(), groups[1].divideEqually());
			Group[] eightGroups = concatAll(fourGroups[0].divideEqually(), fourGroups[1].divideEqually(),
					fourGroups[2].divideEqually(), fourGroups[3].divideEqually());

			fillGrid(grid, eightGroups);
		} else {
			throw new RuntimeException("Unknown grid size " + gridSize);
		}

		return grid;
	}

	private void fillGrid(Grid grid, Group[] groups) {
		for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
			Group[] groupsOfTwo = createGroupsOfTwo(groups[groupIndex]);
			putParticipantsIntoGrid(grid, groupIndex, groupsOfTwo);

		}
	}

	private void putParticipantsIntoGrid(Grid grid, int groupIndex, Group[] groupsOfTwo) {
		int numberOfParticipants = 0;
		for (Group group : groupsOfTwo) {
			numberOfParticipants += group.getSize();
		}
		int strechFactor = 4 / numberOfParticipants;

		int currentPosition = 4 * groupIndex;
		for (Group pairing : groupsOfTwo) {
			grid.setPosition(currentPosition, pairing.removeParticipantFromLargestClub());
			if (pairing.getSize() > 0) { // Needed for uneven numbers
				grid.setPosition(currentPosition + 1 * strechFactor, pairing.removeParticipantFromLargestClub());
			}

			currentPosition += 2 * strechFactor;
		}
	}

	private Group[] createGroupsOfTwo(Group group) {
		if (group.getSize() <= 2) {
			return new Group[] { group };
		} else {
			// Divide
			Group[][] groupsToReturn = new Group[2][];
			Group[] groups = group.divideEqually();
			groupsToReturn[0] = createGroupsOfTwo(groups[0]);
			groupsToReturn[1] = createGroupsOfTwo(groups[1]);
			return concatAll(groupsToReturn[0], groupsToReturn[1]);
		}
	}

	public static <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	private GridSize determineGridSize(int size) {
		if (size == 3) {
			return GridSize.TINY;
		} else if (size <= GridSize.SMALL.getSize()) {
			return GridSize.SMALL;
		} else if (size <= GridSize.LARGE.getSize()) {
			return GridSize.LARGE;
		} else {
			throw new RuntimeException("Cannot handle grid size " + size);
		}
	}
}
