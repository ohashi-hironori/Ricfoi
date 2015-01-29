package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.gui.Config;
import jp.gr.java_conf.ricfoi.gui.Workplace;

public class OpenAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("OpenAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("OpenAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("OpenAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("OpenAction.TIPS"); //$NON-NLS-1$

	public OpenAction() {
		super(LABEL,createImageIcon(ICON, DESC));
		putValue(SHORT_DESCRIPTION, TIPS);
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Workplace.stopEditing();
		performed();
	}


	public static boolean performed() {
		switch (SaveAction.shouldSave()) {
		case JOptionPane.OK_OPTION:
			if (!SaveAction.performed()) return false;
			break;
		case JOptionPane.NO_OPTION:
			break;
		case JOptionPane.CANCEL_OPTION:
			return false;
		default:
			break;
		}
		
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
		int returnVal = chooser.showOpenDialog(Workplace.getParentFrame());
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				Workplace.getParams().clearParams();
				Workplace.setTreefile(chooser.getSelectedFile().toString());
				Workplace.getParams().readFromXML(new FileInputStream(Workplace.getTreefile()));
				Workplace.getParams().prepare(Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_TREE_SEED)));
				Workplace.setModified(false);
				Workplace.fireStateChanged();
				Workplace.getParentFrame().setTitle(Ricfoi.NAME+'['+Workplace.getParams().getSpecies()+']');
				Workplace.remakeTree();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(
						Workplace.getParentFrame(),
						e.getMessage(),
						"Open File Error",
						JOptionPane.ERROR_MESSAGE
					);
			}
			return true;
		} else {
			return false;
		}
		
	}

}
