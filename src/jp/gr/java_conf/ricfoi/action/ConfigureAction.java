package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.gui.ConfigDialog;

public class ConfigureAction extends AbstractAction {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("ConfigureAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("ConfigureAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("ConfigureAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("ConfigureAction.TIPS"); //$NON-NLS-1$

	public ConfigureAction() {
		super(LABEL,createImageIcon(ICON, DESC));
		putValue(SHORT_DESCRIPTION, TIPS);
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unused")
		ConfigDialog configDialog = new ConfigDialog();
//		configDialog.setAlwaysOnTop(true);
	}

}
