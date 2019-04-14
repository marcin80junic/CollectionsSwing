package collectionsMain;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import collectionsMain.collectableItems.Collectable;
import collectionsMain.fileDialog.CollectionsFileFilter;

public class ImageLoader extends Component implements Icon, FocusListener, MouseListener, MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	private DataBase<?> dataBase;
	private Collectable<?> item;
	private BufferedImage image;
	private File imgDir = new File("images");
	private String path, temp;
	private int w, h, indentLeft, indentTop, gap;
	private int inset = 4;
	private Icon open, remove, rotateRight, rotateLeft;
	private boolean isLoaded = false;
	
	
	public ImageLoader(DataBase<?> db, Collectable<?> item, int width, int height, int parentWidth) {
		dataBase = db;
		this.item = item;
		w = width;
		h = height;
		gap = (w - inset - 4*24)/5;
		indentLeft = (parentWidth - w)/2;
		indentTop = 10;
		setFocusable(true);
		if(!imgDir.isDirectory()) imgDir.mkdir();
		temp = "";
		path = item.getPhotoPath();
		path = (path==null)? "": path;
		if(!path.equals("")) loadImage(new File(path));
		initIcons();
		addFocusListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	private void loadImage(File imgFile) {
		if(imgFile.isFile()) {
			try {
				image = ImageIO.read(imgFile);
				if( ( !(image.getHeight()!=h) ^ (image.getWidth()!=w) ) || ( (image.getHeight()>h) || (image.getWidth()>w) ) ) {
					resizeImage(imgFile);
				}
			} catch (IOException e) {
				e.printStackTrace();
				image = null;
				isLoaded = false;
				return;
			}
			isLoaded = true;
			System.out.println(path+", "+temp);
			if(!path.equals(temp) && !temp.equals("")) {
				System.out.println("ins");
				item.setPhotoPath(path=temp);
				dataBase.saveToFile();
			}
		}
	}

	public void resizeImage (File imgFile) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		int scaledWidth, scaledHeight;
		if(width > height) {
			scaledWidth = w;
			double percent = 100.0/width;
			scaledHeight = (int) (height*percent);
		} else {
			scaledHeight = h;
			double percent = 100.0/height;
			scaledWidth = (int) (width*percent);
		}
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());	
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(image, 1, 1, scaledWidth, scaledHeight, this);
		g2d.dispose();
		String fileName = imgFile.getName();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		temp = imgDir.getPath()+"/"+fileName;
		ImageIO.write(outputImage, ext, new File(temp));
		image = outputImage;
	}
	
	private void initIcons() {
		open = createImageIcon("/icons/image_add_24.png", "open icon");
		remove = createImageIcon("/icons/image_remove_24.png", "remove icon");
		rotateLeft = createImageIcon("/icons/rotate_anticlockwise_24.png", "rotate left icon");
		rotateRight = createImageIcon("/icons/rotate_clockwise_24.png", "rotate right icon");
	}
	
	public void paint(Graphics g) {
		g.drawRect(0, 0, w, h);
		if(!isLoaded) {
			String def = "Click to upload photo";
			FontMetrics fm = g.getFontMetrics();
			Rectangle2D rect = fm.getStringBounds(def, g);
			int x = (int) ((w-rect.getWidth())/2);
			int y = h/2;
			g.drawString(def, x, y);
			return;
		}
		if(image != null) {
			int x = (w - image.getWidth())/2;
			int y = (h - image.getHeight())/2;
			g.drawImage(image, x, y, this);
		}
		else {
			g.setColor(CollectionsApp.getTableBackground());
			g.fillRect(1, 1, w-1, h-1);
		}
		if(hasFocus() && isLoaded) {
			Icon[] icons = {open, remove, rotateLeft, rotateRight};
			for(int i=0; i<icons.length; i++) {
				icons[i].paintIcon(this, g, (inset + i*icons[i].getIconWidth() + gap + gap*i), getHeight() - open.getIconHeight()-inset*2);
			}
		}
	}

	private void rotateClockwise90() {
		BufferedImage src = image;
	    int w = src.getWidth();
	    int h = src.getHeight();
	    int a;
	    BufferedImage dest = new BufferedImage(h, w, src.getType());
	    for (int y=0; y<w; y++) {
	    	a = 0;
	        for (int x=h-1; x>=0; x--) {
	            dest.setRGB(x,y,src.getRGB(y,a++));
	        }
	    }
	    image = dest;
	    repaint();
	}
	
	private void rotateAntiClockwise90() {
		BufferedImage src = image;
	    int width = src.getWidth();
	    int height = src.getHeight();
	    int a,b;
	    BufferedImage dest = new BufferedImage(height, width, src.getType());
	    b=width-1;
	    for (int x=0; x<width; x++) {
	    	a = 0;
	        for (int y=0; y<height; y++) {
	            dest.setRGB(a++,b,src.getRGB(x,y));
	        }
	        b--;
	    }
	    image = dest;
	    repaint();
	}
	
	private Rectangle getOpenRect() { 
		return new Rectangle(inset+gap, getHeight()-inset*2-open.getIconHeight(), open.getIconWidth(), open.getIconHeight()); 
	}
	private Rectangle getRemoveRect() {
		return new Rectangle(inset+gap*2+open.getIconWidth(), getHeight()-inset*2-remove.getIconHeight(),
								remove.getIconWidth(), remove.getIconHeight());
	}
	private Rectangle getRotateLeftRect() {
		return new Rectangle(inset+gap*3+open.getIconWidth()+remove.getIconWidth(), getHeight()-inset*2-rotateLeft.getIconHeight(),
								rotateLeft.getIconWidth(), rotateLeft.getIconHeight());
	}
	private Rectangle getRotateRightRect() {
		return new Rectangle(getWidth()-inset-gap-rotateRight.getIconWidth(), getHeight()-inset*2-rotateRight.getIconHeight(),
								rotateRight.getIconWidth(), rotateRight.getIconHeight());
	}
	
	private void openNewImage() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose new image");
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileFilter(new CollectionsFileFilter("Image files", "jpg", "png", "gif"));
		int option = chooser.showDialog(null, "Load");
		if(option == JFileChooser.APPROVE_OPTION) {
			loadImage(chooser.getSelectedFile());
		}
	}
	private void removeImage() { 
		image = null;
		isLoaded = false;
		item.setPhotoPath(path = "");
		dataBase.saveToFile();
		repaint();
	}
	
	@Override
	public Rectangle getBounds() { return new Rectangle(indentLeft, indentTop, getWidth(), getHeight()); }
	@Override
	public int getWidth() { return w+inset; }
	@Override
	public int getHeight() { return h+inset; }
	@Override
	public Dimension getMinimumSize() { return getPreferredSize(); }
	@Override
	public Dimension getPreferredSize() { return new Dimension(getWidth()+getBounds().x, getHeight()+getBounds().y); }
	
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) { if(image != null) g.drawImage(image, x, y, c); }
	@Override
	public int getIconWidth() {
		if(image != null) return image.getWidth(); 
		return 0;
	}
	@Override
	public int getIconHeight() { 
		if(image != null) return image.getHeight();
		return 0;
	}	
	
	
	@Override
	public void mouseDragged(MouseEvent e) { }
	@Override
	public void mouseMoved(MouseEvent e) {
		Point point = new Point(e.getX(), e.getY());
		point.translate(-getBounds().x, -getBounds().y);
		Rectangle openRect = getOpenRect();
		Rectangle removeRect = getRemoveRect();
		Rectangle rotLeftRect = getRotateLeftRect();
		Rectangle rotRightRect = getRotateRightRect();
		if(openRect.contains(point)||removeRect.contains(point)||rotLeftRect.contains(point)||rotRightRect.contains(point)||!isLoaded) {
			setCursor(new Cursor(Cursor.HAND_CURSOR)); 
			}
		else {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point point = new Point(e.getX(), e.getY());
		point.translate(-getBounds().x, -getBounds().y);
		Rectangle openRect = getOpenRect();
		Rectangle removeRect = getRemoveRect();
		Rectangle rotLeftRect = getRotateLeftRect();
		Rectangle rotRightRect = getRotateRightRect();
		if(openRect.contains(point) || !isLoaded) openNewImage();
		else if(removeRect.contains(point)) removeImage();
		else if(rotLeftRect.contains(point)) rotateAntiClockwise90();
		else if(rotRightRect.contains(point)) rotateClockwise90();
	}
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { requestFocusInWindow(true); }
	@Override
	public void mouseExited(MouseEvent e) {	transferFocusUpCycle();}

	
	@Override
	public void focusGained(FocusEvent e) { repaint(); }
	@Override
	public void focusLost(FocusEvent e) { repaint(); }
	
	
	public ImageIcon createImageIcon(String path, String description) {
		URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}