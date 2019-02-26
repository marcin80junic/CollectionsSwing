package collectionsMain;

import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.*;
import collectableItems.AbstractItem;
import collectableItems.Collectable;

public class TableModelCollection<T extends Collectable<? extends AbstractItem>> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private DataBase<T> dataBase, origin;
	private String columns[];
	private boolean filtered, sorted;
	private static int ascending, descending;
	
	
	public TableModelCollection(DataBase<T> dataBase, String[] columns) {
		origin = dataBase;
		this.dataBase = new DataBase<T>(dataBase);
		this.columns = columns;
		filtered = sorted = false;
	}
	
	public TableModelCollection<T> reloadTableModel(){
		dataBase = new DataBase<T>(origin);
		filtered = false;
		ascending = descending = 0;
		return this;
	}
	
	public void saveDataBase() {
		origin = dataBase;
		dataBase.saveToFile();
		sorted = false;
	}
	
	public int getAscending() { return ascending; }
	public int getDescending() { return descending; }
	public boolean isChanged() { return sorted && !filtered; }
	public boolean isFiltered() { return filtered; }
	public void setFiltered(boolean filtered) { this.filtered = filtered; }
	
	@Override
	public int getRowCount() { return dataBase.size(); }

	@Override
	public int getColumnCount() { return columns.length; }
	
	@Override
	public String getColumnName(int columnIndex) { return columns[columnIndex]; }
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		if(dataBase.isEmpty()) return Object.class;
		return getValueAt(0, columnIndex).getClass();
	}
	
	public T getItem(int index) { return dataBase.get(index); }
	
	/**public void addItem(T item) {
		dataBase.add(item);
		origin.add(item);
		ascending = descending = 0;
		fireTableRowsInserted(dataBase.size()-1, dataBase.size()-1);
	}*/
	
	public void createItem(String[] data) {
		T item = dataBase.create(data);
		dataBase.add(item);
		origin.add(item);
		fireTableRowsInserted(getRowCount(), getRowCount());
	}
	
	public void editItem(int index, String[] newData) {
		T item = dataBase.get(index);
		item.editItem(newData);
	}
	
	public void removeItem(int index) {
		dataBase.remove(index);
		fireTableRowsDeleted(index, index);
	}
	
	public void removeItem(Object item) {
		int index = dataBase.indexOf(item);
		dataBase.remove(item);
		fireTableRowsDeleted(index, index);
	}
	
	public void moveItemUp(int index) {
		dataBase.add(index-1, dataBase.remove(index));
		ascending = descending = 0;
	}
	
	public void moveItemDown(int index) {
		dataBase.add(index+1, dataBase.remove(index));
		ascending = descending = 0;
	}
	
	public void sort(int columnIndex, boolean toggle) {
		Comparator<T> comp = (str1, str2) -> ((String) getValueAt(dataBase.indexOf(str1), columnIndex)).compareToIgnoreCase
				(((String) getValueAt(dataBase.indexOf(str2), columnIndex)));
		if(!toggle) {
			Collections.sort(dataBase, comp);
			ascending = columnIndex;
		}
		else {
			Collections.sort(dataBase, comp.reversed());
			descending = columnIndex;
		}
		sorted = true;
		fireTableDataChanged();
	}
	
	public void searchFor(String text) {
		String search = text.toLowerCase();
		String value = null;
		if(!dataBase.isEmpty()) {
			for(int row = getRowCount()-1; row>=0; row--) {
				for(int col = getColumnCount()-1; col>0; col--) {
					value = ((String)getValueAt(row, col)).toLowerCase();
					if(value.contains(search)) break;
					if(col == 1) dataBase.remove(row);
				}
			}
		}
		fireTableDataChanged();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		T item = dataBase.get(rowIndex);
		String columnHeader = columns[columnIndex];
		Object value = (columnIndex==0)? String.valueOf(item.getID()): returnValue(item, columnHeader);
		return value;
	}
	
	private Object returnValue(T item, String columnHeader) {
		Object returnValue = null;
		if(columnHeader.equals("Artist") || columnHeader.equals("Author") || columnHeader.equals("Studio")) returnValue = item.getAuthor();
		else if(columnHeader.equals("Title") || columnHeader.equals("Album")) returnValue = item.getTitle();
		else if(columnHeader.equals("Series")) returnValue = item.getSeries();
		else if(columnHeader.equals("Genre")) returnValue = item.getGenre();
		else if(columnHeader.equals("CDs")) returnValue = String.valueOf(item.getDiscs().length);
		else if(columnHeader.equals("Year")) returnValue = String.valueOf(item.getYear());
		return returnValue;
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		T item = dataBase.get(rowIndex);
		if(columnIndex == 0) item.setID((int) value);
	}
	
}