package collectionsMain;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

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
	protected static final int COLORS_COUNT = 8;
	
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
	protected static final int FONTS_PROP_COUNT = 12;
	
	public static final int BOOKS_COLUMN_SIZES = 20;
	public static final int GAMES_COLUMN_SIZES = 21;
	public static final int MOVIES_COLUMN_SIZES = 22;
	public static final int MUSIC_COLUMN_SIZES = 23;
	protected static final int COLUMN_SIZES_COUNT = 4;
	
	public static final int BOOKS_FILE_PATH = 24;
	public static final int GAMES_FILE_PATH = 25;
	public static final int MOVIES_FILE_PATH = 26;
	public static final int MUSIC_FILE_PATH = 27;
	protected static final int FILE_PATHS_COUNT = 4;
	
	protected static final int PROPERTIES_COUNT = 28;
	
	protected final String[] KEYS = {"main.background", "main.foreground", "welcome.background", "welcome.foreground", "table.background",
			"table.foreground", "highlight.background", "highlight.foreground", "main.font.family", "main.font.bold", "main.font.italic", 
			"welcome.font.family", "welcome.font.bold", "welcome.font.italic", "table.font.family", "table.font.bold", "table.font.italic",
			"highlight.font.family", "highlight.font.bold", "highlight.font.italic", "books.column.sizes", "games.column.sizes",
			"movies.column.sizes", "music.column.sizes", "books.file.path", "games.file.path", "movies.file.path", "music.file.path"};
	
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
				int j = COLORS_COUNT+i;
				if(j == MAIN_FONT_FAMILY || j== WELCOME_FONT_FAMILY || j == TABLE_FONT_FAMILY || j == HIGHLIGHT_FONT_FAMILY)
					defFonts[i] = "serif";
				if(j == WELCOME_FONT_BOLD || j == HIGHLIGHT_FONT_ITALIC || j == HIGHLIGHT_FONT_BOLD) defFonts[i] = "true";
				else defFonts[i] = "false";
				defaults.put(KEYS[j], defFonts[i]);
			}
			String[] defColumns = new String[COLUMN_SIZES_COUNT];
			for(int i=0; i<defColumns.length; i++) {
				int j = COLORS_COUNT+FONTS_PROP_COUNT+i;
				if(j == BOOKS_COLUMN_SIZES) defColumns[i] = "200,200,190,120,80";
				if(j == GAMES_COLUMN_SIZES) defColumns[i] = "225,200,150,200,45";
				if(j == MOVIES_COLUMN_SIZES) defColumns[i] = "225,200,150,200,45";
				if(j == MUSIC_COLUMN_SIZES) defColumns[i] = "225,220,200,45,45";
				defaults.put(KEYS[j], defColumns[i]);
			}
			String[] defPaths = new String[FILE_PATHS_COUNT];
			for(int i=0; i<defPaths.length; i++) {
				int j = COLORS_COUNT+FONTS_PROP_COUNT+COLUMN_SIZES_COUNT+i;
				if(j == BOOKS_FILE_PATH) defPaths[i] = "/books.dat";
				if(j == GAMES_FILE_PATH) defPaths[i] = "/games.dat";
				if(j == MOVIES_FILE_PATH) defPaths[i] = "/movies.dat";
				if(j == MUSIC_FILE_PATH) defPaths[i] = "/music.dat";
				defaults.put(KEYS[j], defPaths[i]);
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
		if(fontFamily != MAIN_FONT_FAMILY && fontFamily != WELCOME_FONT_FAMILY && fontFamily != TABLE_FONT_FAMILY && 
				fontFamily != HIGHLIGHT_FONT_FAMILY) throw new IllegalArgumentException("property is invalid");
		String fontName = getProperty(KEYS[fontFamily]);
		boolean isBold = getProperty(KEYS[fontFamily+1]).equals("true");
		boolean isItalic = getProperty(KEYS[fontFamily+2]).equals("true");
		return new Font(fontName, (isBold? Font.BOLD: 0 ) | (isItalic? Font.ITALIC: 0), 12);
	}
	
	public String getFilePath(int which) {
		if(which >= COLORS_COUNT+FONTS_PROP_COUNT+COLUMN_SIZES_COUNT+FILE_PATHS_COUNT ||
				which < COLORS_COUNT+FONTS_PROP_COUNT+COLUMN_SIZES_COUNT) {
			throw new IllegalArgumentException("property is invalid");
		}
		return getProperty(KEYS[which]);
	}
	
	public int[] getColumnWidths(int whichColumnWidths) {
		if(whichColumnWidths != BOOKS_COLUMN_SIZES && whichColumnWidths != GAMES_COLUMN_SIZES && whichColumnWidths != MOVIES_COLUMN_SIZES &&
				whichColumnWidths != MUSIC_COLUMN_SIZES) throw new IllegalArgumentException("property is invalid");
		String[] columns = getProperty(KEYS[whichColumnWidths]).split(",");
		int[] widths = new int[columns.length];
		for(int i=0; i<columns.length; i++) {
			widths[i] = Integer.parseInt(columns[i]);
		}
		return widths;
	}
	
	public void setColumnSizes(int columnSizes, JTable table) {
		if(columnSizes != BOOKS_COLUMN_SIZES && columnSizes != GAMES_COLUMN_SIZES && columnSizes != MOVIES_COLUMN_SIZES &&
				columnSizes != MUSIC_COLUMN_SIZES) throw new IllegalArgumentException("property is invalid");
		String[] columns = getProperty(KEYS[columnSizes]).split(",");
		TableColumnModel model = table.getColumnModel();
		TableColumn column = null;
		for(int i=0; i<columns.length; i++) {
			column = model.getColumn(i);
			if(i == 0) {
				if(table.getRowCount() < 10) column.setMaxWidth(15);
				else if(table.getRowCount() < 100) column.setPreferredWidth(25);
				else if(table.getRowCount() < 1000) column.setPreferredWidth(35);
				else if(table.getRowCount() < 10000) column.setPreferredWidth(45);
			} else if(i == columns.length-1) {
				column.setMinWidth(Integer.valueOf(columns[i-1]));
				column.setMaxWidth(Integer.valueOf(columns[i-1]));
			} else {
				column.setMinWidth(50);
				column.setPreferredWidth(Integer.valueOf(columns[i-1]));
				column.setMaxWidth(300);
			}
		}
	}
	
	public void removeProperty(int key) {
		if( key > (PROPERTIES_COUNT-1) || key < 0) throw new IllegalArgumentException("property is invalid");
		super.remove(KEYS[key]);
		saveProperties();
	}
	
}
