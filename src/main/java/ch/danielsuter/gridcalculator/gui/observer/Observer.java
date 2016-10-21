package ch.danielsuter.gridcalculator.gui.observer;

public interface Observer<T> {
	public void update(T observable, EventType eventType, Object object);
}
