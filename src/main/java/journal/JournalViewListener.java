package journal;

import com.google.common.flogger.FluentLogger;
import journal.data.EntryStorage;
import journal.data.FileDataSource;
import journal.data.ImportResult;
import journal.data.JournalEntry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static journal.JournalView.*;

class JournalViewListener implements ListSelectionListener, ActionListener, DocumentListener {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private Journal journal;

	JournalViewListener(Journal journal) {
		this.journal = journal;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case NEW_BUTTON:
				newButtonPressed();
				break;
			case EDIT_BUTTON:
				editButtonPressed();
				break;
			case SAVE_BUTTON:
				saveButtonPressed();
				break;
			case CANCEL_BUTTON:
				cancelButtonPressed();
				break;
			case DELETE_BUTTON:
				deleteButtonPressed();
				break;
			case EXPORT_BUTTON:
				exportButtonPressed();
				break;
			case IMPORT_BUTTON:
				importButtonPressed();
				break;
			case SEARCH_BUTTON:
				searchButtonPressed();
				break;
			case SEARCH_RADIO_BUTTON:
				SwingUtilities.invokeLater(() -> {
					journal.view.searchTF.setText("");
					journal.view.update();
				});
				break;
		}
	}

	private void searchButtonPressed() {
		if (journal.view.dateSearchRB.isSelected()) {
			searchDate();
		} else if (journal.view.stringSearchRB.isSelected()) {
			searchString();
		}
	}

	private void importButtonPressed() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./"));
		int returnVal = fc.showOpenDialog(journal.view);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}

		String path = fc.getSelectedFile().getPath();
		File file = new File(path);
		List<JournalEntry> entries = FileDataSource.INSTANCE.readFromFile(file).entries;
		ImportResult importResult = journal.data.importEntries(entries);
		if (!importResult.hasProblems()) {
			LOGGER.atInfo().log("Imported %s successfully.", file);
		} else {
			LOGGER.atWarning().log("Found problems %d while importing from %s.", importResult.problems.size(), file);
			showImportFailedDialog(importResult);
		}
		journal.view.update();
	}

	private void showImportFailedDialog(ImportResult importResult) {
		JDialog dialog = new JDialog(journal.view, "Import Failed", Dialog.ModalityType.APPLICATION_MODAL);
		GridBagConstraints constraints = new GridBagConstraints();
		GridBagLayout layout = new GridBagLayout();
		dialog.setLayout(layout);

		int currentY = 0;
		for (int i = 0; i < importResult.problems.size(); i++) {
			constraints.gridx = 0;
			constraints.gridy = currentY;
			JLabel label = new JLabel("Problem " + (i + 1));
			dialog.getContentPane().add(label, constraints);

			constraints.ipadx = 10;
			constraints.ipady = 10;
			for (ImportResult.Diff diff : importResult.problems.get(i).diffs) {
				constraints.gridy = currentY;

				constraints.gridx = 1;
				JLabel diffOriginal = new JLabel(diff.getOriginal());
				dialog.getContentPane().add(diffOriginal, constraints);

				constraints.gridx = 2;
				JLabel diffNew = new JLabel(diff.getNew());
				dialog.getContentPane().add(diffNew, constraints);

				currentY++;
			}
		}

		JButton okButton = new JButton("OK");
		constraints.gridwidth = 3;
		constraints.gridx = 0;
		constraints.gridy = currentY;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		dialog.getContentPane().add(okButton, constraints);
		okButton.addActionListener((event) -> {
			dialog.dispose();
		});

		dialog.pack();
		dialog.setVisible(true);
	}

	private void exportButtonPressed() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("./"));
		int returnVal = fc.showSaveDialog(journal.view);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return;
		}

		String path = fc.getSelectedFile().getPath();
		File file = new File(path);
		EntryStorage storage = new EntryStorage();
		storage.entries = journal.data.getAllEntries();
		FileDataSource.INSTANCE.writeToFile(storage, file);
	}

	private void deleteButtonPressed() {
		journal.data.delete(journal.view.entryList.getSelectedValue());
		journal.view.update();
	}

	private void saveButtonPressed() {
		finishEditing();

		int index = journal.view.entryList.getSelectedIndex();
		JournalEntry selectedEntry = journal.view.entryList.getSelectedValue();
		for (int i = 0; i < journal.view.feelings.length; i++) {
			if (journal.view.feelings[i].isSelected()) {
				selectedEntry.mood = JournalEntry.Mood.values()[i];
				break;
			}
		}
		selectedEntry.comment = journal.view.commentTP.getText();
		parseDate(selectedEntry);
		journal.view.entryList.setSelectedIndex(index);
		journal.data.save(selectedEntry);

		EntryStorage storage = new EntryStorage();
		storage.entries = journal.data.getAllEntries();
		FileDataSource.INSTANCE.writeToFile(storage);
		journal.view.update();
	}

	private void parseDate(JournalEntry selectedEntry) {
		String monthText = journal.view.monthTF.getText();
		String dayText = journal.view.dayTF.getText();
		int month = selectedEntry.date.getDayOfMonth();
		int day = selectedEntry.date.getMonthValue();
		String yearText = journal.view.yearTF.getText();
		int year = selectedEntry.date.getYear();
		try {
			year = Integer.parseInt(yearText);
		} catch (NumberFormatException e) {
			LOGGER.atWarning().withCause(e).log("Could not parse year");
		}
		try {
			month = Integer.parseInt(monthText);
		} catch (NumberFormatException e) {
			LOGGER.atWarning().withCause(e).log("Could not parse month");
		}
		try {
			day = Integer.parseInt(dayText);
		} catch (NumberFormatException e) {
			LOGGER.atWarning().withCause(e).log("Could not parse day");
		}
		selectedEntry.date = LocalDate.of(year, month, day);
	}

	private void cancelButtonPressed() {
		finishEditing();
		journal.view.update();
	}

	private void finishEditing() {
		journal.view.setUIEnabled(true);
		for (JRadioButton f : journal.view.feelings) {
			f.setEnabled(false);
		}
		journal.view.commentTP.setEditable(false);
		journal.view.editBtn.setText(EDIT_BUTTON);
		journal.view.editBtn.setActionCommand(EDIT_BUTTON);
		journal.view.deleteBtn.setText(DELETE_BUTTON);
		journal.view.deleteBtn.setActionCommand(DELETE_BUTTON);
	}

	private void editButtonPressed() {
		JournalEntry selectedValue = journal.view.entryList.getSelectedValue();
		if (selectedValue == null) {
			LOGGER.atWarning().log("No entry selected.");
			return;
		}
		LOGGER.atInfo().log("Editing entry " + selectedValue.uuid + ".");

		journal.view.setUIEnabled(false);

		journal.view.editBtn.setEnabled(true);
		journal.view.editBtn.setText(SAVE_BUTTON);
		journal.view.editBtn.setActionCommand(SAVE_BUTTON);

		journal.view.deleteBtn.setEnabled(true);
		journal.view.deleteBtn.setText(CANCEL_BUTTON);
		journal.view.deleteBtn.setActionCommand(CANCEL_BUTTON);

		journal.view.commentTP.setEditable(true);
		journal.view.commentTP.grabFocus();

		for (JRadioButton f : journal.view.feelings) {
			f.setEnabled(true);
		}
	}

	private void newButtonPressed() {
		JournalEntry entry = new JournalEntry(LocalDate.now(), JournalEntry.Mood.Undecided, "");
		journal.data.save(entry);

		journal.view.entryList.setSelectedIndex(journal.data.getAllEntries().size() - 1);
		actionPerformed(new ActionEvent(this, 0, EDIT_BUTTON));
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
		List<JournalEntry> es = journal.data.findByDate(date[1], date[0], date[2]);
		journal.view.entryList.setListData(getArrayFromList(es));
		if (es.size() == 0) {
			journal.view.commentTP.setText(NOTHING_TO_DISPLAY);
		} else {
			journal.view.entryList.setSelectedIndex(0);
		}
	}

	private JournalEntry[] getArrayFromList(List<JournalEntry> list) {
		JournalEntry[] result = new JournalEntry[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	private void searchString() {
		List<JournalEntry> entries = journal.data.findByString(journal.view.searchTF.getText());
		journal.view.entryList.setListData(getArrayFromList(entries));
		journal.view.entryList.setSelectedIndex(0);
		if (entries.isEmpty()) {
			journal.view.dayTF.setText("");
			journal.view.monthTF.setText("");
			journal.view.yearTF.setText("");
			journal.view.commentTP.setText("");
			for (int i = 0; i < journal.view.feelings.length;i++) {
				journal.view.feelings[i].setSelected(false);
			}
			journal.view.feelings[3].setSelected(true);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (journal.view.entryList.getSelectedIndex() != -1) {
			JournalEntry selectedEntry = journal.view.entryList.getSelectedValue();
			journal.view.commentTP.setText(selectedEntry.comment);
			LocalDate date = selectedEntry.date;
			journal.view.dayTF.setText("" + date.getDayOfMonth());
			journal.view.monthTF.setText("" + date.getMonthValue());
			journal.view.yearTF.setText("" + date.getYear());
			journal.view.feelings[selectedEntry.mood.ordinal()].setSelected(true);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		actionPerformed(new ActionEvent(journal.view.searchTF, 0, SEARCH_BUTTON));
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		actionPerformed(new ActionEvent(journal.view.searchTF, 0, SEARCH_BUTTON));
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		actionPerformed(new ActionEvent(journal.view.searchTF, 0, SEARCH_BUTTON));
	}
}
