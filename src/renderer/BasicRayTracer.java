package renderer;

import java.util.List;

import geometries.Geometries;
import primitives.*;
import scene.Scene;

/**
 * traces ray to find it's pixel color
 */
public class BasicRayTracer extends RayTracerBase {

	/**
	 * constructs a new BasicRayTracer with scene = 'scene'
	 * 
	 * @param scene the scene of the BasicRayTracer
	 */
	public BasicRayTracer(Scene scene) {
		super(scene);
	}

	@Override
	public Color traceRay(Ray ray) {
		if (scene.geometries == null) {
			return scene.background;
		}
		List<Point3D> points = scene.geometries.findIntersections(ray);
		if (points == null) {
			return scene.background;
		} else {
			return calcColor(ray.findClosestPoint(points));
		}
	}

	/**
	 * calculates the color for a given point
	 * 
	 * @param point the point to find the color for
	 * @return the color of this point
	 */
	private Color calcColor(Point3D point) {
		return scene.ambientLight.getIntensity();
	}
}
