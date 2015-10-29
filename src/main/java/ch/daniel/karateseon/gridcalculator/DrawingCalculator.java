package ch.daniel.karateseon.gridcalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.filter.ParticipantsFilter;
import ch.daniel.karateseon.gridcalculator.model.Grid;
import ch.daniel.karateseon.gridcalculator.model.Group;
import ch.daniel.karateseon.gridcalculator.model.Participant;
import ch.daniel.karateseon.gridcalculator.util.GridCalculatorFormatter;
import ch.daniel.karateseon.gridcalculator.writer.ExcelWriter;

public class DrawingCalculator {
	private final static Logger LOGGER = Logger.getLogger(DrawingCalculator.class);
	
	private static final String DRAWING_SUFFIX = "drawing";
	private static final String GROUP_SHEET_SUFFIX = "group";
	private static final String EXCEL_EXTENSION = ".xlsx";

	private ParticipantsReader reader = new ParticipantsReader();
	private ParticipantsFilter filter = new ParticipantsFilter();
	private GridCalculator gridCalculator = new GridCalculator();
	private ExcelWriter writer = new ExcelWriter();
	private String outputDirectory;

	public DrawingCalculator(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public void calculateGrid(File participantsFile, List<FilterCriteria> filters) throws FileNotFoundException,
			IOException {
		Iterable<Participant> participants = reader.readParticipantsFromExcel(participantsFile);

		Iterable<Group> groups = filter.createGroups(participants, filters);
		
		for (Group group : groups) {
			LOGGER.info(group.toSummaryString());
			
			Grid grid = gridCalculator.calculateGrid(group.clone());
			FilterCriteria criteria = group.getFilterCriteria();
			File gridFile = getOutputFile(criteria, DRAWING_SUFFIX);
			writer.write(grid, criteria, gridFile, null);
			writer.writeGroupSheet(group.getAll(), criteria, getOutputFile(criteria, GROUP_SHEET_SUFFIX));
			
			generateClubGridFile(group, grid, criteria, gridFile);
		}
	}

	private void generateClubGridFile(Group group, Grid grid, FilterCriteria criteria, File gridFile) {
		Set<String> clubs = group.getClubs();
		for (String club : clubs) {
			File clubDirectory = new File(outputDirectory, club);
			clubDirectory.mkdir();
			File clubGridFile = new File(clubDirectory, gridFile.getName());
			writer.write(grid, criteria, clubGridFile, club);
		}
	}

	private File getOutputFile(FilterCriteria criteria, String suffix) {
		return new File(outputDirectory, GridCalculatorFormatter.formatForFilename(criteria, suffix) + EXCEL_EXTENSION);
	}
}
