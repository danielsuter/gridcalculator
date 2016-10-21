package ch.danielsuter.gridcalculator.filter;

import java.util.HashSet;
import java.util.List;

import ch.danielsuter.gridcalculator.model.Group;
import ch.danielsuter.gridcalculator.model.Level;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.MyLogger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ParticipantsFilter {
	private final static MyLogger logger = MyLogger.getLogger(ParticipantsFilter.class);

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
			throw new IllegalArgumentException("Filtercriteria does not match all participants:\n"
					+ missingParticipants);
		}
	}

	private Iterable<Participant> filter(Iterable<Participant> participants, final FilterCriteria criteria) {
		return Iterables.filter(participants, new Predicate<Participant>() {

			public boolean apply(Participant participant) {

				if (!participant.getFightTypes().contains(criteria.getFightType())) {
					return false;
				} else if (isRelevant(criteria.getFromWeight()) && participant.getWeight() < criteria.getFromWeight()) {
					return false;
				} else if (isRelevant(criteria.getToWeight()) && participant.getWeight() >= criteria.getToWeight()) {
					return false;
				} else if (participant.getGender() != criteria.getGender()) {
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
			}

			private boolean isRelevant(Object value) {
				return value != null;
			}
		});
	}
}
