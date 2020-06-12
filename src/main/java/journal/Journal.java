package journal;

import journal.data.EntryStorage;
import journal.data.FileDataSource;
import journal.data.IJournalData;
import journal.data.JournalData;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Journal implements WindowListener {

	JournalView view;
	IJournalData data;

	public boolean isInitialized = false;

	public void run() {
		data = new JournalData();
		data.addUpdateCallback(() -> {
			EntryStorage storage = new EntryStorage();
			storage.entries = data.getAllEntries();
			FileDataSource.INSTANCE.writeToFile(storage);
		});
		SwingUtilities.invokeLater(() -> view = new JournalView(this));
	}

	private void init() {
		synchronized (this) {
			if (isInitialized) {
				return;
			}
			data.init();
			isInitialized = true;
		}
	}

	public static void main(String[] args) {
		// setup logging format
		String format = "%1$tY-%1$tm-%1$td - %1$tH:%1$tM:%1$tS | %4$s: %5$s%6$s%n";
		System.setProperty("java.util.logging.SimpleFormatter.format", format);

		Journal journal = new Journal();
		journal.run();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
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
		if (e.getSource() == view) {
			init();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
