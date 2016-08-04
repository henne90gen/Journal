package base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EntryWriter {

    /*  Storage format

        Stats
        Entries {
            ID
            Date
            Mood
            Comment
        }
     */

    public static void write(Entry entry) {
        try {
            File f = new File(Journal.COMMENTS_FILE_NAME);
            if (!f.exists()) {
                f.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(Journal.COMMENTS_FILE_NAME, true));
            bw.write(entry.getID() + "\n");
            bw.write(entry.date.toString() + "\n");
            bw.write(entry.mood.toString() + "\n");
            bw.write(entry.comment + "\n");
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(ArrayList<Entry> entries, String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            for (int i = 0; i < entries.size(); i++) {
                bw.write(entries.get(i).getID() + "\n");
                bw.write(entries.get(i).date.toString() + "\n");
                bw.write(entries.get(i).mood.toString() + "\n");
                bw.write(entries.get(i).comment + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
