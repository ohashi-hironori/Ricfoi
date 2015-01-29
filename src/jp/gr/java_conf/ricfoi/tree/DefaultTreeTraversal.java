package jp.gr.java_conf.ricfoi.tree;

public class DefaultTreeTraversal implements TreeTraversal {

	@Override
	public boolean enterStem(Stem stem) {
		return true;
	}

	@Override
	public boolean enterTree(Tree tree) {
		return true;
	}

	@Override
	public boolean leaveStem(Stem stem) {
		return true;
	}

	@Override
	public boolean leaveTree(Tree tree) {
		return true;
	}

	@Override
	public boolean visitLeaf(Leaf leaf) {
		return true;
	}

}
