package primitives;

/**
 * the material of the surface of an object
 */
public class Material {
	public double kD = 0, kS = 0;
	int nShinines = 1;
	
	/**
	 * set this.kD to be 'kD'
	 * @param kD the kD to set
	 * @return it self
	 */
	public Material setkD(double kD) {
		this.kD = kD;
		return this;
	}

	/**
	 * set this.kS to be 'kS'
	 * @param kS the kS to set
	 * @return it self
	 */
	public Material setkS(double kS) {
		this.kS = kS;
		return this;
	}

	/**
	 * set this.nShinines to be 'nShinines'
	 * @param nShinines the nShinines to set
	 * return it self
	 */
	public void setnShinines(int nShinines) {
		this.nShinines = nShinines;
	}
	
	
}
