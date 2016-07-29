package base;

import java.io.*;
import java.util.Date;

public class EntryWriter {

    public static void write(Entry entry) {
        String feeling = "This day was " + entry.getMood().toString().toLowerCase() + ".";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Comments.txt", true));
            writer.write("\n\n--------------------------------------------------\n" + new Date() + "\n" + "\n" + feeling + "\n" + "\n" + entry.getText());
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String[] split = null;
        try {
            BufferedReader brs = new BufferedReader(new FileReader("Stats.txt"));
            String tmp = brs.readLine();
            split = tmp.split(";");
            int i = Integer.parseInt(split[entry.getMood().ordinal()]);
            split[entry.getMood().ordinal()] = String.valueOf(++i);
            brs.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter wrs = new BufferedWriter(new FileWriter("Stats.txt"));
            wrs.write(String.valueOf(split[0]) + ";" + split[1] + ";" + split[2] + ";" + split[3] + ";" + split[4] + ";" + split[5] + ";" + split[6]);
            wrs.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
