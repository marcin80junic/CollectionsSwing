package books;


import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class BookStyledDocument extends DefaultStyledDocument {

	
	private static final long serialVersionUID = 1L;
	private SimpleAttributeSet author, series, title, centered;
	private final Book book;
	
	
	public BookStyledDocument(Book book){
		
		this.book = book;
		initAttributes();
		initText();
	
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
	
	
	private void initText() {
		
		String text = "";
		try {
			insertString(0, book.getAuthor()+"\n", author);
			insertString(getLength(), book.getSeries()+"\n", series);
			insertString(getLength(), book.getTitle()+"\n", title);
			text = getText(0, getLength());
		} catch (BadLocationException e) { e.printStackTrace(); }
		
		for(int i=0; i<getLength(); i++) {
			if(text.charAt(i) == '\n') {
				setParagraphAttributes(i, 0, centered, false);
			}
		}
	}

}
