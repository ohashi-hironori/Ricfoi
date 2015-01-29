package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.gui.ExportDialog;
import jp.gr.java_conf.ricfoi.gui.Workplace;

public class ExportAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("ExportAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("ExportAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("ExportAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("ExportAction.TIPS"); //$NON-NLS-1$

	public ExportAction() {
		super(LABEL,createImageIcon(ICON, DESC));
		putValue(SHORT_DESCRIPTION, TIPS);
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_E));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Workplace.stopEditing();
		performed();
	}

	private static boolean performed() {
		@SuppressWarnings("unused")
		ExportDialog dialog = new ExportDialog(false);
//		dialog.setAlwaysOnTop(true);
		return false;
	}
}
