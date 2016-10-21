package ch.danielsuter.gridcalculator.gui.util;

import java.util.Vector;

import javax.swing.JTable;

public class JTableHelper {
	public static JTable createNonEditable(Vector<Vector<Object>> data, Vector<String> columns) {
		return new JTable(data, columns) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}
}
