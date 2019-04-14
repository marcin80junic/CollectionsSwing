package mediaBooks;

import javax.swing.JTable;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import collectionsMain.DataBase;
import collectionsMain.ImageLoader;
import collectionsMain.collectableItems.Collectable;
import collectionsMain.table.TableModelCollection;


public class BookStyledDocument extends DefaultStyledDocument {

	
	private static final long serialVersionUID = 1L;
	private SimpleAttributeSet author, series, title, centered;
	private Collectable<?> item;
	private ImageLoader il;
	
	public BookStyledDocument(DataBase<?> db, JTable table, int width){
		TableModelCollection<?> model = (TableModelCollection<?>) table.getModel();
		item = model.getItem(table.getSelectedRow());
		il = new ImageLoader(db, item, 150, 150, width);
		initAttributes();
		initText();
	}
	
	private void initAttributes() {

		author = new SimpleAttributeSet();
		StyleConstants.setFontFamily(author, "serif");
		StyleConstants.setFontSize(author, 25);
		StyleConstants.setBold(author, true);

		series = new SimpleAttributeSet();
		StyleConstants.setFontFamily(series, "serif");
		StyleConstants.setFontSize(series, 25);
		StyleConstants.setBold(series, false);
		StyleConstants.setItalic(series, true);

		title = new SimpleAttributeSet();
		StyleConstants.setFontFamily(title, "serif");
		StyleConstants.setFontSize(title, 25);
		StyleConstants.setBold(title, true);
		StyleConstants.setItalic(title, true);

		centered = new SimpleAttributeSet();
		StyleConstants.setAlignment(centered, StyleConstants.ALIGN_CENTER);
	}
	
	SimpleAttributeSet getAuthorAttributes() { return author; }
	SimpleAttributeSet getSeriesAttributes() { return series; }
	SimpleAttributeSet getTitleAttributes() { return title; }
	SimpleAttributeSet getCenteredAttributes () { return centered; }
	
	
	private void initText() {
		try {
			insertString(0, item.getAuthor()+"\n", author);
			insertString(getLength(), item.getTitle()+"\n", title);
			insertString(getLength(), item.getSeries()+"\n", series);

			Style style = addStyle("StyleName", null);
			StyleConstants.setComponent(style, il);
			insertString(getLength(), "s", style);

			setParagraphAttributes(0, getLength(), centered, false);

		} catch (BadLocationException e) {
			e.printStackTrace(); 
		}
	}

}
