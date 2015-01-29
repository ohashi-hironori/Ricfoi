package jp.gr.java_conf.ricfoi.export;

import java.io.PrintWriter;
import java.text.NumberFormat;

import jp.gr.java_conf.ricfoi.mesh.Mesh;
import jp.gr.java_conf.ricfoi.mesh.MeshGenerator;
import jp.gr.java_conf.ricfoi.mesh.MeshPart;
import jp.gr.java_conf.ricfoi.mesh.MeshSection;
import jp.gr.java_conf.ricfoi.tree.DefaultTreeTraversal;
import jp.gr.java_conf.ricfoi.tree.Leaf;
import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.type.Face;
import jp.gr.java_conf.ricfoi.type.FloatFormat;
import jp.gr.java_conf.ricfoi.type.LeafMesh;
import jp.gr.java_conf.ricfoi.type.VFace;
import jp.gr.java_conf.ricfoi.type.Vector;

@SuppressWarnings("nls")
final class DXFWriter {
	
	PrintWriter w;
	NumberFormat frm = FloatFormat.getInstance();
	
	public DXFWriter(PrintWriter w) {
		this.w = w;
	}

	public void writeHeader(String comment, Vector minPoint, Vector maxPoint) {
		wg(999,comment);
		wg(0,"SECTION");
		wg(2,"HEADER");
		wg(9,"$INSBASE");
		writePoint(new Vector(),0);
		wg(9,"$EXTMIN");
		writePoint(minPoint,0);
		wg(9,"$EXTMAX");
		writePoint(maxPoint,0);
		wg(0,"ENDSEC");
	}
	
	public void writeTables() {
		wg(0,"SECTION");
		wg(2,"TABLES");
		writeLineTable();
		writeLayerTable();
		//writeStyleTable();
		wg(0,"ENDSEC");
	}
	
	public void writeLineTable() {
		wg(0,"TABLE");
		wg(2,"LTYPE");
		wg(70,"1");         // standard flag values
		wg(0,"LTYPE");
		wg(2,"CONTINUOUS"); // ltype name
		wg(70,"64");        // standard flag values
		wg(3,"Solid line"); // ltype text
		wg(72,"65");        // alignment code 'A'   
		wg(73,"0");         // dash length items
		wg(40,"0.000000");  // total pattern length
		wg(0,"ENDTAB");
	}
	
	public void writeLayerTable() {
		wg(0,"TABLE");
		wg(2,"LAYER");
		wg(70,"6");          // standard flag values
		// LAYER 1
		wg(0,"LAYER");
		wg(2,"1");           // layer name
		wg(70,"64");         // standard flag values
		wg(62,"7");          // color number
		wg(6,"CONTINUOUS");  // line type name
		// LAYER 2
		wg(0,"LAYER");       
		wg(2,"2");           // layer name
		wg(70,"64");         // standard flag values
		wg(62,"7");          // color number
		wg(6,"CONTINUOUS");  // line type name
		wg(0,"ENDTAB");
	}
	
	public void writeStyleTable() {
		wg(0,"TABLE");
		wg(2,"STYLE");
		wg(70,"0");           // standard flags
		wg(0,"ENDTAB");
	}
	
	
	public void writeBlocks() {
		wg(0,"SECTION");
		wg(2,"BLOCKS");
		wg(0,"ENDSEC");
	}
	
	public void writeEntitiesBegin() {
		wg(0,"SECTION");
		wg(2,"ENTITIES");
	}
	
	public void writeEntitiesEnd() {
		wg(0,"ENDSEC");
	}
	
	void wg(int code, String val) {
		w.println(Integer.toString(code));
		w.println(val);
	}
	
	public void writeFace(Vector u, Vector v, Vector w, String layer) {
		wg(0,"3DFACE");
		wg(8,layer);
		wg(62,"1"); // color
		writePoint(u,0);
		writePoint(v,1);
		writePoint(w,2);
		writePoint(w,3); // repeat last point
	}
	
	public void writeFace(VFace face,String layer) {
		// FIXME: maybe could be faster when putting
		// all into one string and then send to stream?
		wg(0,"3DFACE");
		wg(8,layer);
		wg(62,layer); // different colors for layer 1 and 2
		writePoint(face.getPoint(0),0);
		writePoint(face.getPoint(1),1);
		writePoint(face.getPoint(2),2);
		writePoint(face.getPoint(2),3); // repeat last point
	}
	
	public void writePoint(Vector v, int n) {
		wg(10+n,frm.format(v.getX()));
		wg(20+n,frm.format(v.getZ()));
		wg(30+n,frm.format(v.getY()));
	}

}

class DXFLeafWriter extends DefaultTreeTraversal {

	LeafMesh leafMesh;
	VFace vFace;
	String layer;
	DXFWriter writer;
	AbstractExporter exporter;
	MeshGenerator meshGenerator;

	public DXFLeafWriter(AbstractExporter exporter, MeshGenerator meshGenerator, String layer) {
		super();
		this.layer = layer;
		this.exporter = exporter;
		this.writer = new DXFWriter(exporter.getWriter());
		vFace = new VFace(new Vector(),new Vector(),new Vector());
		this.meshGenerator = meshGenerator;
	}
	
	@Override
	public boolean enterTree(Tree tree) {
		this.leafMesh = meshGenerator.createLeafMesh(tree,false);
		return true;
	}

	@Override
	public boolean visitLeaf(Leaf leaf) {
		for (int i=0; i<leafMesh.getShapeFaceCount(); i++) {

			Face face = leafMesh.shapeFaceAt(i);
			for (int k=0; k<3; k++) {
				vFace.setPoint(k, leaf.getTransformation().apply(leafMesh.shapeVertexAt((int)face.getPoint(k)).getPoint()));
			}
			
			writer.writeFace(vFace,layer);
		}
		return true;
	}
}

class DXFExporter extends MeshExporter {

	NumberFormat frm = FloatFormat.getInstance();
	Tree tree;

	public DXFExporter(Tree tree, MeshGenerator meshFactory) {
		super(meshFactory);
		this.tree = tree;
	}

	@SuppressWarnings("nls")
	@Override
	public void doWrite() {
			DXFWriter writer = new DXFWriter(w);
			writer.writeHeader("DXF created with Ricfoi, tree species: "+tree.getSpecies(), tree.getMinPoint(),tree.getMaxPoint());
			writer.writeTables();
			writer.writeBlocks();
			writer.writeEntitiesBegin();

			// Geometric entities go here

			// stems on layer 1
			writeStems("1");
			// leafs on layer 2
			writeLeafs("2");
			
			writer.writeEntitiesEnd();
			writer.wg(0,"EOF");
			w.flush();
	}
	
	private void writeStems(String layer) {
		Mesh mesh = meshGenerator.createStemMesh(tree);
		DXFWriter writer = new DXFWriter(w);

		for (int i=0; i<mesh.size(); i++) {
			
			MeshPart mp = (MeshPart)mesh.elementAt(i);
			
			for (int k=0; k<mp.size()-1; k++) { 
				java.util.Vector<VFace> faces = mp.vFaces((MeshSection)mp.elementAt(k));

				for (int j=0; j<faces.size(); j++) {
					VFace face =(VFace)faces.elementAt(j);
					writer.writeFace(face,layer);
				}
			}
		}
	}
	
	private void writeLeafs(String layer) {
		DXFLeafWriter exporter = new DXFLeafWriter(this, meshGenerator, layer);
		tree.traverseTree(exporter);
	}

}
