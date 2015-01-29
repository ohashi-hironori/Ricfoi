package jp.gr.java_conf.ricfoi.mesh;

import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.type.Face;
import jp.gr.java_conf.ricfoi.type.FloatFormat;
import jp.gr.java_conf.ricfoi.type.UVVector;
import jp.gr.java_conf.ricfoi.type.Vector;
import jp.gr.java_conf.ricfoi.type.Vertex;

public class MeshSection extends java.util.Vector<Object> {
	private static final long serialVersionUID = 1L;
	
	public MeshSection previous;
	public MeshSection next;
	public double mapV; // v-coordinate of uv-map
	
	private class UVVertexEnumerator implements Enumeration<Object> {
		private int i;
		
		public UVVertexEnumerator() {
			i=0;
		}
		
		@Override
		public boolean hasMoreElements() {
			return (i==0 && size()==1) || (i<=size() && size()>1);
		}
		
		@Override
		public Object nextElement() {
			if (i<size())
				return ((Vertex)elementAt(i++)).getUV();
			else if (i==size() && size()>1) {
				i++;
				return new UVVector(1.0,mapV);
			} else 
				throw new NoSuchElementException();
		}
	}
	
	private class FaceEnumerator implements Enumeration<Face> {
		private int i;
		private int ni;
		private int cnt_i;
		private int cnt_ni;
		private int inx;
		private int ninx;
		private boolean quads;
		private boolean uv;
		private Face face;

		public FaceEnumerator(int startIndex, boolean UVFaces, boolean useQuads) {
			if (next==null) return;
			
			i=0;
			ni=0;
			inx=startIndex;
			if (UVFaces) 
				ninx = inx + (size()==1? 1 : size()+1);
			else
				ninx=inx+size();
			quads = useQuads;
			uv = UVFaces;
			
			if (uv) {
				cnt_i = size()+1;
				cnt_ni = next.size()+1;
			} else {
				cnt_i = size();
				cnt_ni = next.size();
			}

			if (size() == 1 && next.size() == 1) {
				// normaly this shouldn't occur, only for very small radius?
				// I should warn about this
				Ricfoi.logger.warning("two adjacent mesh sections with only one point."); //$NON-NLS-1$
			}
		}
		
		@Override
		public boolean hasMoreElements() {
			return ! (next==null || 
					(size()==1 &&  ni >= next.size()) ||
					(next.size()==1 && i >= size()) ||
					(ni >= next.size() && i >= size()));
		}
		
		@Override
		public Face nextElement() {
			if (! hasMoreElements())
				throw new NoSuchElementException();
			
			if (quads && size()>1 && next.size()==size()) {
				face = new Face(inx+i,
						ninx+ni,
						ninx+(++ni)%cnt_ni,
						inx+(++i)%cnt_i);
			} else {
				if (i<=ni || next.size()==1) {
					face = new Face(inx+i,
							ninx+ni,
							inx+(++i)%cnt_i);
				} else {
					face = new Face(
							inx+i%cnt_i,
							ninx+ni,
							ninx+(++ni)%cnt_ni);
				}
			}
			
			return face;
		}
	}

	
	public MeshSection(int ptcnt, double v) {
		super(ptcnt);
		mapV = v;
	}
	
	/**
	 * Adds a point to the mesh section
	 * 
	 * @param pt
	 * @param mapU 
	 */
	public void addPoint(Vector pt, double mapU) {
		addElement(new Vertex(pt,null,new UVVector(mapU,mapV)));
	} 
	
	/**
	 * Returns the location point of the vertex i.
	 * 
	 * @param i
	 * @return location point of the vertex i
	 */
	public Vector pointAt(int i) {
		return ((Vertex)elementAt(i)).getPoint();
	}
	
	public Enumeration<Object> allVertices(boolean UVVertices) {
		if (UVVertices)
			return new UVVertexEnumerator();
		else
			return elements();
	}
	
	public Enumeration<Face> allFaces(int startIndex, boolean UVFaces, boolean useQuads) {
		return new FaceEnumerator(startIndex, UVFaces, useQuads);
	}
	
	/**
	 * Returns the texture's uv-coordinates of the point.
	 * 
	 * @param i
	 * @return texture's uv-coordinates of the point
	 */
	public UVVector uvAt(int i) {
		if (i<size())
			return ((Vertex)elementAt(i)).getUV();
		else
			return new UVVector(1.0,mapV);
	}
	
	public boolean isFirst() {
		return (previous==null);
	}
	
	public boolean isLast() {
		return (next==null);
	}
	
	/**
	 * Returns the number of faces between this section and the next one.
	 * 
	 * @param useQuads 
	 * @return number of faces between this section and the next one
	 */
	public int faceCount(boolean useQuads) {
		if (size() == 1) return next.size();
		else if (next.size()==1 || useQuads) return size();
		else return size()*2; // two triangles per vertice (quad)
	}
	
	
	/**
	 * Returns the normal of the vertex i.
	 * 
	 * @param i
	 * @return normal of the vertex i
	 * @throws Exception
	 */
	public Vector normalAt(int i) throws Exception {
		Vertex v = (Vertex)elementAt(i);
		if (v.getNormal() == null) throw new MeshException("Normal not set for point "+vectorStr(v.getPoint())); //$NON-NLS-1$
		return v.getNormal();
	}
	
	private static String vectorStr(Vector v) {
		NumberFormat fmt = FloatFormat.getInstance();
		return Constant.LT+fmt.format(v.getX())+Constant.COMMA+fmt.format(v.getZ())+Constant.COMMA+fmt.format(v.getY())+Constant.GT;
	}

	
	/**
	 * Returns the point number i.
	 * 
	 * @param i
	 * @return point number i
	 */
	public Vector here(int i) {
		
		return ((Vertex)elementAt(i)).getPoint();
	}
	
	/**
	 * Returns the point to the left of the point number i
	 * 
	 * @param i
	 * @return point to the left of the point number i
	 */
	public Vector left(int i) {
		return ((Vertex)elementAt((i-1+size())%size())).getPoint();
	}
	
	/**
	 * Returns the point to the right of the point number i
	 * 
	 * @param i
	 * @return point to the right of the point number i
	 */
	public Vector right(int i) {
		return ((Vertex)elementAt((i+1)%size())).getPoint();
	}
	
	/**
	 * Returns the point on top of the point number i (from the next section).
	 * The next section has the same number of points or only one point.
	 * @param i
	 * @return point on top of the point number i
	 */
	public Vector up(int i) {
		return ((Vertex)(next.elementAt(i%next.size()))).getPoint();
	}
	
	/**
	 * Returns the point below the point number i (from the previous section).
	 * The next section has the same number of points or only one point.
	 * @param i
	 * @return point below the point number i
	 */
	public Vector down(int i) {
		return ((Vertex)(previous.elementAt(i%previous.size()))).getPoint();
	}	  	  
	
	/**
	 * Returns the normal of the plane built by the vectors a-b and c-b
	 * 
	 * @param a  
	 * @param b 
	 * @param c
	 * @return normal of the plane built by the vectors a-b and c-b
	 */
	public static Vector normal(Vector a, Vector b, Vector c) {
		Vector u = (a.subtract(b)).normalize();
		Vector v = (c.subtract(b)).normalize();
		Vector norm = new Vector(u.getY()*v.getZ() - u.getZ()*v.getY(),
				u.getZ()*v.getX() - u.getX()*v.getZ(),
				u.getX()*v.getY() - u.getY()*v.getX()).normalize();
		if (Double.isNaN(norm.getX()) && Double.isNaN(norm.getY()) && Double.isNaN(norm.getZ())) {
			Ricfoi.logger.warning("invalid normal vector - stem radius too small?"); //$NON-NLS-1$
			norm = new Vector(0,0,1);
		}
		return norm;
	}
	
	/**
	 * Sets all normals to the vector vec
	 *
	 * @param vec
	 */
	public void setNormalsToVector(Vector vec) {
		for (int i=0; i<size(); i++) {
			((Vertex)elementAt(i)).setNormal(vec);
		}
	}
	
	/**
	 * Sets all normals to the average
	 * of the two left and right upper triangles
	 * 
	 */
	public void setNormalsUp() {
		for (int i=0; i<size(); i++) {
			((Vertex)elementAt(i)).setNormal(
				(normal(up(i),here(i),left(i)).add(
						normal(right(i),here(i),up(i)))).normalize());
		}
	}
	
	/**
	 * Sets all normals to the average
	 * of the two left and right lower triangles
	 * 
	 */
	public void setNormalsDown() {
		for (int i=0; i<size(); i++) {
			((Vertex)elementAt(i)).setNormal(
				(normal(down(i),here(i),right(i)).add(
						normal(left(i),here(i),down(i)))).normalize());
		}
	}
	
	/**
	 * Sets all normals to the average
	 * of the four left and right upper and lower triangles
	 */
	public void setNormalsUpDown() {
		for (int i=0; i<size(); i++) {
			((Vertex)elementAt(i)).setNormal(
				(normal(up(i),here(i),left(i)).add(
						normal(right(i),here(i),up(i))).add(
								normal(down(i),here(i),right(i))).add(
										normal(left(i),here(i),down(i)))).normalize());
		}
	}
}
