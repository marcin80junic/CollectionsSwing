package collectionsMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.SimpleAttributeSet;

public class Appearance extends JPanel implements ChangeListener, ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private CollectionsApp parent;
	private AppProperties properties;
	private JPanel previewPanel;
	private JList<String> list;
	private JColorChooser colorChooser;
	private JLabel textPreview, boxPreview;
	private JButton btnBackground, btnForeground, btnApply, btnDefault;
	private JCheckBox checkBold, checkItalic;
	private JComboBox<String> fontChooser;
	private Color newColor;
	private Font newFont;
	private SimpleAttributeSet[] defaultAttributes, initAttributes, currentAttributes;
	private int index;
	
	
	public Appearance(CollectionsApp app, AppProperties props, JButton apply, JButton def) {
		parent = app;
		properties = props;
		btnApply = apply;
		btnDefault = def;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		initAttributes();
		add(colorChooserInit());
		add(fontPanelInit());
		btnApply.setEnabled(false);
		btnDefault.setEnabled(!currentAttributes[index].equals(defaultAttributes[index]));
	}
	
	private void updateAppearance() {
		AbstractColorChooserPanel[] panes = colorChooser.getChooserPanels();
		for(int i=0; i<panes.length; i++) {
			if(i!=0) colorChooser.removeChooserPanel(panes[i]);
		}
	}
	
	private JPanel colorChooserInit() {
		JPanel colorPane = new JPanel();
		colorPane.setLayout(new BorderLayout());
		colorChooser = new JColorChooser();
		updateAppearance();
		colorChooser.setPreviewPanel(previewPanelInit());
		colorChooser.getSelectionModel().addChangeListener(this);
		newColor = colorChooser.getColor();
		colorPane.add(colorChooser, BorderLayout.CENTER);
		return colorPane;
	}
	
	private JPanel previewPanelInit() {
		JPanel westPane = new JPanel();
		westPane.setLayout(new BorderLayout());
		westPane.setOpaque(false);
		String[] elements = new String[] {"Application's Frame", "Welcome Screen", "Table", "Highlight Style"};
		list = new JList<>(elements);
		index = 0;
		list.setSelectedIndex(index);
		list.setPreferredSize(new Dimension(120, 40));
		list.addListSelectionListener(this);
		JScrollPane listScroll = new JScrollPane(list);
		westPane.add(listScroll, BorderLayout.CENTER);
		
		textPreview = new JLabel("sample text...", JLabel.CENTER);
		textPreview.setPreferredSize(new Dimension(340, 40));
		textPreview.setFont(CollectionsApp.getMainFont());
		textPreview.setForeground(CollectionsApp.getMainForeground());
		btnBackground = new JButton("Set Background");
		btnForeground = new JButton("Set Font Color");
		btnBackground.addActionListener(this);
		btnForeground.addActionListener(this);
		boxPreview = new JLabel(new ColorBox(50));
		
		JPanel east = new JPanel();
		east.setOpaque(false);
		east.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1; c.gridy = 0;
		east.add(btnForeground, c);
		c.gridx = 1; c.gridy = 1;
		east.add(btnBackground, c);
		c.gridx = 0; c.gridy = 0;
		c.gridheight = 2; c.insets = new Insets(0,0,0,20);
		east.add(boxPreview, c);
		
		previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout());
		previewPanel.setPreferredSize(new Dimension(630, 85));
		previewPanel.setBackground(CollectionsApp.getMainBackground());
		previewPanel.add(textPreview, BorderLayout.CENTER);
		previewPanel.add(westPane, BorderLayout.LINE_START);
		previewPanel.add(east, BorderLayout.LINE_END);
		return previewPanel;
	}
	
	private JPanel fontPanelInit() {
		
		JPanel bottomPanel = new JPanel();
		JPanel fontSettings = new JPanel();
		JPanel combo = new JPanel();
		JPanel text = new JPanel();
		fontSettings.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		fontSettings.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Font Settings"));
		
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fontChooser = new JComboBox<>(e.getAvailableFontFamilyNames());
		fontChooser.setActionCommand("Font Chooser");
		fontChooser.addActionListener(this);
		checkBold = new JCheckBox("Bold");
		checkBold.setFont(getFont().deriveFont(Font.BOLD));
		checkBold.addActionListener(this);
		checkItalic = new JCheckBox("Italic");
		checkItalic.setFont(getFont().deriveFont(Font.ITALIC));
		checkItalic.addActionListener(this);
		newFont = CollectionsApp.getMainFont();
		updateFontSettings();
		
		combo.add(new JLabel("Choose font: "));
		combo.add(fontChooser);
		fontSettings.add(combo);
		fontSettings.add(text);
		fontSettings.add(checkBold);
		fontSettings.add(checkItalic);
		bottomPanel.add(fontSettings);
		return bottomPanel;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Set Background")) {	
			previewPanel.setBackground(newColor);
		}
		else if(e.getActionCommand().equals("Set Font Color")) {
			textPreview.setForeground(newColor); 
		}
		else if(e.getActionCommand().equals("Font Chooser")) {
			String fontName = (String) fontChooser.getSelectedItem();
			newFont = new Font(fontName, (checkBold.isSelected()? Font.BOLD: 0) | (checkItalic.isSelected()? Font.ITALIC: 0), 12);
			textPreview.setFont(newFont);
		}
		else if(e.getActionCommand().equals("Bold")) {	
			newFont = textPreview.getFont().deriveFont((checkBold.isSelected()? Font.BOLD: 0) | (checkItalic.isSelected()? Font.ITALIC: 0));
			textPreview.setFont(newFont);
		}
		else if(e.getActionCommand().equals("Italic")) {
			newFont = textPreview.getFont().deriveFont((checkItalic.isSelected()? Font.ITALIC: 0) | (checkBold.isSelected()? Font.BOLD: 0));
			textPreview.setFont(newFont);
		}
		saveAttributes();
		btnApply.setEnabled(!initAttributes[index].equals(currentAttributes[index]));
		btnDefault.setEnabled(!currentAttributes[index].equals(defaultAttributes[index]));
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		index = list.getSelectedIndex();
		updatePreview();
		updateFontSettings();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		newColor = colorChooser.getSelectionModel().getSelectedColor();
		boxPreview.repaint();
	}
	
	private void updatePreview() {
		Enumeration<?> keys = currentAttributes[index].getAttributeNames();
		String family = "";
		boolean bold = false, italic = false;
		while(keys.hasMoreElements()) {
			String name = (String)keys.nextElement();
			String value = (String)currentAttributes[index].getAttribute(name);
			if(name.endsWith("background")) previewPanel.setBackground(Color.decode(value));
			else if(name.endsWith("foreground")) textPreview.setForeground(Color.decode(value));
			else if(name.endsWith("family")) family = value;
			else if(name.endsWith("bold")) bold = value.equals("true")? true: false;
			else if(name.endsWith("italic")) italic = value.equals("true")? true: false;
		}
		newFont = new Font(family, (bold? Font.BOLD:0) | (italic? Font.ITALIC:0), 12);
		textPreview.setFont(newFont);
	}
	
	private void updateFontSettings() {
		checkBold.setSelected(newFont.isBold());
		checkItalic.setSelected(newFont.isItalic());
		fontChooser.setSelectedItem((String) newFont.getFamily());
	}
	
	void apply() {
		saveProperties();
		properties.saveProperties();
		btnApply.setEnabled(!initAttributes[index].equals(currentAttributes[index]));
		btnDefault.setEnabled(!currentAttributes[index].equals(defaultAttributes[index]));
		parent.updateAppearance(list.getSelectedValue());
		updateAppearance();
	}
	
	void setDefault() {
		Enumeration<?> keys = currentAttributes[index].getAttributeNames();
		while(keys.hasMoreElements()) {
			String name = (String)keys.nextElement();
			properties.remove(name);	
		}
		currentAttributes[index] = new SimpleAttributeSet(defaultAttributes[index]);
		updatePreview();
		updateFontSettings();
		btnDefault.setEnabled(!currentAttributes[index].equals(defaultAttributes[index]));
	}
	
	private void saveAttributes() {
		Enumeration<?> keys = currentAttributes[index].getAttributeNames();
		while(keys.hasMoreElements()) {
			String name = (String)keys.nextElement();
			if(name.endsWith("background")) {
				currentAttributes[index].addAttribute(name, colorToHex(previewPanel.getBackground()));
			}
			else if(name.endsWith("foreground")) {
				currentAttributes[index].addAttribute(name, colorToHex(textPreview.getForeground()));
			}
			else if(name.endsWith("family")) {
				currentAttributes[index].addAttribute(name, newFont.getFamily());
			}
			else if(name.endsWith("bold")) {
				currentAttributes[index].addAttribute(name, newFont.isBold()? "true": "false");
			}
			else if(name.endsWith("italic")) {
				currentAttributes[index].addAttribute(name, newFont.isItalic()? "true": "false");
			}
		}
	}
	
	private void saveProperties() {
		Enumeration<?> keys = currentAttributes[index].getAttributeNames();
		while(keys.hasMoreElements()) {
			String name = (String)keys.nextElement();
			if(name.endsWith("background")) {
				properties.put(name, colorToHex(previewPanel.getBackground()));
			}
			else if(name.endsWith("foreground")) {
				properties.put(name, colorToHex(textPreview.getForeground()));
			}
			else if(name.endsWith("family")) {
				properties.put(name, newFont.getFamily());
			}
			else if(name.endsWith("bold")) {
				properties.put(name, newFont.isBold()? "true": "false");
			}
			else if(name.endsWith("italic")) {
				properties.put(name, newFont.isItalic()? "true": "false");
			}
		}
		initAttributes[index] = new SimpleAttributeSet(currentAttributes[index]);
	}
	
	private void initAttributes() {
		initAttributes = new SimpleAttributeSet[4];
		currentAttributes = new SimpleAttributeSet[4];
		defaultAttributes = new SimpleAttributeSet[4];
		for(int i=0; i<4; i++) {
			initAttributes[i] = new SimpleAttributeSet();
			defaultAttributes[i] = new SimpleAttributeSet();
		}
		String[] keys = AppProperties.KEYS;
		for(int i=0; i<keys.length; i++) {
			if(keys[i].startsWith("main")) initAttributes[0].addAttribute(keys[i], properties.getProperty(keys[i]));
			else if(keys[i].startsWith("welcome")) initAttributes[1].addAttribute(keys[i], properties.getProperty(keys[i]));
			else if(keys[i].startsWith("table")) initAttributes[2].addAttribute(keys[i], properties.getProperty(keys[i]));
			else if(keys[i].startsWith("highlight")) initAttributes[3].addAttribute(keys[i], properties.getProperty(keys[i]));
		}
		for(int i=0; i<4; i++) {
			currentAttributes[i] = new SimpleAttributeSet(initAttributes[i]);
		}
		Properties defaults = new Properties();
		File def = new File("defaults.xml");
		if(def.isFile()) {
			try(FileInputStream fis = new FileInputStream(def)){
				defaults.loadFromXML(fis);
			} catch (IOException e) { e.printStackTrace(); }
		}
		String[] defKeys = AppProperties.KEYS;
		for(int i=0; i<defKeys.length; i++) {
			if(defKeys[i].startsWith("main")) defaultAttributes[0].addAttribute(defKeys[i], defaults.getProperty(keys[i]));
			else if(defKeys[i].startsWith("welcome")) defaultAttributes[1].addAttribute(defKeys[i], defaults.getProperty(keys[i]));
			else if(defKeys[i].startsWith("table")) defaultAttributes[2].addAttribute(defKeys[i], defaults.getProperty(keys[i]));
			else if(defKeys[i].startsWith("highlight")) defaultAttributes[3].addAttribute(defKeys[i], defaults.getProperty(keys[i]));
		}	
	}
	
	public String colorToHex(Color color) { 
		String hex = Integer.toHexString(color.getRGB() & 0xffffff);
	    while(hex.length() < 6) {
	    	hex = "0" + hex;
	    }
	    return hex = "#" + hex;
	}
	
	class ColorBox implements Icon {
		int size;
		public ColorBox(int size) { this.size = size; }
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(newColor);
			g.fillRect(x, y, getIconWidth(), getIconHeight());
		}
		@Override
		public int getIconWidth() { return size; }
		@Override
		public int getIconHeight() { return size; }
	}

}
