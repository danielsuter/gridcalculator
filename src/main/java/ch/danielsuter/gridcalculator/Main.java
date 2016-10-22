package ch.danielsuter.gridcalculator;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import ch.danielsuter.gridcalculator.filter.ParticipantsFilter;
import ch.danielsuter.gridcalculator.model.Group;
import ch.danielsuter.gridcalculator.model.Level;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.writer.ExcelWriter;
import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Gender;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File outputDirectory = Files.createTempDirectory("tournamentGrids").toFile();
		System.out.println(outputDirectory);

		List<FilterCriteria> filters = create2016Filters();

		ParticipantsReader reader = new ParticipantsReader();
		Iterable<Participant> participants = reader.readParticipantsFromExcel(new File("C:\\Users\\dsu\\Documents\\Shinseikan\\Anmeldelisten\\teilnehmer.xlsx"));
		ParticipantsFilter participantsFilter = new ParticipantsFilter();
		Iterable<Group> groups = participantsFilter.createGroups(participants, filters);

        ExcelWriter writer = new ExcelWriter();
        writer.writeRanking(groups, new File(outputDirectory, "rangliste.xlsx"));

        //DrawingCalculator calculator = new DrawingCalculator(outputDirectory.getAbsolutePath());
		//calculator.calculateGrid(groups);

		Desktop.getDesktop().open(outputDirectory);
	}

	
	private static List<FilterCriteria> create2016Filters() {
		List<FilterCriteria> filters = Lists.newLinkedList();
		filters.add(FilterCriteria.createKata(Gender.MALE, 1, 3000, Level.LOWER_STAGE));
//		filters.add(FilterCriteria.createKata(Gender.MALE, 1, 3000, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 1, 3000, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 1, 3000, Level.UPPER_STAGE));

		filters.add(FilterCriteria.createKumite(Gender.MALE, 0, 3000, 0, 100, Level.LOWER_STAGE, ""));
//		filters.add(FilterCriteria.createKumite(Gender.MALE, 0, 3000, 0, 100, Level.UPPER_STAGE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 0, 3000, 0, 100, Level.LOWER_STAGE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 0, 3000, 0, 100, Level.UPPER_STAGE, ""));


		return filters;
	}
	
	private static List<FilterCriteria> create2015Filters() {
		List<FilterCriteria> filters = Lists.newLinkedList();
		
		filters.add(FilterCriteria.createKata(Gender.MALE, 2000, 2003, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2003, 2006, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2006, 2008, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2008, 2010, Level.LOWER_STAGE));
		
		filters.add(FilterCriteria.createKata(Gender.MALE, 2000, 2002, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2002, 2003, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2003, 2006, Level.UPPER_STAGE));
		
		
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2002, 2004, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2004, 2006, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2006, 2008, Level.LOWER_STAGE));
		
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 1999, 2003, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2003, 2004, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2004, 2007, Level.UPPER_STAGE));
		
		
		filters.add(FilterCriteria.createKumite(Gender.MALE, 1998, 2002, 0, 100, Level.LOWER_STAGE, ""));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 1998, 2002, 0, 100, Level.UPPER_STAGE, ""));
		
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2002, 2005, 0, 51, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2002, 2005, 51, 100, "schwer"));

		filters.add(FilterCriteria.createKumite(Gender.MALE, 2005, 2006, 0, 100, ""));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2006, 2008, 0, 100, ""));
		
		
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2001, 2004, 0, 40, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2001, 2004, 40, 60, "schwer"));
		
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2004, 2006, 0, 38, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2004, 2006, 38, 60, "schwer"));
		
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2006, 2008, 0, 100, ""));
		
		return filters;
	}




	private static List<FilterCriteria> createFilters() {
		List<FilterCriteria> filters = Lists.newLinkedList();
		filters.add(FilterCriteria.createKata(Gender.MALE, 1998, 2001, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2001, 2003, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2003, 2006, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 1999, 2002, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2002, 2004, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2004, 2006, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.MALE, 2006, 2008, Level.LOWER_STAGE));
		
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 1997, 2000, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2000, 2002, Level.UPPER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2002, 2006, Level.UPPER_STAGE));
		
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 1998, 2002, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2002, 2004, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2004, 2005, Level.LOWER_STAGE));
		filters.add(FilterCriteria.createKata(Gender.FEMALE, 2005, 2007, Level.LOWER_STAGE));
		
		filters.add(FilterCriteria.createKumite(Gender.MALE, 1998, 2001, 0, 57, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 1998, 2001, 57, Double.MAX_VALUE, "schwer"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2001, 2003, 30, 42, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2001, 2003, 42, Double.MAX_VALUE, "schwer"));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2003, 2005, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.MALE, 2005, 2008, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 1997, 2000, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2000, 2002, 0, Double.MAX_VALUE, ""));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2002, 2005, 0, 40, "leicht"));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2002, 2005, 40, Double.MAX_VALUE, "schwer"));
		filters.add(FilterCriteria.createKumite(Gender.FEMALE, 2005, 2007, 0, Double.MAX_VALUE, ""));
		return filters;
	}

}
