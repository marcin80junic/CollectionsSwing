package collectableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Book extends AbstractItem implements Comparable<Book> {

	private String series;

	
	public Book(String author, String series, String title, String genre, int year) {
		super(author, title, genre, year);
		this.series = series;
	}
	
	@Override
	public String getName() { return "Book"; }

	@Override
	public String toString() { return "Author: "+author+"\nSeries: "+series+"\nTitle: "+title+"\nGenre: "+genre+"\nYear: "+year; }

	@Override
	public String getSeries() { return series; }
	
	@Override
	public void setSeries(String series) { this.series = series; }
	
	@Override
	public String[] getGenres() {
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
	public String[] getTableHeaders() {	return new String[] {"#", "Author", "Title", "Series", "Genre", "Year"}; }
	
	@Override
	public int compareTo(Book book) {
		if (author.equals(book.author)) {
			if (series.equals(book.series)) {
				if (year == book.year)
					return title.compareTo(book.title);
				else if (year > book.year) return 1;
				else return -1;
			} else return series.compareTo(book.series);
		} else return author.compareTo(book.author);
	}

}