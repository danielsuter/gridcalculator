package ch.danielsuter.gridcalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ch.danielsuter.gridcalculator.model.Gender;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParticipantsReader {
	private static final int CLUB_COLUMN_INDEX = 8;
	private static final int KUMITE_COLUMN_INDEX = 7;
	private static final int KATA_COLUMN_INDEX = 6;
	private static final int GENDER_COLUMN_INDEX = 5;
	private static final int WEIGHT_COLUMN_INDEX = 4;
	private static final int KYU_COLUMN_INDEX = 3;
	private static final int YEAR_COLUMN_INDEX = 2;
	private static final int LASTNAME_COLUMN_INDEX = 0;
	private static final int FIRSTNAME_COLUMN_INDEX = 1;

	public Iterable<Participant> readParticipantsFromExcel(File excelFile) throws FileNotFoundException, IOException {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(excelFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);

			List<Participant> participants = getDataFromSheet(firstSheet);

			return participants;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	private List<Participant> getDataFromSheet(Sheet sheet) {
		List<Participant> returnValue = new LinkedList<Participant>();
		for (int rowIndex = 1; rowIndex < sheet.getLastRowNum() + 1; rowIndex++) {
			Row currentRow = sheet.getRow(rowIndex);
			if(isValidRow(currentRow)) {
				Participant participant = parseRow(currentRow);
				returnValue.add(participant);
			}
		}

		return returnValue;
	}

	private boolean isValidRow(Row currentRow) {
		if(currentRow == null) return false;
		
		boolean yearIsAvailable = currentRow.getCell(YEAR_COLUMN_INDEX) != null;
		Cell lastnameCell = currentRow.getCell(LASTNAME_COLUMN_INDEX);
		boolean lastnameIsAvailable = StringUtils.isNotBlank(ExcelUtils.getStringCellValue(lastnameCell));
		return yearIsAvailable && lastnameIsAvailable;
	}

	private Participant parseRow(Row currentRow) {
		try {
			String lastname = ExcelUtils.getStringCellValue(currentRow.getCell(LASTNAME_COLUMN_INDEX));
			String firstname = ExcelUtils.getStringCellValue(currentRow.getCell(FIRSTNAME_COLUMN_INDEX));
			int year = (int) currentRow.getCell(YEAR_COLUMN_INDEX).getNumericCellValue();
			int kyu = (int) currentRow.getCell(KYU_COLUMN_INDEX).getNumericCellValue();
			Cell weightCell = currentRow.getCell(WEIGHT_COLUMN_INDEX);
			Double weight = null;
			if (weightCell != null) {
				weight = weightCell.getNumericCellValue();
			}
			String genderAsString = currentRow.getCell(GENDER_COLUMN_INDEX).getStringCellValue();
			Gender gender = genderAsString.equals("m") ? Gender.MALE : Gender.FEMALE;
			String kataAsString = ExcelUtils.getStringCellValue(currentRow.getCell(KATA_COLUMN_INDEX));
			boolean startsKata = isChecked(kataAsString);
			String kumiteAsString = ExcelUtils.getStringCellValue(currentRow.getCell(KUMITE_COLUMN_INDEX));
			boolean startsKumite = isChecked(kumiteAsString);
			String club = currentRow.getCell(CLUB_COLUMN_INDEX).getStringCellValue();

			Participant participant = new Participant(firstname, lastname, year, kyu, weight, gender, startsKata,
					startsKumite, club);
			return participant;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to parse row " + currentRow.getRowNum(), ex);
		}

	}

	private boolean isChecked(String s) {
		if (s != null && s.equalsIgnoreCase("x")) {
			return true;
		}
		return false;
	}
}
