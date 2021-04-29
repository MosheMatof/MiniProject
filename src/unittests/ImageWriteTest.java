package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import primitives.Color;
import renderer.ImageWriter;

/**
 * 
 * @author erenb
 *
 */
public class ImageWriteTest {

	/**
	 * 
	 */
	@Test
	public void WriteImageTest() {
		ImageWriter imgWriter = new ImageWriter("image writer try", 800, 500);
		// create picture
		// ... imgWriter.writePixel(x-index, y-index, pixel-color);
		imgWriter.writePixel(200, 200, new Color(255,0,0));
		imgWriter.writePixel(400, 400, new Color(255,255,255));
		imgWriter.writeToImage();
	}

}
