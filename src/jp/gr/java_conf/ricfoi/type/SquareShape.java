package jp.gr.java_conf.ricfoi.type;

public class SquareShape extends LeafShape {

	private Face[] _faces;

	public SquareShape(double length, double width, double stemLen, boolean useQuads) {
		super(length, width, stemLen, useQuads);
		
		setVertices(new Vertex[4]);
		setPoint(0, -0.5, 0, 0);
		setPoint(1, -0.5, 0, 1);
		setPoint(2, 0.5, 0, 1);
		setPoint(3, 0.5, 0, 0);
		
		if (isUseQuads()) {
			_faces = new Face[1];
			_faces[0] = new Face(0,3,2,1);
		} else {
			_faces = new Face[2];
			_faces[0] = new Face(0,2,1);
			_faces[1] = new Face(0,3,2);
		}
	}
	
	@Override
	public int getFaceCount() {
		return _faces.length;
	}

}
