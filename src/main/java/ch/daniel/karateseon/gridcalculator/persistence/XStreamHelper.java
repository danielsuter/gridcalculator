package ch.daniel.karateseon.gridcalculator.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamHelper {
	private XStream xStream = new XStream(new DomDriver());

	public void save(File destination, Object objectToSave) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(destination);
			xStream.toXML(objectToSave, writer);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	public Object load(File input) throws FileNotFoundException {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(input);
			Object deserializedObject = xStream.fromXML(fileInputStream);
			return deserializedObject;
		} finally {
			IOUtils.closeQuietly(fileInputStream);
		}
	}
}
