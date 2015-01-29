package jp.gr.java_conf.ricfoi.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import jp.gr.java_conf.ricfoi.Constant;

public class ProgressBar extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final String LABEL = "Creating tree structure";
	
	private JProgressBar progressbar;
	private JLabel label;
	
	/**
	 * Create the panel.
	 */
	public ProgressBar() {
		super(new BorderLayout());
		((BorderLayout)getLayout()).setHgap(20);
		
		setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
		
		label = new JLabel(LABEL);
    	Font font = label.getFont().deriveFont(Font.PLAIN,12);
    	label.setFont(font);
		add(label,BorderLayout.WEST);
    	
		progressbar = new JProgressBar(0,100);
		progressbar.setValue(-1);
		progressbar.setStringPainted(true);
		progressbar.setString(Constant.ZS);
		progressbar.setIndeterminate(true);
		add(progressbar,BorderLayout.CENTER);
	}

	public void setProgress(int val) {
		if (val<=0) {
			progressbar.setIndeterminate(true);
			progressbar.setString(Constant.ZS);
		} else {
			progressbar.setIndeterminate(false);
			progressbar.setValue(val);
			progressbar.setString(Integer.toString(val)+Constant.PERCENT);
		}
	}

	public void setNote(String note) {
		label.setText(note);
	}

}
