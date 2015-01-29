package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.gui.Config;
import jp.gr.java_conf.ricfoi.gui.Workplace;

public class NewAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("NewAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("NewAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("NewAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("NewAction.TIPS"); //$NON-NLS-1$

	public NewAction() {
		super(LABEL,createImageIcon(ICON, DESC));
		putValue(SHORT_DESCRIPTION, TIPS);
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_N));
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
		Workplace.getParams().clearParams();
		Workplace.getParams().prepare(Integer.parseInt(Workplace.getConfig().getProperty(Config.KEY_TREE_SEED)));
		Workplace.setTreefile(null);
		Workplace.setModified(false);
		Workplace.fireStateChanged();
		Workplace.getParentFrame().setTitle(Ricfoi.NAME+'['+Workplace.getParams().getSpecies()+']');
		Workplace.remakeTree();
		return true;
	}
}
