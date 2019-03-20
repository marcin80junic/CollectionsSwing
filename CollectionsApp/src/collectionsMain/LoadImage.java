package collectionsMain;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;

class LoadImage extends Component implements Icon {
	
	private static final long serialVersionUID = 1L;
	BufferedImage image;
	String path;
	
	public LoadImage(String path) {
		setFocusable(true);
		this.path = path;
		try {
			image = ImageIO.read(new File(path));
			if((image.getHeight() > 100) || (image.getWidth() > 100) || ((image.getHeight() != 100) && (image.getWidth() != 100))) {
				resizeImage(path);
				image = ImageIO.read(new File(path));
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void paint(Graphics g) {
		int x = (100 - image.getWidth())/2;
		int y = (100 - image.getHeight())/2;
        g.drawImage(image, x, y, null);
        g.drawRect(0, 0, 100, 100);
    }
	
	public Dimension getPreferredSize() {
		if(image==null) {
			return null;
		} else {
			return new Dimension(image.getWidth(null), image.getHeight(null));
		}
	}
	
	public void resizeImage (String path) throws IOException {
		
		File inputFile = new File(path);
		BufferedImage inputImage = ImageIO.read(inputFile);
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		int scaledWidth, scaledHeight;
		
		if(width > height) {
			scaledWidth = 100;
			double percent = 100.0/width;
			scaledHeight = (int) (height*percent);
		} else {
			scaledHeight = 100;
			double percent = 100.0/height;
			scaledWidth = (int) (width*percent);
		}
		BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());	
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		g2d.dispose();
		
		String formatName = path.substring(path.lastIndexOf(".") + 1);
		ImageIO.write(outputImage, formatName, new File(path));
	}
	
	public void rotateClockwise90() {
		BufferedImage src = image;
	    int w = src.getWidth();
	    int h = src.getHeight();
	    int a;
	    BufferedImage dest = new BufferedImage(h, w, src.getType());
	    for (int y=0; y<w; y++) {
	    	a = 0;
	        for (int x=h-1; x>0; x--) {
	            dest.setRGB(x,y,src.getRGB(y,a++));
	        }
	    }
	    image = dest;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) { g.drawImage(image, x, y, c);	}

	@Override
	public int getIconWidth() { return image.getWidth(); }

	@Override
	public int getIconHeight() { return image.getHeight(); }
	
}