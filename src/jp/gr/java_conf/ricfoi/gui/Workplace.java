package jp.gr.java_conf.ricfoi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.action.QuitAction;
import jp.gr.java_conf.ricfoi.export.ExporterFactory;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.tree.PreviewTree;

public class Workplace extends JFrame {
	private static final long serialVersionUID = 1L;

	private static final Color _bgClr = new Color(242,242,229);
	
	private static JFrame _frame;
	private static Config _config;
	private static Params _params;
	private static boolean _modified;
	private static String _treefile;
	static PreviewTree _previewTree;
	static ParamGroupsView _tree;
	static ParamValueTable _valueEditor;
	ImageLabel _imageLabel;
	JSlider _rotator;
	TreePreview _topView;
	TreePreview _frontView;

	/**
	 * Create the frame.
	 */
	public Workplace() {
		super(Ricfoi.NAME);
		
		setParentFrame(this);
		setConfig(new Config());
		setParams(new Params());
		setTreefile(null);
		
		ExporterFactory.apply();
		
		_params.prepare(Integer.parseInt(_config.getProperty(Config.KEY_TREE_SEED)));
		_tree = new ParamGroupsView();
		_valueEditor = new ParamValueTable(_params);
		_previewTree = new PreviewTree(_params);
		_topView = new TreePreview(_previewTree, TreePreview.PERSPECTIVE_TOP, _config);
		_topView.setOpaque(true); 
		_topView.setBackground(_bgClr);
		_frontView = new TreePreview(_previewTree, TreePreview.PERSPECTIVE_FRONT, _config);
		_frontView.setOpaque(true);
		_frontView.setBackground(Color.WHITE);
		_rotator = new JSlider();
		_rotator.setMinimum(-180);
		_rotator.setMaximum(180);
		_rotator.setPaintLabels(true);
		_rotator.setPaintTicks(true);
		_rotator.setPaintTrack(true);
		_rotator.setMinorTickSpacing(10);
		_rotator.setMajorTickSpacing(90);
		_rotator.setBackground(new Color(250,250,245));
		_rotator.setBorder(BorderFactory.createMatteBorder(0,0,0,1,getBackground()));
		
		setTitle(Ricfoi.NAME+'['+getParams().getSpecies()+']');
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new QuitAction());
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(Ricfoi.class.getResource(Ricfoi.ICON32)));
		setJMenuBar(new MenuBar());
		getContentPane().add(new ToolBar(), BorderLayout.PAGE_START);
		
		// main pane
		JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainPane.setResizeWeight(0.25);
		mainPane.setPreferredSize(new Dimension(800,600));
		
		JPanel leftPane = new JPanel();
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
		leftPane.setOpaque(false);
		leftPane.add(new JScrollPane(_tree));
		leftPane.add(_valueEditor);
		mainPane.add(leftPane, JSplitPane.LEFT);
		
		JPanel rightPane = new JPanel();
		GridBagLayout grid = new GridBagLayout();
		rightPane.setLayout(grid);
		rightPane.setOpaque(true);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(1,1,0,0);
		JPanel sliderPane = new JPanel();
		sliderPane.setLayout(new BorderLayout());
		sliderPane.add(_frontView, BorderLayout.CENTER);
		sliderPane.add(_rotator, BorderLayout.SOUTH);
		constraints.weightx=1.0;
		constraints.weighty=1.0;
		constraints.fill = GridBagConstraints.BOTH;		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		grid.setConstraints(sliderPane, constraints);
		rightPane.add(sliderPane);
		
		constraints.weightx=0.2;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		grid.setConstraints(_topView, constraints);
		rightPane.add(_topView);
		
		_imageLabel = new ImageLabel();
		constraints.weighty=0.2;
		constraints.gridx = 2;
		constraints.gridy = 1;
		grid.setConstraints(_imageLabel, constraints);
		rightPane.add(_imageLabel);
		
		mainPane.add(rightPane, JSplitPane.RIGHT);
		
		addListener();
		
		getContentPane().add(mainPane, BorderLayout.CENTER);
		pack();
		_tree.fireStateChanged();
		setModified(false);
	}

	private void addListener() {
		
		_valueEditor.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setModified(true);
				_previewTree.remake(true);
				getParentFrame().setTitle(Ricfoi.NAME+'['+getParams().getSpecies()+']');
			}
		});

		_tree.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int level = _tree.getGroupLevel();
				String group = _tree.getGroupName();
				
				// show parameters in value editor
				_valueEditor.showGroup(group,level);
				
				// change preview trees level
				if (level==GroupNode.LEVEL_GENERAL) {
//					if (group.equals(GroupNode.NAME_LEAVES) || group.equals("LEAVESADD")) {
					if (group.equals(GroupNode.NAME_LEAVES)) {
						_previewTree.setShowLevel(getParams().getLevels());
					} else {
						_previewTree.setShowLevel(1);
					}
				} else {
					_previewTree.setShowLevel(level);
				}
				
				// change explaining image
				ImageIcon icon = ImageLabel.ICON_SHAPE;
				if (group.equals(GroupNode.NAME_SHAPE)) icon = ImageLabel.ICON_SHAPE;
				else if (group.equals(GroupNode.NAME_TRUNK)) icon=ImageLabel.ICON_RADIUS;
//				else if (group.equals(GroupNode.NAME_LEAVES) || group.equals("LEAVESADD")) icon=ImageLabel.ICON_LEAVES;
				else if (group.equals(GroupNode.NAME_LEAVES)) icon=ImageLabel.ICON_LEAVES;
				else if (group.equals(GroupNode.NAME_PRUNING)) icon=ImageLabel.ICON_PRUNE;
//				else if (group.equals("MISC")) icon=ImageLabel.ICON_MISC;
				else if (group.equals(GroupNode.NAME_LENTAPER)) icon=ImageLabel.ICON_LEN_TAP;
				else if (group.equals(GroupNode.NAME_CURVATURE)) icon=ImageLabel.ICON_CURVE;
				else if (group.equals(GroupNode.NAME_SPLITTING)) icon=ImageLabel.ICON_SPLIT;
				else if (group.equals(GroupNode.NAME_BRANCHING)) icon=ImageLabel.ICON_SUBSTEM;
				
				_imageLabel.setIcon(icon);
				((TitledBorder)_imageLabel.getBorder()).setTitle(icon.getDescription());
				_previewTree.remake(true);
			}
		});
		
		_rotator.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				// fast draw while adjusting rotation slider
				_frontView.setDraft(source.getValueIsAdjusting());
				_topView.setDraft(source.getValueIsAdjusting());
				// set new rotation
				_frontView.setRotation(_rotator.getValue());
				_topView.setRotation(_rotator.getValue());
			}
		});

	}
	
	/**
	 * @return ParentFrame
	 */
	public static JFrame getParentFrame() {
		return _frame;
	}

/**
	 * @param frame セットする _frame
	 */
	public static void setParentFrame(JFrame frame) {
		_frame = frame;
	}

	/**
	 * @return Config
	 */
	public static Config getConfig() {
		return _config;
	}

	/**
	 * @param config セットする config
	 */
	public static void setConfig(Config config) {
		_config = config;
	}

	/**
	 * @return Params
	 */
	public static Params getParams() {
		return _params;
	}

	/**
	 * @param params セットする params
	 */
	public static void setParams(Params params) {
		_params = params;
	}

	/**
	 * @return Modified
	 */
	public static boolean isModified() {
		return _modified;
	}

	/**
	 * @param modified セットする modified
	 */
	public static void setModified(boolean modified) {
		_modified = modified;
		_params.enableDisable();
	}

	public static void stopEditing() {
		_valueEditor.stopEditing();
	}

	/**
	 * @return treefile
	 */
	public static String getTreefile() {
		return _treefile;
	}

	/**
	 * @param treefile セットする treefile
	 */
	public static void setTreefile(String treefile) {
		_treefile = treefile;
	}

	public static void fireStateChanged() {
		_tree.fireStateChanged();
	}

	public static void remakeTree() {
		_previewTree.setParams(_params);
		_previewTree.remake(true);
	}

}
