package base;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class Journal implements WindowListener {

    private ArrayList<Entry> m_entries = new ArrayList<Entry>();
    private ViewingWindow m_window;

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
                m_window = new ViewingWindow(tmp);
            }
        });
    }

    ArrayList<Entry> getEntries() {
        return m_entries;
    }

	public static void main(String[] args) {
		new Journal();
	}

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        m_window.updateUI();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
