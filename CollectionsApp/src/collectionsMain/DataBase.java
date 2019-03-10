package collectionsMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import collectableItems.AbstractItem;
import collectableItems.Collectable;

public class DataBase <T extends Collectable<? extends AbstractItem>> extends ArrayList<T> {
	
	private static final long serialVersionUID = 1L;
	private T item;
	private File dbFile;
	private TableModelCollection<T> tableModel;
	private DefaultTableColumnModel comboTableColumnModel, tableColumnModel;
	private boolean[] comboFlags = new boolean[5];

	
	public DataBase() { super(); }
	
	public DataBase(DataBase<T> dB) { 
		super(dB);
		this.item = dB.item;
		this.dbFile = dB.dbFile;
	}
	
	public DataBase(String filePath) { 
		this(new File(filePath)); 
	}
	
	public DataBase(File file) {
		super();
		dbFile = file;
		if(!dbFile.isFile()) saveToFile();
		else loadFromFile(dbFile);
		if(size()>0) {
			instantiateType(get(0));
		}
	}
	
	public void instantiateType(T item) {
		this.item = item;
		tableModel = new TableModelCollection<T>(this, item.getTableHeaders());
	}
	
	@SuppressWarnings("unchecked")
	private void loadFromFile(File file) {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			Collection<T> list = (Collection<T>)ois.readObject();
			addAll(list);
		} catch (FileNotFoundException e) {	e.printStackTrace(); } 
		  catch (IOException e) { e.printStackTrace(); } 
		  catch (ClassNotFoundException e) { e.printStackTrace(); }
	}
	
	void saveToFile() {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dbFile))){
			ArrayList<T> list = new ArrayList<>(this);
			out.writeObject(list);
		} catch (FileNotFoundException e) { e.printStackTrace(); } 
		  catch (IOException e) { e.printStackTrace(); }
	}
	
	void saveToFile(String path) {
		dbFile = new File(path);
		saveToFile();
	}
	
	void saveToFile(File file) {
		dbFile = file;
		saveToFile();
	}
	
	@SuppressWarnings("unchecked")
	public T create(String[] data) {
		item = (T) item.createItem(data);
		add(item);
		saveToFile();
		return item;
	}
	
	public boolean remove(T item) {
		boolean success = super.remove(item);
		saveToFile();
		return success;
	}
	
	public DefaultTableColumnModel getTableColumnModel(JTable table) {
		if(tableColumnModel == null) {
			tableColumnModel = new DefaultTableColumnModel();
			for(int i=0; i<table.getColumnModel().getColumnCount(); i++) {
				tableColumnModel.addColumn(table.getColumnModel().getColumn(i));
			}
			tableColumnModel.setColumnMargin(0);
		}
		return tableColumnModel;
	}
	
	public DefaultTableColumnModel getComboHeaderTableColumnModel(JTable table) {
		if(comboTableColumnModel == null) {
			comboTableColumnModel = new DefaultTableColumnModel();
			TableColumn column = null;
			for(int i=0; i<table.getColumnModel().getColumnCount(); i++) {
				column = table.getColumnModel().getColumn(i);
				comboTableColumnModel.addColumn(column);
			}
			comboTableColumnModel.setColumnMargin(0);
			comboTableColumnModel.addColumnModelListener(new TableColumnModelListener() {
				@Override
				public void columnAdded(TableColumnModelEvent e) { }
				@Override
				public void columnRemoved(TableColumnModelEvent e) { }
				@Override
				public void columnMoved(TableColumnModelEvent e) { }
				@Override
				public void columnMarginChanged(ChangeEvent e) { synchronizeTables(); }
				@Override
				public void columnSelectionChanged(ListSelectionEvent e) { }
			});
		}
		initHeaderRenderers();
		return comboTableColumnModel;
	}
	
	public void setColumnWidths(int rows, int ...widths) {
		if(comboTableColumnModel == null || tableColumnModel == null) return;
		int perc = 0;
		for(int i=0; i<comboTableColumnModel.getColumnCount(); i++) {
			if(i==0) {
				int a = 9+4*String.valueOf(rows).length();
				perc = 4*(String.valueOf(rows).length()-1)/(comboTableColumnModel.getColumnCount()-2)+1;
				tableColumnModel.getColumn(i).setMinWidth(a);
				tableColumnModel.getColumn(i).setMaxWidth(a);
				comboTableColumnModel.getColumn(i).setMinWidth(a);
				comboTableColumnModel.getColumn(i).setMaxWidth(a);
			} else if((widths.length != 0) && (i==comboTableColumnModel.getColumnCount()-1)) {
				tableColumnModel.getColumn(i).setMinWidth(15);
				tableColumnModel.getColumn(i).setPreferredWidth(widths[i-1]-20-perc);
				comboTableColumnModel.getColumn(i).setMinWidth(15+20);
				comboTableColumnModel.getColumn(i).setPreferredWidth(widths[i-1]-perc);
			} else if(widths.length != 0) {
				tableColumnModel.getColumn(i).setPreferredWidth(widths[i-1]-perc);
				comboTableColumnModel.getColumn(i).setPreferredWidth(widths[i-1]-perc);
			}
		}
	}
	
	private void synchronizeTables() {
		for(int i=0; i<comboTableColumnModel.getColumnCount(); i++) {
			if(i==0) continue;
			else if(i==comboTableColumnModel.getColumnCount()-1) {
				tableColumnModel.getColumn(i).setPreferredWidth(comboTableColumnModel.getColumn(i).getWidth()-20);
			}
			else tableColumnModel.getColumn(i).setPreferredWidth(comboTableColumnModel.getColumn(i).getWidth());
		}
	}
	
	private void initHeaderRenderers() {
		for(int i=0; i<comboTableColumnModel.getColumnCount(); i++) {
			comboTableColumnModel.getColumn(i).setHeaderRenderer(new TableHeaderRenderer(tableModel));
		}	
	}
	
	public TableModelCollection<T> getTableModel() { return tableModel.updateTableModel(); }
	public String[] getComboHeaders() { return item.getComboHeaders(); }
	public String getFilePath() { return dbFile.getPath(); }
	public String getName() { return (item != null)? item.getName(): ""; }
	public String[] getGenres() { return item.getGenres(); }
	public int getNumOfDiscs() { return item.getDiscs().length; }
	public void setComboFlags(boolean[] flags) { for (int i = 0; i < flags.length; i++) comboFlags[i] = flags[i]; }
	public boolean[] getComboFlags() { return comboFlags; }
	public ImageIcon getIcon() { return item.createImageIcon(item.getIconPath(), "Collection's icon"); }

}