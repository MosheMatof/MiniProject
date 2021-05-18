package geometries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import geometries.Intersectable.GeoPoint;
import primitives.*;

import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 * 
 * @author Dan
 */
public class Polygon extends Geometry {
	/**
	 * List of polygon's vertices
	 */
	protected List<Point3D> vertices;
	/**
	 * Associated plane in which the polygon lays
	 */
	protected Plane plane;

	/**
	 * Polygon constructor based on vertices list. The list must be ordered by edge
	 * path. The polygon must be convex.
	 * 
	 * @param vertices list of vertices according to their order by edge path
	 * @throws IllegalArgumentException in any case of illegal combination of
	 *                                  vertices:
	 *                                  <ul>
	 *                                  <li>Less than 3 vertices</li>
	 *                                  <li>Consequent vertices are in the same
	 *                                  point
	 *                                  <li>The vertices are not in the same
	 *                                  plane</li>
	 *                                  <li>The order of vertices is not according
	 *                                  to edge path</li>
	 *                                  <li>Three consequent vertices lay in the
	 *                                  same line (180&#176; angle between two
	 *                                  consequent edges)
	 *                                  <li>The polygon is concave (not convex)</li>
	 *                                  </ul>
	 */
	public Polygon(Point3D... vertices) {
		if (vertices.length < 3)
			throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
		this.vertices = List.of(vertices);
		// Generate the plane according to the first three vertices and associate the
		// polygon with this plane.
		// The plane holds the invariant normal (orthogonal unit) vector to the polygon
		plane = new Plane(vertices[0], vertices[1], vertices[2]);
		if (vertices.length == 3)
			return; // no need for more tests for a Triangle

		Vector n = plane.getNormal();

		// Subtracting any subsequent points will throw an IllegalArgumentException
		// because of Zero Vector if they are in the same point
		Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
		Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

		// Cross Product of any subsequent edges will throw an IllegalArgumentException
		// because of Zero Vector if they connect three vertices that lay in the same
		// line.
		// Generate the direction of the polygon according to the angle between last and
		// first edge being less than 180 deg. It is hold by the sign of its dot product
		// with
		// the normal. If all the rest consequent edges will generate the same sign -
		// the
		// polygon is convex ("kamur" in Hebrew).
		boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
		for (int i = 1; i < vertices.length; ++i) {
			// Test that the point is in the same plane as calculated originally
			if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
				throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
			// Test the consequent edges have
			edge1 = edge2;
			edge2 = vertices[i].subtract(vertices[i - 1]);
			if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
				throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
		}
	}

	@Override
	public Vector getNormal(Point3D point3d) {
		return getNormal();
	}
	
	/**
	 * get the normal to the plane that contains the polygon
	 * @return the normal to the plane that contains the polygon
	 */
	public Vector getNormal() {
		return plane.getNormal();
	}
	
	@Override
	public String toString() {
		String result = "";
		for(Point3D vertex : vertices) {
			result += vertex.toString() + ", ";
		}
		return result;
	}

//	@Override
//	public List<Point3D> findIntersections(Ray ray) {
//		//
//		List<Point3D> interPoint = this.plane.findIntersections(ray);
//		if (interPoint == null) {
//			return null;
//		}
//		int size = vertices.size();
//		
//		//array of all the vectors from the ray point to the vertices
//		Vector[] rayToVertices = new Vector[size];
//		int i = 0;
//		for (Point3D p : vertices) {
//			rayToVertices[i++] = p.subtract(ray.getOrigin());
//		}
//		//array of all the normal vectors that come form crossProduct between each adjacent vectors(rayToVertices)
//		Vector[] normalsFromVertices = new Vector[size];
//		for (int j = 0; j < size - 1; j++) {
//			normalsFromVertices[j] = rayToVertices[j].crossProduct(rayToVertices[j + 1]).normalize();
//		}
//		normalsFromVertices[size - 1] = rayToVertices[size - 1].crossProduct(rayToVertices[0]).normalize();
//		
//		//the values of all the normals * ray
//		List<Double> normals = new ArrayList<Double>();
//		for (Vector s : normalsFromVertices) {
//			normals.add(Util.alignZero(ray.getDir().dotProduct(s)));
//		}
//		
//		//if all the scalars have the same sign and non of theme is zero => the point is inside the polygon
//		if (normals.stream().allMatch(d -> d > 0) || normals.stream().allMatch(d -> d < 0)) {
//			return interPoint;
//		}
//		return null;
//	}

	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray) {
		List<GeoPoint> interPoint = this.plane.findGeoIntersections(ray);
		if (interPoint == null) {
			return null;
		}
		int size = vertices.size();
		
		//array of all the vectors from the ray point to the vertices
		Vector[] rayToVertices = new Vector[size];
		int i = 0;
		for (Point3D p : vertices) {
			rayToVertices[i++] = p.subtract(ray.getOrigin());
		}
		//array of all the normal vectors that come form crossProduct between each adjacent vectors(rayToVertices)
		Vector[] normalsFromVertices = new Vector[size];
		for (int j = 0; j < size - 1; j++) {
			normalsFromVertices[j] = rayToVertices[j].crossProduct(rayToVertices[j + 1]).normalize();
		}
		normalsFromVertices[size - 1] = rayToVertices[size - 1].crossProduct(rayToVertices[0]).normalize();
		
		//the values of all the normals * ray
		List<Double> normals = new ArrayList<Double>();
		for (Vector s : normalsFromVertices) {
			normals.add(Util.alignZero(ray.getDir().dotProduct(s)));
		}
		
		//if all the scalars have the same sign and non of theme is zero => the point is inside the polygon
		if (normals.stream().allMatch(d -> d > 0) || normals.stream().allMatch(d -> d < 0)) {
			interPoint.get(0).geometry = this;
			return interPoint;
		}
		return null;
	}
}
