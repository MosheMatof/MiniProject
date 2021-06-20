package geometries;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import primitives.Point3D;
import primitives.Ray;

/**
 * list of Intersectable objects as a composite object
 */
public class Geometries extends Intersectable{
	
	static final int KNN_ITERATION = 4;
	/**
	 * the number of splits in each iteration of hierarchy building
	 */
	static final int N_SPLITS = 8;
	/**
	 * the max number of {@link Geometry}'s that can be in one boundary
	 */
	static final int MAX_BRANCH = 17;//17 - 2.232
	
	
	/**
	 * if true then the components will be classified by scale of volume
	 */
	static final boolean SPLIT_BY_VOLUME = false;
	/**
	 * the max ratio that can be between volumes of two components that in the same scale of volume
	 * (relevant only if SPLIT_BY_VOLUME = true)
	 */
	static final int MAX_DIFRENCE = 5;
	/**
	 * if true then every split the components will be sorted by the longest axis and then split
	 */
	static final boolean SPLIT_THE_LONGEST_AXIS = true;//36
	/**
	 * if true then every iteration the components will be sorted by the longest axis and then split N_SPLITS times
	 */
	static final boolean SPLIT_THE_LONGEST_AXIS_ONCE = false;//26.122
	
	private List<Intersectable> components = new LinkedList<Intersectable>();

	/**
	 * Default constructor: initialize the class's components list to be an empty list
	 */
	public Geometries() {
		initBoundary();
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
	public Geometries(List<Intersectable> components) {
		this.components = components;
		initBoundary();
	}

	public void add(LinkedList<Intersectable> components) {
		this.components.addAll(components);
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
	protected List<GeoPoint> findGeoIntersections(Ray ray, double maxDist) {
		List<GeoPoint> intrsctPnts = null;
		for (Intersectable component : components) {
			List<GeoPoint> fi = component.findGeoIntersectionsMain(ray, maxDist);
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
	
	/**
	 * Arrange the geometries in of in an efficient hierarchy for ray tracing
	 */
	public void initConstructHeirarchy() {
		initBoundary();
		if (components.size() <= N_SPLITS) {
			return;
		}
		
		if(SPLIT_BY_VOLUME) {
			splitByVolume();
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
		}
	
	/**
	 * use the knn algorithm to split 'comps' to k groups
	 * @param k the number of groups to split to
	 * @param comps list of the objects to split
	 * @return a list of groups of intersectable
	 */
	private List<List<Intersectable>> knn(int k, List<Intersectable> comps){
		List<List<Intersectable>> groups = new ArrayList<>(k);	
		for (int i = 0; i < k; i++)
			groups.add(new LinkedList<Intersectable>());
		int s = comps.size();
		int sdk = s/k;
		Point3D represents[] = new Point3D[k];
		//take the center of k random Intersectable from 'comps' as the represents for the first iteration of the knn
		for(int i = 0; i < k; i++) {
			int r = ThreadLocalRandom.current().nextInt(comps.size());
			represents[i] = comps.get(r).boundary.center;
		}
		//iterate until there is no change in the represents or KNN_ITERATION times
		boolean flag = true;
		for (int i = 0; i < KNN_ITERATION && flag; i++) {
			//insert each intersectable to the most suitable group
			for(Intersectable c : comps) {
				Point3D p = c.boundary.center;
				int repIndex = 0;
				double minDist = p.distanceSquared(represents[0]);
				for(int j = 1; j < k; j++) {
					double dist = p.distanceSquared(represents[j]);
					if(dist < minDist)
						repIndex = j;
				}
				groups.get(repIndex).add(c);
			}
			Point3D newRepresents[] = knnHelper(groups);
			// if the new represents are the same as the current ones then flag need to be false
			for(int j = 0; j < k; j++) {
				flag = false;
				if(represents[j] != newRepresents[j]) {
					flag = true;
					break;
				}
			}
			represents = newRepresents;
		}
		return groups;
	}
	
	/**
	 * calc the point at the center of each group
	 * @param groups a list of the groups to find the represent point of each group
	 * @return an array of the represents points of all the groups in the same order of the groups 
	 */
	private Point3D[] knnHelper(List<List<Intersectable>> groups) {
		int k = groups.size();
		Point3D represents[] = new Point3D[k];
		for (int j = 0; j < k; j++) {
			if(groups.get(j).size() == 0) {
				represents[j] = new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
				continue;
			}
			double x = 0, y = 0, z = 0;
			for (Intersectable c : groups.get(j)) {
				Point3D p = c.boundary.center;
				x += p.getX();
				y += p.getY();
				z += p.getZ();
			}
			double dSize = 1/groups.get(j).size();
			represents[j] = new Point3D(x*dSize, y*dSize, z*dSize);
		}
		return represents;
	}
	
	
	/**
	 * Arrange the geometries in an efficient hierarchy for ray tracing
	 */
	private void constructHeirarchy() {
		if (components.size() <= MAX_BRANCH) {
			return;
		}
		if(SPLIT_THE_LONGEST_AXIS_ONCE) {
			//sort the components list according to the longest axis
			if(boundary.lenX() > boundary.lenY()) {
				if(boundary.lenX() > boundary.lenZ()) //x is the longest dimension
					components.sort(Comparator.comparingDouble(a -> a.boundary.center.getX()));
				else//z is the longest dimension
					components.sort(Comparator.comparingDouble(a -> a.boundary.center.getZ()));
			}
			else {
				if(boundary.lenY() > boundary.lenZ()) //y is the longest dimension
					components.sort(Comparator.comparingDouble(a -> a.boundary.center.getY()));
				else//z is the longest dimension
					components.sort(Comparator.comparingDouble(a -> a.boundary.center.getZ()));
			}
		}
		
		List<List<Intersectable>> groups = new LinkedList<>();
		groups.add(new LinkedList<>(components));
		boolean flag = true;
		for (int i = 0; i < N_SPLITS && flag; i++) {
			List<List<Intersectable>> newGroups = new LinkedList<>();
			for (List<Intersectable> group : groups) {
				if(group.size() > MAX_BRANCH) {
					newGroups.addAll(split(group, 2));
				}
				else {
					newGroups.add(group);
					flag = false;
				}
			}
			groups = newGroups;
		}
		
		components = new LinkedList<Intersectable>();
		groups.forEach(g -> {
			Geometries geo = new Geometries(g); 
			geo.initBoundary();
			components.add(geo);
			});
	}

	/**
	 * splits a list of Intersectable to k smaller list according to the longest axis
	 * @param comps the list of Intersectable to split
	 * @param k the number of groups to split to
	 * @return a list of the groups that created from the split
	 */
	private List<List<Intersectable>> split(List<Intersectable> comps, int k){
		if(SPLIT_THE_LONGEST_AXIS) {
			Boundary b = calcBoundary(comps);
			//sort the components list according to the longest axis
			if(b.lenX() > b.lenY()) {
				if(b.lenX() > b.lenZ()) //x is the longest dimension
					comps.sort(Comparator.comparingDouble(a -> a.boundary.center.getX()));
				else//z is the longest dimension
					comps.sort(Comparator.comparingDouble(a -> a.boundary.center.getZ()));
			}
			else {
				if(b.lenY() > b.lenZ()) //y is the longest dimension
					comps.sort(Comparator.comparingDouble(a -> a.boundary.center.getY()));
				else//z is the longest dimension
					comps.sort(Comparator.comparingDouble(a -> a.boundary.center.getZ()));
			}
		}
				
		List<List<Intersectable>> groups = new LinkedList<>();

		int interval = comps.size()/k;
		for(int i = 0, j = interval; i < comps.size(); i = j, j = i + interval) {
			if(j > comps.size())
				j = comps.size();
			groups.add(new LinkedList(comps.subList(i, j)));
		}
		return groups;
	}
	
	private Boundary calcBoundary(List<Intersectable> comps) {
		double maxX = Double.NEGATIVE_INFINITY, minX = Double.POSITIVE_INFINITY,
				maxY =Double.NEGATIVE_INFINITY, minY = Double.POSITIVE_INFINITY,
				maxZ = Double.NEGATIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
		for (Intersectable i : components) {
			if(i.isInfinite()) {
				return new Boundary
						(Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY
						,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY
						,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY);
			}
			if(i.boundary == null)
				i.initBoundary();
			Boundary b = i.boundary;
			maxX = b.maxX < maxX ? maxX : b.maxX; 
			minX = b.minX > minX ? minX : b.minX; 
			maxY = b.maxY < maxY ? maxY : b.maxY; 
			minY = b.minY > minY ? minY : b.minY;
			maxZ = b.maxZ < maxZ ? maxZ : b.maxZ; 
			minZ = b.minZ > minZ ? minZ : b.minZ;
		}
		return new Boundary(maxX, minX, maxY, minY, maxZ, minZ);
	}
	
	/**
	 * Divide the geometries to groups according to there volume of the  
	 */
	private void splitByVolume() {
		Hashtable<Double, List<Intersectable>> groups = new Hashtable<>();
		List<Double> volums = new LinkedList<Double>();
		for(Intersectable c : components) {
			boolean flag = true;
			for(double v : groups.keySet()) {
				if(c.boundary.volume <= MAX_DIFRENCE*v && MAX_DIFRENCE*c.boundary.volume >= v) {
					flag = false;
					groups.get(v).add(c);
					break;
				}
			}
			if(flag)
				groups.put(c.boundary.volume, new LinkedList<>(List.of(c)));
		}
		components = new LinkedList<Intersectable>();
		for(List<Intersectable> group : groups.values()) {
			Geometries geo = new Geometries(group);
			geo.constructHeirarchy();
			components.add(geo);
		}
	}
	
	/**
	 * Initialize the boundary of the Geometries
	 */
	@Override
	protected void initBoundary() {
		this.boundary = calcBoundary(this.components);
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
