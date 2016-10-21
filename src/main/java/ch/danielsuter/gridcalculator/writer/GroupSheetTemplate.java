package ch.danielsuter.gridcalculator.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Preconditions;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.GridCalculatorFormatter;

public class GroupSheetTemplate {
	private static final int GROUP_HEADER_COLUMN_INDEX = 0;

	private static final int GROUP_HEADER_ROW = 0;

	private final static int FIRST_PARTICIPANT_ROW = 2;

	private final static int LASTNAME_COLUM_INDEX = 0;
	private final static int FIRSTNAME_COLUM_INDEX = 1;
	private final static int WEIGHT_COLUM_INDEX = 2;
	private final static int CLUB_COLUM_INDEX = 3;

	private Workbook templateWorkbook;
	private Sheet templateSheet;
	private int currentRow;

	public GroupSheetTemplate(File templateFile) {
		try {
			templateWorkbook = new XSSFWorkbook(new FileInputStream(templateFile));
		} catch (IOException ex) {
			throw new RuntimeException("Could not read template " + templateFile.getAbsolutePath());
		}

		Preconditions.checkArgument(templateWorkbook.getNumberOfSheets() == 1,
				"Template must have exactly one worksheet");
		templateSheet = templateWorkbook.getSheetAt(0);

		currentRow = FIRST_PARTICIPANT_ROW;
	}

	public void setParticipants(Iterable<Participant> participants, FilterCriteria criteria) {
		Preconditions.checkArgument(participants.iterator().hasNext(), "Participants may not be empty");

		templateSheet.getRow(GROUP_HEADER_ROW).getCell(GROUP_HEADER_COLUMN_INDEX)
				.setCellValue(GridCalculatorFormatter.formatForGroupSheetHeader(criteria));

		for (Participant participant : participants) {
			Row row = templateSheet.createRow(currentRow);
			writeCell(row, participant.getLastname(), LASTNAME_COLUM_INDEX);
			writeCell(row, participant.getFirstname(), FIRSTNAME_COLUM_INDEX);
			writeCell(row, participant.getClub(), CLUB_COLUM_INDEX);
			if(participant.getWeight() != null) {
				writeCell(row, participant.getWeight(), WEIGHT_COLUM_INDEX);
			}

			currentRow++;
		}
	}

	private Row writeCell(Row row, String value, int columnIndex) {
		Cell cell = row.createCell(columnIndex, Cell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		return row;
	}

	private Row writeCell(Row row, double value, int columnIndex) {
		Cell cell = row.createCell(columnIndex, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		return row;
	}

	public Workbook getWorkbook() {
		return templateWorkbook;
	}
}
