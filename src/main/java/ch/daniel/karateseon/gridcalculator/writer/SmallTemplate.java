package ch.daniel.karateseon.gridcalculator.writer;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;

import ch.daniel.karateseon.gridcalculator.model.GridSize;
import ch.daniel.karateseon.gridcalculator.model.Participant;
import ch.daniel.karateseon.gridcalculator.util.GridCalculatorFormatter;

public class SmallTemplate extends DrawingTemplate {
	private static final int ROW_DIFFERENCE_PER_PAIR = 6;
	private static final int ROW_DIFFERENCE_WITHIN_PAIR = 2;
	private static final int CATEGORY_ROW = 1;
	private static final int CATEGORY_COLUMN_INDEX = 6;
	private static final int FIRST_PARTICIPANT_ROW = 11;
	private static final int PARTICIPANT_COLUM_INDEX = 1;
	private static final int CLUB_COLUMN_INDEX = 2;

	private Cell category;
	private Cell[] participantCells;
	private Cell[] participantClubCells;

	public SmallTemplate(File templateFile) {
		super(templateFile);
		initCells();
	}

	private void initCells() {
		category = templateSheet.getRow(CATEGORY_ROW).getCell(CATEGORY_COLUMN_INDEX);

		participantCells = new Cell[GridSize.SMALL.getSize()];
		participantClubCells = new Cell[GridSize.SMALL.getSize()];
		
		initPositions();
	}

	private void initPositions() {
		for (int pairIndex = 0; pairIndex < GridSize.SMALL.getSize() / 2; pairIndex++) {
			// Participant 1
			int rowIndex = pairIndex * ROW_DIFFERENCE_PER_PAIR + FIRST_PARTICIPANT_ROW;
			participantCells[pairIndex * 2] = templateSheet.getRow(rowIndex).getCell(PARTICIPANT_COLUM_INDEX);
			participantClubCells[pairIndex * 2] = templateSheet.getRow(rowIndex).getCell(CLUB_COLUMN_INDEX);
			// Participant 2
			participantCells[pairIndex * 2 + 1] = templateSheet.getRow(rowIndex + ROW_DIFFERENCE_WITHIN_PAIR).getCell(
					PARTICIPANT_COLUM_INDEX);
			participantClubCells[pairIndex * 2 + 1] = templateSheet.getRow(rowIndex + ROW_DIFFERENCE_WITHIN_PAIR)
					.getCell(CLUB_COLUMN_INDEX);
		}
	}

	@Override
	public void setCategory(String categoryName) {
		category.setCellValue(categoryName);
	}

	@Override
	public void setParticipant(int positionIndex, Participant participant, boolean isMarked) {
		participantCells[positionIndex].setCellValue(GridCalculatorFormatter.formatForGrid(participant));
		participantClubCells[positionIndex].setCellValue(participant.getClub());
		//throw new RuntimeException("color not yet implemented");
	}

}
