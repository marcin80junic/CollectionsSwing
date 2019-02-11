package item;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import collectionsMain.Collection;
import collectionsMain.TableModelCollection;

public class ItemCollection implements Collection<Item> {

	@Override
	public Iterator<Item> iterator() { return null;	}

	@Override
	public void generateCollection() throws IOException { }

	@Override
	public Collection<Item> loadCollection(File file) {	return null; }

	@Override
	public void reload() { }

	@Override
	public void saveCollection() { }

	@Override
	public void saveCollection(LinkedList<?> dataBase) { }

	@Override
	public void exportTo(File file) { }

	@Override
	public void addNewItem(String... s) { }

	@Override
	public void editItem(int index, String... s) { }

	@Override
	public boolean isStatusChanged() { return false; }

	@Override
	public void setFilteredStatus(boolean status) {	}

	@Override
	public LinkedList<Item> getDataBase() {	return null; }

	@Override
	public int sizeOfDB() {	return 0; }

	@Override
	public ImageIcon getIcon() { return null; }

	@Override
	public String getItemName() { return "Item"; }

	@Override
	public Item getItem(int index) { return null; }

	@Override
	public String printItem(Object item) { return null; }

	@Override
	public String[] getGenreList() { return null; }

	@Override
	public TableModelCollection getTableModel() { return null; }

	@Override
	public void setColumnSize(JTable table) { }

	@Override
	public String[] getComboHeaders() { return null; }

	@Override
	public void setComboFlags(boolean[] flags) { }

	@Override
	public boolean[] getComboFlags() { return null; }

	@Override
	public TableColumnModel getTableColumnModel(JTable table) {	return null; }

	@Override
	public File getDBFile() { return null; }

}
