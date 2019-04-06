package collectionsMain;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MiniColorChooser extends JPanel {

	private static final long serialVersionUID = 1L;
	int row;
	
	public MiniColorChooser() {

		Color[] colors = {Color.WHITE, Color.YELLOW,Color.ORANGE, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, Color.GREEN, Color.RED,
				Color.BLUE, Color.CYAN, Color.MAGENTA, Color.PINK, Color.BLACK, Color.decode("#00ff88ff"), Color.decode("#008ff88"),
				 Color.decode("#00ff8888")};
		int row = (int) Math.sqrt(colors.length);
		setLayout(new GridLayout(row, row));
		JPanel color;
		for(int i=0; i<row; i++) {
			for(int j=0; j<row; j++) {
				color = new JPanel();
				color.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				color.setPreferredSize(new Dimension(10,10));
				color.setBackground(colors[i*row+j]);
				add(color, i, j);
			}
		}
	}
	
	
	
/*	private String intToColorHexString(int num) {
		String hex = Integer.toHexString(num & 0xffffff);
	    while(hex.length() < 6) {
	    	hex = "0" + hex;
	    }
	    return hex = "#" + hex;
	}*/

}
