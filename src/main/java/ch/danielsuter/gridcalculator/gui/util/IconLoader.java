package ch.danielsuter.gridcalculator.gui.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconLoader {
	public ImageIcon load(String fileName) {
		try {
			BufferedImage image = ImageIO.read(getClass().getResource("/icons/add.png"));
			return new ImageIcon(image);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to read image: " + fileName);
		}
	}
}
