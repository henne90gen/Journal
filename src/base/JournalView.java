package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class JournalView extends JFrame {

    private static final int WIDTH = 900;
    private static final int HEIGHT = 550;

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

    private Journal journal;
    private JournalViewListener listener;
    JTextPane commentTP;
    JList<Entry> dateList;
	JTextField searchTF;
    JRadioButton[] feelings;
	JButton newBtn, searchBtn, exportBtn, importBtn, editBtn, deleteBtn;
    JRadioButton dateSearchRB, stringSearchRB;
	
	JournalView(Journal journal) {
		super("My First Application");
		this.journal = journal;
        listener = new JournalViewListener(journal);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        // TODO switch to GroupLayout to make window resizable
        setResizable(false);
        addWindowListener(journal);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createSearchPanel());
        panel.add(createShowPanel());
        add(panel);

        pack();
	}

	void update() {
        setUIEnabled(true);

	    commentTP.setText("");
        Entry[] entries = new Entry[journal.data.entries.size()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = journal.data.entries.get(i);
        }
        dateList.setListData(entries);
        dateList.setSelectedIndex(0);
        searchTF.setText("");
        dateList.requestFocus();
    }

    void setUIEnabled(boolean enabled) {
        newBtn.setEnabled(enabled);
        editBtn.setEnabled(enabled);
        deleteBtn.setEnabled(enabled);
        searchBtn.setEnabled(enabled);
        importBtn.setEnabled(enabled);
        exportBtn.setEnabled(enabled);

        stringSearchRB.setEnabled(enabled);
        dateSearchRB.setEnabled(enabled);
        searchTF.setEnabled(enabled);
        dateList.setEnabled(enabled);
    }

	private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Import button
        importBtn = new JButton(IMPORT_BUTTON);
        importBtn.addActionListener(listener);
        importBtn.setActionCommand(IMPORT_BUTTON);
        importBtn.setEnabled(false);
        panel.add(importBtn);

        // Export button
        exportBtn = new JButton(EXPORT_BUTTON);
        exportBtn.addActionListener(listener);
        exportBtn.setActionCommand(EXPORT_BUTTON);
        exportBtn.setEnabled(false);
        panel.add(exportBtn);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));

        // Search text field
        searchTF = new JTextField();
        searchTF.setActionCommand(SEARCH_BUTTON);
        searchTF.addActionListener(listener);
        searchTF.setEnabled(false);
        searchTF.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) && dateSearchRB.isSelected()) {
                    e.consume();
                }
            }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
        searchTF.getDocument().addDocumentListener(listener);
        searchTF.setToolTipText("Date format: mm.dd.yyyy");
        panel.add(searchTF);

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
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(stringSearchRB);
        panel.add(dateSearchRB);

        // Search button
        searchBtn = new JButton(SEARCH_BUTTON);
        searchBtn.addActionListener(listener);
        searchBtn.setActionCommand(SEARCH_BUTTON);
        searchBtn.setEnabled(false);
        panel.add(searchBtn);

	    return panel;
    }

    private JPanel createShowPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // List
        dateList = new JList<>();
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dateList.setVisibleRowCount(2);
        dateList.addListSelectionListener(listener);
        JScrollPane listScrollPane = new JScrollPane(dateList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScrollPane.setPreferredSize(new Dimension(130, 430));
        listScrollPane.setMinimumSize(new Dimension(130, 430));
        panel.add(listScrollPane);

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        JPanel commentBtnPanel = new JPanel();
        commentBtnPanel.setLayout(new BoxLayout(commentBtnPanel, BoxLayout.X_AXIS));


        // New button
        newBtn = new JButton(NEW_BUTTON);
        newBtn.addActionListener(listener);
        newBtn.setActionCommand(NEW_BUTTON);
        newBtn.setEnabled(false);
        commentBtnPanel.add(newBtn);

        // Edit button
        editBtn = new JButton(EDIT_BUTTON);
        editBtn.addActionListener(listener);
        editBtn.setActionCommand(EDIT_BUTTON);
        editBtn.setEnabled(false);
        commentBtnPanel.add(editBtn);

        // Delete button
        deleteBtn = new JButton(DELETE_BUTTON);
        deleteBtn.addActionListener(listener);
        deleteBtn.setActionCommand(DELETE_BUTTON);
        deleteBtn.setEnabled(false);
        commentBtnPanel.add(deleteBtn);
        commentBtnPanel.add(Box.createGlue());
        commentPanel.add(commentBtnPanel);

        // Mood selection
        JPanel moodPanel = new JPanel();
        moodPanel.setLayout(new GridLayout(0, 7));
        moodPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        feelings = new JRadioButton[Entry.Mood.values().length];
        for (int i = 0; i < Entry.Mood.values().length; i++) {
            feelings[i] = new JRadioButton(Entry.Mood.values()[i].toString());
            feelings[i].setEnabled(false);
        }
        ButtonGroup group = new ButtonGroup();
        for (JRadioButton feeling : feelings) {
            group.add(feeling);
            moodPanel.add(feeling);
        }
        feelings[feelings.length/2].setSelected(true);
        commentPanel.add(moodPanel);

        // Text field
        commentTP = new JTextPane();
        commentTP.setEditable(false);
        commentTP.setText(LOADING_APPLICATION);
        JScrollPane textScrollPane = new JScrollPane(commentTP);
        textScrollPane.setPreferredSize(new Dimension(500, 430));
        commentPanel.add(textScrollPane);

        panel.add(commentPanel);
        return panel;
    }
}
