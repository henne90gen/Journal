package base;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewingWindow extends JFrame implements ListSelectionListener, ActionListener {

    private static final String NEW_BUTTON = "New";
    private static final String SEARCH_BUTTON = "Search";
    private static final String RELOAD_BUTTON = "Reload";
    private static final String SEARCH_TEXTFIELD = "Search text field";

    private Journal m_journal;
    private JTextPane textPane;
    private JList<Entry> list;
	private JTextField searchTF;
	private JButton newBtn, searchBtn, reloadBtn;
	
	public ViewingWindow(Journal j) {
		super("My First Application");
		m_journal = j;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(700, 500));
        setMinimumSize(new Dimension(700, 500));

		add(createGUI());

        pack();
        setVisible(true);

        updateUI();
	}

	public void updateUI() {
        System.out.println("Updating UI");
        Entry[] entries = new Entry[m_journal.getEntries().size()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = m_journal.getEntries().get(i);
        }
        list.setListData(entries);
        list.setSelectedIndex(0);
    }
	
	public JPanel createGUI() {
		// Panels
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        // New button
        newBtn = new JButton(NEW_BUTTON);
        newBtn.addActionListener(this);
        newBtn.setActionCommand(NEW_BUTTON);
        topPanel.add(newBtn);

        // Search text field
        searchTF = new JTextField();
        searchTF.addActionListener(this);
        searchTF.setActionCommand(SEARCH_TEXTFIELD);
        searchTF.setToolTipText("Date format: dd.mm");
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
        list = new JList<>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(2);
		list.addListSelectionListener(this);
		
		JScrollPane listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
	    int selection = list.getSelectedIndex();
	    if (selection == -1) {
	        selection = 0;
        }
		textPane.setText(m_journal.getEntries().get(selection).getText());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    switch (e.getActionCommand()) {
            case NEW_BUTTON:
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        NewEntryWindow window = new NewEntryWindow(m_journal);
                        window.addWindowListener(m_journal);
                    }
                });
                break;
            case RELOAD_BUTTON:
                updateUI();
                break;
            case SEARCH_BUTTON:

                break;
            case SEARCH_TEXTFIELD:

                break;
        }
/*
if (checkDateFormat(searchTF.getText())) {
int index = getIndex(searchTF.getText());

textPane.setText(entriesToSearch.get(index));
for (int i = 0; i < entries.size(); i++) {
if (entries.get(i).equalsIgnoreCase(entriesToSearch.get(index))) {
list.setSelectedIndex(i);
}
}
searchTF.setText("");
}
dates = null;
entries = new ArrayList<String>();
entriesToSearch = new HashMap<Integer, String>();
readComments();
*/
    }

	private int getIndex(String date) {
		String index = "";

		String[] parts = date.split("\\.");

		switch (parts[1]) {
			case "01":
				index = 11 + parts[0] + parts[2];
				break;
			case "02":
				index = 12 + parts[0] + parts[2];
				break;
			case "03":
				index = 13 + parts[0] + parts[2];
				break;
			case "04":
				index = 14 + parts[0] + parts[2];
				break;
			case "05":
				index = 15 + parts[0] + parts[2];
				break;
			case "06":
				index = 16 + parts[0] + parts[2];
				break;
			case "07":
				index = 17 + parts[0] + parts[2];
				break;
			case "08":
				index = 18 + parts[0] + parts[2];
				break;
			case "09":
				index = 19 + parts[0] + parts[2];
				break;
			case "10":
				index = 20 + parts[0] + parts[2];
				break;
			case "11":
				index = 21 + parts[0] + parts[2];
				break;
			case "12":
				index = 22 + parts[0] + parts[2];
				break;
		}

		return new Integer(index);
	}

	private boolean checkDateFormat(String text) {
		String[] parts = text.split("\\.");

		if (parts.length < 3 || parts.length > 3) {
			return false;
		}

		if (parts[0].equalsIgnoreCase("")) {
			return false;
		} else if (parts[1].equalsIgnoreCase("")){
			return false;
		} else if (parts[2].equalsIgnoreCase("")) {
			return false;
		}

		if (parts[0].length() != 2) {
			return false;
		} else if (parts[1].length() != 2) {
			return false;
		} else if (parts[2].length() != 4) {
			return false;
		}

		return true;
	}
}
