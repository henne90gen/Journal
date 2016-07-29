package base;

import javax.swing.*;
import java.util.ArrayList;

public class Journal {

    private ArrayList<Entry> m_entries = new ArrayList<Entry>();
    private NewEntryWindow m_window;

    public Journal() {

        m_entries = EntryReader.read();

//        if (m_entries.size() != 0) {
//            list.setListData(dates);
//            textPane.setText(entries.get(0));
//        } else {
//            textPane.setText("There was an error while reading the 'Comments.txt' file.");
//        }

        Journal tmp = this;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ViewingWindow(tmp);
            }
        });
    }

    public ArrayList<Entry> getEntries() {
        return m_entries;
    }

	public static void main(String[] args) {
		new Journal();
	}
}
