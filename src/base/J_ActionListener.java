package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class J_ActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (checkDateFormat(J_Window.searchBar.getText())) {
			int index = getIndex(J_Window.searchBar.getText());
			
		}
	}

	private int getIndex(String date) {
		String index = "";
		
		String[] parts = date.split("\\.");
		
		switch (parts[1]) {
		case "Jan":
			index = 11 + parts[0] + parts[2];
			break;
		case "Feb":
			index = 12 + parts[0] + parts[2];
			break;
		case "Mar":
			index = 13 + parts[0] + parts[2];
			break;
		case "Apr":
			index = 14 + parts[0] + parts[2];
			break;
		case "May":
			index = 15 + parts[0] + parts[2];
			break;
		case "Jun":
			index = 16 + parts[0] + parts[2];
			break;
		case "Jul":
			index = 17 + parts[0] + parts[2];
			break;
		case "Aug":
			index = 18 + parts[0] + parts[2];
			break;
		case "Sep":
			index = 19 + parts[0] + parts[2];
			break;
		case "Oct":
			index = 20 + parts[0] + parts[2];
			break;
		case "Nov":
			index = 21 + parts[0] + parts[2];
			break;
		case "Dec":
			index = 22 + parts[0] + parts[2];
			break;
		}
		
		return new Integer(index);
	}
		
	private boolean checkDateFormat(String text) {
		String[] parts = text.split("\\.");
		
		if (parts.length == 0) {
			return false;
		}
		
		if (parts[0].equalsIgnoreCase("")) {
			return false;
		} else if (parts[1].equalsIgnoreCase("")){
			return false;
		} else if (parts[2].equalsIgnoreCase("")) {
			return false;
		}
		
		if (parts[0].length() != 2) {
			return false;
		} else if (parts[1].length() != 2) {
			return false;
		} else if (parts[2].length() != 4) {
			return false;
		}
		
		return true;
	}
}
