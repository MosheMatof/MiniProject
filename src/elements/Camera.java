/**
 * 
 */
package elements;

import java.util.List;

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
	 * camera constructor by position and target
	 * 
	 * @param p0  the location point of the camera
	 * @param vUp the direction vector from the top of the camera
	 * @param vTo the direction vector of the camera
	 */
	public Camera(Point3D pos, Point3D target) {
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

	/**
	 * generate a beam of rays that start at p0 and goes through the pixel
	 * 
	 * @param bb the black board for the beam
	 * @param nX number of pixels in the right-left axis
	 * @param nY number of pixels in the up-down axis
	 * @param j  the column of the desired pixel
	 * @param i  the row of the desired pixel
	 * @return a beam of rays that start at p0 and goes through the pixel
	 */
	public List<Ray> constructBeamThroughPixel(BlackBoard bb, int nX, int nY, int j, int i) {

		double ry = height / nY; // the height of each pixel
		double rx = width / nX; // the width of each pixel
		bb.setHeight(ry).setWidth(rx);
		Point3D center = calcCenter(nX, nY, j, i);
		Ray mainRay = new Ray(p0, center.subtract(p0));
		// calc distance
		return mainRay.createBeam(bb, center, vUp, vRight);
	}

	/**
	 * calculates the center point in the pixel
	 * 
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
			// Camera is co-directed with Y axis
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
	 * 
	 * @param nX number of pixels in the right-left axis
	 * @param nY number of pixels in the up-down axis
	 * @param j  the column of the desired pixel
	 * @param i  the row of the desired pixel
	 * @return a ray from the camera that pass trough the desired pixel
	 */
	public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
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

		return new Ray(p0, pIJ.subtract(p0)); // create and return the ray
	}

}
