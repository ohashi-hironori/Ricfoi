package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.gui.ExportDialog;
import jp.gr.java_conf.ricfoi.gui.Workplace;

public class RenderAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("RenderAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("RenderAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("RenderAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("RenderAction.TIPS"); //$NON-NLS-1$

	public RenderAction() {
		super(LABEL,createImageIcon(ICON, DESC));
		putValue(SHORT_DESCRIPTION, TIPS);
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Workplace.stopEditing();
		performed();
	}

	private static boolean performed() {
		@SuppressWarnings("unused")
		ExportDialog dialog = new ExportDialog(true);
//		dialog.setAlwaysOnTop(true);
		return false;
	}

}
