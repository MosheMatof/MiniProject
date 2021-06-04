package renderer;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;
import primitives.Ray;

/**
 * Rendering the image from the scene
 */
public class Render {
	private static final double MAX_VARIANCE = 2;
	private ImageWriter imageWriter;
	private Camera camera;
	private RayTracerBase rayTracer;
	private int kA = 5;
	/**
	 * set the imageWriter of the render
	 * @param imageWriter the imageWriter for the render
	 * @return instance of this scene
	 */
	public Render setImageWriter(ImageWriter imageWriter) {
		this.imageWriter = imageWriter;
		return this;
	}

	/**
	 * set the camera of the render
	 * @param camera the camera for the render
	 * @return instance of this scene
	 */
	public Render setCamera(Camera camera) {
		this.camera = camera;
		return this;
	}

	/**
	 * set the rayTracer of the render
	 * @param rayTracer the rayTracer for the render
	 * @return instance of this scene
	 */
	public Render setRayTracer(RayTracerBase rayTracer) {
		this.rayTracer = rayTracer;
		return this;
	}

	/**
	 * setter for the antialising factor.
	 *  the number determine the dimension of the sample rays metrix in each pixel (kA*kA)
	 * @param kA the antialising factor
	 * @return instance of this scene
	 */
	public Render setKA(int kA) {
		this.kA = kA;
		return this;
	}

	/** 
	 * Rendering the image by imageWriter according to the rayTracer and camera 
	 * @param rays the number of rays per pixel
	 */
	public void renderImage() {
		var render = "Render";
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null ", render, "imageWriter");
		if (rayTracer == null)
			throw new MissingResourceException("rayTracer is null ", render, "rayTracer");
		if (camera == null)
			throw new MissingResourceException("camera is null ", render, "camera");

		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		BlackBoard sampleBoard = BlackBoard.sempleSquare(1);
		BlackBoard randomBoard = BlackBoard.squareRandom(1, kA);
		if (kA > 4) {
			for (int i = 0; i < nY; i++) {
				for (int j = 0; j < nX; j++) {
					//get sample of 5 colors
					List<Ray> sampleRays = camera.constructBeamThroughPixel(sampleBoard, nX, nY, i, j);
					List<Color> sampleColors = new LinkedList<>();
					for (Ray ray : sampleRays) {
						sampleColors.add(rayTracer.traceRay(ray));
					}
					Color sampleAvg = new Color(sampleColors);

					if (calcVarianceColors(sampleColors, sampleAvg) < MAX_VARIANCE) {
						imageWriter.writePixel(j, i, sampleAvg);
					} else {
						List<Ray> rays = camera.constructBeamThroughPixel(randomBoard, nX, nY, i, j);
						List<Color> colors = new LinkedList<>();
						for (Ray ray : rays) {
							colors.add(rayTracer.traceRay(ray));
						}
						colors.addAll(sampleColors);
						Color AvgColor = new Color(colors);
					}			
				}
			}
		}
		else{
			for (int i = 0; i < nY; i++) {
				for (int j = 0; j < nX; j++) {
					if (j == 90 && i == 120) {
						int a = 1;
					}
					List<Ray> rays = camera.constructBeamThroughPixel(randomBoard, nX, nY, i, j);
					List<Color> colors = new LinkedList<>();
					for (Ray ray : rays) {
						colors.add(rayTracer.traceRay(ray));
					}
					Color AvgColor = new Color(colors);
					
				}
			}
		}		
	}

	
	
//	/**
//	 * creates image by only 1 ray from each pixel
//	 */
//	private void renderImageByRandomRays(int i, int j, Color average) {
//		List<Ray> randomRays = camera.constructRaysThroughPixel(imageWriter.getNx(), imageWriter.getNy(), j, i, kA);
//		List<Color> colors = new LinkedList<>();
//		if(average != null)
//			colors.add(average);
//		for (Ray ray : randomRays) {
//			colors.add(rayTracer.traceRay(ray));
//		}
//		Color averageColor = new Color(colors);
//		imageWriter.writePixel(j, i, averageColor);
//	}

	// /**
	//  * calculate the average color from all the colors in the list 'colors'
	//  * @param colors the list of colors to find the average from
	//  * @return the average color from all the colors in the list 'colors'
	//  */
	// private Color averageColor(List<Color> colors) {
	// 	Color sumColors = Color.BLACK;
	// 	for (Color color : colors) {
	// 		sumColors = sumColors.add(new Color(color.getColor()));
	// 	}
	// 	return sumColors.reduce(colors.size());
	// }
	/**
	 * calculates the variance of list of color
	 * @param colors list of colors
	 * @param average the averade color
	 * @return the variance 
	 */
	private double calcVarianceColors(List<Color> colors, Color average) {
		int rAvg = average.getColor().getRed();
		int gAvg = average.getColor().getGreen();
		int bAvg = average.getColor().getBlue();
		double r = 0,g = 0,b = 0;
		for (Color color : colors) {
			r += Math.abs(color.getColor().getRed() - rAvg);
			g += Math.abs(color.getColor().getGreen() - gAvg);
			b += Math.abs(color.getColor().getBlue() - bAvg);
		}
		return (r+g+b)/colors.size();
	}

	/**
	 * prints a grid on the image. the spaces between the lines is the size of the "interval"
	 * @param interval the space size between the lines
	 * @param color the color of the grid
	 */
	public void printGrid(int interval, Color color) {
		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		// paint the vertical lines
		for (int i = 0; i < nY; i += interval) {
			for (int j = 0; j < nX; j++) {
				imageWriter.writePixel(i, j, color);
			}
		}
		// paint the horizontal lines
		for (int j = 0; j < nY; j += interval) {
			for (int i = 0; i < nX; i++) {
				imageWriter.writePixel(i, j, color);
			}
		}
	}

	/**
	 * Generates the image
	 */
	public void writeToImage() {
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null ", "Render", "imageWriter");
		imageWriter.writeToImage();
	}

}
