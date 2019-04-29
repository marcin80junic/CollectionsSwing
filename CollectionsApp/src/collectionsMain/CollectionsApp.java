package collectionsMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import collectionsMain.collectableItems.AbstractItem;
import collectionsMain.collectableItems.AudioCD;
import collectionsMain.collectableItems.Book;
import collectionsMain.collectableItems.Collectable;
import collectionsMain.collectableItems.Game;
import collectionsMain.collectableItems.Movie;
import collectionsMain.fileDialog.CollectionsFileFilter;
import collectionsMain.fileDialog.CollectionsThumbNailView;
import collectionsMain.fileDialog.ImportAccessory;
import collectionsMain.table.*;
import mediaBooks.*;

public class CollectionsApp extends JFrame implements ListSelectionListener  {
	
	private static final long serialVersionUID = 1L;
	UIDefaults defaults;
	private DataBase<? extends Collectable<? extends AbstractItem>> dataBase;
	private DataBase<Book> booksCollection;
	private DataBase<Game> gamesCollection;
	private DataBase<Movie> moviesCollection;
	private DataBase<AudioCD> musicCollection;
	private AppProperties properties;
	private String booksFilePath, gamesFilePath, moviesFilePath, musicFilePath;
	private static Color mainBackground, mainForeground, welcomeBackground, welcomeForeground, tableBackground, tableForeground, 
		  	highlightBackground, highlightForeground;
	private static Font mainFont, welcomeFont, tableFont, highlightFont;
	private JInternalFrame colorChooser;
	private JDialog settings;
	private JToggleButton[] collectionsButtons;
	private ButtonGroup collectionsGroup;
	private JButton[] navigationButtons;
	private JTable table, comboHeaderTable;
	private TableModelCollection<? extends Collectable<? extends AbstractItem>> tableModel;
	private TableMouseListener tableMouseListener, headerMouseListener;
	private TableModelComboHeader comboHeaderModel;
	private int[] columnWidths;
	private JPopupMenu popupMenu;
	private JToolBar mainToolBar, detailToolBar;
	private JPanel welcomePane, shortcutPane, westPane, centralPane, btnPane, navigationPane, eastPane, searchPane, detailPanel,
			detailItemPanel, toolbarPane, toolPane, pane;
	private boolean homeFlag;
	private Border lowered, raised;
	private CollectionsAction addAction, editAction, removeAction, upAction, downAction, clearAction, resetAction, findAction,
			displayPopupMenuAction,	exitAction, helpAction,
			importAction, exportAction, settingsAction, homeAction, booksAction, audioAction, gamesAction, moviesAction;
	public CollectionsAction saveAction, saveAllAction;
	private JLabel lblSearch, titleLine1, titleLine2, detailTitle;
	private TimeLabel lblTime;
	private JTextPane titleText;
	private static String search = "";
	private SearchField tfSearch;
	
	CollectionsApp() {
		super ("Collections");
		setPreferredSize(new Dimension(958, 500));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		homeFlag = true;
		propertiesInit();
		lookAndFeelInit();
		dataBase = new DataBase<>();
		actionsInit();
		
		westPaneInit();
		centralPaneInit();
		navigationPaneInit();
		eastPaneInit();
		setJMenuBar(createMenuBar());
		
		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WEST, westPane, 5, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, westPane, 5, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, westPane, 5, SpringLayout.SOUTH, getContentPane());
		
		layout.putConstraint(SpringLayout.WEST, centralPane, 5, SpringLayout.EAST, westPane);
		layout.putConstraint(SpringLayout.NORTH, centralPane, 5, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, centralPane, 5, SpringLayout.SOUTH, getContentPane());
		
		layout.putConstraint(SpringLayout.WEST, navigationPane, 0, SpringLayout.EAST, centralPane);
		layout.putConstraint(SpringLayout.NORTH, navigationPane, 5, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, navigationPane, 5, SpringLayout.SOUTH, getContentPane());
		
		layout.putConstraint(SpringLayout.WEST, eastPane, 0, SpringLayout.EAST, navigationPane);
		layout.putConstraint(SpringLayout.NORTH, eastPane, 9, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, eastPane, 5, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.EAST, eastPane, 5, SpringLayout.EAST, getContentPane());
		
		getContentPane().setLayout(layout);
		getContentPane().setBackground(mainBackground);
		getContentPane().add(westPane);
		getContentPane().add(centralPane);
		getContentPane().add(navigationPane);
		getContentPane().add(eastPane);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void propertiesInit() {
		properties = new AppProperties();
		mainBackground = properties.getColor(AppProperties.MAIN_BACKGROUND);
		mainForeground = properties.getColor(AppProperties.MAIN_FOREGROUND);
		welcomeBackground = properties.getColor(AppProperties.WELCOME_BACKGROUND);
		welcomeForeground = properties.getColor(AppProperties.WELCOME_FOREGROUND);
		tableBackground = properties.getColor(AppProperties.TABLE_BACKGROUND);
		tableForeground = properties.getColor(AppProperties.TABLE_FOREGROUND);
		highlightBackground = properties.getColor(AppProperties.HIGHLIGHT_BACKGROUND);
		highlightForeground = properties.getColor(AppProperties.HIGHLIGHT_FOREGROUND);
		mainFont = properties.getFont(AppProperties.MAIN_FONT_FAMILY);
		welcomeFont = properties.getFont(AppProperties.WELCOME_FONT_FAMILY).deriveFont(30f);
		tableFont = properties.getFont(AppProperties.TABLE_FONT_FAMILY);
		highlightFont = properties.getFont(AppProperties.HIGHLIGHT_FONT_FAMILY);
		booksFilePath = properties.getFilePath(AppProperties.BOOKS_FILE_PATH);
		gamesFilePath = properties.getFilePath(AppProperties.GAMES_FILE_PATH);
		moviesFilePath = properties.getFilePath(AppProperties.MOVIES_FILE_PATH);
		musicFilePath = properties.getFilePath(AppProperties.MUSIC_FILE_PATH);
	}
	
	private void lookAndFeelInit() {
		UIManager.put("PopupMenu.consumeEventOnClose", false);
		UIManager.put("nimbusBase", mainBackground);
		UIManager.put("control", mainBackground);
		UIManager.put("nimbusBlueGrey", mainBackground);
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		    if ("Nimbus".equals(info.getName())) {
		        try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) { e.printStackTrace(); }
		        break;
		    }
		}
		defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", new Painter(tableBackground));
        if(titleText != null) {
        	titleText.putClientProperty("Nimbus.Overrides", defaults);
    	    titleText.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
        }
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu options = new JMenu("Options");
		JMenu help = new JMenu("Help");
		file.add(saveAction).setIcon(null);
		file.add(saveAllAction).setIcon(null);
		file.addSeparator();
		file.add(importAction).setIcon(null);
		file.add(exportAction).setIcon(null);
		file.addSeparator();
		file.add(homeAction).setIcon(null);
		file.add(booksAction).setIcon(null);
		file.add(audioAction).setIcon(null);
		file.add(gamesAction).setIcon(null);
		file.add(moviesAction).setIcon(null);
		file.addSeparator();
		file.add(exitAction);
		edit.add(addAction);
		edit.add(editAction);
		edit.add(removeAction);
		edit.addSeparator();
		edit.add(upAction).setIcon(null);
		edit.add(downAction).setIcon(null);
		edit.addSeparator();
		edit.add(resetAction).setIcon(null);
		options.add(settingsAction).setIcon(null);
		help.add(helpAction);
		menuBar.setPreferredSize(new Dimension(682,21));
		menuBar.add(file); menuBar.add(edit); menuBar.add(options); menuBar.add(help);
		return menuBar;
	}
	
	private void tablePopupMenuInit() {
		popupMenu = new JPopupMenu("Context Menu");
		popupMenu.setBorder(raised);
		CollectionsAction[] actions = {saveAction, saveAllAction, addAction, editAction, removeAction, upAction, downAction, resetAction};
		for(int i=0; i<actions.length; i++) {
			if(i==2 || i==5 || i==7) popupMenu.addSeparator();
			popupMenu.add(actions[i]).setIcon(null);
		}
		popupMenu.pack();
	}
	
	private void westPaneInit() {
		westPane = new JPanel();
		westPane.setLayout(new BoxLayout(westPane, BoxLayout.Y_AXIS));
		Dimension maxBtn = new Dimension(65, 60);
		lowered = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		raised = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		collectionsButtons = new JToggleButton[]{new JToggleButton(homeAction), new JToggleButton(booksAction),
				new JToggleButton(audioAction), new JToggleButton(moviesAction), new JToggleButton(gamesAction)};
		collectionsGroup = new ButtonGroup();
		for(int i=0; i<collectionsButtons.length; i++) {
			collectionsButtons[i].addMouseListener(new ButtonMouseListener(collectionsButtons[i], mainBackground));
			collectionsButtons[i].addChangeListener((l) -> {
				JToggleButton btn = (JToggleButton)l.getSource();
				btn.setBorder(btn.isSelected()? lowered: raised);
			});
			collectionsButtons[i].setHideActionText(true);
			collectionsButtons[i].setBorder(raised);
			collectionsGroup.add(collectionsButtons[i]);
			westPane.add(collectionsButtons[i]).setMaximumSize(maxBtn);
			westPane.add((i == (collectionsButtons.length - 1))? Box.createVerticalGlue(): Box.createRigidArea(new Dimension(0,5)));
		}
		collectionsGroup.setSelected(collectionsButtons[0].getModel(), true);
	}
	
	private void centralPaneInit() {
		centralPane = new JPanel();
		centralPane.setLayout(new BorderLayout());
		centralPane.setPreferredSize(new Dimension(825, 500));
		welcomePaneInit();
		toolbarPaneInit();
		btnPaneInit();
		centralPaneUpdate();
	}
	
	private void welcomePaneInit() {	
		welcomePane = new JPanel();
		welcomePane.setBackground(welcomeBackground);
		welcomePane.setBorder(raised);
		welcomePane.setLayout(new BorderLayout());
		JPanel titlePane = new JPanel();
		titlePane.setOpaque(false);
		titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.PAGE_AXIS));
		titleLine1 = new JLabel("Collections Application");
		titleLine2 = new JLabel("Choose one of following actions:");
		titleLine1.setFont(welcomeFont);
		titleLine2.setFont(welcomeFont);
		titleLine1.setForeground(welcomeForeground);	
		titleLine2.setForeground(welcomeForeground);	
		titleLine1.setAlignmentX(0.5f);
		titleLine2.setAlignmentX(0.5f);
		titlePane.add(Box.createVerticalStrut(20));
		titlePane.add(titleLine1);
		titlePane.add(Box.createVerticalStrut(5));
		titlePane.add(titleLine2);
		shortcutPane = new JPanel();
		shortcutPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		shortcutPane.setOpaque(false);	
		CollectionsAction[] actions = {importAction, booksAction, audioAction, gamesAction, moviesAction, settingsAction};
		for(int i=0; i<actions.length; i++) {
			JButton btn = new JButton(actions[i]);
			btn.setVerticalTextPosition(JButton.BOTTOM);
			btn.setHorizontalTextPosition(JButton.CENTER);
			btn.setBackground(welcomeBackground);
			btn.setForeground(welcomeForeground);
			btn.setFont(welcomeFont.deriveFont(12f));
			btn.addMouseListener(new ButtonMouseListener(btn, welcomeBackground));
			shortcutPane.add(btn).setPreferredSize(new Dimension(150, 90));
		}
		welcomePane.add(shortcutPane, BorderLayout.CENTER);
		welcomePane.add(titlePane, BorderLayout.PAGE_START);
	}
	
	private void centralPaneUpdate() {
		centralPane.removeAll();
		if(homeFlag) {
			centralPane.add(welcomePane);
			centralPane.add(btnPane, BorderLayout.PAGE_END);
		}
		else {
			centralPane.add(toolbarPane, BorderLayout.PAGE_START);
			centralPane.add(pane, BorderLayout.CENTER);
			centralPane.add(btnPane, BorderLayout.PAGE_END);
		}
		centralPane.revalidate();
		centralPane.repaint();
	}
	
	private void tableInit() {
		table = new JTable(tableModel = dataBase.getTableModel());
		tableModel.addTableModelListener((e) -> {
			int rows = tableModel.getRowCount();
			if(((e.getType()==TableModelEvent.INSERT) && (String.valueOf(rows-1).length() != String.valueOf(rows).length())) ||
			   ((e.getType()==TableModelEvent.DELETE) && (String.valueOf(rows+1).length() != String.valueOf(rows).length())) ||
			   (e.getType()==TableModelEvent.UPDATE)) {
				dataBase.setColumnWidths(rows, columnWidths);
				dataBase.setColumnWidths(rows, columnWidths);
			}
			if((e.getType()==TableModelEvent.UPDATE) && tableModel.isFiltered()) tableUpdate();
			if((e.getType()==TableModelEvent.INSERT) && tableModel.isFiltered()) comboHeaderModel.resetModels();
			saveAction.setEnabled(tableModel.isSaveable());
			saveAllAction.setEnabled(areCollectionsChanged());
			resetAction.setEnabled(tableModel.isChanged());
			detailPaneUpdate((tableModel.getRowCount()==0)? true: table.getSelectionModel().isSelectionEmpty());
		});
		table.setColumnModel(dataBase.getTableColumnModel(table));
		table.getActionMap().put("delete item", removeAction);
		table.getActionMap().put("edit item", editAction);
		table.getActionMap().put("display popupMenu", displayPopupMenuAction);
		table.getActionMap().put("add item", addAction);
		table.getActionMap().put("move up", upAction);
		table.getActionMap().put("move down", downAction);
		table.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete item");
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "edit item");
		table.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "display popupMenu");
		table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), "add item");
		table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK), "move up");
		table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK), "move down");
		tablePopupMenuInit();
		table.setComponentPopupMenu(popupMenu);
		if(tableMouseListener != null) table.removeMouseListener(tableMouseListener);
		table.addMouseListener(tableMouseListener = new TableMouseListener(this, table, dataBase));
		table.setBackground(tableBackground);
		table.setDefaultRenderer(String.class, new TableRendererCollection());
		table.setFillsViewportHeight(true);
		table.setSelectionMode(0);
		table.setTableHeader(null);
		table.getSelectionModel().addListSelectionListener(this);
		JScrollPane tableScroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tableScroll.getVerticalScrollBar().setPreferredSize(new Dimension(20, 0));
		tableScroll.setBorder(null);
		comboHeaderModel = new TableModelComboHeader(this, table);
		comboHeaderTable = new JTable(comboHeaderModel);
		comboHeaderTable.setColumnModel(dataBase.getComboHeaderTableColumnModel(comboHeaderTable));
		comboHeaderTable.setBackground(tableBackground);
		comboHeaderTable.setDefaultRenderer(String.class, comboHeaderModel.new Renderer());
		comboHeaderTable.setDefaultEditor(String.class, comboHeaderModel.new Editor());
		comboHeaderTable.setRowHeight(22);
		comboHeaderTable.setRowSelectionAllowed(false);
		comboHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		dataBase.setColumnWidths(table.getRowCount(), columnWidths);
		comboHeaderTable.getTableHeader().setReorderingAllowed(false);
		if(headerMouseListener != null) comboHeaderTable.getTableHeader().removeMouseListener(headerMouseListener);
		comboHeaderTable.getTableHeader().addMouseListener(headerMouseListener = new TableMouseListener(this, comboHeaderTable, dataBase));
		JScrollPane comboHeaderScroll = new JScrollPane(comboHeaderTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, 
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		comboHeaderScroll.setBorder(null);
		JViewport comboHeaderView = new JViewport();
		comboHeaderView.setView(comboHeaderScroll);
		comboHeaderView.setPreferredSize(new Dimension(805, 46));
		pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.add(comboHeaderView, BorderLayout.PAGE_START);
		pane.add(tableScroll, BorderLayout.CENTER);
	}	
	
	public void tableUpdate() {
		if(tableModel.getRowCount()==0) comboHeaderModel.resetModels(); 
		else {
			comboHeaderModel.resetComboFlags();
			comboHeaderModel.updateComboLists();
		}
	}
	
	private void toolbarPaneInit() {
		toolbarPane = new JPanel();
		toolbarPane.setLayout(new BorderLayout());
		toolbarPane.setPreferredSize(new Dimension(825, 35));
		mainToolBar = new JToolBar();
		mainToolBar.setBorderPainted(false);
		mainToolBar.setFloatable(false);
		toolbarPane.add(mainToolBar, BorderLayout.CENTER);
		CollectionsAction[] actions = {importAction, exportAction, saveAction, saveAllAction, settingsAction};
		for(int i=0; i<actions.length; i++) {
			if(i==2 || i==4) mainToolBar.addSeparator(new Dimension(15, 30));
			JButton btn = new JButton(actions[i]);
			btn.setIcon( (Icon) actions[i].getValue(Action.SMALL_ICON) );
			btn.setText("");
			btn.addMouseListener(new ButtonMouseListener(btn, mainBackground));
			mainToolBar.add(btn).setPreferredSize(new Dimension(32, 32));
		}
		JButton b = new JButton("keys");
		b.addActionListener((e) ->{
			if(colorChooser == null) {
				colorChooser = new JInternalFrame();
				colorChooser.setContentPane(new MiniColorChooser());
				Point p = new Point(CollectionsApp.this.getBounds().x + b.getBounds().x, CollectionsApp.this.getBounds().y + b.getBounds().y);
				colorChooser.setBounds(p.x, p.y-20, 0, 0);
				colorChooser.pack();
				colorChooser.setVisible(true);
				colorChooser.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent fe) {
						colorChooser.setVisible(false);
					}
				});
			} else colorChooser.setVisible(true);
		});
		mainToolBar.add(b);
	}	
	
	private void btnPaneInit() {
		btnPane = new JPanel();
		btnPane.setOpaque(false);
		btnPane.setPreferredSize(new Dimension(200, 65));
		btnPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
	}
	
	private void btnPaneUpdate() {
		btnPane.removeAll();
		if(!homeFlag) {
			Dimension btnDim = new Dimension(135, 26);
			CollectionsAction[] actions = {addAction, editAction, removeAction, clearAction, resetAction};
			for(int i=0; i<actions.length; i++) {
				JButton btn = new JButton(actions[i]);
				btn.setForeground(mainForeground);
				btn.setFont(mainFont.deriveFont(12f));
				btn.addMouseListener(new ButtonMouseListener(btn, mainBackground));
				btnPane.add(btn).setPreferredSize(btnDim);
			}
		}
	}
	
	private void navigationPaneInit() {
		navigationPane = new JPanel();
		navigationPane.setLayout(new BoxLayout(navigationPane, BoxLayout.PAGE_AXIS));
		navigationPane.setPreferredSize(new Dimension(48, 500));
		navigationButtons = new JButton[] {new JButton(upAction), new JButton(downAction)};
		for(int i=0; i<navigationButtons.length; i++) {
			navigationButtons[i].setHideActionText(true);
			navigationButtons[i].addMouseListener(new ButtonMouseListener(navigationButtons[i], mainBackground));
		}
	}
	
	private void navigationPaneUpdate() {
		navigationPane.removeAll();
		if(!homeFlag) {
			navigationPane.add(Box.createVerticalGlue());
			for(int i=0; i<navigationButtons.length; i++) {
				navigationPane.add(navigationButtons[i]);
				if(i != navigationButtons.length-1) navigationPane.add(Box.createRigidArea(new Dimension(0, 20)));
			}
			navigationPane.add(Box.createVerticalGlue());
		}
		navigationPane.revalidate();
		navigationPane.repaint();
	}
	
	private void eastPaneInit() {
		eastPane = new JPanel();
		eastPane.setLayout(new BorderLayout());
		searchPane = new JPanel();
		searchPane.setLayout(new BoxLayout(searchPane, BoxLayout.LINE_AXIS));
		searchPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
		searchPane.setOpaque(false);
		lblSearch = new JLabel("Search for: ");
		lblSearch.setFont(mainFont);
		lblSearch.setLabelFor(tfSearch);
		lblSearch.setDisplayedMnemonic('h');
		tfSearch = new SearchField(this, (Icon)findAction.getValue(Action.SMALL_ICON), "Search for...");
		lblTime = new TimeLabel();
		lblTime.setBackground(CollectionsApp.getMainBackground().darker());
		searchPane.add(Box.createHorizontalGlue());
		searchPane.add(lblSearch).setForeground(mainForeground);
		searchPane.add(tfSearch);
		searchPane.add(Box.createHorizontalStrut(10));
		searchPane.add(lblTime);
		searchPane.add(Box.createHorizontalGlue());
		detailPanel = new JPanel(new BorderLayout());
		detailPanel.setBorder(raised);
		detailTitle = new JLabel("Home Screen", JLabel.CENTER);
		detailTitle.setFont(mainFont.deriveFont(Font.BOLD, 25f));
		detailTitle.setForeground(mainForeground);
		detailItemPanel = new JPanel(new BorderLayout());
		detailItemPanel.setBackground(welcomeBackground);
		titleText = new JTextPane();
		titleText.setEditable(false);
		titleText.setPreferredSize(new Dimension(200, 200));
		titleText.putClientProperty("Nimbus.Overrides", defaults);
	    titleText.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
		detailItemPanel.add(titleText, BorderLayout.PAGE_START);
		detailPanel.add(detailTitle, BorderLayout.PAGE_START);
		detailPanel.add(new JScrollPane(detailItemPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
		toolPane = new JPanel();
		toolPane.setLayout(new BorderLayout());
		toolPane.setPreferredSize(new Dimension(200, 65));
		detailToolBar = new JToolBar();
		eastPane.add(searchPane, BorderLayout.PAGE_START);
		eastPane.add(detailPanel, BorderLayout.CENTER);
		eastPane.add(toolPane, BorderLayout.PAGE_END);
		eastPane.add(new JPanel(), BorderLayout.LINE_END);
		detailPaneUpdate(true);
	}
	
	private void detailPaneUpdate(boolean selectionEmpty) {
		detailItemPanel.removeAll();
		if(homeFlag) {
			toolPane.removeAll();
		}
		else {
			toolPane.add(detailToolBar);
		}
		detailTitle.setText(homeFlag? "Welcome...": dataBase.getName()+"s Collection");
		if(!homeFlag && selectionEmpty) {
			CollectionsTree tree = new CollectionsTree(tableModel);
			tree.setBackground(tableBackground);
			detailItemPanel.add(tree);
		}
		else if(!homeFlag && !selectionEmpty) {
			BookStyledDocument bsd = new BookStyledDocument(dataBase, table, titleText.getWidth());
			titleText.setStyledDocument(bsd);
			detailItemPanel.add(titleText);
		}
		detailItemPanel.revalidate();
		detailItemPanel.repaint();
		toolPane.repaint();
	}
	
	private void actionsInit() {
		ImageIcon upIcon = createImageIcon("/icons/ArrowUp.png", "arrow up icon");
		ImageIcon downIcon = createImageIcon("/icons/ArrowDown.png", "arrow down icon");
		ImageIcon saveIcon = createImageIcon("/icons/Save.png", "save icon");
		ImageIcon saveAllIcon = createImageIcon("/icons/SaveAll.png", "save all icon");
		ImageIcon searchIcon = createImageIcon("/icons/search_16.png", "search icon");
		ImageIcon homeIcon = createImageIcon("/icons/Home.png", "home icon");
		ImageIcon booksIcon = createImageIcon("/icons/Books.png", "books icon");
		ImageIcon audioIcon = createImageIcon("/icons/Music.png", "music icon");
		ImageIcon gamesIcon = createImageIcon("/icons/Games.png", "games icon");
		ImageIcon moviesIcon = createImageIcon("/icons/Movies.png", "movies icon");
		ImageIcon smallImport = createImageIcon("/icons/import_24.png", "import small");
		ImageIcon bigImport = createImageIcon("/icons/import_48.png", "import big");
		ImageIcon exportIcon = createImageIcon("/icons/export_24.png", "export small");
		ImageIcon smallSettings = createImageIcon("/icons/settings_24.png", "settings small");
		ImageIcon bigSettings = createImageIcon("/icons/settings_48.png", "settings big");
		homeAction = new CollectionsAction("Home Screen", homeIcon, null, "home", 'H', true);
		homeAction.putValue(Action.SHORT_DESCRIPTION, "Welcome screen");
		booksAction = new CollectionsAction("Books Collection", booksIcon, null, "books", 'B', true);
		booksAction.putValue(Action.SHORT_DESCRIPTION, "Books collection");
		audioAction = new CollectionsAction("Music Collection", audioIcon, null, "music", 'M', true);
		audioAction.putValue(Action.SHORT_DESCRIPTION, "Audio collection");
		gamesAction = new CollectionsAction("Games Collection", gamesIcon, null, "games", 'G', true);
		gamesAction.putValue(Action.SHORT_DESCRIPTION, "Games collection");
		moviesAction = new CollectionsAction("Movies Collection", moviesIcon, null, "movies", 'V', true);
		moviesAction.putValue(Action.SHORT_DESCRIPTION, "Movies collection");
		addAction = new CollectionsAction("Add", null, null, "add", 'A', false);
		editAction = new CollectionsAction("Edit", null, null, "edit", KeyEvent.VK_ENTER, false);
		removeAction = new CollectionsAction("Remove", null, null, "remove", KeyEvent.VK_DELETE, false);
		saveAction = new CollectionsAction("Save", null, saveIcon, "save", 'S', false);
		saveAction.putValue(Action.SHORT_DESCRIPTION, "Save changes in current collection");
		saveAllAction = new CollectionsAction("Save All", null, saveAllIcon, "saveAll", 'L', false);
		saveAllAction.putValue(Action.SHORT_DESCRIPTION, "Save changes in all collections");
		clearAction = new CollectionsAction("Clear Selection", null, null, "clear", 'C', false);
		clearAction.putValue(Action.SHORT_DESCRIPTION, "Clear current selection");
		resetAction = new CollectionsAction("Reset", null, null, "reset", 'R', false);
		resetAction.putValue(Action.SHORT_DESCRIPTION, "Reset current collection");
		findAction = new CollectionsAction("Find...", null, searchIcon, "search", 'F', true);
		findAction.putValue(Action.SHORT_DESCRIPTION, "Search for..");
		upAction = new CollectionsAction("Move Up", upIcon, null, "up", KeyEvent.VK_UP, false);
		downAction = new CollectionsAction("Move Down", downIcon, null, "down", KeyEvent.VK_DOWN, false);
		importAction = new CollectionsAction("Import Collection", bigImport, smallImport, "import", 'I', true);
		importAction.putValue(Action.SHORT_DESCRIPTION, "import collection from a file");
		exportAction = new CollectionsAction("Export Collection", null, exportIcon, "export", 'E', false);
		exportAction.putValue(Action.SHORT_DESCRIPTION, "Export Collection to a file");
		settingsAction = new CollectionsAction("Settings", bigSettings, smallSettings, "settings", 'N', true);
		settingsAction.putValue(Action.SHORT_DESCRIPTION, "Change application's settings");
		displayPopupMenuAction = new CollectionsAction("display popupMenu", null, null, "popupMenu", 'Y', true);
		helpAction = new CollectionsAction("Help", null, null, "help", 'H', true);
		exitAction = new CollectionsAction("Exit", null, null, "exit", 'X', true);
	}
	
	private void actionsUpdate() {
		addAction.putValue(Action.NAME, "Add "+ dataBase.getName());
		addAction.putValue(Action.SHORT_DESCRIPTION, "Add new "+dataBase.getName());
		addAction.setEnabled(!homeFlag);
		editAction.putValue(Action.NAME, "Edit "+dataBase.getName());
		editAction.putValue(Action.SHORT_DESCRIPTION, "Edit selected "+dataBase.getName());
		editAction.setEnabled(!table.getSelectionModel().isSelectionEmpty());
		removeAction.putValue(Action.NAME, "Remove "+dataBase.getName());
		removeAction.putValue(Action.SHORT_DESCRIPTION, "Remove selected "+dataBase.getName());
		removeAction.setEnabled(!table.getSelectionModel().isSelectionEmpty());
		upAction.putValue(Action.SHORT_DESCRIPTION, "Move selected "+dataBase.getName()+" up");
		upAction.setEnabled(false);
		downAction.putValue(Action.SHORT_DESCRIPTION, "Move selected "+dataBase.getName()+" down");
		downAction.setEnabled(false);
		exportAction.putValue(Action.SHORT_DESCRIPTION, "Export "+(!homeFlag? dataBase.getName(): "")+" collection to a file");
		exportAction.setEnabled(!homeFlag);
		clearAction.setEnabled(table.getSelectionModel().isSelectionEmpty()? false: true);
		resetAction.setEnabled(tableModel.isChanged()); 
		saveAction.setEnabled(tableModel.isSaveable());
		saveAllAction.setEnabled(areCollectionsChanged());
	}
	
	private void switchCollection() {
		search = "";
		homeFlag = collectionsGroup.getSelection().getActionCommand().equals("home");
		if(!homeFlag) {
			tableInit();
			actionsUpdate();
		}
		btnPaneUpdate();
		centralPaneUpdate();
		navigationPaneUpdate();
		detailPaneUpdate(table.getSelectionModel().isSelectionEmpty());
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		editAction.setEnabled(!lsm.isSelectionEmpty());
		removeAction.setEnabled(!lsm.isSelectionEmpty());
		clearAction.setEnabled(!lsm.isSelectionEmpty());
		upAction.setEnabled(!lsm.isSelectionEmpty());
		downAction.setEnabled(!lsm.isSelectionEmpty());
		detailPaneUpdate(lsm.isSelectionEmpty());
	}
	
	public void searchFor() {
		search = tfSearch.getText().trim();
		if(!search.equals("") && !homeFlag) {
			table.clearSelection();
			tableModel.searchFor(search);
			tfSearch.selectAll();
		}
	}
	
	public DataBase<? extends Collectable<? extends AbstractItem>> getDataBase(){ return dataBase; }
	public AppProperties getProperties() { return properties; }
	public int[] getColumnWidths() { return columnWidths; }
	public void setColumnWidths(int[] widths) { columnWidths = widths; }
	public static String getSearchText() { return search;	}
	public static Color getMainBackground() { return mainBackground; }
	public static Color getMainForeground() { return mainForeground; }
	public static Font getMainFont() { return mainFont; }
	public static Color getTableBackground() { return tableBackground; }
	public static Color getTableForeground() { return tableForeground; }
	public static Font getTableFont() { return tableFont; }
	public static Color getHighlightBackground() { return highlightBackground; }
	public static Color getHighlightForeground() { return highlightForeground; }
	public static Font getHighlightFont() { return highlightFont; }
	
	private boolean areCollectionsChanged() {
		boolean books, audio, games, movies;
		books = audio = games = movies = false;
		if(booksCollection != null) books = booksCollection.getTableModel().isSaveable();
		if(musicCollection != null) audio = musicCollection.getTableModel().isSaveable();
		if(gamesCollection != null) games = gamesCollection.getTableModel().isSaveable();
		if(moviesCollection != null) movies = moviesCollection.getTableModel().isSaveable();
		return (books || audio || games || movies);
	}
	
	private void saveAllCollections() {
		if(booksCollection != null) {
			if(booksCollection.getTableModel().isSaveable()) {
				booksCollection.getTableModel().saveDataBase();
			}
		}
		if(musicCollection != null) {
			if(musicCollection.getTableModel().isSaveable()) {
				musicCollection.getTableModel().saveDataBase();
			}
		}
		if(gamesCollection != null) {
			if(gamesCollection.getTableModel().isSaveable()) {
				gamesCollection.getTableModel().saveDataBase();
			}
		}
		if(moviesCollection != null) {
			if(moviesCollection.getTableModel().isSaveable()) {
				moviesCollection.getTableModel().saveDataBase();
			}
		}
		saveAction.setEnabled(false);
		saveAllAction.setEnabled(false);
	}
	
	public void clearSelection() {
		tfSearch.setText("");
		search = "";
		table.clearSelection();
		detailPaneUpdate(true);
	}
	
	public ImageIcon createImageIcon(String path, String description) {
		URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	void updateAppearance(String element) {	
		propertiesInit();
		lookAndFeelInit();
		SwingUtilities.updateComponentTreeUI(this);
		SwingUtilities.updateComponentTreeUI(settings);
		if(element.equals("Application's Frame")) {
			getContentPane().setBackground(mainBackground);
			updateButtonListeners(westPane.getComponents(), mainBackground, null, null);
			updateButtonListeners(btnPane.getComponents(), mainBackground, mainForeground, mainFont.deriveFont(12f));
			updateButtonListeners(navigationButtons, mainBackground, null, null);
			lblSearch.setFont(mainFont);
			lblSearch.setForeground(mainForeground);
			detailTitle.setFont(mainFont.deriveFont(Font.BOLD, 25f));
			detailTitle.setForeground(mainForeground);
			centralPaneUpdate();
		}
		else if(element.equals("Welcome Screen")) {
			welcomePane.setBackground(welcomeBackground);
			titleLine1.setFont(welcomeFont);
			titleLine2.setFont(welcomeFont);
			titleLine1.setForeground(welcomeForeground);
			titleLine2.setForeground(welcomeForeground);
			detailItemPanel.setBackground(welcomeBackground);
			updateButtonListeners(shortcutPane.getComponents(), welcomeBackground, welcomeForeground, welcomeFont.deriveFont(12f));
		}
		else if(element.equals("Table")) {
			if(!homeFlag) {
				table.setBackground(tableBackground);
				comboHeaderTable.setBackground(tableBackground);
			}
		}
		else if(element.equals("Highlight Style")) {
			
		}
		if(!homeFlag) {
			tableInit();
			centralPaneUpdate();
		}
	}
	
	private void updateButtonListeners(Component[] buttons, Color background, Color foreground, Font font) {
		for(int i=0; i<buttons.length; i++) {
			buttons[i].setForeground(foreground);
			buttons[i].setFont(font);
			MouseListener[] list = buttons[i].getMouseListeners();
			for(int j=0; j<list.length; j++) {
				if(list[j].toString().contains("ButtonMouseListener")) {
					((ButtonMouseListener) list[j]).setBackground(background);
				}
			}
		}
	}
	
	private void displayExportDialog() {
		JFileChooser export = new JFileChooser();
		export.setDialogTitle("Export your "+dataBase.getName()+" Collection");
		export.setCurrentDirectory(new File("."));
		export.setSelectedFile(new File(dataBase.getFilePath()));
		export.setFileView(new CollectionsThumbNailView(this));
		export.setFileFilter(new CollectionsFileFilter("Collection files *.dat", "dat"));
		int option = export.showDialog(CollectionsApp.this, "Export");
		if(option == JFileChooser.APPROVE_OPTION) {
			File exported = export.getSelectedFile();
			int confirm = JOptionPane.showConfirmDialog(export, "Are you sure you want to export your "+dataBase.getName()+
			" Collection to "+exported.getName(), "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(confirm == JOptionPane.YES_OPTION) dataBase.saveToFile(exported);
			else displayExportDialog();
		}
	}	
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if(e.getID() == WindowEvent.WINDOW_CLOSING) {
			if(saveAllAction.isEnabled()) {
				int confirm = JOptionPane.showConfirmDialog(this, "There are unsaved changes in your collections \nDo you want to save them?",
						"Unsaved Changes!", JOptionPane.YES_NO_CANCEL_OPTION);
				if(confirm == JOptionPane.YES_OPTION) {
					saveAllCollections();
					System.exit(0);
				}
				else if(confirm == JOptionPane.NO_OPTION) System.exit(0);
			} else {
				int exit = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit confirmation",
							JOptionPane.YES_NO_OPTION);
				if(exit == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		} 
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CollectionsApp();
			}
		});
	}
	
	
	class CollectionsAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		
		public CollectionsAction(String name, Icon bigIcon, Icon smallIcon, String actionCommand, int accelerator, boolean isEnabled) {
			
			super(name);
			putValue(Action.SMALL_ICON, smallIcon);
			putValue(Action.LARGE_ICON_KEY, bigIcon);
			putValue(Action.ACTION_COMMAND_KEY, actionCommand);
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			putValue(Action.MNEMONIC_KEY, accelerator);
			setEnabled(isEnabled);
		}
		
		@Override
		public void actionPerformed(ActionEvent ae) {
			if(ae.getActionCommand().equals("add")) {
				new AddNewOrEditDialog(CollectionsApp.this, table, dataBase);
			}
			else if(ae.getActionCommand().equals("edit")) {
				int row = table.getSelectedRow();
				new AddNewOrEditDialog(CollectionsApp.this, table, dataBase, row);
			}		
			else if(ae.getActionCommand().equals("remove")) {
				int row = table.convertRowIndexToModel(table.getSelectedRow());
				Collectable<? extends AbstractItem> item = tableModel.getItem(row);
				int option = JOptionPane.showConfirmDialog(CollectionsApp.this, "Do you really want to remove this "+dataBase.getName()+
						"?\n\n"+item.toString() +"\n\n", "Confirm to remove", JOptionPane.YES_NO_OPTION);
				if(option==JOptionPane.YES_OPTION) {
					tableModel.removeItem(item, row);
					tableUpdate();
				} else return;	
			}
			else if(ae.getActionCommand().equals("save")) {
				tableModel.saveDataBase();
				saveAction.setEnabled(false);
				if(!areCollectionsChanged()) {
					saveAllAction.setEnabled(false);
				}
			}
			else if(ae.getActionCommand().equals("saveAll")) {
				saveAllCollections();
			}
			else if(ae.getActionCommand().equals("up")) {	
				int row = table.getSelectedRow();
				if((row == 0)) return;
				tableModel.moveItemUp(row);
				table.setRowSelectionInterval(row-1, row-1);		
				Rectangle rowRectangle = table.getCellRect(row-1, 0, true);
				table.scrollRectToVisible(rowRectangle);
			}
			else if(ae.getActionCommand().equals("down")) {
				int row = table.getSelectedRow();
				if((row == table.getRowCount()-1)) return;
				tableModel.moveItemDown(row);
				table.setRowSelectionInterval(row+1, row+1);
				Rectangle rowRectangle = table.getCellRect(row+1, 0, true);
				table.scrollRectToVisible(rowRectangle);
			}
			else if(ae.getActionCommand().equals("clear")) {
				clearSelection();
			}
			else if(ae.getActionCommand().equals("reset")) {
				int confirm = JOptionPane.showConfirmDialog(CollectionsApp.this, "Resetting Collection will void all unsaved changes"
						+ "\ndo you want to continue anyway?", "Confirm reset", JOptionPane.YES_NO_OPTION);
				if(confirm == JOptionPane.NO_OPTION) return;
				clearSelection();
				comboHeaderModel.resetModels();
			}
			else if(ae.getActionCommand().equals("find")) {
				searchFor();
			}
			else if(ae.getActionCommand().equals("help")){
				
			}
			else if(ae.getActionCommand().equals("exit")){
				WindowEvent e = new WindowEvent(CollectionsApp.this, WindowEvent.WINDOW_CLOSING);
				processWindowEvent(e);
			}
			else if(ae.getActionCommand().equals("popupMenu")) {
				int popupHeight = table.getComponentPopupMenu().getHeight();
				int bottomPoint = (int) table.getVisibleRect().getHeight();
				if(popupHeight == 0) {
					table.getComponentPopupMenu().setVisible(true);
					popupHeight = table.getComponentPopupMenu().getHeight();
					table.getComponentPopupMenu().setVisible(false);
				}
				int row = table.getSelectedRow();
				Rectangle rowRectangle = table.getCellRect(row, 0, true);
				int x = (int) (table.getWidth()/2.2);
				int pointY = rowRectangle.y;
				
				if((bottomPoint-pointY) > popupHeight) {
					table.getComponentPopupMenu().show(table, x, pointY);
				} else {
					pointY = pointY-popupHeight;
					table.getComponentPopupMenu().show(table, x, pointY);
				}
			}
			else if(ae.getActionCommand().equals("import")) {
				JFileChooser imp = new JFileChooser();
				ImportAccessory ia = new ImportAccessory();
				imp.setDialogTitle((!homeFlag? "Import "+dataBase.getName() : "") +" Collection");
				imp.setCurrentDirectory(new File("/users/marcin/eclipse-workspace/GIT/CollectionsApp/"));
				imp.setAccessory(ia);
				imp.setFileFilter(new CollectionsFileFilter("Collection Files *.dat", "dat"));
				imp.setFileView(new CollectionsThumbNailView(CollectionsApp.this));
				imp.addPropertyChangeListener(ia);
				int option = imp.showDialog(CollectionsApp.this, "Import");
				if(option == JFileChooser.APPROVE_OPTION) {
					File imported = imp.getSelectedFile();
					if(ia.getCollectionName().equals("Book")) {
						booksCollection = new DataBase<Book>(imported);
						collectionsButtons[1].doClick();
					} else if(ia.getCollectionName().equals("Game")) {
						gamesCollection = new DataBase<Game>(imported);
						collectionsButtons[4].doClick();
					} else if(ia.getCollectionName().equals("Movie")) {
						moviesCollection = new DataBase<Movie>(imported);
						collectionsButtons[3].doClick();
					} else if(ia.getCollectionName().equals("AudioCD")) {
						musicCollection = new DataBase<AudioCD>(imported);
						collectionsButtons[2].doClick();
					} else return;
					centralPaneUpdate();
				}
			}
			else if(ae.getActionCommand().equals("export")) {
				displayExportDialog();
			}
			else if(ae.getActionCommand().equals("home")) {
				collectionsGroup.setSelected(collectionsButtons[0].getModel(), true);
				dataBase = null;
				switchCollection();
			}
			else if(ae.getActionCommand().equals("books")) {
				collectionsGroup.setSelected(collectionsButtons[1].getModel(), true);
				if(booksCollection == null) {
					booksCollection = new DataBase<Book>(booksFilePath);
					booksCollection.instantiateType("collectionsMain.collectableItems.Book");
				}
				dataBase = booksCollection;
				columnWidths = properties.getColumnWidths(AppProperties.BOOKS_COLUMN_SIZES);
				switchCollection();
			}
			else if(ae.getActionCommand().equals("music")) {
				collectionsGroup.setSelected(collectionsButtons[2].getModel(), true);
				if(musicCollection == null) {
					musicCollection = new DataBase<AudioCD>(musicFilePath);
					musicCollection.instantiateType("collectionsMain.collectableItems.AudioCD");
				}
				dataBase = musicCollection;
				columnWidths = properties.getColumnWidths(AppProperties.MUSIC_COLUMN_SIZES);
				switchCollection();
			}
			else if(ae.getActionCommand().equals("movies")) {
				collectionsGroup.setSelected(collectionsButtons[3].getModel(), true);
				if(moviesCollection == null) {
					moviesCollection = new DataBase<Movie>(moviesFilePath);
					moviesCollection.instantiateType("collectionsMain.collectableItems.Movie");
				}
				dataBase = moviesCollection;
				columnWidths = properties.getColumnWidths(AppProperties.MOVIES_COLUMN_SIZES);
				switchCollection();
			}
			else if(ae.getActionCommand().equals("games")) {
				collectionsGroup.setSelected(collectionsButtons[4].getModel(), true);
				if(gamesCollection == null) {
					gamesCollection = new DataBase<Game>(gamesFilePath);
					gamesCollection.instantiateType("collectionsMain.collectableItems.Game");
				}
				dataBase = gamesCollection;
				columnWidths = properties.getColumnWidths(AppProperties.GAMES_COLUMN_SIZES);
				switchCollection();
			}
			else if(ae.getActionCommand().equals("settings")) {
				if(settings != null && settings.isShowing()) {
					settings.requestFocus();
					return;
				}
				settings = new JDialog();
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
				Dimension btnDim = new Dimension(85, 30);
				JButton ok = new JButton("Ok");
				JButton cancel = new JButton("Cancel");
				JButton apply = new JButton("Apply");
				JButton def = new JButton("Default");
				buttonPanel.add(ok).setPreferredSize(btnDim);
				buttonPanel.add(cancel).setPreferredSize(btnDim);
				buttonPanel.add(apply).setPreferredSize(btnDim);
				buttonPanel.add(def).setPreferredSize(btnDim);
				
				JTabbedPane tabs = new JTabbedPane();
				Appearance chooser = new Appearance(CollectionsApp.this, properties, apply, def);
				tabs.add("Appearance", chooser);
				
				ok.addActionListener((e) -> settings.setVisible(false));
				cancel.addActionListener((e) -> settings.setVisible(false));
				apply.addActionListener((e) -> {
					if(tabs.getSelectedComponent().equals(chooser)) chooser.apply();
				});
				def.addActionListener((e) -> {
					if(tabs.getSelectedComponent().equals(chooser)) chooser.setDefault();
				});
				settings.add(tabs, BorderLayout.CENTER);
				settings.add(buttonPanel, BorderLayout.PAGE_END);
				settings.pack();
				settings.setLocationRelativeTo(CollectionsApp.this);
				settings.setVisible(true);
			}
		}
	}
	
	
	public class ButtonMouseListener extends MouseAdapter {
		
		AbstractButton btn;
		Color background;
		
		public ButtonMouseListener(AbstractButton button, Color background) {
			btn = button;
			this.background = background;
		}
		public void setBackground(Color newBackground) {
			this.background = newBackground;
			btn.setBackground(background);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			if(btn instanceof JToggleButton && btn.isSelected()) return;
			btn.setBackground(btn.isEnabled()? background.darker(): background);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(btn instanceof JToggleButton && btn.isSelected()) return;
			btn.setBackground(background); 
		}	
	}
}
