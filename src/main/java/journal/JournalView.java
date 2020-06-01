package journal;

import journal.data.Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

class JournalView extends JFrame {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 400;
	private static final int DATE_LIST_WIDTH = 140;

	static final String NEW_BUTTON = "New";
	static final String EDIT_BUTTON = "Edit";
	static final String SAVE_BUTTON = "Save";
	static final String DELETE_BUTTON = "Delete";
	static final String SEARCH_BUTTON = "Search";
	static final String EXPORT_BUTTON = "Export";
	static final String IMPORT_BUTTON = "Import";
	static final String SEARCH_RADIO_BUTTON = "Search radio button";
	static final String NOTHING_TO_DISPLAY = "Nothing to display";
	static final String LOADING_APPLICATION = "Loading database";

	private final Journal journal;
	private final JournalViewListener listener;

	JTextPane commentTP;
	JScrollPane commentScrollPane;
	JList<Entry> dateList;
	JScrollPane dateListScrollPane;
	JTextField searchTF;
	JPanel moodPanel;
	JRadioButton[] feelings;
	JButton newBtn, editBtn, deleteBtn;
	JMenuItem importItem, exportItem;
	JRadioButton dateSearchRB, stringSearchRB;

	public JournalView(Journal journal) {
		super("Journal");
		this.journal = journal;
		listener = new JournalViewListener(journal);
		journal.data.addUpdateCallback(this::update);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
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
						.addComponent(dateSearchRB))
				.addGroup(layout.createParallelGroup()
						.addComponent(dateListScrollPane)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup()
										.addComponent(newBtn)
										.addComponent(editBtn)
										.addComponent(deleteBtn))
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
						.addComponent(dateSearchRB))
				.addGroup(layout.createSequentialGroup()
						.addComponent(dateListScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createParallelGroup()
								.addGroup(layout.createSequentialGroup()
										.addComponent(newBtn)
										.addComponent(editBtn)
										.addComponent(deleteBtn))
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
		List<Entry> entriesList = journal.data.getAllEntries();
		Entry[] entries = new Entry[entriesList.size()];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = entriesList.get(i);
		}
		dateList.setListData(entries);
		dateList.setSelectedIndex(0);
		dateList.requestFocus();
		searchTF.setText("");
	}

	void setUIEnabled(boolean enabled) {
		newBtn.setEnabled(enabled);
		editBtn.setEnabled(enabled);
		deleteBtn.setEnabled(enabled);

		stringSearchRB.setEnabled(enabled);
		dateSearchRB.setEnabled(enabled);
		searchTF.setEnabled(enabled);
		dateList.setEnabled(enabled);
	}

	private void initComponents() {
		initMenuBar();

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

		// List of dates
		dateList = new JList<>();
		dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dateList.setVisibleRowCount(2);
		dateList.addListSelectionListener(listener);
		dateListScrollPane = new JScrollPane(dateList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dateListScrollPane.setPreferredSize(new Dimension(DATE_LIST_WIDTH, 0));

		// Buttons
		newBtn = createButton(NEW_BUTTON);
		editBtn = createButton(EDIT_BUTTON);
		deleteBtn = createButton(DELETE_BUTTON);

		// Mood selection
		moodPanel = new JPanel();
		moodPanel.setLayout(new GridLayout(0, 7));
		moodPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		feelings = new JRadioButton[Entry.Mood.values().length];
		for (int i = 0; i < Entry.Mood.values().length; i++) {
			feelings[i] = new JRadioButton(Entry.Mood.values()[i].toString());
			feelings[i].setEnabled(false);
		}
		group = new ButtonGroup();
		for (JRadioButton feeling : feelings) {
			group.add(feeling);
			moodPanel.add(feeling);
		}
		feelings[feelings.length / 2].setSelected(true);

		// Text field
		commentTP = new JTextPane();
		commentTP.setEditable(false);
		commentTP.setText(LOADING_APPLICATION);
		commentScrollPane = new JScrollPane(commentTP);
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

		setJMenuBar(menuBar);
	}

	private JButton createButton(String text) {
		JButton btn = new JButton(text);
		btn.addActionListener(listener);
		btn.setActionCommand(text);
		btn.setEnabled(false);
		return btn;
	}
}
