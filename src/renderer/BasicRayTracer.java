package renderer;

import java.util.LinkedList;
import java.util.List;

import elements.LightSource;
import elements.PointLight;
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
	private static final double MIN_CALC_COLOR_K = 0.001;

	private int kSS = 0;

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
		return calcColor(intersection, ray.getDir(), MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
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
	 * @param v          the ray of the intersection
	 * @param k            the minimum value of the color that we add by calculating
	 *                     the transparency effect
	 * @return the final color of the intersection point considering all the light
	 *         source of the scene
	 */
	private Color calcLocalEffects(GeoPoint intersection, Vector v, double k) {
		Vector n = intersection.getNormal();
		double nv = alignZero(n.dotProduct(v));
		if (nv == 0)
			return Color.BLACK;
		int nShininess = intersection.geometry.getMaterial().nShinines;
		double kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;
		Color color = Color.BLACK;

		for (LightSource lightSource : scene.lights) {
			Point3D interP = intersection.point;
			Vector l = lightSource.getL(interP);
			double nl = alignZero(n.dotProduct(l));

			if (lightSource instanceof PointLight && kSS > 1) { 
				PointLight pointLight = (PointLight)lightSource;
				List<Vector> vecs = pointLight.getSampleVector(interP, n);
				List<Vector> validVecs = new LinkedList<>();
				for (Vector vc : vecs) {
					if(Math.signum(vc.dotProduct(n)) == Math.signum(nv))
						validVecs.add(vc);
				}
				if (!validVecs.isEmpty()) {
					List<Vector> restVecs = pointLight.getRandomVectors(interP, n, kSS);
					for (Vector vc : restVecs) {
						if(Math.signum(vc.dotProduct(n)) == Math.signum(nv))
							validVecs.add(vc);
					}
					double kTvalue = 0;
					for (Vector vec : validVecs) {
						 kTvalue += transparency(vec, n, intersection, lightSource);
					}
					kTvalue = kTvalue/(vecs.size() + restVecs.size());
					if (kTvalue * k > MIN_CALC_COLOR_K) {
						Color lightIntensity = lightSource.getIntensity(interP).scale(kTvalue);
						color = color.add(calcDiffusive(kd, l, n, lightIntensity),
								calcSpecular(ks, l, n, v, nShininess, lightIntensity));
					}
				}		
			}
			//the light is directional light
			else if (Math.signum(nl) == Math.signum(nv)) {
				double kTvalue = transparency(l, n, intersection, lightSource);
					if (kTvalue * k > MIN_CALC_COLOR_K) {
						Color lightIntensity = lightSource.getIntensity(interP).scale(kTvalue);
						color = color.add(calcDiffusive(kd, l, n, lightIntensity),
								calcSpecular(ks, l, n, v, nShininess, lightIntensity));
					}
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
	 * calculates the sum value of the light ->
	 * <h1>possibilities values:</h1>
	 * <ul>
	 * <li>1 - if there are no intersections with any object</li>
	 * <li>0 - if there is an intersections with a non-transparent object</li>
	 * <li>0 > < 1 - if there are intersections with semi-transparent objects</li>
	 * </ul>
	 * 
	 * @param l  the direction from the light source to the intersection point
	 * @param n  the normal of this GeoPoint
	 * @param gp the GeoPoint to check for shadows
	 * @param lc the light source
	 * @return true the value of light
	 */
	private double transparency(Vector l, Vector n, GeoPoint gp, LightSource lc) {
		Vector lightDirection = l.scale(-1); // from point to light source
		Ray lightRay = new Ray(gp.point, lightDirection, n); // build the (gp to light)'s ray
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay,
				lc.getDistance(lightRay.getOrigin())); // search for intersections with this ray
		// return 1 if there wasn't any intersections with (gp to light)'s ray that aren't
		// clear
		double kTvalue = 1;
		if (intersections != null) {
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
	 * @param v          the original ray direction
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
	 * setter for the soft shadow factor.
	 * the number of the rays to send to the light
	 * @param kSS the soft shadow factor
	 * @return instance of this scene
	 */
	public BasicRayTracer setKSS(int kSS) {
		this.kSS = kSS;
		return this;
	}
}
