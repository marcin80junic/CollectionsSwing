package collectionsMain;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;

public class TableHeaderRenderer extends JPanel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
	private JLabel title, icon;
	private SorterIcon ascIcon, descIcon;
	private boolean isAsc;


	public TableHeaderRenderer() {
		super();
		isAsc = false;
		title = new JLabel();
		icon = new JLabel();
		icon.setPreferredSize(new Dimension(12,24));
		title.setHorizontalAlignment(JLabel.CENTER);
		ascIcon = new SorterIcon(true);
		descIcon = new SorterIcon(false);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(15, 24));
		setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		add(title, BorderLayout.CENTER);
		add(icon, BorderLayout.LINE_END);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		title.setText(value.toString());
		title.setFont(CollectionsApp.getMainFont());
		title.setForeground(CollectionsApp.getMainForeground());
		if(column == 0) {
			remove(icon);
			return this;
		}
		else if(column == TableModelCollection.getAscending()) {
			icon.setIcon(ascIcon);
			isAsc = true;
		}
		else if(column == TableModelCollection.getDescending()) {
			icon.setIcon(descIcon);
			isAsc = false;
		}
		else {
			icon.setIcon(null);
			isAsc = false;
		}
		return this;
	}
	
	public boolean isAscIcon() { return isAsc; }
	
	
	class SorterIcon implements Icon {
		
		private boolean ascending;
		private static final int ARROW_HEIGHT = 5;
		private static final int X_PADDING = 4;
		
		public SorterIcon(boolean ascending) {
			this.ascending = ascending;
		}
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(CollectionsApp.getMainForeground());
			int startX = X_PADDING + x + ARROW_HEIGHT / 2;
			if (ascending) {
				int startY = y;
				g.fillRect(startX, startY, 1, 1);
				for (int line = 1; line < ARROW_HEIGHT; line++) {
					g.fillRect(startX - line, startY + line,
							line + line + 1, 1);
				}
			}
			else {
				int startY = y + ARROW_HEIGHT - 1;
				g.fillRect(startX, startY, 1, 1);
				for (int line = 1; line < ARROW_HEIGHT; line++) {
					g.fillRect(startX - line, startY - line,
							line + line + 1, 1);
				}
			}
		}
		@Override
		public int getIconWidth() { return ARROW_HEIGHT + 2; }
		@Override
		public int getIconHeight() { return ARROW_HEIGHT + 2; }
	}
	
}
