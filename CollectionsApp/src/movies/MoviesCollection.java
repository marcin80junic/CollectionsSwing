package movies;

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


public class MoviesCollection implements Collection<Movie> {
	
	private File moviesFile;
	private LinkedList<Movie> mainDB;
	private TableModelCollection moviesTableModel;
	private TableColumnModel moviesColumnModel;
	private String columns[] = {"#", "Title", "Series", "Genre", "Studio", "Year"};
	private boolean statusChanged, isFiltered;
	private boolean[] comboFlags = new boolean[5];
	
	public MoviesCollection() {
		moviesFile = new File("/users/marcin/eclipse-workspace/collectionsapp/movies.dat");
		try {
			generateCollection();
		} catch (Exception exc) {exc.printStackTrace();}
		moviesTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}
	
	public MoviesCollection(File file) throws IOException {
		moviesFile = new File(file.getAbsolutePath());
		generateCollection();
		moviesTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateCollection() throws IOException {
		if(moviesFile.isFile()) {
			try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(moviesFile))){
				mainDB = new LinkedList<>((LinkedList<Movie>)oin.readObject());
			} catch (IOException | ClassNotFoundException exc) { throw new IOException(); }	
		} else {
			mainDB = new LinkedList<>();
			saveCollection();
			generateCollection();
		}
	}
	
	@Override
	public MoviesCollection loadCollection(File file) {
		try {
			MoviesCollection mc = new MoviesCollection(file);
			if(!mc.mainDB.isEmpty() && (mc.mainDB.getFirst() instanceof Movie)) return mc;
		} catch (IOException | NullPointerException e) { return null; }
		return null;
	}
	
	@Override
	public void reload() {	
		try {
			generateCollection();
		} catch (Exception exc) {exc.printStackTrace();}
		moviesTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = false;
	}
	
	@Override
	public void saveCollection() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(moviesFile, false))){
			out.writeObject(mainDB);
		} catch (IOException e) { e.printStackTrace(); }
		statusChanged = false;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void saveCollection(LinkedList<?> dataBase) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(moviesFile, false))){
			mainDB = new LinkedList<>((LinkedList<Movie>) dataBase);
			out.writeObject(mainDB);
		} catch (IOException e) { e.printStackTrace(); }
		statusChanged = false;
	}
	
	public static boolean isValidDatabase(File file) throws IOException {
		try {
			MoviesCollection mc = new MoviesCollection(file);
			if(!mc.mainDB.isEmpty() && (mc.mainDB.getFirst() instanceof Movie)) {
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
	public void addNewItem(String...s) {
		String title = s[0];
		String series = s[1];
		String genre = s[2];
		String studio = s[3];
		int year = Integer.parseInt(s[4]);
		Movie movie = new Movie(title, series, genre, studio, year);
		movie.setIndex(mainDB.size()+1);
		mainDB.add(movie);
		statusChanged = true;
	}
	
	@Override
	public void editItem(int index, String... s) {	
		String title = s[0];
		String series = s[1];
		String genre = s[2];
		String studio = s[3];
		int year = Integer.parseInt(s[4]);
		Movie movie = mainDB.get(index);
		movie.setTitle(title);
		movie.setSeries(series);
		movie.setGenre(genre);
		movie.setStudio(studio);
		movie.setYear(year);
		statusChanged = true;
	}
	
	@Override
	public boolean isStatusChanged() { return (moviesTableModel.isModelChanged() || statusChanged) && !isFiltered; }
	
	@Override
	public void setFilteredStatus(boolean status) {	isFiltered = status; }

	@Override
	public LinkedList<Movie> getDataBase(){ return new LinkedList<>(mainDB); }
	
	@Override
	public int sizeOfDB() { return mainDB.size(); }
	
	@Override
	public ImageIcon getIcon() { return createImageIcon("/icons/Movies.png", "movies");	}
	
	@Override
	public File getDBFile() { return moviesFile; }

	@Override
	public String getItemName() { return "Movie"; }
	
	public static String getName() { return "Movie"; }
	
	@Override
	public Movie getItem(int index) { return mainDB.get(index);	}
	
	@Override
	public String printItem(Object item) {	
		Movie movie = (Movie) item;
		return movie.printMovie();
	}
	
	@Override
	public String[] getGenreList() {	
		ArrayList<String> genresArray = new ArrayList<>(Arrays.asList("Romance", "Western", "Comedy", "Drama", "Animation", "Pornographic", "Horror", "Action",
				"Adventure", "Documentary", "Thriller", "Crime", "Fiction", "War", "Epic", "Romantic comedy",
				 "Indie", "Disaster", "Science fiction", "Noir", "Biographical", "Superhero", "Musical", "Mystery", "Martial arts",
				"Historical", "Music", "Fantasy", "Experimental", "Spy", "Satire", "Mockumentary", "Family", "Coming-of-age fiction", 
				"Art", "Melodrama", "Slasher", "Black comedy", "Apocalyptic fiction", "Psycological thriller", "Teen", "Gangster",
				"Parody", "Exploitation", "News"));
		Collections.sort(genresArray);
		String[] genres = new String[genresArray.size()];
		genresArray.toArray(genres);
		return genres;
	}
	
	@Override
	public TableModelCollection getTableModel() { return moviesTableModel; }
	
	@Override
	public TableColumnModel getTableColumnModel(JTable table) {
		if(moviesColumnModel == null) {
			moviesColumnModel = new DefaultTableColumnModel();
			for(int i=0; i<table.getColumnCount(); i++) {
				moviesColumnModel.addColumn(table.getColumnModel().getColumn(i));
			}
		}
		return moviesColumnModel;
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
				column.setMinWidth(210);
				column.setMaxWidth(210);
			}
			else if(i==2) {
				column.setMinWidth(195);
				column.setMaxWidth(195);
			}
			else if(i==3) {
				column.setMinWidth(150);
				column.setMaxWidth(150);
			}
			else if(i==4) {
				column.setMinWidth(155);
				column.setMaxWidth(155);
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
	public Iterator<Movie> iterator() { return mainDB.iterator(); }
	
}
