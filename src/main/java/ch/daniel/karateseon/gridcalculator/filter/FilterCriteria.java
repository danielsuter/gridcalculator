package ch.daniel.karateseon.gridcalculator.filter;

import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Level;

public class FilterCriteria {
	private FightType fightType;
	private Gender gender;
	private int fromYear;
	/**
	 * exclusive
	 */
	private int toYear;
	private Double fromWeight;
	/**
	 * exclusive
	 */
	private Double toWeight;
	private String weightString;
	private Level level;

	private FilterCriteria() {
	}

	/**
	 * 
	 * @param weightString
	 *            if null, will be omitted
	 */
	public static FilterCriteria createKumite(Gender gender, int fromYear, int toYear, double fromWeight,
			double toWeight, String weightString) {
		FilterCriteria criteria = new FilterCriteria();
		criteria.fightType = FightType.KUMITE;
		criteria.gender = gender;
		criteria.fromYear = fromYear;
		criteria.toYear = toYear;
		criteria.fromWeight = fromWeight;
		criteria.toWeight = toWeight;
		criteria.weightString = weightString;
		return criteria;
	}

	public static FilterCriteria createKata(Gender gender, int fromYear, int toYear, Level level) {
		FilterCriteria criteria = new FilterCriteria();
		criteria.level = level;
		criteria.fightType = FightType.KATA;
		criteria.gender = gender;
		criteria.fromYear = fromYear;
		criteria.toYear = toYear;
		return criteria;
	}

	public FightType getFightType() {
		return fightType;
	}

	public Gender getGender() {
		return gender;
	}

	public int getFromYear() {
		return fromYear;
	}

	public int getToYear() {
		return toYear;
	}

	/**
	 * Only relevant for {@link FightType#KUMITE}
	 */
	public Double getFromWeight() {
		return fromWeight;
	}

	/**
	 * Only relevant for {@link FightType#KUMITE}
	 */
	public Double getToWeight() {
		return toWeight;
	}

	/**
	 * Only relevant for {@link FightType#KUMITE}
	 */
	public String getWeightString() {
		return weightString;
	}

	/**
	 * Only relevant for {@link FightType#KATA}
	 */
	public Level getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return "FilterCriteria [fightType=" + fightType + ", gender=" + gender + ", fromYear=" + fromYear + ", toYear="
				+ toYear + ", fromWeight=" + fromWeight + ", toWeight=" + toWeight + ", weightString=" + weightString
				+ ", level=" + level + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fightType == null) ? 0 : fightType.hashCode());
		result = prime * result + ((fromWeight == null) ? 0 : fromWeight.hashCode());
		result = prime * result + fromYear;
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((toWeight == null) ? 0 : toWeight.hashCode());
		result = prime * result + toYear;
		result = prime * result + ((weightString == null) ? 0 : weightString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterCriteria other = (FilterCriteria) obj;
		if (fightType != other.fightType)
			return false;
		if (fromWeight == null) {
			if (other.fromWeight != null)
				return false;
		} else if (!fromWeight.equals(other.fromWeight))
			return false;
		if (fromYear != other.fromYear)
			return false;
		if (gender != other.gender)
			return false;
		if (level != other.level)
			return false;
		if (toWeight == null) {
			if (other.toWeight != null)
				return false;
		} else if (!toWeight.equals(other.toWeight))
			return false;
		if (toYear != other.toYear)
			return false;
		if (weightString == null) {
			if (other.weightString != null)
				return false;
		} else if (!weightString.equals(other.weightString))
			return false;
		return true;
	}

}
