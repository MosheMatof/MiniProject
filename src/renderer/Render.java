package renderer;

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

	public Render setBasicRayTracer(RayTracerBase basicRayTracer) {
		this.basicRayTracer = basicRayTracer;
		return this;
	}

	public void renderImage() {
		int nX = imageWriter.getNx();
		int nY = imageWriter.getNy();
		for (int i = 0; i < imageWriter.getNx(); i++) {
			for (int j = 0; j < imageWriter.getNy(); j++) {
				Color color = basicRayTracer.traceRay(camera.constructRayThroughPixel(nX, nY, j, i));
				imageWriter.writePixel(j, i, color);
			}
		}
	}

	public void printGrid(int i, Color color) {
		// TODO Auto-generated method stub
		
	}

	public void writeToImage() {
		// TODO Auto-generated method stub
		
	}

}
