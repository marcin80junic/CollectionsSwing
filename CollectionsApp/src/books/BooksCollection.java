package books;

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

public class BooksCollection implements Collection<Book> {
	
	private File booksFile;
	private LinkedList<Book> mainDB;
	private TableModelCollection booksTableModel;
	private TableColumnModel booksColumnModel;
	private String columns[] = { "#", "Author", "Series", "Title", "Genre", "Year" };
	private boolean statusChanged, isFiltered;
	private boolean[] comboFlags = new boolean[5];

	
	public BooksCollection() {
		booksFile = new File("/users/marcin/eclipse-workspace/collectionsapp/books.dat");
		try {
			generateCollection();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		booksTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}

	public BooksCollection(File file) throws IOException {
		booksFile = new File(file.getAbsolutePath());
		generateCollection();
		booksTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = isFiltered = false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void generateCollection() throws IOException {
		if (booksFile.isFile()) {
			try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream(booksFile))) {
				mainDB = new LinkedList<>((LinkedList<Book>) oin.readObject());
			} catch (IOException | ClassNotFoundException exc) {
				throw new IOException();
			}
		} else {
			mainDB = new LinkedList<>();
			saveCollection();
			generateCollection();
		}
	}
	
	@Override
	public BooksCollection loadCollection(File file) {
		try {
			BooksCollection bc = new BooksCollection(file);
			if (!bc.mainDB.isEmpty() && (bc.mainDB.getFirst() instanceof Book))
				return bc;
		} catch (IOException | NullPointerException e) {
			return null;
		}
		return null;
	}

	@Override
	public void reload() {
		try {
			generateCollection();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		booksTableModel = new TableModelCollection(this, mainDB, columns);
		statusChanged = false;
	}
	
	@Override
	public void saveCollection() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(booksFile, false))) {
			out.writeObject(mainDB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		statusChanged = false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void saveCollection(LinkedList<?> dataBase) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(booksFile, false))) {
			mainDB = new LinkedList<>((LinkedList<Book>) dataBase);
			out.writeObject(mainDB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		statusChanged = false;
	}
	
	public static boolean isValidDatabase(File file) throws IOException {
		try {
			BooksCollection bc = new BooksCollection(file);
			if (!bc.mainDB.isEmpty() && (bc.mainDB.getFirst() instanceof Book)) {
				return true;
			}
		} catch (IOException | NullPointerException e) {
			throw new IOException();
		}
		return false;
	}
	
	@Override
	public void exportTo(File file) {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, false))) {
			out.writeObject(mainDB);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addNewItem(String... s) {
		String author = s[0];
		String series = s[1];
		String title = s[2];
		String genre = s[3];
		int year = Integer.parseInt(s[4]);
		Book book = new Book(author, series, title, genre, year);
		book.setIndex(mainDB.size() + 1);
		mainDB.add(book);
		statusChanged = true;
	}

	@Override
	public void editItem(int index, String... s) {
		String author = s[0];
		String series = s[1];
		String title = s[2];
		String genre = s[3];
		int year = Integer.parseInt(s[4]);
		Book book = mainDB.get(index);
		book.setAuthor(author);
		book.setSeries(series);
		book.setTitle(title);
		book.setGenre(genre);
		book.setYear(year);
		statusChanged = true;
	}
	
	@Override
	public boolean isStatusChanged() { return (booksTableModel.isModelChanged() || statusChanged) && !isFiltered; }

	@Override
	public void setFilteredStatus(boolean status) { isFiltered = status; }
	
	@Override
	public LinkedList<Book> getDataBase() {	return new LinkedList<>(mainDB); }
	
	@Override
	public int sizeOfDB() { return mainDB.size(); }
	
	@Override
	public ImageIcon getIcon() { return createImageIcon("/icons/Books.png", "books"); }
	
	@Override
	public File getDBFile() { return booksFile; }
	
	@Override
	public String getItemName() { return "Book"; }

	public static String getName() { return "Book"; }

	@Override
	public Book getItem(int index) { return mainDB.get(index); }

	@Override
	public String printItem(Object item) {
		Book book = (Book) item;
		return book.printBook();
	}

	@Override
	public String[] getGenreList() {
		ArrayList<String> genresArray = new ArrayList<>(Arrays.asList("Fiction", "Novel", "Non-fiction",
				"Science fiction", "Genre fiction", "Fantasy", "Historical fiction", "Mystery", "Young adult fiction",
				"Horror fiction", "Children's literature", "Romance novel", "Thriller", "Autobiography",
				"Literary fiction", "Memoir", "Fairy tale", "Crime fiction", "Graphic novel", "Comic book", "Review",
				"Science fantasy", "Picture book", "Textbook", "Book review", "Magical realism", "Literary cookbook",
				"Paranormal romanse", "Self-help book", "Chick lit", "Urban fantasy", "Gothic fiction",
				"Literary criticism", "Dictionary", "Detective fiction", "Erotic literarure", "High fantasy",
				"Alternate history", "Anthology", "Encyclopedia", "Domestic fiction", "Allegory", "Spy fiction",
				"Hard science fiction", "Space opera", "Short story collection", "Programming languages", "Suspense",
				"Coming-of-age fiction"));
		Collections.sort(genresArray);
		String[] genres = new String[genresArray.size()];
		genresArray.toArray(genres);
		return genres;
	}

	@Override
	public TableModelCollection getTableModel() { return booksTableModel; }

	@Override
	public TableColumnModel getTableColumnModel(JTable table) {
		if (booksColumnModel == null) {
			booksColumnModel = new DefaultTableColumnModel();
			for (int i = 0; i < table.getColumnCount(); i++) {
				booksColumnModel.addColumn(table.getColumnModel().getColumn(i));
			}
		}
		return booksColumnModel;
	}

	@Override
	public void setColumnSize(JTable table) {

		TableColumn column;
		for (int i = 0; i < table.getModel().getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			if (i == 0) {
				int rowNumber = table.getModel().getRowCount();
				if (rowNumber < 10) {
					column.setMinWidth(15);
					column.setMaxWidth(15);
				} else if (rowNumber > 9 && rowNumber < 100) {
					column.setMinWidth(20);
					column.setMaxWidth(20);
				} else if (rowNumber > 99 && rowNumber < 1000) {
					column.setMinWidth(25);
					column.setMaxWidth(25);
				} else if (rowNumber > 999 && rowNumber < 10000) {
					column.setMinWidth(30);
					column.setMaxWidth(30);
				} else {
					column.setMinWidth(35);
					column.setMaxWidth(35);
				}
			} else if (i == 1) {
				column.setMinWidth(200);
				column.setMaxWidth(200);
			} else if (i == 2) {
				column.setMinWidth(150);
				column.setMaxWidth(150);
			} else if (i == 3) {
				column.setMinWidth(260);
				column.setMaxWidth(260);
			} else if (i == 4) {
				column.setMinWidth(100);
				column.setMaxWidth(100);
			} else if (i == 5) {
				column.setMinWidth(75);
				column.setMaxWidth(75);
			}
		}
	}

	@Override
	public String[] getComboHeaders() {
		String[] headers = { "--Select Author--", "--Select Series--", "--Select Title--", "--Select Genre--",
				"-Year-" };
		return headers;
	}

	@Override
	public Iterator<Book> iterator() { return mainDB.iterator(); }

	@Override
	public void setComboFlags(boolean[] flags) {
		for (int i = 0; i < flags.length; i++) {
			comboFlags[i] = flags[i];
		}
	}

	@Override
	public boolean[] getComboFlags() { return comboFlags; }


}