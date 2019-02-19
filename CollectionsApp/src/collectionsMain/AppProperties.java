package collectionsMain;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties extends Properties {

	private static final long serialVersionUID = 1L;
	
	public static final int MAIN_BACKGROUND = 0;
	public static final int MAIN_FOREGROUND = 1;
	public static final int WELCOME_BACKGROUND = 2;
	public static final int WELCOME_FOREGROUND = 3;
	public static final int TABLE_BACKGROUND = 4;
	public static final int TABLE_FOREGROUND = 5;
	public static final int HIGHLIGHT_BACKGROUND = 6;
	public static final int HIGHLIGHT_FOREGROUND = 7;
	
	protected final int COLORS_COUNT = 8;
	
	public static final int MAIN_FONT_FAMILY = 8;
	public static final int MAIN_FONT_BOLD = 9;
	public static final int MAIN_FONT_ITALIC = 10;
	public static final int WELCOME_FONT_FAMILY = 11;
	public static final int WELCOME_FONT_BOLD = 12;
	public static final int WELCOME_FONT_ITALIC = 13;
	public static final int TABLE_FONT_FAMILY = 14;
	public static final int TABLE_FONT_BOLD = 15;
	public static final int TABLE_FONT_ITALIC = 16;
	public static final int HIGHLIGHT_FONT_FAMILY = 17;
	public static final int HIGHLIGHT_FONT_BOLD = 18;
	public static final int HIGHLIGHT_FONT_ITALIC = 19;
	
	protected final int FONTS_PROP_COUNT = 12;
	
	protected final int PROPERTIES_COUNT = 20;
	
	protected final String[] KEYS = {"main.background", "main.foreground", "welcome.background", "welcome.foreground", "table.background",
			"table.foreground", "highlight.background", "highlight.foreground", "main.font.family", "main.font.bold", "main.font.italic", 
			"welcome.font.family", "welcome.font.bold", "welcome.font.italic", "table.font.family", "table.font.bold", "table.font.italic",
			"highlight.font.family", "highlight.font.bold", "highlight.font.italic"};
	
	private final File XML_FILE = new File("properties.xml");
	private final File DEF_XML_FILE = new File("defaults.xml");
	
	
	public AppProperties() {
		super();
		loadDefaults();
		if(XML_FILE.isFile()) {
			try(FileInputStream fis = new FileInputStream(XML_FILE)){
				super.loadFromXML(fis);
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	private void loadDefaults() {
		Properties defaults = new Properties();
		if(DEF_XML_FILE.isFile()) {
			try(FileInputStream fis = new FileInputStream(DEF_XML_FILE)){
				defaults.loadFromXML(fis);
			} catch (IOException e) { e.printStackTrace(); }
		} else {
			String[] defColors = new String[COLORS_COUNT];
			for(int i=0; i<defColors.length; i++) {
				if(i == MAIN_BACKGROUND || i == WELCOME_BACKGROUND) defColors[i] = "#d6d9df";
				else if(i == MAIN_FOREGROUND || i == WELCOME_FOREGROUND || i == TABLE_FOREGROUND || i == HIGHLIGHT_BACKGROUND)
					defColors[i] = "#000000";
				else if(i == TABLE_BACKGROUND || i == HIGHLIGHT_FOREGROUND) defColors[i] = "#ffffff";
				defaults.put(KEYS[i], defColors[i]);
			}
			String[] defFonts = new String[FONTS_PROP_COUNT];
			for(int i=0; i<defFonts.length; i++) {
				if(i == MAIN_FONT_FAMILY || i== WELCOME_FONT_FAMILY || i == TABLE_FONT_FAMILY || i == HIGHLIGHT_FONT_FAMILY)
					defFonts[i] = "serif";
				if(i == WELCOME_FONT_BOLD || i == HIGHLIGHT_FONT_ITALIC || i == HIGHLIGHT_FONT_BOLD) defFonts[i] = "true";
				else defFonts[i] = "false";
				defaults.put(KEYS[COLORS_COUNT+i], defFonts[i]);
			}
			try(FileOutputStream out = new FileOutputStream(DEF_XML_FILE)){
				defaults.storeToXML(out, "defaults");
			} catch (IOException e) { e.printStackTrace(); }
		}
		this.defaults = defaults;
	}
	
	private void saveProperties() {
		try(FileOutputStream out = new FileOutputStream(XML_FILE)){
			super.storeToXML(out, "properties");
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void putProperty(int key, String value) {
		if( key > (PROPERTIES_COUNT-1) || key < 0) throw new IllegalArgumentException("property is invalid");
		super.put(KEYS[key], value);
		saveProperties();
	}
	
	public Color getColor(int property) {
		if(property >= COLORS_COUNT || property < 0) throw new IllegalArgumentException("property is invalid");
		return Color.decode(getProperty(KEYS[property]));
	}
	
	public Font getFont(int fontFamily) {
		if(fontFamily != MAIN_FONT_FAMILY || fontFamily != WELCOME_FONT_FAMILY || fontFamily != TABLE_FONT_FAMILY || 
				fontFamily != HIGHLIGHT_FONT_FAMILY) throw new IllegalArgumentException("property is invalid");
		String fontName = getProperty(KEYS[fontFamily]);
		boolean isBold = getProperty(KEYS[fontFamily+1]).equals("true");
		boolean isItalic = getProperty(KEYS[fontFamily+2]).equals("true");
		return new Font(fontName, (isBold? Font.BOLD: 0 ) | (isItalic? Font.ITALIC: 0), 12);
	}
	
	public void removeProperty(int key) {
		if( key > (PROPERTIES_COUNT-1) || key < 0) throw new IllegalArgumentException("property is invalid");
		super.remove(KEYS[key]);
		saveProperties();
	}
	
}
