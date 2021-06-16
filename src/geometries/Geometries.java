package geometries;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


import primitives.Ray;

/**
 * list of Intersectable objects as a composite object
 */
public class Geometries implements Intersectable{
	
	static final int MAX_BRANCH = 10;
	
	private List<Intersectable> components = new LinkedList<Intersectable>();
	private Boundary boundary;

	/**
	 * Default constructor: initialize the class's components list to be an empty list
	 */
	public Geometries() {
		//initBoundary();
	}

	/**
	 * initializes the list with the values in 'geometries'
	 * 
	 * @param geometries the values to initialize the class's list with
	 */
	public Geometries(Intersectable... geometries) {
		add(geometries);
		//initBoundary();
	}

	/**
	 * construct a new Geometries by list
	 * @param components the list of components of the new geometry
	 */
	public Geometries(List<Intersectable> components) {
		this.components = components;
		//initBoundary();
	}

	/**
	 * adds a collection of geometries to the components list
	 * 
	 * @param geometries geometries to add to the components list
	 */
	public void add(Intersectable... geometries) {
//		double maxX = boundary.maxX, minX = boundary.minX,
//				maxY = boundary.maxX, minY = boundary.minX,
//				maxZ = boundary.maxX, minZ = boundary.minX;
		for (Intersectable intersectable : geometries) {
			components.add(intersectable);
//			Boundary b = intersectable.getBoundary();
//			maxX = b.maxX < maxX ? maxX : b.maxX; 
//			minX = b.minX > minX ? minX : b.minX; 
//			maxY = b.maxY < maxY ? maxY : b.maxY; 
//			minY = b.minY > minY ? minY : b.minY;
//			maxZ = b.maxZ < maxZ ? maxZ : b.maxZ; 
//			minZ = b.minZ > minZ ? minZ : b.minZ; 
		}
//		this.boundary = new Boundary(maxX, minX, maxY, minY, maxZ, minZ);
	}

	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray, double maxDist) {
		if(!boundary.isIntersect(ray, maxDist))
			return null;
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
		initBoundary();
		if (components.size() <= MAX_BRANCH) {
			return;
		}
		Geometries infinit = new Geometries();
		Geometries finite = new Geometries();
		
		for(Intersectable c : components) {
			if(c.isInfinite()) infinit.add(c);
			else 
				finite.add(c);
		}

		this.components = new LinkedList<Intersectable>(List.of(infinit, finite));
		finite.constructHeirarchy();
		infinit.initBoundary();
	}
	
	private void classifyBysize() {
		double maxVolume = Double.NEGATIVE_INFINITY, minVolume = Double.POSITIVE_INFINITY;
		for(Intersectable i : components) {
			Boundary b = i.getBoundary();
			maxVolume = maxVolume < b.volume ? b.volume : maxVolume;
			minVolume = minVolume > b.volume ? b.volume : minVolume;
		}
	}
	
	private void constructHeirarchy() {
		initBoundary();
		if (components.size() <= MAX_BRANCH) {
			return;
		}
		
		//sort the components list according to the longest axis
		if(boundary.lenX() > boundary.lenY()) {
			if(boundary.lenX() > boundary.lenZ()) //x is the longest dimension
				components.sort(Comparator.comparingDouble(a -> a.getBoundary().center.getX()));
			else//z is the longest dimension
				components.sort(Comparator.comparingDouble(a -> a.getBoundary().center.getZ()));
		}
		else {
			if(boundary.lenY() > boundary.lenZ()) //y is the longest dimension
				components.sort(Comparator.comparingDouble(a -> a.getBoundary().center.getY()));
			else//z is the longest dimension
				components.sort(Comparator.comparingDouble(a -> a.getBoundary().center.getZ()));
		}
		
		Geometries g1 = new Geometries(components.subList(0, (int) (components.size() * 0.5)));
		Geometries g2 = new Geometries(components.subList((int) (components.size() * 0.5), components.size()));
		
		g1.constructHeirarchy();
		g2.constructHeirarchy();
		this.components = List.of(g1, g2);
	}

	
	
	/**
	 * Initialize the boundary of the Geometries
	 */
	private void initBoundary() {
		//if(Double.isInfinite(maxX + maxY + maxZ) || Double.isInfinite(minX + minY + minZ)) {
		if(isInfinite()) {
			this.boundary = new Boundary
					(Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY
					,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY
					,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY);
			return;
		}
		double maxX = Double.NEGATIVE_INFINITY, minX = Double.POSITIVE_INFINITY,
				maxY =Double.NEGATIVE_INFINITY, minY = Double.POSITIVE_INFINITY,
				maxZ = Double.NEGATIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
		for (Intersectable i : components) {
			Boundary b = i.getBoundary();
			maxX = b.maxX < maxX ? maxX : b.maxX; 
			minX = b.minX > minX ? minX : b.minX; 
			maxY = b.maxY < maxY ? maxY : b.maxY; 
			minY = b.minY > minY ? minY : b.minY;
			maxZ = b.maxZ < maxZ ? maxZ : b.maxZ; 
			minZ = b.minZ > minZ ? minZ : b.minZ; 
		}
		this.boundary = new Boundary(maxX, minX, maxY, minY, maxZ, minZ);
	}
	
	
	@Override
	public boolean isInfinite() {
		return components.stream().anyMatch(x -> x.isInfinite());
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("size: " + components.size() + "\n");
		components.forEach(s -> {
		if(s instanceof Geometries)
			str.append(s.toString());
		});
		return str.toString();
	}
}
