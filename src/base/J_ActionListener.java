package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class J_ActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == J_Window.redButton) {
			J_Window.addRedScore();
		} else if (e.getSource() == J_Window.blueButton) {
			J_Window.addBlueScore();
		} else if (e.getSource() == J_Window.resetButton) {
			J_Window.resetScore();
		}
	}

}
