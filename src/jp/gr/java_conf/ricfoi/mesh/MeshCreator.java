package jp.gr.java_conf.ricfoi.mesh;

import jp.gr.java_conf.ricfoi.tree.Leaf;
import jp.gr.java_conf.ricfoi.tree.Stem;
import jp.gr.java_conf.ricfoi.tree.Tree;
import jp.gr.java_conf.ricfoi.tree.TreeTraversal;

/**
 * Create a mesh from the tree's stems using then TreeTraversal interface
 */
class MeshCreator implements TreeTraversal {
	Mesh mesh;
	Tree tree;
	int level; // only stems of this level should be created
	boolean useQuads;

	public MeshCreator(Mesh mesh, int level, boolean useQuads) {
		super();
		this.mesh=mesh;
		this.level=level;
		this.useQuads = useQuads;
	}
	
	@Override
	public boolean enterStem(Stem stem) {
		// TODO instead of addToMesh, the traversal should
		// proceed into segments and subsegments itself
		// removing all mesh creation code from Stem, Segment, 
		// Subsegment

		if (level >= 0 && stem.getLevel() < level) {
			return true; // look further for stems
			
		} else if (level >= 0 && stem.getLevel() > level) {
			return false; // go back to higher level
			
		} else { 
			// FIXME: for better performance create only
			// one MeshPartCreator and change stem for every stem
			MeshPartCreator partCreator = new MeshPartCreator(stem, useQuads);
			MeshPart meshpart = partCreator.createMeshPart();
			if (meshpart != null) { mesh.addMeshpart(meshpart); }
			return true; // proceed
		}	
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
		return true; // Mesh created successfully
	}

	@Override
	public boolean visitLeaf(Leaf leaf) {
		return false;
	}
}
