package collectableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Movie extends AbstractItem implements Comparable<Movie> {

	private String series;
	
	
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
	public int compareTo(Movie movie) {
		if(series.equals(movie.series)) {
			if(year == movie.year) {
				return title.compareTo(movie.title);
			} else if (year > movie.year) return 1;
			else return -1;
		} else return series.compareTo(movie.series);
	}

}
