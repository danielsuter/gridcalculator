package ch.danielsuter.gridcalculator.gui.observer;

public interface Observer<T> {
	void update(T observable, EventType eventType, Object object);
}
