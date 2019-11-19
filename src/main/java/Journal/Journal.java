package Journal;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;

public class Journal implements WindowListener {

	JournalView view;
	JournalData data;
	private Thread initThread;

	public void run() {
		data = new JournalData();
		SwingUtilities.invokeLater(() -> view = new JournalView(this));
	}

	private void init() {
	    data.load();
		view.update();
	}

	public static void main(String[] args) throws SQLException {
		Journal journal = new Journal();
		journal.run();
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (e.getSource() == view) {
			try {
				initThread.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
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
			initThread = new Thread(this::init);
			initThread.start();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}
}
