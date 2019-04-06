package collectionsMain.table;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import collectionsMain.CollectionsApp;

public class TableRendererCollection extends JTextField implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private String s;
	
	
	public TableRendererCollection() {
		super();
		setBorder(null);
		setOpaque(true);
	}
	
	@Override
	public Insets getInsets() {
		return new Insets(0,1,0,1);
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
			setBackground(CollectionsApp.getHighlightBackground());
			setForeground(CollectionsApp.getHighlightForeground());
			setFont(CollectionsApp.getHighlightFont());
		}
		else {
			setBackground(CollectionsApp.getTableBackground());
			setForeground(CollectionsApp.getTableForeground());
			setFont(CollectionsApp.getTableFont());
		}
		
		String search = CollectionsApp.getSearchText();
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
						new DefaultHighlighter.DefaultHighlightPainter(CollectionsApp.getHighlightBackground()));
			} catch (BadLocationException e) { e.printStackTrace(); }
			
		} else {
			getHighlighter().removeAllHighlights();
		}
	}
	
	
}