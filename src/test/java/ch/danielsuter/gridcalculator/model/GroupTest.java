package ch.danielsuter.gridcalculator.model;

import static org.junit.Assert.*;

import java.util.List;

import ch.danielsuter.gridcalculator.TestBase;
import org.junit.Test;

public class GroupTest extends TestBase {
	@Test
	public void getParticipantFromLargestClub() {
		Group group = new Group();
		group.add(createParticipants(4, CLUB1));
		group.add(createParticipants(3, CLUB2));
		group.add(createParticipants(2, CLUB3));

		Participant participantFromLargestClub = group.removeParticipantFromLargestClub();
		assertEquals(CLUB1, participantFromLargestClub.getClub());
		assertEquals("Participant must be removed", 8, group.getSize());
	}

	@Test
	public void removeRandomParticipant() {
		Group group = new Group();
		group.add(createParticipants(4, CLUB1));
		group.add(createParticipants(1, CLUB2));
		Participant randomParticipant = group.removeRandomParticipant(CLUB1);
		assertEquals("Participant must not be of club 1", CLUB2, randomParticipant.getClub());
		assertEquals("Participant must be removed", 4, group.getSize());
	}

	@Test
	public void divideEquallyDifferentClubs() {
		for (int i = 0; i < 100; i++) {
			reset();

			Group group = new Group();
			group.add(createParticipants(2, CLUB1));
			group.add(createParticipants(2, CLUB2));
			assertEquals("4 participants", 4, group.getSize());

			Group[] groups = group.divideEqually();
			Group group1 = groups[0];
			assertEquals("2 participants per group", 2, group1.getSize());
			Group group2 = groups[1];
			assertEquals("2 participants per group", 2, group2.getSize());

			Participant randomParticipant = group1.removeRandomParticipant("");
			Participant randomParticipant2 = group1.removeRandomParticipant("");
			assertTrue("Clubs must differ",
					!randomParticipant.getClub().equals(randomParticipant2.getClub()));
		}

	}

	@Test
	public void divideEquallySameClub() {
		Group group = new Group();
		group.add(createParticipants(8, CLUB1));
		assertEquals("Number of participants", 8, group.getSize());

		Group[] groups = group.divideEqually();
		Group group1 = groups[0];
		assertEquals("4 participants per group", 4, group1.getSize());
		Group group2 = groups[1];
		assertEquals("4 participants per group", 4, group2.getSize());
	}

	@Test
	public void divideEquallyUnevenNumber() {
		Group group = new Group();
		group.add(createParticipants(5, CLUB1));
		group.add(createParticipants(2, CLUB2));
		assertEquals("7 participants", 7, group.getSize());

		Group[] groups = group.divideEqually();
		Group group1 = groups[0];
		Group group2 = groups[1];

		assertTrue("Difference must be smaller than 2",
				Math.abs(group1.getSize() - group2.getSize()) < 2);
		List<Participant> group1Club1 = group1.getAll(CLUB1);
		List<Participant> group2Club1 = group2.getAll(CLUB1);
		assertTrue("Difference must be smaller than 2",
				Math.abs(group1Club1.size() - group2Club1.size()) < 2);

		assertEquals("No participants get lost", 7, group1.getSize() + group2.getSize());

	}
}
