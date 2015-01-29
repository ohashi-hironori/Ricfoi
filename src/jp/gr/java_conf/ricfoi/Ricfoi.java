package jp.gr.java_conf.ricfoi;

import java.awt.EventQueue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JFrame;

import jp.gr.java_conf.ricfoi.gui.Workplace;

public class Ricfoi {

	public static final String NAME = Ricfoi.class.getSimpleName();
	public static final String VERSION = "1.0";
	public static final String DESC = "Creates trees objects for rendering from xml parameter files";
    
	public static final String ICON32 = "images/arbaro32.png";
	public static final String ICON64 = "images/arbaro64.png";

	@SuppressWarnings("nls")
	protected static final String LOGGING_PROPERTIES_DATA
		= "handlers=java.util.logging.ConsoleHandler\n"
//		= "handlers=java.util.logging.FileHandler,java.util.logging.ConsoleHandler\n"
		+ ".level=CONFIG\n"
		// SEVERE(重大) | WARNING(警告) | INFO(情報) | CONFIG(構成) | FINE(普通) | FINER(詳細) | FINEST(最も詳細)
		// FINE(普通) - Java Component Message
//		+ "java.util.logging.FileHandler.pattern=%h/java%u.log\n"
//		+ "java.util.logging.FileHandler.limit=50000\n"
//		+ "java.util.logging.FileHandler.count=1\n"
//		+ "java.util.logging.FileHandler.formatter=java.util.logging.XMLFormatter\n"
//		+ "java.util.logging.ConsoleHandler.level=INFO\n"
		+ "java.util.logging.ConsoleHandler.level=CONFIG\n"
		+ "java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter\n"
		+ "java.util.logging.SimpleFormatter.format=%1$tF %1$tT [%4$s] %2$s %5$s%6$s%n";

	static {
		@SuppressWarnings("unused")
		final Logger logger = Logger.getLogger(Ricfoi.class.getName());
		InputStream inStream = null;
		try {
			inStream = new ByteArrayInputStream(LOGGING_PROPERTIES_DATA.getBytes("UTF-8")); //$NON-NLS-1$
			LogManager.getLogManager().readConfiguration(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null)
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	private JFrame frame;
	
	public static final Logger logger = Logger.getLogger(Ricfoi.class.getName());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			@SuppressWarnings("synthetic-access")
			public void run() {
				try {
					Ricfoi window = new Ricfoi();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Ricfoi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new Workplace();
	}

}
