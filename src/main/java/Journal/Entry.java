package Journal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Entry {

	enum Mood {
		Awesome, Great, Good, Undecided, Bad, Poor, Waste
	}

	public int id;
	public LocalDate date;
	public String comment;
	public Mood mood;

	public Entry() {
		date = LocalDate.now();
		comment = "";
		mood = Mood.Undecided;
	}

	public Entry(LocalDate date, Mood mood, String comment) {
		this.date = date;
		this.comment = comment;
		this.mood = mood;
	}

	@Override
	public String toString() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.YYYY");
		return id + " - " + date.format(f);
	}
}