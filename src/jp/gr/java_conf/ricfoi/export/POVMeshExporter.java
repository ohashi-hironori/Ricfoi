package jp.gr.java_conf.ricfoi.export;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Enumeration;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.Ricfoi;
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

class POVMeshLeafWriterBase extends DefaultTreeTraversal {

	LeafMesh leafMesh;
	AbstractExporter exporter;
	long leafVertexOffset;
	PrintWriter w;
//	long leavesProgressCount=0;
	Tree tree;

	static final NumberFormat fmt = FloatFormat.getInstance();

	public POVMeshLeafWriterBase(AbstractExporter exporter, LeafMesh leafMesh,
			long leafVertexOffset) {
		super();
		this.w = exporter.getWriter();
		this.exporter = exporter;
		this.leafMesh = leafMesh;
		this.leafVertexOffset = leafVertexOffset;
	}

	@Override
	public boolean enterTree(Tree tree) {
		this.tree = tree;
		return true;
	}
	
	void writeVector(Vector v) {
		w.print(Constant.LT+fmt.format(v.getX())+Constant.COMMA+fmt.format(v.getZ())+Constant.COMMA+fmt.format(v.getY())+Constant.GT);
	}
	
}

class POVMeshLeafFaceWriter extends POVMeshLeafWriterBase {

	public POVMeshLeafFaceWriter(AbstractExporter exporter, LeafMesh leafMesh, long leafVertexOffset) {
		super(exporter,leafMesh,leafVertexOffset);
	}
	
	@Override
	public boolean visitLeaf(Leaf leaf) {
		
		for (int i=0; i<leafMesh.getShapeFaceCount(); i++) {
			Face face = leafMesh.shapeFaceAt(i);
			w.print(Constant.LT + (leafVertexOffset+face.getPoint(0)) + Constant.COMMA 
					+ (leafVertexOffset+face.getPoint(1)) + Constant.COMMA 
					+ (leafVertexOffset+face.getPoint(2)) + Constant.GT);
			if (i<leafMesh.getShapeFaceCount()-1) {
				w.print(Constant.COMMA);
			}
			if (i % 6 == 4) {
				// new line
				w.println();
				w.print("              "); //$NON-NLS-1$
			}
		}
		w.println();
		
		// increment face offset
		leafVertexOffset += leafMesh.getShapeVertexCount();
		return true;
	}

}

//class POVMeshLeafNormalWriter extends POVMeshLeafWriterBase {
//
//	public POVMeshLeafNormalWriter(AbstractExporter exporter, LeafMesh leafMesh, long leafVertexOffset) {
//		super(exporter, leafMesh, leafVertexOffset);
//	}
//
//	@Override
//	public boolean visitLeaf(Leaf leaf) {
//		for (int i=0; i<leafMesh.getShapeVertexCount(); i++) {
//			writeVector(leaf.getTransformation().apply(leafMesh.shapeVertexAt(i).getNormal()));
//			
//			if (i<leafMesh.getShapeVertexCount()-1) {
//				w.print(',');
//			}
//			if (i % 3 == 2) {
//				// new line
//				w.println();
//				w.print("              "); //$NON-NLS-1$
//			} 
//		}
////		throw new RuntimeException("Not implemented: if using normals for leaves use factor 3 instead of 2 in progress.beginPhase");
//		return false;
//	}
//
//}

class POVMeshLeafUVFaceWriter extends POVMeshLeafWriterBase {

	public POVMeshLeafUVFaceWriter(AbstractExporter exporter, LeafMesh leafMesh, long leafVertexOffset) {
		super(exporter, leafMesh, leafVertexOffset);
	}

	@Override
	public boolean visitLeaf(Leaf l) {
		for (int i=0; i<leafMesh.getShapeFaceCount(); i++) {
			Face face = leafMesh.shapeFaceAt(i);
			w.print(Constant.LT+face.getPoint(0)+Constant.COMMA+face.getPoint(1)+Constant.COMMA+face.getPoint(2)+Constant.GT);
			if (i<leafMesh.getShapeFaceCount()-1) {
				w.print(Constant.COMMA);
			}
			if (i % 6 == 4) {
				// new line
				w.println();
				w.print("              "); //$NON-NLS-1$
			}
		}
		w.println();
		return true;
	}

}

class POVMeshLeafVertexWriter extends POVMeshLeafWriterBase {

	public POVMeshLeafVertexWriter(AbstractExporter exporter, LeafMesh leafMesh, long leafVertexOffset) {
		super(exporter, leafMesh, leafVertexOffset);
	}

	@Override
	public boolean visitLeaf(Leaf l) {
		for (int i=0; i<leafMesh.getShapeVertexCount(); i++) {
			writeVector(l.getTransformation().apply(leafMesh.shapeVertexAt(i).getPoint()));
			
			if (i<leafMesh.getShapeVertexCount()-1) {
				w.print(Constant.COMMA);
			}
			if (i % 3 == 2) {
				// new line
				w.println();
				w.print("              "); //$NON-NLS-1$
			} 
		}
		
		return true;
	}

}

class POVMeshExporter extends MeshExporter {

	Mesh mesh;
	LeafMesh leafMesh;
	Tree tree;
	long leafVertexOffset;
	long stemsProgressCount=0;
	
	boolean outputStemNormals=true;
	boolean outputLeafNormals=false;
	public boolean outputLeafUVs=true;
	public boolean outputStemUVs=true;
	
	String povrayDeclarationPrefix;
	
	static final NumberFormat fmt = FloatFormat.getInstance();
	
	public POVMeshExporter(Tree tree, MeshGenerator meshGenerator) {
		super(meshGenerator);
		this.tree = tree;
		this.povrayDeclarationPrefix = tree.getSpecies() + Constant.LL + tree.getSeed() + Constant.LL;
	}

	@Override
	@SuppressWarnings("nls")
	public void doWrite() {
		// write tree definition as comment
		w.println("/*************** Tree made by: ******************");
		w.println();
		w.println(Ricfoi.NAME);
		w.println();
		tree.paramsToXML(w);
		w.println("************************************************/");
		
		// tree scale
		w.println("#declare " + povrayDeclarationPrefix + "height = " + fmt.format(tree.getHeight()) + ";");
		
		writeStems();
		writeLeaves();
		
		w.flush();
	}

	@SuppressWarnings("nls")
	private void writeLeaves() {
	
		leafMesh = meshGenerator.createLeafMesh(tree, false);
	
		int passes = 2; 
		if (outputLeafNormals) passes++;
		if (outputLeafUVs) passes=passes++;
		
		long leafCount = tree.getLeafCount();
		
		if (leafCount>0) {
			w.println("#declare " + povrayDeclarationPrefix + "leaves = mesh2 {");
			w.println("     vertex_vectors { "+leafMesh.getShapeVertexCount()*leafCount);
			tree.traverseTree(new POVMeshLeafVertexWriter(this,leafMesh,leafVertexOffset));
			w.println("     }");
	
			// output uv vectors
			if (outputLeafUVs && leafMesh.isFlat()) {
				w.println("     uv_vectors {  " + leafMesh.getShapeVertexCount());
				for (int i=0; i<leafMesh.getShapeVertexCount(); i++) {
					writeUVVector(leafMesh.shapeUVAt(i));
	
					if (i<leafMesh.getShapeVertexCount()-1) {
						w.print(Constant.COMMA);
					}
					if (i % 6 == 2) {
						// new line
						w.println();
						w.print("          ");
					} 
					w.println();
				}	
				w.println("    }");
			}
			
			leafVertexOffset=0;
			
			w.println("     face_indices { "+leafMesh.getShapeFaceCount()*leafCount);
			tree.traverseTree(new POVMeshLeafFaceWriter(this,leafMesh,leafVertexOffset));
			w.println("     }");
	
			if (outputLeafUVs && leafMesh.isFlat()) {
				w.println("     uv_indices { "+leafMesh.getShapeFaceCount()*leafCount);
				tree.traverseTree(new POVMeshLeafUVFaceWriter(this,leafMesh,leafVertexOffset));
				w.println("     }");
			}
			
			w.println("}");
		} else {
			// empty declaration
			w.println("#declare " + povrayDeclarationPrefix + "leaves = sphere {<0,0,0>,0}");		
		}
	}	

	@SuppressWarnings("nls")
	private void writeStems() {
		
		String indent="  ";
		outputStemNormals = true;
		mesh = meshGenerator.createStemMesh(tree);
		long vertex_cnt = mesh.vertexCount();
		long face_cnt = mesh.faceCount();
		long uv_cnt = mesh.uvCount();
		
//		long elements = vertex_cnt+face_cnt;
//		if (outputStemNormals) elements += vertex_cnt;
//		if (outputStemUVs) elements += face_cnt;
		
		w.println("#declare " + povrayDeclarationPrefix + "stems = "); 
		w.println(indent + "mesh2 {");
		
		// output section points
		w.println(indent+"  vertex_vectors { " + vertex_cnt);
		writeStemPoints();
		w.println(indent + "  }");
		
		// output normals
		if (outputStemNormals) {
			w.println(indent + "  normal_vectors { " + vertex_cnt); 
			writeStemNormals();
			w.println(indent+"  }");
		}
		
		// output uv vectors
		if (outputStemUVs) {
			w.println(indent + "  uv_vectors {  " + uv_cnt);
			writeStemUVs();
			w.println();
			w.println(indent+"  }");
		}
		
		// output mesh triangles
		w.println(indent + "  face_indices { " + face_cnt);
		writeStemFaces(false);
		w.println();
		w.println(indent + "  }");
		
		// output uv faces
		if (outputStemUVs) {
			/*offset = 0;*/
			w.println(indent + "  uv_indices {  " + face_cnt);
			writeStemFaces(true);
			w.println();
			
			w.println(indent+"/* */  }");
		}	
		w.println(indent + "}");
	}

	private void writeStemPoints(/*String indent*/) {
		int i = 0;
		for (Enumeration<Vertex> vertices = mesh.allVertices(false); vertices.hasMoreElements();) {
			Vertex vertex = (Vertex)vertices.nextElement();
			writeVector(vertex.getPoint());
			if (vertices.hasMoreElements()) w.print(Constant.COMMA);
			if (++i % 6 == 2) w.println();
		}
		w.println();
	}

	public void writeStemFaces(boolean uv) {
		int j=0;
		for (Enumeration<Face> faces=mesh.allFaces(0,uv,-1); faces.hasMoreElements();) {
			Face face = (Face)faces.nextElement();
			w.print(Constant.LT+face.getPoint(0)+Constant.COMMA+face.getPoint(1)+Constant.COMMA+face.getPoint(2)+Constant.GT);
			if (faces.hasMoreElements()) w.print(Constant.COMMA);
			if (j++ % 6 == 4) w.println();
		}
	}

	private void writeStemNormals() {
		
		for (Enumeration<MeshPart> parts=mesh.elements(); parts.hasMoreElements();) {
//			((MeshPart)parts.nextElement()).setNormals(true);
				((MeshPart)parts.nextElement()).setNormals();
		}
		
		int i = 0;
		for (Enumeration<Vertex> vertices = mesh.allVertices(false); vertices.hasMoreElements();) {
			Vertex vertex = (Vertex)vertices.nextElement();
			writeVector(vertex.getNormal());
			if (vertices.hasMoreElements()) w.print(Constant.COMMA);
			if (++i % 6 == 2) w.println();
		}
		w.println();
	}

	private void writeStemUVs() {
		int j=0;
		for (Enumeration<UVVector> vertices=mesh.allVertices(true); vertices.hasMoreElements();) {
			UVVector vertex = (UVVector)vertices.nextElement();
			writeUVVector(vertex);
			if (vertices.hasMoreElements()) w.print(Constant.COMMA);
			if (j++ % 6 == 2) w.println();
		}
		w.println();
	}

	private void writeVector(Vector v) {
		w.print(Constant.LT+fmt.format(v.getX())+Constant.COMMA+fmt.format(v.getZ())+Constant.COMMA+fmt.format(v.getY())+Constant.GT);
	}

	private void writeUVVector(UVVector uv) {
		w.print(Constant.LT+fmt.format(uv.getU())+Constant.COMMA+fmt.format(uv.getV())+Constant.GT);
	}

}
