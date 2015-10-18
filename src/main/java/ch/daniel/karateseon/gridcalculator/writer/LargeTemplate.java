package ch.daniel.karateseon.gridcalculator.writer;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;

import ch.daniel.karateseon.gridcalculator.model.GridSize;
import ch.daniel.karateseon.gridcalculator.model.Participant;
import ch.daniel.karateseon.gridcalculator.util.MyLogger;
import ch.daniel.karateseon.gridcalculator.util.GridCalculatorFormatter;

public class LargeTemplate extends DrawingTemplate {
	private final static MyLogger logger = MyLogger.getLogger(LargeTemplate.class);

	private static final int ROW_DIFFERENCE_PER_PAIR = 6;
	private static final int ROW_DIFFERENCE_WITHIN_PAIR = 2;

	private static final int CATEGORY_ROW_PAGE1 = 1;
	private static final int CATEGORY_ROW_PAGE2 = 66;

	private static final int CATEGORY_COLUMN_INDEX = 6;
	private static final int PARTICIPANT_COLUM_INDEX = 1;
	private static final int CLUB_COLUMN_INDEX = 2;

	private static final int FIRST_PARTICIPANT_ROW_PAGE1 = 11;
	private static final int FIRST_PARTICIPANT_ROW_PAGE2 = 76;

	private Cell categoryPage1;
	private Cell categoryPage2;
	private Cell[] participantCells;
	private Cell[] participantClubCells;

	public LargeTemplate(File templateFile) {
		super(templateFile);
		initCells();
	}

	private void initCells() {
		categoryPage1 = templateSheet.getRow(CATEGORY_ROW_PAGE1).getCell(CATEGORY_COLUMN_INDEX);
		categoryPage2 = templateSheet.getRow(CATEGORY_ROW_PAGE2).getCell(CATEGORY_COLUMN_INDEX);

		participantCells = new Cell[GridSize.LARGE.getSize()];
		participantClubCells = new Cell[GridSize.LARGE.getSize()];

		initPositions(FIRST_PARTICIPANT_ROW_PAGE1, 0);
		initPositions(FIRST_PARTICIPANT_ROW_PAGE2, 16);
	}

	private void initPositions(int firstParticipantRow, int participantCellIndexOffset) {
		for (int pairIndex = 0; pairIndex < GridSize.LARGE.getSize() / 4; pairIndex++) {
			// Participant 1
			int rowIndex = pairIndex * ROW_DIFFERENCE_PER_PAIR + firstParticipantRow;
			logger.debug("Processing pair {0} expecting participant at row {1}", pairIndex, rowIndex);
			participantCells[pairIndex * 2 + participantCellIndexOffset] = templateSheet.getRow(rowIndex).getCell(
					PARTICIPANT_COLUM_INDEX);
			participantClubCells[pairIndex * 2 + participantCellIndexOffset] = templateSheet.getRow(rowIndex).getCell(
					CLUB_COLUMN_INDEX);
			// Participant 2
			participantCells[pairIndex * 2 + participantCellIndexOffset + 1] = templateSheet.getRow(
					rowIndex + ROW_DIFFERENCE_WITHIN_PAIR).getCell(PARTICIPANT_COLUM_INDEX);
			participantClubCells[pairIndex * 2 + participantCellIndexOffset + 1] = templateSheet.getRow(
					rowIndex + ROW_DIFFERENCE_WITHIN_PAIR).getCell(CLUB_COLUMN_INDEX);
		}
	}

	@Override
	public void setCategory(String categoryName) {
		categoryPage1.setCellValue(categoryName);
		categoryPage2.setCellValue(categoryName);
	}

	@Override
	public void setParticipant(int positionIndex, Participant participant, boolean isMarked) {
		participantCells[positionIndex].setCellValue(GridCalculatorFormatter.formatForGrid(participant));
		participantClubCells[positionIndex].setCellValue(participant.getClub());
//		throw new RuntimeException("color not yet implemented");
	}

}
