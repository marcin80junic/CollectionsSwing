package collectableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AudioCD extends AbstractItem implements Comparable<AudioCD> {
	
	private int[] albumCds;
	
	
	public AudioCD (String artist, String albumName, String genre, int year, int[] albumCds) {
		super(artist, albumName, genre, year);
		this.albumCds = albumCds;
	}
	
	@Override
	public String getName() { return "Audio CD"; }
	
	@Override
	public String toString() { return "Artist: "+author+"\nAlbum: "+title+"\nGenre: "+genre+"\nYear: "+year; }
	
	@Override
	public int[] getDiscs() { return albumCds; }
	
	@Override
	public void setDiscs(int[] albumCds) { this.albumCds = albumCds; }
	
	@Override
	public String[] getGenres() {
		ArrayList<String> genresArray = new ArrayList<>(Arrays.asList("Electronic dance", "Musical theatre", "Jazz", "Rock", "Pop", "Folk", 
				"Classical", "Blues", "Hip hop", "Country",	"Reggae", "Heavy metal", "Singing", "Punk rock", "Funk", "Alternative rock",
				"Disco", "House", "Opera", "Techno", "Soul", "Dance", "Instrumental", "Dubstep", "Rapping", "Drum and bass", "Ambient",
				"Trance", "Indie rock", "Orchestra", "Grunge", "Vocal", "Progressive rock", "Industrial", "Pop rock", "Black metal", "New wave", "Ska", "Hardcore punk", "Soundtrack",
				"Death metal", "Rock and roll", "Swing", "Electro", "Bluegrass", "Gospel", "Folk rock", "Alternative metal"));
		Collections.sort(genresArray);
		String[] genres = new String[genresArray.size()];
		genresArray.toArray(genres);
		return genres;
	}

	@Override
	public String[] getTableHeaders() { return new String[] {"#", "Artist", "Album", "CDs", "Genre", "Year"}; }
	
	@Override
	public int compareTo(AudioCD cd) {
		if (author.equals(cd.author)) {
			if(year == cd.year) return title.compareTo(cd.title);
			else if(year > cd.year) return 1;
			return -1;
		}
		return author.compareTo(cd.author);
	}

}
