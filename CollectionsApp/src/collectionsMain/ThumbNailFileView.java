package collectionsMain;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

public class ThumbNailFileView extends FileView {
	
	Component observer;
	
	public ThumbNailFileView(Component c){
		
		observer = c;
	}
	
	public String getDescription(File f) {
		
		return this.getTypeDescription(f);
	}

	public Icon getIcon(File f) {
		
		if(f.isDirectory()) return null;
		String name = f.getName().toLowerCase();
		if(name.endsWith("jpg") || name.endsWith("png") || name.endsWith("gif")) return new Icon16(f.getAbsolutePath());
		return null;
	}
	
	public String getName(File f) {
		
		String name = f.getName();
		return name.equals("")? f.getPath() : name;
	}
	
	public String getTypeDescription(File f) {
		
		String name = f.getName().toLowerCase();
		if(f.isDirectory()) return "Folder";
		else if(name.endsWith("jpg")) return "JPEG Image";
		else if(name.endsWith("gif")) return "GIF Image";
		else return "name.substring(name.length()-3)"+" File";
	}
	
	public Boolean isTraversable(File f) {
		
		return f.isDirectory()? Boolean.TRUE : Boolean.FALSE;
	}
	
	public class Icon16 extends ImageIcon{
		private static final long serialVersionUID = 1L;
		private Image img;
		
		public Icon16(String path) {
			super(path);
			img = observer.createImage(16, 16);
			img.getGraphics().drawImage(img, 0, 0, observer);
			setImage(img);
		}
		
		public int getIconWidth() {return img.getWidth(observer);}
		
		public int getIconHeight() {return img.getHeight(observer);}
		
		public void painIcon(Component c, Graphics g, int x, int y) {
			g.drawImage(img, x, y, c);
		}
	}
}
