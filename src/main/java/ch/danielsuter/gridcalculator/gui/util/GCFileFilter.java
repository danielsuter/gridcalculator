package ch.danielsuter.gridcalculator.gui.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public enum GCFileFilter {
	FILTER("Filter files", ".filter"),
	GRID("Grid files", ".grid"),
	EXCEL("Excel files", ".xlsx");

	private String description;
	private String extension;

	private GCFileFilter(String description, String extension) {
		this.description = description;
		this.extension = extension;
	}

	public FileFilter get() {
		return new FileFilter() {

			@Override
			public String getDescription() {
				return String.format("%s (*%s)", description, extension);
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				if (f.getName().endsWith(extension)) {
					return true;
				}

				return false;
			}
		};
	}

	public String getExtension() {
		return null;
	}

	public String getDescription() {
		return null;
	}
}
