package ch.daniel.karateseon.gridcalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Participant;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class ParticipantsReaderTest extends TestBase {
	private ParticipantsReader reader = new ParticipantsReader();

	@Test
	public void readKataParticipants() throws FileNotFoundException, IOException {
		Iterable<Participant> participants = parseAndAssertSize("teilnehmerliste_kata", 9);

		Participant participant = getParticipantByName(participants, "Nachname9", "Vorname9");
		assertParticipantData(participant, 1990, 5, 56d, Gender.MALE, true, false, "Club 5");
	}

	@Test
	public void readKata2Participants() throws FileNotFoundException, IOException {
		Iterable<Participant> participants = parseAndAssertSize("teilnehmerliste_kumite", 6);

		Participant participant = getParticipantByName(participants, "Nachname3", "Vorname3");
		assertParticipantData(participant, 1990, 5, 56d, Gender.MALE, false, true, "Club1");
	}

	@Test
	public void readKumiteParticipants() throws FileNotFoundException, IOException {
		Iterable<Participant> participants = parseAndAssertSize("teilnehmerliste_kumite", 6);

		Participant participant = getParticipantByName(participants, "Nachname4", "Vorname4");
		assertParticipantData(participant, 1990, 5, 56d, Gender.MALE, true, true, "Club1");
	}

	private void assertParticipantData(Participant participant, int year, Integer kyu, Double weight, Gender gender,
			boolean startsKata, boolean startsKumite, String club) {
		assertEquals("year", year, participant.getYear());
		assertEquals("kyu", kyu, participant.getKyu());
		assertEquals("weight", weight, participant.getWeight(), 0.01);
		assertEquals("gender", gender, participant.getGender());
		assertEquals("kata", startsKata, participant.isStartsKata());
		assertEquals("kumite", startsKumite, participant.isStartsKumite());
		assertEquals("club", club, participant.getClub());
	}

	private Participant getParticipantByName(Iterable<Participant> participants, String lastname, String firstname) {
		Iterable<Participant> filteredParticpants = Iterables.filter(participants, new FindByFullName(lastname,
				firstname));
		int size = Iterables.size(filteredParticpants);
		assertEquals("Exactly one participant found", 1, size);
		return filteredParticpants.iterator().next();
	}

	private Iterable<Participant> parseAndAssertSize(String fileName, int expectedSize) throws FileNotFoundException,
			IOException {
		File excelFile = getExistingExcel(fileName);
		Iterable<Participant> participants = reader.readParticipantsFromExcel(excelFile);
		assertEquals("Number of participants", expectedSize, Iterables.size(participants));
		return participants;
	}

	private File getExistingExcel(String name) {
		File excelFile = new File(TEST_RESOURCES, name + ".xlsx");
		assertTrue("Test-Excel-File exists " + excelFile.getAbsolutePath(), excelFile.exists());
		return excelFile;
	}

	class FindByFullName implements Predicate<Participant> {

		private final String lastname;
		private final String firstname;

		public FindByFullName(String lastname, String firstname) {
			this.lastname = lastname;
			this.firstname = firstname;
		}

		public boolean apply(Participant participant) {
			if (participant.getLastname().equals(lastname) && participant.getFirstname().equals(firstname)) {
				return true;
			}

			return false;
		}

	}
}
