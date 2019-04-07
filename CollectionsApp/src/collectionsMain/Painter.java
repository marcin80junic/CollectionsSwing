package collectionsMain;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

public class Painter extends AbstractRegionPainter {
    
	private final Color color;

    public Painter(Color color) {
        this.color = color;
    }
    
    @Override
    protected AbstractRegionPainter.PaintContext getPaintContext() {
        return new AbstractRegionPainter.PaintContext(null, null, false);
    }

    @Override
    protected void doPaint(Graphics2D g, JComponent c, 
            int width, int height, Object[] extendedCacheKeys) {
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }
}