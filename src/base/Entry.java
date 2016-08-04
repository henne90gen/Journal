package base;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by henne on 29.07.16.
 */
public class Entry {

    public enum Mood {
        Awesome, Great, Good, Undecided, Bad, Poor, Waste
    }

    private int id;
    public LocalDate date;
    public String comment;
    public Mood mood;

    public Entry() {
        date = LocalDate.now();
        comment = "";
        mood = Mood.Undecided;
    }

    public Entry(int id, LocalDate date, Mood mood, String comment) {
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

    public int getID() {
        return id;
    }
}
