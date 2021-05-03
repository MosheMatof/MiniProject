package renderer;

import primitives.*;
import scene.*;

public abstract class RayTracerBase {
	protected Scene scene;

	public RayTracerBase(Scene scene) {
		this.scene = scene;
	}
	
	public abstract Color traceRay(Ray ray);
}
