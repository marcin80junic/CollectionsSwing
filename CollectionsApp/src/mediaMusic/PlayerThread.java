package mediaMusic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.SwingWorker;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

class PlayerThread extends SwingWorker <Void, Void> {
	
	private BufferedInputStream bis;
	private AdvancedPlayer player;
	private File musicFile;
	private int totalLength;
	private int progress;
	private PlayersListener pl;
	
	public PlayerThread(File input, int progress) throws IOException, JavaLayerException {
		musicFile = input;
		bis = new BufferedInputStream(new FileInputStream(musicFile));
		this.progress = progress;
		totalLength = bis.available();
		pl = new PlayersListener();
		player = new AdvancedPlayer(bis);
		player.setPlayBackListener(pl);
	}
	
	@Override
	protected Void doInBackground() throws Exception {	
		
		if(progress != 0) {
			bis.skip(totalLength - progress);
			player = new AdvancedPlayer(bis);
			player.setPlayBackListener(pl);
		}
		player.play();
		return null;
	}
	
	public int pauseAndGetProgress() {
		player.stop();
		return progress;
	}
	
	public int getPlayerProgress() throws IOException {
		if(bis.equals(null)) throw new IOException();
		try {
			progress = bis.available();
		} catch (IOException e) { e.printStackTrace(); }
		return progress;
	}
	
	public void closePlayer() {
		if(player != null) {
			player.close();
		}
	}
	
	
	class PlayersListener extends PlaybackListener{
		
		public void playbackStarted(PlaybackEvent evt){
			
		}
		public void playbackFinished(PlaybackEvent evt) {
			try {
				progress = bis.available();
			} catch (IOException e) { closePlayer(); }
		}
	}
	
}

