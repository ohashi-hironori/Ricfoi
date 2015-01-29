package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.gui.Workplace;

public class SaveAsAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("SaveAsAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("SaveAsAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("SaveAsAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("SaveAsAction.TIPS"); //$NON-NLS-1$

	public SaveAsAction() {
		super(LABEL,createImageIcon(ICON, DESC));
    	putValue(SHORT_DESCRIPTION, TIPS);
    	putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Workplace.stopEditing();
		performed();
	}

	public static boolean fileChooser() {
		JFileChooser chooser;
		if (Workplace.getTreefile() == null) {
			chooser = new JFileChooser();
		} else {
			File file = new File(Workplace.getTreefile());
			while(!file.exists()) {file = new File(file.getParent());}
			chooser = new JFileChooser(file);
		}
		FileFilter filter = new FileNameExtensionFilter("XML - Extensible Markup Language","XML");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(Workplace.getParentFrame());
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			Workplace.setTreefile(chooser.getSelectedFile().toString());
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean performed() {
		if (fileChooser()) {
			SaveAction.performed();
			return true;
		} else {
			return false;
		}
	}
}
