package ch.danielsuter.gridcalculator.gui;

import java.util.List;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.gui.observer.EventType;
import ch.danielsuter.gridcalculator.gui.observer.Observable;
import ch.danielsuter.gridcalculator.model.Grid;

import com.google.common.collect.Lists;

public class MainModel extends Observable<MainModel> {
	private List<FilterCriteria> filters = Lists.newLinkedList();
	private List<Grid> grids = Lists.newLinkedList();

	public void addFilter(FilterCriteria filter) {
		filters.add(filter);
		notifyAll(this, EventType.ADD_FILTER, filter);
	}

	public boolean removeFilter(FilterCriteria filter) {
		boolean success = filters.remove(filter);
		if(success) {
			notifyAll(this, EventType.REMOVE_FILTER, filter);
		}
		return success;
	}
	
	public Iterable<FilterCriteria> getFilters() {
		return filters;
	}

	public void setFilters(Iterable<FilterCriteria> filters) {
		this.filters = Lists.newLinkedList(filters);
		notifyAll(this, EventType.ADD_ALL_FILTERS, filters);
	}

	public void clearFilters() {
		this.filters = Lists.newLinkedList(filters);
		notifyAll(this, EventType.CLEAR_FILTERS, null);
	}

	public void addGrid(Grid grid) {
		grids.add(grid);
		notifyAll(this, EventType.ADD_GRID, grid);
	}
	
	public void clearGrids() {
		grids.clear();
		notifyAll(this, EventType.CLEAR_GRIDS, null);
	}

	public Iterable<Grid> getGrids() {
		return grids;
	}

	public void setGrids(Iterable<Grid> grids) {
		this.grids = Lists.newLinkedList(grids);
		notifyAll(this, EventType.ADD_ALL_GRIDS, grids);
	}
	
	public void notifyProcessingStarted() {
		notifyAll(this, EventType.PROCESSING_STARTED, null);
	}
	
	public void notifyProcessingStopped() {
		notifyAll(this, EventType.PROCESSING_STOPPED, null);
	}
}
