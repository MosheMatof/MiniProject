package renderer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
	 * the initial value of the
	 */
	private static final double INITIAL_K = 1.0;
	/**
	 * 
	 */
	private static final int MAX_CALC_COLOR_LEVEL = 4;
	/**
	 * 
	 */
	protected static final double MIN_CALC_COLOR_K = 0.001;

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
		GeoPoint closestPoint = findClosestIntersection(ray);
		if (closestPoint == null)
			return scene.background;

		return calcColor(closestPoint, ray);
	}

	/**
	 * calculates the color for a given point
	 * 
	 * @param intersection the point to find the color for
	 * @param ray          the ray from the camera
	 * @return the color of this point
	 */
	private Color calcColor(GeoPoint intersection, Ray ray) {
		return calcColor(intersection, ray.getDir(), MAX_CALC_COLOR_LEVEL, INITIAL_K)
				.add(scene.ambientLight.getIntensity());
	}

	/**
	 * calculates the final color for a given point with all the factors
	 * 
	 * @param intersection the point to find the color for
	 * @param v            the original ray direction
	 * @param level        the level of the recursion tree (the number of times we
	 *                     produce refracted/reflection ray)
	 * @param k            the minimum value of the color that we add by calculating
	 *                     the transparency/reflection effect
	 * @return the final color of this point
	 */
	private Color calcColor(GeoPoint intersection, Vector v, int level, double k) {
		Color color = intersection.geometry.getEmission();
		color = color.add(calcLocalEffects(intersection, v, k));
		return 1 == level ? color : color.add(calcGlobalEffects(intersection, v, level, k));
	}

	/**
	 * calculating the color of the intersection point considering all the light
	 * source of the scene
	 * 
	 * @param intersection the intersection point
	 * @param v            the ray of the intersection
	 * @param k            the minimum value of the color that we add by calculating
	 *                     the transparency effect
	 * @return the final color of the intersection point considering all the light
	 *         source of the scene
	 */
	private Color calcLocalEffects(GeoPoint intersection, Vector v, double k) {
		Vector n = intersection.getNormal();
		if (intersection.geometry.getMaterial().isSnow)
			n = snowEffect(n);
		double nv = alignZero(n.dotProduct(v));
		if (nv == 0)
			return Color.BLACK;
		int nShininess = intersection.geometry.getMaterial().nShinines;
		double kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;
		Color color = Color.BLACK;

		for (LightSource lightSource : scene.lights) {
			Point3D interP = intersection.point;
			Vector l = lightSource.getL(interP);
//			double nl = alignZero(n.dotProduct(l));
//			if (nl * nv > 0) {
			double kTvalue = transparency(lightSource, intersection, l, n, nv);
			if (kTvalue * k > MIN_CALC_COLOR_K) {
				Color lightIntensity = lightSource.getIntensity(interP).scale(kTvalue);
				color = color.add(calcDiffusive(kd, l, n, lightIntensity),
						calcSpecular(ks, l, n, v, nShininess, lightIntensity));
			}
//			}
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
		double nl = alignZero(n.dotProduct(l));
		Vector r = nl == 0? l.subtract(n) : l.subtract(n.scale(2 * n.dotProduct(l)));
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
	 * calculates the sum value of the light ->
	 * <h1>possibilities values:</h1>
	 * <ul>
	 * <li>1 - if there are no intersections with any object</li>
	 * <li>0 - if there is an intersections with a non-transparent object</li>
	 * <li>0 > < 1 - if there are intersections with semi-transparent objects</li>
	 * </ul>
	 * 
	 * @param ray the ray from the intersection point to the light
	 * @param l   the light source
	 * @return the level of transparency
	 */
	/**
	 * 
	 * @param ls           the light source
	 * @param intersection
	 * @param l            the ray direction from the light to the intersection
	 *                     point
	 * @param n            normal of the intersection point
	 * @param nv           n*v dot-product
	 * @return the level of transparency
	 */
	protected double transparency(LightSource ls, GeoPoint intersection, Vector l, Vector n, double nv) {
		Ray ray = new Ray(intersection.point, l.scale(-1), n);
		// search for intersections with the ray
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray, ls.getDistance(intersection.point));
		// return 1 if there wasn't any intersections with (gp to light)'s ray that
		// aren't clear
		double kTvalue = l.dotProduct(n) * nv > 0 ? 1 : 0; // : intersection.geometry.getMaterial().kT;
		if (intersections != null && kTvalue >= MIN_CALC_COLOR_K) {
			for (GeoPoint geoPoint : intersections) {
				kTvalue *= geoPoint.geometry.getMaterial().kT;
				if (kTvalue < MIN_CALC_COLOR_K)
					return 0;
			}
		}
		return kTvalue;
	}

	/**
	 * find the intersection with the ray that is the closest intersection to the
	 * start of the ray
	 * 
	 * @param ray the ray to find the intersection with
	 * @return the intersection with the ray that is the closest intersection to the
	 *         start of the ray (if there is no intersections then return null)
	 */
	private GeoPoint findClosestIntersection(Ray ray) {
		return ray.findClosestGeoPoint(scene.geometries.findGeoIntersections(ray, Double.POSITIVE_INFINITY));
	}

	/**
	 * calculating the color of the intersection point that effected by factors like
	 * refraction and reflection
	 * 
	 * @param intersection the point to find the color for
	 * @param v            the original ray direction
	 * @param level        the level of the recursion tree (the number of times we
	 *                     produce refracted/reflection ray)
	 * @param k            the minimum value of the color that we add by calculating
	 *                     the transparency/reflection effect
	 * @return conclusion color of the transparency/reflection effect
	 */
	private Color calcGlobalEffects(GeoPoint intersection, Vector v, int level, double k) {
		Color color = Color.BLACK;
		Vector n = intersection.getNormal();
		// calculates reflection
		double kr = intersection.geometry.getMaterial().kR, kkr = k * kr;
		if (kkr > MIN_CALC_COLOR_K) { // calculates until we reach the minimum effect boundary
			Ray reflectedRay = getReflectRay(v, n, intersection);
			GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
			if (reflectedPoint != null)
				color = color.add(calcColor(reflectedPoint, reflectedRay.getDir(), level - 1, kkr).scale(kr));
			else {
				color = color.add(scene.background).scale(kr);
			}
		}
		// calculates transparency
		double kt = intersection.geometry.getMaterial().kT, kkt = k * kt;
		if (kkt > MIN_CALC_COLOR_K) {// calculates until we reach the minimum effect boundary
			Ray transpRay = getTransparencyRay(v, n, intersection);
			GeoPoint transpPoint = findClosestIntersection(transpRay);
			if (transpPoint != null)
				color = color.add(calcColor(transpPoint, transpRay.getDir(), level - 1, kkt).scale(kt));
			else {
				color = color.add(scene.background).scale(kr);
			}
		}
		return color;
	}

	/**
	 * calculate the reflected ray relative to the original ray, the normal at the
	 * intersection point and the intersection point
	 * 
	 * @param v  the original ray direction
	 * @param n  the normal at the intersection point
	 * @param gp the GeoPoint of the intersection
	 * @return the reflected ray relative to the original ray and the normal at the
	 *         intersection point
	 */
	private Ray getReflectRay(Vector v, Vector n, GeoPoint gp) {
		Vector reflectVec = v.subtract(n.scale(2 * v.dotProduct(n))).normalize();// the direction of the reflection
		return new Ray(gp.point, reflectVec, n);
	}

	/**
	 * calculate the transparency ray relative to the original ray, the normal at
	 * the intersection point and the intersection point
	 * 
	 * @param v  the original ray direction
	 * @param n  the normal at the intersection point
	 * @param gp the GeoPoint of the intersection
	 * @return the transparency ray relative to the original ray, the normal at the
	 *         intersection point and the intersection point
	 */
	private Ray getTransparencyRay(Vector v, Vector n, GeoPoint gp) {
		return new Ray(gp.point, v, n);
	}
	/**
	 * change the direction of the normal randomly, in the range of 180 degrees
	 * @param n the old normal
	 * @return a new random normal
	 */
	public Vector snowEffect(Vector n) {
		Vector temp;
		do {
			double x = ThreadLocalRandom.current().nextDouble(1);
			double y = ThreadLocalRandom.current().nextDouble(1);
			double z = ThreadLocalRandom.current().nextDouble(1);
			temp = new Vector(x,y,z);
		} while (n.dotProduct(temp) < 0);
		return temp.add(n).normalize();
	}
}
