package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.gui.Workplace;
import jp.gr.java_conf.ricfoi.params.ParamException;

public class SaveAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("SaveAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("SaveAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("SaveAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("SaveAction.TIPS"); //$NON-NLS-1$

	public SaveAction() {
		super(LABEL,createImageIcon(ICON, DESC));
    	putValue(SHORT_DESCRIPTION, TIPS);
    	putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Workplace.stopEditing();
		performed();
	}

	public static int shouldSave() {
		if (Workplace.isModified()) {
			return JOptionPane.showConfirmDialog(
						Workplace.getParentFrame(),
						"Some parameters are modified. Should the tree definition be saved?",
						"Tree definition modified",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE
					);  
		} else {
			return JOptionPane.NO_OPTION;
		}
	}

	public static boolean performed() {
		
		if (Workplace.getTreefile() == null) {
			if (!SaveAsAction.fileChooser()) return false;
		}
		
		try {
			try(PrintWriter out = new PrintWriter(new FileWriter(Workplace.getTreefile()))) {
				Workplace.getParams().toXML(out);
				Workplace.setModified(false);
			}
			return true;
		} catch (ParamException err) {
			JOptionPane.showMessageDialog(
				Workplace.getParentFrame(),
				err.getMessage(),
				"Parameter Error",
				JOptionPane.ERROR_MESSAGE
			);
		} catch (FileNotFoundException err) {
			JOptionPane.showMessageDialog(
				Workplace.getParentFrame(),
				err.getMessage(),
				"File not found",
				JOptionPane.ERROR_MESSAGE
			);
		}
		catch (IOException err) {
			JOptionPane.showMessageDialog(
				Workplace.getParentFrame(),
				err.getMessage(),
				"Output error",
				JOptionPane.ERROR_MESSAGE
			);
		}
		return false;
	}

}
