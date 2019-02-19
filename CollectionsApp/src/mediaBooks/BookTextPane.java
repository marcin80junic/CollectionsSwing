package mediaBooks;


import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class BookTextPane extends JTextPane {

	
	private static final long serialVersionUID = 7820203299581007071L;
	private SimpleAttributeSet author, series, title, centered;
	
	
	public BookTextPane() {
		
		initAttributes();
		
	}
	
	
	private void initAttributes() {

		author = new SimpleAttributeSet();
		StyleConstants.setFontFamily(author, "serif");
		StyleConstants.setFontSize(author, 20);
		StyleConstants.setBold(author, true);

		series = new SimpleAttributeSet();
		StyleConstants.setFontFamily(series, "serif");
		StyleConstants.setFontSize(series, 25);
		StyleConstants.setBold(series, true);
		StyleConstants.setItalic(series, true);

		title = new SimpleAttributeSet();
		StyleConstants.setFontFamily(title, "serif");
		StyleConstants.setFontSize(title, 30);
		StyleConstants.setBold(title, true);

		centered = new SimpleAttributeSet();
		StyleConstants.setAlignment(centered, StyleConstants.ALIGN_CENTER);
	}
	
	SimpleAttributeSet getAuthorAttributes() { return author; }
	SimpleAttributeSet getSeriesAttributes() { return series; }
	SimpleAttributeSet getTitleAttributes() { return title; }
	SimpleAttributeSet getCenteredAttributes () { return centered; }

}
