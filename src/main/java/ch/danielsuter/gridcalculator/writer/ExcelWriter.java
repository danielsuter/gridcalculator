package ch.danielsuter.gridcalculator.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.danielsuter.gridcalculator.model.Group;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Grid;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.GridCalculatorFormatter;

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

		writeExcel(destination, template.getWorkbook());
	}

	public void writeGroupSheet(Iterable<Participant> participants, FilterCriteria criteria, File destination) {
		GroupSheetTemplate template = templateResolver.getGroupSheetTemplate();
		template.setParticipants(participants, criteria);
		writeExcel(destination, template.getWorkbook());
	}

	public void writeRanking(Iterable<Group> groups, File destination) {
		RankingTemplate template = templateResolver.getRankingTemplate();
		template.setGroups(groups);
		writeExcel(destination, template.getWorkbook());
	}


	private void writeExcel(File destination, Workbook workbook) {
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

	private boolean isMarked(Participant participant, String clubToBeMarked) {
		return participant.getClub().equals(clubToBeMarked);
	}
}
