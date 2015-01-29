package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.gui.Workplace;

public class QuitAction extends AbstractAction implements WindowListener {
	private static final long serialVersionUID = 1L;

	private final static String LABEL = Messages.getString("QuitAction.LABEL"); //$NON-NLS-1$
//	private final static String ICON = Messages.getString("QuitAction.ICON"); //$NON-NLS-1$
//	private final static String DESC = Messages.getString("QuitAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("QuitAction.TIPS"); //$NON-NLS-1$

	public QuitAction() {
		super(LABEL,null);
    	putValue(SHORT_DESCRIPTION, TIPS);
    	putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_Q));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Workplace.stopEditing();
		performed();
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		Workplace.stopEditing();
		performed();
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	private static void performed() {
		switch (SaveAction.shouldSave()) {
		case JOptionPane.OK_OPTION:
			SaveAction.performed();
			break;
		case JOptionPane.NO_OPTION:
			break;
		case JOptionPane.CANCEL_OPTION:
			return;
		default:
			break;
		}
		Workplace.getParentFrame().dispose();
		System.exit(0);
	}

}
