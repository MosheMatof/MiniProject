package geometries;

import java.util.LinkedList;
import java.util.List;

import primitives.Point3D;
import primitives.Ray;

/**
 * list of Intersectable objects as a composite object
 */
public class Geometries implements Intersectable {
	private LinkedList<Intersectable> components = new LinkedList<Intersectable>();

	/**
	 * Default constructor: initialize the class's components list to be an empty list
	 */
	public Geometries() {
	}

	/**
	 * initializes the list with the values in 'geometries'
	 * 
	 * @param geometries the values to initialize the class's list with
	 */
	public Geometries(Intersectable... geometries) {
		add(geometries);
	}

	/**
	 * adds a collection of geometries to the components list
	 * 
	 * @param geometries geometries to add to the components list
	 */
	public void add(Intersectable... geometries) {
		for (Intersectable intersectable : geometries)
			components.add(intersectable);
	}

	// finds all the intersections point of all the components with 'ray'
	@Override
	public List<Point3D> findIntersections(Ray ray) {
		List<Point3D> intrsctPnts = null;
		for (Intersectable component : components) {
			List<Point3D> fi = component.findIntersections(ray);
			if (fi != null) {
				if (intrsctPnts == null) {
					intrsctPnts = new LinkedList<Point3D>(fi);
				} else {
					intrsctPnts.addAll(fi);
				}
			}		
		}
		return intrsctPnts;
	}

}
