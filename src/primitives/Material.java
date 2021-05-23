package primitives;

/**
 * the material of the surface of an object
 */
public class Material {
	/**
	 * defuse level
	 */
	public double kD = 0;
	/**
	 * specular level
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
	 * set this.kD to be 'kD'
	 * @param kD the kD to set
	 * @return it self
	 */
	public Material setKd(double kD) {
		this.kD = kD;
		return this;
	}

	/**
	 * set this.kS to be 'kS'
	 * @param kS the kS to set
	 * @return it self
	 */
	public Material setKs(double kS) {
		this.kS = kS;
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
	
}
