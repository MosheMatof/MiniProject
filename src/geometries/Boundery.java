/**
 * 
 */
package geometries;

import primitives.*;

/**
 * a region in space
 */
public class Boundery {

	final double maxX, minX;
	final double maxY, minY;
	final double maxZ, minZ;
	
	
	/**
	 * full constructor for {@link Boundery}
	 */
	public Boundery(double maxX, double minX, double maxY, double minY, double maxZ, double minZ) {
		this.maxX = maxX;
		this.minX = minX;
		this.maxY = maxY;
		this.minY = minY;
		this.maxZ = maxZ;
		this.minZ = minZ;
	}
	
	public boolean isIntersect(Ray r) {
		return true;
	}
}
