package ch.daniel.karateseon.gridcalculator.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ch.daniel.karateseon.gridcalculator.GridCalculator;
import ch.daniel.karateseon.gridcalculator.GridFilter;
import ch.daniel.karateseon.gridcalculator.ParticipantsReader;
import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.filter.ParticipantsFilter;
import ch.daniel.karateseon.gridcalculator.model.Grid;
import ch.daniel.karateseon.gridcalculator.model.Group;
import ch.daniel.karateseon.gridcalculator.model.Participant;
import ch.daniel.karateseon.gridcalculator.persistence.FilterDao;
import ch.daniel.karateseon.gridcalculator.persistence.GridDao;
import ch.daniel.karateseon.gridcalculator.util.GridCalculatorFormatter;
import ch.daniel.karateseon.gridcalculator.writer.ExcelWriter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class MainController {

	private final MainModel model;
	@SuppressWarnings("unused")
	private final MainWindow view;

	public static void main(String[] args) {
		MainModel model = new MainModel();
		new MainController(model);
	}

	public MainController(MainModel model) {
		this.model = model;
		this.view = new MainWindow(this, model);

	}

	public void addFilter(FilterCriteria filter) {
		model.addFilter(filter);
	}

	public void removeFilter(FilterCriteria filter) {
		model.removeFilter(filter);
	}

	public void saveFilters(File file) throws IOException {
		Iterable<FilterCriteria> filters = model.getFilters();
		FilterDao filterDao = new FilterDao();
		filterDao.save(file, filters);
	}

	public void loadFilters(File file) throws FileNotFoundException {
		FilterDao filterDao = new FilterDao();
		Iterable<FilterCriteria> filters = filterDao.load(file);
		model.setFilters(filters);
	}

	public void clearFilters() {
		model.clearFilters();
	}

	public void generateGrids(File inputFile) throws FileNotFoundException, IOException {
		model.notifyProcessingStarted();
		model.clearGrids();
		if (Iterables.isEmpty(model.getFilters())) {
			throw new RuntimeException("No filters found");
		}

		ParticipantsFilter participantsFilter = new ParticipantsFilter();
		ParticipantsReader participantsReader = new ParticipantsReader();
		GridCalculator gridCalculator = new GridCalculator();
		Iterable<FilterCriteria> filters = model.getFilters();

		Iterable<Participant> participants = participantsReader.readParticipantsFromExcel(inputFile);
		Iterable<Group> groups = participantsFilter.createGroups(participants, filters);

		for (Group group : groups) {
			Grid grid = gridCalculator.calculateGrid(group);
			model.addGrid(grid);
		}
		model.notifyProcessingStopped();
	}

	public void saveGrids(File selectedFile) throws IOException {
		GridDao gridDao = new GridDao();
		gridDao.save(selectedFile, model.getGrids());
	}

	public void loadGrids(File file) throws FileNotFoundException {
		model.clearGrids();
		GridDao gridDao = new GridDao();
		Iterable<Grid> grids = gridDao.load(file);
		model.setGrids(grids);

		List<FilterCriteria> filters = Lists.newLinkedList();
		for (Grid grid : grids) {
			FilterCriteria filterCriteria = grid.getGroup().getFilterCriteria();
			filters.add(filterCriteria);
		}
		model.setFilters(filters);
	}

	public void generateExcels(File outputDirectory) throws IllegalStateException {
		model.notifyProcessingStarted();
		Iterable<Grid> grids = model.getGrids();
		Preconditions.checkState(!Iterables.isEmpty(grids), "No grids found");
		ExcelWriter excelWriter = new ExcelWriter();

		generateAllExcels(outputDirectory, grids, excelWriter);
		generateExcelsByClub(outputDirectory, grids, excelWriter);
		model.notifyProcessingStopped();
	}

	private void generateExcelsByClub(File outputDirectory, Iterable<Grid> grids, ExcelWriter excelWriter) {
		GridFilter gridFilter = new GridFilter();

		List<String> clubs = Lists.newLinkedList();
		for (Grid grid : grids) {
			Group group = grid.getGroup();
			clubs.addAll(group.getClubs());
		}

		for (String club : clubs) {
			File clubDirectory = new File(outputDirectory, club);
			clubDirectory.mkdir();
			Iterable<Grid> gridsFromClub = gridFilter.getByClub(grids, club);
			for (Grid grid : gridsFromClub) {
				generateExcel(clubDirectory, excelWriter, grid, club);
			}
		}
	}

	private void generateAllExcels(File outputDirectory, Iterable<Grid> grids, ExcelWriter excelWriter) {
		for (Grid grid : grids) {
			generateExcel(outputDirectory, excelWriter, grid, null);
		}
	}

	private void generateExcel(File outputDirectory, ExcelWriter excelWriter, Grid grid, String clubToBeMarked) {
		FilterCriteria filterCriteria = grid.getGroup().getFilterCriteria();
		File drawingFile = new File(outputDirectory, GridCalculatorFormatter.formatForFilename(filterCriteria,
				"drawing" + ".xlsx"));
		excelWriter.write(grid, filterCriteria, drawingFile, clubToBeMarked);

		File groupFile = new File(outputDirectory, GridCalculatorFormatter.formatForFilename(filterCriteria,
				"group" + ".xlsx"));
		excelWriter.writeGroupSheet(grid.getGroup().getAll(), filterCriteria, groupFile);
	}

}
