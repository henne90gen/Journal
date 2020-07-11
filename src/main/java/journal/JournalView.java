package journal;

import journal.data.JournalEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

class JournalView extends JFrame {

	private static final int WIDTH = 900;
	private static final int HEIGHT = 600;
	private static final int MIN_WIDTH = 600;
	private static final int MIN_HEIGHT = 300;
	private static final int DATE_LIST_WIDTH = 140;

	static final String NEW_BUTTON = "New";
	static final String EDIT_BUTTON = "Edit";
	static final String SAVE_BUTTON = "Save";
	static final String CANCEL_BUTTON = "Cancel";
	static final String DELETE_BUTTON = "Delete";
	static final String SEARCH_BUTTON = "Search";
	static final String EXPORT_BUTTON = "Export";
	static final String IMPORT_BUTTON = "Import";
	static final String SYNC_BUTTON = "Sync";
	static final String SEARCH_RADIO_BUTTON = "Search radio button";
	static final String NOTHING_TO_DISPLAY = "Nothing to display";
	static final String LOADING_APPLICATION = "Loading database";
	static final String SHOW_DELETED = "Show deleted";

	private final Journal journal;
	private final JournalViewListener listener;

	JTextPane commentTP;
	JScrollPane commentScrollPane;
	JList<JournalEntry> entryList;
	JScrollPane entryListScrollPane;
	JTextField searchTF;
	JPanel moodPanel;
	JRadioButton[] feelings;
	JButton newBtn, editBtn, deleteBtn;
	JMenuItem importItem, exportItem, syncItem;
	JRadioButton dateSearchRB, stringSearchRB;
	JCheckBox showDeletedCB;
	JTextField dayTF, monthTF, yearTF;

	public JournalView(Journal journal) {
		super("Journal");
		this.journal = journal;
		listener = new JournalViewListener(journal);
		journal.data.addUpdateCallback(this::update);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		addWindowListener(this.journal);
		setVisible(true);

		initComponents();

		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(createHorizontalLayout(layout));
		layout.setVerticalGroup(createVerticalLayout(layout));
		panel.setLayout(layout);
		add(panel);

		pack();
	}

	private GroupLayout.Group createVerticalLayout(GroupLayout layout) {
		return layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(searchTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(stringSearchRB)
						.addComponent(dateSearchRB)
						.addComponent(showDeletedCB))
				.addGroup(layout.createParallelGroup()
						.addComponent(entryListScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(newBtn)
										.addComponent(editBtn)
										.addComponent(deleteBtn))
								.addGroup(layout.createParallelGroup()
										.addComponent(dayTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(monthTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(yearTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createParallelGroup()
										.addComponent(feelings[0])
										.addComponent(feelings[1])
										.addComponent(feelings[2])
										.addComponent(feelings[3])
										.addComponent(feelings[4])
										.addComponent(feelings[5])
										.addComponent(feelings[6]))
								.addComponent(commentScrollPane)));
	}

	private GroupLayout.Group createHorizontalLayout(GroupLayout layout) {
		return layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
						.addComponent(searchTF)
						.addComponent(stringSearchRB)
						.addComponent(dateSearchRB)
						.addComponent(showDeletedCB))
				.addGroup(layout.createSequentialGroup()
						.addComponent(entryListScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(newBtn)
										.addComponent(editBtn)
										.addComponent(deleteBtn))
								.addGroup(layout.createSequentialGroup()
										.addComponent(dayTF)
										.addComponent(monthTF)
										.addComponent(yearTF))
								.addGroup(layout.createSequentialGroup()
										.addComponent(feelings[0])
										.addComponent(feelings[1])
										.addComponent(feelings[2])
										.addComponent(feelings[3])
										.addComponent(feelings[4])
										.addComponent(feelings[5])
										.addComponent(feelings[6]))
								.addComponent(commentScrollPane)));
	}

	public void update() {
		setUIEnabled(true);

		commentTP.setText("");
		searchTF.setText("");

		int previouslySelectedIndex = entryList.getSelectedIndex();
		List<JournalEntry> entriesList = journal.data.getAllEntries(showDeletedCB.isSelected());
		JournalEntry[] entries = new JournalEntry[entriesList.size()];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = entriesList.get(i);
		}
		entryList.setListData(entries);

		if (previouslySelectedIndex >= entries.length) {
			previouslySelectedIndex = entries.length - 1;
		}
		if (previouslySelectedIndex < 0) {
			previouslySelectedIndex = 0;
		}
		if (!journal.isInitialized) {
			previouslySelectedIndex = entriesList.size() - 1;
		}
		entryList.setSelectedIndex(previouslySelectedIndex);

		entryList.requestFocus();
	}

	void setUIEnabled(boolean enabled) {
		newBtn.setEnabled(enabled);
		editBtn.setEnabled(enabled);
		deleteBtn.setEnabled(enabled);

		dayTF.setEnabled(!enabled);
		dayTF.setEditable(!enabled);
		monthTF.setEnabled(!enabled);
		monthTF.setEditable(!enabled);
		yearTF.setEnabled(!enabled);
		yearTF.setEditable(!enabled);

		stringSearchRB.setEnabled(enabled);
		dateSearchRB.setEnabled(enabled);
		searchTF.setEnabled(enabled);
		entryList.setEnabled(enabled);

		showDeletedCB.setEnabled(enabled);
	}

	private void initComponents() {
		initMenuBar();
		initSearchBar();
		initSideBar();

		// Buttons
		newBtn = createButton(NEW_BUTTON);
		editBtn = createButton(EDIT_BUTTON);
		deleteBtn = createButton(DELETE_BUTTON);

		dayTF = new JTextField();
		dayTF.setEditable(false);
		dayTF.setEnabled(false);
		monthTF = new JTextField();
		monthTF.setEditable(false);
		monthTF.setEnabled(false);
		yearTF = new JTextField();
		yearTF.setEditable(false);
		yearTF.setEnabled(false);

		initMoodSelection();
		initCommentTextField();
	}

	private void initCommentTextField() {
		commentTP = new JTextPane();
		commentTP.setEditable(false);
		commentTP.setText(LOADING_APPLICATION);
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.saveButtonPressed();
			}
		};
		KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
		commentTP.getInputMap().put(keyStroke, SAVE_BUTTON);
		commentTP.getActionMap().put(SAVE_BUTTON, action);
		setFontSize(commentTP, 18.0F);
		commentScrollPane = new JScrollPane(commentTP);
	}

	private void initMoodSelection() {
		moodPanel = new JPanel();
		moodPanel.setLayout(new GridLayout(0, 7));
		moodPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		feelings = new JRadioButton[JournalEntry.Mood.values().length];
		for (int i = 0; i < JournalEntry.Mood.values().length; i++) {
			feelings[i] = new JRadioButton(JournalEntry.Mood.values()[i].toString());
			feelings[i].setEnabled(false);
		}
		ButtonGroup group = new ButtonGroup();
		for (JRadioButton feeling : feelings) {
			group.add(feeling);
			moodPanel.add(feeling);
		}
		feelings[feelings.length / 2].setSelected(true);
	}

	private void initSideBar() {
		entryList = new JList<>();
		entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		entryList.setVisibleRowCount(2);
		entryList.addListSelectionListener(listener);
		setFontSize(entryList, 15.0F);
		entryListScrollPane = new JScrollPane(entryList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		entryListScrollPane.setPreferredSize(new Dimension(DATE_LIST_WIDTH, 0));
	}

	private void initSearchBar() {
		// Search text field
		initSearchTextField();

		// Search type
		ButtonGroup group = new ButtonGroup();

		// String search
		stringSearchRB = new JRadioButton("String");
		stringSearchRB.setSelected(true);
		stringSearchRB.setActionCommand(SEARCH_RADIO_BUTTON);
		stringSearchRB.addActionListener(listener);
		stringSearchRB.setEnabled(false);
		group.add(stringSearchRB);

		// Date search
		dateSearchRB = new JRadioButton("Date");
		dateSearchRB.setActionCommand(SEARCH_RADIO_BUTTON);
		dateSearchRB.addActionListener(listener);
		dateSearchRB.setEnabled(false);
		group.add(dateSearchRB);

		// Show deleted
		showDeletedCB = new JCheckBox(SHOW_DELETED);
		showDeletedCB.addItemListener((event) -> SwingUtilities.invokeLater(this::update));
	}

	private void initSearchTextField() {
		searchTF = new JTextField();
		searchTF.setPreferredSize(new Dimension(0, 25));
		searchTF.setActionCommand(SEARCH_BUTTON);
		searchTF.addActionListener(listener);
		searchTF.setEnabled(false);
		searchTF.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar()) && dateSearchRB.isSelected()) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		searchTF.getDocument().addDocumentListener(listener);
		searchTF.setToolTipText("Date format: mm.dd.yyyy");
	}

	private void initMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.getAccessibleContext().setAccessibleDescription("File");
		menuBar.add(fileMenu);

		importItem = new JMenuItem(IMPORT_BUTTON, KeyEvent.VK_I);
		importItem.setMnemonic(KeyEvent.VK_I);
		importItem.addActionListener(listener);
		importItem.setActionCommand(IMPORT_BUTTON);
		fileMenu.add(importItem);

		exportItem = new JMenuItem(EXPORT_BUTTON, KeyEvent.VK_E);
		exportItem.setMnemonic(KeyEvent.VK_E);
		exportItem.addActionListener(listener);
		exportItem.setActionCommand(EXPORT_BUTTON);
		fileMenu.add(exportItem);

		syncItem = new JMenuItem(SYNC_BUTTON, KeyEvent.VK_S);
		syncItem.setMnemonic(KeyEvent.VK_S);
		syncItem.addActionListener(listener);
		syncItem.setActionCommand(SYNC_BUTTON);
		menuBar.add(syncItem);

		setJMenuBar(menuBar);
	}

	private JButton createButton(String text) {
		JButton btn = new JButton(text);
		btn.addActionListener(listener);
		btn.setActionCommand(text);
		btn.setEnabled(false);
		return btn;
	}

	private void setFontSize(Component component, float newFontSize) {
		Font font = component.getFont();
		font = font.deriveFont(newFontSize);
		component.setFont(font);
	}
}
