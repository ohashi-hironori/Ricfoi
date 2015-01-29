package jp.gr.java_conf.ricfoi.export;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.tree.Tree;

public class POVSceneExporter extends AbstractExporter {

	Tree tree;
	String povrayDeclarationPrefix;
	int width;
	int height;

	public POVSceneExporter(Tree tree, int width, int height) {
		this.tree = tree;
		this.width = width;
		this.height = height;
		this.povrayDeclarationPrefix = tree.getSpecies() + Constant.LL + tree.getSeed() + Constant.LL; 
	}

	@SuppressWarnings("nls")
	@Override
	protected void doWrite() {
		w.println("// render as "+height+"x"+width);
		w.println("#include \"" + tree.getSpecies() + ".inc\"");
		w.println("background {rgb <0.95,0.95,0.9>}");
		w.println("light_source { <5000,5000,-3000>, rgb 1.2 }");
		w.println("light_source { <-5000,2000,3000>, rgb 0.5 shadowless }");
		w.println("#declare HEIGHT = " + povrayDeclarationPrefix + "height * 1.3;");
		w.println("#declare WIDTH = HEIGHT*"+width+"/"+height+";");
		w.println("camera { orthographic location <0, HEIGHT*0.45, -100>");
		w.println("         right <WIDTH, 0, 0> up <0, HEIGHT, 0>");
		w.println("         look_at <0, HEIGHT*0.45, -80> }");
		w.println("union { ");
		w.println("         object { " + povrayDeclarationPrefix + "stems");
		w.println("                pigment {color rgb 0.9} }"); 
		w.println("         object { " + povrayDeclarationPrefix + "leaves");
		w.println("                texture { pigment {color rgb 1} ");
		w.println("                          finish { ambient 0.15 diffuse 0.8 }}}");
		w.println("         rotate 90*y }");
		if (tree.getLeafCount() > 0) {
		    w.println("         object { " + povrayDeclarationPrefix + "stems");
		    w.println("                scale 0.7 rotate 45*y");  
		    w.println("                translate <WIDTH*0.33,HEIGHT*0.33,WIDTH>");
		    w.println("                pigment {color rgb 0.9} }"); 
		}
		w.flush();
	}

}
