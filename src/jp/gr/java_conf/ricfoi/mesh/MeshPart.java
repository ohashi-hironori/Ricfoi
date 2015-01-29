package jp.gr.java_conf.ricfoi.mesh;

import java.util.Enumeration;
import java.util.Vector;

import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.tree.Stem;
import jp.gr.java_conf.ricfoi.type.Face;
import jp.gr.java_conf.ricfoi.type.VFace;

public class MeshPart extends Vector<Object> {
	private static final long serialVersionUID = 1L;

	Stem stem;
	boolean useNormals;
	boolean useQuads;
	
	private class VertexEnumerator implements Enumeration<Object> {
		private Enumeration<?> sections;
		private Enumeration<?> sectionVertices;
		private boolean UVVertices;
		
		public VertexEnumerator(boolean uv) {
			UVVertices = uv;
			sections = elements();
			
			// ignore root point
			sections.nextElement();
			sectionVertices = ((MeshSection)sections.nextElement()).allVertices(UVVertices);
		}
		
		@Override
		public boolean hasMoreElements() {
			if (! sectionVertices.hasMoreElements() && sections.hasMoreElements()) {
				sectionVertices = ((MeshSection)sections.nextElement()).allVertices(UVVertices);
			}
			return sectionVertices.hasMoreElements();
		}
		
		@Override
		public Object nextElement() {
			if (! sectionVertices.hasMoreElements() && sections.hasMoreElements()) {
				sectionVertices = ((MeshSection)sections.nextElement()).allVertices(UVVertices);
			}
			return sectionVertices.nextElement();
		}
	}

	private class FaceEnumerator implements Enumeration<Object> {
		private Enumeration<?> sections;
		private Enumeration<?> sectionFaces;
		private MeshSection section;
		private boolean UVFaces;
		private boolean useQuads;
		private int startIndex;
//		private int sectionSize;
		
		public FaceEnumerator(Mesh mesh, int startInx, boolean uv, boolean quads) {
			UVFaces = uv;
			startIndex = startInx;
			useQuads = quads;
			sections = elements();
			
			// ignore root point
//			MeshSection sec = (MeshSection)sections.nextElement();
			
			// if it's a clone calculate a vertex offset
			// finding the corresponding segment in the parent stem's mesh
			int uvVertexOffset=0; 
			if (uv && stem.isClone()) {
				MeshPart mp = ((MeshPart)mesh.elementAt(mesh.firstMeshPart[stem.getLevel()]));
				
				MeshSection ms = ((MeshSection)mp.elementAt(1)); // ignore root vertex
				
				for (int i=0; i<stem.getCloneSectionOffset(); i++) {
					uvVertexOffset += ms.size()==1? 1 : ms.size()+1;
					ms = ms.next;
				}
				
				startIndex += uvVertexOffset;
			}
			
			nextSection(true);
		}
		
		private void nextSection(boolean firstSection) {
			if (! firstSection) {
				if (UVFaces) 
					startIndex += section.size()==1? 1 : section.size()+1;
				else
					startIndex += section.size();
			}
			
			section = (MeshSection)sections.nextElement(); 
			sectionFaces = section.allFaces(startIndex,UVFaces,useQuads);
		}
		
		@Override
		public boolean hasMoreElements() {
			if (! sectionFaces.hasMoreElements() && sections.hasMoreElements()) {
				nextSection(false);
			}
			return sectionFaces.hasMoreElements();
		}
		
		@Override
		public Object nextElement() {
			if (! sectionFaces.hasMoreElements() && sections.hasMoreElements()) {
				nextSection(false);
			}
			return sectionFaces.nextElement();
		}
	}
	
	
	public MeshPart(Stem stem, boolean useNormals, boolean useQuads) { 
		// FIXME normals not yet used,
		// other mesh output format needed if theire are
		// less normals then vertices
		this.useNormals = useNormals;
		this.useQuads = useQuads;
		this.stem = stem;
	}
	
	public Stem getStem() {
		return stem;
	}
	
	public String getTreePosition() {
		return stem.getTreePosition();
	}
	
	public int getLevel() {
		return stem.getLevel();
	}
	
	/**
	 * Adds a mesh section to the mesh part.
	 * 
	 * @param section
	 */
	public void addSection(MeshSection section) {
		
		if (size() > 0) {
			// connect section with last of sections
			((MeshSection)lastElement()).next = section;
			section.previous = (MeshSection)lastElement();
		}
		
		addElement(section);
	}
	
	@SuppressWarnings("rawtypes")
	public Enumeration allVertices(boolean UVVertices) {
		return new VertexEnumerator(UVVertices);
	}
	
	@SuppressWarnings("rawtypes")
	public Enumeration allFaces(Mesh mesh, int startIndex, boolean UVFaces) {
		return new FaceEnumerator(mesh,startIndex,UVFaces,useQuads);
	}
	
	
	/**
	 * Sets the normals in all mesh sections
	 * 
	 */
//	public void setNormals(boolean checkNull) {
	public void setNormals() {
		// normals for begin and end point (first and last section)
		// are set to a z-Vector by MeshPartCreator
//		if (checkNull) {
//			MeshSection s = ((MeshSection)elementAt(0));
//			for (int i=0; i<s.size(); i++) {
//				if (((Vertex)s.elementAt(i)).getNormal() == null)
//					Console.errorOutput("No normal set for lower section of mesh part of stem "+stem.getTreePosition());
//			}
//					
//			s = ((MeshSection)elementAt(size()-1));
//			for (int i=0; i<s.size(); i++) {
//				if (((Vertex)s.elementAt(i)).getNormal() == null)
//					Console.errorOutput("No normal set for upper section of mesh part of stem "+stem.getTreePosition());
//			}
//
//		}
//		
		if (size()>1) {
			((MeshSection)elementAt(1)).setNormalsUp();
			for (int i=2; i<size()-1; i++) {
				((MeshSection)elementAt(i)).setNormalsUpDown();
			}
		} else {
			Ricfoi.logger.warning("degnerated MeshPart with only "+size()+" sections at tree position "+stem.getTreePosition()+"."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		
	}
	
	
	
	/**
	 * @return the number of all meshpoints of all sections
	 */
	public int vertexCount() {
		int cnt=0;
		
		for (int i = 1; i<size(); i++) {
			cnt += ((MeshSection)elementAt(i)).size();
		}
		return cnt;
	}
	
	/**
	 * @return the number of uv vectors for the mesh part
	 */
	public int uvCount() {
		int cnt=0;
		
		for (int i = 1; i<size(); i++) {
			cnt += ((MeshSection)elementAt(i)).size()==1 ?
					1 : ((MeshSection)elementAt(i)).size()+1;
		}
		return cnt;
	}
	
	
	/**
	 * @return the number of faces, that have to be created - povray wants 
	 * to know this before the faces itself
	 */
	public int faceCount()  {
		int cnt = 0;
		for (int i=1; i<size()-1; i++) {
			int c_i = ((MeshSection)elementAt(i)).size();
			int c_i1 = ((MeshSection)elementAt(i+1)).size();
			
			if (c_i != c_i1) {
				cnt += Math.max(c_i,c_i1);
			} else if (c_i > 1) {
				cnt += 2 * c_i;
			}
		}
		return cnt;
	}
	
	/**
	 * @param inx
	 * @param section
	 * @return the triangles between a section and the next section.
	 * @throws MeshException
	 */
	public Vector<Face> faces(long inx, MeshSection section) throws MeshException {
		MeshSection next = section.next;
		Vector<Face> faces = new Vector<>();
		
		if (section.size() ==1 && next.size() == 1) {
			// normaly this shouldn't occur, only for very small radius?
			// I should warn about this
			Ricfoi.logger.warning("two adjacent mesh sections with only one point."); //$NON-NLS-1$
			return faces;
		}
		
		// if it's the first section (root vertex of the stem)
		// make triangles to fill the base section
		if (section.isFirst()) {
			for (int i=1; i<next.size()-1; i++) {
				faces.addElement(new Face(inx,inx+i,inx+i+1));
			}
		}

		// if the section has only one vertex, draw triangles
		// to every point of the next secion
		else if (section.size() == 1) {
			for (int i=0; i<next.size(); i++) {
				faces.addElement(new Face(inx,inx+1+i,inx+1+(i+1)%next.size()));
			}
			
		} else if (next.size() == 1) {
			long ninx = inx+section.size();
			for (int i=0; i<section.size(); i++) {
				faces.addElement(new Face(inx+i,ninx,inx+(i+1)%section.size()));
			}
			
		} else { // section and next must have same point_cnt>1!!!
			long ninx = inx+section.size();
			if (section.size() != next.size()) {
				throw new MeshException("vertice numbers of two sections differ ("+inx+","+ninx+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			for (int i=0; i<section.size(); i++) {
				if (useQuads) {
					faces.addElement(new Face(inx+i,ninx+i, ninx+(i+1)%next.size(),inx+(i+1)%section.size()));
				} else {
					faces.addElement(new Face(inx+i,ninx+i,inx+(i+1)%section.size()));
					faces.addElement(new Face(inx+(i+1)%section.size(),ninx+i, ninx+(i+1)%next.size()));
				}
			}
		}
		
		return faces;
	}
	

	/**
	 * @param section
	 * @return the triangles between a section and the next section.
	 * @throws MeshException
	 */
	@SuppressWarnings("static-method")
	public Vector<VFace> vFaces(MeshSection section) throws MeshException {
		MeshSection next = section.next;
		Vector<VFace> faces = new Vector<>();
		
		if (section.size() ==1 && next.size() == 1) {
			// normaly this shouldn't occur, only for very small radius?
			// I should warn about this
			Ricfoi.logger.warning("two adjacent mesh sections with only one point."); //$NON-NLS-1$
			return faces;
		}
		
		// if it's the first section (root vertex of the stem)
		// make triangles to fill the base section
		if (section.isFirst()) {
			for (int i=1; i<next.size()-1; i++) {
				faces.addElement(new VFace(
							next.pointAt(0),
							next.pointAt(i),
							next.pointAt(i+1)));
			}
		}		
		
		else if (section.size() == 1) {
			for (int i=0; i<next.size(); i++) {
				faces.addElement(new VFace(
							section.pointAt(0),
							next.pointAt(i),
// FIXME: this %next.size should be handled in MeshSection.pointAt,
// this would be easier to use							
							next.pointAt((i+1)%next.size())));
			}
		} else if (next.size() == 1) {
			for (int i=0; i<section.size(); i++) {
				faces.addElement(new VFace(
						section.pointAt(i),
						next.pointAt(0),
						section.pointAt((i+1)%section.size())
						));
			}
		} else { // section and next must have same point_cnt>1!!!
			if (section.size() != next.size()) {
				throw new MeshException("vertice numbers of two sections differ ("+section.size()+","+next.size()+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			for (int i=0; i<section.size(); i++) {
				faces.addElement(new VFace(
						section.pointAt(i),
						next.pointAt(i),
						section.pointAt((i+1)%section.size())
						));
				faces.addElement(new VFace(
						section.pointAt((i+1)%section.size()),
						next.pointAt(i),
						next.pointAt((i+1)%next.size())));
			}
		}
		return faces;
	}

	
	/**
	 * @param inx
	 * @param section
	 * @return the texture's uv-coordinates of the triangles between a section and the next section.
	 * @throws MeshException
	 */
	public Vector<Face> uvFaces(long l, MeshSection section, Mesh mesh) throws MeshException {
		long inx = l;
		MeshSection next = section.next;
		Vector<Face> faces = new Vector<>();
		
		if (section.size() ==1 && next.size() == 1) {
			// normaly this shouldn't occur, only for very small radius?
			// I should warn about this
			Ricfoi.logger.warning("two adjacent mesh sections with only one point."); //$NON-NLS-1$
			return faces;
		}
		

		// if it's a clone calculate a vertex offset
		// finding the corresponding segment in the parent stem's mesh
		int uvVertexOffset=0;
		
		if (stem.isClone()) {
			MeshPart mp = ((MeshPart)mesh.elementAt(mesh.firstMeshPart[stem.getLevel()]));
			MeshSection ms = ((MeshSection)mp.elementAt(1)); // ignore root vertex
					
			for (int i=0; i<stem.getCloneSectionOffset(); i++) {
				
//			for (MeshSection ms=((MeshSection)mp.elementAt(1)); // ignore root vertex
//				ms.next.segment.getIndex() < section.segment.getIndex();
//				ms = ms.next) {
				
				uvVertexOffset += ms.size()==1? 1 : ms.size()+1;
				ms = ms.next;
			}
			
		}

		long ninx; // start index of next section

		
		// first section of clone starts at the bottom of the texture
		// but all following are raised to the corresponding segment
		// of the parent stem
		if (section.size()>1) {
			ninx = inx+section.size()+1+uvVertexOffset;
		} else {
			ninx = 1+uvVertexOffset;
		}
		if (section.isFirst()) {
			inx += uvVertexOffset;
		} 

		// if it's the first section (root vertex of the stem)
		// make triangles to fill the base section
		if (section.isFirst()) {
			for (int i=1; i<next.size()-1; i++) {
				faces.addElement(new Face(inx,inx+i,inx+i+1));
			}
		}
		
		else if (section.size() == 1) {
			for (int i=0; i<next.size(); i++) {
				faces.addElement(new Face(inx,inx+ninx+i,inx+ninx+(i+1)));
			}
		} 
		
		else if (next.size() == 1) {
			for (int i=0; i<section.size(); i++) {
				faces.addElement(new Face(inx+i,ninx,inx+(i+1)));
			}
		} 
		
		else { // section and next must have same point_cnt>1!!!
			if (section.size() != next.size()) {
				throw new MeshException("vertex numbers of two sections differ ("+inx+","+ninx+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			for (int i=0; i<section.size(); i++) {
				if (useQuads) {
					faces.addElement(new Face(inx+i,ninx+i,ninx+(i+1),inx+(i+1)));
				} else {
					faces.addElement(new Face(inx+i,ninx+i,inx+(i+1)));
					faces.addElement(new Face(inx+(i+1),ninx+i,
							ninx+(i+1)));
				}
			}
		}
		
		return faces;
	}
}
