package jp.gr.java_conf.ricfoi.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.export.ExporterFactory;
import jp.gr.java_conf.ricfoi.params.Params;

public class ExportPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private int line=-1;
	private static final String DefaultFullName = ExporterFactory.getOutputPath()+Config.FILE_SEPARATOR+Workplace.getParams().getSpecies();
	
	JLabel labelFormat = new JLabel("Export format:");
	JComboBox<String> formatBox = new JComboBox<>(ExporterFactory.getFormats());
	JLabel labelFile = new JLabel("Export to file:");
	JTextField fileField = new JTextField(30);
	JButton selectFile = new JButton("Choose...");
	JLabel labelUV = new JLabel("UV-coordinates:");
	JCheckBox uvStemsCheckbox = new JCheckBox("for Stems");
	JCheckBox uvLeavesCheckbox = new JCheckBox("for Leaves");
	JCheckBox sceneCheckbox = new JCheckBox("POV Scene file:");
	JTextField sceneFileField = new JTextField(30);
	JButton selectSceneFile = new JButton("Choose...");
	JFileChooser sceneFileChooser = new JFileChooser();
	JLabel  labelSeed = new JLabel("Seed:");
	JTextField seedField = new JTextField(6);
	private JLabel labelSmooth = new JLabel("Smooth value:");
	JTextField smoothField = new JTextField(6);
	
	JFrame parent;
	
	/**
	 * Create the panel.
	 */
	public ExportPanel(JFrame parent, boolean render) {
		super();
		this.parent = parent;
		// create GridBagLayout
		GridBagLayout grid = new GridBagLayout();
		setLayout(grid);
		setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
		
		// Constraints for the left labels
		GridBagConstraints clabel = new GridBagConstraints();
		clabel.gridx = 0;
		clabel.anchor = GridBagConstraints.WEST;
		
		// Constraint for the input fields
		GridBagConstraints ctext = new GridBagConstraints();
		ctext.gridx = 1;
		ctext.ipady = 4;
		ctext.anchor = GridBagConstraints.WEST;
		ctext.insets = new Insets(1,5,1,5);
		
		// Constraint for the choose buttons
		GridBagConstraints cbutton = new GridBagConstraints();
		cbutton.gridx = 2;
		cbutton.anchor = GridBagConstraints.WEST;
		
		// export format
		clabel.gridy = ++line;
		grid.setConstraints(labelFormat,clabel);
		add(labelFormat);
		
		ctext.gridy = line;
		int format = Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_EXPORT_FORMAT)); 
		if (render) {
			for (int i=formatBox.getItemCount()-1; i>=ExporterFactory.DXF; i--) {
				formatBox.removeItemAt(i);
			}
			if (format>=ExporterFactory.DXF) format=ExporterFactory.POV_MESH;
		}
		formatBox.setEditable(false);
		formatBox.setSelectedIndex(format);
		grid.setConstraints(formatBox,ctext);
		add(formatBox);
		
		
		// export file path and name 
		clabel.gridy = ++line;
		grid.setConstraints(labelFile,clabel);
		add(labelFile);
		
		ctext.gridy = line;
		fileField.setText(DefaultFullName+Constant.FS+Constant.INC);
		fileField.setMinimumSize(new Dimension(250,19));
		grid.setConstraints(fileField,ctext);
		add(fileField);
		
		cbutton.gridy = line;
		grid.setConstraints(selectFile,cbutton);
		add(selectFile);
		
		// UV-coordinates
		clabel.gridy = ++line;
		grid.setConstraints(labelUV,clabel);
		add(labelUV);
		
		ctext.gridy = line;
		uvStemsCheckbox.setSelected(ExporterFactory.isOutputStemUVs());
		uvLeavesCheckbox.setSelected(ExporterFactory.isOutputLeafUVs());
		JPanel uv = new JPanel();
		uv.add(uvStemsCheckbox);
		uv.add(uvLeavesCheckbox);
		grid.setConstraints(uv,ctext);
		add(uv);
		
		// POV scene file 
		clabel.gridy = ++line;
		sceneCheckbox.setSelected(render);
		grid.setConstraints(sceneCheckbox,clabel);
		add(sceneCheckbox);
		
		ctext.gridy = line;
		sceneFileField.setEnabled(sceneCheckbox.isSelected());
		sceneFileField.setText(DefaultFullName+Constant.FS+Constant.POV);
		sceneFileField.setMinimumSize(new Dimension(250,19));
		grid.setConstraints(sceneFileField,ctext);
		add(sceneFileField);
		
		cbutton.gridy = line;
		selectSceneFile.setEnabled(sceneCheckbox.isSelected());
		grid.setConstraints(selectSceneFile,cbutton);
		add(selectSceneFile);
		
		// random seed
		clabel.gridy = ++line;
		grid.setConstraints(labelSeed,clabel);
		add(labelSeed);
		
		ctext.gridy = line;
		seedField.setText(Workplace.getConfig().getProperty(Config.KEY_TREE_SEED)); 
		seedField.setMinimumSize(new Dimension(80,19));
		grid.setConstraints(seedField,ctext);
		add(seedField);
		
		// smooth value
		clabel.gridy = ++line;
		grid.setConstraints(labelSmooth,clabel);
		add(labelSmooth);
		
		ctext.gridy = line;
		smoothField.setText(Workplace.getParams().getParam(Params.NAME_SMOOTH).toString());
		smoothField.setMinimumSize(new Dimension(80,19));
		grid.setConstraints(smoothField,ctext);
		add(smoothField);
		
		addListener();
	}

	private void addListener() {
		
		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				File file = new File(fileField.getText());
				while(!file.exists()) {file = new File(file.getParent());}
				JFileChooser incChooser = new JFileChooser();
				incChooser.setCurrentDirectory(file);
				
				switch (formatBox.getSelectedIndex()) {
				case ExporterFactory.POV_MESH: 
				case ExporterFactory.POV_CONES: 
					incChooser.setFileFilter(new FileNameExtensionFilter("Include Files (*.inc)", Constant.INC));
					break;
				case ExporterFactory.DXF: 
					incChooser.setFileFilter(new FileNameExtensionFilter("AutoCAD (*.dxf)",Constant.DXF));
					break;
				case ExporterFactory.OBJ: 
					incChooser.setFileFilter(new FileNameExtensionFilter("Wavefront (*.obj)",Constant.OBJ));
					break;
				default:
					break;
				}
				
				int returnVal = incChooser.showSaveDialog(parent);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					fileField.setText(incChooser.getSelectedFile().getPath());
				}
			}
		});

		selectSceneFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				File file = new File(sceneFileField.getText());
				while(!file.exists()) {file = new File(file.getParent());}
				JFileChooser povChooser = new JFileChooser();
				povChooser.setCurrentDirectory(file);
				povChooser.setFileFilter(new FileNameExtensionFilter("POV-Ray Source (*.pov)",Constant.POV));
				int returnVal = povChooser.showSaveDialog(parent);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					sceneFileField.setText(povChooser.getSelectedFile().getPath());
				}
			}
		});

	}

}
