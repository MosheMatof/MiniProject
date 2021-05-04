package renderer;

import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;
import scene.Scene;

/**
 * Rendering the image from the scene
 */
public class Render {
	ImageWriter imageWriter;
	Scene scene;
	Camera camera;
	RayTracerBase rayTracer;

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
	 * set the scene of the render
	 * @param scene the scene for the render
	 * @return instance of this scene
	 */
	public Render setScene(Scene scene) {
		this.scene = scene;
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
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null ", "Render", "imageWriter");
		if (rayTracer == null)
			throw new MissingResourceException("rayTracer is null ", "Render", "rayTracer");
		if (camera == null)
			throw new MissingResourceException("camera is null ", "Render", "camera");
		if (scene == null)
			throw new MissingResourceException("scene is null ", "Render", "scene");

		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		for (int i = 0; i < imageWriter.getNy(); i++) {
			for (int j = 0; j < imageWriter.getNx(); j++) {
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
				imageWriter.writePixel(i, j, new Color(255, 0, 0));
			}
		}
		// paint the horizontal lines
		for (int j = 0; j < nY; j += interval) {
			for (int i = 0; i < nX; i++) {
				imageWriter.writePixel(i, j, new Color(255, 0, 0));
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
