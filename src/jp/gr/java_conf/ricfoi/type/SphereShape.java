package jp.gr.java_conf.ricfoi.type;

public class SphereShape extends LeafShape {

	private Face[] _faces;

	public SphereShape(double length, double width, double stemLen) {
		super(length, width, stemLen, false);
		double s = (Math.sqrt(5)-1)/2*Math.sqrt(2/(5-Math.sqrt(5))) / 2; 
		// half edge length so, that the vertices are at distance of 0.5 from the center
		double t = Math.sqrt(2/(5-Math.sqrt(5))) / 2;
		
		setVertices(new Vertex[12]);
		setPoint(0, 0,s,-t+0.5);
		setPoint(1, t,0,-s+0.5);
		setPoint(2, -s,t,0+0.5);
		setPoint(3, 0,s,t+0.5);
		setPoint(4, -t,0,-s+0.5);
		setPoint(5,-s,-t,0+0.5);
		setPoint(6, 0,-s,-t+0.5);
		setPoint(7, t,0,s+0.5);
		setPoint(8, s,t,0+0.5);
		setPoint(9, 0,-s,t+0.5);
		setPoint(10,-t,0,s+0.5);
		setPoint(11, s,-t,0+0.5);
		
		_faces = new Face[20];

		_faces[0] = new Face(0,1,6);
		_faces[1] = new Face(0,6,4);
		_faces[2] = new Face(1,8,7);
		_faces[3] = new Face(1,7,11);
		_faces[4] = new Face(2,3,0);
		_faces[5] = new Face(2,3,8);
		
		_faces[6] = new Face(3,9,7);
		_faces[7] = new Face(3,10,9);
		_faces[8] = new Face(4,10,2);
		_faces[9] = new Face(4,5,10);
		_faces[10] = new Face(5,6,11);
		_faces[11] = new Face(5,11,9);
		
		_faces[12] = new Face(0,8,1);
		_faces[13] = new Face(6,1,11);
		_faces[14] = new Face(6,5,4);
		_faces[15] = new Face(0,4,2);
		
		_faces[16] = new Face(7,8,3);
		_faces[17] = new Face(10,3,2);
		_faces[18] = new Face(10,5,9);
		_faces[19] = new Face(9,11,7);
	}

}
