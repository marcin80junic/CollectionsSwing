package collectableItems;

public interface Collectable<T> {
	
	String getName();
	String getAuthor();
	String getTitle();
	String getSeries();
	String getGenre();
	int[] getDiscs();
	int getYear();
	int getID();
	
	T createItem(String[] data);
	void editItem(String[] newData);
	void setAuthor(String author);
	void setTitle(String title);
	void setSeries(String series);
	void setGenre(String genre);
	void setDiscs(int[] discs);
	void setYear(int year);
	void setID(int id);
	
	String[] getGenres();
	String[] getTableHeaders();
	String[] getComboHeaders();
	String toString();
}
