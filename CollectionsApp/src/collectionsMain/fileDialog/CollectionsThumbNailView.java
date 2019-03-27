package collectionsMain.fileDialog;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

import collectionsMain.collectableItems.AbstractItem;
import collectionsMain.collectableItems.Collectable;

public class CollectionsThumbNailView extends FileView {

	private Component observer;
	
	
	public CollectionsThumbNailView(Component c) {
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
	public Icon getIcon(File file) {
		if(file.isDirectory()) return FileSystemView.getFileSystemView().getSystemIcon(file);
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			ArrayList<?> list = (ArrayList<?>)ois.readObject();
			@SuppressWarnings("unchecked")
			Collectable<? extends AbstractItem> item = (Collectable<? extends AbstractItem>) list.get(0);
			return new Icon16(item.createImageIcon(item.getIconPath(), "Icon"));
		} catch (IOException e) { return FileSystemView.getFileSystemView().getSystemIcon(file); } 
		catch (IndexOutOfBoundsException e) { return FileSystemView.getFileSystemView().getSystemIcon(file); } 
		catch (ClassNotFoundException e) { return FileSystemView.getFileSystemView().getSystemIcon(file); }
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
