/**
 * 
 */
package renderer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static primitives.Util.*;
import primitives.*;

/**
 * Arrange dots in a certain pattern in a 2D board
 */
public class BlackBoard {

	static class Point2D {
		private double x;
		private double y;

		/**
		 * ctor for Point2D
		 * 
		 * @param x the x coordinate
		 * @param y the y coordinate
		 */
		public Point2D(double x, double y) {
			this.x = x;
			this.y = y;
		}

	}

	/**
	 * generates a black board with 5 points in a 2X2 square boundary(from center
	 * and 4 carves)
	 * 
	 * @return a black board with 5 points 2X2 square boundary(from center and 4
	 *         carves)
	 */
	public static BlackBoard sempleSquare(double size) {
		Point2D ur = new Point2D(size, size); // the up-right point
		Point2D ul = new Point2D(-size, size); // the up-left point
		Point2D mid = new Point2D(0, 0); // the mid point
		Point2D dr = new Point2D(size, -size); // the down-right point
		Point2D dl = new Point2D(-size, -size); // the down-left point
		return new BlackBoard(ur, ul, mid, dr, dl);
	}

	/**
	 * generate a BlackBoard with points randomly arrange in 2X2 square boundary
	 * 
	 * @param n    the square root of the number of points to generate in the
	 *             blackBoard
	 * @param size the dimantions of the square
	 * @return a BlackBoard with points randomly arrange in square boundary
	 */
	public static BlackBoard squareRandom(int n, double size) {
		if (n == 1)
			return new BlackBoard(List.of(new Point2D(0, 0)));
		LinkedList<Point2D> points = new LinkedList<Point2D>();
		double interval = 2 * size / (n - 1);
		for (double i = -size; i < size; i += interval) {
			for (double j = -size; j < size; j += interval) {
				double rH = ThreadLocalRandom.current().nextDouble(interval) + i;
				double rW = ThreadLocalRandom.current().nextDouble(interval) + j;
				points.add(new Point2D(rW, rH));
			}
		}
		return new BlackBoard(points);
	}

	/**
	 * generates a black board with 5 points in the unit circle boundary(from center
	 * and 4 vertices)
	 * 
	 * @return a black board with 5 points in the unit circle boundary(from center
	 *         and 4 vertices)
	 */
	public static BlackBoard sampleCircle(double radius) {
		Point2D up = new Point2D(0, radius); // up point
		Point2D dp = new Point2D(0, -radius); // down point
		Point2D rp = new Point2D(radius, 0); // right point
		Point2D lp = new Point2D(-radius, 0); // left point
		Point2D cp = new Point2D(0, 0); // center point
		return new BlackBoard(up, dp, rp, lp, cp);
	}

	/**
	 * the square root of the ratio between the unit circle and a 2X2 square
	 */
	// private static final double RATIO_CIRCLE_SQUAR_SQURT = 1.128379167;

	/**
	 * generate a BlackBoard with points randomly arrange in the unit circle
	 * boundary
	 * 
	 * @param n the square root of the number of points to generate in the
	 *          blackBoard
	 * @return a BlackBoard with points randomly arrange in the unit circle boundary
	 */
	public static BlackBoard circleRandom(int n, double radius) {
		LinkedList<Point2D> points = new LinkedList<>();
		double radiusSquared = radius * radius;
		double x, y;
		for (int i = 0; i < n; i++) {
			do {
				x = ThreadLocalRandom.current().nextDouble(-radius, radius);
				y = ThreadLocalRandom.current().nextDouble(-radius, radius);
			} while (x * x + y * y >= radiusSquared);
			points.add(new Point2D(x, y));
		}
		return new BlackBoard(points);
		// n *= RATIO_CIRCLE_SQUAR_SQURT;
		// double interval = 2*radius/(n-1);
		// for (double i = -radius; i < radius; i+=interval) {
		// for (double j = -radius; j < radius; j+=interval) {
		// double rH = ThreadLocalRandom.current().nextDouble(interval) + i;
		// double rW = ThreadLocalRandom.current().nextDouble(interval) + j;
		// if(Util.alignZero(rH*rH + rW*rW - radius*radius) > 0) // if this coordinates
		// is outside of the circle
		// continue;
		// points.add(new Point2D(rW, rH));
		// }
		// }
//		for (int i = 0; i < n; i++) {
//			// create random polar system coordinates of a point in circle of radius r
//			double cosTeta = ThreadLocalRandom.current().nextDouble(-1, 1);
//			double sinTeta = Math.sqrt(1 - cosTeta * cosTeta); //
//			double d = ThreadLocalRandom.current().nextDouble(-radius, radius);
//			// Convert polar coordinates to Cartesian ones
//			double x = d * cosTeta;
//			double y = d * sinTeta;
//			// pC - center of the circle
//			// p0 - start of central ray, v - its direction, distance - from p0 to pC
//			Point2D point = new Point2D(0, 0);
//			if (!Util.isZero(x))
//				point.x = x;
//			if (!Util.isZero(y))
//				point.y = y;
//			points.add(point); // normalized inside Ray ctor
//		}
		// for (int i = 0; i < n; i++) {
		// Point2D point = new Point2D(0, 0);
		// double x = ThreadLocalRandom.current().nextDouble(-radius,radius);
		// double y = ThreadLocalRandom.current().nextDouble(-radius,radius);
		// if (!Util.isZero(x))
		// point.x = x;
		// if (!Util.isZero(y))
		// point.y = y;
		// points.add(point);
		// }
		//return new BlackBoard(points);
	}

	private List<Point2D> points = new LinkedList<Point2D>();
	private double width = 1;
	private double height = 1;
	/**
	 * when generating a 3d points from the black board the scaleWidth will make all
	 * the points in the right width place according to the current width
	 */
	private double scaleWidth = 1;
	/**
	 * when generating a 3d points from the black board the scaleHeight will make
	 * all the points in the right Height place according to the current height
	 */
	private double scaleHeight = 1;

	/**
	 * construct a BlackBoard with a given points
	 * 
	 * @param points the points for the new BlackBoard
	 */
	private BlackBoard(Point2D... points) {
		for (Point2D point : points)
			this.points.add(point);
	}

	/**
	 * construct a BlackBoard with a given list of points
	 * 
	 * @param points the points for the new BlackBoard
	 */
	private BlackBoard(List<Point2D> points) {
		this.points = points;
	}

	/**
	 * get the number of points in the board
	 * 
	 * @return
	 */
	public int getN() {
		return points.size();
	}

	/**
	 * transform the 2d points of the BlackBord to a 3d points in space relative to
	 * the position and orientation of the board in space
	 * 
	 * @param up     the up direction of the board (has to be normalized) (has to be
	 *               vertical to right)
	 * @param right  the right direction of the board (has to be normalized) (has to
	 *               be vertical to up)
	 * @param center the point in the center of the board
	 * @param scale  a scalar to scale the size of the blackBoard by
	 * @return a List<Point3D> of 3d points that is a transformation of the 2d
	 *         points of the BlackBord to a 3d points in space relative to the
	 *         position and orientation of the board in space
	 */
	public List<Point3D> generate3dPoints(Vector up, Vector right, Point3D center) {
		LinkedList<Point3D> points3d = new LinkedList<Point3D>();
		for (Point2D point2d : points) {
			Point3D point3d = center;
			if (!isZero(point2d.x))
				point3d = point3d.add(right.scale(point2d.x * scaleWidth));
			if (!isZero(point2d.y))
				point3d = point3d.add(up.scale(point2d.y * scaleHeight));
			points3d.add(point3d);
		}
		return points3d;
	}

	/**
	 * changes the width of the board
	 * 
	 * @param w the new width
	 * @return it self
	 */
	public BlackBoard setWidth(double w) {
		scaleWidth *= w / width;
		width = w;
		return this;
	}

	/**
	 * changes the height of the board
	 * 
	 * @param h the new height
	 * @return it self
	 */
	public BlackBoard setHeight(double h) {
		scaleHeight *= h / height;
		height = h;
		return this;
	}

	/**
	 * changes the radius of the circle blocked in the board of the board
	 * 
	 * @param r the new radius
	 * @return it self
	 */
	public BlackBoard setRadius(double r) {
		setHeight(2 * r);
		setWidth(2 * r);
		return this;
	}
}
