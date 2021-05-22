/**
 * 
 */
package elements;

import primitives.*;

/**
 * interface for a light source 
 */
public interface LightSource {
	/**
	 * get the intensity of the light at a given point
	 * @param p the point to get it's intensity
	 * @return the intensity of the light in the point
	 */
	public Color getIntensity(Point3D p);
	/**
	 * get the vector from the light source to a given point
	 * @param p the requested point
	 * @return the vector from the light source to a given point
	 */
	public Vector getL(Point3D p);
	
	/**
	 * finds the distance from the light source to point
	 * @param point the point to fin the distance to
	 * @return the distance from the light source to point
	 */
	public double getDistance(Point3D point);
}
