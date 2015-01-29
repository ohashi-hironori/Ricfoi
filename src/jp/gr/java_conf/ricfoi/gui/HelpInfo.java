package jp.gr.java_conf.ricfoi.gui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import jp.gr.java_conf.ricfoi.Constant;

public class HelpInfo extends JLabel {
	private static final long serialVersionUID = 1L;

	String longText = Constant.ZS;
	boolean errorShowing=false;
	
	public HelpInfo () {
		super();
		setFont(getFont().deriveFont(Font.PLAIN,12));
		setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
	}
	
	public void setLongText(String text) {
		if(! errorShowing) longText = text;
	}
	
	public void showLongText() {
		if ((!errorShowing) && (longText.length() > 0)) {
			JLabel msg = new JLabel(longText.replace('\n',' '));
			JOptionPane.showMessageDialog(Workplace.getParentFrame(),msg,
					"Parameter description",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void showError(String err) {
		setText("<html><font color='red'>"+err+"</font></html>");
		errorShowing=true;
	}
	
	public void noError() {
		errorShowing=false;
	}
	
	@Override
	public void setText(String str) {
		if (! errorShowing) super.setText(str);
	}

}
