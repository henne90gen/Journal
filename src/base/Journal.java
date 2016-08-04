package base;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class Journal implements WindowListener {

    public static final String COMMENTS_FILE_NAME = "comments";

    public ArrayList<Entry> entries = new ArrayList<Entry>();
    private ViewingWindow window;
    private int lastEntryID = 0;

    public Journal() {
        entries = EntryReader.read();
        lastEntryID = entries.get(entries.size()-1).getID();

        Journal tmp = this;
        SwingUtilities.invokeLater(() -> window = new ViewingWindow(tmp));
    }

    public Entry[] searchForDate(int day, int month, int year) {
        ArrayList<Entry> list = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            if (day == -1 || entries.get(i).date.getDayOfMonth() == day) {
                if (month == -1 || entries.get(i).date.getMonthValue() == month) {
                    if (year == -1 || entries.get(i).date.getYear() == year) {
                        list.add(entries.get(i));
                    }
                }
            }
        }

        return getArrayFromList(list);
    }

    public Entry[] searchForString(String text) {
        ArrayList<Entry> list = new ArrayList<>();
        if (EntryReader.getMood(text) != null) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).mood == EntryReader.getMood(text)) {
                    list.add(entries.get(i));
                }
            }
        } else {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).comment.toLowerCase().contains(text.toLowerCase())) {
                    list.add(entries.get(i));
                }
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

    public int getNextID() {
        return ++lastEntryID;
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
