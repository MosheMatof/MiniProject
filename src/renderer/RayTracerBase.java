package renderer;

import primitives.*;
import scene.*;
/**
 *  Responsible to trace the ray in the scene, and find it's intersections points and their colors
 */
public abstract class RayTracerBase {
	protected Scene scene;

	/**
	 * constructs the scene for RayTracerBase
	 * @param scene the scene for RayTracerBase
	 */
	public RayTracerBase(Scene scene) {
		this.scene = scene;
	}
	/**
	 * find the closet intersection point and it's color
	 * @param ray the ray to trace
	 * @return the color of the closet intersection point
	 */
	public abstract Color traceRay(Ray ray);
}
