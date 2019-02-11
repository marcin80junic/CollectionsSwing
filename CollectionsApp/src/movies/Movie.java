package movies;

import item.Item;

public class Movie extends Item implements Comparable<Movie> {

	private static final long serialVersionUID = 1L;
	private String series, title, studio;
	
	public Movie(String title, String series, String genre, String studio, int year) {
		
		super(genre, year);
		this.title = title;
		this.series = series;
		this.studio = studio;
	}
	
	String printMovie(){
		
		return "Title: "+title+"\n"+ "Series: "+series+"\n"+ "Genre: "+genre+"\n"+ "Studio: "+studio+"\n"+ "Year: "+year;
	}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	public String getSeries() {return series;}
	public void setSeries(String series) {this.series = series;}
	
	public String getStudio() {return studio;}
	public void setStudio(String studio) {this.studio = studio;}

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
