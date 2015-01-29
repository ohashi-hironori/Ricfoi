package jp.gr.java_conf.ricfoi.gui;
import java.awt.Color;

import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ParamGroupsView extends JTree {
	private static final long serialVersionUID = 1L;

	private static DefaultMutableTreeNode _root = new DefaultMutableTreeNode(GroupNode.LABEL_ROOT);
	private static final Color _bgClr = new Color(250,250,240);
	private ChangeEvent _changeEvent;

	public ParamGroupsView() {
		super(_root);
		
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		renderer.setBackground(_bgClr);
		renderer.setBackgroundNonSelectionColor(_bgClr);

		GroupNode general = new GroupNode(GroupNode.NAME_ROOT, GroupNode.LABEL_GENERAL, GroupNode.LEVEL_GENERAL);
		GroupNode firstGroup = new GroupNode(GroupNode.NAME_SHAPE, GroupNode.LABEL_SHAPE, GroupNode.LEVEL_GENERAL);
		general.add(firstGroup);
		general.add(new GroupNode(GroupNode.NAME_TRUNK, GroupNode.LABEL_TRUNK, GroupNode.LEVEL_GENERAL));
		general.add(new GroupNode(GroupNode.NAME_LEAVES, GroupNode.LABEL_LEAVES, GroupNode.LEVEL_GENERAL));
		general.add(new GroupNode(GroupNode.NAME_PRUNING, GroupNode.LABEL_PRUNING, GroupNode.LEVEL_GENERAL));
		general.add(new GroupNode(GroupNode.NAME_QUALITY, GroupNode.LABEL_QUALITY, GroupNode.LEVEL_GENERAL));
		_root.add(general);

		GroupNode level0 = new GroupNode(GroupNode.NAME_ROOT, GroupNode.LABEL_LEVEL0, GroupNode.LEVEL_0);
		level0.add(new GroupNode(GroupNode.NAME_LENTAPER, GroupNode.LABEL_LENTAPER, GroupNode.LEVEL_0));
		level0.add(new GroupNode(GroupNode.NAME_CURVATURE, GroupNode.LABEL_CURVATURE, GroupNode.LEVEL_0));
		level0.add(new GroupNode(GroupNode.NAME_SPLITTING, GroupNode.LABEL_SPLITTING, GroupNode.LEVEL_0));
		level0.add(new GroupNode(GroupNode.NAME_BRANCHING, GroupNode.LABEL_BRANCHING, GroupNode.LEVEL_0));
		_root.add(level0);

		GroupNode level1 = new GroupNode(GroupNode.NAME_ROOT, GroupNode.LABEL_LEVEL1, GroupNode.LEVEL_1);
		level1.add(new GroupNode(GroupNode.NAME_LENTAPER, GroupNode.LABEL_LENTAPER, GroupNode.LEVEL_1));
		level1.add(new GroupNode(GroupNode.NAME_CURVATURE, GroupNode.LABEL_CURVATURE, GroupNode.LEVEL_1));
		level1.add(new GroupNode(GroupNode.NAME_SPLITTING, GroupNode.LABEL_SPLITTING, GroupNode.LEVEL_1));
		level1.add(new GroupNode(GroupNode.NAME_BRANCHING, GroupNode.LABEL_BRANCHING, GroupNode.LEVEL_1));
		_root.add(level1);

		GroupNode level2 = new GroupNode(GroupNode.NAME_ROOT, GroupNode.LABEL_LEVEL2, GroupNode.LEVEL_2);
		level2.add(new GroupNode(GroupNode.NAME_LENTAPER, GroupNode.LABEL_LENTAPER, GroupNode.LEVEL_2));
		level2.add(new GroupNode(GroupNode.NAME_CURVATURE, GroupNode.LABEL_CURVATURE, GroupNode.LEVEL_2));
		level2.add(new GroupNode(GroupNode.NAME_SPLITTING, GroupNode.LABEL_SPLITTING, GroupNode.LEVEL_2));
		level2.add(new GroupNode(GroupNode.NAME_BRANCHING, GroupNode.LABEL_BRANCHING, GroupNode.LEVEL_2));
		_root.add(level2);

		GroupNode level3 = new GroupNode(GroupNode.NAME_ROOT, GroupNode.LABEL_LEVEL3, GroupNode.LEVEL_3);
		level3.add(new GroupNode(GroupNode.NAME_LENTAPER, GroupNode.LABEL_LENTAPER, GroupNode.LEVEL_3));
		level3.add(new GroupNode(GroupNode.NAME_CURVATURE, GroupNode.LABEL_CURVATURE, GroupNode.LEVEL_3));
		level3.add(new GroupNode(GroupNode.NAME_SPLITTING, GroupNode.LABEL_SPLITTING, GroupNode.LEVEL_3));
		level3.add(new GroupNode(GroupNode.NAME_BRANCHING, GroupNode.LABEL_BRANCHING, GroupNode.LEVEL_3));
		_root.add(level3);
		
		addTreeSelectionListener(
			new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)getLastSelectedPathComponent();
					if (node == null) return;
					fireStateChanged();
				}
			}
		);
		
		setBackground(_bgClr);
		setRootVisible(false);
		setShowsRootHandles(true);
		setExpandsSelectedPaths(true);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(renderer);
		setSelectionPath(new TreePath(firstGroup.getPath()));
	}

	protected void fireStateChanged() {
		Object [] listeners = listenerList.getListenerList();
		for (int i = listeners.length -2; i>=0; i-=2) {
			if (listeners[i] == ChangeListener.class) {
				if (_changeEvent == null) { _changeEvent = new ChangeEvent(this); }
				((ChangeListener)listeners[i+1]).stateChanged(_changeEvent);
			}
		}
	}

	public String getGroupName() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)getLastSelectedPathComponent();
		if (!node.isRoot()) { return ((GroupNode)node).getGroupName(); }
		return GroupNode.NAME_ROOT;
	}
	
	public int getGroupLevel() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)getLastSelectedPathComponent();
		if (!node.isRoot()) { return ((GroupNode)node).getGroupLevel(); }
		return 0;
	}
	
	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

}