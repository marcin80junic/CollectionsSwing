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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.swing.table.TableColumnModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import books.*;
import games.*;
import item.ItemCollection;
import movies.*;
import music.*;

public class CollectionsApp extends JFrame implements ListSelectionListener  {
	
	private static final long serialVersionUID = 1L;
	private StyleContext sc;
	private final String[] styleNames = {"mainAttributes", "welcomeAttributes", "tableAttributes", "highlightAttributes"};
	Color mainBackground, mainForeground, welcomeBackground, welcomeForeground, tableBackground, tableForeground, 
			highlightBackground, highlightForeground;
	Font mainFont, welcomeFont, tableFont, highlightFont;
	private JDialog settings;
	private JButton btnHome, btnBooks, btnMusic, btnMovies, btnGames;
	private JButton[] navigationButtons;
	private JTable table, headerTable;
	private TableModelCollection tableModel;
	private TableModelComboHeader headerModel;
	private TableColumnModel headerColumnModel;
	private JScrollPane tableScroll;
	private JPopupMenu popupMenu;
	private JToolBar mainToolBar, detailToolBar;
	private JPanel homePane, homeBottom, shortcutPane, westPane, centralPane, btnPane, navigationPane, eastPane, searchPane, detailPanel,
			detailItemPanel, toolbarPane, pane;
	private boolean homeFlag, booksFlag, musicFlag, moviesFlag, gamesFlag, selectionFlag;
	private Border lowered, raised;
	private TableMouseListener mice;
	private ListSelectionModel listSelectionModel;
	private Collection<?> collection, itemCollection, booksCollection, gamesCollection, moviesCollection, musicCollection;
	private CollectionsAction addAction, editAction, removeAction, upAction, downAction, clearAction, resetAction, findAction,
			displayPopupMenuAction,	exitAction, helpAction,
			importAction, exportAction, settingsAction, homeAction, booksAction, audioAction, gamesAction, moviesAction;
	public CollectionsAction saveAction, saveAllAction;
	private JLabel lblSearch, titleLine1, titleLine2, detailTitle;
	private JTextPane titleText;
	private String search = "";
	private SearchField tfSearch;
	private Object element;
	
	
	CollectionsApp() {
		super ("Collections");
		setMinimumSize(new Dimension(940, 300));
		setPreferredSize(new Dimension(940, 500));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
		itemCollection = new ItemCollection();
		collection = itemCollection;
		homeFlag = true;
		propertiesLoad();
		lookAndFeelInit();
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
	
	private void propertiesLoad() {
		sc = StyleContext.getDefaultStyleContext();
		ObjectInputStream ois = null;
		File defFile = null, file = null;
		Style defStyle = null, style = null;
		String parentName = "";
		try {
			for(int i=0; i<styleNames.length; i++) {
				parentName = styleNames[i]+"Default";
				defStyle = sc.addStyle(parentName, null);
				style = sc.addStyle(styleNames[i], defStyle);
				defFile = new File(parentName+".set");
				file = new File(styleNames[i]+".set");
				if(defFile.isFile()) {
					ois = new ObjectInputStream(new FileInputStream(defFile));
					StyleContext.readAttributeSet(ois, defStyle);
				} else defaultsInit(parentName);
				if(file.isFile()) {
					ois = new ObjectInputStream(new FileInputStream(file));
					StyleContext.readAttributeSet(ois, style);
				}
			}
		} catch(IOException | ClassNotFoundException e) { e.printStackTrace(); }
		
		propertiesUpdate();
	}

	private void defaultsInit(String styleName) {
		if(styleName.endsWith("Default")) {
			Style defStyle = null;
			if(styleName.equals("mainAttributesDefault")) {
				defStyle = sc.getStyle(styleName);
				StyleConstants.setBackground(defStyle, Color.decode("#d6d9df"));
				StyleConstants.setForeground(defStyle, Color.BLACK);
				StyleConstants.setFontFamily(defStyle, "SansSerif");
				StyleConstants.setFontSize(defStyle, 12);
				StyleConstants.setBold(defStyle, false);
				StyleConstants.setItalic(defStyle, false);
			}
			else if(styleName.equals("welcomeAttributesDefault")) {
				defStyle = sc.getStyle(styleName);
				StyleConstants.setBackground(defStyle, Color.decode("#d6d9df"));
				StyleConstants.setForeground(defStyle, Color.BLACK);
				StyleConstants.setFontFamily(defStyle, "SansSerif");
				StyleConstants.setFontSize(defStyle, 30);
				StyleConstants.setBold(defStyle, true);
				StyleConstants.setItalic(defStyle, true);
			}
			else if(styleName.equals("tableAttributesDefault")) {
				defStyle = sc.getStyle(styleName);
				StyleConstants.setBackground(defStyle, Color.WHITE);
				StyleConstants.setForeground(defStyle, Color.BLACK);
				StyleConstants.setFontFamily(defStyle, "SansSerif");
				StyleConstants.setFontSize(defStyle, 12);
				StyleConstants.setBold(defStyle, false);
				StyleConstants.setItalic(defStyle, false);
			}
			else if(styleName.equals("highlightAttributesDefault")) {
				defStyle = sc.getStyle(styleName);
				StyleConstants.setBackground(defStyle, Color.GREEN);
				StyleConstants.setForeground(defStyle, Color.BLACK);
				StyleConstants.setFontFamily(defStyle, "SansSerif");
				StyleConstants.setFontSize(defStyle, 12);
				StyleConstants.setBold(defStyle, false);
				StyleConstants.setItalic(defStyle, false);
			}
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(styleName+".set")))) {
				StyleContext.writeAttributeSet(oos, defStyle);
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	private void propertiesUpdate() {
	
		Style style = sc.getStyle("mainAttributes");
		mainBackground = sc.getBackground(style);
		mainForeground = sc.getForeground(style);
		mainFont = sc.getFont(style);
		style = sc.getStyle("welcomeAttributes");
		welcomeBackground = sc.getBackground(style);
		welcomeForeground = sc.getForeground(style);
		welcomeFont = sc.getFont(style);
		style = sc.getStyle("tableAttributes");
		tableBackground = sc.getBackground(style);
		tableForeground = sc.getForeground(style);
		tableFont = sc.getFont(style);
		style = sc.getStyle("highlightAttributes");
		highlightBackground = sc.getBackground(style);
		highlightForeground = sc.getForeground(style);
		highlightFont = sc.getFont(style);
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
	
	void westPaneUpdate() {
		
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
		homePaneInit();
		toolbarPaneInit();
		tableInit();
		btnPaneInit();
		centralPaneUpdate();
	}
	
	void homePaneInit() {
		
		homePane = new JPanel();
		homePane.setBackground(welcomeBackground);
		homePane.setLayout(new BorderLayout());
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
		homePane.add(shortcutPane, BorderLayout.CENTER);
		homePane.add(titlePane, BorderLayout.PAGE_START);
		homePane.add(homeBottom, BorderLayout.PAGE_END);
	}
	
	void centralPaneUpdate() {
		
		centralPane.removeAll();
		if(homeFlag) {
			centralPane.add(homePane);
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
		
		table = new JTable();
		table.setBackground(tableBackground);
		table.setDefaultRenderer(String.class, new TableRendererCollection(this));
		table.setDefaultRenderer(Integer.class, new TableRendererCollection(this));
		table.setFillsViewportHeight(true);
		table.setIntercellSpacing(new Dimension(0,0));
		table.setSelectionMode(0);
		listSelectionModel = table.getSelectionModel();
		listSelectionModel.addListSelectionListener(this);
		tableScroll = new JScrollPane(table);
		tableScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		tableScroll.getVerticalScrollBar().setPreferredSize(new Dimension(20, table.getMaximumSize().height));
		mice = new TableMouseListener(this, table, collection);
		table.addMouseListener(mice);
		
		headerModel = new TableModelComboHeader(this, table);
		headerTable = new JTable(headerModel, table.getColumnModel());
		headerTable.setBackground(tableBackground);
		headerTable.setRowHeight(22);
		headerTable.setRowSelectionAllowed(false);
		headerTable.setDefaultRenderer(String.class, headerModel.new Renderer());
		headerTable.setDefaultEditor(String.class, headerModel.new Editor());
		headerTable.setTableHeader(table.getTableHeader());
		headerTable.getTableHeader().setDefaultRenderer(new TableHeaderRenderer(this));
		
		JScrollPane headerScroll = new JScrollPane(headerTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, 
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		headerScroll.setBorder(null);
		tableScroll.setBorder(null);
		JViewport headerView = new JViewport();
		headerView.setView(headerScroll);
		headerView.setPreferredSize(new Dimension(800, 46));
		
		headerTable.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
		    public void mouseClicked(MouseEvent e) {
				int columnIndex = headerTable.columnAtPoint(e.getPoint());
				if(columnIndex == 0) return;
				TableHeaderRenderer hr = (TableHeaderRenderer) headerTable.getColumnModel().getColumn(columnIndex).getHeaderRenderer();
				if(hr.isAscIcon()) tableModel.toggleSort(columnIndex);
				else tableModel.sort(columnIndex);
				initHeaderRenderers();
			}
		});
		table.setTableHeader(null);
		pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.add(headerView, BorderLayout.NORTH);
		pane.add(tableScroll);
	}
	
	void tableUpdate() {
		
		tableModel = collection.getTableModel();
		tableModel.addTableModelListener((e) -> table.repaint());
		table.setModel(tableModel);
		table.getActionMap().put("delete item", removeAction);
		table.getActionMap().put("edit item", editAction);
		table.getActionMap().put("display popupMenu", displayPopupMenuAction);
		table.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "delete item");
		table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "edit item");
		table.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "display popupMenu");
		tablePopupMenuInit();
		table.setComponentPopupMenu(popupMenu);
		table.removeMouseListener(mice);
		mice = new TableMouseListener(this, table, collection);
		table.addMouseListener(mice);
		collection.setColumnSize(table);
		headerModel.resetComboFlags();
		headerModel.updateComboLists();
		headerColumnModel = collection.getTableColumnModel(table);
		headerTable.setColumnModel(headerColumnModel);
		initHeaderRenderers();
	}
	
	void initHeaderRenderers() {
		for(int i=0; i<headerTable.getColumnModel().getColumnCount(); i++) {
			headerTable.getColumnModel().getColumn(i).setHeaderRenderer(new TableHeaderRenderer(this));
		}
		headerTable.getTableHeader().repaint();
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
	
	void navigationPaneUpdate() {
		
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
	
	void detailPaneUpdate() {
		
		updateDetailPanelComponentsAppearance();
		
		if (homeFlag) {
			detailTitle.setText("Welcome...");
		}
		else if (booksFlag) {
			detailTitle.setText("Books Collection");
			if (!table.getSelectionModel().isSelectionEmpty()) {
				Book book = (Book) tableModel.getElement(table.getSelectedRow());
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
	
	void actionsInit() {
		
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
		addAction = new CollectionsAction("Add "+collection.getItemName(), null, null, "add", 'A', false);
		addAction.putValue(Action.SHORT_DESCRIPTION, "Adds new "+collection.getItemName());
		editAction = new CollectionsAction("Edit "+collection.getItemName(), null, null, "edit", 'E', false);
		editAction.putValue(Action.SHORT_DESCRIPTION, "Edits highlighted "+collection.getItemName());
		removeAction = new CollectionsAction("Remove "+collection.getItemName(), null, null, "remove", 'R', false);
		removeAction.putValue(Action.SHORT_DESCRIPTION, "Removes highlighted "+collection.getItemName());
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
		upAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted "+collection.getItemName()+" up");
		downAction = new CollectionsAction("Move Down", downIcon, null, "down", KeyEvent.VK_DOWN, false);
		downAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted "+collection.getItemName()+" down");
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
		
		addAction.putValue(Action.NAME, "Add "+ collection.getItemName());
		addAction.putValue(Action.SHORT_DESCRIPTION, "Adds new "+collection.getItemName());
		editAction.putValue(Action.NAME, "Edit "+collection.getItemName());
		editAction.putValue(Action.SHORT_DESCRIPTION, "Edits highlighted "+collection.getItemName());
		removeAction.putValue(Action.NAME, "Remove "+collection.getItemName());
		removeAction.putValue(Action.SHORT_DESCRIPTION, "Removes highlighted "+collection.getItemName());
		upAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted "+collection.getItemName()+" up");
		downAction.putValue(Action.SHORT_DESCRIPTION, "Moves highlighted "+collection.getItemName()+" down");
		if(!homeFlag) {
			exportAction.putValue(Action.SHORT_DESCRIPTION, "Export "+collection.getItemName()+" collection to a file");
			exportAction.setEnabled(true);
		} else {
			exportAction.putValue(Action.SHORT_DESCRIPTION, "Export Collection to a file");
			exportAction.setEnabled(false);
		}
		addAction.setEnabled(homeFlag? false: true);
		editAction.setEnabled(listSelectionModel.isSelectionEmpty()? false: true);
		removeAction.setEnabled(listSelectionModel.isSelectionEmpty()? false: true);
		if(!collection.isStatusChanged()) saveAction.setEnabled(false);
		if(!areCollectionsChanged()) saveAllAction.setEnabled(false);
		clearAction.setEnabled(listSelectionModel.isSelectionEmpty()? false: true);
		resetAction.setEnabled(homeFlag? false: true);
		upAction.setEnabled(false);
		downAction.setEnabled(false);
	}
	
	private void switchCollection() {
		
		search = "";
		actionsUpdate();
		if(!homeFlag) tableUpdate();
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
					((element != null) && !(selectionFlag) && !(element.equals(tableModel.getElement(lsm.getAnchorSelectionIndex()))))) {
				detailPaneUpdate();
			} else selectionFlag = false;
			element = tableModel.getElement(lsm.getAnchorSelectionIndex());
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
	
	Collection<?> getCollection() {	return collection; }
	String getSearchText() { return search;	}
	
	Color getMainBackground() { return mainBackground; }
	Color getMainForeground() { return mainForeground; }
	Font getMainFont() { return mainFont; }
	
	Color getTableBackground() { return tableBackground; }
	Color getTableForeground() { return tableForeground; }
	Font getTableFont() { return tableFont; }
	
	Color getHighlightBackground() { return highlightBackground; }
	Color getHighlightForeground() { return highlightForeground; }
	Font getHighlightFont() { return highlightFont; }
	
	
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
		if(booksCollection != null) books = booksCollection.isStatusChanged();
		if(musicCollection != null) audio = musicCollection.isStatusChanged();
		if(gamesCollection != null) games = gamesCollection.isStatusChanged();
		if(moviesCollection != null) movies = moviesCollection.isStatusChanged();
		return (books || audio || games || movies);
	}
	
	
	boolean areOtherCollectionsChanged(Collection<?> collection) {
		
		boolean books, audio, games, movies;
		books = audio = games = movies = false;
		if(!collection.equals(booksCollection)) {
			if(booksCollection != null) books = booksCollection.isStatusChanged();
		}
		if(!collection.equals(musicCollection)) {
			if(musicCollection != null) audio = musicCollection.isStatusChanged();
		}
		if(!collection.equals(gamesCollection)) {
			if(gamesCollection != null) games = gamesCollection.isStatusChanged();
		}
		if(!collection.equals(moviesCollection)) {
			if(moviesCollection != null) movies = moviesCollection.isStatusChanged();
		}
		return (books || audio || games || movies);
	}
	
	
	private void saveAllCollections() {
		
		if(booksCollection != null) {
			if(booksCollection.isStatusChanged()) {
				booksCollection.saveCollection();
			}
		}
		if(musicCollection != null) {
			if(musicCollection.isStatusChanged()) {
				musicCollection.saveCollection();
			}
		}
		if(gamesCollection != null) {
			if(gamesCollection.isStatusChanged()) {
				gamesCollection.saveCollection();
			}
		}
		if(moviesCollection != null) {
			if(moviesCollection.isStatusChanged()) {
				moviesCollection.saveCollection();
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
		
		propertiesUpdate();
		
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
			homePane.setBackground(welcomeBackground);
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
			headerTable.setBackground(tableBackground);
			headerTable.repaint();
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
		export.setDialogTitle("Export your "+collection.getItemName()+" Collection");
		export.setCurrentDirectory(new File("/users/marcin/eclipse-workspace/collectionsapp/"));
		export.setSelectedFile(collection.getDBFile());
		export.setFileView(new CollectionsThumbNailView(this));
		export.setFileFilter(new CollectionsFileFilter("Collection files *.dat", "dat"));
		int option = export.showDialog(CollectionsApp.this, "Export");
		if(option == JFileChooser.APPROVE_OPTION) {
			File exported = export.getSelectedFile();
			int confirm = JOptionPane.showConfirmDialog(export, "Are you sure you want to export your "+collection.getItemName()+
			" Collection to "+exported.getName(), "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(confirm == JOptionPane.YES_OPTION) collection.exportTo(exported);
			else displayExportDialog();
		}
	}	
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if(e.getID() == WindowEvent.WINDOW_CLOSING) {
			if((saveAllAction.isEnabled() || areCollectionsChanged()) && (table.getRowCount() == collection.sizeOfDB())) {
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
				new AddNewOrEditDialog(CollectionsApp.this, table, collection);
			}
			else if(ae.getActionCommand().equals("edit")) {
				int row = table.getSelectedRow();
				new AddNewOrEditDialog(CollectionsApp.this, table, collection, row);
			}		
			else if(ae.getActionCommand().equals("remove")) {
				int row = table.getSelectedRow();
				Object item = tableModel.getElement(row);
				int option = JOptionPane.showConfirmDialog(CollectionsApp.this, "Do you really want to remove this "+collection.getItemName()+
						"?\n\n"+collection.printItem(item) +"\n\n", "Confirm to remove", JOptionPane.YES_NO_OPTION);
				if(option==JOptionPane.YES_OPTION) {
					tableModel.removeElement(item, row);
					saveAction.setEnabled(true);
					saveAllAction.setEnabled(true);				
				} else return;	
			}
			else if(ae.getActionCommand().equals("save")) {
				tableModel.updateAndSaveCollection();
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
				if(collection.sizeOfDB() == table.getRowCount()) {
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
				if(collection.sizeOfDB() == table.getRowCount()) {
					saveAction.setEnabled(true);
					saveAllAction.setEnabled(true);
				}
			}
			else if(ae.getActionCommand().equals("clear")) {
				clearSelection();
			}
			else if(ae.getActionCommand().equals("reset")) {
				if(saveAction.isEnabled() || tableModel.isModelChanged() || headerModel.isComboSelected()) {
					int confirm = JOptionPane.showConfirmDialog(CollectionsApp.this, "Resetting Collection will void all unsaved changes"
							+ "\ndo you want to continue anyway?", "Confirm reset", JOptionPane.YES_NO_OPTION);
					if(confirm == JOptionPane.NO_OPTION) return;
					clearSelection();
					collection.reload();
					tableModel.reloadTableModel();
					headerModel.resetModel();
					initHeaderRenderers();
					saveAction.setEnabled(false);
					if(!areOtherCollectionsChanged(collection)) {
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
				ImportAccessory ia = new ImportAccessory(collection, homeFlag);
				imp.setDialogTitle("Import "+collection.getItemName()+" Collection");
				imp.setCurrentDirectory(new File("/users/marcin/eclipse-workspace/collectionsapp/"));
				imp.setAccessory(ia);
				imp.setFileFilter(new CollectionsFileFilter("Collection Files *.dat", "dat"));
				imp.setFileView(new CollectionsThumbNailView(CollectionsApp.this));
				imp.addPropertyChangeListener(ia);
				int option = imp.showDialog(CollectionsApp.this, "Import");
				if(option == JFileChooser.APPROVE_OPTION) {
					File imported = imp.getSelectedFile();
					try {
						if(ia.getCollectionsName().equals(BooksCollection.getName())) {
							booksCollection = new BooksCollection(imported);
							btnBooks.doClick();
						} else if(ia.getCollectionsName().equals(GamesCollection.getName())) {
							gamesCollection = new GamesCollection(imported);
							btnGames.doClick();
						} else if(ia.getCollectionsName().equals(MoviesCollection.getName())) {
							moviesCollection = new MoviesCollection(imported);
							btnMovies.doClick();
						} else if(ia.getCollectionsName().equals(MusicCollection.getName())) {
							musicCollection = new MusicCollection(imported);
							btnMusic.doClick();
						} else return;
					} catch (IOException exc) {exc.printStackTrace();}
					centralPaneUpdate();
				}
			}
			else if(ae.getActionCommand().equals("export")) {
				displayExportDialog();
			}
			else if(ae.getActionCommand().equals("home")) {
				btnHome.setBorder(lowered);
				btnHome.setBackground(btnHome.getBackground().darker());
				collection = itemCollection;
				resetBtnFlags();
				homeFlag = true;
				switchCollection();
			}
			else if(ae.getActionCommand().equals("books")) {
				btnBooks.setBorder(lowered);
				btnBooks.setBackground(btnBooks.getBackground().darker());
				if(booksCollection == null) booksCollection = new BooksCollection();
				collection = booksCollection;
				resetBtnFlags();
				booksFlag = true;
				switchCollection();
			}
			else if(ae.getActionCommand().equals("music")) {
				btnMusic.setBorder(lowered);
				btnMusic.setBackground(btnMusic.getBackground().darker());
				if(musicCollection == null) musicCollection = new MusicCollection();
				collection = musicCollection;
				resetBtnFlags();
				musicFlag = true;
				switchCollection();
			}
			else if(ae.getActionCommand().equals("movies")) {
				btnMovies.setBorder(lowered);
				btnMovies.setBackground(btnMovies.getBackground().darker());
				if(moviesCollection == null) moviesCollection = new MoviesCollection();
				collection = moviesCollection;
				resetBtnFlags();
				moviesFlag = true;
				switchCollection();
			}
			else if(ae.getActionCommand().equals("games")) {
				btnGames.setBorder(lowered);
				btnGames.setBackground(btnGames.getBackground().darker());
				if(gamesCollection == null) gamesCollection = new GamesCollection();
				collection = gamesCollection;
				resetBtnFlags();
				gamesFlag = true;
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
				Appearance chooser = new Appearance(CollectionsApp.this, sc, styleNames);
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
