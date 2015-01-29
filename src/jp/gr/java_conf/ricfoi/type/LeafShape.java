package jp.gr.java_conf.ricfoi.type;


public abstract class LeafShape {

	private Vertex[] _vertices;
	private Face[] _faces;
	
	private double _length=1;
	private double _width=1;
	private double _stemLen=0.5;
	private boolean _useQuads;
	
	public LeafShape(double length, double width, double stemLen, boolean useQuads) {
		_length = length;
		_width = width;
		_stemLen = stemLen;
		setUseQuads(useQuads);
	}
	
	void setPoint(int i, double x, double y, double z) {
		Vector point = new Vector(x*_width,y*_width,(_stemLen+z)*_length);
		UVVector uv = new UVVector(x+0.5,z);
		if (_vertices[i] == null) {
			// FIXME: add uv-mapping
			_vertices[i] = new Vertex(point,null,uv);
		} else {
			_vertices[i].setPoint(point);
		}
	}

	/**
	 * @return _useQuads
	 */
	public boolean isUseQuads() {
		return _useQuads;
	}

	int getVertexCount() {
		return _vertices.length;
	}

	/**
	 * @param _useQuads セットする _useQuads
	 */
	public void setUseQuads(boolean useQuads) {
		_useQuads = useQuads;
	}

	public Vertex[] getVertices() {
		return _vertices;
	}

	public void setVertices(Vertex[] vertices) {
		_vertices = vertices;
	}
	
	public Vertex getVertex(int i) {
		return _vertices[i];
	}

	int getFaceCount() {
		return _faces.length;
	}

	public Face[] getFaces() {
		return _faces;
	}

	public void setFaces(Face[] faces) {
		_faces = faces;
	}
	
	public Face getFace(int i) {
		return _faces[i];
	}
	
	public void setFace(int i, Face face) {
		_faces[i] = face;
	}

}
