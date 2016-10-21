package ch.danielsuter.gridcalculator.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.danielsuter.gridcalculator.TestBase;
import ch.danielsuter.gridcalculator.model.GridSize;
import ch.danielsuter.gridcalculator.model.Participant;
import ch.danielsuter.gridcalculator.util.MyLogger;
import org.apache.poi.util.IOUtils;
import org.junit.Test;

public class TemplateTest extends TestBase {
	private final static MyLogger logger = MyLogger.getLogger(TemplateTest.class);

	@Test
	public void testSmallDrawing() throws Exception {
		DrawingTemplate template = createTemplate(GridSize.SMALL);

		addTestData(template, GridSize.SMALL.getSize(), 1);

		writeTemplate(template, "small-drawing");
	}

	@Test
	public void testBigDrawing() throws Exception {
		DrawingTemplate template = createTemplate(GridSize.LARGE);

		addTestData(template, GridSize.LARGE.getSize(), 1);

		writeTemplate(template, "large-drawing");
	}

	@Test
	public void testNulls() throws IOException {
		DrawingTemplate template = createTemplate(GridSize.SMALL);

		addTestData(template, GridSize.SMALL.getSize(), 2);

		writeTemplate(template, "null-test");
	}

	private void addTestData(DrawingTemplate template, int numPositions, int stepSize) {
		template.setCategory("Testcategory A");
		for (int i = 0; i < numPositions; i += stepSize) {
			Participant participant = new Participant("Firstname " + i, "Lastname " + i, "Club " + i);
			template.setParticipant(i, participant, false);
		}
	}

	private DrawingTemplate createTemplate(GridSize gridSize) {
		TemplateResolver resolver = new TemplateResolver();
		DrawingTemplate template = resolver.getTemplate(gridSize);
		return template;
	}

	private void writeTemplate(DrawingTemplate template, String name) throws IOException {
		FileOutputStream outputStream = null;
		try {
			File outputFile = new File(BASE_PATH, name + ".xlsx");
			outputStream = new FileOutputStream(outputFile);
			template.getWorkbook().write(outputStream);
			logger.info("Wrote file to {0}", outputFile.getAbsolutePath());
		} finally {
			IOUtils.closeQuietly(outputStream);
		}

	}
}
