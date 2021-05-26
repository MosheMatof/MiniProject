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
	private int RaysPerPixel = 1;

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
	 * Rendering the image by imageWriter according to the rayTracer and camera 
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
		for (int i = 0; i < nY; i++) {
			for (int j = 0; j < nX; j++) {
				List<Ray> Rays = camera.constructRayThroughPixel(nX, nY, j, i, RaysPerPixel);
				List<Color> colors = new LinkedList<Color>();
				for (Ray ray : Rays) {
					colors.add(rayTracer.traceRay(ray));
				}
				Color color = averageColor(colors);
				imageWriter.writePixel(j, i, color);
			}
		}
	}

	/**
	 * calculate the average color from all the colors in the list 'colors'
	 * @param colors the list of colors to find the average from
	 * @return the average color from all the colors in the list 'colors'
	 */
	private Color averageColor(List<Color> colors) {
		Color sumColors = Color.BLACK;
		for (Color color : colors) {
			sumColors = sumColors.add(color);
		}
		return sumColors.reduce(colors.size());
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
	
	/**
	 * setter for RaysPerPixel
	 * @param raysPerPixel the raysPerPixel to set
	 * @return it self
	 */
	public Render setRaysPerPixel(int raysPerPixel) {
		RaysPerPixel = raysPerPixel;
		return this;
	}

}
