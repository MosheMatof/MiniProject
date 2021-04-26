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
	 * @param p0 the location point of the camera
	 * @param vUp the up direction of the camera 
	 * @param vTo the up direction of the camera
	 */
	public Camera(Point3D p0, Vector vUp, Vector vTo) {
		super();
		this.p0 = p0;
		this.vUp = vUp.normalize();
		this.vTo = vTo.normalize();
		//if the vectors are orthogonal -> generates the right vector
		if (vUp.dotProduct(vTo) == 0) {
			this.vRight = vTo.crossProduct(vUp);//need to normalize?
		}
		else {
			throw new IllegalArgumentException("the vTo and vUp aren't orthogonal");
		}
		
	}
	/**
	 * setting the view plane size of the camera
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
	 * @param distance the distance of the view plane from the camera
	 * @return this camera
	 */
	public Camera setDistance(double distance) {
		this.dis = distance;
		return this;
	}
	/**
	 * computes the ray that start from the camera and go trough the i,j pixel in the view plane
	 * @param nX number of pixels in the right-left axis 
	 * @param nY number of pixels in the up-down axis
	 * @param j the column of the desired pixel
	 * @param i the row of the desired pixel
	 * @return a ray from the camera that pass trough the desired pixel 
	 */
	public Ray constructRayThroughPixel(int nX, int nY, int j, int i){
		double ry = height/nY;
		double rx = width/nX;
		Point3D pc = p0.add(vTo.scale(dis));
		double yi = ((nY-1)/2 - i)*ry;
		double xj = (j - (nX-1)/2) * rx;
		Point3D p = pc;
		if(yi != 0) {
			p = p.add(vUp.scale(yi));
		}
		if(xj != 0) {
			p = p.add(vRight.scale(xj));
		}
		Vector dir = p.subtract(p0);
		return new Ray(p0, dir);
	}
	
	public Point3D getP0() {
		return p0;
	}
	public Vector getvUp() {
		return vUp;
	}
	public Vector getvTo() {
		return vTo;
	}
	public Vector getvRight() {
		return vRight;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	public double getDis() {
		return dis;
	}
	
}
