/**
 * 
 */
package jp.gr.java_conf.ricfoi.tree;

/**
 * An interface, for traversal through the stems and leaves of a tree.
 * (Compare Hierarchical Visitor Pattern)
 */
public interface TreeTraversal {

	/**
	 * going into a Tree
	 * 
	 * @param tree
	 * @return when false, stop traversal at this level
	 */
	boolean enterTree(Tree tree); 
	
	/**
	 * coming out of a Tree
	 * 
	 * @param tree
	 * @return when false, stop traversal at this level
	 */
	boolean leaveTree(Tree tree);
	
	/**
	 * going into a Stem
	 * 
	 * @param stem
	 * @return when false, stop traversal at this level
	 */
	boolean enterStem(Stem stem);
	
	/**
	 * coming out of a Stem
	 * 
	 * @param stem
	 * @return when false, stop traversal at this level
	 */
	boolean leaveStem(Stem stem);
	
	/**
	 * passing a Leaf
	 * 
	 * @param leaf
	 * @return when false, stop traversal at this level
	 */
	boolean visitLeaf(Leaf leaf); 

}
