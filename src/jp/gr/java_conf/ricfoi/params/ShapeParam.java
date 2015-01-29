package jp.gr.java_conf.ricfoi.params;

import java.net.URL;

import javax.swing.ImageIcon;

import jp.gr.java_conf.ricfoi.Ricfoi;

public class ShapeParam extends IntParam {

	private final static String[] _items = {
		"conical",				//$NON-NLS-1$
		"spherical",			//$NON-NLS-1$
		"hemispherical",		//$NON-NLS-1$
		"cylindrical", 			//$NON-NLS-1$
		"tapered cylindrical",	//$NON-NLS-1$
		"flame",				//$NON-NLS-1$
		"inverse conical",		//$NON-NLS-1$
		"tend flame",			//$NON-NLS-1$
		"envelope"				//$NON-NLS-1$
	};
	
	private final static String[] _images = {
		"images/shape0.png",
		"images/shape1.png",
		"images/shape2.png",
		"images/shape3.png",
		"images/shape4.png",
		"images/shape5.png",
		"images/shape6.png",
		"images/shape7.png",
		"images/shape8.png",
	};
	
	private static ImageIcon[] _icons = null;

	private static ImageIcon createImageIcon(String path, String description) {
		URL imgURL = Ricfoi.class.getResource(path);
		return new ImageIcon(imgURL, description);
	}

	private static void createImageIcons() {
		_icons = new ImageIcon[_images.length];
		for (int i=0; i<_images.length; i++) {
			_icons[i] = createImageIcon(_images[i], _items[i]);
		}
	}

	public ShapeParam(String name, int min, int max, int def, String group,
			int level, int order, String shortDesc, String longDesc) {
		super(name, min, max, def, group, level, order, shortDesc, longDesc);
	}

	@Override
	public String toString() {
		return _items[getIntValue()];
	}

	public static String[] getValues() {
		return _items;
	}

	public static ImageIcon[] getIcons() {
		if(_icons == null) { createImageIcons(); };
		return _icons;
	}

	public static ImageIcon getIcon(int index) {
		if(_icons == null) { createImageIcons(); };
		return _icons[index];
	}

}
