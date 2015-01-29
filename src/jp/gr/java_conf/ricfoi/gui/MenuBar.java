package jp.gr.java_conf.ricfoi.gui;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import jp.gr.java_conf.ricfoi.action.AboutAction;
import jp.gr.java_conf.ricfoi.action.ConfigureAction;
import jp.gr.java_conf.ricfoi.action.ExportAction;
import jp.gr.java_conf.ricfoi.action.NewAction;
import jp.gr.java_conf.ricfoi.action.OpenAction;
import jp.gr.java_conf.ricfoi.action.QuitAction;
import jp.gr.java_conf.ricfoi.action.RenderAction;
import jp.gr.java_conf.ricfoi.action.SaveAction;
import jp.gr.java_conf.ricfoi.action.SaveAsAction;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	MenuBar() {
		super();
		add(new FileMenu());
		add(new SetupMenu());
		add(new HelpMenu());
	}
	
	class FileMenu extends JMenu {
		private static final long serialVersionUID = 1L;
		
		private static final String LABEL = "File";
		
		FileMenu() {
			super(LABEL);
			setMnemonic(KeyEvent.VK_F);
			add(new JMenuItem(new NewAction()));
			add(new JMenuItem(new OpenAction()));
			add(new JMenuItem(new SaveAction()));
			add(new JMenuItem(new SaveAsAction()));
			add(new JSeparator());
			add(new JMenuItem(new ExportAction()));
			add(new JMenuItem(new RenderAction()));
			add(new JSeparator());
			add(new JMenuItem(new QuitAction()));
		}
	}

	class SetupMenu extends JMenu {
		private static final long serialVersionUID = 1L;
		
		private static final String LABEL = "Setup";

		SetupMenu() {
			super(LABEL);
			setMnemonic(KeyEvent.VK_S);
			add(new JMenuItem(new ConfigureAction()));	
		}
	}
	
	class HelpMenu extends JMenu {
		private static final long serialVersionUID = 1L;
		
		private static final String LABEL = "Help";

		HelpMenu() {
			super(LABEL);
			setMnemonic(KeyEvent.VK_H);
			add(new JMenuItem(new AboutAction()));	
		}
	}
}
