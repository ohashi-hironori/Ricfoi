package jp.gr.java_conf.ricfoi.type;

public final class VFace {

	private Vector[] _points;

	public VFace(Vector u, Vector v, Vector w) {
    	_points = new Vector[3];
    	_points[0]=u;
    	_points[1]=v;
    	_points[2]=w;
    }

	public Vector[] getPoints() {
		return _points;
	}

	public Vector getPoint(int i) {
		return _points[i];
	}

	public void setPoint(int i, Vector point) {
		_points[i] = point;
	}
}
