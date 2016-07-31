package base;

import java.time.LocalDate;

/**
 * Created by henne on 29.07.16.
 */
public class Entry {

    public enum Mood {
        Awesome, Great, Good, Undecided, Bad, Poor, Waste
    }

    private LocalDate m_date;
    private String m_entry;
    private Mood m_mood;

    public Entry() {
        m_date = LocalDate.now();
        m_entry = "";
        m_mood = Mood.Undecided;
    }

    public Entry(LocalDate d, String e, Mood m) {
        m_date = d;
        m_entry = e;
        m_mood = m;
    }

    @Override
    public String toString() {
        return m_date.toString();
    }

    public LocalDate getDate() {
        return m_date;
    }

    public void setDate(LocalDate d) {
        m_date = d;
    }

    public boolean setDate(String d) {

        return false;
    }

    public String getText() {
        return m_entry;
    }

    public void setText(String t) {
        m_entry = t;
    }

    public Mood getMood() {
        return m_mood;
    }

    public void setMood(Mood m) {
        m_mood = m;
    }
}
