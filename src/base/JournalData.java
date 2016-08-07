package base;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by henne on 07.08.16.
 */
class JournalData {

    private Journal journal;
    private static final String DB = "jdbc:derby:journal;create=true";

    ArrayList<Entry> entries = new ArrayList<Entry>();

    private Connection dbConnection;
    private int lastEntryID = 0;

    JournalData(Journal journal) {
        this.journal = journal;
    }

    void connectToDB() {
        System.out.println("Connecting to database...");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            dbConnection = DriverManager.getConnection(DB);
            ResultSet resultSet = dbConnection.getMetaData().getTables(null, null, "ENTRIES", null);
            if (!resultSet.next()) {
                dbConnection.createStatement().execute("create table entries(id int, date date, mood varchar(10), text clob)");
            }
            readFromDB();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (entries.size() > 0) {
            lastEntryID = entries.get(entries.size()-1).getID();
        }
        System.out.println("Connected.");
    }

    void edit(Entry entry) {
        try {
            Statement s = dbConnection.createStatement();
            s.execute("update entries set text='" + entry.comment + "', mood='" + entry.mood.toString() + "' where id=" + entry.getID());
            System.out.println("Entry " + entry.getID() + " saved.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        journal.view.update();
    }

    void delete(Entry entry) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getID() == entry.getID()) {
                entries.remove(i);
                break;
            }
        }
        if (entries.size() > 0) {
            lastEntryID = entries.get(entries.size() - 1).getID();
        } else {
            lastEntryID = 0;
        }
        try {
            Statement s = dbConnection.createStatement();
            s.execute("delete from entries where id=" + entry.getID());
            System.out.println("Entry " + entry.getID() + " deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        journal.view.update();
    }

    Entry[] searchForDate(int day, int month, int year) {
        ArrayList<Entry> list = new ArrayList<>();
        for (Entry entry : entries) {
            if (day == -1 || entry.date.getDayOfMonth() == day) {
                if (month == -1 || entry.date.getMonthValue() == month) {
                    if (year == -1 || entry.date.getYear() == year) {
                        list.add(entry);
                    }
                }
            }
        }
        return getArrayFromList(list);
    }

    Entry[] searchForString(String text) {
        ArrayList<Entry> list = new ArrayList<>();
        if (getMood(text) != null) {
            list.addAll(entries.stream().filter(entry -> entry.mood == getMood(text)).collect(Collectors.toList()));
        } else {
            list.addAll(entries.stream().filter(entry -> entry.comment.toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList()));
        }
        return getArrayFromList(list);
    }

    private Entry[] getArrayFromList(ArrayList<Entry> list) {
        Entry[] result = new Entry[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    private void readFromDB() {
        try {
            Statement s = dbConnection.createStatement();
            ResultSet resultSet = s.executeQuery("select * from entries");
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                Date date = resultSet.getDate(2);
                Entry.Mood mood = getMood(resultSet.getString(3));
                String text = resultSet.getString(4);
                entries.add(new Entry(id, date.toLocalDate(), mood, text));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void readFromFile(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return;
            }
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            ArrayList<Entry> list = new ArrayList<>();
            while(line != null) {
                LocalDate date = getDate(line);
                Entry.Mood mood = getMood(br.readLine());
                list.add(new Entry(getNextID(), date, mood, br.readLine()));
                line = br.readLine();
            }
            entries.addAll(list);
            writeToDB();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeToDB(Entry entry) {
        try {
            Statement s = dbConnection.createStatement();
            s.execute("insert into entries values (" +
                    entry.getID() + ", '" +
                    entry.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "', '" +
                    entry.mood.toString() + "', '" +
                    escapeText(entry.comment) + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void writeToDB() {
        try {
            Statement s = dbConnection.createStatement();
            for (Entry entry : entries) {
                s.execute("insert into entries values (" +
                        entry.getID() + ", '" +
                        entry.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "', '" +
                        entry.mood.toString() + "', '" +
                        escapeText(entry.comment) + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void writeToFile(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            for (Entry entry : entries) {
                bw.write(entry.date.toString() + "\n");
                bw.write(entry.mood.toString() + "\n");
                bw.write(entry.comment + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escapeText(String text) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == "'".charAt(0)) {
                result = result + "''";
            } else {
                result = result + text.charAt(i);
            }
        }
        return result;
    }
    private static LocalDate getDate(String line) {
        return LocalDate.parse(line, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private static Entry.Mood getMood(String line) {
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
    int getNextID() {
        return ++lastEntryID;
    }
}
