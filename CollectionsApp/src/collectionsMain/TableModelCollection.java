package collectionsMain;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import javax.swing.table.*;
import books.Book;
import games.Game;
import movies.Movie;
import music.AudioCD;

public class TableModelCollection extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static final int COLUMN_NO = 0;
	private static final int COLUMN_1 = 1;
	private static final int COLUMN_2 = 2;
	private static final int COLUMN_3 = 3;
	private static final int COLUMN_4 = 4;
	private static final int COLUMN_5 = 5;
	
	private Collection<?> collection;
	private LinkedList<?> dataBase;
	private LinkedList<Book> booksCollection;
	private LinkedList<AudioCD> musicCollection;
	private LinkedList<Movie> moviesCollection;
	private LinkedList<Game> gamesCollection;
	private String columns[];
	private boolean isModelChanged;
	private static int columnIndexAsc, columnIndexDesc;
		
	
	public TableModelCollection(Collection<?> collection, LinkedList<?> dataBase, String[] columns) {
		
		this.collection = collection;
		this.dataBase = new LinkedList<>(dataBase);
		this.columns = new String[columns.length];
		this.columns = columns;
		initCollections();
	}
	
	
	public int getRowCount() {
		
		return dataBase.size();
	}

	
	public int getColumnCount() {
		
		return columns.length;
	}
	
	
	public String getColumnName(int columnIndex) {
		
		return columns[columnIndex];
	}
	
	
	public Class<?> getColumnClass(int columnIndex){
		
		if(dataBase.isEmpty()) return Object.class;
		return getValueAt(0, columnIndex).getClass();
	}
	
	
	public Object getElement(int index) {
		
		return dataBase.get(index);
	}
	
	
	@SuppressWarnings("unchecked")
	public void addElement(Object element) {
		
		if (element instanceof Book) {
			if(booksCollection==null) booksCollection = new LinkedList<>((LinkedList<Book>) dataBase);
			((Book) element).setIndex(booksCollection.size()+1);
			booksCollection.add((Book) element);
			dataBase = booksCollection;
		}
		else if (element instanceof AudioCD) {
			if(musicCollection==null) musicCollection = new LinkedList<>((LinkedList<AudioCD>) dataBase);
			((AudioCD) element).setIndex(musicCollection.size()+1);
			musicCollection.add((AudioCD) element);
			dataBase = musicCollection;
		}
		else if (element instanceof Movie) {
			if(moviesCollection==null) moviesCollection = new LinkedList<>((LinkedList<Movie>) dataBase);
			((Movie) element).setIndex(moviesCollection.size()+1);
			moviesCollection.add((Movie) element);
			dataBase = moviesCollection;
		}
		else if (element instanceof Game) {
			if(gamesCollection==null) gamesCollection = new LinkedList<>((LinkedList<Game>) dataBase);
			((Game) element).setIndex(gamesCollection.size()+1);
			gamesCollection.add((Game) element);
			dataBase = gamesCollection;
		}
		isModelChanged = true;
		columnIndexAsc = columnIndexDesc = -1;
		fireTableRowsInserted(dataBase.size()-1, dataBase.size()-1);
	}
	
	public void addElementAt(Object element, int row) {
		
		if (element instanceof Book) {
			((Book) element).setIndex(row+1);
			booksCollection.add(row, (Book) element);
			dataBase = booksCollection;
		}
		else if (element instanceof AudioCD) {
			((AudioCD) element).setIndex(row+1);
			musicCollection.add(row, (AudioCD) element);
			dataBase = musicCollection;
		}
		else if (element instanceof Movie) {
			((Movie) element).setIndex(row+1);
			moviesCollection.add(row, (Movie) element);
			dataBase = moviesCollection;
		}
		else if (element instanceof Game) {
			((Game) element).setIndex(row+1);
			gamesCollection.add(row, (Game) element);
			dataBase = gamesCollection;
		}
		isModelChanged = true;
		columnIndexAsc = columnIndexDesc = -1;
		fireTableRowsInserted(row, row);
	}
	
	public void editElement(int index, Object element) {
		
		if (element instanceof Book) {
			Book b = (Book) element;
			Book book = booksCollection.get(index);
			book.setAuthor(b.getAuthor());
			book.setSeries(b.getSeries());
			book.setTitle(b.getTitle());
			book.setGenre(b.getGenre());
			book.setYear(b.getYear());
		}
		else if (element instanceof AudioCD) {
			AudioCD cd = (AudioCD) element;
			AudioCD cdChanged = musicCollection.get(index);
			cdChanged.setArtist(cd.getArtist());
			cdChanged.setAlbumName(cd.getAlbumName());
			cdChanged.setGenre(cd.getGenre());
			cdChanged.setYear(cd.getYear());
			cdChanged.setAlbumCds(cd.getAlbumCds());
		}
		else if (element instanceof Movie) {
			Movie m = (Movie) element;
			Movie movie = moviesCollection.get(index);
			movie.setTitle(m.getTitle());
			movie.setSeries(m.getSeries());
			movie.setGenre(m.getGenre());
			movie.setStudio(m.getStudio());
			movie.setYear(m.getYear());
		}
		else if (element instanceof Game) {
			Game g = (Game) element;
			Game game = gamesCollection.get(index);
			game.setTitle(g.getTitle());
			game.setSeries(g.getSeries());
			game.setGenre(g.getGenre());
			game.setStudio(g.getStudio());
			game.setYear(g.getYear());
		}
		isModelChanged = true;
		columnIndexAsc = columnIndexDesc = -1;
		fireTableRowsUpdated(index, index);
	}
	
	public void removeElement(int index) {
		
		dataBase.remove(index);
		isModelChanged = true;
		columnIndexAsc = columnIndexDesc = -1;
		fireTableRowsDeleted(index, index);
	}
	
	public void removeElement(Object element, int index) {
		
		if (element instanceof Book) {
			booksCollection.remove((Book) element);
			dataBase = booksCollection;
		}
		else if (element instanceof AudioCD) {
			musicCollection.remove((AudioCD) element);
			dataBase = musicCollection;
		}
		else if (element instanceof Movie) {
			moviesCollection.remove((Movie) element);
			dataBase = moviesCollection;
		}
		else if (element instanceof Game) {
			gamesCollection.remove((Game) element);
			dataBase = gamesCollection;
		}
		isModelChanged = true;
		columnIndexAsc = columnIndexDesc = -1;
		fireTableRowsDeleted(index, index);
	}
	
	
	public void moveItemUp(int row) {
		
		Object o = dataBase.get(row);
		
		if(o instanceof Book) {
			Book b = (Book) o;
			b.setIndex(row);
			booksCollection.remove(row);
			booksCollection.add(row-1, b);
			booksCollection.get(row).setIndex(row+1);
			dataBase = booksCollection;
		}
		else if(o instanceof Game) {
			Game g = (Game) o;
			g.setIndex(row);
			gamesCollection.remove(row);
			gamesCollection.add(row-1, g);
			gamesCollection.get(row).setIndex(row+1);
			dataBase = gamesCollection;
		}
		else if(o instanceof AudioCD) {
			AudioCD cd = (AudioCD) o;
			cd.setIndex(row);
			musicCollection.remove(row);
			musicCollection.add(row-1, cd);
			musicCollection.get(row).setIndex(row+1);
			dataBase = musicCollection;
		}
		else if(o instanceof Movie) {
			Movie m = (Movie) o;
			m.setIndex(row);
			moviesCollection.remove(row);
			moviesCollection.add(row-1, m);
			moviesCollection.get(row).setIndex(row+1);
			dataBase = moviesCollection;
		}
		isModelChanged = true;
		columnIndexAsc = columnIndexDesc = -1;
		fireTableRowsUpdated(row-1, row);
	}

	
	public void moveItemDown(int row) {
		
		Object o = dataBase.get(row);
		
		if(o instanceof Book) {
			Book b = (Book) o;
			b.setIndex(row+2);
			booksCollection.remove(row);
			booksCollection.add(row+1, b);
			booksCollection.get(row).setIndex(row+1);
			dataBase = booksCollection;
		}
		else if(o instanceof Game) {
			Game g = (Game) o;
			g.setIndex(row+2);
			gamesCollection.remove(row);
			gamesCollection.add(row+1, g);
			gamesCollection.get(row).setIndex(row+1);
			dataBase = gamesCollection;
		}
		else if(o instanceof AudioCD) {
			AudioCD cd = (AudioCD) o;
			cd.setIndex(row+2);
			musicCollection.remove(row);
			musicCollection.add(row+1, cd);
			musicCollection.get(row).setIndex(row+1);
			dataBase = musicCollection;
		}
		else if(o instanceof Movie) {
			Movie m = (Movie) o;
			m.setIndex(row+2);
			moviesCollection.remove(row);
			moviesCollection.add(row+1, m);
			moviesCollection.get(row).setIndex(row+1);
			dataBase = moviesCollection;
		}
		isModelChanged = true;
		columnIndexAsc = columnIndexDesc = -1;
		fireTableRowsUpdated(row, row+1);
	}
	
	
	public void sort(int columnIndex) {
		
		Collections.sort(dataBase, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				
				return ((String) getValueAt(dataBase.indexOf(o1), columnIndex)).compareToIgnoreCase
						(((String) getValueAt(dataBase.indexOf(o2), columnIndex)));
			}
		});
		initCollections();
		isModelChanged = true;
		columnIndexAsc = columnIndex;
		columnIndexDesc = -1;
		fireTableDataChanged();
	}
	
	
	public void toggleSort(int columnIndex) {
		
		Collections.sort(dataBase, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				
				return ((String) getValueAt(dataBase.indexOf(o2), columnIndex)).compareToIgnoreCase
						(((String) getValueAt(dataBase.indexOf(o1), columnIndex)));
			}
		});
		initCollections();
		isModelChanged = true;
		columnIndexDesc = columnIndex;
		columnIndexAsc = -1;
		fireTableDataChanged();
	}
	
	
	public static int getAscendingSortedColumnIndex() { return columnIndexAsc; }
	public static int getDescendingSortedColumnIndex() { return columnIndexDesc; }
	
	
	public void searchFor(String text) {
		
		String search = text.toLowerCase();
		if(!dataBase.isEmpty()) {
			Object o = dataBase.getFirst();
			
			if(o instanceof Book) {
				for(int row = getRowCount()-1; row>=0; row--) {
					Book b = booksCollection.get(row);
					String author = b.getAuthor().toLowerCase();
					String series = b.getSeries().toLowerCase();
					String title = b.getTitle().toLowerCase();
					String genre = b.getGenre().toLowerCase();
					String year = String.valueOf(b.getYear());
					if(!author.contains(search) && !series.contains(search) && !title.contains(search) && !genre.contains(search) 
							&& !year.contains(search)) {
						booksCollection.remove(row);
					}
				}
				dataBase = booksCollection;
			}
			if(o instanceof AudioCD) {
				for(int row = getRowCount()-1; row>=0; row--) {
					AudioCD cd = musicCollection.get(row);
					String artist = cd.getArtist().toLowerCase();
					String album = cd.getAlbumName().toLowerCase();
					String genre = cd.getGenre().toLowerCase();
					String year = String.valueOf(cd.getYear());
					if(!artist.contains(search) && !album.contains(search) && !genre.contains(search) && !year.contains(search)) {
						musicCollection.remove(row);
					}
				}
				dataBase = musicCollection;
			}
			if(o instanceof Game) {
				for(int row = getRowCount()-1; row>=0; row--) {
					Game g = gamesCollection.get(row);
					String title = g.getTitle().toLowerCase();
					String series = g.getSeries().toLowerCase();
					String genre = g.getGenre().toLowerCase();
					String studio = g.getStudio().toLowerCase();
					String year = String.valueOf(g.getYear());
					if(!title.contains(search) && !series.contains(search) && !genre.contains(search) &&
							!studio.contains(search) && !year.contains(search)) {
						gamesCollection.remove(row);
					}
				}
				dataBase = gamesCollection;
			}
			if(o instanceof Movie) {
				for(int row = getRowCount()-1; row>=0; row--) {
					Movie m = moviesCollection.get(row);
					String title = m.getTitle().toLowerCase();
					String series = m.getSeries().toLowerCase();
					String genre = m.getGenre().toLowerCase();
					String studio = m.getStudio().toLowerCase();
					String year = String.valueOf(m.getYear());
					if(!studio.contains(search) && !series.contains(search) && !title.contains(search) &&
							!genre.contains(search) && !year.contains(search)) {
						moviesCollection.remove(row);
					}
				}
				dataBase = moviesCollection;
			}
			isModelChanged = true;
			fireTableDataChanged();
		}
	}
	
	
	public void reloadTableModel() {
		
		dataBase = collection.getDataBase();
		initCollections();
		fireTableDataChanged();
	}
	
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Object o = dataBase.get(rowIndex);
		Object returnValue = null;
		
		switch(columnIndex) {
		case COLUMN_NO:
			if (o instanceof Book) returnValue = String.valueOf(((Book) o).getIndex());
			else if (o instanceof AudioCD) returnValue = String.valueOf(((AudioCD) o).getIndex());
			else if (o instanceof Movie) returnValue = String.valueOf(((Movie) o).getIndex());
			else if (o instanceof Game) returnValue = String.valueOf(((Game) o).getIndex());
			else return (String) o;
			break;
		case COLUMN_1:
			if (o instanceof Book) returnValue = ((Book) o).getAuthor();
			else if (o instanceof AudioCD) returnValue = ((AudioCD) o).getArtist();
			else if (o instanceof Movie) returnValue = ((Movie) o).getTitle();
			else if (o instanceof Game) returnValue = ((Game) o).getTitle();
			else return o;
			break;
		case COLUMN_2:
			if (o instanceof Book) returnValue = ((Book) o).getSeries();
			else if (o instanceof AudioCD) returnValue = ((AudioCD) o).getAlbumName();
			else if (o instanceof Movie) returnValue = ((Movie) o).getSeries();
			else if (o instanceof Game) returnValue = ((Game) o).getSeries();
			else return o;
			break;
		case COLUMN_3:
			if (o instanceof Book) returnValue = ((Book) o).getTitle();
			else if (o instanceof AudioCD) returnValue = ((AudioCD) o).getGenre();
			else if (o instanceof Movie) returnValue = ((Movie) o).getGenre();
			else if (o instanceof Game) returnValue = ((Game) o).getGenre();
			else return o;
			break;
		case COLUMN_4:
			if (o instanceof Book) returnValue = ((Book) o).getGenre();
			else if (o instanceof AudioCD) returnValue = String.valueOf(((AudioCD) o).getYear());
			else if (o instanceof Movie) returnValue = ((Movie) o).getStudio();
			else if (o instanceof Game) returnValue = ((Game) o).getStudio();
			else return o;
			break;
		case COLUMN_5:
			if (o instanceof Book) returnValue = String.valueOf(((Book) o).getYear());
			else if (o instanceof Movie) returnValue = String.valueOf(((Movie) o).getYear());
			else if (o instanceof Game) returnValue = String.valueOf(((Game) o).getYear());
			else return o;
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
			}
		
		return returnValue;
	}
	
	
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		
		Object o = dataBase.get(rowIndex);
		if(columnIndex == COLUMN_NO) {
			if (o instanceof Book) ((Book) o).setIndex((int) value);
			else if (o instanceof AudioCD) ((AudioCD) o).setIndex((int) value);
			else if (o instanceof Movie) ((Movie) o).setIndex((int) value);
			else if (o instanceof Game) ((Game) o).setIndex((int) value);
			else return;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void initCollections() {
		
		if(!dataBase.isEmpty()) {
			
			Object o = dataBase.getFirst();
			int indexCount = 1;
			
			if (o instanceof Book) {
				booksCollection = new LinkedList<>((LinkedList<Book>) dataBase);
				for(Book b: booksCollection) b.setIndex(indexCount++);
				dataBase = booksCollection;
			}
			else if (o instanceof AudioCD) {
				musicCollection = new LinkedList<>((LinkedList<AudioCD>) dataBase);
				for(AudioCD cd: musicCollection) cd.setIndex(indexCount++);
				dataBase = musicCollection;
			}
			else if (o instanceof Movie) {
				moviesCollection = new LinkedList<>((LinkedList<Movie>) dataBase);
				for(Movie m: moviesCollection) m.setIndex(indexCount++);
				dataBase = moviesCollection;
			}
			else if (o instanceof Game) {
				gamesCollection = new LinkedList<>((LinkedList<Game>) dataBase);
				for(Game g: gamesCollection) g.setIndex(indexCount++);
				dataBase = gamesCollection;
			}
			isModelChanged = false;
			columnIndexAsc = columnIndexDesc = -1;
		} 
	}
	
	
	public boolean isModelChanged() {
		
		return isModelChanged;
	}
	
	
	public void updateAndSaveCollection(){
		
		collection.saveCollection(dataBase);
		isModelChanged = false;
	}
	
}