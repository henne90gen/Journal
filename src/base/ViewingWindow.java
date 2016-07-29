package base;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewingWindow extends JFrame implements ListSelectionListener, ActionListener {

    private Journal m_journal;
//    public ArrayList<String> entries = new ArrayList<String>();
//    public HashMap<Integer, String> entriesToSearch = new HashMap<Integer, String>();
//    public String[] dates;
	public JTextPane textPane = new JTextPane();
	public JList<String> list = new JList<String>();
	public JTextField searchBar = new JTextField();
	public JButton searchButton = new JButton();
	public JButton reload = new JButton();
	
	public ViewingWindow(Journal j) {
		super("My First Application");
		m_journal = j;
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		setContentPane(createContentPane());
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		setVisible(true);

//        if (readComments()) {
//            list.setListData();
//            textPane.setText(m_journal.getEntries().get(0).getText());
//        } else {
//            textPane.setText("There was an error while reading the 'Comments.txt' file.");
//        }
	}
	
	public JPanel createContentPane() {
		// Main Panel:
		JPanel mainPanel = new JPanel();
		mainPanel.setOpaque(true);
		mainPanel.setSize(this.getWidth(), this.getHeight());
		
		// Layout:
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbcList = new GridBagConstraints();
		GridBagConstraints gbcReload = new GridBagConstraints();
		GridBagConstraints gbcSearch = new GridBagConstraints();
		GridBagConstraints gbcText = new GridBagConstraints();
		GridBagConstraints gbcButton = new GridBagConstraints();
		mainPanel.setLayout(layout);
		mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		
		// Reload button:
		reload.addActionListener(this);
		reload.setText("Reload");
		reload.setPreferredSize(new Dimension(20, 20));
		gbcReload.fill = GridBagConstraints.BOTH;
		gbcReload.gridx = 0;
		gbcReload.gridy = 0;
		mainPanel.add(reload, gbcReload);
		
		// List:
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(2);
		list.addListSelectionListener(this);
		
		JScrollPane listScrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScrollPane.setPreferredSize(new Dimension(130, 430));
		gbcList.fill = GridBagConstraints.BOTH;
		gbcList.gridx = 0;
		gbcList.gridy = 1;
		mainPanel.add(listScrollPane, gbcList);
		
		// Searchbar:
		searchBar.setPreferredSize(new Dimension(400, 20));
		searchBar.setToolTipText("Datumsformat: DD.MM.JJJJ");
		gbcSearch.fill = GridBagConstraints.BOTH;
		gbcSearch.gridx = 1;
		gbcSearch.gridy = 0;
		mainPanel.add(searchBar, gbcSearch);
		
		// Searchbutton:
		searchButton.setPreferredSize(new Dimension(20, 20));
		searchButton.setText("Search");
		searchButton.addActionListener(this);
		gbcButton.fill = GridBagConstraints.BOTH;
		gbcButton.gridx = 2;
		gbcButton.gridy = 0;
		mainPanel.add(searchButton, gbcButton);
		
		// Textfield:
		gbcText.fill = GridBagConstraints.BOTH;
		gbcText.gridwidth = 2;
		gbcText.gridx = 1;
		gbcText.gridy = 1;
		textPane.setEditable(false);
		JScrollPane textScrollPane = new JScrollPane(textPane);
		textScrollPane.setPreferredSize(new Dimension(500, 430));
		mainPanel.add(textScrollPane, gbcText);
		
		return mainPanel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		textPane.setText(m_journal.getEntries().get(list.getSelectedIndex()).getText());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(searchButton)) {
			if (checkDateFormat(searchBar.getText())) {
				int index = getIndex(searchBar.getText());

//				textPane.setText(entriesToSearch.get(index));
//				for (int i = 0; i < entries.size(); i++) {
//					if (entries.get(i).equalsIgnoreCase(entriesToSearch.get(index))) {
//						list.setSelectedIndex(i);
//					}
//				}
				searchBar.setText("");
			}
		} else if (e.getSource().equals(reload)) {
//			dates = null;
//			entries = new ArrayList<String>();
//			entriesToSearch = new HashMap<Integer, String>();
//			readComments();
		}
	}

    /*public boolean readComments() {
        try {
            int numDays = (countLines("Comments.txt") + 1) / 8;
            //dates = new String[numDays];

            BufferedReader br = new BufferedReader(new FileReader("Comments.txt"));
            String line = br.readLine();
            int lineCounter = 10;
            int key = 0;
            String date = "";

            while (line != null) {
                if (line.equalsIgnoreCase("--------------------------------------------------")) { lineCounter = 0; } else { lineCounter++; }
                if (lineCounter == 1) {
                    key = getKey(line);
                    date = getDate(line);
                } else if (lineCounter == 5) {
                    entriesToSearch.put(key, line);
                    entries.add(line);
                    dates[entries.size() - 1] = date;
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }*/

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
