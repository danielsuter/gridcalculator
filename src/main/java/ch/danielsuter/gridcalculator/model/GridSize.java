package ch.danielsuter.gridcalculator.model;

public enum GridSize {
	TINY(3), SMALL(16), LARGE(32);

	private final int size;

	GridSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", name(), size);
	}
}
