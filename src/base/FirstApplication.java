package base;

import javax.swing.SwingUtilities;

public class FirstApplication {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FA_Window();
			}
		});
	}
}
