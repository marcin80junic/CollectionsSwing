package books;

import item.Item;

public class Book extends Item implements Comparable<Book> {

	private static final long serialVersionUID = 1L;
	private String author;
	private String series;
	private String title;

	public Book(String author, String series, String title, String genre, int year) {

		super(genre, year);
		this.author = author;
		this.series = series;
		this.title = title;
	}

	public String printBook() {

		return "Author: " + author + "\n" + "Series: " + series + "\n" + "Title: " + title + "\n" + "Year: " + year
				+ "\n" + "Genre: " + genre;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int compareTo(Book book) {

		if (author.equals(book.author)) {
			if (series.equals(book.series)) {
				if (year == book.year)
					return title.compareTo(book.title);
				else if (year > book.year)
					return 1;
				else
					return -1;
			} else
				return series.compareTo(book.series);
		} else
			return author.compareTo(book.author);
	}

}