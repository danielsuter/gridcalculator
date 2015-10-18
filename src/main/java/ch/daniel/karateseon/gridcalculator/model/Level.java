package ch.daniel.karateseon.gridcalculator.model;

public enum Level {
	LOWER_STAGE("Unterstufe"), UPPER_STAGE("Oberstufe");

	public final String german;

	public static Level createFromKyu(int kyu) {
		if (kyu >= 6) {
			return LOWER_STAGE;
		} else {
			return Level.UPPER_STAGE;
		}
	}

	private Level(String german) {
		this.german = german;
	}

	public String toGerman() {
		return german;
	}
}
