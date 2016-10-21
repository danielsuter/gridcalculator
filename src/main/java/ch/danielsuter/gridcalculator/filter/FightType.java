package ch.danielsuter.gridcalculator.filter;

public enum FightType {
	KUMITE, KATA;
	
	public String toCamelCase() {
		return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
	}
}
