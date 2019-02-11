package collectionsMain;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

	
	private static final long serialVersionUID = 1L;
	private CollectionsApp app;


	public ComboBoxRenderer(CollectionsApp application){
		
		app = application;
		setOpaque(true);
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);
        
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		
		String s = (String) value;
		setText(s);
		Font highlightFont = app.getHighlightFont();
		
		if (isSelected) {
			if(index==0) setFont(highlightFont.deriveFont(Font.ITALIC));
			else setFont(highlightFont);
			setBackground(app.getHighlightBackground());
			setForeground(app.getHighlightForeground());
		} else {
			if(index==0) setFont(app.getTableFont().deriveFont(Font.PLAIN | Font.ITALIC));
			else setFont(app.getTableFont());
			setBackground(app.getTableBackground());
			setForeground(app.getTableForeground());
		}
		
		return this;
	}
	

}
