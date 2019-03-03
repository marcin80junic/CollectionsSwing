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

import javax.swing.JTable;
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
		loadFromFile();
		if(size()>0) {
			instantiateType(get(0));
		}
	}
	
	public void instantiateType(T item) {
		this.item = item;
		tableModel = new TableModelCollection<T>(this, item.getTableHeaders());
	}
	
	@SuppressWarnings("unchecked")
	private void loadFromFile() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile))) {
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
	
	public T remove(T item, int index) {
		item = super.remove(index);
		saveToFile();
		return item;
	}
	
	public DefaultTableColumnModel getTableColumnModel(JTable table) {
		if(tableColumnModel == null) {
			tableColumnModel = new DefaultTableColumnModel();
			TableColumn column = null;
			for(int i=0; i<table.getColumnModel().getColumnCount(); i++) {
				column = table.getColumnModel().getColumn(i);
				tableColumnModel.addColumn(column);
			}
			//tableColumnModel.setColumnMargin(0);
		}
		return tableColumnModel;
	}
	
	public DefaultTableColumnModel getComboHeaderTableColumnModel(JTable table) {
		if(comboTableColumnModel == null) {
			comboTableColumnModel = new DefaultTableColumnModel();
			TableColumn column = null;
			for(int i=0; i<table.getColumnModel().getColumnCount(); i++) {
				column = table.getColumnModel().getColumn(i);
				column.setHeaderRenderer(new TableHeaderRenderer());
				comboTableColumnModel.addColumn(column);
			}
			comboTableColumnModel.setColumnMargin(0);
		}
		return comboTableColumnModel;
	}
	
	public void setColumnsWidths(int ...widths) {
		if(comboTableColumnModel == null) return;
		for(int i=0; i<comboTableColumnModel.getColumnCount(); i++) {
			if(i==0) {
				if(tableModel.getRowCount() < 10) {
					tableColumnModel.getColumn(i).setMinWidth(15);
					tableColumnModel.getColumn(i).setMaxWidth(15);
					comboTableColumnModel.getColumn(i).setMinWidth(15);
					comboTableColumnModel.getColumn(i).setMaxWidth(15);
				} else if(tableModel.getRowCount() < 100) {
					tableColumnModel.getColumn(i).setMinWidth(25);
					tableColumnModel.getColumn(i).setMaxWidth(25);
					comboTableColumnModel.getColumn(i).setMinWidth(25);
					comboTableColumnModel.getColumn(i).setMaxWidth(25);
				} else if(tableModel.getRowCount() < 1000) {
					tableColumnModel.getColumn(i).setMinWidth(35);
					tableColumnModel.getColumn(i).setMaxWidth(35);
					comboTableColumnModel.getColumn(i).setMinWidth(35);
					comboTableColumnModel.getColumn(i).setMaxWidth(35);
				} else if(tableModel.getRowCount() < 10000) {
					tableColumnModel.getColumn(i).setMinWidth(45);
					tableColumnModel.getColumn(i).setMaxWidth(45);
					comboTableColumnModel.getColumn(i).setMinWidth(45);
					comboTableColumnModel.getColumn(i).setMaxWidth(45);
				}
			} else if(i == widths.length-1) {
				tableColumnModel.getColumn(i).setMinWidth(widths[i-1]-20);
				tableColumnModel.getColumn(i).setMaxWidth(widths[i-1]-20);
				comboTableColumnModel.getColumn(i).setMinWidth(widths[i-1]);
				comboTableColumnModel.getColumn(i).setMaxWidth(widths[i-1]);
			} else {
				tableColumnModel.getColumn(i).setMinWidth(widths[i-1]);
				tableColumnModel.getColumn(i).setMaxWidth(widths[i-1]);
				comboTableColumnModel.getColumn(i).setMinWidth(widths[i-1]);
				comboTableColumnModel.getColumn(i).setMaxWidth(widths[i-1]);
			}
		}
	}
	
	public TableModelCollection<T> getTableModel() { return tableModel.updateTableModel(); }
	public String[] getComboHeaders() { return item.getComboHeaders(); }
	public String getFilePath() { return dbFile.getPath(); }
	public String getName() { 
		if(item != null) return item.getName();
		return "";
	}
	public String[] getGenres() { return item.getGenres(); }
	public int getNumOfDiscs() { return item.getDiscs().length; }
	
	public void setComboFlags(boolean[] flags) {
		for (int i = 0; i < flags.length; i++) {
			comboFlags[i] = flags[i];
		}
	}

	public boolean[] getComboFlags() { return comboFlags; }


}