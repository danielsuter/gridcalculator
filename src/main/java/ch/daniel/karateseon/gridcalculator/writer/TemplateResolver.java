package ch.daniel.karateseon.gridcalculator.writer;

import java.io.File;

import ch.daniel.karateseon.gridcalculator.model.GridSize;

public class TemplateResolver {
	private final static String BASE_PATH = "target/classes/";

	public DrawingTemplate getTemplate(GridSize gridSize) {
		switch (gridSize) {
		case TINY:
			return new TinyTemplate(new File(BASE_PATH, "auslosungsformular_winzig.xlsx"));
		case SMALL:
			return new SmallTemplate(new File(BASE_PATH, "Auslosungsformular_klein.xlsx"));
		case LARGE:
			return new LargeTemplate(new File(BASE_PATH, "Auslosungsformular_gross.xlsx"));
		default:
			throw new RuntimeException("Cannot handle template size " + gridSize);
		}

	}

	public GroupSheetTemplate getGroupSheetTemplate() {
		return new GroupSheetTemplate(new File(BASE_PATH, "Gruppenformular.xlsx"));
	}
}
