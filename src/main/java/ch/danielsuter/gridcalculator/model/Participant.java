package ch.danielsuter.gridcalculator.model;

import java.util.Set;

import ch.danielsuter.gridcalculator.filter.FightType;

import com.google.common.collect.Sets;

public class Participant {
	private String firstname;
	private String lastname;
	private String club;

	private int year;
	private Integer kyu;
	private Double weight;
	private Gender gender;
	private boolean startsKata;
	private boolean startsKumite;

	public Participant(String firstname, String lastname, int year, Integer kyu, Double weight, Gender gender,
			boolean startsKata, boolean startsKumite, String club) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.year = year;
		this.kyu = kyu;
		this.weight = weight;
		this.gender = gender;
		this.startsKata = startsKata;
		this.startsKumite = startsKumite;
		this.club = club;
	}

	/**
	 * For testing only
	 */
	public Participant(String firstname, String lastname, String club) {
		this(firstname, lastname, 0, null, null, null, false, false, club);
	}

	/**
	 * Only for serialization
	 */
	public Participant() {
	}

	public int getYear() {
		return year;
	}

	public Integer getKyu() {
		return kyu;
	}

	public Double getWeight() {
		return weight;
	}

	public Gender getGender() {
		return gender;
	}

	public boolean isStartsKata() {
		return startsKata;
	}

	public boolean isStartsKumite() {
		return startsKumite;
	}

	public String getClub() {
		return club;
	}

	public String getLastname() {
		return lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	@Override
	public String toString() {
		return "Participant [firstname=" + firstname + ", lastname=" + lastname + ", club=" + club + ", year=" + year
				+ ", kyu=" + kyu + ", weight=" + weight + ", gender=" + gender + ", startsKata=" + startsKata
				+ ", startsKumite=" + startsKumite + "]";
	}

	public Set<FightType> getFightTypes() {
		Set<FightType> fightTypes = Sets.newHashSet();
		if (isStartsKata()) {
			fightTypes.add(FightType.KATA);
		}
		if (isStartsKumite()) {
			fightTypes.add(FightType.KUMITE);
		}
		return fightTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((club == null) ? 0 : club.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((kyu == null) ? 0 : kyu.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + (startsKata ? 1231 : 1237);
		result = prime * result + (startsKumite ? 1231 : 1237);
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		result = prime * result + year;
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
		Participant other = (Participant) obj;
		if (club == null) {
			if (other.club != null)
				return false;
		} else if (!club.equals(other.club))
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (gender != other.gender)
			return false;
		if (kyu == null) {
			if (other.kyu != null)
				return false;
		} else if (!kyu.equals(other.kyu))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (startsKata != other.startsKata)
			return false;
		if (startsKumite != other.startsKumite)
			return false;
		if (weight == null) {
			if (other.weight != null)
				return false;
		} else if (!weight.equals(other.weight))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

}
