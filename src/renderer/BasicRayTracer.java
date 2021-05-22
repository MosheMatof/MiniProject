package renderer;

import java.util.List;

import elements.LightSource;
import geometries.Intersectable.GeoPoint;
import primitives.*;
import static primitives.Util.*;
import scene.Scene;

/**
 * traces ray to find it's pixel color
 */
public class BasicRayTracer extends RayTracerBase {

	/**
	 * small number to move the start point of reflection's or transparency's ray
	 */
	private static final double DELTA = 0.1;
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
		List<GeoPoint> points = scene.geometries.findGeoIntersections(ray, Double.POSITIVE_INFINITY);
		if (points == null) {
			return scene.background;
		} else {
			return calcColor(ray.findClosestGeoPoint(points), ray);
		}
	}

	/**
	 * calculates the color for a given point
	 * 
	 * @param gPoint the point to find the color for
	 * @return the color of this point
	 */
	private Color calcColor(GeoPoint gPoint, Ray ray) {
		Color color = scene.ambientLight.getIntensity().add(gPoint.geometry.getEmission());
		color = color.add(calcLocalEffects(gPoint, ray));
		return color;
	}

	/**
	 * calculating the final color of the intersection point considering all the
	 * light source of the scene
	 * 
	 * @param intersection the intersection point
	 * @param ray          the ray of the intersection
	 * @return the final color of the intersection point considering all the light
	 *         source of the scene
	 */
	private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
		Vector v = ray.getDir();
		Vector n = intersection.getNormal();
		double nv = alignZero(n.dotProduct(v));
		if (nv == 0)
			return Color.BLACK;
		int nShininess = intersection.geometry.getMaterial().nShinines;
		double kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;
		Color color = Color.BLACK;

		for (LightSource lightSource : scene.lights) {
			Vector l = lightSource.getL(intersection.point);
			double nl = alignZero(n.dotProduct(l));

			if (nl * nv > 0 && unshaded(l, n, intersection, lightSource)) { // sign(nl) == sing(nv)
				Color lightIntensity = lightSource.getIntensity(intersection.point);
				color = color.add(calcDiffusive(kd, l, n, lightIntensity),
						calcSpecular(ks, l, n, v, nShininess, lightIntensity));
			}
		}
		return color;
	}

	/**
	 * calculate the specular component of the final color
	 * 
	 * @param ks             specular
	 * @param l              the normalized vector from the light source to the
	 *                       intersection point
	 * @param n              the normal to the geometry at the intersection point
	 * @param v              the normalized vector from the camera to the
	 *                       intersection point
	 * @param nShininess     the shininess level of the geometry
	 * @param lightIntensity the intensity of the light
	 * @return the specular component of the final color
	 */
	private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
		Vector r = l.subtract(n.scale(2 * n.dotProduct(l)));
		double vr = alignZero(v.dotProduct(r));
		if (vr >= 0)
			return Color.BLACK;
		double scale = ks * Math.pow(-vr, nShininess);
		return lightIntensity.scale(scale);
	}

	/**
	 * calculate the diffuse component of the final color
	 * 
	 * @param kd             diffuse
	 * @param l              the normalized vector from the light source to the
	 *                       intersection point
	 * @param n              the normal to the geometry at the intersection point
	 * @param lightIntensity the intensity of the light
	 * @return the diffuse component of the final color
	 */
	private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity) {
		double scale = l.dotProduct(n) * kd;
		if (scale < 0)
			scale = -scale;
		return lightIntensity.scale(scale);
	}
	
	/**
	 * checks if the light from a light source arrives to this point
	 * @param l the direction from the light source to the intersection point
	 * @param n the normal of this GeoPoint
	 * @param gp the GeoPoint to check for shadows
	 * @return true if there wasn't any intersections with (gp to light)'s ray, otherwise false 
	 */
	private boolean unshaded(Vector l, Vector n, GeoPoint gp, LightSource lc) {
		Vector lightDirection = l.scale(-1);  // from point to light source
		Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : - DELTA);  // build the delta vector
		Point3D point = gp.point.add(delta);  // move the start point of the (gp to light)'s ray by the delta vector
		Ray lightRay = new Ray(point, lightDirection);  // build the (gp to light)'s ray
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, lc.getDistance(point));  // search for intersections with this ray
		return intersections == null;  // return if there wasn't any intersections with (gp to light)'s ray
	}
}
