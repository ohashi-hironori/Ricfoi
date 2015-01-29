package jp.gr.java_conf.ricfoi.type;


public class Vertex {

    private Vector _point;
	private Vector _normal;
	private UVVector _uv;

	public Vertex(Vector point, Vector normal, UVVector uv) {
    	setPoint(point);
    	setNormal(normal);
    	setUV(uv);
    }

	/**
	 * @return _point
	 */
	public Vector getPoint() {
		return _point;
	}

	/**
	 * @param _point セットする _point
	 */
	public void setPoint(Vector point) {
		_point = point;
	}

	/**
	 * @return _normal
	 */
	public Vector getNormal() {
		return _normal;
	}

	/**
	 * @param _normal セットする _normal
	 */
	public void setNormal(Vector normal) {
		_normal = normal;
	}

	/**
	 * @return _uv
	 */
	public UVVector getUV() {
		return _uv;
	}

	/**
	 * @param _uv セットする _uv
	 */
	public void setUV(UVVector uv) {
		_uv = uv;
	}

}
