package jp.gr.java_conf.ricfoi.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import jp.gr.java_conf.ricfoi.Ricfoi;

public class Config extends Properties {
	private static final long serialVersionUID = 1L;

	private final static String FILE_NAME  = ".ricfoi";						//$NON-NLS-1$

	private final static String OS_NAME    = "os.name";						//$NON-NLS-1$
	private final static String OS_WINDOWS = "Windows";						//$NON-NLS-1$

	public final static String USER_HOME = System.getProperty("user.home");	//$NON-NLS-1$
	public final static String USER_DIR = System.getProperty("user.dir");	//$NON-NLS-1$
	public final static String FILE_SEPARATOR =System.getProperty("file.separator");	//$NON-NLS-1$

	public final static String KEY_EXPORT_FORMAT     = "export.format";		//$NON-NLS-1$
	public final static String KEY_EXPORT_PATH       = "export.path";		//$NON-NLS-1$
	public final static String KEY_POVRAY_EXE        = "povray.executable";	//$NON-NLS-1$
	public final static String KEY_POVRAY_WIDTH      = "povray.width";		//$NON-NLS-1$
	public final static String KEY_POVRAY_HEIGHT     = "povray.height";		//$NON-NLS-1$
	public final static String KEY_TREE_SEED         = "tree.seed";			//$NON-NLS-1$
	public final static String KEY_PREVIEW_ANTIALIAS = "preview.antialias";	//$NON-NLS-1$

	public final static String PREVIEW_ANTIALIAS_ON  = "on";				//$NON-NLS-1$
	public final static String PREVIEW_ANTIALIAS_OFF = "off";				//$NON-NLS-1$

	private final static String DEFAULT_EXPORT_FORMAT     = "0";			//$NON-NLS-1$
	private final static String DEFAULT_EXPORT_PATH       = USER_DIR+FILE_SEPARATOR+"pov";	//$NON-NLS-1$
	private final static String DEFAULT_POVRAY_EXE        = setDefaultPovrayExe();
	private final static String DEFAULT_POVRAY_WIDTH      = "400";			//$NON-NLS-1$
	private final static String DEFAULT_POVRAY_HEIGHT     = "600";			//$NON-NLS-1$
	private final static String DEFAULT_TREE_SEED         = "13";			//$NON-NLS-1$
	private final static String DEFAULT_PREVIEW_ANTIALIAS = PREVIEW_ANTIALIAS_ON;
	
	private final static String POVRAY_EXE_WINDOWS        = "pvengine.exe";	//$NON-NLS-1$
	private final static String POVRAY_EXE_UNIX           = "povray";		//$NON-NLS-1$

	private final static String SETUP_COMMENT = Ricfoi.NAME+' '+Config.class.getSimpleName();
	private final static String CONFIG_NAME = USER_HOME+FILE_SEPARATOR+FILE_NAME;

	public Config() {
		super();
		try{
			@SuppressWarnings("resource")
			FileInputStream fis = new FileInputStream(CONFIG_NAME);
			load(fis);
		} catch(FileNotFoundException e) {
			setDefaults();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void store() {
		try {
			@SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(CONFIG_NAME);
			store(fos, SETUP_COMMENT);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setDefaults() {
		setProperty(KEY_EXPORT_FORMAT, DEFAULT_EXPORT_FORMAT);
		setProperty(KEY_EXPORT_PATH, DEFAULT_EXPORT_PATH);
		setProperty(KEY_POVRAY_EXE, DEFAULT_POVRAY_EXE);
		setProperty(KEY_POVRAY_WIDTH, DEFAULT_POVRAY_WIDTH);
		setProperty(KEY_POVRAY_HEIGHT, DEFAULT_POVRAY_HEIGHT);
		setProperty(KEY_TREE_SEED, DEFAULT_TREE_SEED);
		setProperty(KEY_PREVIEW_ANTIALIAS, DEFAULT_PREVIEW_ANTIALIAS);
	}

	private static String setDefaultPovrayExe() {
		if (System.getProperty(OS_NAME).indexOf(OS_WINDOWS) >= 0) {
			return POVRAY_EXE_WINDOWS;
		} else {
			return POVRAY_EXE_UNIX;
		}
	}

}
