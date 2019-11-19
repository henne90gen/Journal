package Journal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Entry {

    enum Mood {
        Awesome, Great, Good, Undecided, Bad, Poor, Waste
    }

    private int id;
    LocalDate date;
    String comment;
    Mood mood;

    public Entry() {
        date = LocalDate.now();
        comment = "";
        mood = Mood.Undecided;
    }

    Entry(int id, LocalDate date, Mood mood, String comment) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.mood = mood;
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        return id + " - " + date.format(f);
    }

    int getID() {
        return id;
    }
}
