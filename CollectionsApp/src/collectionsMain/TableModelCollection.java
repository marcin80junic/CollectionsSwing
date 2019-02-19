package collectionsMain;

import java.util.Collections;
import java.util.Comparator;
import javax.swing.table.*;
import collectableItems.Collectable;

public class TableModelCollection<T extends DataBase<S>, S extends Collectable> extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private T dataBase, origin;
	private String columns[];
	
	
	@SuppressWarnings("unchecked")
	public TableModelCollection(T dataBase, String[] columns) {
		origin = dataBase;
		this.dataBase = (T) new DataBase<S>(dataBase);
		this.columns = columns;
	}
	
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
	
	public void addItem(S item) {
		dataBase.add(item);
		origin.add(item);
		fireTableRowsInserted(dataBase.size()-1, dataBase.size()-1);
	}
	
	public void removeItem(int index) {
		dataBase.remove(index);
		origin.remove(index);
		fireTableRowsDeleted(index, index);
	}
	
	public void sort(int columnIndex, boolean toggle) {
		Comparator<S> comp = (str1, str2) -> ((String) getValueAt(dataBase.indexOf(str1), columnIndex)).compareToIgnoreCase
				(((String) getValueAt(dataBase.indexOf(str2), columnIndex)));
		if(toggle) Collections.sort(dataBase, comp);
		else Collections.sort(dataBase, comp.reversed());
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
		S item = dataBase.get(rowIndex);
		String columnHeader = columns[columnIndex];
		Object value = (columnIndex==0)? String.valueOf(item.getID()): returnValue(item, columnHeader);
		return value;
	}
	
	private Object returnValue(S item, String columnHeader) {
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
		S item = dataBase.get(rowIndex);
		if(columnIndex == 0) item.setID((int) value);
	}
	
}