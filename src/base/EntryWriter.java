package base;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class EntryWriter {

    public static void write(Entry entry) {
        String feeling = "This day was " + entry.getMood().toString().toLowerCase() + ".";

        try {
            File f = new File(Journal.COMMENTS_FILE_NAME);
            if (!f.exists()) {
                f.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(Journal.COMMENTS_FILE_NAME, true));
            writer.write("\n\n--------------------------------------------------\n" + new Date() + "\n" + "\n" + feeling + "\n" + "\n" + entry.getText());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String[] split = null;
        try {
            File f = new File(Journal.STATS_FILE_NAME);
            if (!f.exists()) {
                f.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(Journal.STATS_FILE_NAME));
                bw.write("0;0;0;0;0;0;0");
                bw.close();
            }
            BufferedReader br = new BufferedReader(new FileReader(Journal.STATS_FILE_NAME));
            String tmp = br.readLine();
            split = tmp.split(";");
            int i = Integer.parseInt(split[entry.getMood().ordinal()]);
            split[entry.getMood().ordinal()] = String.valueOf(++i);
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter wrs = new BufferedWriter(new FileWriter(Journal.STATS_FILE_NAME));
            wrs.write(String.valueOf(split[0]) + ";" + split[1] + ";" + split[2] + ";" + split[3] + ";" + split[4] + ";" + split[5] + ";" + split[6]);
            wrs.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(ArrayList<Entry> entries, String filePath) {

    }
}
