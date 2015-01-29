package jp.gr.java_conf.ricfoi.type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.gr.java_conf.ricfoi.Constant;


public class LeafMesh {

	private LeafShape _shape;

	public LeafMesh(String leafShape, double length, double width, double stemLen, boolean useQuads) {
		Pattern pattern = Pattern.compile("disc(\\d*)");
		Matcher m = pattern.matcher(leafShape);
		// disc shape
		if (m.matches()) {
			// FIXME: given "disc" without a number, the face count could be dependent from the smooth value
			int facecnt = 6;
			if (! m.group(1).equals(Constant.ZS)) {
				facecnt = Integer.parseInt(m.group(1));
			}
			_shape = new DiscShape(facecnt,length,width,stemLen,useQuads);
		} else if (leafShape.equals("square")) {
			_shape = new SquareShape(length,width,stemLen,useQuads);
		} else if (leafShape.equals("sphere")) {
			_shape = new SphereShape(length,width,stemLen);
		} else {
			// test other shapes like  palm here any other leaf shape, like "0" a.s.o. - use normal disc
			
			// FIXME: given "disc" without a number, the face count could be dependent from the smooth value
			int facecnt = 6;
			_shape = new DiscShape(facecnt,length,width,stemLen,useQuads);
		}
	}

	public boolean isFlat() {
		return (_shape.getClass() != SphereShape.class);
	}

	/**
	 * Returns the i-th vertex.
	 * 
	 * @param i
	 * @return i-th vertex
	 */
	public Vertex shapeVertexAt(int i) {
		return _shape.getVertex(i);
	}

	/**
	 * Returns the i-th uv-vector.
	 * 
	 * @param i
	 * @return i-th uv-vector
	 */
	public UVVector shapeUVAt(int i) {
		return _shape.getVertex(i).getUV();
	}

	/**
	 * Returns the i-th face (triangle).
	 * 
	 * @param i
	 * @return i-th face (triangle)
	 */
	public Face shapeFaceAt(int i) {
		return _shape.getFace(i);
	}

	/**
	 * Returns the number of vertices the leaf mesh consist of.
	 * 
	 * @return number of vertices the leaf mesh consist of
	 */
	public int getShapeVertexCount() {
		return _shape.getVertexCount();
	}

	/**
	 * Returns the number ov faces the leaf mesh consists of.
	 * 
	 * @return number ov faces the leaf mesh consists of
	 */
	public int getShapeFaceCount() {
		return _shape.getFaceCount();
	}

}
