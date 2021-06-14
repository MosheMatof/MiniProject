package geometries;

import java.lang.invoke.LambdaConversionException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import geometries.Intersectable.Boundary;
import primitives.Ray;

/**
 * list of Intersectable objects as a composite object
 */
public class Geometries implements Intersectable{
	
	static final int MAX_BRANCH = 3;
	
	private LinkedList<Intersectable> components = new LinkedList<Intersectable>();
	private Boundary boundary;

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

	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray, double maxDist) {
		List<GeoPoint> intrsctPnts = null;
		for (Intersectable component : components) {
			List<GeoPoint> fi = component.findGeoIntersections(ray, maxDist);
			if (fi != null) {
				if (intrsctPnts == null) {
					intrsctPnts = new LinkedList<GeoPoint>(fi);
				} else {
					intrsctPnts.addAll(fi);
				}
			}		
		}
		return intrsctPnts;
	}

	@Override
	public Boundary getBoundary() {
		return boundary;
	}

	public void constructHeirarchy() {
		if(components.size() > MAX_BRANCH) {
			BiPredicate<Intersectable, Intersectable> sort;
			if(true) {}
		}
	}
}
