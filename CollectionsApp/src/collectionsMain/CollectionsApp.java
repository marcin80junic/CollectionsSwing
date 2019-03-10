package collectionsMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import collectableItems.AbstractItem;
import collectableItems.AudioCD;
import collectableItems.Book;
import collectableItems.Collectable;
import collectableItems.Game;
import collectableItems.Movie;
import mediaBooks.*;

public class CollectionsApp extends JFrame implements ListSelectionListener  {
	
	private static final long serialVersionUID = 1L;
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
	private JDialog settings;
	private JButton btnHome, btnBooks, btnMusic, btnMovies, btnGames;
	private JButton[] navigationButtons;
	private JTable table, comboHeaderTable;
	private TableModelCollection<? extends Collectable<? extends AbstractItem>> tableModel;
	private ListSelectionModel listSelectionModel;
	private TableMouseListener mice;
	private TableModelComboHeader comboHeaderModel;
	private int[] columnWidths;
	private JPopupMenu popupMenu;
	private JToolBar mainToolBar, detailToolBar;
	private JPanel welcomePane, homeBottom, shortcutPane, westPane, centralPane, btnPane, navigationPane, eastPane, searchPane, detailPanel,
			detailItemPanel, toolbarPane, pane;
	private boolean homeFlag, booksFlag, musicFlag, moviesFlag, gamesFlag, selectionFlag;
	private Border lowered, raised;
	private CollectionsAction addAction, editAction, removeAction, upAction, downAction, clearAction, resetAction, findAction,
			displayPopupMenuAction,	exitAction, helpAction,
			importAction, exportAction, settingsAction, homeAction, booksAction, audioAction, gamesAction, moviesAction;
	public CollectionsAction saveAction, saveAllAction;
	private JLabel lblSearch, titleLine1, titleLine2, detailTitle;
	private JTextPane titleText;
	private static String search = "";
	private SearchField tfSearch;
	private Object element;
	
	
	CollectionsApp() {
		super ("Collections");
		setMinimumSize(new Dimension(940, 300));
		setPreferredSize(new Dimension(940, 500));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		homeFlag = true;
		propertiesInit();
		lookAndFeelInit();
		dataBase = new DataBase<>();
		actionsInit();
		
		westPaneInit();
		GridBagConstraints wp = new GridBagConstraints();
		wp.gridx = 0;
		wp.weighty = 1.0;
		wp.fill = GridBagConstraints.VERTICAL;
		centralPaneInit();
		GridBagConstraints cp = new GridBagConstraints();
		cp.gridx = 1;
		cp.weighty = 1.0;
		cp.fill = GridBagConstraints.BOTH;
		navigationPaneInit();
		GridBagConstraints np = new GridBagConstraints();
		np.gridx = 2;
		np.weighty = 1.0;
		np.fill = GridBagConstraints.BOTH;
		eastPaneInit();
		GridBagConstraints ep = new GridBagConstraints();
		ep.gridx = 3;
		ep.weighty = 1.0;
		ep.weightx = 1.0;
		ep.fill = GridBagConstraints.BOTH;

		setJMenuBar(createMenuBar());
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new GridBagLayout());
		contentPane.add(westPane, wp);
		contentPane.add(centralPane, cp);
		contentPane.add(navigationPane, np);
		contentPane.add(eastPane, ep);
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
		westPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 0,	5));
		westPane.setLayout(new BoxLayout(westPane, BoxLayout.Y_AXIS));
		westPane.setMinimumSize(new Dimension(65, 300));
		westPane.setPreferredSize(new Dimension(65, 300));	
		Dimension maxBtn = new Dimension(65, 60);
		lowered = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		raised = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		JButton[] collectionButtons = new JButton[]{btnHome = new JButton(homeAction), btnBooks = new JButton(booksAction),
				btnMusic = new JButton(audioAction), btnMovies = new JButton(moviesAction), btnGames = new JButton(gamesAction)};
		for(int i=0; i<collectionButtons.length; i++) {
			collectionButtons[i].addMouseListener(new ButtonMouseListener(collectionButtons[i], mainBackground));
			collectionButtons[i].setHideActionText(true);
			collectionButtons[i].setBorder((i==0)? lowered: raised);
			westPane.add(collectionButtons[i]).setMaximumSize(maxBtn);
			westPane.add((i == (collectionButtons.length - 1))? Box.createVerticalGlue(): Box.createRigidArea(new Dimension(0,5)));
		}
	}
	
	private void westPaneUpdate() {
		
		if(!homeFlag) {
			btnHome.setBorder(raised);
		}
		if(!booksFlag) {
			btnBooks.setBorder(raised);
		}
		if(!musicFlag) {
			btnMusic.setBorder(raised);
		}
		if(!moviesFlag) {
			btnMovies.setBorder(raised);
		}
		if(!gamesFlag) {
			btnGames.setBorder(raised);
		}
	}
	
	private void centralPaneInit() {
		
		centralPane = new JPanel();
		centralPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		centralPane.setLayout(new BorderLayout());
		centralPane.setMinimumSize(new Dimension(805, 500));
		centralPane.setPreferredSize(new Dimension(805, 500));
		welcomePaneInit();
		toolbarPaneInit();
		//tableInit();
		btnPaneInit();
		centralPaneUpdate();
	}
	
	private void welcomePaneInit() {
		
		welcomePane = new JPanel();
		welcomePane.setBackground(welcomeBackground);
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
		titlePane.add(Box.createVerticalStrut(30));
		titlePane.add(titleLine1);
		titlePane.add(Box.createVerticalStrut(10));
		titlePane.add(titleLine2);
		shortcutPane = new JPanel();
		shortcutPane.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
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
		homeBottom = new JPanel();
		homeBottom.setPreferredSize(new Dimension(680, 66));
		welcomePane.add(shortcutPane, BorderLayout.CENTER);
		welcomePane.add(titlePane, BorderLayout.PAGE_START);
		welcomePane.add(homeBottom, BorderLayout.PAGE_END);
	}
	
	private void centralPaneUpdate() {
		
		centralPane.removeAll();
		if(homeFlag) {
			centralPane.add(welcomePane);
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
		tableModel.addTableModelListener((e) -> table.repaint());
		table.setColumnModel(dataBase.getTableColumnModel(table));
		table.getActionMap().put("delete item", removeAction);
		table.getActionMap().put("edit item", editAction);
		table.getActionMap().put("display popupMenu", displayPopupMenuAction);
		table.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete item");
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "edit item");
		table.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "display popupMenu");
		tablePopupMenuInit();
		table.setComponentPopupMenu(popupMenu);
		if(mice != null) table.removeMouseListener(mice);
		table.addMouseListener(mice = new TableMouseListener(this, table, dataBase));
		table.setBackground(tableBackground);
		table.setDefaultRenderer(String.class, new TableRendererCollection());
		table.setFillsViewportHeight(true);
		table.setSelectionMode(0);
		table.setTableHeader(null);
		listSelectionModel = table.getSelectionModel();
		listSelectionModel.addListSelectionListener(this);
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
		comboHeaderTable.getTableHeader().setReorderingAllowed(false);
		comboHeaderTable.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
		    public void mouseClicked(MouseEvent e) {
				int columnIndex = comboHeaderTable.columnAtPoint(e.getPoint());
				if(columnIndex == 0) return;
				TableHeaderRenderer hr = (TableHeaderRenderer) comboHeaderTable.getColumnModel().getColumn(columnIndex).getHeaderRenderer();
				if(hr.isAscIcon()) tableModel.sort(columnIndex, true);
				else tableModel.sort(columnIndex, false);
				comboHeaderTable.getTableHeader().repaint();
			}
		});
		dataBase.setColumnWidths(table.getRowCount(), columnWidths);
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
	
	void columnsUpdate() {
		dataBase.setColumnWidths(tableModel.getRowCount(), columnWidths);
		dataBase.setColumnWidths(tableModel.getRowCount(), columnWidths);
	}
	
	void tableUpdate() {
		if(tableModel.getRowCount() == 0) {
			tableModel.reloadTableModel();
			comboHeaderModel.resetModel();
		} 
		else {
			comboHeaderModel.resetComboFlags();
			comboHeaderModel.updateComboLists();		
		}
	}
	
	private void toolbarPaneInit() {
		
		toolbarPane = new JPanel();
		toolbarPane.setLayout(new BorderLayout());
		toolbarPane.setPreferredSize(new Dimension(805, 35));
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
	}	
	
	private void btnPaneInit() {
		
		btnPane = new JPanel();
		btnPane.setOpaque(false);
		btnPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
		btnPane.setPreferredSize(new Dimension(680, 66));
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
	
	private void navigationPaneInit() {
		
		navigationPane = new JPanel();
		navigationPane.setLayout(new BoxLayout(navigationPane, BoxLayout.PAGE_AXIS));
		navigationPane.setMinimumSize(new Dimension(48, 500));
		navigationPane.setPreferredSize(new Dimension(48, 500));
		navigationPane.setMaximumSize(new Dimension(48, 500));
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
		eastPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0,	5));
		eastPane.setLayout(new BorderLayout());
		
		searchPane = new JPanel();
		searchPane.setOpaque(false);
		searchPane.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		searchPane.setLayout(new BoxLayout(searchPane, BoxLayout.LINE_AXIS));
		searchPane.setMaximumSize(new Dimension(200, 25));
		lblSearch = new JLabel("Search for: ");
		lblSearch.setFont(mainFont);
		lblSearch.setLabelFor(tfSearch);
		lblSearch.setDisplayedMnemonic('h');
		tfSearch = new SearchField(this, (Icon)findAction.getValue(Action.SMALL_ICON));
		tfSearch.setMaximumSize(new Dimension(150, 30));
		searchPane.add(Box.createHorizontalStrut(100));
		searchPane.add(lblSearch).setForeground(mainForeground);
		searchPane.add(tfSearch);
		searchPane.add(Box.createHorizontalStrut(100));
		
		detailPanel = new JPanel(new BorderLayout());
		detailPanel.setBorder(raised);
		detailPanel.setBackground(welcomeBackground);
		detailTitle = new JLabel("Home Screen", JLabel.CENTER);
		detailTitle.setFont(welcomeFont.deriveFont(Font.BOLD, 25f));
		detailTitle.setForeground(welcomeForeground);
		detailItemPanel = new JPanel(new BorderLayout());
		detailItemPanel.setPreferredSize(new Dimension(100, 100));
		detailItemPanel.setBackground(welcomeBackground);
		titleText = new JTextPane();
		titleText.setEditable(false);
		detailItemPanel.add(titleText, BorderLayout.PAGE_START);
		
		detailPanel.add(detailTitle, BorderLayout.PAGE_START);
		JScrollPane scroll = new JScrollPane(detailItemPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		detailPanel.add(scroll, BorderLayout.CENTER);
		
		JPanel toolPane = new JPanel();
		toolPane.setLayout(new BorderLayout());
		toolPane.setPreferredSize(new Dimension(200, 60));
		detailToolBar = new JToolBar();
		toolPane.add(detailToolBar);
		
		eastPane.add(searchPane, BorderLayout.PAGE_START);
		eastPane.add(detailPanel, BorderLayout.CENTER);
		eastPane.add(toolPane, BorderLayout.PAGE_END);
		detailPaneUpdate();
	}
	
	private void detailPaneUpdate() {
		
		updateDetailPanelComponentsAppearance();
		
		if (homeFlag) {
			detailTitle.setText("Welcome...");
		}
		else if (booksFlag) {
			detailTitle.setText("Books Collection");
			if (!table.getSelectionModel().isSelectionEmpty()) {
				Book book = (Book) tableModel.getItem(table.getSelectedRow());
				BookStyledDocument bsd = new BookStyledDocument(book);
				titleText.setStyledDocument(bsd);
			}
		}
		else if (musicFlag) {
			detailTitle.setText("Audio Collection");
			
		}
		else if (gamesFlag) {
			detailTitle.setText("Games Collection");
		}
		else if (moviesFlag) {
			detailTitle.setText("Movies Collection");
		}
		eastPane.revalidate();
		eastPane.repaint();
	}
	
	private void updateDetailPanelComponentsAppearance() {
		
		if(homeFlag) {
			detailPanel.setBackground(welcomeBackground);
			detailItemPanel.setBackground(welcomeBackground);
			detailTitle.setFont(welcomeFont.deriveFont(Font.BOLD, 25f));
			detailTitle.setForeground(welcomeForeground);
		}
		else {
			detailPanel.setBackground(mainBackground);
			detailItemPanel.setBackground(tableBackground);
			detailTitle.setFont(mainFont.deriveFont(Font.BOLD, 25f));
			detailTitle.setForeground(mainForeground);
		}
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
		booksAction = new CollectionsAction("Books Collection", booksIcon, null, "books", 'B', true);
		audioAction = new CollectionsAction("Music Collection", audioIcon, null, "music", 'M', true);
		gamesAction = new CollectionsAction("Games Collection", gamesIcon, null, "games", 'G', true);
		moviesAction = new CollectionsAction("Movies Collection", moviesIcon, null, "movies", 'V', true);
		addAction = new CollectionsAction("Add", null, null, "add", 'A', false);
		//addAction.putValue(Action.SHORT_DESCRIPTION, "Adds new "+dataBase.getName());
		editAction = new CollectionsAction("Edit", null, null, "edit", 'E', false);
		//editAction.putValue(Action.SHORT_DESCRIPTION, "Edits highlighted "+dataBase.getName());
		removeAction = new CollectionsAction("Remove", null, null, "remove", 'R', false);
		//removeAction.putValue(Action.SHORT_DESCRIPTION, "Removes highlighted "+dataBase.getName());
		saveAction = new CollectionsAction("Save", null, saveIcon, "save", 'S', false);
		saveAction.putValue(Action.SHORT_DESCRIPTION, "Saves changes in current collection");
		saveAllAction = new CollectionsAction("Save All", null, saveAllIcon, "saveAll", 'L', false);
		saveAllAction.putValue(Action.SHORT_DESCRIPTION, "Saves changes in all collections");
		clearAction = new CollectionsAction("Clear Selection", null, null, "clear", 'C', false);
		clearAction.putValue(Action.SHORT_DESCRIPTION, "Clears current selection");
		resetAction = new CollectionsAction("Reset", null, null, "reset", 'T', true);
		resetAction.putValue(Action.SHORT_DESCRIPTION, "Resets current collection");
		findAction = new CollectionsAction("Find...", null, searchIcon, "search", 'F', true);
		findAction.putValue(Action.SHORT_DESCRIPTION, "Search for..");
		upAction = new CollectionsAction("Move Up", upIcon, null, "up", KeyEvent.VK_UP, false);
		upAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted item up");
		downAction = new CollectionsAction("Move Down", downIcon, null, "down", KeyEvent.VK_DOWN, false);
		downAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted item down");
		importAction = new CollectionsAction("Import Collection", bigImport, smallImport, "import", 'I', true);
		importAction.putValue(Action.SHORT_DESCRIPTION, "import collection from a file");
		exportAction = new CollectionsAction("Export Collection", null, exportIcon, "export", 'O', false);
		exportAction.putValue(Action.SHORT_DESCRIPTION, "Export Collection to a file");
		settingsAction = new CollectionsAction("Settings", bigSettings, smallSettings, "settings", 'N', true);
		settingsAction.putValue(Action.SHORT_DESCRIPTION, "Change application's settings");
		displayPopupMenuAction = new CollectionsAction("display popupMenu", null, null, "popupMenu", 'Y', true);
		helpAction = new CollectionsAction("Help", null, null, "help", 'H', true);
		exitAction = new CollectionsAction("Exit", null, null, "exit", 'X', true);
	}
	
	private void actionsUpdate() {
		
		addAction.putValue(Action.NAME, "Add "+ dataBase.getName());
		addAction.putValue(Action.SHORT_DESCRIPTION, "Adds new "+dataBase.getName());
		editAction.putValue(Action.NAME, "Edit "+dataBase.getName());
		editAction.putValue(Action.SHORT_DESCRIPTION, "Edits highlighted "+dataBase.getName());
		removeAction.putValue(Action.NAME, "Remove "+dataBase.getName());
		removeAction.putValue(Action.SHORT_DESCRIPTION, "Removes highlighted "+dataBase.getName());
		upAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted "+dataBase.getName()+" up");
		downAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted "+dataBase.getName()+" down");
		if(!homeFlag) {
			exportAction.putValue(Action.SHORT_DESCRIPTION, "Export "+dataBase.getName()+" collection to a file");
			exportAction.setEnabled(true);
		} else {
			exportAction.putValue(Action.SHORT_DESCRIPTION, "Export Collection to a file");
			exportAction.setEnabled(false);
		}
		addAction.setEnabled(homeFlag? false: true);
		editAction.setEnabled(listSelectionModel.isSelectionEmpty()? false: true);
		removeAction.setEnabled(listSelectionModel.isSelectionEmpty()? false: true);
		if(!tableModel.isChanged()) saveAction.setEnabled(false);
		if(!areCollectionsChanged()) saveAllAction.setEnabled(false);
		clearAction.setEnabled(listSelectionModel.isSelectionEmpty()? false: true);
		resetAction.setEnabled(homeFlag? false: true);
		upAction.setEnabled(false);
		downAction.setEnabled(false);
	}
	
	private void switchCollection() {
		
		search = "";
		if(!homeFlag) {
			tableInit();
			actionsUpdate();
		}
		westPaneUpdate();
		centralPaneUpdate();
		navigationPaneUpdate();
		detailPaneUpdate();
		setJMenuBar(createMenuBar());
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		if(!lsm.isSelectionEmpty()) {
			editAction.setEnabled(true);
			removeAction.setEnabled(true);
			clearAction.setEnabled(true);
			upAction.setEnabled(true);
			downAction.setEnabled(true);
			if((element == null) || 
					((element != null) && !(selectionFlag) && !(element.equals(tableModel.getItem(lsm.getAnchorSelectionIndex()))))) {
				detailPaneUpdate();
			} else selectionFlag = false;
			element = tableModel.getItem(lsm.getAnchorSelectionIndex());
		} else {	
			editAction.setEnabled(false);
			removeAction.setEnabled(false);
			clearAction.setEnabled(false);
			upAction.setEnabled(false);
			downAction.setEnabled(false);
		}
	}
	
	void searchFor() {
		search = tfSearch.getText().trim();
		if(!search.equals("") && !homeFlag) {
			tableModel.searchFor(search);
			tfSearch.selectAll();
		}
	}
	
	DataBase<? extends Collectable<? extends AbstractItem>> getDataBase(){ return dataBase; }
	
	static String getSearchText() { return search;	}
	static Color getMainBackground() { return mainBackground; }
	static Color getMainForeground() { return mainForeground; }
	static Font getMainFont() { return mainFont; }
	static Color getTableBackground() { return tableBackground; }
	static Color getTableForeground() { return tableForeground; }
	static Font getTableFont() { return tableFont; }
	static Color getHighlightBackground() { return highlightBackground; }
	static Color getHighlightForeground() { return highlightForeground; }
	static Font getHighlightFont() { return highlightFont; }
	
	
	private void resetBtnFlags() {
		if(homeFlag) btnHome.setBorder(raised);
		if(booksFlag) btnBooks.setBorder(raised);
		if(musicFlag) btnMusic.setBorder(raised);
		if(moviesFlag) btnMovies.setBorder(raised);
		if(gamesFlag) btnGames.setBorder(raised);
		homeFlag = booksFlag = musicFlag = moviesFlag = gamesFlag = false;
	}
	
	
	boolean areCollectionsChanged() {
		boolean books, audio, games, movies;
		books = audio = games = movies = false;
		if(booksCollection != null) books = booksCollection.getTableModel().isChanged();
		if(musicCollection != null) audio = musicCollection.getTableModel().isChanged();
		if(gamesCollection != null) games = gamesCollection.getTableModel().isChanged();
		if(moviesCollection != null) movies = moviesCollection.getTableModel().isChanged();
		return (books || audio || games || movies);
	}
	
	
	boolean areOtherCollectionsChanged(DataBase<? extends Collectable<? extends AbstractItem>> collection) {
		
		boolean books, audio, games, movies;
		books = audio = games = movies = false;
		if(!collection.equals(booksCollection)) {
			if(booksCollection != null) books = booksCollection.getTableModel().isChanged();
		}
		if(!collection.equals(musicCollection)) {
			if(musicCollection != null) audio = musicCollection.getTableModel().isChanged();
		}
		if(!collection.equals(gamesCollection)) {
			if(gamesCollection != null) games = gamesCollection.getTableModel().isChanged();
		}
		if(!collection.equals(moviesCollection)) {
			if(moviesCollection != null) movies = moviesCollection.getTableModel().isChanged();
		}
		return (books || audio || games || movies);
	}
	
	
	private void saveAllCollections() {
		
		if(booksCollection != null) {
			if(booksCollection.getTableModel().isChanged()) {
				booksCollection.saveToFile();
			}
		}
		if(musicCollection != null) {
			if(musicCollection.getTableModel().isChanged()) {
				musicCollection.saveToFile();
			}
		}
		if(gamesCollection != null) {
			if(gamesCollection.getTableModel().isChanged()) {
				gamesCollection.saveToFile();
			}
		}
		if(moviesCollection != null) {
			if(moviesCollection.getTableModel().isChanged()) {
				moviesCollection.saveToFile();
			}
		}
		saveAction.setEnabled(false);
		saveAllAction.setEnabled(false);
	}
	
	
	public void clearSelection() {
		tfSearch.setText("");
		search = "";
		table.clearSelection();
		detailPaneUpdate();
		element = null;
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
		
		if(element.equals("Application's Frame")) {
			SwingUtilities.updateComponentTreeUI(this);
			SwingUtilities.updateComponentTreeUI(settings);
			lookAndFeelInit();
			updateButtonListeners(westPane.getComponents(), mainBackground, null, null);
			updateButtonListeners(btnPane.getComponents(), mainBackground, mainForeground, mainFont.deriveFont(12f));
			updateButtonListeners(navigationButtons, mainBackground, null, null);
			lblSearch.setFont(mainFont);
			lblSearch.setForeground(mainForeground);
			tableInit();
			if(!homeFlag) tableUpdate();
			centralPaneUpdate();
			titleLine1.setFont(welcomeFont);
			titleLine2.setFont(welcomeFont);
			detailTitle.setFont(welcomeFont);
		}
		else if(element.equals("Welcome Screen")) {
			welcomePane.setBackground(welcomeBackground);
			titleLine1.setFont(welcomeFont);
			titleLine2.setFont(welcomeFont);
			titleLine1.setForeground(welcomeForeground);
			titleLine2.setForeground(welcomeForeground);
			detailTitle.setFont(welcomeFont);
			detailTitle.setForeground(welcomeForeground);
			detailPanel.setBackground(welcomeBackground);
			updateButtonListeners(shortcutPane.getComponents(), welcomeBackground, welcomeForeground, welcomeFont.deriveFont(12f));
		}
		else if(element.equals("Table")) {
			table.setBackground(tableBackground);
			table.repaint();
			comboHeaderTable.setBackground(tableBackground);
			comboHeaderTable.repaint();
		}
		else if(element.equals("Highlight Style")) {
			
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
		export.setCurrentDirectory(new File("/users/marcin/eclipse-workspace/collectionsapp/"));
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
			if((saveAllAction.isEnabled() || areCollectionsChanged()) && (table.getRowCount() == dataBase.size())) {
				int confirm = JOptionPane.showConfirmDialog(this, "There are unsaved changes in your collections \nDo you want to save them?",
						"Unsaved Changes!", JOptionPane.YES_NO_CANCEL_OPTION);
				if(confirm == JOptionPane.YES_OPTION) {
					saveAllCollections();
					System.exit(0);
				}
				else if(confirm == JOptionPane.NO_OPTION) System.exit(0);
			} else {
				int exit = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit confirmation", JOptionPane.YES_NO_OPTION);
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
		private Rectangle rowRectangle;
		
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
					if(String.valueOf(tableModel.getRowCount()+1).length() != String.valueOf(tableModel.getRowCount()).length()) 
						tableUpdate();
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
				rowRectangle = table.getCellRect(row-1, 0, true);
				table.scrollRectToVisible(rowRectangle);
				if(dataBase.size() == table.getRowCount()) {
					saveAction.setEnabled(true);
					saveAllAction.setEnabled(true);
				}
			}
			else if(ae.getActionCommand().equals("down")) {
				int row = table.getSelectedRow();
				if((row == table.getRowCount()-1)) return;
				tableModel.moveItemDown(row);
				table.setRowSelectionInterval(row+1, row+1);
				rowRectangle = table.getCellRect(row+1, 0, true);
				table.scrollRectToVisible(rowRectangle);
				if(dataBase.size() == table.getRowCount()) {
					saveAction.setEnabled(true);
					saveAllAction.setEnabled(true);
				}
			}
			else if(ae.getActionCommand().equals("clear")) {
				clearSelection();
			}
			else if(ae.getActionCommand().equals("reset")) {
				if(saveAction.isEnabled() || tableModel.isChanged() || comboHeaderModel.isComboSelected()) {
					int confirm = JOptionPane.showConfirmDialog(CollectionsApp.this, "Resetting Collection will void all unsaved changes"
							+ "\ndo you want to continue anyway?", "Confirm reset", JOptionPane.YES_NO_OPTION);
					if(confirm == JOptionPane.NO_OPTION) return;
					clearSelection();
					tableModel.reloadTableModel();
					comboHeaderModel.resetModel();
					saveAction.setEnabled(false);
					if(!areOtherCollectionsChanged(dataBase)) {
						saveAllAction.setEnabled(false);
					}
				}
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
				rowRectangle = table.getCellRect(row, 0, true);
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
				imp.setDialogTitle("Import "+dataBase.getName()+" Collection");
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
						btnBooks.doClick();
					} else if(ia.getCollectionName().equals("Game")) {
						gamesCollection = new DataBase<Game>(imported);
						btnGames.doClick();
					} else if(ia.getCollectionName().equals("Movie")) {
						moviesCollection = new DataBase<Movie>(imported);
						btnMovies.doClick();
					} else if(ia.getCollectionName().equals("AudioCD")) {
						musicCollection = new DataBase<AudioCD>(imported);
						btnMusic.doClick();
					} else return;
					centralPaneUpdate();
				}
			}
			else if(ae.getActionCommand().equals("export")) {
				displayExportDialog();
			}
			else if(ae.getActionCommand().equals("home")) {
				btnHome.setBorder(lowered);
				btnHome.setBackground(btnHome.getBackground().darker());
				dataBase = null;
				resetBtnFlags();
				homeFlag = true;
				switchCollection();
			}
			else if(ae.getActionCommand().equals("books")) {
				btnBooks.setBorder(lowered);
				btnBooks.setBackground(btnBooks.getBackground().darker());
				if(booksCollection == null) {
					booksCollection = new DataBase<Book>(booksFilePath);
					booksCollection.instantiateType(new Book("a","a","a","a",0));
				}
				dataBase = booksCollection;
				resetBtnFlags();
				booksFlag = true;
				columnWidths = properties.getColumnWidths(AppProperties.BOOKS_COLUMN_SIZES);
				switchCollection();
			}
			else if(ae.getActionCommand().equals("music")) {
				btnMusic.setBorder(lowered);
				btnMusic.setBackground(btnMusic.getBackground().darker());
				if(musicCollection == null) {
					musicCollection = new DataBase<AudioCD>(musicFilePath);
					musicCollection.instantiateType(new AudioCD("","","",0,new int[0]));
				}
				dataBase = musicCollection;
				resetBtnFlags();
				musicFlag = true;
				columnWidths = properties.getColumnWidths(AppProperties.MUSIC_COLUMN_SIZES);
				switchCollection();
			}
			else if(ae.getActionCommand().equals("movies")) {
				btnMovies.setBorder(lowered);
				btnMovies.setBackground(btnMovies.getBackground().darker());
				if(moviesCollection == null) {
					moviesCollection = new DataBase<Movie>(moviesFilePath);
					moviesCollection.instantiateType(new Movie("","","","",0));
				}
				dataBase = moviesCollection;
				resetBtnFlags();
				moviesFlag = true;
				columnWidths = properties.getColumnWidths(AppProperties.MOVIES_COLUMN_SIZES);
				switchCollection();
			}
			else if(ae.getActionCommand().equals("games")) {
				btnGames.setBorder(lowered);
				btnGames.setBackground(btnGames.getBackground().darker());
				if(gamesCollection == null) {
					gamesCollection = new DataBase<Game>(gamesFilePath);
					gamesCollection.instantiateType(new Game("","","","",0));
				}
				dataBase = gamesCollection;
				resetBtnFlags();
				gamesFlag = true;
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
				Appearance chooser = new Appearance(CollectionsApp.this, properties);
				tabs.add("Appearance", chooser);
				
				ok.addActionListener((e) -> {
					settings.setVisible(false);
				});
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
		
		JButton btn;
		Color background;
		
		public ButtonMouseListener(JButton button, Color background) {
			btn = button;
			this.background = background;
		}
		public void setBackground(Color newBackground) {
			this.background = newBackground;
			btn.setBackground(background);
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			btn.setBackground(btn.isEnabled()? background.darker(): background);
			btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		@Override
		public void mouseExited(MouseEvent e) { 
			btn.setBackground(background); 
		}	
	}
}
