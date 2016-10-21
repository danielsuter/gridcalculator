package ch.danielsuter.gridcalculator.model;

import java.util.Arrays;

import ch.danielsuter.gridcalculator.util.GridCalculatorFormatter;

public class Grid {
	private Participant[] positions;
	private GridSize size;
	private Group group;

	public Grid(GridSize gridSize, Group group) {
		this.size = gridSize;
		this.group = group;
		this.positions = new Participant[gridSize.getSize()];
	}
	
	/**
	 * Only for serialization
	 */
	public Grid() {
	}
	
	public Participant getPosition(int positionIndex) {
		return positions[positionIndex];
	}

	public Participant[] getPositions() {
		return positions;
	}

	public void setPosition(int positionIndex, Participant participant) {
		positions[positionIndex] = participant;
	}

	public GridSize getSize() {
		return size;
	}
	
	public Group getGroup() {
		return group;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Grid size: ").append(size).append("\n");

		if (size == GridSize.TINY) {
			for (Participant participant : positions) {
				sb.append("\t").append(GridCalculatorFormatter.formatForGrid(participant)).append("\n");
			}
		} else {
			for (int i = 0; i < size.getSize(); i += 2) {
				Participant participant = positions[i];
				Participant participant2 = positions[i + 1];
				sb.append(GridCalculatorFormatter.formatForGrid(participant)).append(" vs ")
						.append(GridCalculatorFormatter.formatForGrid(participant2)).append("\n");
			}
		}

		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(positions);
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		Grid other = (Grid) obj;
		if (!Arrays.equals(positions, other.positions))
			return false;
		if (size != other.size)
			return false;
		return true;
	}
}
