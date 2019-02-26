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
import collectableItems.AbstractItem;
import collectableItems.Collectable;

public class DataBase <T extends Collectable<? extends AbstractItem>> extends ArrayList<T> {
	
	private static final long serialVersionUID = 1L;
	private File dbFile;
	private TableModelCollection<T> tableModel;
	private T item;
	private boolean[] comboFlags = new boolean[5];

	
	public DataBase() { super(); }
	
	public DataBase(DataBase<T> dB) { 
		super(dB);
		this.item = dB.item;
		this.dbFile = dB.dbFile;
	}
	
	public DataBase(String filePath) { this(new File(filePath)); }
	
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
		item.setID(size()+1);
		return item;
	}
	
	public boolean add(T item) {
		boolean success = super.add(item);
		saveToFile();
		return success;
	}
	
	public T remove(int index) {
		T item = super.remove(index);
		saveToFile();
		return item;
	}
	
	public boolean remove(Object item) {
		boolean success = super.remove(item);
		saveToFile();
		return success;
	}
	
	public TableModelCollection<T> getTableModel() { return tableModel; }
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