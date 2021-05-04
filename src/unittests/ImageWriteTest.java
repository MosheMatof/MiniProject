package unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import primitives.Color;
import renderer.ImageWriter;

/**
 * 
 * test class for {@link renderer.ImageWriter}}
 */
public class ImageWriteTest {

	/**
	 * exercising the use of the {@link renderer.ImageWriter#writePixel(int, int, Color)}}
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
