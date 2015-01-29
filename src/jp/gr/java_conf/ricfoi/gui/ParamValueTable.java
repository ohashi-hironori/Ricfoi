package jp.gr.java_conf.ricfoi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import jp.gr.java_conf.ricfoi.Constant;
import jp.gr.java_conf.ricfoi.params.AbstractParam;
import jp.gr.java_conf.ricfoi.params.ParamException;
import jp.gr.java_conf.ricfoi.params.Params;

public class ParamValueTable extends JPanel {
	private static final long serialVersionUID = 1L;

	private final static Color _bgClr = new Color(250,250,240);
	
	JTable table;
	String groupName;
	int groupLevel;
	ParamTableModel tableModel;
	Params params;
	HelpInfo helpInfo;
	ChangeEvent changeEvent;
	
	class ParamTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		
//		private String[] columnNames = {"Parameter", "Value"};
//		
//		@Override
//		public int getColumnCount() {
//			return columnNames.length;
//		}
//		
//		@Override
//		public String getColumnName(int col) {
//			return columnNames[col];
//		}
		
		@Override
		public int getColumnCount() { return 2; }
		
		@Override
		public int getRowCount() {
			TreeMap<Integer, AbstractParam> par = params.getParamGroup(groupLevel,groupName);
			return par.size();
		}
		@Override
		public Object getValueAt(int row, int col) {
			// FIXME: maybe the params should be stored directly in the model
			
			TreeMap<Integer, AbstractParam> par = params.getParamGroup(groupLevel,groupName);
			int r = 0;
			for (Iterator<AbstractParam> e=par.values().iterator(); e.hasNext();) {
				AbstractParam p = (AbstractParam)e.next();
				if (row==r++) { 
					if (col==0) return p.getName();
					else return p;
				}
			}
			return Constant.ZS; // if not found
			
		}
		
		@Override
		public void setValueAt(Object value, int row, int col) {
			noError();
			
			// FIXME: maybe the params should be stored directly in the model
			try {
				// enable/disable
				tableModel.fireTableDataChanged();
				// propagate change to other components, e.g. the preview
				fireStateChanged();
			} catch (Exception e) {
				if (e.getClass()==ParamException.class) {
					System.err.println(e);
					showError(e);
				} else {
					System.err.println(e);
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public boolean isCellEditable(int row, int col) { 
			return (col==1) && ((AbstractParam)getValueAt(row,col)).isEnabled(); 
		}	
	};
	
	public ParamValueTable(Params params) {
		super(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder());
		setBackground(_bgClr);
		
		this.params=params;
		
		tableModel = new ParamTableModel();
		table = new JTable(tableModel);
		table.setBackground(_bgClr);
		table.setRowHeight((int)(table.getRowHeight()*1.3));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		TableColumn paramColumn = table.getColumnModel().getColumn(0);
		paramColumn.setHeaderValue("Parameter");
		
		TableColumn valueColumn = table.getColumnModel().getColumn(1);
		valueColumn.setHeaderValue("Value");
		valueColumn.setCellEditor(new CellEditor());
		valueColumn.setCellRenderer(new CellRenderer());
		
		add(table,BorderLayout.NORTH);
		
		// Ask to be notified of selection changes.
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if (e.getValueIsAdjusting()) return;
				
				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if (lsm.isSelectionEmpty()) {
					// no rows are selected
					helpInfo.setText(Constant.ZS);
					helpInfo.setLongText(Constant.ZS);
				} else {
					int selectedRow = lsm.getMinSelectionIndex();
					//selectedRow is selected
					AbstractParam param=(AbstractParam)tableModel.getValueAt(selectedRow,1);
					helpInfo.setText("<html><a href=\"longDesc\">"
							+param.getName()+"</a>: "
							+param.getShortDesc()
							+"</html>");
					helpInfo.setLongText("<html>"+param.getLongDesc()+"</html>");
				}
			}
		});
		
		// add label for parameter info
		helpInfo = new HelpInfo();
		helpInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((HelpInfo)e.getSource()).showLongText();
			}
		});
		add(helpInfo,BorderLayout.CENTER);
		
	}
	
	public void showGroup(String group, int level) {
		if (table.isEditing())
			table.getCellEditor().stopCellEditing();
		
		groupName = group;
		groupLevel = level;
		
		tableModel.fireTableDataChanged();
	}
	
	public void stopEditing() {
		if (table.isEditing())
			table.getCellEditor().stopCellEditing();
	}
	
	public void showError(Exception e) {
		helpInfo.showError(e.getMessage());
	}
	
	public void noError() {
		helpInfo.noError();
	}
	
	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}
	
	protected void fireStateChanged() {
		Object [] listeners = listenerList.getListenerList();
		for (int i = listeners.length -2; i>=0; i-=2) {
			if (listeners[i] == ChangeListener.class) {
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
			}
		}
	}

}
