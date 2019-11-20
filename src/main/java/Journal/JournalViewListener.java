package Journal;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static Journal.JournalView.*;

class JournalViewListener implements ListSelectionListener, ActionListener, DocumentListener {

	private Journal journal;

	JournalViewListener(Journal journal) {
		this.journal = journal;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case NEW_BUTTON:
				Entry entry = new Entry(journal.data.getNextID(), LocalDate.now(), Entry.Mood.Undecided, "");

				journal.data.save(entry);

				journal.view.update();
				journal.view.dateList.setSelectedIndex(journal.data.getAllEntries().size() - 1);
				actionPerformed(new ActionEvent(this, 0, EDIT_BUTTON));
				break;
			case EDIT_BUTTON:
				System.out.println("Editing entry " + journal.view.dateList.getSelectedValue().id + ".");
				journal.view.setUIEnabled(false);
				journal.view.editBtn.setEnabled(true);
				journal.view.commentTP.setEditable(true);
				for (JRadioButton f : journal.view.feelings) {
					f.setEnabled(true);
				}
				journal.view.editBtn.setText(SAVE_BUTTON);
				journal.view.editBtn.setActionCommand(SAVE_BUTTON);
				break;
			case SAVE_BUTTON:
				journal.view.setUIEnabled(true);
				for (JRadioButton f : journal.view.feelings) {
					f.setEnabled(false);
				}
				journal.view.commentTP.setEditable(false);
				journal.view.editBtn.setText(EDIT_BUTTON);
				journal.view.editBtn.setActionCommand(EDIT_BUTTON);
				int index = journal.view.dateList.getSelectedIndex();
				for (int i = 0; i < journal.view.feelings.length; i++) {
					if (journal.view.feelings[i].isSelected()) {
						journal.view.dateList.getSelectedValue().mood = Entry.Mood.values()[i];
						break;
					}
				}
				journal.view.dateList.getSelectedValue().comment = journal.view.commentTP.getText();
				journal.view.dateList.setSelectedIndex(index);
				journal.data.save();
				journal.view.update();
				break;
			case DELETE_BUTTON:
				journal.data.delete(journal.view.dateList.getSelectedValue());
				journal.view.update();
				break;
			case EXPORT_BUTTON: {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./"));
				int returnVal = fc.showSaveDialog(journal.view);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					journal.data.writeToFile(fc.getSelectedFile().getPath());
				}
			}
			break;
			case IMPORT_BUTTON: {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./"));
				int returnVal = fc.showOpenDialog(journal.view);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					journal.data.readFromFile(fc.getSelectedFile().getPath());
				}
				journal.view.update();
			}
			break;
			case SEARCH_BUTTON:
				if (journal.view.dateSearchRB.isSelected())
					searchDate();
				else if (journal.view.stringSearchRB.isSelected())
					searchString();
				break;
			case SEARCH_RADIO_BUTTON:
				SwingUtilities.invokeLater(() -> {
					journal.view.searchTF.setText("");
					journal.view.update();
				});
				break;
		}
	}

	private void searchDate() {
		// Adding dot after day and month automatically
		if (journal.view.searchTF.getText().length() == 2 || journal.view.searchTF.getText().length() == 5) {
			SwingUtilities.invokeLater(() -> journal.view.searchTF.setText(journal.view.searchTF.getText() + "."));
			return;
		}

		// Getting date from searchTF
		String[] tmp = journal.view.searchTF.getText().split("\\.");
		int[] date = {-1, -1, -1};
		if (tmp.length == 1 && !tmp[0].equalsIgnoreCase("")) {
			date[0] = Integer.parseInt(tmp[0]);
		} else if (tmp.length == 2) {
			date[0] = Integer.parseInt(tmp[0]);
			date[1] = Integer.parseInt(tmp[1]);
		} else if (tmp.length == 3) {
			date[0] = Integer.parseInt(tmp[0]);
			date[1] = Integer.parseInt(tmp[1]);
			date[2] = Integer.parseInt(tmp[2]);
		}
		List<Entry> es = journal.data.findByDate(date[1], date[0], date[2]);
		journal.view.dateList.setListData(getArrayFromList(es));
		if (es.size() == 0) {
			journal.view.commentTP.setText(NOTHING_TO_DISPLAY);
		} else {
			journal.view.dateList.setSelectedIndex(0);
		}
	}

	private Entry[] getArrayFromList(List<Entry> list) {
		Entry[] result = new Entry[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	private void searchString() {
		List<Entry> entries = journal.data.findByString(journal.view.searchTF.getText());
		journal.view.dateList.setListData(getArrayFromList(entries));
		journal.view.dateList.setSelectedIndex(0);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (journal.view.dateList.getSelectedIndex() != -1) {
			journal.view.commentTP.setText(journal.view.dateList.getSelectedValue().comment);
			journal.view.feelings[journal.view.dateList.getSelectedValue().mood.ordinal()].setSelected(true);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		actionPerformed(new ActionEvent(journal.view.searchTF, 0, SEARCH_BUTTON));
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if (journal.view.searchTF.getText().length() != 2 && journal.view.searchTF.getText().length() != 5) {
			actionPerformed(new ActionEvent(journal.view.searchTF, 0, SEARCH_BUTTON));
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		actionPerformed(new ActionEvent(journal.view.searchTF, 0, SEARCH_BUTTON));
	}
}
