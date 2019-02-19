package collectableItems;

public interface Collectable {
	
	String getName();
	
	String getAuthor();
	String getTitle();
	String getSeries();
	String getGenre();
	int[] getDiscs();
	int getYear();
	int getID();
	
	void setAuthor(String author);
	void setTitle(String title);
	void setSeries(String series);
	void setDiscs(int[] discs);
	void setYear(int year);
	void setID(int id);
	
	String[] getGenres();
	String[] getTableHeaders();
	String toString();
}
