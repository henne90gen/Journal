package journal;

import journal.data.FileHandler;
import journal.data.IJournalData;
import journal.data.JournalData;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Journal implements WindowListener {

	JournalView view;
	IJournalData data;

	public void run() {
		data = new JournalData();
		SwingUtilities.invokeLater(() -> view = new JournalView(this));
	}

	private void init() {
		data.saveAll(FileHandler.INSTANCE.readFromFile());
	}

	public static void main(String[] args) {
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
