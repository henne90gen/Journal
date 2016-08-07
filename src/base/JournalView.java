package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class JournalView extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 400;
    private static final int DATE_LIST_WIDTH = 130;

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
    JScrollPane commentScrollPane;
    JList<Entry> dateList;
    JScrollPane dateListScrollPane;
    JTextField searchTF;
    JPanel moodPanel;
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
        addWindowListener(this.journal);
        setVisible(true);

        initComponents();

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(importBtn)
                                .addComponent(exportBtn)
                                .addComponent(searchTF)
                                .addComponent(stringSearchRB)
                                .addComponent(dateSearchRB)
                                .addComponent(searchBtn))
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
                                        .addComponent(commentScrollPane)))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup()
                                .addComponent(importBtn)
                                .addComponent(exportBtn)
                                .addComponent(searchTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(stringSearchRB)
                                .addComponent(dateSearchRB)
                                .addComponent(searchBtn))
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
                                        .addComponent(commentScrollPane)))
        );
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

    private void initComponents() {
        importBtn = new JButton(IMPORT_BUTTON);
        importBtn.addActionListener(listener);
        importBtn.setActionCommand(IMPORT_BUTTON);
        importBtn.setEnabled(false);

        // Export button
        exportBtn = new JButton(EXPORT_BUTTON);
        exportBtn.addActionListener(listener);
        exportBtn.setActionCommand(EXPORT_BUTTON);
        exportBtn.setEnabled(false);

        // Search text field
        searchTF = new JTextField();
        searchTF.setPreferredSize(new Dimension(0, 25));
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

        // Search button
        searchBtn = new JButton(SEARCH_BUTTON);
        searchBtn.addActionListener(listener);
        searchBtn.setActionCommand(SEARCH_BUTTON);
        searchBtn.setEnabled(false);



        // List of dates
        dateList = new JList<>();
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dateList.setVisibleRowCount(2);
        dateList.addListSelectionListener(listener);
        dateListScrollPane = new JScrollPane(dateList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dateListScrollPane.setPreferredSize(new Dimension(DATE_LIST_WIDTH, 0));

        // New button
        newBtn = new JButton(NEW_BUTTON);
        newBtn.addActionListener(listener);
        newBtn.setActionCommand(NEW_BUTTON);
        newBtn.setEnabled(false);

        // Edit button
        editBtn = new JButton(EDIT_BUTTON);
        editBtn.addActionListener(listener);
        editBtn.setActionCommand(EDIT_BUTTON);
        editBtn.setEnabled(false);

        // Delete button
        deleteBtn = new JButton(DELETE_BUTTON);
        deleteBtn.addActionListener(listener);
        deleteBtn.setActionCommand(DELETE_BUTTON);
        deleteBtn.setEnabled(false);

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
        feelings[feelings.length/2].setSelected(true);

        // Text field
        commentTP = new JTextPane();
        commentTP.setEditable(false);
        commentTP.setText(LOADING_APPLICATION);
        commentScrollPane = new JScrollPane(commentTP);
    }
}
