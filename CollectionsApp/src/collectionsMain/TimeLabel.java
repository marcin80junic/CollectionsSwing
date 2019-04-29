package collectionsMain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;

public class TimeLabel extends JLabel implements Runnable {

	private static final long serialVersionUID = 1L;
	Thread t;
	String formatter;
	
	public TimeLabel() {
		super("Initializing...", JLabel.CENTER);
		setBackground(CollectionsApp.getMainBackground().darker());
		formatter = "dd MMM yyyy',' HH':'mm':'ss";
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while(true) {
			LocalDateTime time = LocalDateTime.now();
			setText(time.format(DateTimeFormatter.ofPattern(formatter)));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				setText("Time Display Error");
			}
		}
	}



}
