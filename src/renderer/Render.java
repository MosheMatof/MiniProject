package renderer;

import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;

/**
 * Rendering the image from the scene
 */
public class Render {
	private ImageWriter imageWriter;
	private Camera camera;
	private RayTracerBase rayTracer;

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
				Color color = rayTracer.traceRay(camera.constructRayThroughPixel(nX, nY, j, i));
				imageWriter.writePixel(j, i, color);
			}
		}
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
