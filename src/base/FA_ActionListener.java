package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FA_ActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == FA_Window.redButton) {
			FA_Window.addRedScore();
		} else if (e.getSource() == FA_Window.blueButton) {
			FA_Window.addBlueScore();
		} else if (e.getSource() == FA_Window.resetButton) {
			FA_Window.resetScore();
		}
	}

}
