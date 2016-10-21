package ch.danielsuter.gridcalculator.gui.tabs;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import ch.danielsuter.gridcalculator.gui.ActionCommands;
import ch.danielsuter.gridcalculator.gui.MainModel;
import ch.danielsuter.gridcalculator.gui.MainWindow;
import ch.danielsuter.gridcalculator.gui.observer.EventType;
import ch.danielsuter.gridcalculator.gui.observer.Observer;
import ch.danielsuter.gridcalculator.gui.util.GCFileFilter;
import ch.danielsuter.gridcalculator.gui.util.JTableHelper;
import ch.danielsuter.gridcalculator.model.Grid;
import ch.danielsuter.gridcalculator.model.Group;
import ch.danielsuter.gridcalculator.util.GridCalculatorFormatter;

public class DrawingsTab extends JPanel implements Observer<MainModel>, ActionListener {

	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow;
	@SuppressWarnings("unused")
	private MainModel model;

	private Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	private JTextField participantsFile;
	private JTable gridTable;
	private JTextField excelDirectory;
	private JProgressBar progressBar;

	public DrawingsTab(MainWindow mainWindow, MainModel model) {
		this.mainWindow = mainWindow;
		this.model = model;
		model.addObserver(this);

		applyPanelSettings();
	}

	private void applyPanelSettings() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		addParticipantFileChooser();
		addGridGenerator();
		addExcelGenerator();

		addProgressBar();
	}

	private void addProgressBar() {
		progressBar = new JProgressBar();
		add(progressBar);
	}

	private void addExcelGenerator() {
		JPanel excelPanel = new JPanel();
		excelPanel.setLayout(new BoxLayout(excelPanel, BoxLayout.Y_AXIS));
		excelPanel.setBorder(BorderFactory.createTitledBorder("Excel"));
		excelPanel.setMaximumSize(new Dimension(mainWindow.getWidth(), 100));
		excelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(excelPanel);

		Box excelDirectoryBox = Box.createHorizontalBox();
		excelDirectoryBox.add(new JLabel("Output directory"));
		excelDirectoryBox.add(Box.createHorizontalStrut(10));
		excelDirectory = new JTextField();
		excelDirectoryBox.add(excelDirectory);
		JButton selectExcelOutputDir = new JButton("...");
		selectExcelOutputDir.setActionCommand(ActionCommands.SELECT_EXCEL_OUTPUT_DIR);
		selectExcelOutputDir.addActionListener(this);
		excelDirectoryBox.add(selectExcelOutputDir);
		JButton openLocation = new JButton("Open");
		openLocation.setActionCommand(ActionCommands.OPEN_EXCEL_LOCATION);
		openLocation.addActionListener(this);
		excelDirectoryBox.add(openLocation);
		excelPanel.add(excelDirectoryBox);

		Box buttonBox = Box.createVerticalBox();
		buttonBox.setMaximumSize(new Dimension(mainWindow.getWidth(), 50));
		buttonBox.setAlignmentX(CENTER_ALIGNMENT);
		excelPanel.add(buttonBox);
		buttonBox.add(Box.createVerticalStrut(5));
		JButton generateExcels = new JButton("Generate excels");
		generateExcels.setActionCommand(ActionCommands.GENERATE_EXCELS);
		generateExcels.addActionListener(this);
		buttonBox.add(generateExcels);
	}

	private void addGridGenerator() {
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
		gridPanel.setBorder(BorderFactory.createTitledBorder("Grid"));
		gridPanel.setMaximumSize(new Dimension(mainWindow.getWidth(), 200));
		gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(gridPanel);

		Vector<String> columnNames = new Vector<String>(Arrays.asList("Category name", "Number of participants"));
		gridTable = JTableHelper.createNonEditable(rowData, columnNames);
		gridTable.setFillsViewportHeight(true);
		gridTable.setAutoCreateRowSorter(true);
		gridPanel.add(new JScrollPane(gridTable));
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.setMaximumSize(new Dimension(mainWindow.getWidth(), 55));
		gridPanel.add(buttonBox);
		JButton generateGrids = new JButton("Generate grids");
		generateGrids.setActionCommand(ActionCommands.GENERATE_GRIDS);
		generateGrids.addActionListener(this);
		buttonBox.add(generateGrids);
		JButton saveButton = new JButton("Save...");
		saveButton.setActionCommand(ActionCommands.SAVE_GRIDS);
		saveButton.addActionListener(this);
		buttonBox.add(saveButton);
		JButton loadButton = new JButton("Load...");
		loadButton.setActionCommand(ActionCommands.LOAD_GRIDS);
		loadButton.addActionListener(this);
		buttonBox.add(loadButton);

	}

	private void addParticipantFileChooser() {
		JPanel participants = new JPanel();
		participants.setLayout(new BoxLayout(participants, BoxLayout.X_AXIS));
		participants.setBorder(BorderFactory.createTitledBorder("Participants"));
		participants.setMaximumSize(new Dimension(mainWindow.getWidth(), 50));
		add(participants);
		participants.add(new JLabel("Participants file"));
		participants.add(Box.createHorizontalStrut(10));
		participantsFile = new JTextField();
		participants.add(participantsFile);
		JButton openParticipantsFile = new JButton("...");
		openParticipantsFile.setActionCommand(ActionCommands.OPEN_PARTICIPANTS_FILE);
		openParticipantsFile.addActionListener(this);
		participants.add(openParticipantsFile);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (ActionCommands.OPEN_PARTICIPANTS_FILE.equals(command)) {
			handleOpenParticipantsFile();
		} else if (ActionCommands.GENERATE_GRIDS.equals(command)) {
			handleGenerateGrids();
		} else if (ActionCommands.SAVE_GRIDS.equals(command)) {
			handleSaveGrids();
		} else if (ActionCommands.LOAD_GRIDS.equals(command)) {
			handleLoadGrids();
		} else if (ActionCommands.SELECT_EXCEL_OUTPUT_DIR.equals(command)) {
			handleSelectExcelOutputDir();
		} else if (ActionCommands.GENERATE_EXCELS.equals(command)) {
			handleGenerateExcels();
		} else if (ActionCommands.OPEN_EXCEL_LOCATION.equals(command)) {
			handleOpenLocation();
		} else {
			throw new RuntimeException("Cannot handle command " + command);
		}
	}

	private void handleOpenLocation() {
		File excelLocation = new File(excelDirectory.getText());
		if (excelLocation.exists()) {
			try {
				Desktop.getDesktop().open(excelLocation);
			} catch (IOException e1) {
				showError("Could not open folder: " + e1.getMessage());
			}
		}
	}

	private void handleGenerateExcels() {
		new Thread(new Runnable() {
			public void run() {
				File excelDir = new File(excelDirectory.getText());
				if (excelDir.exists()) {
					try {
						mainWindow.getController().generateExcels(excelDir);
						JOptionPane.showMessageDialog(DrawingsTab.this, "Successfully generated excel files",
								"Excel generation", JOptionPane.INFORMATION_MESSAGE);
					} catch (RuntimeException e) {
						showError("Failed to generate excels: " + e.getMessage());
					}
				} else {
					showError("Invalid output dir " + excelDirectory.getText());
				}
			}
		}).start();
	}

	private void handleSelectExcelOutputDir() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int status = fileChooser.showOpenDialog(this);
		if (status == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			excelDirectory.setText(selectedFile.getAbsolutePath());
		}
	}

	private void handleLoadGrids() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(getFileFilter());
		int fileChooserResult = fileChooser.showOpenDialog(this);
		if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				mainWindow.getController().loadGrids(selectedFile);
			} catch (FileNotFoundException e1) {
				showError("Failed to load grids");
			}
		}
	}

	private void handleSaveGrids() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(getFileFilter());
		int fileChooserResult = fileChooser.showSaveDialog(this);
		if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			boolean extensionMissing = !selectedFile.getName().endsWith(".grid");
			if (extensionMissing) {
				selectedFile = new File(selectedFile.getAbsolutePath() + ".grid");
			}
			try {
				mainWindow.getController().saveGrids(selectedFile);
			} catch (IOException e1) {
				showError("Failed to save grids");
			}
		}
	}

	private FileFilter getFileFilter() {
		return GCFileFilter.GRID.get();
	}

	private void handleGenerateGrids() {
		new Thread(new Runnable() {
			public void run() {
				String participantsFileText = participantsFile.getText();
				File inputFile = new File(participantsFileText);
				if (inputFile.exists()) {
					try {
						mainWindow.getController().generateGrids(inputFile);
					} catch (FileNotFoundException e) {
						showError("Failed to read participant file " + participantsFileText);
					} catch (IOException e) {
						showError("Failed to read participant file " + participantsFileText);
					} catch (RuntimeException e) {
						showError("Failed to calculate grids: " + e.getMessage());
						e.printStackTrace();
					}
				} else {
					showError("Invalid input file: " + participantsFileText);
				}
			}
		}).start();

	}

	private void showError(String message) {
		hideProcessing();
		JOptionPane.showMessageDialog(this, message, "Invalid data", JOptionPane.ERROR_MESSAGE);
	}

	private void handleOpenParticipantsFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(GCFileFilter.EXCEL.get());
		int status = fileChooser.showOpenDialog(this);
		if (status == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			participantsFile.setText(selectedFile.getAbsolutePath());
		}
	}

	public void update(MainModel observable, EventType eventType, Object object) {
		DefaultTableModel tableModel = (DefaultTableModel) gridTable.getModel();

		switch (eventType) {
		case CLEAR_GRIDS:
			rowData.clear();
			tableModel.fireTableStructureChanged();
			break;
		case ADD_GRID:
			Grid grid = (Grid) object;
			addGrid(grid);
			tableModel.fireTableRowsInserted(tableModel.getRowCount() - 1, tableModel.getRowCount() - 1);
			break;
		case ADD_ALL_GRIDS:
			@SuppressWarnings("unchecked")
			Iterable<Grid> grids = (Iterable<Grid>) object;
			for (Grid gridToAdd : grids) {
				addGrid(gridToAdd);
			}
			tableModel.fireTableRowsInserted(0, tableModel.getRowCount() - 1);
			break;
		case PROCESSING_STARTED:
			showProcessing();
			break;
		case PROCESSING_STOPPED:
			hideProcessing();
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void addGrid(Grid grid) {
		Group group = grid.getGroup();
		String groupName = GridCalculatorFormatter.formatForGroupSheetHeader(group.getFilterCriteria());
		Integer groupSize = group.getSize();
		rowData.add(new Vector<Object>(Arrays.asList(groupName, groupSize)));
	}

	private void showProcessing() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setIndeterminate(true);
			}
		});
	}

	private void hideProcessing() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setIndeterminate(false);
			}
		});
	}
}
