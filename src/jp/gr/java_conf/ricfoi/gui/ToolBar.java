package jp.gr.java_conf.ricfoi.gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JToolBar;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.action.ExportAction;
import jp.gr.java_conf.ricfoi.action.NewAction;
import jp.gr.java_conf.ricfoi.action.OpenAction;
import jp.gr.java_conf.ricfoi.action.RenderAction;
import jp.gr.java_conf.ricfoi.action.SaveAction;

public class ToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;

	private JButton button;

	public ToolBar() {
		super();

		button = new JButton(new NewAction());
		button.setText(Constant.ZS);
	    add(button);

		button = new JButton(new OpenAction());
		button.setText(Constant.ZS);
	    add(button);

		button = new JButton(new SaveAction());
		button.setText(Constant.ZS);
	    add(button);
	    
	    add(new JToolBar.Separator(new Dimension(10,10)));

		button = new JButton(new ExportAction());
		button.setText(Constant.ZS);
	    add(button);

		button = new JButton(new RenderAction());
		button.setText(Constant.ZS);
	    add(button);
	}

}
