package jp.gr.java_conf.ricfoi.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.export.ExporterFactory;

public class RenderPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final String LABEL_RENDER_CHECKBOX = "Render scene to:";
	private static final String LABEL_CHOOSE_RENDERFILE = "Choose...";
	private static final String LABEL_IMAGE_WIDTH = "Image width:";
	private static final String LABEL_IMAGE_HEIGHT = "Image height:";
	
	JFrame parent;
	
	JCheckBox renderCheckbox = new JCheckBox(LABEL_RENDER_CHECKBOX);
	JTextField renderFileField = new JTextField(30);
	JButton selectRenderFile = new JButton(LABEL_CHOOSE_RENDERFILE);
	JLabel labelWidth = new JLabel(LABEL_IMAGE_WIDTH);
	JTextField widthField=new JTextField(6);
	JLabel labelHeight = new JLabel(LABEL_IMAGE_HEIGHT);
	JTextField heightField = new JTextField(6);
	
	private int line=-1;
	private static final String DefaultFullName = ExporterFactory.getOutputPath()+Config.FILE_SEPARATOR+Workplace.getParams().getSpecies();
	
	/**
	 * Create the panel.
	 */
	public RenderPanel(JFrame parent, boolean render) {
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
		
		// path and filename of rendered image
		clabel.gridy = ++line;
		renderCheckbox.setSelected(render);
		grid.setConstraints(renderCheckbox,clabel);
		add(renderCheckbox);
		
		ctext.gridy = line;
		renderFileField.setEnabled(renderCheckbox.isSelected());
		renderFileField.setText(DefaultFullName+Constant.FS+Constant.PNG);
		renderFileField.setMinimumSize(new Dimension(250,19));
		grid.setConstraints(renderFileField,ctext);
		add(renderFileField);
		
		cbutton.gridy = line;
		selectRenderFile.setEnabled(renderCheckbox.isSelected());
		grid.setConstraints(selectRenderFile,cbutton);
		add(selectRenderFile);
		
		// render width
		clabel.gridy = ++line;
		grid.setConstraints(labelWidth,clabel);
		add(labelWidth);
		
		ctext.gridy = line;
		widthField.setText(Integer.toString(ExporterFactory.getRenderWidth()));
		widthField.setMinimumSize(new Dimension(80,19));
		grid.setConstraints(widthField,ctext);
		add(widthField);
		
		// render height
		clabel.gridy = ++line;
		grid.setConstraints(labelHeight,clabel);
		add(labelHeight);
		
		ctext.gridy = line;
		heightField.setText(Integer.toString(ExporterFactory.getRenderHeight()));
		heightField.setMinimumSize(new Dimension(80,19));
		grid.setConstraints(heightField,ctext);
		add(heightField);
		
		addListener();
	}

	private void addListener() {
		
		renderCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				renderFileField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
				selectRenderFile.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		selectRenderFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				File file = new File(renderFileField.getText());
				while(!file.exists()) {file = new File(file.getParent());}
				JFileChooser renderFileChooser = new JFileChooser();
				renderFileChooser.setCurrentDirectory(file);
				renderFileChooser.setFileFilter(new FileNameExtensionFilter("Portable Network Graphics (*.png)", Constant.PNG));
				int returnVal = renderFileChooser.showSaveDialog(parent);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					renderFileField.setText(renderFileChooser.getSelectedFile().getPath());
				}
			}
		});
	}

}
