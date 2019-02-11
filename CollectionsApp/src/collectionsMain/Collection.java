package collectionsMain;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;


public interface Collection<E> extends Iterable<E> {
	
	void generateCollection() throws IOException;
	Collection <E> loadCollection(File file);
	void reload();
	void saveCollection();
	void saveCollection(LinkedList<?> dataBase);
	void exportTo(File file);
	void addNewItem(String... s);
	void editItem(int index, String... s);
	boolean isStatusChanged();
	void setFilteredStatus(boolean status);
	
	LinkedList<E> getDataBase();
	int sizeOfDB();
	ImageIcon getIcon();
	File getDBFile();
	String getItemName();
	E getItem(int index);
	String printItem(Object item);
	TableModelCollection getTableModel();
	TableColumnModel getTableColumnModel(JTable table);
	void setColumnSize(JTable table);
	String[] getGenreList();
	String[] getComboHeaders();
	boolean[] getComboFlags();
	void setComboFlags(boolean[] flags);
	
	default ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
