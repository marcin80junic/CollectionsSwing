package collectableItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AudioCD extends AbstractItem implements Comparable<AudioCD> {
	
	private static final long serialVersionUID = 1L;
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
	public AudioCD createItem(String[] data) {
		int year = Integer.parseInt(data[3]);
		return new AudioCD(data[0], data[1], data[2], year, parseCDs(data));
	}
	
	@Override
	public void editItem(String[] newData) {
		if(!author.equals(newData[0])) setAuthor(newData[0]);
		if(!title.equals(newData[1])) setTitle(newData[2]);
		if(!genre.equals(newData[2])) setGenre(newData[3]);
		int year = Integer.parseInt(newData[3]);
		if(this.year != year) setYear(year);
		int[] cds = parseCDs(newData);
		if(cds != albumCds) setDiscs(cds);
	}
	
	private int[] parseCDs(String[] data) {
		int[] cds = new int[data.length - 4];
		for(int i=4; i<data.length; i++) {
			cds[i-4] = Integer.parseInt(data[i]);
		}
		return cds;
	}
	
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
	public String[] getComboHeaders() {
		return new String[] {"--select Artist--", "--select Album--", "-CDs-", "--select Genre--", "-Year-"}; 
	}
	
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
