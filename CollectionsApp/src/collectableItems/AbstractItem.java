package collectableItems;


public abstract class AbstractItem implements Collectable {

	protected String author;
	protected String title;
	protected String genre;
	protected int year;
	protected int index;
	
	protected AbstractItem(String author, String title, String genre, int year) {
		this.author = author;
		this.title = title;
		this.genre = genre;
		this.year = year;
	}
	
	@Override
	public String getAuthor() { return author; }

	@Override
	public String getTitle() { return title; }

	@Override
	public String getSeries() {	return null; }
	
	@Override
	public String getGenre() { return genre; }

	@Override
	public int[] getDiscs() { return null; }

	@Override
	public int getYear() { return year; }

	@Override
	public int getID() { return year; }

	@Override
	public void setAuthor(String author) { this.author = author; }

	@Override
	public void setTitle(String title) { this.title = title; }

	@Override
	public void setSeries(String series) { 	}

	@Override
	public void setDiscs(int[] discs) {  }

	@Override
	public void setYear(int year) { this.year = year; }

	@Override
	public void setID(int index) { this.index = index; }	

	
}
