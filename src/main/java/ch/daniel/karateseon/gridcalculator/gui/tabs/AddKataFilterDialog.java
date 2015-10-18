package ch.daniel.karateseon.gridcalculator.gui.tabs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.gui.ActionCommands;
import ch.daniel.karateseon.gridcalculator.model.Gender;
import ch.daniel.karateseon.gridcalculator.model.Level;

public class AddKataFilterDialog extends AbstractAddDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JRadioButton upperstageOption;
	private JRadioButton lowerStageOption;

	public AddKataFilterDialog(JFrame parent) {
		super(parent);
		applyUI();
	}

	private void applyUI() {
		applyWindowSettings();

		addGenderButtons();
		addYearFields();
		addLevelButtons();
		addButtons();

		setVisible(true);
	}

	private void addLevelButtons() {
		add(new JLabel("Level"));
		ButtonGroup levelGroup = new ButtonGroup();
		upperstageOption = new JRadioButton("Upper stage");
		levelGroup.add(upperstageOption);
		add(upperstageOption);
		add(new Box(BoxLayout.LINE_AXIS));
		lowerStageOption = new JRadioButton("Lower stage");
		levelGroup.add(lowerStageOption);
		add(lowerStageOption);
	}

	private void applyWindowSettings() {
		setLayout(new GridLayout(0, 2));
		setTitle("Add Kata filter");
		setSize(200, 200);
	}

	private void addButtons() {
		JButton add = new JButton("Add");
		add.setActionCommand(ActionCommands.ADD_KATA_FILTER);
		add.addActionListener(this);
		add(add);
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand(ActionCommands.CANCEL_ADD_KATA_FILTER);
		cancel.addActionListener(this);
		add(cancel);
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (ActionCommands.ADD_KATA_FILTER.equals(command)) {
			boolean hasError = false;

			Gender gender = getGenderOrNull();

			Integer fromYear = getFromYearOrNull();
			Integer toYear = getToYearOrNull();
			if (fromYear == null || toYear == null) {
				hasError = true;
			}

			Level level = null;
			if (upperstageOption.isSelected()) {
				level = Level.UPPER_STAGE;
			} else if (lowerStageOption.isSelected()) {
				level = Level.LOWER_STAGE;
			} else {
				hasError = true;
			}

			if (!hasError) {
				filter = FilterCriteria.createKata(gender, fromYear, toYear, level);
				operationState = OperationState.ADD;
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid data", "Validation error", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			operationState = OperationState.CANCEL;
			dispose();
		}
	}
}
