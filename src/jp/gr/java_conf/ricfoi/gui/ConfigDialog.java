package jp.gr.java_conf.ricfoi.gui;

import java.awt.Component;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jp.gr.java_conf.ricfoi.export.ExporterFactory;

public class ConfigDialog extends JFrame {
	private final static long serialVersionUID = 1L;

	private final static String TITLE = "Setup";
	private final static String LABEL_FORMAT = "Default export format:";
	private final static String LABEL_OUTPATH = "Output path:";
	private final static String LABEL_CHOOSER = "Choose...";
	private final static String LABEL_POVEXE = "POVRay executable:";
	private final static String LABEL_WIDYH = "Render width:";
	private final static String LABEL_HEIGHT = "Render height:";
	private final static String LBEL_SEED = "Default seed:";
	private final static String LABEL_ANTIALIAS = "Antialias:";
	private final static String TEXT_ANTIALIAS = "use antialiasing in tree preview";
	private final static String LABEL_BUTTON_OK = "OK";
	private final static String LABEL_BUTTON_CANCEL = "Cancel";
	private final static String TITLE_ERROR_DIALOG = "Setup Error";

	/**
	 * Create the panel.
	 */
	public ConfigDialog() {
		super(TITLE);
		setIconImage(Workplace.getParentFrame().getIconImage());
		ConfigDialog dialog = this;
		Config config = Workplace.getConfig();
		int line = -1;

		JPanel mainPanel = new JPanel();
		GridBagLayout grid = new GridBagLayout();
		mainPanel.setLayout(grid);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
		
		GridBagConstraints clabel = new GridBagConstraints();
		clabel.gridx = 0;
		clabel.anchor = GridBagConstraints.WEST;
		clabel.gridy = ++line;
		
		GridBagConstraints ctext = new GridBagConstraints();
		ctext.gridx = 1;
		ctext.ipady = 4;
		ctext.anchor = GridBagConstraints.WEST;
		ctext.insets = new Insets(1,5,1,5);
		
		GridBagConstraints cbutton = new GridBagConstraints();
		cbutton.gridx = 2;
		cbutton.anchor = GridBagConstraints.WEST;
		

		JLabel label = new JLabel(LABEL_FORMAT);
		grid.setConstraints(label,clabel);
		mainPanel.add(label);
		
		ctext.gridy = line;
		JComboBox<String> formatBox = new JComboBox<>(ExporterFactory.getFormats());
		formatBox.setEditable(false);
		formatBox.setSelectedIndex(Integer.parseInt(config.getProperty(Config.KEY_EXPORT_FORMAT)));
		grid.setConstraints(formatBox,ctext);
		mainPanel.add(formatBox);
		
		// default output path
		clabel.gridy = ++line;
		label = new JLabel(LABEL_OUTPATH);
		grid.setConstraints(label,clabel);
		mainPanel.add(label);
		
		ctext.gridy = line;
		JTextField pathField = new JTextField(30);
		pathField.setText(config.getProperty(Config.KEY_EXPORT_PATH));
		grid.setConstraints(pathField,ctext);
		mainPanel.add(pathField);
		
		cbutton.gridy = line;
		JButton selectFile = new JButton(LABEL_CHOOSER);
		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				File file = new File(pathField.getText());
				while(!file.exists()) {file = new File(file.getParent());}
				JFileChooser _fileChooser = new JFileChooser(file);
				_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
				int returnVal = _fileChooser.showOpenDialog(dialog);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					pathField.setText(_fileChooser.getSelectedFile().getPath());
				}
			}
		});
		grid.setConstraints(selectFile,cbutton);
		mainPanel.add(selectFile);

		// povray executable  
		clabel.gridy = ++line;
		label = new JLabel(LABEL_POVEXE);
		grid.setConstraints(label,clabel);
		mainPanel.add(label);
		
		ctext.gridy = line;
		JTextField fileField = new JTextField(30);
		fileField.setText(config.getProperty(Config.KEY_POVRAY_EXE));
		grid.setConstraints(fileField,ctext);
		mainPanel.add(fileField);
		
		cbutton.gridy = line;
		selectFile = new JButton(LABEL_CHOOSER);
		selectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				File file = new File(fileField.getText());
				if(file.getParent() != null) {
					while(!file.exists()) { file = new File(file.getParent()); }
				}
				JFileChooser _fileChooser = new JFileChooser(file);
				int returnVal = _fileChooser.showOpenDialog(dialog);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					fileField.setText(_fileChooser.getSelectedFile().getPath());
				}
			}
		});
		grid.setConstraints(selectFile,cbutton);
		mainPanel.add(selectFile);

		// render width 
		clabel.gridy = ++line;
		label = new JLabel(LABEL_WIDYH);
		grid.setConstraints(label,clabel);
		mainPanel.add(label);
		
		ctext.gridy = line;
		JTextField widthField = new JTextField(10);
		widthField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		widthField.setText(config.getProperty(Config.KEY_POVRAY_WIDTH));
		grid.setConstraints(widthField,ctext);
		mainPanel.add(widthField);

		// render height
		clabel.gridy = ++line;
		label = new JLabel(LABEL_HEIGHT);
		grid.setConstraints(label,clabel);
		mainPanel.add(label);
		
		ctext.gridy = line;
		JTextField heightField = new JTextField(10);
		heightField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		heightField.setText(config.getProperty(Config.KEY_POVRAY_HEIGHT));
		grid.setConstraints(heightField,ctext);
		mainPanel.add(heightField);

		// random seed
		clabel.gridy = ++line;
		label = new JLabel(LBEL_SEED);
		grid.setConstraints(label,clabel);
		mainPanel.add(label);
		
		ctext.gridy = line;
		JTextField seedField = new JTextField(10);
		seedField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		seedField.setText(config.getProperty(Config.KEY_TREE_SEED));
		grid.setConstraints(seedField,ctext);
		mainPanel.add(seedField);
		
		// antialias
		clabel.gridy = ++line;
		label = new JLabel(LABEL_ANTIALIAS);
		grid.setConstraints(label,clabel);
		mainPanel.add(label);
		
		ctext.gridy = line;
		JCheckBox antialiasBox = new JCheckBox();
		antialiasBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		antialiasBox.setText(TEXT_ANTIALIAS);
		antialiasBox.setSelected(config.getProperty(Config.KEY_PREVIEW_ANTIALIAS).equals(Config.PREVIEW_ANTIALIAS_ON));
		grid.setConstraints(antialiasBox,ctext);
		mainPanel.add(antialiasBox);
		
		// buttons
		JButton okButton = new JButton(LABEL_BUTTON_OK);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				config.setProperty(Config.KEY_EXPORT_FORMAT, Integer.toString(formatBox.getSelectedIndex()));
				config.setProperty(Config.KEY_EXPORT_PATH, pathField.getText());
				config.setProperty(Config.KEY_POVRAY_EXE, fileField.getText());
				config.setProperty(Config.KEY_POVRAY_WIDTH, widthField.getText());
				config.setProperty(Config.KEY_POVRAY_HEIGHT, heightField.getText());
				config.setProperty(Config.KEY_TREE_SEED, seedField.getText());
				if (antialiasBox.isSelected()) {
					config.setProperty(Config.KEY_PREVIEW_ANTIALIAS, Config.PREVIEW_ANTIALIAS_ON);
				} else {
					config.setProperty(Config.KEY_PREVIEW_ANTIALIAS, Config.PREVIEW_ANTIALIAS_OFF);
				}
				dispose();
				try {
					config.store();
					ExporterFactory.apply();
				} catch (Exception err) {
					JOptionPane.showMessageDialog(
							Workplace.getParentFrame(),
							err.getMessage(),
							TITLE_ERROR_DIALOG,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		JButton cancelButton = new JButton(LABEL_BUTTON_CANCEL);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel buttons = new JPanel();
		buttons.add(okButton);
		buttons.add(cancelButton);
		
		cbutton.gridx = 1;
		cbutton.gridy = 7;
		cbutton.anchor = GridBagConstraints.CENTER;
		grid.setConstraints(buttons,cbutton);
		mainPanel.add(buttons);
		
		add(mainPanel);
		pack();
		setVisible(true);
	}

}
