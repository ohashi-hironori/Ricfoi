package jp.gr.java_conf.ricfoi.gui;

import javax.swing.JComboBox;

import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.LeafShapeParam;

public class LeafShapeBox extends JComboBox<String> {
	private static final long serialVersionUID = 1L;

	public LeafShapeBox() {
		super();
		setEditable(true);
		
		String[] items = LeafShapeParam.getValues();
		
		for (int i=0; i<items.length; i++) {
			addItem(items[i]);
		}
	}

	public void setValue(AbstractParam p) {
		// select item
		for (int i=0; i<getItemCount(); i++) {
			if (getItemAt(i).equals(p.getValue())) {
				setSelectedIndex(i);
				return;
			}
		}
	}

	public String getValue() {
		return (String)getSelectedItem();
	}

}
