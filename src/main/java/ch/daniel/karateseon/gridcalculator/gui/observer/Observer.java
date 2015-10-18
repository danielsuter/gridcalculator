package ch.daniel.karateseon.gridcalculator.gui.observer;

public interface Observer<T> {
	public void update(T observable, EventType eventType, Object object);
}
