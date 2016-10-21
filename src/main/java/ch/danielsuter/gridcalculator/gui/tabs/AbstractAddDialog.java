package ch.danielsuter.gridcalculator.gui.tabs;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ch.danielsuter.gridcalculator.filter.FilterCriteria;
import ch.danielsuter.gridcalculator.model.Gender;

public abstract class AbstractAddDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	protected OperationState operationState;
	protected FilterCriteria filter;
	protected JRadioButton maleOption;
	protected JRadioButton femaleOption;
	protected JTextField fromYearField;
	protected JTextField toYearField;
	
	public AbstractAddDialog(JFrame parent) {
		super(parent, true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(parent);
	}


	protected void addYearFields() {
		add(new JLabel("From year"));
		fromYearField = new JTextField();
		add(fromYearField);
		add(new JLabel("To year"));
		toYearField = new JTextField();
		add(toYearField);
	}

	protected void addGenderButtons() {
		ButtonGroup genderGroup = new ButtonGroup();
		add(new JLabel("Gender"));
		maleOption = new JRadioButton("Male");
		genderGroup.add(maleOption);
		add(maleOption);
		add(new Box(BoxLayout.LINE_AXIS));
		femaleOption = new JRadioButton("Female");
		genderGroup.add(femaleOption);
		add(femaleOption);
	}

	protected Integer getNumberOrNull(String numberString) {
		try {
			int number = Integer.parseInt(numberString);
			return number;
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
	protected Double getDoubleOrNull(String numberString) {
		try {
			double number = Double.parseDouble(numberString);
			return number;
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public FilterCriteria getFilter() {
		return filter;
	}

	public OperationState getOperationState() {
		return operationState;
	}
	
	protected Gender getGenderOrNull() {
		if (maleOption.isSelected()) {
			return Gender.MALE;
		} else if (femaleOption.isSelected()) {
			return Gender.FEMALE;
		} else {
			return null;
		}
	}
	
	protected Integer getFromYearOrNull() {
		return getNumberOrNull(fromYearField.getText());
	}
	
	protected Integer getToYearOrNull() {
		return getNumberOrNull(toYearField.getText());
	}
}
