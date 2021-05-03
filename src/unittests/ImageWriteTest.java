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
		for (int i = 0; i < 500; i++) {
			for (int j = 0; j < 800; j++) {
				if (i%50 == 0 || j%50 == 0 ) {
					imgWriter.writePixel(j, i, new Color(255,0,0));				
				}
				else {
					imgWriter.writePixel(j, i, new Color(0,255,0));				
				}
			}
		}
		imgWriter.writeToImage();
	}

}
