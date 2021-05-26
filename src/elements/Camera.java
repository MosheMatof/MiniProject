/**
 * 
 */
package elements;

import primitives.*;

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
	 * @param width the width of the view plane
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
	 * computes the ray that start from the camera and go trough the i,j pixel in
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
		Vector dir = pIJ.subtract(p0); // calculate the direction vector of the ray
		return new Ray(p0, dir); // create and return the ray
	}

	/**
	 * set a new position for the camera, keeps the vUp vector around the axis y positive direction
	 * @param pos the position point
	 * @param target the target point
	 * @return this camera
	 */
	public Camera setPosition(Point3D pos,Point3D target) {
		this.p0 = pos;
		vTo = target.subtract(pos).normalize();
		//produce the vUp vector
		Vector yN = new Vector(0,1,0);	
		try {
			Vector t1 = new Vector(1,0,0).crossProduct(vTo).normalize();
			if(t1.dotProduct(yN) < 0)
				t1.scale(-1);
			Vector t2 = new Vector(0,0,-1).crossProduct(vTo).normalize();
			if(t1.dotProduct(yN) < 0)
				t1.scale(-1);	
			vUp = t1.add(t2).normalize();
		} catch (Exception e) { // the vTo vector is parallel to the x-z plain
			vUp = new Vector(0,1,0);
		}
			
		// if the vectors are orthogonal -> generates the vRight
				if (Util.isZero(vUp.dotProduct(vTo))) {
					this.vRight = vTo.crossProduct(vUp).normalize();
				} else {
					throw new IllegalArgumentException("the vTo and vUp aren't orthogonal");
				}
		return this;
	}
	/**
	 * get the location of the camera
	 * @return the location of the camera
	 */
	public Point3D getP0() {
		return p0;
	}

	/**
	 * get the direction vector from the top of the camera
	 * @return the direction vector from the top of the camera
	 */
	public Vector getvUp() {
		return vUp;
	}

	/**
	 * get the direction vector of the camera
	 * @return the direction vector of the camera
	 */
	public Vector getvTo() {
		return vTo;
	}

	/**
	 * get the direction vector from the Right side of the camera
	 * @return the direction vector from the Right side of the camera
	 */
	public Vector getvRight() {
		return vRight;
	}

	/**
	 * get the width of the view plane
	 * @return the width of the view plane
	 */
	public double getWidth() {
		return width;
	}

	/** 
	 * get the height of the view plane
	 * @return the height of the view plane
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * get the view plane distance from the camera
	 * @return the view plane distance from the camera
	 */
	public double getDis() {
		return dis;
	}

}
