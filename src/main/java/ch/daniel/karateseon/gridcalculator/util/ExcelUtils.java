package ch.daniel.karateseon.gridcalculator.util;

import org.apache.poi.ss.usermodel.Cell;

public class ExcelUtils {
	public static String getStringCellValue(Cell cell) {
		if (cell != null) {
			return cell.getStringCellValue();
		}
		return null;
	}
}
