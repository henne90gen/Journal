package base;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class J_ListSelectionListener implements ListSelectionListener{
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		J_Window.textPane.setText(Journal.entries.get(J_Window.list.getSelectedIndex()));
	}
}
