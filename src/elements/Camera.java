/**
 * 
 */
package elements;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import primitives.*;
import renderer.BlackBoard;

/**
 * the point of view the scene
 */
public class Camera {

	private Point3D p0;

	private Vector vUp;
	private Vector vTo;
	private Vector vRight;
	private double width;
	private double height;
	private double dis;

	/**
	 * camera constructor
	 * 
	 * @param p0  the location point of the camera
	 * @param vUp the direction vector from the top of the camera
	 * @param vTo the direction vector of the camera
	 */
	public Camera(Point3D p0, Vector vTo, Vector vUp) {
		super();
		this.p0 = p0;
		this.vUp = vUp.normalize();
		this.vTo = vTo.normalize();
		// if the vectors are orthogonal -> generates the right vector
		if (Util.isZero(vUp.dotProduct(vTo))) {
			this.vRight = vTo.crossProduct(vUp).normalize();
		} else {
			throw new IllegalArgumentException("the vTo and vUp aren't orthogonal");
		}

	}

	/**
	 * setting the view plane size of the camera
	 * 
	 * @param width  the width of the view plane
	 * @param height the height of the view plane
	 * @return this camera
	 */
	public Camera setViewPlaneSize(double width, double height) {
		this.height = height;
		this.width = width;
		return this;
	}

	/**
	 * setting view plane distance from the camera
	 * 
	 * @param distance the distance of the view plane from the camera
	 * @return this camera
	 */
	public Camera setDistance(double distance) {
		this.dis = distance;
		return this;
	}

//	/**
//	 * generate a list of rays that start at p0 and pass at a points on the pixel
//	 * the view plane
//	 * @param nX number of pixels in the right-left axis
//	 * @param nY number of pixels in the up-down axis
//	 * @param j  the column of the desired pixel
//	 * @param i  the row of the desired pixel
//	 * @param n the dimnension of matrix of the random rays contracted through the pixel
//	 * @return a list of rays that start at p0 and pass at a points in the pixel
//	 */
	public List<Ray> constructRaysThroughPixel(int nX, int nY, int j, int i, int n) {
		double ry = height / nY; // the height of each pixel
		double rx = width / nX; // the width of each pixel
		double radius = rx > ry ? ry : rx;
		Point3D pIJ = calcCenter(nX, nY, j, i);
		Ray ray = new Ray(p0, pIJ.subtract(p0));
		if (n > 1) { // generats random rays through the pixel
			return ray.getRaysSquare(pIJ, radius, vUp, n);
			// List<Point3D> points = randomPointsInPixel(pIJ, ry, rx, n);
			// List<Ray> rays = new LinkedList<>();
			// for (Point3D point : points) {
			// 	rays.add(new Ray(p0, point.subtract(p0)));
			// }
			// return rays; // create and return the ray
		}
		return List.of(ray); // create and return the ray
	}
	
//	/**
//	 * generate a square black board with 5 points 
//	 * (one in the center and 4 in the vertices of the
//	 *  square blocked in the rectangle shaped pixel) 
//	 * @param nX number of pixels in the right-left axis
//	 * @param nY number of pixels in the up-down axis
//	 * @return a square black board with 5 points 
//	 * (one in the center and 4 in the vertices of the
//	 * square blocked in the rectangle shaped pixel)
//	 */
//	public BlackBoard getSempleBlackBoard(int nX, int nY) {
//		double ry = height / nY; // the height of each pixel
//		double rx = width / nX; // the width of each pixel
//		double radius = rx > ry ? ry : rx;
//		
//		return BlackBoard.sempleSquare(rx > ry? ry : rx); // the square blocked in the rectangle shaped pixel
//	}	
//	
	/**
	 * generate a square black board with n points 
	 * randomly spread on the board
	 * @param nX number of pixels in the right-left axis
	 * @param nY number of pixels in the up-down axis
	 * @param n
	 * @return a square black board with n points 
	 * randomly spread on the board
	 */
	public List<Ray> constructBeamThroughPixel(BlackBoard bb, int nX, int nY, int i, int j) {
		double ry = height / nY; // the height of each pixel
		double rx = width / nX; // the width of each pixel
		bb.setHeight(ry).setWidth(rx);
		Ray mainRay = constructRayThroughPixel(nX, nY, i, j);
		//calc distance
		double a = (j + 0.5) * width;
		double b = (i + 0.5) * height;
		double distance = Math.sqrt(a*a + b*b + dis*dis);
		return mainRay.createBeam(bb, distance, vUp, vRight);
	}
	
	/**
	 * calculates the center point in the pixel
	 * @param nX number of pixels in the right-left axis
	 * @param nY number of pixels in the up-down axis
	 * @param j  the column of the desired pixel
	 * @param i  the row of the desired pixel
	 * @return the center point in the pixel
	 */
	private Point3D calcCenter(int nX, int nY, int j, int i) {
		double ry = height / nY; // the height of each pixel
		double rx = width / nX; // the width of each pixel
		Point3D pCenter = p0.add(vTo.scale(dis)); // the center of the view plane
		double yi = ((nY - 1) / 2d - i) * ry; // the distance from the center of the view plane to the middle of the
												// pixel in the y axis
		double xj = (j - (nX - 1) / 2d) * rx; // the distance from the center of the view plane to the middle of the
												// pixel in the y axis
		Point3D pIJ = pCenter;
		if (yi != 0) { // to prevent a creation of a zero vector
			pIJ = pIJ.add(vUp.scale(yi));
		}
		if (xj != 0) { // to prevent a creation of a zero vector
			pIJ = pIJ.add(vRight.scale(xj));
		}
		return pIJ;
	}

	// /**
	//  * generate a list of random points in a pixel
	//  * @param center the point at the center of the pixel
	//  * @param height the height of the pixel
	//  * @param width the width of the pixel
	//  * @param n the number of points to generate
	//  * @return a list of 'n*n' points in the pixel
	//  */
	// private List<Point3D> randomPointsInPixel(Point3D center, double height, double width, int n){
	// 	double supHeight = height/(n-1);
	// 	double supWidth = width/(n-1);
	// 	List<Point3D> points = new LinkedList<>();
	// 	double halfH = height/2 , halfW = width/2;
	// 	for (double i = -halfW; i < halfW; i+=supWidth) {
	// 		for (double j = -halfH; j < halfH; j+=supHeight) {
	// 			double rH = ThreadLocalRandom.current().nextDouble(i - supHeight/2, i + supHeight) ;
	// 			double rW = ThreadLocalRandom.current().nextDouble(j - supWidth/2, j + supWidth) ;
	// 			Point3D pIJ = center;
	// 			if (rH != 0) { // to prevent a creation of a zero vector
	// 				pIJ = pIJ.add(vUp.scale(rH));
	// 			}
	// 			if (rW != 0) { // to prevent a creation of a zero vector
	// 				pIJ = pIJ.add(vRight.scale(rW));
	// 			}
	// 			points.add(pIJ);
	// 		}
	// 	}
	// 	return points;
	// }

	// /**
	//  * get sample of 4 points in the pixel
 	//  * @param center the point at the center of the pixel
	//  * @param height the height of the pixel
	//  * @param width the width of the pixel
	//  * @return list of 5 points in the pixel (the center included) 
	//  */
	// private List<Point3D> samplePoints(Point3D center, double height, double width){
	// 	double thirdH = height/3 , thirdW = width/3;
	// 	return List.of(
	// 		center.add(vUp.scale(thirdH).add(vRight.scale(thirdW))),
	// 		center.add(vUp.scale(-thirdH).add(vRight.scale(thirdW))),
	// 		center.add(vUp.scale(thirdH).add(vRight.scale(-thirdW))),
	// 		center.add(vUp.scale(-thirdH).add(vRight.scale(-thirdW)))
	// 	);
	// }
	/**
	 * set a new position for the camera, keeps the vUp vector around the axis y
	 * positive direction
	 * 
	 * @param pos    the position point
	 * @param target the target point
	 * @return this camera
	 */
	public Camera setPositionAndTarget(Point3D pos, Point3D target) {
		this.p0 = pos;
		vTo = target.subtract(pos).normalize();
		try {
			vRight = vTo.crossProduct(Vector.Y).normalize();
			vUp = vRight.crossProduct(vTo).normalize();
		} catch (IllegalArgumentException e) {
			// Camera is codirected with Y axis
			vUp = Vector.Z;
			vRight = vTo.crossProduct(vUp);
		}
		return this;
	}

	/**
	 * get the location of the camera
	 * 
	 * @return the location of the camera
	 */
	public Point3D getP0() {
		return p0;
	}

	/**
	 * get the direction vector from the top of the camera
	 * 
	 * @return the direction vector from the top of the camera
	 */
	public Vector getvUp() {
		return vUp;
	}

	/**
	 * get the direction vector of the camera
	 * 
	 * @return the direction vector of the camera
	 */
	public Vector getvTo() {
		return vTo;
	}

	/**
	 * get the direction vector from the Right side of the camera
	 * 
	 * @return the direction vector from the Right side of the camera
	 */
	public Vector getvRight() {
		return vRight;
	}

	/**
	 * get the width of the view plane
	 * 
	 * @return the width of the view plane
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * get the height of the view plane
	 * 
	 * @return the height of the view plane
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * get the view plane distance from the camera
	 * 
	 * @return the view plane distance from the camera
	 */
	public double getDis() {
		return dis;
	}
	
	/**
	 * generate the ray that start from the camera and go trough the i,j pixel in
	 * the view plane
	 * @param nX number of pixels in the right-left axis
	 * @param nY number of pixels in the up-down axis
	 * @param j  the column of the desired pixel
	 * @param i  the row of the desired pixel
	 * @return a ray from the camera that pass trough the desired pixel
	 */
	public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
		return constructRaysThroughPixel(nX, nY, j, i, 1).get(0);
	// 	double ry = height / nY; // the height of each pixel
	// 	double rx = width / nX; // the width of each pixel

	// 	Point3D pCenter = p0.add(vTo.scale(dis)); // the center of the view plane
	// 	double yi = ((nY - 1) / 2d - i) * ry; // the distance from the center of the view plane to the middle of the
	// 											// pixel in the y axis
	// 	double xj = (j - (nX - 1) / 2d) * rx; // the distance from the center of the view plane to the middle of the
	// 											// pixel in the y axis
	// 	Point3D pIJ = pCenter;
	// 	if (yi != 0) { // to prevent a creation of a zero vector
	// 		pIJ = pIJ.add(vUp.scale(yi));
	// 	}
	// 	if (xj != 0) { // to prevent a creation of a zero vector
	// 		pIJ = pIJ.add(vRight.scale(xj));
	// 	}
		
	// 	return new Ray(p0, pIJ.subtract(p0)); // create and return the ray
	 }

	
}
