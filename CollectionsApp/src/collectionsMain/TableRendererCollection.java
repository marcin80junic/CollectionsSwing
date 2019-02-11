package collectionsMain;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;


public class TableRendererCollection extends JTextField implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private CollectionsApp app;
	private String s;
	
	
	public TableRendererCollection(CollectionsApp application) {
		
		super();
		app = application;
		setBorder(null);
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		if(value instanceof String) {
			s = (String) value;
			setText(s);
		}
		if(column == 0) setHorizontalAlignment(RIGHT);
		else setHorizontalAlignment(CENTER);
		
		if(isSelected) {
			setBackground(app.getHighlightBackground());
			setForeground(app.getHighlightForeground());
			setFont(app.getHighlightFont());
		}
		else {
			setBackground(app.getTableBackground());
			setForeground(app.getTableForeground());
			setFont(app.getTableFont());
		}
		
		String search = app.getSearchText();
		if(!search.equals("")) {
			setHighlight(search);
		}
		return this;
	}
	
	
	private void setHighlight(String search) {
		
		if(s.toLowerCase().contains(search.toLowerCase())) {
			int indexOf = s.toLowerCase().indexOf(search.toLowerCase());
			int searchLength = search.length();
			try {
				getHighlighter().addHighlight(indexOf, indexOf+searchLength, 
						new DefaultHighlighter.DefaultHighlightPainter(app.getHighlightBackground()));
			} catch (BadLocationException e) { e.printStackTrace(); }
			
		} else {
			getHighlighter().removeAllHighlights();
		}
	}
	
	
}