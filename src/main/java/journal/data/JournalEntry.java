package journal.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class JournalEntry {

	public enum Mood {
		Awesome, Great, Good, Undecided, Bad, Poor, Waste
	}

	public UUID uuid;
	public LocalDateTime date;
	public String comment;
	public Mood mood;

	public JournalEntry() {
		this.uuid = UUID.randomUUID();
		this.date = LocalDateTime.now();
		this.comment = "";
		this.mood = Mood.Undecided;
	}

	public JournalEntry(LocalDateTime date, Mood mood, String comment) {
		this.uuid = UUID.randomUUID();
		this.date = date;
		this.comment = comment;
		this.mood = mood;
	}

	@Override
	public String toString() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return date.format(f);
	}
}