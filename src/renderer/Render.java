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
	private ImageWriter imageWriter;
	private Camera camera;
	private RayTracerBase rayTracer;
	private int kA = 0;
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

		if (kA < 1) {
			createsSimpleImage();
			return;
		}

		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();

		for (int i = 0; i < nY; i++) {
			for (int j = 0; j < nX; j++) {
				//get sample of 5 colors
				List<Ray> sampleRays = camera.constructRayThroughPixel(nX, nY, j, i, 1);
				List<Color> sampleColors = new LinkedList<>();
				for (Ray ray : sampleRays) {
					sampleColors.add(rayTracer.traceRay(ray));
				}
				Color sampleAvg = new Color(sampleColors);

				if (calcVarianceColors(sampleColors, sampleAvg) < 2) {
					imageWriter.writePixel(j, i, sampleAvg);
				} else {
					List<Ray> randomRays = camera.constructRayThroughPixel(nX, nY, j, i, kA);
					List<Color> colors = sampleColors;
					for (Ray ray : randomRays) {
						colors.add(rayTracer.traceRay(ray));
					}
					Color averageColor = new Color(colors);
					imageWriter.writePixel(j, i, averageColor);
				}			
			}
		}
	}

	/**
	 * creates image by only 1 ray from each pixel
	 */
	private void createsSimpleImage() {
		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		for (int i = 0; i < nY; i++) {
			for (int j = 0; j < nX; j++) {
				Ray ray = camera.constructRayThroughPixel(nX, nY, j, i);
				Color color = rayTracer.traceRay(ray);
				imageWriter.writePixel(j, i, color);
			}
		}
	}

	// /**
	//  * calculate the average color from all the colors in the list 'colors'
	//  * @param colors the list of colors to find the average from
	//  * @return the average color from all the colors in the list 'colors'
	//  */
	// private Color averageColor(List<Color> colors) {
	// 	Color sumColors = Color.BLACK;
	// 	for (Color color : colors) {
	// 		//sumColors = sumColors.add(new Color(color.getColor()));
	// 		sumColors = sumColors.add(color);
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
