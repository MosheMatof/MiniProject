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
	 * 
	 */
	private static final double INITIAL_K = 1.0;
	/**
	 * 
	 */
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	/**
	 * 
	 */
	private static final double MIN_CALC_COLOR_K = 0.001;
	
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
		GeoPoint closestPoint = findClosestIntersection(ray);
		if (closestPoint == null)
			return scene.background;
			
		return calcColor(closestPoint, ray);
	}

	/**
	 * calculates the color for a given point
	 * @param intersection the point to find the color for
	 * @param ray the ray from the camera
	 * @return the color of this point
	 */
	private Color calcColor(GeoPoint intersection, Ray ray) {
		return calcColor(intersection, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K).add(scene.ambientLight.getIntensity());
	}
	/**
	 * calculates the final color for a given point with all the factors
	 * @param intersection the point to find the color for
	 * @param ray the ray from the camera
	 * @param level the level of the recursion tree (the number of times we produce refracted/reflection ray)
	 * @param k the minimum value of the color that we add by calculating the transparency/reflection effect
	 * @return the final color of this point
	 */
	private Color calcColor(GeoPoint intersection, Ray ray, int level, double k) {
		Color color = intersection.geometry.getEmission();
		color = color.add(calcLocalEffects(intersection, ray,k));
		return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray, level, k));
	}

	/**
	 * calculating the color of the intersection point considering all the
	 * light source of the scene
	 * 
	 * @param intersection the intersection point
	 * @param ray the ray of the intersection
	 * @param k the minimum value of the color that we add by calculating the transparency effect
	 * @return the final color of the intersection point considering all the light
	 *         source of the scene
	 */
	private Color calcLocalEffects(GeoPoint intersection, Ray ray, double k) {
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

			if (nl * nv > 0 ) { // sign(nl) == sing(nv)
				double kTvalue = transparency(l, n, intersection, lightSource);
				if (kTvalue*k > MIN_CALC_COLOR_K) {
					Color lightIntensity = lightSource.getIntensity(intersection.point).scale(kTvalue);
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
	
//	/**
//	 * checks if the light from a light source arrives to this point
//	 * @param l the direction from the light source to the intersection point
//	 * @param n the normal of this GeoPoint
//	 * @param gp the GeoPoint to check for shadows
//	 * @param lc the light source
//	 * @return true if there wasn't any intersections with (gp to light)'s ray that aren't clear, otherwise false 
//	 */
//	private boolean unshaded(Vector l, Vector n, GeoPoint gp, LightSource lc) {
//		Vector lightDirection = l.scale(-1);  // from point to light source
//		Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);  // build the delta vector
//		Point3D point = gp.point.add(delta);  // move the start point of the (gp to light)'s ray by the delta vector
//		Ray lightRay = new Ray(point, lightDirection);  // build the (gp to light)'s ray
//		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, lc.getDistance(point));  // search for intersections with this ray
//		 // return if there wasn't any intersections with (gp to light)'s ray that aren't clear
//		return intersections == null || intersections.stream().noneMatch(g -> g.geometry.getMaterial().kT == 0) ; 
//	}
	
	/**
	 * calculates the sum value of the light 
	 * -> <h1>possibilities values:</h1> 
	 * <ul>
	 *  <li>1 - if there are no intersections with any object </li>
	 *  <li>0 - if there is an intersections with a non-transparent object </li>
	 *  <li>0 > < 1 - if there are intersections with semi-transparent objects </li>
	 *  </ul>
	 * @param l the direction from the light source to the intersection point
	 * @param n the normal of this GeoPoint
	 * @param gp the GeoPoint to check for shadows
	 * @param lc the light source
	 * @return true the value of light
	 */
	private double transparency(Vector l, Vector n, GeoPoint gp, LightSource lc) {
		Vector lightDirection = l.scale(-1);  // from point to light source
		Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);  // build the delta vector
		Point3D point = gp.point.add(delta);  // move the start point of the (gp to light)'s ray by the delta vector
		Ray lightRay = new Ray(point, lightDirection);  // build the (gp to light)'s ray
		List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay, lc.getDistance(point));  // search for intersections with this ray
		 // return if there wasn't any intersections with (gp to light)'s ray that aren't clear
		double kTvalue = 1;
		if (intersections != null) {
			for (GeoPoint geoPoint : intersections) {
				kTvalue *= geoPoint.geometry.getMaterial().kT;
				if (kTvalue < MIN_CALC_COLOR_K) return 0;
			}
		}		
		return kTvalue;
	}
	
	/**
	 * find the intersection with the ray that is the closest intersection to the start of the ray
	 * @param ray the ray to find the intersection with
	 * @return the intersection with the ray that is the closest intersection to the start of the ray (if there is no intersections then return null)
	 */
	private GeoPoint findClosestIntersection(Ray ray) {
		return ray.findClosestGeoPoint(scene.geometries.findGeoIntersections(ray, Double.POSITIVE_INFINITY));
	}
	
	/**
	 * calculating the color of the intersection point that effected by factors like 
	 * refraction and reflection
	 * @param intersection the point to find the color for
	 * @param ray the ray from the camera
	 * @param level the level of the recursion tree (the number of times we produce refracted/reflection ray)
	 * @param k the minimum value of the color that we add by calculating the transparency/reflection effect
	 * @return conclusion color of the transparency/reflection effect
	 */
	private Color calcGlobalEffects(GeoPoint intersection, Ray ray, int level, double k) {
		Color color = Color.BLACK;
		Vector n = intersection.getNormal();
		//calculates reflection
		double kr = intersection.geometry.getMaterial().kR, kkr = k * kr;
		if (kkr > MIN_CALC_COLOR_K) { //calculates until we reach the minimum effect boundary
			Ray reflectedRay = getReflectRay(ray, n, intersection);
			GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
			if(reflectedPoint != null)
				color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
			else {
				color = color.add(scene.background);
			}
			//color = color.add(calcGlobalEffect( getReflectRay(ray, n, intersection), level - 1,kr, kkr));
		}
		//calculates transparency
		double kt = intersection.geometry.getMaterial().kT, kkt = k * kt;
		if (kkt > MIN_CALC_COLOR_K) {//calculates until we reach the minimum effect boundary
			Ray transpRay = getTransparencyRay(ray, n, intersection);
			GeoPoint transpPoint = findClosestIntersection(transpRay);
			if(transpPoint != null)
				color = color.add(calcColor(transpPoint, transpRay, level - 1, kkt).scale(kt));
			else {
				color = color.add(scene.background);
			}
			//color = color.add(calcGlobalEffect( getTransparencyRay(ray, n, intersection), level - 1,kt, kkt));
		}
		return color;
	}

	/**
	 * calculate the reflected ray relative to the original ray, the normal at the intersection point and the intersection point
	 * @param r the original ray
	 * @param n the normal at the intersection point
	 * @param gp the GeoPoint of the intersection
	 * @return the reflected ray relative to the original ray and the normal at the intersection point
	 */
	private Ray getReflectRay(Ray r, Vector n, GeoPoint gp) {
		Vector v = r.getDir();
		Vector reflectVec = v.subtract(n.scale(2*v.dotProduct(n)));//the direction of the reflection
		Vector deltaVec = n.scale(n.dotProduct(v) > 0 ? DELTA : -DELTA);
		return new Ray(gp.point.add(deltaVec), reflectVec);
	}
	
	/**
	 * calculate the transparency ray relative to the original ray, the normal at the intersection point and the intersection point
	* @param r the original ray
	 * @param n the normal at the intersection point
	 * @param gp the GeoPoint of the intersection
	 * @return the transparency ray relative to the original ray, the normal at the intersection point and the intersection point
	 */
	private Ray getTransparencyRay(Ray r, Vector n, GeoPoint gp) {
		Vector v = r.getDir();
		Vector deltaVec = n.scale(n.dotProduct(v) > 0 ? -DELTA : DELTA);
		return new Ray(gp.point.add(deltaVec), v);
	}
}
