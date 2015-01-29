package jp.gr.java_conf.ricfoi.export;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Enumeration;

import jp.gr.java_conf.ricfoi.mesh.Mesh;
import jp.gr.java_conf.ricfoi.mesh.MeshGenerator;
import jp.gr.java_conf.ricfoi.mesh.MeshPart;
import jp.gr.java_conf.ricfoi.tree.DefaultTreeTraversal;
import jp.gr.java_conf.ricfoi.tree.Leaf;
import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.type.Face;
import jp.gr.java_conf.ricfoi.type.FloatFormat;
import jp.gr.java_conf.ricfoi.type.LeafMesh;
import jp.gr.java_conf.ricfoi.type.UVVector;
import jp.gr.java_conf.ricfoi.type.Vector;
import jp.gr.java_conf.ricfoi.type.Vertex;

class OBJLeafWriterBase extends DefaultTreeTraversal {

	LeafMesh leafMesh;
	AbstractExporter exporter;
	public int leafVertexOffset;
	PrintWriter w;
	long leavesProgressCount=0;
	Tree tree;

	static final NumberFormat fmt = FloatFormat.getInstance();

	public OBJLeafWriterBase(Tree tree, LeafMesh leafMesh, AbstractExporter exporter, int leafVertexOffset) {
		super();
		this.exporter = exporter;
		this.w = exporter.getWriter();
		this.leafMesh = leafMesh;
		this.leafVertexOffset = leafVertexOffset;
	}

	@Override
	public boolean enterTree(Tree tree) {
		this.tree = tree;
		return true;
	}
}

class OBJLeafFaceWriter extends OBJLeafWriterBase {

	boolean firstLeaf;
	long faceProgressCount=0;
	long smoothingGroup;
	int uvVertexOffset;
	boolean outputLeafUVs=true;
	boolean outputStemUVs=true;
	boolean outputNormals = false;

	public OBJLeafFaceWriter(Tree tree, AbstractExporter exporter,
			LeafMesh leafMesh,
			int leafVertexOffset, int uvVertexOffset,
			long smoothingGroup,	
			boolean outputLeafUVs, boolean outputStemUVs) {
		super(tree, leafMesh, exporter, leafVertexOffset);
		
		firstLeaf = true;
		this.smoothingGroup = smoothingGroup;
		this.uvVertexOffset = uvVertexOffset;
		this.outputLeafUVs = outputLeafUVs;
		this.outputStemUVs = outputStemUVs;
	}
	
	
	@Override
	public boolean visitLeaf(Leaf l) {
		if (firstLeaf) {
			
			w.println("g leaves");
			w.println("usemtl leaves");
			
			firstLeaf = false;
		}
	
		w.println("s "+smoothingGroup++);
		for (int i=0; i<leafMesh.getShapeFaceCount(); i++) {
			Face face = leafMesh.shapeFaceAt(i);
			writeFace(face,leafVertexOffset,face,uvVertexOffset,outputLeafUVs,outputNormals);
		}
		
		// increment face offset
		leafVertexOffset += leafMesh.getShapeVertexCount();
		
		return true;
	}

	
	private void writeFace(Face f, long offset, Face uv, long uvOffset, boolean writeUVs, boolean writeNormals) {
		w.print("f "); 
				
		for (int i=0; i<f.getPoints().length; i++) {
			w.print(offset+f.getPoint(i));
			if (writeUVs || writeNormals) {
				w.print("/");
				if (writeUVs) w.print(uvOffset+uv.getPoint(i));
				if (writeNormals) w.print("/"+offset+f.getPoint(i));
			}
			if (i<f.getPoints().length-1) w.print(" ");
			else w.println();
		}
	}
}


class OBJLeafVertexWriter extends OBJLeafWriterBase {

	String type;
	long vertexProgressCount=0;
	
	public OBJLeafVertexWriter(Tree tree, AbstractExporter exporter, LeafMesh leafMesh, int leafVertexOffset, String type) {
		super(tree, leafMesh, exporter, leafVertexOffset);
		this.type=type;
	}

	@Override
	public boolean visitLeaf(Leaf l) {
		for (int i=0; i<leafMesh.getShapeVertexCount(); i++) {
			if (type=="v") {
				writeVertex(l.getTransformation().apply(leafMesh.shapeVertexAt(i).getPoint()),type);
			} else {
				writeVertex(l.getTransformation().apply(leafMesh.shapeVertexAt(i).getNormal()),type);
			}
		}
		
		return true;
	}

	@SuppressWarnings("nls")
	private void writeVertex(Vector v, String type) {
		w.println(type+" "+fmt.format(v.getX())+" "+fmt.format(v.getZ())+" "+fmt.format(v.getY()));
	}
	
}


/**
 * Exports a tree mesh as Wavefront OBJ file 
 *
 */
final class OBJExporter extends MeshExporter {
	long vertexProgressCount=0;
	long faceProgressCount=0;
	NumberFormat frm = FloatFormat.getInstance();
	Mesh mesh;
	LeafMesh leafMesh;
	Tree tree;
	long smoothingGroup;
	int vertexOffset;
	int uvVertexOffset;
	public boolean outputLeafUVs=true;
	public boolean outputStemUVs=true;
	
	boolean outputNormals = false;

	/**
	 * @param aTree
	 * @param pw
	 * @param p
	 */
	public OBJExporter(Tree tree, MeshGenerator meshGenerator) {
		super(meshGenerator);
		this.tree = tree;
	}
	
	@Override
	public void doWrite()  {
		smoothingGroup=1;

		long objCount = (tree.getStemCount()+tree.getLeafCount())*(outputNormals? 2 : 1); 

		mesh = meshGenerator.createStemMeshByLevel(tree);
		leafMesh = meshGenerator.createLeafMesh(tree,meshGenerator.getUseQuads());

		// vertices
		
		writeStemVertices("v");
		writeLeafVertices("v");
		
		if (outputStemUVs) writeStemVertices("vt");
		if (outputLeafUVs) writeLeafVertices("vt");

		// use smoothing to interpolate normals			
		if (outputNormals) {
			writeStemVertices("vn");
			writeLeafVertices("vn");
		}
		
		// faces
		writeStemFaces();
		//writeLeafFaces();
		OBJLeafFaceWriter faceExporter = 
			new OBJLeafFaceWriter(tree,this,leafMesh,
				vertexOffset, uvVertexOffset,smoothingGroup,
				outputLeafUVs, outputStemUVs);
		tree.traverseTree(faceExporter);
		vertexOffset = faceExporter.leafVertexOffset;
		w.flush();
	}
	
	private void writeStemVertices(String type) {
		if (type == "vt") { 
			// texture vectors
			for (Enumeration<UVVector> vertices = mesh.allVertices(true);
			vertices.hasMoreElements();) {
				UVVector vertex = (UVVector)vertices.nextElement();
				writeUVVertex(vertex);
			}
		} else {
			// vertex and normal vectors
			for (Enumeration<Vertex> vertices = mesh.allVertices(false);
				vertices.hasMoreElements();) {
					Vertex vertex = (Vertex)vertices.nextElement();
					
					if (type=="v") {
						writeVertex(vertex.getPoint(),"v");
					} else {
						writeVertex(vertex.getNormal(),"vn");
					}
			}
		}
	}
	
	private void writeLeafVertices(String type) {
		if (type == "vt") { 
			// texture vectors
			if (leafMesh.isFlat()) {
				for (int i=0; i<leafMesh.getShapeVertexCount(); i++) {
					writeUVVertex(leafMesh.shapeUVAt(i));
				}	
			}
		} else {
			OBJLeafVertexWriter vertexExporter = new OBJLeafVertexWriter(tree, this, leafMesh, vertexOffset, type);
			tree.traverseTree(vertexExporter);
			vertexOffset = vertexExporter.leafVertexOffset;
		}
	}
	
	private void writeStemFaces() {
		// output mesh triangles
		vertexOffset = 1;
		//boolean separate_trunk = false;
		for (int stemLevel = 0; stemLevel<tree.getLevels(); stemLevel++) {
		
			// => start a new group
			w.println("g "+(stemLevel==0 ? "trunk" : "stems_"+stemLevel));
			w.println("usemtl "+(stemLevel==0 ? "trunk" : "stems_"+stemLevel));
			
			for (Enumeration<MeshPart> parts=mesh.allParts(stemLevel); parts.hasMoreElements();) { 
				MeshPart mp = (MeshPart)parts.nextElement();
				uvVertexOffset = 1 + mesh.firstUVIndex(mp.getStem().getLevel());
				w.println("s "+smoothingGroup++);
				
				Enumeration<Face> faces=mp.allFaces(mesh,vertexOffset,false);
				Enumeration<Face> uvFaces=mp.allFaces(mesh,uvVertexOffset,true);
				
				while (faces.hasMoreElements()) {
					Face face = (Face)faces.nextElement();
					Face uvFace = (Face)uvFaces.nextElement();
					writeFace(face,0,uvFace,0,outputStemUVs,outputNormals);
				}
				
				vertexOffset += mp.vertexCount();
				
				// FIXME: only needed for last stem before leaves
				uvVertexOffset += mp.uvCount();
			}
		}
	}

	@SuppressWarnings("nls")
	private void writeVertex(Vector v, String type) {
		w.println(type+" "+frm.format(v.getX())+" "+frm.format(v.getZ())+" "+frm.format(v.getY()));
	}

	@SuppressWarnings("nls")
	private void writeUVVertex(UVVector v) {
		w.println("vt "+frm.format(v.getU())+" "+frm.format(v.getV())+" "+frm.format(0));
	}
	
	private void writeFace(Face f, long offset, Face uv, long uvOffset, boolean writeUVs, boolean writeNormals) {
		w.print("f "); 
				
		for (int i=0; i<f.getPoints().length; i++) {
			w.print(offset+f.getPoint(i));
			if (writeUVs || writeNormals) {
				w.print('/');
				if (writeUVs) w.print(uvOffset+uv.getPoint(i));
				if (writeNormals) w.print('/'+offset+f.getPoint(i));
			}
			if (i<f.getPoints().length-1) w.print(" ");
			else w.println();
		}
	}

	
}
