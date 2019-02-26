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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Appearance extends JPanel implements ChangeListener, ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	private CollectionsApp parent;
	private AppProperties properties;
	private JPanel previewPanel;
	private JList<String> list;
	private JColorChooser colorChooser;
	private JLabel preview, colorPreview;
	private JButton background, foreground;
	private JCheckBox bold, italic;
	private JTextField fontSize;
	private JComboBox<String> fontChooser;
	private Color newColor;
	private Font newFont;
	
	
	public Appearance(CollectionsApp app, AppProperties props) {

		parent = app;
		properties = props;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(colorChooserInit());
		add(fontPanelInit());
	}
	
	private JPanel colorChooserInit() {
		
		JPanel color = new JPanel();
		color.setLayout(new BorderLayout());
		colorChooser = new JColorChooser();
		colorChooser.setPreviewPanel(previewPanelInit());
		colorChooser.getSelectionModel().addChangeListener(this);
		newColor = colorChooser.getColor();
		color.add(colorChooser, BorderLayout.CENTER);
		return color;
	}
	
	private JPanel previewPanelInit() {
		
		JPanel west = new JPanel();
		west.setLayout(new BorderLayout());
		west.setOpaque(false);
		String[] elements = new String[] {"Application's Frame", "Welcome Screen", "Table", "Highlight Style"};
		list = new JList<>(elements);
		list.setSelectedIndex(0);
		list.setPreferredSize(new Dimension(120, 40));
		list.addListSelectionListener(this);
		JScrollPane listScroll = new JScrollPane(list);
		west.add(listScroll, BorderLayout.CENTER);
		
		style = sc.getStyle(styleNames[0]);
		preview = new JLabel("sample text...", JLabel.CENTER);
		preview.setPreferredSize(new Dimension(340, 40));
		preview.setFont(sc.getFont(style));
		preview.setForeground(sc.getForeground(style));
		background = new JButton("Set Background");
		foreground = new JButton("Set Font Color");
		background.addActionListener(this);
		foreground.addActionListener(this);
		colorPreview = new JLabel(new ColorBox(50));
		
		JPanel east = new JPanel();
		east.setOpaque(false);
		east.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1; c.gridy = 0;
		east.add(foreground, c);
		c.gridx = 1; c.gridy = 1;
		east.add(background, c);
		c.gridx = 0; c.gridy = 0;
		c.gridheight = 2; c.insets = new Insets(0,0,0,20);
		east.add(colorPreview, c);
		
		previewPanel = new JPanel();
		previewPanel.setLayout(new BorderLayout());
		previewPanel.setPreferredSize(new Dimension(630, 85));
		previewPanel.setBackground(sc.getBackground(style));
		previewPanel.add(preview, BorderLayout.CENTER);
		previewPanel.add(west, BorderLayout.LINE_START);
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
		Font[] fonts = e.getAllFonts();
		String[] fontNames = new String[fonts.length];
		for(int i=0; i<fonts.length; i++) fontNames[i] = fonts[i].getFontName();
		
		fontChooser = new JComboBox<>(fontNames);
		fontChooser.setActionCommand("Font Chooser");
		fontChooser.addActionListener(this);
		fontSize = new JTextField(2);
		fontSize.setHorizontalAlignment(SwingConstants.TRAILING);
		fontSize.setActionCommand("Font Size");
		fontSize.addActionListener(this);
		bold = new JCheckBox("Bold");
		bold.setFont(getFont().deriveFont(Font.BOLD));
		bold.addActionListener(this);
		italic = new JCheckBox("Italic");
		italic.setFont(getFont().deriveFont(Font.ITALIC));
		italic.addActionListener(this);
		newFont = sc.getFont(style);
		updateFontSettings();
		
		combo.add(new JLabel("Choose font: "));
		combo.add(fontChooser);
		text.add(new JLabel("Font size: "));
		text.add(fontSize);
		fontSettings.add(combo);
		fontSettings.add(text);
		fontSettings.add(bold);
		fontSettings.add(italic);
		bottomPanel.add(fontSettings);
		return bottomPanel;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equals("Set Background")) {
			previewPanel.setBackground(newColor);
			previewPanel.repaint();
			StyleConstants.setBackground(style, newColor);
		}
		else if(e.getActionCommand().equals("Set Font Color")) {
			preview.setForeground(newColor);
			preview.repaint();
			StyleConstants.setForeground(style, newColor);
		}
		else if(e.getActionCommand().equals("Font Chooser")) {
			String fontName = (String) fontChooser.getSelectedItem();
			int size = Integer.parseInt(fontSize.getText());
			bold.setSelected(false);
			italic.setSelected(false);
			if( fontName.toLowerCase().contains("bold") ) {
				bold.setSelected(true);
				bold.setEnabled(false);
			} 
			else {
				bold.setEnabled(true);
			}
			if( fontName.toLowerCase().contains("italic") ) {
				italic.setSelected(true);
				italic.setEnabled(false);
			}
			else {
				italic.setEnabled(true);
			}
			newFont = new Font(fontName, (bold.isSelected()? Font.BOLD: 0) | (italic.isSelected()? Font.ITALIC: 0), size);
			preview.setFont(newFont);
			preview.repaint();
			StyleConstants.setFontFamily(style, fontName);
			StyleConstants.setBold(style, bold.isSelected());
			StyleConstants.setItalic(style, italic.isSelected());
		}
		else if(e.getActionCommand().equals("Font Size")) {
			float size = Float.parseFloat(fontSize.getText());
			newFont = preview.getFont().deriveFont(size);
			preview.setFont(newFont);
			preview.repaint();
			StyleConstants.setFontSize(style, (int)size);
		}
		else if(e.getActionCommand().equals("Bold")) {
			newFont = preview.getFont().deriveFont((bold.isSelected()? Font.BOLD: 0) | (italic.isSelected()? Font.ITALIC: 0));
			preview.setFont(newFont);
			preview.repaint();
			StyleConstants.setBold(style, bold.isSelected());
		}
		else if(e.getActionCommand().equals("Italic")) {
			newFont = preview.getFont().deriveFont((italic.isSelected()? Font.ITALIC: 0) | (bold.isSelected()? Font.BOLD: 0));
			preview.setFont(newFont);
			preview.repaint();
			StyleConstants.setItalic(style, italic.isSelected());
		}
	}
	
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		style = sc.getStyle(styleNames[list.getSelectedIndex()]);
		updatePreview();
		updateFontSettings();
	}

	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		newColor = colorChooser.getSelectionModel().getSelectedColor();
		colorPreview.repaint();
	}
	
	
	private void updatePreview() {
		
		previewPanel.setBackground(sc.getBackground(style));
		preview.setForeground(sc.getForeground(style));
		newFont = sc.getFont(style);
		preview.setFont(newFont);
		previewPanel.repaint();
		preview.repaint();	
	}
	
	
	private void updateFontSettings() {
		
		fontSize.setText(String.valueOf(newFont.getSize()));
		fontChooser.setSelectedItem((String) newFont.getFontName());
		bold.setSelected(newFont.isBold());
		italic.setSelected(newFont.isItalic());
		if(newFont.getFontName().toLowerCase().equals("bold")) bold.setEnabled(false);
		if(newFont.getFontName().toLowerCase().equals("italic")) italic.setEnabled(false);
	}
	

	void apply() {
		
		parent.updateAppearance(list.getSelectedValue());
		saveAttributes();
	}
	
	
	void setDefault() {
		
		Style defStyle = (Style) style.getResolveParent();
		StyleConstants.setBackground(style, sc.getBackground(defStyle));
		StyleConstants.setForeground(style, sc.getForeground(defStyle));
		StyleConstants.setFontFamily(style, StyleConstants.getFontFamily(defStyle));
		StyleConstants.setFontSize(style, StyleConstants.getFontSize(defStyle));
		StyleConstants.setBold(style, StyleConstants.isBold(defStyle));
		StyleConstants.setItalic(style, StyleConstants.isItalic(defStyle));
		updatePreview();
		updateFontSettings();
	}
	
	
	private void saveAttributes() {
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(style.getName()+".set")));
			StyleContext.writeAttributeSet(out, style);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
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
