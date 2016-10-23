package ch.danielsuter.gridcalculator.model;

public enum Gender {
	MALE("Knaben"), 
	FEMALE("Mädchen");

	private final String german;

	Gender(String german) {
		this.german = german;
	}

	public String toGerman() {
		return german;
	}
}
