package collectionsMain.collectableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Movie extends AbstractItem implements Comparable<Movie> {

	private static final long serialVersionUID = 1L;
	private String series;
	
	
	public Movie() {   }
	
	public Movie(String title, String series, String genre, String studio, int year) {
		super(studio, title, genre, year);
		this.series = series;
	}
	
	@Override
	public String getName() { return "Movie"; }

	@Override
	public String toString() { return "Title: "+title+"\nSeries: "+series+"\nGenre: "+genre+"\nStudio: "+author+"\nYear: "+year; }
	
	@Override
	public String getSeries() { return series; }
	
	@Override
	public void setSeries(String series) { this.series = series; }

	@Override
	public Game createItem(String[] data) {
		int year = Integer.parseInt(data[4]);
		return new Game(data[0], data[1], data[2], data[3], year);
	}
	
	@Override
	public void editItem(String[] newData) {
		if(!title.equals(newData[2])) setTitle(newData[0]);
		if(!series.equals(newData[1])) setSeries(newData[1]);
		if(!genre.equals(newData[3])) setGenre(newData[2]);
		if(!author.equals(newData[0])) setAuthor(newData[3]);
		int year = Integer.parseInt(newData[4]);
		if(this.year != year) setYear(year);
	}
	
	@Override
	public String[] getGenres() {
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
	public String[] getTableHeaders() { return new String[] {"#", "Title", "Series", "Genre", "Studio", "Year"}; }
	
	@Override
	public String[] getComboHeaders() {
		return new String[] {"--select Title--", "--select Series--", "--select Genre--", "--select Studio--", "-Year-"}; 
	}

	@Override
	public String getIconPath() { return "/icons/Movies.png"; }
	
	@Override
	public int compareTo(Movie movie) {
		if(series.equals(movie.series)) {
			if(year == movie.year) {
				return title.compareTo(movie.title);
			} else if (year > movie.year) return 1;
			else return -1;
		} else return series.compareTo(movie.series);
	}

	@Override
	public int[] getHierachyOfData() { 
		int[] nums = {4, 2, 1};
		return nums;
	}
}
