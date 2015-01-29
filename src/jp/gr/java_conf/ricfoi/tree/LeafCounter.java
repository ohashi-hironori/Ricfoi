package jp.gr.java_conf.ricfoi.tree;

public class LeafCounter extends DefaultTreeTraversal {

	long leafCount;
	
	public long getLeafCount() {
		return leafCount;
	}

	@Override
	public boolean enterStem(Stem stem) {
		// add leaves of this stem
		leafCount += stem.getLeafCount(); 
		return true;
	}

	@Override
	public boolean enterTree(Tree tree) {
		leafCount=0; // start counting leaves
		return true;
	}

	@Override
	public boolean visitLeaf(Leaf leaf) {
		return false; // don't visit more leaves
		  // for efficency leaf count was got from the parent stem 
	}

}
