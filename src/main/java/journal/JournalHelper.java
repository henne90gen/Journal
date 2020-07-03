package journal;

import journal.data.JournalEntry;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JournalHelper {

	public static final JournalHelper INSTANCE = new JournalHelper();

	public static final String DEFAULT_FILE_NAME = "journal.json";
	public static final String DEFAULT_DEV_FILE_NAME = "journal_dev.json";

	private JournalHelper() {
	}

	public LocalDate getDate(String line) {
		return LocalDate.parse(line, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public JournalEntry.Mood getMood(String line) {
		switch (line) {
			case "Awesome":
				return JournalEntry.Mood.Awesome;
			case "Great":
				return JournalEntry.Mood.Great;
			case "Good":
				return JournalEntry.Mood.Good;
			case "Undecided":
				return JournalEntry.Mood.Undecided;
			case "Bad":
				return JournalEntry.Mood.Bad;
			case "Poor":
				return JournalEntry.Mood.Poor;
			case "Waste":
				return JournalEntry.Mood.Waste;
			default:
				return null;
		}
	}
}
