package base;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class Journal implements WindowListener {

    public static final String COMMENTS_FILE_NAME = "Comments.txt";
    public static final String STATS_FILE_NAME = "Stats.txt";

    private ArrayList<Entry> entries = new ArrayList<Entry>();
    private ViewingWindow window;

    public Journal() {
        entries = EntryReader.read();

        Journal tmp = this;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                window = new ViewingWindow(tmp);
            }
        });
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public Entry[] searchForDate(int day, int month, int year) {
        ArrayList<Entry> list = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            if (day == -1 || entries.get(i).getDate().getDayOfMonth() == day) {
                if (month == -1 || entries.get(i).getDate().getMonthValue() == month) {
                    if (year == -1 || entries.get(i).getDate().getYear() == year) {
                        list.add(entries.get(i));
                    }
                }
            }
        }

        return getArrayFromList(list);
    }

    public Entry[] searchForString(String text) {
        ArrayList<Entry> list = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getText().toLowerCase().contains(text.toLowerCase())) {
                list.add(entries.get(i));
            }
        }
        return getArrayFromList(list);
    }

    public Entry[] getArrayFromList(ArrayList<Entry> list) {
        Entry[] result = new Entry[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

	public static void main(String[] args) {
		new Journal();
	}

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        window.updateUI();
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
