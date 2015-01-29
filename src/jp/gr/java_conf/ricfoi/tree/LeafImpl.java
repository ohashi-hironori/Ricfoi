package jp.gr.java_conf.ricfoi.tree;

import jp.gr.java_conf.ricfoi.math.Transformation;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.type.Vector;

public class LeafImpl implements Leaf {

	public Transformation transf;

	public LeafImpl(Transformation trf) {
		transf = trf;
	}

	@Override
	public Transformation getTransformation() {
		return transf;
	}

	public void make(Params par) {
		setLeafOrientation(par);
	}

	private void setLeafOrientation(Params par) {
		if (par.getLeafBend()==0) return;
		
		// FIXME: make this function as fast as possible - a tree has a lot of leafs
		// rotation outside 
		Vector pos = transf.getT();
		// the z-vector of transf is parallel to the
		// axis of the leaf, the y-vector is the normal
		// (of the upper side) of the leaf
		Vector norm = transf.getY();
		double tpos = Vector.atan2(pos.getY(),pos.getX());
		double tbend = tpos - Vector.atan2(norm.getY(),norm.getX());
		double bend_angle = par.getLeafBend()*tbend;
		// rotate about global z-axis
		transf = transf.rotationAxis(bend_angle,Vector.Z_AXIS);
		// rotation up
		norm = transf.getY();
		double fbend = Vector.atan2(Math.sqrt(norm.getX()*norm.getX() + norm.getY()*norm.getY()),norm.getZ());
		
		bend_angle = par.getLeafBend()*fbend;
		transf = transf.rotationX(bend_angle); 
	}
	
	@Override
	public boolean traverseTree(TreeTraversal traversal) {
		return traversal.visitLeaf(this);
	}

}
