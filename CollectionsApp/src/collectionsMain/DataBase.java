package collectionsMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import collectableItems.Collectable;

public class DataBase <T extends Collectable> extends ArrayList<T> {
	
	private static final long serialVersionUID = 1L;
	private File dbFile;
	private boolean[] comboFlags = new boolean[5];

	
	public DataBase(DataBase<T> dataBase) {
		super(dataBase);
	}
	
	public DataBase(String filePath) {
		super();
		dbFile = new File(filePath);
		loadFromFile();
	}
	
	@SuppressWarnings("unchecked")
	private void loadFromFile() {
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dbFile))) {
			addAll((Collection<T>) ois);
		} catch (FileNotFoundException e) {	e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	
	
	public void setComboFlags(boolean[] flags) {
		for (int i = 0; i < flags.length; i++) {
			comboFlags[i] = flags[i];
		}
	}

	public boolean[] getComboFlags() { return comboFlags; }


}