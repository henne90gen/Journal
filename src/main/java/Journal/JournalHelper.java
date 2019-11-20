package Journal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JournalHelper {

	public static final JournalHelper INSTANCE = new JournalHelper();

	private JournalHelper() {}

	public LocalDate getDate(String line) {
		return LocalDate.parse(line, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	public Entry.Mood getMood(String line) {
		switch (line) {
			case "Awesome":
				return Entry.Mood.Awesome;
			case "Great":
				return Entry.Mood.Great;
			case "Good":
				return Entry.Mood.Good;
			case "Undecided":
				return Entry.Mood.Undecided;
			case "Bad":
				return Entry.Mood.Bad;
			case "Poor":
				return Entry.Mood.Poor;
			case "Waste":
				return Entry.Mood.Waste;
			default:
				return null;
		}
	}
}
