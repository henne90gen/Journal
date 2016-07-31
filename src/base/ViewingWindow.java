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

public class ViewingWindow extends JFrame implements ListSelectionListener, ActionListener, DocumentListener {

    private static final String NEW_BUTTON = "New";
    private static final String SEARCH_BUTTON = "Search";
    private static final String RELOAD_BUTTON = "Reload";

    private Journal journal;
    private JTextPane textPane;
    private JList<Entry> dateList;
	private JTextField searchTF;
	private JButton newBtn, searchBtn, reloadBtn;
	
	public ViewingWindow(Journal journal) {
		super("My First Application");
		this.journal = journal;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 500));
        setMinimumSize(new Dimension(700, 500));

		add(createGUI());

        pack();
        setVisible(true);

        updateUI();
	}

	public void updateUI() {
        Entry[] entries = new Entry[journal.getEntries().size()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = journal.getEntries().get(i);
        }
        dateList.setListData(entries);
        dateList.setSelectedIndex(0);
        searchTF.setText("");
    }
	
	public JPanel createGUI() {
		// Panels
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // New button
        newBtn = new JButton(NEW_BUTTON);
        newBtn.addActionListener(this);
        newBtn.setActionCommand(NEW_BUTTON);
        topPanel.add(newBtn);

		// Search text
        JLabel searchL = new JLabel("Type in a date:");
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        topPanel.add(searchL);

        // Search text field
        searchTF = new JTextField();
        searchTF.setActionCommand(SEARCH_BUTTON);
        searchTF.addActionListener(this);
        searchTF.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
            }
            @Override public void keyPressed(KeyEvent e) {}
            @Override public void keyReleased(KeyEvent e) {}
        });
        searchTF.getDocument().addDocumentListener(this);
        searchTF.setToolTipText("Date format: mm.dd.yyyy");
        topPanel.add(searchTF);

        // Search button
        searchBtn = new JButton(SEARCH_BUTTON);
        searchBtn.addActionListener(this);
        searchBtn.setActionCommand(SEARCH_BUTTON);
        topPanel.add(searchBtn);

		// Reload button
        reloadBtn = new JButton(RELOAD_BUTTON);
		reloadBtn.addActionListener(this);
        reloadBtn.setActionCommand(RELOAD_BUTTON);
		topPanel.add(reloadBtn);
		

        // List
        dateList = new JList<>();
		dateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dateList.setVisibleRowCount(2);
		dateList.addListSelectionListener(this);
		
		JScrollPane listScrollPane = new JScrollPane(dateList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setPreferredSize(new Dimension(130, 430));
		bottomPanel.add(listScrollPane);
		
		// Text field
        textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane textScrollPane = new JScrollPane(textPane);
		textScrollPane.setPreferredSize(new Dimension(500, 430));
		bottomPanel.add(textScrollPane);

		mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);
		return mainPanel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
	    if (dateList.getSelectedIndex() != -1) {
            textPane.setText(dateList.getSelectedValue().getText());
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    switch (e.getActionCommand()) {
            case NEW_BUTTON:
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        NewEntryWindow window = new NewEntryWindow(journal);
                        window.addWindowListener(journal);
                    }
                });
                break;
            case RELOAD_BUTTON:
                updateUI();
                break;
            case SEARCH_BUTTON:
                // Adding dot after day and month automatically
                if (searchTF.getText().length() == 2 || searchTF.getText().length() == 5) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            searchTF.setText(searchTF.getText() + ".");
                        }
                    });
                    break;
                }

                // Search
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
                dateList.setListData(journal.searchForDate(date[1], date[0], date[2]));
                dateList.setSelectedIndex(0);
                break;
        }
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
