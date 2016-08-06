package base;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

class ViewingWindow extends JFrame implements ListSelectionListener, ActionListener, DocumentListener {

    private static final String NEW_BUTTON = "New";
    private static final String EDIT_BUTTON = "Edit";
    private static final String SAVE_BUTTON = "Save";
    private static final String DELETE_BUTTON = "Delete";
    private static final String SEARCH_BUTTON = "Search";
    private static final String EXPORT_BUTTON = "Export";
    private static final String IMPORT_BUTTON = "Import";
    private static final String SEARCH_RADIO_BUTTON = "Search radio button";
    private static final String NOTHING_TO_DISPLAY = "Nothing to display";
    private static final String LOADING_APPLICATION = "Loading database";

    private Journal journal;
    private JTextPane commentTP;
    JList<Entry> dateList;
	private JTextField searchTF;
	private JButton newBtn, searchBtn, exportBtn, importBtn, editBtn, deleteBtn;
    private JRadioButton dateSearchRB, stringSearchRB;
	
	ViewingWindow(Journal journal) {
		super("My First Application");
		this.journal = journal;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 500));
        setMinimumSize(new Dimension(700, 500));
        addWindowListener(journal);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createSearchPanel());
        panel.add(createShowPanel());
        add(panel);

        pack();
	}

	void updateUI() {
        setUIEnabled(true);

	    commentTP.setText("");
        Entry[] entries = new Entry[journal.entries.size()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = journal.entries.get(i);
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

	JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // New button
        newBtn = new JButton(NEW_BUTTON);
        newBtn.addActionListener(this);
        newBtn.setActionCommand(NEW_BUTTON);
        newBtn.setEnabled(false);
        panel.add(newBtn);

        // Search type
        ButtonGroup group = new ButtonGroup();
        // String search
        stringSearchRB = new JRadioButton("String");
        stringSearchRB.setSelected(true);
        stringSearchRB.setActionCommand(SEARCH_RADIO_BUTTON);
        stringSearchRB.addActionListener(this);
        stringSearchRB.setEnabled(false);
        group.add(stringSearchRB);
        // Date search
        dateSearchRB = new JRadioButton("Date");
        dateSearchRB.setActionCommand(SEARCH_RADIO_BUTTON);
        dateSearchRB.addActionListener(this);
        dateSearchRB.setEnabled(false);
        group.add(dateSearchRB);
        panel.add(Box.createRigidArea(new Dimension(5, 0)));
        panel.add(stringSearchRB);
        panel.add(dateSearchRB);

        // Search text field
        searchTF = new JTextField();
        searchTF.setActionCommand(SEARCH_BUTTON);
        searchTF.addActionListener(this);
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
        searchTF.getDocument().addDocumentListener(this);
        searchTF.setToolTipText("Date format: mm.dd.yyyy");
        panel.add(searchTF);

        // Search button
        searchBtn = new JButton(SEARCH_BUTTON);
        searchBtn.addActionListener(this);
        searchBtn.setActionCommand(SEARCH_BUTTON);
        searchBtn.setEnabled(false);
        panel.add(searchBtn);

        // Load button
        importBtn = new JButton(IMPORT_BUTTON);
        importBtn.addActionListener(this);
        importBtn.setActionCommand(IMPORT_BUTTON);
        importBtn.setEnabled(false);
        panel.add(importBtn);

        // Save button
        exportBtn = new JButton(EXPORT_BUTTON);
        exportBtn.addActionListener(this);
        exportBtn.setActionCommand(EXPORT_BUTTON);
        exportBtn.setEnabled(false);
        panel.add(exportBtn);

	    return panel;
    }

    JPanel createShowPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // List
        dateList = new JList<>();
        dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dateList.setVisibleRowCount(2);
        dateList.addListSelectionListener(this);
        JScrollPane listScrollPane = new JScrollPane(dateList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScrollPane.setPreferredSize(new Dimension(130, 430));
        panel.add(listScrollPane);

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(commentPanel, BoxLayout.Y_AXIS));
        JPanel commentBtnPanel = new JPanel();
        commentBtnPanel.setLayout(new BoxLayout(commentBtnPanel, BoxLayout.X_AXIS));

        // Edit button
        editBtn = new JButton(EDIT_BUTTON);
        editBtn.addActionListener(this);
        editBtn.setActionCommand(EDIT_BUTTON);
        editBtn.setEnabled(false);
        commentBtnPanel.add(editBtn);

        // Delete button
        deleteBtn = new JButton(DELETE_BUTTON);
        deleteBtn.addActionListener(this);
        deleteBtn.setActionCommand(DELETE_BUTTON);
        deleteBtn.setEnabled(false);
        commentBtnPanel.add(deleteBtn);
        commentBtnPanel.add(Box.createGlue());
        commentPanel.add(commentBtnPanel);

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

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    if (dateList.getSelectedIndex() != -1) {
            commentTP.setText(dateList.getSelectedValue().comment);
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    switch (e.getActionCommand()) {
            case NEW_BUTTON:
                SwingUtilities.invokeLater(() -> {
                    NewEntryWindow window = new NewEntryWindow(journal);
                    window.addWindowListener(journal);
                });
                break;
            case EDIT_BUTTON:
                setUIEnabled(false);
                editBtn.setEnabled(true);
                commentTP.setEditable(true);
                editBtn.setText(SAVE_BUTTON);
                editBtn.setActionCommand(SAVE_BUTTON);
                break;
            case SAVE_BUTTON:
                setUIEnabled(true);
                commentTP.setEditable(false);
                editBtn.setText(EDIT_BUTTON);
                editBtn.setActionCommand(EDIT_BUTTON);
                int index = dateList.getSelectedIndex();
                dateList.getSelectedValue().comment = commentTP.getText();
                journal.edit(dateList.getSelectedValue());
                dateList.setSelectedIndex(index);
                break;
            case DELETE_BUTTON:
                journal.delete(dateList.getSelectedValue());
                break;
            case EXPORT_BUTTON: {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("./"));
                int returnVal = fc.showSaveDialog(ViewingWindow.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    journal.writeToFile(fc.getSelectedFile().getPath());
                }
            }
                break;
            case IMPORT_BUTTON: {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("./"));
                int returnVal = fc.showOpenDialog(ViewingWindow.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    journal.readFromFile(fc.getSelectedFile().getPath());
                }
                updateUI();
            }
                break;
            case SEARCH_BUTTON:
                if (dateSearchRB.isSelected())
                    searchDate();
                else if (stringSearchRB.isSelected())
                    searchString();
                break;
            case SEARCH_RADIO_BUTTON:
                SwingUtilities.invokeLater(() -> {
                    searchTF.setText("");
                    updateUI();
                });
                break;
        }
    }

    private void searchDate() {
        // Adding dot after day and month automatically
        if (searchTF.getText().length() == 2 || searchTF.getText().length() == 5) {
            SwingUtilities.invokeLater(() -> searchTF.setText(searchTF.getText() + "."));
            return;
        }

        // Getting date from searchTF
        String[] tmp = searchTF.getText().split("\\.");
        int[] date = {-1, -1, -1};
        if (tmp.length == 1 && !tmp[0].equalsIgnoreCase("")) {
            date[0] = Integer.parseInt(tmp[0]);
        } else if (tmp.length == 2) {
            date[0] = Integer.parseInt(tmp[0]);
            date[1] = Integer.parseInt(tmp[1]);
        } else if (tmp.length == 3) {
            date[0] = Integer.parseInt(tmp[0]);
            date[1] = Integer.parseInt(tmp[1]);
            date[2] = Integer.parseInt(tmp[2]);
        }
        Entry[] es = journal.searchForDate(date[1], date[0], date[2]);
        dateList.setListData(es);
        if (es.length == 0) {
            commentTP.setText(NOTHING_TO_DISPLAY);
        } else {
            dateList.setSelectedIndex(0);
        }
    }

    private void searchString() {
        dateList.setListData(journal.searchForString(searchTF.getText()));
        dateList.setSelectedIndex(0);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        actionPerformed(new ActionEvent(searchTF, 0, SEARCH_BUTTON));
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (searchTF.getText().length() != 2 && searchTF.getText().length() != 5) {
            actionPerformed(new ActionEvent(searchTF, 0, SEARCH_BUTTON));
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        actionPerformed(new ActionEvent(searchTF, 0, SEARCH_BUTTON));
    }
}
