package jp.gr.java_conf.ricfoi.mesh;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;

public class Mesh  extends Vector<MeshPart> {
	private static final long serialVersionUID = 1L;

	final boolean debugMesh = false;
	public int[] firstMeshPart; // first mesh part of each level 
		
	
	private class VertexEnumerator implements Enumeration<Object> {
		private Enumeration<?> parts;
		private Enumeration<?> partVertices;
		
		public VertexEnumerator() {
			parts = elements();
			
			partVertices = ((MeshPart)parts.nextElement()).allVertices(false);
		}
		
		@Override
		public boolean hasMoreElements() {
			if (! partVertices.hasMoreElements() && parts.hasMoreElements()) {
				partVertices = ((MeshPart)parts.nextElement()).allVertices(false);
			}
			return partVertices.hasMoreElements();
		}
		
		@Override
		public Object nextElement() {
			if (! partVertices.hasMoreElements() && parts.hasMoreElements()) {
				partVertices = ((MeshPart)parts.nextElement()).allVertices(false);
			}
			return partVertices.nextElement();
		}
	}

	private class UVVertexEnumerator implements Enumeration<Object> {
		private int level;
		private Enumeration<?> partVertices;
		
		public UVVertexEnumerator() {
			level=0;
			partVertices = ((MeshPart)elementAt(firstMeshPart[level])).allVertices(true);
		}
		
		@Override
		public boolean hasMoreElements() {
			if (! partVertices.hasMoreElements() && ++level<firstMeshPart.length && firstMeshPart[level]>0) {
				partVertices = ((MeshPart)elementAt(firstMeshPart[level])).allVertices(true);
			}
			return partVertices.hasMoreElements();
		}
		
		@Override
		public Object nextElement() {
			if (! partVertices.hasMoreElements() && ++level<firstMeshPart.length && firstMeshPart[level]>0) {
				partVertices = ((MeshPart)elementAt(firstMeshPart[level])).allVertices(true);
			}
			return partVertices.nextElement();
		}
	}
	
	
	private class PartEnumerator implements Enumeration<MeshPart> {
		private int level;
		private Enumeration<MeshPart> parts;
		private MeshPart part;
		
		public PartEnumerator(int stemLevel) {
			level = stemLevel;
			parts = elements();
			nextPart();
		}
		
		private void nextPart() {
			if (parts.hasMoreElements()) {
				part = (MeshPart)parts.nextElement();
				if (level>=0) {
					while (part.stem.getLevel() != level) {
						if (parts.hasMoreElements()) 
							part = (MeshPart)parts.nextElement();
						else {
							part = null;
							return;
						}
					}
				}
			}
			else part = null;
		}
		
		@Override
		public boolean hasMoreElements() {
			return part!=null;
		}
		
		@Override
		public MeshPart nextElement() {
			if (part != null) {
				MeshPart result = part;
				nextPart();
				return result;
			} else 
				throw new NoSuchElementException();
		}
	}

	private class FaceEnumerator implements Enumeration<Object> {
		//private boolean UVFaces;
		private Enumeration<?> parts;
		private Enumeration<?> partFaces;
		private MeshPart part;
		private boolean UVFaces;
		private int startIndex;
		private int level;
		private Mesh mesh;
		
		public FaceEnumerator(Mesh mesh, int startInx, boolean uv, int stemLevel) {
			UVFaces=uv;
			startIndex=startInx;
			level=stemLevel;
			parts = allParts(level);
			this.mesh = mesh;
			
			nextPart(true);
		}
		
		private void nextPart(boolean firstPart) {
			if (UVFaces) {
				part = (MeshPart)parts.nextElement();
//				startIndex += part.uvCount();
				startIndex = firstUVIndex(part.stem.getLevel());
			} else {
				if (! firstPart) startIndex += part.vertexCount();
				part = (MeshPart)parts.nextElement();
			}
			partFaces = part.allFaces(mesh,startIndex,UVFaces);
			
//			System.err.println("next part "+part.stem.getTreePosition());
		}
		
		@Override
		public boolean hasMoreElements() {
			if (! partFaces.hasMoreElements() && parts.hasMoreElements()) {
				nextPart(false);
			}
			return partFaces.hasMoreElements();
		}
		
		@Override
		public Object nextElement() {
			if (! partFaces.hasMoreElements() && parts.hasMoreElements()) {
				nextPart(false);
			}
			return partFaces.nextElement();
		}

	}	
	
	
	public Mesh(int levels) { 
		firstMeshPart = new int[levels];
		for (int i=0; i<levels; i++) {
			firstMeshPart[i]=-1;
		}
	}
	
	/**
	 * Adds a mesh part (i.e. a stem) to the mesh.
	 * 
	 * @param meshpart
	 */
	public void addMeshpart(MeshPart meshpart) {
		addElement(meshpart);
		if (firstMeshPart[meshpart.stem.getLevel()]<0) 
			firstMeshPart[meshpart.stem.getLevel()] = size()-1;
	}
	
	@SuppressWarnings("rawtypes")
	public Enumeration allVertices(boolean UVVertices) {
		if (UVVertices)
			return new UVVertexEnumerator();
		else
			return new VertexEnumerator();
	}
	
	@SuppressWarnings("rawtypes")
	public Enumeration allFaces(int startIndex, boolean UVFaces, int stemLevel) {
		return new FaceEnumerator(this,startIndex,UVFaces,stemLevel);
	}	

	@SuppressWarnings("rawtypes")
	public Enumeration allParts(int stemLevel) {
		return new PartEnumerator(stemLevel);
	}	
		
	
	/**
	 * Returns the total number of vertices in the mesh.
	 * 
	 * @return total number of vertices in the mesh
	 */
	public int vertexCount() {
		// count all meshpoints of all parts
		int cnt=0;
		
		for (int i = 0; i<size(); i++) {
			cnt += ((MeshPart)elementAt(i)).vertexCount();
		}
		return cnt;
	}
	
	/**
	 * Returns the total number of faces, that has to be
	 * created for the mesh. Povray wants to know this
	 * before the faces itself.
	 * 
	 * @return total number of faces, that has to be created for the mesh
	 */
	public int faceCount()  {
		int cnt = 0;
		for (int i=0; i<size(); i++) {
			cnt += ((MeshPart)elementAt(i)).faceCount();
		}
		return cnt;
	}

	/**
	 * Returns the total number of uv-vectors, that has to be
	 * created for the mesh. Povray wants to know this
	 * before the uv vectors itself.
	 * 
	 * @return total number of uv-vectors, that has to be created for the mesh
	 */
	public int uvCount()  {
		int cnt = 0;
		
		for (int i=0; i<firstMeshPart.length; i++) {
			cnt += ((MeshPart)elementAt(firstMeshPart[i])).uvCount();
		}
		return cnt;
	}
	
	/**
	 * Returns the index of the first uv-vector, of the given level
	 * 
	 * @param level level
	 * @return index of the first uv-vector, of the given level
	 */
	public int firstUVIndex(int level) {
		int cnt = 0;
		
		for (int i=0; i<level ; i++) {
			cnt += ((MeshPart)elementAt(firstMeshPart[i])).uvCount();
		}
		return cnt;
	}

}
