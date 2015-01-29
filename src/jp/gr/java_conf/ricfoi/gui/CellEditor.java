package jp.gr.java_conf.ricfoi.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.Params;

class CellEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;

	AbstractParam param;
	JTextField paramField;
	ShapeBox shapeBox;
	LeafShapeBox leafShapeBox;
	JComponent editor;
	
	void editingStopped() {
		fireEditingStopped();
	}
	
	public CellEditor() {
		super();
		paramField = new JTextField();
		
		shapeBox = new ShapeBox();
		shapeBox.addActionListener(new ActionListener() {
    		@Override
			public void actionPerformed(ActionEvent e) {
    			editingStopped();
    		}
    	});
		
		leafShapeBox = new LeafShapeBox();
		leafShapeBox.addActionListener(new ActionListener() {
    		@Override
			public void actionPerformed(ActionEvent e) {
    			editingStopped();
    		}
    	});
		
	}
	
	@Override
	public Object getCellEditorValue() {
		try {
			if (editor == shapeBox) {
				param.setValue(shapeBox.getValue());
			} else if (editor == leafShapeBox) {
				param.setValue(leafShapeBox.getValue());
			} else { 
				param.setValue(paramField.getText());
			}
		} catch (Exception err) {
			HelpInfo info = new HelpInfo();
			info.showError(err.getMessage());
		}

		return param;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table,
			Object value,
			boolean isSelected,
			int row,
			int column) {
		param = (AbstractParam)value;

		if (param.getName().equals(Params.NAME_SHAPE)) {
			shapeBox.setValue(param);
			editor = shapeBox;
		} else if (param.getName().equals(Params.NAME_LEAF_SHAPE)) {
			leafShapeBox.setValue(param);
			editor = leafShapeBox;
		} else {
			paramField.setText(param.toString());
			paramField.selectAll();
			editor = paramField;
		}
		return editor;
	}
}
