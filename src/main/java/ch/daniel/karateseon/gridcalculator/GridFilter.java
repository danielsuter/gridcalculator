package ch.daniel.karateseon.gridcalculator;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import ch.daniel.karateseon.gridcalculator.model.Grid;
import ch.daniel.karateseon.gridcalculator.model.Group;
import ch.daniel.karateseon.gridcalculator.model.Participant;

public class GridFilter {
	public Iterable<Grid> getByClub(Iterable<Grid> grids, final String club) {
		return Iterables.filter(grids, new Predicate<Grid>() {

			public boolean apply(Grid grid) {
				Group group = grid.getGroup();
				List<Participant> participants = group.getAll(club);
				if (participants.size() > 0) {
					return true;
				}

				return false;
			}
		});
	}
}
