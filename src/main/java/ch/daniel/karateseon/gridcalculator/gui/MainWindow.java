package ch.daniel.karateseon.gridcalculator.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import ch.daniel.karateseon.gridcalculator.gui.tabs.DrawingsTab;
import ch.daniel.karateseon.gridcalculator.gui.tabs.FilterTab;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = -8511788152763206384L;
	private MainController controller;
	private MainModel model;

	public MainWindow(MainController controller, MainModel model) {
		this.controller = controller;
		this.model = model;
		applyLookAndFell();
		applyWindowSettings();
		addTabs();
		setVisible(true);
	}

	private void applyLookAndFell() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Use default
		}
	}

	private void addTabs() {
		JTabbedPane tabbedPane = new JTabbedPane();
//		tabbedPane.addTab("Welcome", new WelcomeTab(controller));
		tabbedPane.addTab("Group filters", new FilterTab(this, model));
		tabbedPane.addTab("Drawings", new DrawingsTab(this, model));
		
		add(tabbedPane);
	}

	private void applyWindowSettings() {
		setTitle("Drawing Calculator");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public MainController getController() {
		return controller;
	}
}
