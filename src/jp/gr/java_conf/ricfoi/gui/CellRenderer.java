package jp.gr.java_conf.ricfoi.gui;

import javax.swing.table.DefaultTableCellRenderer;

import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.LeafShapeParam;
import jp.gr.java_conf.ricfoi.params.ShapeParam;
import jp.gr.java_conf.ricfoi.params.StringParam;

class CellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    public CellRenderer() { super(); }

    @Override
	public void setValue(Object value) {
    	// alignment for parameter type
    	if (value.getClass() == ShapeParam.class) {
    		setHorizontalAlignment(LEFT);
    		setText(Integer.toString(((ShapeParam)value).getIntValue())+" - "+value.toString()); //$NON-NLS-1$
    	} else if (value.getClass() == LeafShapeParam.class) {
    		setHorizontalAlignment(LEFT);
    		setText(value.toString());
    	} else if (value.getClass() == StringParam.class) {
    		setHorizontalAlignment(LEFT);
    		setText(value.toString());
    	} else {
    		setHorizontalAlignment(RIGHT);
    		setText(value.toString());
    	}
    	
    	this.setEnabled(((AbstractParam)value).isEnabled());
    }
}
