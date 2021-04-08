package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;

/**
 * list of Intersectable objects as a composite object
 */
public class Geometries implements Intersectable {
	private LinkedList<Intersectable> components;
	
	/**
	 * Default constructor:
	 * initialize the class's components list to be an empty list
	 */
	public Geometries() {
		components = new LinkedList<Intersectable>();
	}
	
	/**
	 * initializes the list with the values in 'geometries'
	 * @param geometries the values to initialize the class's list with
	 */
	public Geometries(Intersectable... geometries) {
		components = new LinkedList<Intersectable>();
		for (Intersectable intersectable : geometries) {
			components.add(intersectable);
		}
	}
	
	/**
	 * adds a collection of geometries to the components list
	 * @param geometries geometries to add to the components list
	 */
	public void add(Intersectable...geometries) {
		for (Intersectable intersectable : geometries) {
			components.add(intersectable);
		}
	}
	
	@Override
	public List<Point3D> findIntersections(Ray ray) {
		return null;
	}

}
