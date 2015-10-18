package ch.daniel.karateseon.gridcalculator.model;

public enum Gender {
	MALE("Knaben"), 
	FEMALE("Mädchen");

	private final String german;

	private Gender(String german) {
		this.german = german;
	}

	public String toGerman() {
		return german;
	}
}
