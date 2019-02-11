package games;

import item.Item;

public class Game extends Item implements Comparable<Game> {
	
	private static final long serialVersionUID = 1L;
	private String title;
	private String series;
	private String studio;
	
	public Game (String title, String series, String genre, String studio, int year) {
		
		super(genre, year);
		this.title = title;
		this.series = series;
		this.studio = studio;
	}
	
	public String printGame() {
		
		return "Title: "+title+ "\nSeries: "+series+ "\nGenre: "+genre+ "\nStudio: "+studio+ "\nYear: "+year;
	}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	public String getSeries() {return series;}
	public void setSeries(String series) {this.series = series;}
	
	public String getStudio() {return studio;}
	public void setStudio(String studio) {this.studio = studio;}

	@Override
	public int compareTo(Game game) {
		
		if(title.equals(game.title)) {
			if(genre.equals(game.genre)) {
				if(studio.equals(game.studio)) {
					if(year == game.year) return 0;
					else if(year > game.year) return 1;
					else return -1;
				} else return studio.compareTo(game.studio);
			} else return genre.compareTo(game.genre);
		} else return title.compareTo(game.title);
	}

}
