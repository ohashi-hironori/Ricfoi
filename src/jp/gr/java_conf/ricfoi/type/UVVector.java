package jp.gr.java_conf.ricfoi.type;

public final class UVVector {

	private double _u;
	private double _v;

	public UVVector(double u, double v) {
		setU(u);
		setV(v);
	}

	/**
	 * @return u
	 */
	public double getU() {
		return _u;
	}

	/**
	 * @param u セットする u
	 */
	public void setU(double u) {
		_u = u;
	}

	/**
	 * @return v
	 */
	public double getV() {
		return _v;
	}

	/**
	 * @param v セットする v
	 */
	public void setV(double v) {
		_v = v;
	}

}
