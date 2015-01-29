package jp.gr.java_conf.ricfoi.export;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Enumeration;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.tree.Leaf;
import jp.gr.java_conf.ricfoi.tree.Stem;
import jp.gr.java_conf.ricfoi.tree.StemSection;
import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.tree.TreeTraversal;
import jp.gr.java_conf.ricfoi.type.FloatFormat;
import jp.gr.java_conf.ricfoi.type.Matrix;
import jp.gr.java_conf.ricfoi.type.Vector;

class POVConeLeafWriter implements TreeTraversal {
	
	Tree tree;
	PrintWriter w;
	String povrayDeclarationPrefix;
	AbstractExporter exporter;
	
	public POVConeLeafWriter(AbstractExporter exporter, Tree tree) {
		super();
		this.exporter = exporter;
		this.w = exporter.getWriter();
		this.tree = tree;
		this.povrayDeclarationPrefix = tree.getSpecies() + Constant.LL + tree.getSeed() + Constant.LL;
	}

	@Override
	public boolean enterStem(Stem stem) {
		return true;
	}

	@Override
	public boolean enterTree(Tree tree) {
		this.tree = tree;
		return true;
	}

	@Override
	public boolean leaveStem(Stem stem) {
		return true;
	}

	@Override
	public boolean leaveTree(Tree tree) {
		w.flush();
		return true;
	}

	@Override
	public boolean visitLeaf(Leaf leaf) {
		w.println("    object { " + povrayDeclarationPrefix + "leaf " + transformationStr(leaf.getTransformation())+"}");
		return true;
	}

	private static String transformationStr(Transformation trf) {
		NumberFormat fmt = FloatFormat.getInstance();
		Matrix matrix = trf.getMatrix();
		Vector vector = trf.getVector();
		return "matrix "+Constant.LT 
		+ fmt.format(matrix.get(Transformation.X,Transformation.X)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.Z,Transformation.X)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.Y,Transformation.X)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.X,Transformation.Z)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.Z,Transformation.Z)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.Y,Transformation.Z)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.X,Transformation.Y)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.Z,Transformation.Y)) + Constant.COMMA
		+ fmt.format(matrix.get(Transformation.Y,Transformation.Y)) + Constant.COMMA
		+ fmt.format(vector.getX()) + Constant.COMMA
		+ fmt.format(vector.getZ()) + Constant.COMMA
		+ fmt.format(vector.getY()) + Constant.GT;
	}

}

class POVConeStemWriter implements TreeTraversal {

	Tree tree;
	AbstractExporter exporter;
	PrintWriter w;
	Params params;
	int level;
	
	public POVConeStemWriter(AbstractExporter exporter, int level) {
		super();
		this.exporter = exporter;
		this.w = exporter.getWriter();
		this.level=level;
	}

	@Override
	public boolean enterStem(Stem stem) {
		if (level >= 0 && stem.getLevel() < level) {
			return true; // look further for stems
			
		} else if (level >= 0 && stem.getLevel() > level) {
			return false; // go back to higher level
			
		} else {
			
			String indent = whitespace(stem.getLevel()*2+4);
			NumberFormat fmt = FloatFormat.getInstance();
			Enumeration<?> sections = stem.sections();
			
			if (sections.hasMoreElements()) {
				StemSection from = (StemSection)sections.nextElement();
				StemSection to = null;
			
				while (sections.hasMoreElements()) {
					to = (StemSection)sections.nextElement();

					w.println(indent + "cone   { " + vectorStr(from.getPosition()) + ", "
							+ fmt.format(from.getRadius()) + ", " 
							+ vectorStr(to.getPosition()) + ", " 
							+ fmt.format(to.getRadius()) + " }");
					
					// put spheres where z-directions changes
					if (! from.getZ().equals(to.getPosition().subtract(from.getPosition()).normalize())) {
						w.println(indent + "sphere { " 
									+ vectorStr(from.getPosition()) + ", "
									+ fmt.format(from.getRadius()-0.0001) + " }");
					}
				
					from = to;
				}
			
			}
			return true;
		}
	}

	private static String vectorStr(Vector v) {
		NumberFormat fmt = FloatFormat.getInstance();
		return '<'+fmt.format(v.getX())+','+fmt.format(v.getZ())+','+fmt.format(v.getY())+'>';
	}

	private static String whitespace(int len) {
		char[] ws = new char[len];
		for (int i=0; i<len; i++) {
			ws[i] = ' ';
		}
		return new String(ws);
	}
	
	@Override
	public boolean enterTree(Tree tree) {
		this.tree = tree;
		return true;
	}

	@Override
	public boolean leaveStem(Stem stem) {
		return true;
	}

	@Override
	public boolean leaveTree(Tree tree) {
		w.flush();
		return true;
	}

	@Override
	public boolean visitLeaf(Leaf leaf) {
		// don't wirte leaves here
		return false;
	}
	
}


class POVConeExporter extends AbstractExporter {

	Tree tree;
	private String povrayDeclarationPrefix;

	public POVConeExporter(Tree tree) {
		super();
		this.tree = tree;
		this.povrayDeclarationPrefix = tree.getSpecies() + '_' + tree.getSeed() + '_';
	}
	
	@SuppressWarnings("nls")
	@Override
	public void doWrite() {
		// some declarations in the POV file
		NumberFormat frm = FloatFormat.getInstance();
		
		// tree scale
		w.println("#declare " + povrayDeclarationPrefix + "height = "  + frm.format(tree.getHeight()) + ";");
		
		// leaf declaration
		if (tree.getLeafCount()!=0) writeLeafDeclaration();

		// stems
		for (int level=0; level < tree.getLevels(); level++) {
			w.println("#declare " + povrayDeclarationPrefix + "stems_" + level + " = union {");
			
			POVConeStemWriter writer = new POVConeStemWriter(this, level);
			tree.traverseTree(writer);
			
			w.println("}");
		}
		
		// leaves
		if (tree.getLeafCount()!=0) {
			w.println("#declare " + povrayDeclarationPrefix + "leaves = union {");
			
			POVConeLeafWriter lexporter = new POVConeLeafWriter(this, tree);
			tree.traverseTree(lexporter);
			
			w.println("}");
		} else { // empty declaration
			w.println("#declare " + povrayDeclarationPrefix + "leaves = sphere {<0,0,0>,0}"); 
		}
		
		// all stems together
		w.println("#declare " + povrayDeclarationPrefix + "stems = union {"); 
		for (int level=0; level < tree.getLevels(); level++) {
			w.println("  object {" + povrayDeclarationPrefix + "stems_" + level + "}");
		}
		w.println("}");
		w.flush();
	}

	private void writeLeafDeclaration() {
		double length = tree.getLeafLength();
		double width = tree.getLeafWidth();
		w.println("#include \"ricfoi.inc\"");
		w.println("#declare " + povrayDeclarationPrefix + "leaf = " +
				"object { Arb_leaf_" + (tree.getLeafShape().equals("0")? "disc" : tree.getLeafShape())
				+ " translate " + (tree.getLeafStemLength()+0.5) + "*y scale <" 
				+ width + ',' + length + ',' + width + "> }");
	}

}
