package collectionsMain;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class SearchField extends JTextField implements FocusListener, KeyListener, MouseListener, MouseMotionListener {

	
	private static final long serialVersionUID = 1L;
	private String text;
	private CollectionsApp app;
	private Icon icon;


	public SearchField(CollectionsApp app, Icon icon, String textDisplayed) {
		super ();
		this.app = app;
		this.icon = icon;
		text = textDisplayed;
		setPreferredSize(new Dimension(150, 27));
		addFocusListener(this);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void setIcon(Icon icon) { this.icon = icon; }
 
    public Icon getIcon() { return icon; }
    
    public Dimension getMaximumSize() { return new Dimension(150,30); }
    
	public String getTextWhenNotFocused() { return text; }
 
    public void setTextWhenNotFocused(String newText) { text = newText; }
    
    public Rectangle getIconRectangle() {
    	int x = getWidth() - 3 - icon.getIconWidth() - 3;
    	int y = getInsets().top;
    	Rectangle rect = new Rectangle(x, y, icon.getIconWidth()+3, icon.getIconHeight());
    	return rect;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
       
    	super.paintComponent(g);
    	
    	int textX = 4;
    	 
        if(icon != null){
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int x = getWidth() - 3 - iconWidth;//this is our icon's x
            int y = (getHeight() - iconHeight)/2;
            textX = iconWidth + textX; //this is the x where text should end 
            icon.paintIcon(this, g, x, y);
        }
        setMargin(new Insets(0, 3, 0, textX));
        
        if (!hasFocus() && getText().equals("")) {
            int height = this.getHeight();
            Font prev = g.getFont();
            Font italic = prev.deriveFont(Font.ITALIC);
            Color prevColor = g.getColor();
            g.setFont(italic);
            g.setColor(UIManager.getColor("textInactiveText"));
            int h = g.getFontMetrics().getHeight();
            int textBottom = (height - h) / 2 + h - 3;
            int x = getInsets().left;
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints hints = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawString(text, x, textBottom);
            g2d.setRenderingHints(hints);
            g.setFont(prev);
            g.setColor(prevColor);
        }
    }
	
    
	@Override
	public void focusGained(FocusEvent e) { repaint(); }
	@Override
	public void focusLost(FocusEvent e) { repaint(); }

	
	@Override
	public void keyPressed(KeyEvent ke) { if(ke.getKeyCode() == KeyEvent.VK_ENTER) { app.searchFor(); } }
	@Override
	public void keyReleased(KeyEvent ke) {}
	@Override
	public void keyTyped(KeyEvent ke) {}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Rectangle rect = getIconRectangle();
		Point point = new Point(e.getX(), e.getY());
		if(rect.contains(point)) { 
			app.searchFor();
		}
	}
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }
	
	
	@Override
	public void mouseDragged(MouseEvent e) { }
	@Override
	public void mouseMoved(MouseEvent e) {
		Point point = new Point(e.getX(), e.getY());
		Rectangle rect = getIconRectangle();
		if(rect.contains(point)) {
			setCursor(new Cursor(Cursor.HAND_CURSOR)); 
			}
		else {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
			}
	}

}
