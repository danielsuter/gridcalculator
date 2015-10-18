package ch.daniel.karateseon.gridcalculator.gui.observer;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class Observable<T> {
	private List<Observer<T>> observers = Lists.newLinkedList();
	
	public void notifyAll(T observable, EventType eventType, Object object) {
		for (Observer<T> observer : observers) {
			observer.update(observable, eventType, object);
		}
	}
	
	public void addObserver(Observer<T> observer) {
		observers.add(observer);
	}
}
