package ch.danielsuter.gridcalculator.writer;

import java.io.File;

import ch.danielsuter.gridcalculator.model.GridSize;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.GridCalculatorFormatter;
import org.apache.poi.ss.usermodel.Cell;

public class TinyTemplate extends DrawingTemplate {
	private static final int CATEGORY_ROW = 1;
	private static final int CATEGORY_COLUMN_INDEX = 5;
	private static final int FIRST_PARTICIPANT_ROW = 5;
	private static final int PARTICIPANT_COLUMN_1 = 0;
	private static final int PARTICIPANT_COLUMN_2 = 5;
	private static final int CLUB_COLUMN_INDEX = 2;

	private Cell category;
	private Cell[] participantCells;
	private Cell[] participantClubCells;

	public TinyTemplate(File templateFile) {
		super(templateFile);
		initCells();
	}

	private void initCells() {
		category = templateSheet.getRow(CATEGORY_ROW).getCell(CATEGORY_COLUMN_INDEX);

		participantCells = new Cell[GridSize.TINY.getSize() * 2];
		participantClubCells = new Cell[GridSize.TINY.getSize()];

		initPositions();
	}

	private void initPositions() {
		for (int positionIndex = 0; positionIndex < GridSize.TINY.getSize(); positionIndex++) {
			int rowIndex = FIRST_PARTICIPANT_ROW + positionIndex;
			participantCells[positionIndex] = templateSheet.getRow(rowIndex).getCell(PARTICIPANT_COLUMN_1);
			participantCells[positionIndex + 3] = templateSheet.getRow(rowIndex).getCell(PARTICIPANT_COLUMN_2);
			participantClubCells[positionIndex] = templateSheet.getRow(rowIndex).getCell(CLUB_COLUMN_INDEX);
		}
	}

	@Override
	public void setCategory(String categoryToSet) {
		category.setCellValue(categoryToSet);
	}

	@Override
	public void setParticipant(int positionIndex, Participant participant, boolean isMarked) {
		String formattedParticipant = GridCalculatorFormatter.formatForGrid(participant);
		if (positionIndex == 0) {
			participantCells[0].setCellValue(formattedParticipant);
			participantCells[4].setCellValue(formattedParticipant);
		} else if (positionIndex == 1) {
			participantCells[1].setCellValue(formattedParticipant);
			participantCells[5].setCellValue(formattedParticipant);
		} else if (positionIndex == 2) {
			participantCells[2].setCellValue(formattedParticipant);
			participantCells[3].setCellValue(formattedParticipant);
		}
		participantClubCells[positionIndex].setCellValue(participant.getClub());
		
		applyParticipantMarking(participantCells[positionIndex], isMarked);
	}
}
