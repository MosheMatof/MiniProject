package geometries;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

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
	 * construct a new Geometries by list
	 * @param components the list of components of the new geometry
	 */
	public Geometries(LinkedList<Intersectable> components) {
		this.components = components;
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
		if(!boundary.isIntersect(ray, maxDist)) return null;
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
	
	/**
	 * Arrange the geometries in of in an efficient hierarchy for ray tracing
	 */
	public void initConstructHeirarchy() {
		Geometries infinit = new Geometries();
		Geometries finite = new Geometries();
		
		for(Intersectable c : components) {
			if(c.isInfinite()) infinit.add(c);
			else finite.add(c);
		}

		this.components = new LinkedList<Intersectable>(List.of(infinit, finite));
		finite.constructHeirarchy();
	}
	
	
	private void constructHeirarchy() {
		if (components.size() <= MAX_BRANCH) {
			return;
		}
		BiPredicate<Intersectable, Intersectable> sort;
		if(true) {
			
		}
	}

	@Override
	public boolean isInfinite() {
		return !components.stream().anyMatch(x -> x.isInfinite());
	}
}
