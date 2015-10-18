package ch.daniel.karateseon.gridcalculator.gui.tabs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import ch.daniel.karateseon.gridcalculator.gui.ActionCommands;
import ch.daniel.karateseon.gridcalculator.gui.MainWindow;

public class WelcomeTab extends JPanel {

	private static final long serialVersionUID = 1L;
	private ActionListener listener;

	public WelcomeTab(MainWindow mainWindow) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(Box.createRigidArea(new Dimension(0, 100)));
		
		JButton buttonReadParticipants = new JButton("Load participants list");
		addButton(buttonReadParticipants, ActionCommands.LOAD_PARTICIPANTS);

		JButton buttonReadGrids = new JButton("Load grids");
		addButton(buttonReadGrids, ActionCommands.LOAD_GRIDS);

		JButton buttonReadFilters = new JButton("Load filters");
		addButton(buttonReadFilters, ActionCommands.LOAD_FILTERS);
	}

	private void addButton(JButton buttonToAdd, String actionCommand) {
		buttonToAdd.setActionCommand(actionCommand);
		buttonToAdd.addActionListener(listener);
		buttonToAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonToAdd.setMaximumSize(new Dimension(500, 50));
		buttonToAdd.setFont(new Font("Verdana", Font.BOLD, 16));
		add(buttonToAdd);
		add(Box.createRigidArea(new Dimension(0, 20)));
	}
}
