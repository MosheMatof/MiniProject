package renderer;

import java.util.List;

import geometries.Geometries;
import geometries.Intersectable.GeoPoint;
import primitives.*;
import scene.Scene;

/**
 * traces ray to find it's pixel color
 */
public class BasicRayTracer extends RayTracerBase {

	/**
	 * constructs a new BasicRayTracer with scene = 'scene'
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
		List<GeoPoint> points = scene.geometries.findGeoIntersections(ray);
		if (points == null) {
			return scene.background;
		} else {
			return calcColor(ray.findClosestGeoPoint(points));
		}
	}

	/**
	 * calculates the color for a given point
	 * @param point the point to find the color for
	 * @return the color of this point
	 */
	private Color calcColor(GeoPoint point, Ray ray) {
		return scene.ambientLight.getIntensity().add(point.geometry.getEmission());
	}
}
