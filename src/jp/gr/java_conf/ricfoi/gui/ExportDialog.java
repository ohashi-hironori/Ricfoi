package jp.gr.java_conf.ricfoi.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.action.TreeCreationTask;
import jp.gr.java_conf.ricfoi.export.ExporterFactory;
import jp.gr.java_conf.ricfoi.params.Params;
import jp.gr.java_conf.ricfoi.tree.ShieldedTreeGenerator;
import jp.gr.java_conf.ricfoi.tree.TreeGenerator;
import jp.gr.java_conf.ricfoi.tree.TreeGeneratorFactory;

public class ExportDialog extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final static int INTERVAL = 500; // 0.5 sec
	
	JTabbedPane tabbedPane;
	ExportPanel exportPanel;
	RenderPanel renderPanel;
	JButton startButton = new JButton("Start");
	JButton cancelButton = new JButton("Close");
	ProgressBar progressbar = new ProgressBar();
	Timer timer;
	TreeCreationTask treeCreationTask = new TreeCreationTask();
	
	JFrame frame;
	
	/**
	 * Create the frame.
	 */
	public ExportDialog(boolean render) {
		super("Create and export tree");
		frame = this;
		setIconImage(Workplace.getParentFrame().getIconImage());

		tabbedPane = new JTabbedPane();

		exportPanel = new ExportPanel(frame, render);
		tabbedPane.addTab("Export", null, exportPanel, "Export options");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		renderPanel = new RenderPanel(frame, render);
		tabbedPane.addTab("Render", null, renderPanel, "Render options");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		if (render) { tabbedPane.setSelectedIndex(1); }
		
		formatSettings(exportPanel.formatBox.getSelectedIndex());
		
		JPanel buttons = new JPanel();
		buttons.add(startButton);
		buttons.add(cancelButton);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(tabbedPane,BorderLayout.CENTER);
		panel.add(buttons,BorderLayout.SOUTH);
		add(panel,BorderLayout.CENTER);

		progressbar.setVisible(false);
		add(progressbar,BorderLayout.PAGE_END);

		addListener();
		
		setVisible(true);
		pack();
	}

	private void addListener() {
		
		timer = new Timer(INTERVAL,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (treeCreationTask.notActive()) 
				{
					treeCreationTask.stop();
					progressbar.setProgress(100);
					progressbar.setNote("Ready");
					Toolkit.getDefaultToolkit().beep();
					timer.stop();
					startButton.setEnabled(true);
					startButton.setText("Restart");
					cancelButton.setText("Close");
//				} else {
//					progressbar.setProgress(treeCreationTask.getProgress());
//					progressbar.setNote(treeCreationTask.getProgressMsg());
				}
			}
		});
		
		exportPanel.formatBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				formatSettings(exportPanel.formatBox.getSelectedIndex());
			}
		});
		
		exportPanel.sceneCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				exportPanel.sceneFileField.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
				exportPanel.selectSceneFile.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
				renderPanel.renderCheckbox.setSelected(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
//				TreeGenerator treeGenerator = new ShieldedGUITreeGenerator(frame, TreeGeneratorFactory.createTreeGenerator(Workplace.getParams()));
				TreeGenerator treeGenerator = new ShieldedTreeGenerator(TreeGeneratorFactory.createTreeGenerator(Workplace.getParams()));
				
				try{
					treeGenerator.setSeed(Integer.parseInt(exportPanel.seedField.getText()));
					treeGenerator.setParam(Params.NAME_SMOOTH,exportPanel.smoothField.getText());
					ExporterFactory.setRenderWidth(Integer.parseInt(renderPanel.widthField.getText()));
					ExporterFactory.setRenderHeight(Integer.parseInt(renderPanel.heightField.getText()));
					ExporterFactory.setExportFormat(exportPanel.formatBox.getSelectedIndex());
					ExporterFactory.setOutputStemUVs(exportPanel.uvStemsCheckbox.isSelected());
					ExporterFactory.setOutputLeafUVs(exportPanel.uvLeavesCheckbox.isSelected());
				} catch (Exception exc) {
//					Console.printException(exc);
//					ShowException.msgBox(frame,"Export initialization error",exc);
				}
				
				progressbar.setVisible(true);
				
				startButton.setEnabled(false);
				cancelButton.setText("Cancel");
				
				// start tree creation
				File incfile = new File(exportPanel.fileField.getText());
				File povfile = null;
				if (exportPanel.sceneCheckbox.isSelected()) povfile = new File(exportPanel.sceneFileField.getText());
				String imgFilename = null;
				if (renderPanel.renderCheckbox.isSelected()) imgFilename = renderPanel.renderFileField.getText();
				treeCreationTask.start(treeGenerator, incfile,povfile,imgFilename);
				timer.start();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if (treeCreationTask.notActive())
					frame.dispose();
				else {
					treeCreationTask.stop();
					Toolkit.getDefaultToolkit().beep();
					timer.stop();
					startButton.setEnabled(true);
					startButton.setText("Restart");
					cancelButton.setText("Close");
					progressbar.setVisible(false);
				}
			}
		});
	}

	void formatSettings(int outputFormat) {
		File exportFile = new File(exportPanel.fileField.getText());
		String exportName = exportFile.getParent()+Config.FILE_SEPARATOR+Workplace.getParams().getSpecies();
		
		switch (outputFormat) {
		case ExporterFactory.POV_MESH: 
			exportPanel.fileField.setText(exportName+Constant.FS+Constant.INC);
			exportPanel.sceneCheckbox.setEnabled(true);
			renderPanel.renderCheckbox.setEnabled(true);
			exportPanel.smoothField.setEnabled(true);
			tabbedPane.setEnabledAt(1,true);
			exportPanel.uvStemsCheckbox.setEnabled(true);
			exportPanel.uvLeavesCheckbox.setEnabled(true);
			break;
		case ExporterFactory.POV_CONES: 
			exportPanel.fileField.setText(exportName+Constant.FS+Constant.INC);
			exportPanel.sceneCheckbox.setEnabled(true);
			renderPanel.renderCheckbox.setEnabled(true);
			exportPanel.smoothField.setEnabled(false);
			tabbedPane.setEnabledAt(1,true);
			exportPanel.uvStemsCheckbox.setEnabled(false);
			exportPanel.uvLeavesCheckbox.setEnabled(false);
			break;
		case ExporterFactory.DXF: 
			exportPanel.fileField.setText(exportName+Constant.FS+Constant.DXF);
			exportPanel.sceneCheckbox.setSelected(false);
			exportPanel.sceneCheckbox.setEnabled(false);
			renderPanel.renderCheckbox.setSelected(false);
			renderPanel.renderCheckbox.setEnabled(false);
			exportPanel.smoothField.setEnabled(true);
			tabbedPane.setEnabledAt(1,false);
			exportPanel.uvStemsCheckbox.setEnabled(false);
			exportPanel.uvLeavesCheckbox.setEnabled(false);
			break;
		case ExporterFactory.OBJ: 
			exportPanel.fileField.setText(exportName+Constant.FS+Constant.OBJ);
			exportPanel.sceneCheckbox.setSelected(false);
			exportPanel.sceneCheckbox.setEnabled(false);
			renderPanel.renderCheckbox.setSelected(false);
			renderPanel.renderCheckbox.setEnabled(false);
			exportPanel.smoothField.setEnabled(true);
			tabbedPane.setEnabledAt(1,false);
			exportPanel.uvStemsCheckbox.setEnabled(true);
			exportPanel.uvLeavesCheckbox.setEnabled(true);
			break;
		default:
			break;
		}
	}

}