package ch.daniel.karateseon.gridcalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.filter.ParticipantsFilter;
import ch.daniel.karateseon.gridcalculator.model.Grid;
import ch.daniel.karateseon.gridcalculator.model.Group;
import ch.daniel.karateseon.gridcalculator.model.Participant;
import ch.daniel.karateseon.gridcalculator.util.GridCalculatorFormatter;
import ch.daniel.karateseon.gridcalculator.writer.ExcelWriter;

public class DrawingCalculator {
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
			Grid grid = gridCalculator.calculateGrid(group.clone());
			FilterCriteria criteria = group.getFilterCriteria();
			writer.write(grid, criteria, getOutputFile(criteria, DRAWING_SUFFIX), null);
			writer.writeGroupSheet(group.getAll(), criteria, getOutputFile(criteria, GROUP_SHEET_SUFFIX));
//			throw new RuntimeException("color not yet implemented");
		}
		
		System.out.println("Sizes");
		for (Group group : groups) {
			System.out.println(group.getSize());
		}
	}

	private File getOutputFile(FilterCriteria criteria, String suffix) {
		return new File(outputDirectory, GridCalculatorFormatter.formatForFilename(criteria, suffix) + EXCEL_EXTENSION);
	}
	
	public Iterable<Grid> calculateGrids(File participantsFile, List<FilterCriteria> filters) {
		throw new RuntimeException("not implemented");
	}
	
	public void calculateExcels(Iterable<Grid> grids, File outputDirectory) {
		throw new RuntimeException("not implemented");
	}
}
