package collectionsMain;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import collectableItems.AbstractItem;
import collectableItems.Collectable;

public class TableMouseListener extends MouseAdapter {
	
	private CollectionsApp application;
	private JTable table;
	private JPopupMenu popupMenu;
	private DataBase<? extends Collectable<? extends AbstractItem>> dataBase;
	private Point point;
	
	
	public TableMouseListener (CollectionsApp app, JTable table, DataBase<? extends Collectable<? extends AbstractItem>> dB){
		application = app;
		this.table = table;
		popupMenu = table.getComponentPopupMenu();
		dataBase = dB;
		point = new Point();
	}
	
	@Override
	public void mousePressed (MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON3) {
			point = e.getPoint();
			showPopupMenu(point);
		}	
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
		if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
			point = e.getPoint();
			int currentRow = table.rowAtPoint(point);
			if(currentRow == -1) return;
			int row = table.getSelectedRow();
			new AddNewOrEditDialog(application, table, dataBase, row);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) { }
	
	
	private void showPopupMenu(Point point) {
		
		int currentRow = table.rowAtPoint(point);
		if(currentRow == -1) table.clearSelection();
		else table.setRowSelectionInterval(currentRow, currentRow);
		
		int popupHeight = popupMenu.getHeight();
		int popupWidth = popupMenu.getWidth();
		int bottomPoint = (int) table.getVisibleRect().getHeight();
		int rightHandPoint = table.getWidth();
		
		if(popupHeight == 0 || popupWidth == 0) {
			popupMenu.setVisible(true);
			popupHeight = popupMenu.getHeight();
			popupWidth = popupMenu.getWidth();
			popupMenu.setVisible(false);
		}
		
		if (((rightHandPoint - point.x) > popupWidth) && ((bottomPoint - point.y) > popupHeight)) {
			popupMenu.show(table, point.x, point.y);
		}
		else if (((rightHandPoint - point.x) > popupWidth) && ((bottomPoint - point.y) < popupHeight)) {
			int y = point.y - popupHeight;
			popupMenu.show(table, point.x, y);
		}
		else if (((rightHandPoint - point.x) < popupWidth) && ((bottomPoint - point.y) < popupHeight)) {
			int y = point.y - popupHeight;
			int x = point.x - popupWidth;
			popupMenu.show(table, x, y);
		}
		else {
			int x = point.x - popupWidth;
			popupMenu.show(table, x, point.y);
		}
		
	}

}
