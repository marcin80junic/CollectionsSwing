package collectableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game extends AbstractItem implements Comparable<Game> {
	
	private String series;
	
	public Game (String title, String series, String genre, String author, int year) {
		super(author, title, genre, year);
		this.series = series;
	}
	
	@Override
	public String getName() { return "Game"; }
	
	@Override
	public String toString() { return "Title: "+title+"\nSeries: "+series+"\nGenre: "+genre+"\nStudio: "+author+"\nYear: "+year; }
	
	@Override
	public String getSeries() {return series;}
	
	@Override
	public void setSeries(String series) {this.series = series;}

	@Override
	public String[] getGenres() {
		ArrayList<String> genresArray = new ArrayList<>(Arrays.asList("Action", "RPG"));
		Collections.sort(genresArray);
		String[] genres = new String[genresArray.size()];
		genresArray.toArray(genres);
		return genres;
	}

	@Override
	public String[] getTableHeaders() { return new String[] {"#", "Title", "Series", "Genre", "Studio", "Year"}; }
	
	@Override
	public int compareTo(Game game) {
		if(title.equals(game.title)) {
			if(genre.equals(game.genre)) {
				if(author.equals(game.author)) {
					if(year == game.year) return 0;
					else if(year > game.year) return 1;
					else return -1;
				} else return author.compareTo(game.author);
			} else return genre.compareTo(game.genre);
		} else return title.compareTo(game.title);
	}
	
}
