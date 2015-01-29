package jp.gr.java_conf.ricfoi.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import jp.gr.java_conf.ricfoi.params.ShapeParam;

public class ShapeRenderer extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = 1L;

	public ShapeRenderer() {
		setOpaque(true);
	}
	
	@Override
	public Component getListCellRendererComponent(
		JList<? extends Object> list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus
	) {
		int myIndex = Integer.parseInt(value.toString());
		
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		
		//Set the icon and text.  If icon was null, say so.
		ImageIcon icon = ShapeParam.getIcon(myIndex);
		setIcon(icon);
		setText(icon.getDescription());
		
		return this;
	}

}
