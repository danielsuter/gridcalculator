package ch.daniel.karateseon.gridcalculator.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.util.GridCalculatorFormatter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Group implements Cloneable {

	private List<Participant> participants = new LinkedList<Participant>();
	private Map<String, List<Participant>> clubToParticipantsMap = new HashMap<String, List<Participant>>();
	private final FilterCriteria filterCriteria;

	public Group(FilterCriteria criteria) {
		this.filterCriteria = criteria;
	}

	public Group() {
		this(null);
	}

	public Group[] divideEqually() {
		Group[] groups = new Group[2];
		groups[0] = new Group();
		groups[1] = new Group();

		double halfGroupSize = participants.size() / 2;
		for (int i = 0; i < halfGroupSize; i++) {
			Participant participant = removeParticipantFromLargestClub();
			groups[0].add(participant);

			Participant participant2 = removeParticipantFromClub(participant.getClub());
			if (participant2 == null) {
				participant2 = removeParticipantFromLargestClub();
			}
			groups[1].add(participant2);
		}

		boolean groupUneven = participants.size() % 2 != 0;
		if (groupUneven) {
			groups[0].add(participants.get(getSize() - 1));
		}

		return groups;
	}

	private Participant removeParticipantFromClub(String club) {
		Random random = new Random();

		List<Participant> participantsFromClub = clubToParticipantsMap.get(club);
		if (participantsFromClub == null || participantsFromClub.size() == 0) {
			return null;
		}

		Participant randomParticipant = participantsFromClub.get(random.nextInt(participantsFromClub.size()));

		removeParticipant(randomParticipant);

		return randomParticipant;
	}

	/**
	 * Removes a random {@link Participant} from this group. If it is not
	 * possible to remove a participant without the excluded club, the excluded
	 * club will be used as well.
	 */
	public Participant removeRandomParticipant(String excludedClub) {
		Random random = new Random();
		Set<String> clubs = clubToParticipantsMap.keySet();

		clubs.remove(excludedClub);
		String[] clubArray = clubs.toArray(new String[] {});
		String randomClub = null;
		if (clubArray.length == 0) {
			randomClub = excludedClub;
		} else {
			randomClub = clubArray[random.nextInt(clubArray.length)];
		}

		Participant returnValue = removeParticipantFromClub(randomClub);
		return returnValue;
	}

	public Participant removeParticipantFromLargestClub() {
		List<Participant> largestClub = getLargestClub();
		Random random = new Random();
		int randomId = random.nextInt(largestClub.size());
		Participant participantToReturn = largestClub.get(randomId);
		removeParticipant(participantToReturn);
		return participantToReturn;
	}

	private void removeParticipant(Participant participant) {
		participants.remove(participant);
		List<Participant> participantsFromClub = clubToParticipantsMap.get(participant.getClub());
		participantsFromClub.remove(participant);
		if (participantsFromClub.size() == 0) {
			clubToParticipantsMap.remove(participant.getClub());
		}
	}

	private List<Participant> getLargestClub() {
		List<Participant> currLargestClub = new LinkedList<Participant>();
		Set<Entry<String, List<Participant>>> entrySet = clubToParticipantsMap.entrySet();
		for (Entry<String, List<Participant>> entry : entrySet) {
			if (entry.getValue().size() > currLargestClub.size()) {
				currLargestClub = entry.getValue();
			}
		}

		return currLargestClub;
	}

	public int getSize() {
		return participants.size();
	}

	public void add(Participant... participants) {
		add(Arrays.asList(participants));
	}

	public void add(Iterable<Participant> participants) {
		for (Participant participant : participants) {
			this.participants.add(participant);
			addToClubMap(participant);
		}
	}

	private void addToClubMap(Participant participant) {
		String club = participant.getClub();
		List<Participant> participantList = this.clubToParticipantsMap.get(club);
		if (participantList == null) {
			participantList = new LinkedList<Participant>();
			clubToParticipantsMap.put(club, participantList);
		}
		participantList.add(participant);
	}

	public List<Participant> getAll(String club) {
		List<Participant> returnValue = new LinkedList<Participant>();
		for (Participant participant : participants) {
			if (participant.getClub().equals(club)) {
				returnValue.add(participant);
			}
		}

		return returnValue;
	}

	public List<Participant> getAll() {
		return participants;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Participant participant : participants) {
			sb.append("\t").append(participant).append("\n");
		}
		return sb.toString();
	}
	
	public String toSummaryString() {
		String groupName = GridCalculatorFormatter.formatForGroupSheetHeader(filterCriteria);
		return groupName + " (" + participants.size() + ")";
	}

	public FilterCriteria getFilterCriteria() {
		Preconditions.checkNotNull(filterCriteria, "FilterCriteria must be set for top level group");
		return filterCriteria;
	}

	@Override
	public Group clone() {
		Group returnValue = new Group(filterCriteria);
		List<Participant> clonedParticipantsList = Lists.newLinkedList();

		for (Participant participant : participants) {
			clonedParticipantsList.add(participant);
		}
		returnValue.add(clonedParticipantsList);

		return returnValue;
	}

	public Set<String> getClubs() {
		return clubToParticipantsMap.keySet();
	}
}
