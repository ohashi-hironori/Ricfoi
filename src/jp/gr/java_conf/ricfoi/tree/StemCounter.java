package jp.gr.java_conf.ricfoi.tree;

public class StemCounter extends DefaultTreeTraversal {

	long stemCount;
	
	public long getStemCount() {
		return stemCount;
	}

	@Override
	public boolean enterStem(Stem stem) {
		stemCount++; // one more stem
		return true;
	}

	@Override
	public boolean enterTree(Tree tree) {
		stemCount = 0; // start stem counting
		return true;
	}

	@Override
	public boolean visitLeaf(Leaf leaf) {
		return false; // don't count leaves
	}

}
