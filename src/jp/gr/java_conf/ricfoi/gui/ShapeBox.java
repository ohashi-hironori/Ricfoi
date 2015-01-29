package jp.gr.java_conf.ricfoi.gui;

import javax.swing.JComboBox;

import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.IntParam;
import jp.gr.java_conf.ricfoi.params.ShapeParam;

public class ShapeBox extends JComboBox<Object> {
	private static final long serialVersionUID = 1L;

	public ShapeBox() {
		super();
		ShapeRenderer sr= new ShapeRenderer();
		setRenderer(sr);
		
		for (int i=0; i<ShapeParam.getValues().length; i++) {
			addItem(new String(Integer.toString(i)));
		}
	}
	
	public void setValue(AbstractParam p) {
		setSelectedIndex(((IntParam)p).getIntValue());
	}
	
	public String getValue() {
		return Integer.toString(getSelectedIndex());
	}

}
