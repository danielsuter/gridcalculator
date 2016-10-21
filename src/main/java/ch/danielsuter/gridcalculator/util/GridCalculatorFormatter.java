package ch.danielsuter.gridcalculator.util;

import ch.danielsuter.gridcalculator.filter.FightType;
import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Participant;

public class GridCalculatorFormatter {
	private GridCalculatorFormatter() {
	}

	public static String formatForGrid(Participant participant) {
		return String.format("%s, %s", participant.getLastname(), participant.getFirstname());
	}

	/**
	 * Formats a given filter criteria like this:<br>
	 * <b>Kata</b> Kata männlich Unterstufe 2000-2002<br>
	 * <b>Kumite</b> Kumite männlich 2000-2002 leicht<br>
	 */
	public static String formatForGroupSheetHeader(FilterCriteria filterCriteria) {
		if (filterCriteria.getFightType() == FightType.KATA) {
			return formatForKataGroupSheetHeader(filterCriteria);
		} else if (filterCriteria.getFightType() == FightType.KUMITE) {
			return formatForKumiteGroupSheetHeader(filterCriteria);
		} else {
			throw new RuntimeException("Unknown FightType " + filterCriteria.getFightType());
		}

	}

	private static String formatForKumiteGroupSheetHeader(FilterCriteria criteria) {
		String levelString = criteria.getLevel() == null ? ""  : " " + criteria.getLevel().toGerman();
		
		if(criteria.getWeightString() == null) {
			return String.format("%s %s%s %d-%d", criteria.getFightType().toCamelCase(), criteria
					.getGender().toGerman(), levelString, criteria.getFromYear(), criteria.getToYear() - 1);
		} else {
			return String.format("%s %s%s %d-%d %s", criteria.getFightType().toCamelCase(), criteria
					.getGender().toGerman(), levelString, criteria.getFromYear(), criteria.getToYear() - 1,
					criteria.getWeightString());
		}
	}

	private static String formatForKataGroupSheetHeader(FilterCriteria filterCriteria) {
		return String.format("%s %s %s %d-%d", filterCriteria.getFightType().toCamelCase(), filterCriteria
				.getGender().toGerman(), filterCriteria.getLevel().toGerman(), filterCriteria.getFromYear(),
				filterCriteria.getToYear() - 1);
	}

	/**
	 * Formats a given filter criteria like this:<br>
	 * <b>Kata</b> kata-male-2000_2002-unterstufe-group<br>
	 * <b>Kumite</b> kumite-female-1999_2002-schwer-drawing<br>
	 * <b>Kumite without weight</b> kumite-female-1999_2002-drawing<br>
	 */
	public static String formatForFilename(FilterCriteria criteria, String suffix) {
		if (criteria.getFightType() == FightType.KATA) {
			return formatForKataFilename(criteria, suffix);
		} else if (criteria.getFightType() == FightType.KUMITE) {
			return formatForKumiteFilename(criteria, suffix);
		} else {
			throw new RuntimeException("Unknown FightType " + criteria.getFightType());
		}
	}

	private static String formatForKataFilename(FilterCriteria criteria, String suffix) {
		return String.format("%s-%s-%d_%d-%s-%s", criteria.getFightType(), criteria.getGender(),
				criteria.getFromYear(), criteria.getToYear() - 1, criteria.getLevel().toGerman(), suffix)
				.toLowerCase();
	}

	private static String formatForKumiteFilename(FilterCriteria criteria, String suffix) {
		String levelString = criteria.getLevel() == null ? "" : criteria.getLevel().toGerman() + "-";
		if (criteria.getWeightString() == null) {
			return String.format("%s-%s-%s%d_%d-%s", criteria.getFightType(), criteria.getGender(),
					levelString, criteria.getFromYear(), criteria.getToYear() - 1, suffix).toLowerCase();
		} else {
			return String.format("%s-%s-%s%d_%d-%s-%s", criteria.getFightType(), criteria.getGender(),
					levelString, criteria.getFromYear(), criteria.getToYear() - 1, criteria.getWeightString(), suffix)
					.toLowerCase();
		}
	}
}
