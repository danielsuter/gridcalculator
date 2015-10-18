package ch.daniel.karateseon.gridcalculator.gui.tabs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ch.daniel.karateseon.gridcalculator.filter.FilterCriteria;
import ch.daniel.karateseon.gridcalculator.gui.ActionCommands;
import ch.daniel.karateseon.gridcalculator.model.Gender;

public class AddKumiteFilterDialog extends AbstractAddDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField fromWeightField;
	private JTextField toWeightField;
	private JTextField weightText;

	
	public AddKumiteFilterDialog(JFrame parent) {
		super(parent);
		applyUI();
	}
	
	private void applyUI() {
		applyWindowSettings();

		addGenderButtons();
		addYearFields();
		addWeightFields();
		addWeightStringField();
		addButtons();

		setVisible(true);
	}
	
	private void addWeightFields() {
		add(new JLabel("From weight"));
		fromWeightField = new JTextField();
		add(fromWeightField);
		add(new JLabel("To weight"));
		toWeightField = new JTextField();
		add(toWeightField);
	}
	
	private void addWeightStringField() {
		add(new JLabel("Weight text"));
		weightText = new JTextField();
		add(weightText);
	}



	private void addButtons() {
		JButton add = new JButton("Add");
		add.setActionCommand(ActionCommands.ADD_KUMITE_FILTER);
		add.addActionListener(this);
		add(add);
		JButton cancel = new JButton("Cancel");
		cancel.setActionCommand(ActionCommands.CANCEL_ADD_KUMITE_FILTER);
		cancel.addActionListener(this);
		add(cancel);
	}

	private void applyWindowSettings() {
		setLayout(new GridLayout(0, 2));
		setTitle("Add Kumite filter");
		setSize(200, 240);
	}
	

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (ActionCommands.ADD_KUMITE_FILTER.equals(command)) {
			boolean hasError = false;

			Gender gender = getGenderOrNull();
			if(gender == null) {
				hasError = true;
			}

			Integer fromYear = getFromYearOrNull();
			Integer toYear = getToYearOrNull();
			if (fromYear == null || toYear == null) {
				hasError = true;
			}

			Double fromWeight = getFromWeightOrNull();
			Double toWeight = getToWeightOrNull();
			if(fromWeight == null|| toWeight == null) {
				hasError = true;
			}
			
			String weightString = weightText.getText();
			if(weightString.trim().isEmpty()) {
				weightString = null;
			}
			
			if (!hasError) {
				filter = FilterCriteria.createKumite(gender, fromYear, toYear, fromWeight, toWeight, weightString);
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

	private Double getFromWeightOrNull() {
		return getDoubleOrNull(fromWeightField.getText());
	}
	
	private Double getToWeightOrNull() {
		return getDoubleOrNull(toWeightField.getText());
	}
}
