package journal.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class JournalEntry {

	public enum Mood {
		Awesome, Great, Good, Undecided, Bad, Poor, Waste
	}

	public UUID uuid;
	public LocalDate date;
	public String comment;
	public Mood mood;
	public boolean deleted;

	public JournalEntry() {
		this(LocalDate.now(), Mood.Undecided, "");
	}

	public JournalEntry(LocalDate date, Mood mood, String comment) {
		this.uuid = UUID.randomUUID();
		this.date = date;
		this.comment = comment;
		this.mood = mood;
		this.deleted = false;
	}

	@Override
	public String toString() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return date.format(f);
	}
}
