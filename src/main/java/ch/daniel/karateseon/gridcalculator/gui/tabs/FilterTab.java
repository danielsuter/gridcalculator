package ch.daniel.karateseon.gridcalculator.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ch.daniel.karateseon.gridcalculator.filter.FightType;
import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.gui.ActionCommands;
import ch.daniel.karateseon.gridcalculator.gui.MainModel;
import ch.daniel.karateseon.gridcalculator.gui.MainWindow;
import ch.daniel.karateseon.gridcalculator.gui.observer.EventType;
import ch.daniel.karateseon.gridcalculator.gui.observer.Observer;
import ch.daniel.karateseon.gridcalculator.gui.util.GCFileFilter;
import ch.daniel.karateseon.gridcalculator.gui.util.JTableHelper;
import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Level;

import com.google.common.collect.Lists;

public class FilterTab extends JPanel implements Observer<MainModel>, ActionListener {

	private static final long serialVersionUID = -5434690837373955108L;

	private final MainModel model;

	private Vector<Vector<Object>> kumiteData;
	private Vector<Vector<Object>> kataData;
	private JPanel mainPanel;
	private final MainWindow mainWindow;
	private JTable kataTable;
	private JTable kumiteTable;

	public FilterTab(MainWindow mainWindow, MainModel model) {
		this.mainWindow = mainWindow;
		this.model = model;
		model.addObserver(this);

		applyPanelSettings();
		addKataFilterBox();
		addKumiteFilterBox();
	}

	private void applyPanelSettings() {
		setLayout(new BorderLayout());
		mainPanel = new JPanel(new GridLayout(0, 1));
		add(mainPanel, BorderLayout.CENTER);

		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox, BorderLayout.SOUTH);

		addMainButton(horizontalBox, "Save...", ActionCommands.SAVE_FILTERS);
		addMainButton(horizontalBox, "Load...", ActionCommands.LOAD_FILTERS_ON_FILTERTAB);
		addMainButton(horizontalBox, "Clear all", ActionCommands.CLEAR_ALL_FILTERS);
	}

	private void addMainButton(JComponent parent, String buttonName, String actionCommand) {
		JButton button = new JButton(buttonName);
		button.setFont(new Font("Verdana", Font.BOLD, 16));
		button.setActionCommand(actionCommand);
		button.addActionListener(this);
		parent.add(button);
	}

	private void addKataFilterBox() {
		JPanel kataBox = new JPanel(new BorderLayout());
		kataBox.setBorder(BorderFactory.createTitledBorder("Kata filters"));

		kataData = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Gender", "From year", "To year (exclusive)",
				"Level"));
		kataTable = JTableHelper.createNonEditable(kataData, columnNames);
		kataTable.setFillsViewportHeight(true);
		kataTable.setRowSorter(createKataRowSorter());
		kataBox.add(new JScrollPane(kataTable), BorderLayout.CENTER);

		Box horizontalBox = Box.createHorizontalBox();
		kataBox.add(horizontalBox, BorderLayout.SOUTH);
		addButton(horizontalBox, "Add...", ActionCommands.ADD_KATA);
		addButton(horizontalBox, "Delete...", ActionCommands.DELETE_KATA);

		mainPanel.add(kataBox);
	}

	private void addKumiteFilterBox() {
		JPanel kumiteBox = new JPanel(new BorderLayout());
		kumiteBox.setBorder(BorderFactory.createTitledBorder("Kumite filters"));

		kumiteData = new Vector<Vector<Object>>();
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Gender", "From year", "To year (exclusive)",
				"From weight", "To weight (exclusive)", "Weight desc"));
		kumiteTable = JTableHelper.createNonEditable(kumiteData, columnNames);
		kumiteTable.setRowSorter(createKumiteRowSorter());
		kumiteTable.setFillsViewportHeight(true);
		kumiteBox.add(new JScrollPane(kumiteTable), BorderLayout.CENTER);

		Box horizontalBox = Box.createHorizontalBox();
		kumiteBox.add(horizontalBox, BorderLayout.SOUTH);
		addButton(horizontalBox, "Add...", ActionCommands.ADD_KUMITE);
		addButton(horizontalBox, "Delete...", ActionCommands.DELETE_KUMITE);

		mainPanel.add(kumiteBox);
	}

	private void addButton(JComponent parent, String buttonName, String actionCommand) {
		JButton button = new JButton(buttonName);
		button.setActionCommand(actionCommand);
		button.addActionListener(this);
		parent.add(button);
	}

	public void update(MainModel observable, EventType eventType, Object object) {
		switch (eventType) {
		case ADD_FILTER:
			addFilter((FilterCriteria) object);
			break;
		case REMOVE_FILTER:
			removeFilter((FilterCriteria) object);
			break;
		case ADD_ALL_FILTERS:
			addAll(model.getFilters());
			break;
		case CLEAR_FILTERS:
			clearAll();
			break;
		default:
			break;
		}
	}

	private void clearAll() {
		kataData.clear();
		kumiteData.clear();
		refreshTable(kataTable);
		refreshTable(kumiteTable);
	}

	private void addAll(Iterable<FilterCriteria> filters) {
		kataData.clear();
		kumiteData.clear();
		for (FilterCriteria filter : filters) {
			if (filter.getFightType() == FightType.KATA) {
				kataData.add(mapKataVector(filter));
			} else {
				kumiteData.add(mapKumiteVector(filter));
			}
		}

		refreshTable(kataTable);
		refreshTable(kumiteTable);
	}

	private void refreshTable(JTable table) {
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.fireTableDataChanged();
	}

	private void addFilter(FilterCriteria filter) {
		if (filter.getFightType() == FightType.KATA) {
			kataData.add(mapKataVector(filter));
			DefaultTableModel tableModel = (DefaultTableModel) kataTable.getModel();
			tableModel.fireTableRowsInserted(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
		} else {
			kumiteData.add(mapKumiteVector(filter));
			DefaultTableModel tableModel = (DefaultTableModel) kumiteTable.getModel();
			tableModel.fireTableRowsInserted(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
		}
	}

	private void removeFilter(FilterCriteria filter) {
		if (filter.getFightType() == FightType.KATA) {
			kataData.remove(mapKataVector(filter));
			DefaultTableModel tableModel = (DefaultTableModel) kataTable.getModel();
			tableModel.fireTableRowsDeleted(0, tableModel.getRowCount());
		} else {
			kumiteData.remove(mapKumiteVector(filter));
			DefaultTableModel tableModel = (DefaultTableModel) kumiteTable.getModel();
			tableModel.fireTableRowsDeleted(0, tableModel.getRowCount());
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (ActionCommands.ADD_KATA.equals(e.getActionCommand())) {
			AddKataFilterDialog addFilterDialog = new AddKataFilterDialog(mainWindow);
			switch (addFilterDialog.getOperationState()) {
			case ADD:
				mainWindow.getController().addFilter(addFilterDialog.getFilter());
				break;
			default:
				break;
			}
		} else if (ActionCommands.DELETE_KATA.equals(e.getActionCommand())) {
			int selectedRow = kataTable.getSelectedRow();
			if (selectedRow != -1) {
				Vector<Object> selectedRowData = kataData.get(selectedRow);
				FilterCriteria kataFilter = mapKataFilter(selectedRowData);
				mainWindow.getController().removeFilter(kataFilter);
			}
		} else if (ActionCommands.ADD_KUMITE.equals(e.getActionCommand())) {
			AddKumiteFilterDialog addFilterDialog = new AddKumiteFilterDialog(mainWindow);
			switch (addFilterDialog.getOperationState()) {
			case ADD:
				mainWindow.getController().addFilter(addFilterDialog.getFilter());
				break;
			default:
				break;
			}
		} else if (ActionCommands.DELETE_KUMITE.equals(e.getActionCommand())) {
			int selectedRow = kumiteTable.getSelectedRow();
			if (selectedRow != -1) {
				Vector<Object> selectedRowData = kumiteData.get(selectedRow);
				FilterCriteria kumiteFilter = mapKumiteFilter(selectedRowData);
				mainWindow.getController().removeFilter(kumiteFilter);
			}
		} else if (ActionCommands.LOAD_FILTERS_ON_FILTERTAB.equals(e.getActionCommand())) {
			handleLoad();
		} else if (ActionCommands.SAVE_FILTERS.equals(e.getActionCommand())) {
			handleSave();
		} else if (ActionCommands.CLEAR_ALL_FILTERS.equals(e.getActionCommand())) {
			mainWindow.getController().clearFilters();
		} else {
			throw new RuntimeException("Cannot handle action command " + e.getActionCommand());
		}
	}

	private void handleLoad() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(getFileFilter());
		int fileChooserResult = fileChooser.showOpenDialog(this);
		if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				mainWindow.getController().loadFilters(selectedFile);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(this, "Failed to load filters", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void handleSave() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(getFileFilter());
		int fileChooserResult = fileChooser.showSaveDialog(this);
		if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			boolean extensionMissing = !selectedFile.getName().endsWith(GCFileFilter.FILTER.getExtension());
			if (extensionMissing) {
				selectedFile = new File(selectedFile.getAbsolutePath() + GCFileFilter.FILTER.getExtension());
			}
			try {
				mainWindow.getController().saveFilters(selectedFile);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "Failed to save filters", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private FileFilter getFileFilter() {
		return GCFileFilter.FILTER.get();
	}

	private FilterCriteria mapKataFilter(Vector<Object> dataVector) {
		return FilterCriteria.createKata((Gender) dataVector.get(0), (Integer) dataVector.get(1),
				(Integer) dataVector.get(2), (Level) dataVector.get(3));
	}

	private FilterCriteria mapKumiteFilter(Vector<Object> dataVector) {
		return FilterCriteria.createKumite((Gender) dataVector.get(0), (Integer) dataVector.get(1),
				(Integer) dataVector.get(2), (Double) dataVector.get(3), (Double) dataVector.get(4),
				(String) dataVector.get(5));
	}

	@SuppressWarnings("unchecked")
	private Vector<Object> mapKataVector(FilterCriteria filter) {
		return new Vector<Object>(Arrays.asList(filter.getGender(), filter.getFromYear(), filter.getToYear(),
				filter.getLevel()));
	}

	@SuppressWarnings("unchecked")
	private Vector<Object> mapKumiteVector(FilterCriteria filter) {
		return new Vector<Object>(Arrays.asList(filter.getGender(), filter.getFromYear(), filter.getToYear(),
				filter.getFromWeight(), filter.getToWeight(), filter.getWeightString()));
	}

	private RowSorter<TableModel> createKataRowSorter() {
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(kataTable.getModel());
		List<RowSorter.SortKey> sortKeys = Lists.newLinkedList();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		rowSorter.setSortKeys(sortKeys);
		return rowSorter;
	}

	private RowSorter<TableModel> createKumiteRowSorter() {
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(kumiteTable.getModel());
		List<RowSorter.SortKey> sortKeys = Lists.newLinkedList();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
		rowSorter.setSortKeys(sortKeys);
		return rowSorter;
	}
}
