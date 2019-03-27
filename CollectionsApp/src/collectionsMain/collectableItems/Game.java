package collectionsMain.collectableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Game extends AbstractItem implements Comparable<Game> {
	
	private static final long serialVersionUID = 1L;
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
		ArrayList<String> genresArray = new ArrayList<>(Arrays.asList("Action", "RPG"));
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
	public String getIconPath() { return "/icons/Games.png"; }
	
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
	
	@Override
	public int[] getHierachyOfData() { 
		int[] nums = {4, 2, 1};
		return nums;
	}
	
}
