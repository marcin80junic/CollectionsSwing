package collectionsMain;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import collectableItems.AbstractItem;
import collectableItems.Collectable;


class TableModelComboHeader extends AbstractTableModel implements TableModel, ActionListener {
	
	private static final long serialVersionUID = 1L;
	CollectionsApp application;
	DataBase<? extends Collectable<? extends AbstractItem>> dataBase;
	JTable table;
	private TableModelCollection<? extends Collectable<? extends AbstractItem>> tableModel;
	private JComboBox<String> combo1, combo2, combo3, combo4, combo5;
	private DefaultComboBoxModel<String> combo1Model, combo2Model, combo3Model, combo4Model, combo5Model;
	private boolean combo1Flag = false, combo2Flag = false, combo3Flag = false, combo4Flag = false, combo5Flag = false;
	private String combo1Selection, combo2Selection, combo3Selection, combo4Selection, combo5Selection;
	
	
	public TableModelComboHeader(CollectionsApp app, JTable table) {
		
		application = app;
		this.table = table;
		comboInit();
	}
	
	@Override
	public int getRowCount() { return 1; }
	
	@Override
	public int getColumnCount() { return table.getColumnCount(); }
	
	@Override
	public String getColumnName(int index) { return table.getColumnName(index);	}
	
	@Override
	public Class<?> getColumnClass(int column){ return column==0? Integer.class: String.class; }
	
	@Override
	public boolean isCellEditable(int row, int column) { return column == 0 ? false: true; }
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 0: return "";
		case 1: return combo1.getSelectedItem();
		case 2: return combo2.getSelectedItem();
		case 3: return combo3.getSelectedItem();
		case 4: return combo4.getSelectedItem();
		case 5: return combo5.getSelectedItem();
		default: return "bad data";
		}
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		switch(column) {
		case 1: combo1.setSelectedItem(value); break;
		case 2: combo2.setSelectedItem(value); break;
		case 3: combo3.setSelectedItem(value); break;
		case 4: combo4.setSelectedItem(value); break;
		case 5: combo5.setSelectedItem(value); break;
		}
	}
	
	void comboInit() {
		combo1 = new JComboBox <String>();
		combo1.setRenderer(new ComboBoxRenderer(application));
		combo1.addActionListener(this);
		combo1.setActionCommand("combo1");
		combo2 = new JComboBox <String> ();
		combo2.setRenderer(new ComboBoxRenderer(application));
		combo2.addActionListener(this);
		combo2.setActionCommand("combo2");
		combo3 = new JComboBox <String> ();
		combo3.setRenderer(new ComboBoxRenderer(application));
		combo3.addActionListener(this);
		combo3.setActionCommand("combo3");
		combo4 = new JComboBox <String> ();
		combo4.setRenderer(new ComboBoxRenderer(application));
		combo4.addActionListener(this);
		combo4.setActionCommand("combo4");
		combo5 = new JComboBox <String> ();
		combo5.setRenderer(new ComboBoxRenderer(application));
		combo5.addActionListener(this);
		combo5.setActionCommand("combo5");
	}
	
	
	private void saveComboFlags() {
		
		boolean[] flags = {combo1Flag, combo2Flag, combo3Flag, combo4Flag, combo5Flag};
		dataBase.setComboFlags(flags);
	}
	
	
	private void updateComboFlags() {
		
		boolean[] flags = dataBase.getComboFlags();
		for(int i=0; i<flags.length; i++) {
			switch(i) {
			case 0: combo1Flag = flags[i];
					if(combo1Flag) combo1.setSelectedIndex(1);
					break;
			case 1: combo2Flag = flags[i];
					if(combo2Flag) combo2.setSelectedIndex(1);
					break;
			case 2: combo3Flag = flags[i];
					if(combo3Flag) combo3.setSelectedIndex(1);
					break;
			case 3: combo4Flag = flags[i];
					if(combo4Flag) combo4.setSelectedIndex(1);
					break;
			case 4: combo5Flag = flags[i];
					if(combo5Flag) combo5.setSelectedIndex(1);
					break;
			}
		}
	}


	void resetComboFlags() {
		
		combo1Flag = combo2Flag = combo3Flag = combo4Flag = combo5Flag = false;
	}
	
	
	boolean isComboSelected() {
		
		return (combo1Flag || combo2Flag || combo3Flag || combo4Flag || combo5Flag)? true: false;
	}
	
	
	void resetModel() {
		resetComboFlags();
		saveComboFlags();
		updateComboLists();
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	void updateComboLists() {
		
		dataBase = application.getDataBase();
		tableModel = (TableModelCollection) table.getModel();
		
		String[] comboHeaders = dataBase.getComboHeaders();
		String[] combo1List = new String[tableModel.getRowCount()+1];
		String[] combo2List = new String[tableModel.getRowCount()+1];
		String[] combo3List = new String[tableModel.getRowCount()+1];
		String[] combo4List = new String[tableModel.getRowCount()+1];
		String[] combo5List = new String[tableModel.getRowCount()+1];

		if(tableModel.getRowCount() == 0) {
			DefaultComboBoxModel [] comboModels = {combo1Model, combo2Model, combo3Model, combo4Model, combo5Model};
			String[][] comboLists = {combo1List, combo2List, combo3List, combo4List, combo5List};
			JComboBox[] combos = {combo1, combo2, combo3, combo4, combo5};
			for(int i=0; i<tableModel.getColumnCount()-1; i++) {
				comboLists[i][0] = comboHeaders[i];
				comboModels[i] = new DefaultComboBoxModel<>(comboLists[i]);
				combos[i].setModel(comboModels[i]);
			}
		} else {
			for(int i=0; i<tableModel.getRowCount(); i++) {
				for (int j=1; j<tableModel.getColumnCount(); j++){
					String value = (String) tableModel.getValueAt(i, j);
					switch(j) {
					case 1:
						if(!combo1Flag) {
							if(i == 0) combo1List[0] = comboHeaders[0];
							if(value.equals("")) combo1List[i+1] = comboHeaders[0];
							else combo1List[i+1] = value;
							if(i == tableModel.getRowCount()-1) {
								combo1List = removeDuplicates(combo1List);
								combo1Model = new DefaultComboBoxModel<>(combo1List);
								combo1.setModel(combo1Model);
							}
						} 
						break;
					case 2: 
						if(!combo2Flag) {
							if(i == 0) combo2List[0] = comboHeaders[1];
							if(value.equals("")) combo2List[i+1] = comboHeaders[1];
							else combo2List[i+1] = value;
							if(i == tableModel.getRowCount()-1) {
								combo2List = removeDuplicates(combo2List);
								combo2Model = new DefaultComboBoxModel<>(combo2List);
								combo2.setModel(combo2Model);
							}
						}
						break;
					case 3:
						if(!combo3Flag) {
							if(i == 0) combo3List[0] = comboHeaders[2];
							if(value.equals("")) combo3List[i+1] = comboHeaders[2];
							else combo3List[i+1] = value;
							if(i == tableModel.getRowCount()-1) {
								combo3List = removeDuplicates(combo3List);
								combo3Model = new DefaultComboBoxModel<>(combo3List);
								combo3.setModel(combo3Model);
							}
						}
						break;
					case 4:
						if(!combo4Flag) {
							if(i == 0) combo4List[0] = comboHeaders[3];
							if(value.equals("")) combo4List[i+1] = comboHeaders[3];
							else combo4List[i+1] = value;
							if(i == tableModel.getRowCount()-1) {
								combo4List = removeDuplicates(combo4List);
								combo4Model = new DefaultComboBoxModel<>(combo4List);
								combo4.setModel(combo4Model);
							}
						}
						break;
					case 5:
						if(!combo5Flag) {
							if(i == 0) combo5List[0] = comboHeaders[4];
							if(value.equals("")) combo5List[i+1] = comboHeaders[4];
							else combo5List[i+1] = value;
							if(i == tableModel.getRowCount()-1) {
								combo5List = removeDuplicates(combo5List);
								combo5Model = new DefaultComboBoxModel<>(combo5List);
								combo5.setModel(combo5Model);
							}
						}
						break;
					}
				}
			}
		}
		updateComboFlags();
	}
	
	@SuppressWarnings("unchecked")
	private void comboSelected(int comboNumber, String selection) {
		
		tableModel = (TableModelCollection<? extends Collectable<? extends AbstractItem>>) table.getModel();
		dataBase = application.getDataBase();
		
		int size, size2 = 0;
		int numOfVar = tableModel.getColumnCount()-1;
		String element = "";
		
		size = tableModel.getRowCount()-1;
		for(int i=size; i>=0; i--) {
			if(!tableModel.getValueAt(i, comboNumber).equals(selection)){
				tableModel.removeItem(i);
			}
		}
		size = tableModel.getRowCount()-1;
		if(!combo1Flag) {
			size2 = combo1Model.getSize()-1;
			for(int i=size2; i>0; i--) {
				element = combo1Model.getElementAt(i);
				for(int j=size; j>=0; j--) {
					if(tableModel.getValueAt(j, 1).equals(element)) break;
					if(j == 0) combo1Model.removeElementAt(i);
				}
			}
		}
		if(!combo2Flag && (numOfVar > 1)) {
			size2 = combo2Model.getSize()-1;
			for(int i=size2; i>0; i--) {
				element = combo2Model.getElementAt(i);
				for(int j=size; j>=0; j--) {
					if(tableModel.getValueAt(j, 2).equals(element)) break;
					if(j == 0) combo2Model.removeElementAt(i);
				}
			}
		}
		if(!combo3Flag && (numOfVar > 2)) {
			size2 = combo3Model.getSize()-1;
			for(int i=size2; i>0; i--) {
				element = combo3Model.getElementAt(i);
				for(int j=size; j>=0; j--) {
					if(tableModel.getValueAt(j, 3).equals(element)) break;
					if(j == 0) combo3Model.removeElementAt(i);
				}
			}
		}
		if(!combo4Flag && (numOfVar > 3)) {
			size2 = combo4Model.getSize()-1;
			for(int i=size2; i>0; i--) {
				element = combo4Model.getElementAt(i);
				for(int j=size; j>=0; j--) {
					if(tableModel.getValueAt(j, 4).equals(element)) break;
					if(j == 0) combo4Model.removeElementAt(i);
				}
			}
		}
		if(!combo5Flag && (numOfVar > 4)) {
			size2 = combo5Model.getSize()-1;
			for(int i=size2; i>0; i--) {
				element = combo5Model.getElementAt(i);
				for(int j=size; j>=0; j--) {
					if(tableModel.getValueAt(j, 5).equals(element)) break;
					if(j == 0) combo5Model.removeElementAt(i);
				}
			}
		}
		application.saveAction.setEnabled(false);
		tableModel.setFiltered(true);
		if(!application.areOtherCollectionsChanged(dataBase)) application.saveAllAction.setEnabled(false);
	}
	
	private void comboUnselected() {
		tableModel = tableModel.reloadTableModel();
		if(combo1Flag) {
			for(int i=tableModel.getRowCount()-1; i>=0; i--) {
				if(!tableModel.getValueAt(i, 1).equals(combo1Selection)) {
					tableModel.removeItem(i);
					tableModel.setFiltered(true);
				}
			}
		}
		if(combo2Flag) {
			for(int i=tableModel.getRowCount()-1; i>=0; i--) {
				if(!tableModel.getValueAt(i, 2).equals(combo2Selection)) {
					tableModel.removeItem(i);
					tableModel.setFiltered(true);
				}
			}
		}
		if(combo3Flag) {
			for(int i=tableModel.getRowCount()-1; i>=0; i--) {
				if(!tableModel.getValueAt(i, 3).equals(combo3Selection)) {
					tableModel.removeItem(i);
					tableModel.setFiltered(true);
				}
			}
		}
		if(combo4Flag) {
			for(int i=tableModel.getRowCount()-1; i>=0; i--) {
				if(!tableModel.getValueAt(i, 4).equals(combo4Selection)) {
					tableModel.removeItem(i);
					tableModel.setFiltered(true);
				}
			}
		}
		if(combo5Flag) {
			for(int i=tableModel.getRowCount()-1; i>=0; i--) {
				if(!tableModel.getValueAt(i, 5).equals(combo5Selection)) {
					tableModel.removeItem(i);
					tableModel.setFiltered(true);
				}
			}
		}
		saveComboFlags();
		updateComboLists();
		application.initHeaderRenderers();
	}
	
	public static String[] removeDuplicates(String[] array) {
		Set<String> uniqueSetArray = new LinkedHashSet<>(Arrays.asList(array));
		ArrayList<String> uniqueListArray = new ArrayList<>(uniqueSetArray);
		uniqueListArray.sort(new Comparator<String>() {
			public int compare(String s1, String s2) {
				if (s1 != null && s2 != null) {
					return s1.compareTo(s2);
				}
				else return 1;
			}
		});
		String[] uniqueArray = new String[uniqueSetArray.size()];
		int i = 0;
		for(String element: uniqueListArray) {
			uniqueArray[i++] = element;
		}
		return uniqueArray;	
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if(ae.getActionCommand().equals("combo1")) {
			int index = combo1.getSelectedIndex();
			combo1Selection = (String)combo1.getSelectedItem();
			if(index == 0 && !combo1Flag) return;
			if(!combo1Flag) {
				comboSelected(1, combo1Selection);
				combo1Flag = true;
				combo1Model.setSelectedItem(combo1Selection);
				saveComboFlags();
			}
			else if (index == 0) {
				combo1Flag = false;
				comboUnselected();
			}
			
		}
		else if(ae.getActionCommand().equals("combo2")) {
			int index = combo2.getSelectedIndex();
			combo2Selection = (String)combo2.getSelectedItem();
			if(index == 0 && !combo2Flag) return;
			if(!combo2Flag) {
				comboSelected(2, combo2Selection);
				combo2Flag = true;
				combo2Model.setSelectedItem(combo2Selection);
				saveComboFlags();
			}
			else if (index == 0) {
				combo2Flag = false;
				comboUnselected();
			}
		}
		else if(ae.getActionCommand().equals("combo3")) {	
			int index = combo3.getSelectedIndex();
			combo3Selection = (String)combo3.getSelectedItem();
			if(index == 0 && !combo3Flag) return;
			if(!combo3Flag) {
				comboSelected(3, combo3Selection);
				combo3Flag = true;
				combo3Model.setSelectedItem(combo3Selection);
				saveComboFlags();
			}
			else if (index == 0) {
				combo3Flag = false;
				comboUnselected();
			}
		}
		else if(ae.getActionCommand().equals("combo4")) {
			int index = combo4.getSelectedIndex();
			combo4Selection = (String)combo4.getSelectedItem();
			if(index == 0 && !combo4Flag) return;
			if(!combo4Flag) {
				comboSelected(4, combo4Selection);
				combo4Flag = true;
				combo4Model.setSelectedItem(combo4Selection);
				saveComboFlags();
			}
			else if (index == 0) {
				combo4Flag = false;
				comboUnselected();
			}
		}
		else if(ae.getActionCommand().equals("combo5")) {			
			int index = combo5.getSelectedIndex();
			combo5Selection = (String)combo5.getSelectedItem();
			if(index == 0 && !combo5Flag) return;
			if(!combo5Flag) {
				comboSelected(5, combo5Selection);
				combo5Flag = true;
				combo5Model.setSelectedItem(combo5Selection);
				saveComboFlags();
			}
			else if (index == 0) {
				combo5Flag = false;
				comboUnselected();
			}
		}
	}
	
	public class Renderer implements TableCellRenderer{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			switch(column) {
			case 1: return combo1;
			case 2: return combo2;
			case 3: return combo3;
			case 4: return combo4;
			case 5: return combo5;
			default: return null;
			}
		}
	}
	
	public class Editor extends DefaultCellEditor{
		private static final long serialVersionUID = 1L;
		public Editor() { this (combo1); }
		public Editor(JComboBox<?> comboBox) { super(comboBox); }
		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			switch(column) {
			case 1: return combo1;
			case 2: return combo2;
			case 3: return combo3;
			case 4: return combo4;
			case 5: return combo5;
			default: return null;
			}
		}	
	}
}