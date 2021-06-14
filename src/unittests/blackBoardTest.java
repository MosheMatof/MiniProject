/**
 * 
 */
package unittests;

import static org.junit.Assert.*;
import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

import org.junit.Test;

import renderer.BlackBoard;
import renderer.ImageWriter;


public class blackBoardTest {

	@Test
	public void test() {
		BlackBoard bb = BlackBoard.squareRandom(100, 1);//new BlackBoard(new Point2D(-1,-1), new Point2D(1,1), new Point2D(1,-1), new Point2D(-1,1));
		ImageWriter imageWriter = new ImageWriter("Black Board Test", 1000, 1000);
		double width = 450;
		double height = 103;
		bb.setHeight(height).setWidth(width);
		for(int i = (int) (500 - width); i < 500 + width; i++) {
			for (int j = (int)(500 - height); j < 500 + height; j++) {
				imageWriter.writePixel(i, j, new Color(0, 0, 256));
			}
		}
		for(Point3D p : bb.generate3dPoints(new Vector(0, 1, 0), new Vector(1, 0, 0), new Point3D(0,0,0))) {
			imageWriter.writePixel((int)p.getX() + 500, (int)p.getY() + 500, new Color(255, 0, 0));
		}
		imageWriter.writeToImage();
	}

}
