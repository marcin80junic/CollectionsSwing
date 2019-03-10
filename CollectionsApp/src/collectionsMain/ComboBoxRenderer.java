package collectionsMain;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
	
	private static final long serialVersionUID = 1L;
	

	public ComboBoxRenderer(){
		super();
		setOpaque(true);
        setVerticalAlignment(CENTER);
        setHorizontalAlignment(CENTER);    
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		setText(value.toString());
		Font highlightFont = CollectionsApp.getHighlightFont();
		if (isSelected) {
			if(index==0) setFont(highlightFont.deriveFont(Font.ITALIC));
			else setFont(highlightFont);
			setBackground(CollectionsApp.getHighlightBackground());
			setForeground(CollectionsApp.getHighlightForeground());
		} else {
			if(index==0) setFont(CollectionsApp.getTableFont().deriveFont(Font.PLAIN | Font.ITALIC));
			else setFont(CollectionsApp.getTableFont());
			setBackground(CollectionsApp.getTableBackground());
			setForeground(CollectionsApp.getTableForeground());
		}
		return this;
	}

}
