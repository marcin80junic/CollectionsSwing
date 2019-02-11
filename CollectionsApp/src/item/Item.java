package item;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String genre;
	protected int year;
	protected int index;
	
	public Item(String genre, int year) {
		this.genre = genre;
		this.year = year;
	}
	
	public String getGenre() {return genre;}
	public void setGenre(String genre) {this.genre = genre;}
	
	public int getYear() {return year;}
	public void setYear(int year) {this.year = year;}
	
	public int getIndex() {return index;}
	public void setIndex(int index) {this.index = index;}

}
