package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EntryReader {

    /*  Storage format

        Stats
        Entries {
            ID
            Date
            Mood
            Comment
        }
     */

    public static ArrayList<Entry> read() {
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            File f = new File(Journal.COMMENTS_FILE_NAME);
            if (!f.exists()) {
                f.createNewFile();
                return entries;
            }
            BufferedReader br = new BufferedReader(new FileReader(Journal.COMMENTS_FILE_NAME));
            String line = br.readLine();

            while(line != null) {
                int id = Integer.parseInt(line);
                LocalDate date = getDate(br.readLine());
                Entry.Mood mood = getMood(br.readLine());
                entries.add(new Entry(id, date, mood, br.readLine()));
                line = br.readLine();
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
            BufferedReader br = new BufferedReader(new FileReader(Journal.COMMENTS_FILE_NAME));
            String line = br.readLine();
            String[] lastLines = new String[4];
            while(line != null) {
                lastLines[0] = line;
                lastLines[1] = br.readLine();
                lastLines[2] = br.readLine();
                lastLines[3] = br.readLine();
                line = br.readLine();
            }
            br.close();
            entry = new Entry(Integer.parseInt(lastLines[0]), getDate(lastLines[1]), getMood(lastLines[2]), lastLines[3]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entry;
    }

    private static LocalDate getDate(String line) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(line, f);
    }

    public static Entry.Mood getMood(String line) {
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
