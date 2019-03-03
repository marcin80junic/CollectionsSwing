package collectionsMain;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import collectableItems.AbstractItem;
import collectableItems.AudioCD;
import collectableItems.Book;
import collectableItems.Collectable;
import collectableItems.Game;
import collectableItems.Movie;

public class CollectionsThumbNailView extends FileView {

	
	private DataBase<? extends Collectable<? extends AbstractItem>> dataBase;
	private Component observer;
	
	
	public CollectionsThumbNailView(Component c) {
		dataBase = new DataBase<>();
		observer = c;
	}
	
	@Override
	public String getName(File f) {
		return super.getName(f);
	}

	@Override
	public String getDescription(File f) {
		return super.getDescription(f);
	}

	@Override
	public String getTypeDescription(File f) {
		return super.getTypeDescription(f);
	}

	@Override
	public Icon getIcon(File f) {
		
		if(f.isDirectory()) return FileSystemView.getFileSystemView().getSystemIcon(f);
		if(dataBase.loadCollection(f) != null) {
			return new Icon16(dataBase.createImageIcon("/icons/Books.png", "books"));
		}
		return FileSystemView.getFileSystemView().getSystemIcon(f);
	}

	@Override
	public Boolean isTraversable(File f) {
		return super.isTraversable(f);
	}
	
	
	public class Icon16 extends ImageIcon {
		
		private static final long serialVersionUID = 1L;

		public Icon16(ImageIcon icon) {
			Image i = observer.createImage(16, 16);
			i.getGraphics().drawImage(icon.getImage(), 0, 0, 16, 16, observer);
			setImage(i);
		}
		
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.drawImage(getImage(), x, y, c);
		}
		
		public int getIconHeight() { return 16; }
		public int getIconWidth() { return 16; }
	}

}
