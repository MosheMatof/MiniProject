package renderer;

import java.util.MissingResourceException;

import elements.Camera;
import primitives.Color;
import scene.Scene;

public class Render {
	ImageWriter imageWriter;
	Scene scene;
	Camera camera;
	RayTracerBase basicRayTracer;

	public Render setImageWriter(ImageWriter imageWriter) {
		this.imageWriter = imageWriter;
		return this;
	}

	public Render setScene(Scene scene) {
		this.scene = scene;
		return this;
	}

	public Render setCamera(Camera camera) {
		this.camera = camera;
		return this;
	}

	public Render setRayTracer(RayTracerBase basicRayTracer) {
		this.basicRayTracer = basicRayTracer;
		return this;
	}

	public void renderImage() {
		if (imageWriter == null ) 
			throw new MissingResourceException("imageWriter is null ","ImageWriter","");
		if (basicRayTracer == null )
			throw new MissingResourceException("basicRayTracer is null ","BasicRayTracer","");
		if (camera == null ) 
			throw new MissingResourceException("camera is null ","Camera","");
		if (scene == null )
			throw new MissingResourceException("scene is null ","Scene","");

		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		for (int i = 0; i < imageWriter.getNy(); i++) {
			for (int j = 0; j < imageWriter.getNx(); j++) {
				Color color = basicRayTracer.traceRay(camera.constructRayThroughPixel(nX, nY, j, i));
				imageWriter.writePixel(j, i, color);
				writeToImage();
			}
		}
	}

	public void printGrid(int interval, Color color) {
		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		for (int i = 0; i < nY; i++) {
			for (int j = 0; j < nX; j++) {
				if (i % interval == 0 || j % interval == 0) {
					imageWriter.writePixel(j, i, new Color(255, 0, 0));
				}
			}
		}
	}

	public void writeToImage() {
		if (imageWriter == null ) 
			throw new MissingResourceException("imageWriter is null ","ImageWriter","");
		imageWriter.writeToImage();
	}

}
