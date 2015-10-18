package ch.daniel.karateseon.gridcalculator.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.model.Grid;
import ch.daniel.karateseon.gridcalculator.model.Participant;
import ch.daniel.karateseon.gridcalculator.util.GridCalculatorFormatter;

public class ExcelWriter {
	private TemplateResolver templateResolver = new TemplateResolver();

	public void write(Grid grid, FilterCriteria criteria, File destination, String clubToBeMarked) {
		DrawingTemplate template = templateResolver.getTemplate(grid.getSize());
		template.setCategory(GridCalculatorFormatter.formatForGroupSheetHeader(criteria));

		int size = grid.getSize().getSize();
		for (int positionIndex = 0; positionIndex < size; positionIndex++) {
			Participant participant = grid.getPosition(positionIndex);
			if (participant != null) {
				template.setParticipant(positionIndex, participant, isMarked(participant, clubToBeMarked));
			}
		}

		Workbook workbook = template.getWorkbook();
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(destination);
			workbook.write(outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write to " + destination.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}


	public void writeGroupSheet(Iterable<Participant> participants, FilterCriteria criteria, File destination) {
		GroupSheetTemplate template = templateResolver.getGroupSheetTemplate();
		template.setParticipants(participants, criteria);

		Workbook workbook = template.getWorkbook();
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(destination);
			workbook.write(outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to write to " + destination.getAbsolutePath());
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	private boolean isMarked(Participant participant, String clubToBeMarked) {
		return participant.getClub().equals(clubToBeMarked);
	}
}
