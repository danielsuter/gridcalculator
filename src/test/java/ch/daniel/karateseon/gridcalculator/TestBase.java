package ch.daniel.karateseon.gridcalculator;

import org.junit.Before;

import ch.daniel.karateseon.gridcalculator.model.Participant;

public class TestBase {

	protected final static String BASE_PATH = "target/";
	protected final static String TEST_RESOURCES = "src/test/resources";

	protected static final String CLUB4 = "Club4";
	protected static final String CLUB3 = "Club3";
	protected static final String CLUB2 = "Club2";
	protected static final String CLUB1 = "Club1";

	private int counter;

	@Before
	public void reset() {
		counter = 0;
	}

	protected Participant[] createParticipants(int amount, String club) {
		Participant[] returnValue = new Participant[amount];
		for (int i = 0; i < amount; i++) {
			returnValue[i] = new Participant("Firstname" + counter, "Lastname" + counter, club);
			++counter;
		}

		return returnValue;
	}

}
