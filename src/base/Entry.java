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

    private LocalDate date;
    private String entry;
    private Mood mood;

    public Entry() {
        date = LocalDate.now();
        entry = "";
        mood = Mood.Undecided;
    }

    public Entry(LocalDate date, String entry, Mood mood) {
        this.date = date;
        this.entry = entry;
        this.mood = mood;
    }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        return date.format(f);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate d) {
        date = d;
    }

    public boolean setDate(String d) {

        return false;
    }

    public String getText() {
        return entry;
    }

    public void setText(String t) {
        entry = t;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood m) {
        mood = m;
    }
}
