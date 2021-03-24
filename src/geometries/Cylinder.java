package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

/**
 * 
 * represents a Cylinder in space
 *
 */
public class Cylinder extends Tube{

	private double height;
	
	/**
	 * 
	 * @param axis the axis of the Cylinder
	 * @param radius the radius of the Cylinder
	 * @param height the height of the Cylinder
	 */
	public Cylinder(Ray axis, double radius, double height) {
		super(axis, radius);
		this.height = height;
	}
	
	@Override
	public Vector getNormal(Point3D point) {
		Vector vector = point.subtract(axis.getOrigin());
		double t = axis.getDir().dotProduct(vector);
		if(Util.isZero(t))//if t == 0 then the point is on the lower surface of the cylinder
		{
			return axis.getDir().scale(-1);
		}
		if(Util.isZero(t - height))//if t == height then the point is on the lower surface of the cylinder
		{
			return axis.getDir();
		}
		Point3D o = axis.getOrigin().add(vector.scale(t));
		
		return point.subtract(o).normalize();	
	}
	
	@Override
	public String toString() {
		return super.toString() + ", height: " + height;
	}

	/**
	 * 
	 * @return the height of the Cylinder
	 */
	public double getHeight() {
		return height;
	}
}
