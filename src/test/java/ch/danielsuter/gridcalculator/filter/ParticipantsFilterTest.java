package ch.danielsuter.gridcalculator.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ch.danielsuter.gridcalculator.TestBase;
import ch.danielsuter.gridcalculator.model.Level;
import org.junit.Test;

import ch.danielsuter.gridcalculator.model.Gender;
import ch.danielsuter.gridcalculator.model.Group;
import ch.danielsuter.gridcalculator.model.Participant;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ParticipantsFilterTest extends TestBase {
	private ParticipantsFilter filter = new ParticipantsFilter(true);


	@Test
	public void createGroups_KataMixed() {
		List<Participant> participants = new ArrayList<>();
		participants.add(new Participant("vorname1", "nachname1", 2000, 8, null, Gender.MALE, true, false, "club1"));
		participants.add(new Participant("vorname2", "nachname2", 2000, 8, null, Gender.MALE, true, false, "club1"));
		participants.add(new Participant("vorname3", "nachname3", 2000, 8, null, Gender.FEMALE, true, false, "club1"));
		participants.add(new Participant("vorname4", "nachname4", 2000, 8, null, Gender.MALE, true, false, "club1"));

		List<FilterCriteria> criteria = new ArrayList<>();
		criteria.add(FilterCriteria.createKata(Gender.MIXED, 0, Integer.MAX_VALUE, Level.LOWER_STAGE));

		Iterable<Group> groups = filter.createGroups(participants, criteria);

		assertEquals(1, Iterables.size(groups));
	}

	@Test
	public void createGroups_KumiteMixed() {
		List<Participant> participants = new ArrayList<>();
		participants.add(new Participant("vorname1", "nachname1", 2000, 8, 1d, Gender.MALE, false, true, "club1"));
		participants.add(new Participant("vorname2", "nachname2", 2000, 8, 1d, Gender.MALE, false, true, "club1"));
		participants.add(new Participant("vorname3", "nachname3", 2000, 8, 1d, Gender.FEMALE, false, true, "club1"));
		participants.add(new Participant("vorname4", "nachname4", 2000, 8, 1d, Gender.MALE, false, true, "club1"));

		List<FilterCriteria> criteria = new ArrayList<>();
		criteria.add(FilterCriteria.createKumite(Gender.MIXED, 0, Integer.MAX_VALUE, 0, 2d, null));

		Iterable<Group> groups = filter.createGroups(participants, criteria);

		assertEquals(1, Iterables.size(groups));
	}

	@Test
	public void createGroupsForGenderAndFightType() {
		Iterable<Participant> participants = createParticipants();

		List<FilterCriteria> filterCriteria = Lists.newLinkedList();
		filterCriteria.add(FilterCriteria
				.createKumite(Gender.MALE, 0, Integer.MAX_VALUE, 0, Double.MAX_VALUE, "schwer"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.FEMALE, 0, Integer.MAX_VALUE, 0, Double.MAX_VALUE,
				"schwer"));

		filterCriteria.add(FilterCriteria.createKata(Gender.MALE, 0, Integer.MAX_VALUE, Level.LOWER_STAGE));
		filterCriteria.add(FilterCriteria.createKata(Gender.MALE, 0, Integer.MAX_VALUE, Level.UPPER_STAGE));

		filterCriteria.add(FilterCriteria.createKata(Gender.FEMALE, 0, Integer.MAX_VALUE, Level.LOWER_STAGE));
		filterCriteria.add(FilterCriteria.createKata(Gender.FEMALE, 0, Integer.MAX_VALUE, Level.UPPER_STAGE));

		Iterable<Group> groups = filter.createGroups(participants, filterCriteria);
		assertEquals(6, Iterables.size(groups));
	}

	@Test(expected = IllegalArgumentException.class)
	public void createIncompleteCriteria() {
		Iterable<Participant> participants = createParticipants();

		List<FilterCriteria> filterCriteria = Lists.newLinkedList();
		filterCriteria.add(FilterCriteria
				.createKumite(Gender.MALE, 0, Integer.MAX_VALUE, 0, Double.MAX_VALUE, "schwer"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.FEMALE, 0, Integer.MAX_VALUE, 0, Double.MAX_VALUE,
				"schwer"));
		filterCriteria.add(FilterCriteria.createKata(Gender.MALE, 0, Integer.MAX_VALUE, Level.LOWER_STAGE));

		filter.createGroups(participants, filterCriteria);
	}

	@Test
	public void createComplexGroups() {
		Iterable<Participant> participants = createMaleKataParticipants();

		List<FilterCriteria> filterCriteria = Lists.newLinkedList();

		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1990, 1991, 10, 14, "leicht"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1990, 1991, 14, 16, "mittel"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1990, 1991, 16, 19.5, "schwer"));

		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1991, 1993, 10, 14, "leicht"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1991, 1993, 14, 16, "mittel"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1991, 1993, 16, 19.5, "schwer"));

		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1993, 1994, 10, 14, "leicht"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1993, 1994, 14, 16, "mittel"));
		filterCriteria.add(FilterCriteria.createKumite(Gender.MALE, 1993, 1994, 16, 20, "schwer"));

		Iterable<Group> groups = filter.createGroups(participants, filterCriteria);
		assertEquals(9, Iterables.size(groups));
	}

	private Iterable<Participant> createParticipants() {
		List<Participant> participants = Lists.newLinkedList();
		for (int i = 0; i < 20; i++) {
			Gender gender = i % 2 == 0 ? Gender.MALE : Gender.FEMALE;
			boolean startsKata = i % 2 == 0 || i % 5 == 0;
			boolean startsKumite = i % 3 == 0 || !startsKata;
			Participant participant = new Participant("firstname " + i, "lastname " + i, 1990 + (i % 4), 7 - (i % 3),
					10 + i * 0.5, gender, startsKata, startsKumite, "Club" + (i % 4));
			participants.add(participant);
		}
		return participants;
	}

	private Iterable<Participant> createMaleKataParticipants() {
		List<Participant> participants = Lists.newLinkedList();
		for (int i = 0; i < 20; i++) {
			Participant participant = new Participant("firstname " + i, "lastname " + i, 1990 + (i % 4), 7 - (i % 3),
					10 + i * 0.5, Gender.MALE, false, true, "Club" + (i % 4));
			participants.add(participant);
		}
		return participants;
	}
}
