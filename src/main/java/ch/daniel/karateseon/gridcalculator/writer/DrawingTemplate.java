package ch.daniel.karateseon.gridcalculator.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.base.Preconditions;

import ch.daniel.karateseon.gridcalculator.model.Participant;

public abstract class DrawingTemplate {
	protected Workbook templateWorkbook;
	protected Sheet templateSheet;

	public DrawingTemplate(File templateFile) {
		try {
			templateWorkbook = new XSSFWorkbook(new FileInputStream(templateFile));
		} catch (IOException ex) {
			throw new RuntimeException("Could not read template " + templateFile.getAbsolutePath(), ex);
		}

		Preconditions.checkArgument(templateWorkbook.getNumberOfSheets() == 1,
				"Template must have exactly one worksheet");
		templateSheet = templateWorkbook.getSheetAt(0);
	}

	public abstract void setCategory(String category);

	public abstract void setParticipant(int positionIndex, Participant participant, boolean isMarked);

	public Workbook getWorkbook() {
		return templateWorkbook;
	}

}
