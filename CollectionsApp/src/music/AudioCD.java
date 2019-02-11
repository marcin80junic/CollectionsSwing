package music;

import item.Item;


public class AudioCD extends Item implements Comparable<AudioCD> {
	
	
	private static final long serialVersionUID = 1L;
	private String artist;
	private String albumName;
	private int[] albumCds;
	
	
	public AudioCD(String artist, String albumName, String genre, int year, int[] albumCds) {
		
		super(genre, year);
		this.artist = artist;
		this.albumName = albumName;
		this.albumCds = albumCds;
	}
	
	public String printAudioCD() {
		
		return "Artist: "+artist+"\n"+ "Album: "+albumName+"\n"+ "Genre: "+genre+
				"\n"+ "Year: "+year;
	}
	
	public String getArtist() { return artist; }
	public void setArtist(String artist) { this.artist = artist; }
	
	public String getAlbumName() { return albumName; }
	public void setAlbumName(String albumName) { this.albumName = albumName; }
	
	public int[] getAlbumCds() { return albumCds; }
	public void setAlbumCds(int[] albumCds) { this.albumCds = albumCds; }
	
	@Override
	public int compareTo(AudioCD cd) {
		if (artist.equals(cd.artist)) {
			if(year == cd.year) {
				return albumName.compareTo(cd.albumName);
			}
			if(year > cd.year) return 1;
		}
		return artist.compareTo(cd.artist);
	}
	
}
