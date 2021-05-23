package primitives;

/**
 * the material of the surface of an object
 */
public class Material {
	public double kD = 0, kS = 0;
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
