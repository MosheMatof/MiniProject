package renderer;

import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;
import scene.Scene;

/**
 * 
 *
 */
public class Render {
	ImageWriter imageWriter;
	Scene scene;
	Camera camera;
	RayTracerBase basicRayTracer;

	/**
	 * 
	 * @param imageWriter
	 * @return
	 */
	public Render setImageWriter(ImageWriter imageWriter) {
		this.imageWriter = imageWriter;
		return this;
	}

	/**
	 * 
	 * @param scene
	 * @return
	 */
	public Render setScene(Scene scene) {
		this.scene = scene;
		return this;
	}

	/**
	 * 
	 * @param camera
	 * @return
	 */
	public Render setCamera(Camera camera) {
		this.camera = camera;
		return this;
	}

	/**
	 * 
	 * @param basicRayTracer
	 * @return
	 */
	public Render setRayTracer(RayTracerBase basicRayTracer) {
		this.basicRayTracer = basicRayTracer;
		return this;
	}

	/**
	 * 
	 */
	public void renderImage() {
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null ", "Render", "imageWriter");
		if (basicRayTracer == null)
			throw new MissingResourceException("basicRayTracer is null ", "Render", "basicRayTracer");
		if (camera == null)
			throw new MissingResourceException("camera is null ", "Render", "camera");
		if (scene == null)
			throw new MissingResourceException("scene is null ", "Render", "scene");

		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		for (int i = 0; i < imageWriter.getNy(); i++) {
			for (int j = 0; j < imageWriter.getNx(); j++) {
				Color color = basicRayTracer.traceRay(camera.constructRayThroughPixel(nX, nY, j, i));
				imageWriter.writePixel(j, i, color);
			}
		}
	}

	/**
	 * 
	 * @param interval
	 * @param color
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
	 * 
	 */
	public void writeToImage() {
		if (imageWriter == null)
			throw new MissingResourceException("imageWriter is null ", "ImageWriter", "");
		imageWriter.writeToImage();
	}

}
