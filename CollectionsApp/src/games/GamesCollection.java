package games;

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
import collectionsMain.Collection;
import collectionsMain.TableModelCollection;


public class GamesCollection implements Collection<Game> {
	
	private File gamesFile;
	private LinkedList<Game> mainDB;
	private TableModelCollection gamesTableModel;
	private TableColumnModel gamesColumnModel;
	private String columns[] = {"#", "Title", "Series", "Genre", "Studio", "Year"};
	private boolean statusChanged, isFiltered;
	private boolean[] comboFlags = new boolean[5];
	
	
	public GamesCollection() {
		gamesFile = new File("/users/marcin/eclipse-workspace/collectionsapp/games.dat");
		try {
			generateCollection();
		} catch (Exception exc) { exc.printStackTrace(); }
		gamesTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}
	
	public GamesCollection(File file) throws IOException {	
		gamesFile = new File(file.getAbsolutePath());
		generateCollection();
		gamesTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateCollection() throws IOException {	
		if(gamesFile.isFile()) {
			try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(gamesFile))){
				mainDB = new LinkedList<>((LinkedList<Game>)oin.readObject());
			} catch (IOException | ClassNotFoundException | NullPointerException exc) { throw new IOException(); }
		} else {
			mainDB = new LinkedList<>();
			saveCollection();
			generateCollection();
		}
	}
	
	@Override
	public GamesCollection loadCollection(File file) {
		try {
			GamesCollection gc = new GamesCollection(file);
			if(!gc.mainDB.isEmpty() && (gc.mainDB.getFirst() instanceof Game)) return gc;
		} catch (IOException | NullPointerException e) { return null; }
		return null;
	}
	
	@Override
	public void reload() {
		try {
			generateCollection();
		} catch (Exception exc) {exc.printStackTrace();}
		gamesTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = false;
	}

	@Override
	public void saveCollection() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(gamesFile, false))){
			out.writeObject(mainDB);
		} catch (IOException e) { e.printStackTrace(); }
		statusChanged = false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void saveCollection(LinkedList<?> dataBase) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(gamesFile, false))){
			mainDB = new LinkedList<>((LinkedList<Game>) dataBase);
			out.writeObject(mainDB);
		} catch (IOException e) { e.printStackTrace(); }
		statusChanged = false;
	}
	
	public static boolean isValidDatabase(File file) throws IOException {	
		try {
			GamesCollection gc = new GamesCollection(file);
			if(!gc.mainDB.isEmpty() && (gc.mainDB.getFirst() instanceof Game)) {
				return true;
			}
		} catch (IOException | NullPointerException e) { throw new IOException(); }
		return false;
	}
	
	@Override
	public void exportTo(File file) {
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false))) {
			out.writeObject(mainDB);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	@Override
	public void addNewItem(String... s) {
		String title = s[0];
		String series = s[1];
		String genre = s[2];
		String studio = s[3];
		int year = Integer.parseInt(s[4]);
		Game game = new Game(title, series, genre, studio, year);
		game.setIndex(mainDB.size()+1);
		mainDB.add(game);
		statusChanged = true;
	}
	
	@Override
	public void editItem(int index, String... s) {
		String title = s[0];
		String series = s[1];
		String genre = s[2];
		String studio = s[3];
		int year = Integer.parseInt(s[4]);
		Game game = mainDB.get(index);
		game.setTitle(title);
		game.setSeries(series);
		game.setGenre(genre);
		game.setStudio(studio);
		game.setYear(year);
		statusChanged = true;
	}

	@Override
	public boolean isStatusChanged() { return (gamesTableModel.isModelChanged() || statusChanged) && !isFiltered; }
	
	@Override
	public void setFilteredStatus(boolean status) { isFiltered = status; }
	
	@Override
	public LinkedList<Game> getDataBase(){ return new LinkedList<Game>(mainDB); }
	
	@Override
	public Game getItem(int index) { return mainDB.get(index); }

	@Override
	public ImageIcon getIcon() { return createImageIcon("/icons/Games.png", "games"); }

	@Override
	public File getDBFile() { return gamesFile; }

	@Override
	public String getItemName() { return "Game"; }

	public static String getName() { return "Game"; }

	@Override
	public String printItem(Object item) {	
		Game game = (Game) item;
		return game.printGame();
	}

	@Override
	public String[] getGenreList() {
		ArrayList<String> genresArray = new ArrayList<>(Arrays.asList("Action", "RPG"));
		Collections.sort(genresArray);
		String[] genres = new String[genresArray.size()];
		genresArray.toArray(genres);
		return genres;
	}
	
	@Override
	public int sizeOfDB() { return mainDB.size(); }

	@Override
	public TableModelCollection getTableModel() { return gamesTableModel; }
	
	@Override
	public TableColumnModel getTableColumnModel(JTable table) {
		if(gamesColumnModel == null) {
			gamesColumnModel = new DefaultTableColumnModel();
			for(int i=0; i<table.getColumnCount(); i++) {
				gamesColumnModel.addColumn(table.getColumnModel().getColumn(i));
			}
		}
		return gamesColumnModel;
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
				column.setMinWidth(195);
				column.setMaxWidth(195);
			}
			else if(i==2) {
				column.setMinWidth(190);
				column.setMaxWidth(190);
			}
			else if(i==3) {
				column.setMinWidth(180);
				column.setMaxWidth(180);
			}
			else if(i==4) {
				column.setMinWidth(150);
				column.setMaxWidth(150);
			}
			else if(i==5) {
				column.setMinWidth(75);
				column.setMaxWidth(75);
			}
		}
	}
	
	@Override
	public String[] getComboHeaders () {
		String[] headers = {" --Select Title-- ", " --Select Series-- ", " --Select Genre-- ", " --Select Studio-- ", "-Year-"};
		return headers;
	}

	@Override
	public void setComboFlags(boolean[] flags) { comboFlags = flags; }

	@Override
	public boolean[] getComboFlags() { return comboFlags; }

	@Override
	public Iterator<Game> iterator() { return mainDB.iterator(); }

}
