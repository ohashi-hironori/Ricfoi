package jp.gr.java_conf.ricfoi.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import jp.gr.java_conf.ricfoi.Messages;
import jp.gr.java_conf.ricfoi.Ricfoi;
import jp.gr.java_conf.ricfoi.gui.Workplace;

public class AboutAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private final static String LABEL = Messages.getString("AboutAction.LABEL"); //$NON-NLS-1$
	private final static String ICON = Messages.getString("AboutAction.ICON"); //$NON-NLS-1$
	private final static String DESC = Messages.getString("AboutAction.DESC"); //$NON-NLS-1$
	private final static String TIPS = Messages.getString("AboutAction.TIPS"); //$NON-NLS-1$
	
	public AboutAction() {
		super(LABEL,createImageIcon(ICON, DESC));
		putValue(SHORT_DESCRIPTION, TIPS);
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(
			Workplace.getParentFrame(),
			Ricfoi.NAME+' '+Ricfoi.VERSION+' '+'-'+' '+Ricfoi.DESC,
			TIPS,
			JOptionPane.INFORMATION_MESSAGE,
			createImageIcon(Ricfoi.ICON64, Ricfoi.NAME)
		);
	}

}
