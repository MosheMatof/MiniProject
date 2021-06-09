/**
 * 
 */
package elements;

import java.util.List;

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
	 * get the normalize vector from the light source to a given point
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
	
	/**
	 * gives the radius of the light source
	 * @return the radius
	 */
	public double getRadius();

	/**
	 * get beam of rays from the given point to all the radius of the light source
	 * 
	 * @param p   the requested point
	 * @param kSS the amount of vectors
	 * @return the vector from the edge of the light source to a given point
	 */
	List<Ray> getRandomBeam(Point3D p, int kSS);
	
	/**
	 * get beam of sample rays from the a given point to the center and 4 edges of
	 * the light source
	 * 
	 * @param p the requested point
	 * @return the vector from the edge of the light source to a given point
	 */
	List<Ray> getSampleBeam(Point3D p);
}
