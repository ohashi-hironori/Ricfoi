package jp.gr.java_conf.ricfoi.type;

public final class Face {

    private long [] _points;

    public Face(long i, long j, long k) {
    	_points = new long[3];
    	_points[0]=i;
    	_points[1]=j;
    	_points[2]=k;
    }
    
    public Face(long i, long j, long k, long m) {
		_points = new long[4];
		_points[0]=i;
		_points[1]=j;
		_points[2]=k;
		_points[3]=m;
    }

	public long[] getPoints() {
		return _points;
	}

	public void setPoints(long[] points) {
		_points = points;
	}

	public long getPoint(int index) {
		return _points[index];
	}

	public void setPoint(int index, long point) {
		_points[index] = point;
	}

}
