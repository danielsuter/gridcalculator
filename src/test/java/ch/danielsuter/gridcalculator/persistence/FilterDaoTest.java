package ch.danielsuter.gridcalculator.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.danielsuter.gridcalculator.TestBase;
import ch.danielsuter.gridcalculator.model.Level;
import org.junit.Before;
import org.junit.Test;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Gender;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class FilterDaoTest extends TestBase {
	private FilterDao dao;

	@Before
	public void before() {
		dao = new FilterDao();
	}

	@Test
	public void saveAndLoad() throws IOException {
		File destination = new File("target/filterCriteria1.xml");
		List<FilterCriteria> filter = createFilterCritria();

		dao.save(destination, filter);
		Iterable<FilterCriteria> loadedFilters = dao.load(destination);
		assertEquals("Same amount of filters expected", filter.size(), Iterables.size(loadedFilters));

		for (FilterCriteria loadedFilter : loadedFilters) {
			assertTrue("Filter stays the same", filter.contains(loadedFilter));
		}
	}

	private List<FilterCriteria> createFilterCritria() {
		List<FilterCriteria> filter = Lists.newLinkedList();
		filter.add(FilterCriteria.createKata(Gender.MALE, 1999, 2001, Level.LOWER_STAGE));
		filter.add(FilterCriteria.createKata(Gender.MALE, 1999, 2001, Level.UPPER_STAGE));
		filter.add(FilterCriteria.createKata(Gender.FEMALE, 1999, 2001, Level.LOWER_STAGE));
		filter.add(FilterCriteria.createKata(Gender.FEMALE, 1999, 2001, Level.UPPER_STAGE));
		filter.add(FilterCriteria.createKumite(Gender.MALE, 1999, 2001, 10d, 20d, null));
		filter.add(FilterCriteria.createKumite(Gender.MALE, 1999, 2001, 10d, 20d, "schwer"));
		filter.add(FilterCriteria.createKumite(Gender.FEMALE, 1999, 2001, 10d, 20d, "leicht"));
		filter.add(FilterCriteria.createKumite(Gender.FEMALE, 1999, 2001, 10d, 20d, null));
		return filter;
	}

}
