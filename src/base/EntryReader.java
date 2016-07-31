package base;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EntryReader {

    public static ArrayList<Entry> read() {
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("Comments.txt"));
            int lineCounter = 10;
            LocalDate date = LocalDate.now();
            Entry.Mood mood = Entry.Mood.Undecided;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase("--------------------------------------------------")) { lineCounter = 0; } else { lineCounter++; }
                if (lineCounter == 1) {
                    date = getDate(line);
                } else if (lineCounter == 3) {
                    mood = getMood(line);
                } else if (lineCounter == 5) {
                    Entry e = new Entry(date, line, mood);
                    entries.add(e);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public static Entry readLast() {
        Entry entry = new Entry();
        try {
            BufferedReader br = new BufferedReader(new FileReader("Comments.txt"));
            String line;
            String[] lastLines = new String[3];
            int lineCounter = 10;
            while ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase("--------------------------------------------------")) { lineCounter = 0; } else { lineCounter++; }
                if (lineCounter == 1) {
                    lastLines[0] = line;
                } else if (lineCounter == 3) {
                    lastLines[1] = line;
                } else if (lineCounter == 5) {
                    lastLines[2] = line;
                }
            }
            entry = new Entry(getDate(lastLines[0]), lastLines[2], getMood(lastLines[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entry;
    }

    private static LocalDate getDate(String line) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss zz yyyy");
        return LocalDate.parse(line, f);
    }

    private static Entry.Mood getMood(String line) {
        String[] tmp = line.split(" ");
        if (tmp[3].equalsIgnoreCase("awesome.")) {
            return Entry.Mood.Awesome;
        }
        return Entry.Mood.Undecided;
    }
}
