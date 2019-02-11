package music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import collectionsMain.CollectionsApp;
import javazoom.jl.decoder.JavaLayerException;

public class MusicPanel {
	
	private JPanel playerPanel;
	private PlayerListener pl;
	
	public MusicPanel() {
		
		playerPanel = new JPanel();
		JLabel musicLabel = new JLabel("Audio CD Detail Page");
		musicLabel.setFont(musicLabel.getFont().deriveFont(36f));
		musicLabel.setAlignmentX(0.5f);
		playerPanel.add(musicLabel);
	}
	
	public MusicPanel(JFrame frame, int numOfTracks) {
		
		ImageIcon imgPlay = ((CollectionsApp) frame).createImageIcon("icons/play_16.png", "play");
		ImageIcon imgStop = ((CollectionsApp) frame).createImageIcon("icons/stop_16.png", "stop");
		ImageIcon imgOpen = ((CollectionsApp) frame).createImageIcon("icons/open_16.png", "open");
		
		playerPanel = new JPanel();
		playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.PAGE_AXIS));
		playerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		for(int i=0 ; i<numOfTracks; i++) {
			final int a = i+1;
			JPanel player = new JPanel();
			JPanel top = new JPanel();
			JPanel bottom = new JPanel();
			player.setPreferredSize(new Dimension(300, 35));
			JSlider slider = new JSlider();
			JLabel title = new JLabel("Track "+(a)+": ");
			JButton play = new JButton(imgPlay);
			JButton stop = new JButton(imgStop);
			JButton open = new JButton(imgOpen);
			slider.setValue(0);
			title.setPreferredSize(new Dimension(200, 23));
			play.setActionCommand("play");
			stop.setActionCommand("stop");
			open.setActionCommand("open");
			play.setPreferredSize(new Dimension(imgPlay.getIconWidth(), imgPlay.getIconHeight()));
			stop.setPreferredSize(new Dimension(imgStop.getIconWidth(), imgStop.getIconHeight()));
			open.setPreferredSize(new Dimension(imgOpen.getIconWidth(), imgOpen.getIconHeight()));
			play.setEnabled(false);
			stop.setEnabled(false);
			open.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					JFileChooser open = new JFileChooser();
					open.setCurrentDirectory(new File("D:\\Muzyka"));
					String[] audios = {"mp3", "wav"};
					open.setFileFilter(new MusicFileFilter(audios));
					int action = open.showOpenDialog((CollectionsApp) frame);
					if(action==JFileChooser.APPROVE_OPTION) {
						if(play.isEnabled()) {
							pl.killPlayerThreads();
							play.removeActionListener(pl);
							stop.removeActionListener(pl);
						}
						File musicFile = open.getSelectedFile();
						try {
							pl = new PlayerListener(frame, musicFile, slider, play);
							play.addActionListener(pl);
							stop.addActionListener(pl);
							play.setEnabled(true);
							stop.setEnabled(true);
						} catch (IOException | JavaLayerException e1) { e1.printStackTrace(); }
						title.setText("Track "+a+": "+musicFile.getName().substring(0, musicFile.getName().indexOf('.')));
					}
				}
			});
			top.add(title);
			bottom.add(slider);
			bottom.add(play);
			bottom.add(stop);
			bottom.add(open);
			player.add(top);
			player.add(bottom);
			playerPanel.add(player);
		}
	}
	
	public JPanel getMusicPanel() {
		
		return playerPanel;
	}

}
