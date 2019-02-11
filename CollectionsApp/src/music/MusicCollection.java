package music;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import collectionsMain.*;

public class MusicCollection implements Collection<AudioCD> {
	
	private File audioFile;
	private LinkedList<AudioCD> mainDB;
	private TableModelCollection audioTableModel;
	private TableColumnModel musicColumnModel;
	private String columns[] = {"#", "Artist", "Album", "Genre", "Year"};
	private boolean statusChanged, isFiltered;
	boolean[] comboFlags = new boolean[4];
	
	
	public MusicCollection() {
		audioFile = new File("/users/marcin/eclipse-workspace/collectionsapp/music.dat");
		try {
			generateCollection();
		}catch(Exception exc) {exc.printStackTrace();}
		audioTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}
	
	public MusicCollection(File file) throws IOException {
		audioFile = new File(file.getAbsolutePath());
		generateCollection();
		audioTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void generateCollection() throws IOException {
		if(audioFile.isFile()) {
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(audioFile))){
				mainDB = new LinkedList<>((LinkedList<AudioCD>) ois.readObject());
			} catch (IOException | ClassNotFoundException exc) { throw new IOException(); }
		} else {
			mainDB = new LinkedList<>();
			saveCollection();
			generateCollection();
		}
	}
	
	@Override
	public MusicCollection loadCollection(File file) {
		try {
			MusicCollection mc = new MusicCollection(file);
			if(!mc.mainDB.isEmpty() && (mc.mainDB.getFirst() instanceof AudioCD)) return mc;
		} catch (IOException | NullPointerException e) { return null; }
		return null;
	}
	
	@Override
	public void reload() {	
		try {
			generateCollection();
		} catch (Exception exc) {exc.printStackTrace();}
		audioTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = false;
	}
	
	@Override
	public void saveCollection() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(audioFile, false))){
			out.writeObject(mainDB);
		} catch (IOException e) { e.printStackTrace(); }
		statusChanged = false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void saveCollection(LinkedList<?> dataBase) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(audioFile, false))){
			mainDB = new LinkedList<>((LinkedList<AudioCD>) dataBase);
			out.writeObject(mainDB);
		} catch (IOException e) { e.printStackTrace(); }
		statusChanged = false;
	}
	
	public static boolean isValidDatabase(File file) throws IOException {
		try {
			MusicCollection mc = new MusicCollection(file);
			if(!mc.mainDB.isEmpty() && (mc.mainDB.getFirst() instanceof AudioCD)) {
				return true;
			}
		} catch (IOException e) { throw new IOException(); }
		return false;
	}
	
	@Override
	public void exportTo(File file) {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false))) {
			out.writeObject(mainDB);
		} catch (IOException | NullPointerException e) { e.printStackTrace(); }
	}
	
	@Override
	public void addNewItem(String... strings) {
		String artist = strings[0];
		String albumName = strings[1];
		String genre = strings[2];
		int year = Integer.parseInt(strings[3]);
		int[] albumCds = new int[strings.length - 4];
		for(int i=4; i<strings.length; i++) {
			albumCds[i-4] = Integer.parseInt(strings[i]);
		}
		AudioCD cd = new AudioCD(artist, albumName, genre, year, albumCds);
		cd.setIndex(mainDB.size()+1);
		mainDB.add(cd);
		statusChanged = true;
	}
	
	@Override
	public void editItem(int index, String... strings) {
		String artist = strings[0];
		String albumName = strings[1];
		String genre = strings[2];
		int year = Integer.parseInt(strings[3]);
		int[] albumCds = new int[strings.length - 4];
		for(int i=4; i<strings.length; i++) {
			albumCds[i-4] = Integer.parseInt(strings[i]);
		}
		AudioCD cd = mainDB.get(index);
		cd.setArtist(artist);
		cd.setAlbumName(albumName);
		cd.setGenre(genre);
		cd.setAlbumCds(albumCds);
		cd.setYear(year);
		statusChanged = true;
	}
	
	@Override
	public boolean isStatusChanged() { return (audioTableModel.isModelChanged() || statusChanged) && !isFiltered; }
	
	@Override
	public void setFilteredStatus(boolean status) {	isFiltered = status; }

	@Override
	public LinkedList<AudioCD> getDataBase() { return new LinkedList<>(mainDB); }
	
	@Override
	public int sizeOfDB() {	return mainDB.size(); }
	
	@Override
	public ImageIcon getIcon() { return createImageIcon("/icons/Music.png", "audio"); }

	@Override
	public File getDBFile() { return audioFile; }

	@Override
	public String getItemName() { return "AudioCD"; }
	
	public static String getName() { return "AudioCD"; }
	
	@Override
	public AudioCD getItem(int index) {	return mainDB.get(index); }
	
	@Override
	public String printItem(Object item) {
		AudioCD cd = (AudioCD) item;
		return cd.printAudioCD();
	}
	
	@Override
	public String[] getGenreList() {
		ArrayList<String> genresArray = new ArrayList<>(Arrays.asList("Electronic dance", "Musical theatre", "Jazz", "Rock", "Pop", "Folk", "Classical", "Blues", "Hip hop", "Country",
				"Reggae", "Heavy metal", "Singing", "Punk rock", "Funk", "Alternative rock", "Disco", "House", "Opera", "Techno", "Soul",
				"Dance", "Instrumental", "Dubstep", "Rapping", "Drum and bass", "Ambient", "Trance", "Indie rock", "Orchestra", "Grunge",
				"Vocal", "Progressive rock", "Industrial", "Pop rock", "Black metal", "New wave", "Ska", "Hardcore punk", "Soundtrack",
				"Death metal", "Rock and roll", "Swing", "Electro", "Bluegrass", "Gospel", "Folk rock", "Alternative metal"));
		Collections.sort(genresArray);
		String[] genres = new String[genresArray.size()];
		genresArray.toArray(genres);
		return genres;
	}
	
	@Override
	public TableModelCollection getTableModel() { return audioTableModel; }
	
	@Override
	public TableColumnModel getTableColumnModel(JTable table) {
		if(musicColumnModel == null) {
			musicColumnModel = new DefaultTableColumnModel();
			for(int i=0; i<table.getColumnCount(); i++) {
				musicColumnModel.addColumn(table.getColumnModel().getColumn(i));
			}
		}
		return musicColumnModel;
	}
	
	@Override
	public void setColumnSize(JTable table) {
		TableColumn column;
		for (int i=0; i<table.getModel().getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			if(i==0) {
				int rowNumber = table.getModel().getRowCount();
				if(rowNumber < 10) {
					column.setMinWidth(15);
					column.setMaxWidth(15);
				} else if(rowNumber > 9 && rowNumber < 100) {
					column.setMinWidth(20);
					column.setMaxWidth(20);
				} else if(rowNumber > 99 && rowNumber < 1000) {
					column.setMinWidth(25);
					column.setMaxWidth(25);
				} else if(rowNumber > 999 && rowNumber < 10000) {
					column.setMinWidth(30);
					column.setMaxWidth(30);
				} else {
					column.setMinWidth(35);
					column.setMaxWidth(35);
				}
			}
			else if(i==1) {
				column.setMinWidth(245);
				column.setMaxWidth(245);
			}
			else if(i==2) {
				column.setMinWidth(270);
				column.setMaxWidth(270);
			}
			else if(i==3) {
				column.setMinWidth(200);
				column.setMaxWidth(200);
			}
			else if(i==4) {
				column.setMinWidth(75);
				column.setMaxWidth(75);
			}
		}
	}
	
	@Override
	public String[] getComboHeaders () {
		String[] headers = {"--Select Artist--", "--Select Album--", "--Select Genre--", "-Year-"};
		return headers;
	}
	
	@Override
	public void setComboFlags(boolean[] flags) { comboFlags = flags; }

	@Override
	public boolean[] getComboFlags() { return comboFlags; }
	
	@Override
	public Iterator<AudioCD> iterator() { return mainDB.iterator(); }

}