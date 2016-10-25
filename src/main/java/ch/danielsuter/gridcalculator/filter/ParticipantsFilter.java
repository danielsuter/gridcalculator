package ch.danielsuter.gridcalculator.filter;

import ch.danielsuter.gridcalculator.model.Gender;
import ch.danielsuter.gridcalculator.model.Group;
import ch.danielsuter.gridcalculator.model.Level;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.MyLogger;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ParticipantsFilter {
	private final static MyLogger logger = MyLogger.getLogger(ParticipantsFilter.class);
	private boolean throwErrorOnNotMatchedParticipant;

	public ParticipantsFilter(boolean throwErrorOnNotMatchedParticipant) {
		this.throwErrorOnNotMatchedParticipant = throwErrorOnNotMatchedParticipant;
	}

	public Iterable<Group> createGroups(Iterable<Participant> participants, Iterable<FilterCriteria> filterCriteria) {
		List<Group> groups = Lists.newLinkedList();

		for (FilterCriteria criteria : filterCriteria) {
			Group group = new Group(criteria);

			Iterable<Participant> filteredParticipants = filter(participants, criteria);
			if (Iterables.size(filteredParticipants) == 0) {
				logger.warn("Filter " + criteria + " did not match any participants!");
			}

			group.add(filteredParticipants);

			groups.add(group);
		}

		assertFilteredMassIsSameAsUnfiltered(participants, groups);

		return groups;
	}

	private void assertFilteredMassIsSameAsUnfiltered(Iterable<Participant> allParticipants, List<Group> groups) {
		HashSet<Participant> participants = Sets.newHashSet(allParticipants);

		for (Group group : groups) {
			List<Participant> participantsFromGroup = group.getAll();
			for (Participant participant : participantsFromGroup) {
				participants.remove(participant);
			}
		}

		if (participants.size() > 0) {
			StringBuilder missingParticipants = new StringBuilder();
			for (Participant participant : participants) {
				missingParticipants.append("\t").append(participant.toString()).append("\n");
			}
			String errorMessage = "Filtercriteria does not match all participants:\n"
					+ missingParticipants;
			if(throwErrorOnNotMatchedParticipant) {
				throw new IllegalArgumentException(errorMessage);
			} else {
				logger.warn(errorMessage);
			}
		}
	}

	private Iterable<Participant> filter(Iterable<Participant> participants, final FilterCriteria criteria) {
		return StreamSupport.stream(participants.spliterator(), false).filter(participant -> {
			if (!participant.getFightTypes().contains(criteria.getFightType())) {
				return false;
			} else if (isRelevant(criteria.getFromWeight()) && participant.getWeight() < criteria.getFromWeight()) {
				return false;
			} else if (isRelevant(criteria.getToWeight()) && participant.getWeight() >= criteria.getToWeight()) {
				return false;
			} else if (criteria.getGender() != Gender.MIXED && participant.getGender() != criteria.getGender()) {
				return false;
			} else if (isRelevant(criteria.getLevel())
					&& Level.createFromKyu(participant.getKyu()) != criteria.getLevel()) {
				return false;
			} else if (participant.getYear() < criteria.getFromYear()) {
				return false;
			} else if (participant.getYear() >= criteria.getToYear()) {
				return false;
			}

			return true;
		}).collect(Collectors.toList());
	}

	private boolean isRelevant(Object value) {
		return value != null;
	}
}
