package jp.gr.java_conf.ricfoi.action;

import java.net.URL;

import javax.swing.ImageIcon;

import jp.gr.java_conf.ricfoi.Ricfoi;

public abstract class AbstractAction extends javax.swing.AbstractAction {
	private static final long serialVersionUID = 1L;

	public AbstractAction(String name, ImageIcon imageIcon) {
    	super(name, imageIcon);
	}

    /**
     * @param path
     * @param description
     * @return ImageIcon
     */
    public static ImageIcon createImageIcon(String path, String description) {
    	URL url = Ricfoi.class.getResource(path);
    	if (url != null) {
    		return new ImageIcon(url, description);
    	} else {
    		return null;
    	}
    }

}
