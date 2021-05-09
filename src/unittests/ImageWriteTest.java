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
	 * exercising the use of the
	 * {@link renderer.ImageWriter#writePixel(int, int, Color)}}
	 */
	@Test
	public void WriteImageTest() {
		ImageWriter imgWriter = new ImageWriter("image writer try", 800, 500);
		Color color1 = new Color(255, 0, 0);
		Color color2 = new Color(0, 255, 0);
//		for (int i = 0; i < 500; i++) {
//			for (int j = 0; j < 800; j++) {
//				if (i % 50 == 0 || j % 50 == 0) {
//					imgWriter.writePixel(j, i, color1);
//				} else {
//					imgWriter.writePixel(j, i, color2);
//				}
//			}
//		}
		double intervalI = 0;
		double intervalJ = 0;
		for (int i = 0; i < 500; i++) {
			if (i == intervalI) {
				for (int j = 0; j < 800; j++) {
					imgWriter.writePixel(j, i, color1);
				}
				intervalI += 50;
			}
			else {
				for (int j = 0; j < 800; j++) {
					if (j == intervalJ) {
						imgWriter.writePixel(j, i, color1);
						intervalJ += 50;
					}
					else {
						imgWriter.writePixel(j, i, color2);
					}
				}
			}
		}
	}
}
