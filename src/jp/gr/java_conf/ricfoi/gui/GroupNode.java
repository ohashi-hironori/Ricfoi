package jp.gr.java_conf.ricfoi.gui;

import javax.swing.tree.DefaultMutableTreeNode;

import jp.gr.java_conf.ricfoi.Constant;

public class GroupNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;

	private String _groupName;
	private int _groupLevel;
	
	/**
	 * value is ""
	 */
	public static final String NAME_ROOT      = Constant.ZS;
	/**
	 * valus is "SHAPE"
	 */
	public static final String NAME_SHAPE     = "SHAPE";
	/**
	 * value is "TRUNK"
	 */
	public static final String NAME_TRUNK     = "TRUNK";
	/**
	 * value is "LEAVES"
	 */
	public static final String NAME_LEAVES    = "LEAVES";
	/**
	 * value is "PRUNING"
	 */
	public static final String NAME_PRUNING   = "PRUNING";
	/**
	 * value is "QUALITY"
	 */
	public static final String NAME_QUALITY   = "QUALITY";
	/**
	 * value is "LENTAPER"
	 */
	public static final String NAME_LENTAPER  = "LENTAPER";
	/**
	 * value is "CURVATURE"
	 */
	public static final String NAME_CURVATURE = "CURVATURE";
	/**
	 * value is "SPLITTING"
	 */
	public static final String NAME_SPLITTING = "SPLITTING";
	/**
	 * value is "BRANCHING"
	 */
	public static final String NAME_BRANCHING = "BRANCHING";

	/**
	 * value is "root"
	 */
	public static final String LABEL_ROOT      = "root";
	/**
	 * value is "General"
	 */
	public static final String LABEL_GENERAL   = "General";
	/**
	 * value is "Tree shape"
	 */
	public static final String LABEL_SHAPE     = "Tree shape";
	/**
	 * value is "Trunk radius"
	 */
	public static final String LABEL_TRUNK     = "Trunk radius";
	/**
	 * value is "Leaves"
	 */
	public static final String LABEL_LEAVES    = "Leaves";
	/**
	 * value is "Pruning/Envelope"
	 */
	public static final String LABEL_PRUNING   = "Pruning/Envelope";
	/**
	 * value is "Quality"
	 */
	public static final String LABEL_QUALITY   = "Quality";
	/**
	 * value is "Level 0 (trunk)"
	 */
	public static final String LABEL_LEVEL0    = "Level 0 (trunk)";
	/**
	 * value is "Level 1"
	 */
	public static final String LABEL_LEVEL1    = "Level 1";
	/**
	 * value is "Level 2"
	 */
	public static final String LABEL_LEVEL2    = "Level 2";
	/**
	 * value is "Level 3"
	 */
	public static final String LABEL_LEVEL3    = "Level 3";
	/**
	 * value is "Length and taper"
	 */
	public static final String LABEL_LENTAPER  = "Length and taper";
	/**
	 * value is "Curvature"
	 */
	public static final String LABEL_CURVATURE = "Curvature";
	/**
	 * value is "Splitting"
	 */
	public static final String LABEL_SPLITTING = "Splitting";
	/**
	 * value is "Branching"
	 */
	public static final String LABEL_BRANCHING = "Branching";

	public static final int LEVEL_GENERAL = -999; // no level - general params
	public static final int LEVEL_0 = 0;
	public static final int LEVEL_1 = 1;
	public static final int LEVEL_2 = 2;
	public static final int LEVEL_3 = 3;

	public GroupNode(String name, String label, int level) {
		super(label);
		_groupName = name;
		_groupLevel = level;
	}
	
	public String getGroupName() {
		return _groupName;
	}

	public int getGroupLevel() {
		return _groupLevel;
	}

}