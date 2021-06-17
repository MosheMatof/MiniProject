package primitives;

/**
 * the material of the surface of an object
 */
public class Material {
	/**
	 * diffuse factor
	 */
	public double kD = 0;
	/**
	 * specular factor
	 */
	public double kS = 0;
	/**
	 * transparency level
	 */
	public double kT = 0;
	/**
	 * reflection level
	 */
	public double kR = 0;
	/**
	 * shininess level
	 */
	public int nShinines = 1;
	/**
	 * snow effect
	 */
	public boolean isSnow = false;
	
	/**
	 * setter for kS (diffuse factor)
	 * @param kD the kD to set
	 * @return it self
	 */
	public Material setKd(double kD) {
		
		this.kD = kD;
		return this;
	}

	/**
	 * setter for kS (specular factor)
	 * @param kS the kS to set
	 * @return it self
	 */
	public Material setKs(double kS) {
		this.kS = kS;
		return this;
	}

	/**
	 * setter for kT (transparency level)
	 * @param kT the kT to set
	 * @return it self 
	 */
	public Material setkT(double kT) {
		this.kT = kT;
		return this;
	}
	
	/**
	 * setter for kR (reflection level)
	 * @param kR the kR to set
	 * @return it self
	 */
	public Material setkR(double kR) {
		this.kR = kR;
		return this;
	}
	
	/**
	 * set this.nShinines to be 'nShinines'
	 * @param nShinines the nShinines to set
	 * @return it self
	 */
	public Material setShininess(int nShinines) {
		this.nShinines = nShinines;
		return this;
	}
	/**
	 * setter for isSnow (snow effect)
	 * @return it self
	 */
	public Material setSnow() {
		this.isSnow = true;
		return this;
	}
}
