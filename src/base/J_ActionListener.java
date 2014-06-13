package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class J_ActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(J_Window.searchButton)) {
			if (checkDateFormat(J_Window.searchBar.getText())) {
				int index = getIndex(J_Window.searchBar.getText());
			
				J_Window.textPane.setText(Journal.entriesToSearch.get(index));
				for (int i = 0; i < Journal.entries.size(); i++) {
					if (Journal.entries.get(i).equalsIgnoreCase(Journal.entriesToSearch.get(index))) {
						J_Window.list.setSelectedIndex(i);
					}
				}
				J_Window.searchBar.setText("");
			}
		} else if (e.getSource().equals(J_Window.reload)) {
			Journal.dates = null;
			Journal.entries = new ArrayList<String>();
			Journal.entriesToSearch = new HashMap<Integer, String>();
			Journal.readComments();
		}
	}

	private int getIndex(String date) {
		String index = "";
		
		String[] parts = date.split("\\.");
		
		switch (parts[1]) {
		case "01":
			index = 11 + parts[0] + parts[2];
			break;
		case "02":
			index = 12 + parts[0] + parts[2];
			break;
		case "03":
			index = 13 + parts[0] + parts[2];
			break;
		case "04":
			index = 14 + parts[0] + parts[2];
			break;
		case "05":
			index = 15 + parts[0] + parts[2];
			break;
		case "06":
			index = 16 + parts[0] + parts[2];
			break;
		case "07":
			index = 17 + parts[0] + parts[2];
			break;
		case "08":
			index = 18 + parts[0] + parts[2];
			break;
		case "09":
			index = 19 + parts[0] + parts[2];
			break;
		case "10":
			index = 20 + parts[0] + parts[2];
			break;
		case "11":
			index = 21 + parts[0] + parts[2];
			break;
		case "12":
			index = 22 + parts[0] + parts[2];
			break;
		}
		
		return new Integer(index);
	}
		
	private boolean checkDateFormat(String text) {
		String[] parts = text.split("\\.");
		
		if (parts.length == 0 || parts.length == 1 || parts.length == 2 || parts.length >= 4) {
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
