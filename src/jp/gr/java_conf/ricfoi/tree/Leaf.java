package jp.gr.java_conf.ricfoi.tree;

import jp.gr.java_conf.ricfoi.math.Transformation;

public interface Leaf {

	/**
	 * used with TreeTraversal interface
	 *  
	 * @param traversal
	 * @return when false stop travers tree at this level
	 */
	abstract boolean traverseTree(TreeTraversal traversal);

	/**
	 * @return the leaf's transformation matrix containing
	 * the position vector and the rotation matrix.
	 */
	public abstract Transformation getTransformation();

}
