package music;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import collectionsMain.CollectionsApp;
import javazoom.jl.decoder.JavaLayerException;


public class PlayerListener implements ActionListener {
	
	private PlayerThread thread;
	private UpdateSlider us;
	private BufferedInputStream bis;
	private SliderMouseListener ml;
	private JButton play;
	private ImageIcon imgPlay, imgPause;
	private File musicFile;
	private int totalLength;
	private JSlider slider;
	private int progress;
	
	public PlayerListener(JFrame frame, File input, JSlider slider, JButton button) throws IOException, JavaLayerException {
		musicFile = input;
		this.slider = slider;
		play = button;
		bis = new BufferedInputStream(new FileInputStream(musicFile));
		progress = 0;
		totalLength = bis.available();
		this.slider.setMaximum(totalLength);
		this.slider.setValue(0);
		thread = new PlayerThread(musicFile, progress);
		us = new UpdateSlider();
		ml = new SliderMouseListener();
		imgPause = ((CollectionsApp) frame).createImageIcon("icons/pause_16.png", "pause");
		imgPlay = (ImageIcon) button.getIcon();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("play")) {
			if(thread.getState()==SwingWorker.StateValue.PENDING) {
				startPlayerThreads();
				slider.addMouseListener(ml);
				play.setIcon(imgPause);
			} 
			else if(thread.getState()==SwingWorker.StateValue.STARTED) {
				us.stopSlider();
				progress = thread.pauseAndGetProgress();
				thread.closePlayer();
				slider.removeMouseListener(ml);
				play.setIcon(imgPlay);
				createPlayerThreads();
				
			}
			else if(thread.getState()==SwingWorker.StateValue.DONE) {
				progress = 0;
				createPlayerThreads();
				startPlayerThreads();
				play.setIcon(imgPause);
			}
		}
		
		else if(e.getActionCommand().equals("stop")) {
			killPlayerThreads();
			progress = 0;
			createPlayerThreads();
			play.setIcon(imgPlay);
		}
	}
	
	private void createPlayerThreads() {
		if(progress == 0) slider.setValue(0);
		else slider.setValue(totalLength - progress);
		try {
			thread = new PlayerThread(musicFile, progress);
			us = new UpdateSlider();
		} catch (IOException | JavaLayerException e1) {	e1.printStackTrace(); }
	}
	
	private void startPlayerThreads() {
		thread.execute();
		us.execute();	
	}
	
	protected void killPlayerThreads() {
		slider.removeMouseListener(ml);
		us.stopSlider();
		thread.closePlayer();
	}
	
	
	class UpdateSlider extends SwingWorker <Void, Void> {
		
		boolean running;
		
		@Override
		protected Void doInBackground() throws Exception {
			running = true;
			while(running) {
				try {
					if(thread.getPlayerProgress() != 0)	slider.setValue(totalLength - thread.getPlayerProgress());
					else {
						slider.setValue(0);
						play.setIcon(imgPlay);
						running = false;
					}
				} catch (IOException e) { e.printStackTrace(); }
			}
			return null;
		}
		
		public void stopSlider() {
			running = false;
		}
		
	}
	
	
	class SliderMouseListener extends MouseAdapter {
		
		@Override
		public void mousePressed (MouseEvent e) {
			us.stopSlider();
			thread.closePlayer();
			play.setIcon(imgPlay);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			
			progress = totalLength - slider.getValue();
			createPlayerThreads();
			startPlayerThreads();
			play.setIcon(imgPause);
		}
	}

}
