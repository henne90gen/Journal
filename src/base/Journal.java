package base;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Journal implements WindowListener {

    public static final String DB = "jdbc:derby:journal;create=true";

    public ArrayList<Entry> entries = new ArrayList<Entry>();

    public Connection dbConnection;
    private Thread initThread;
    private ViewingWindow window;
    private int lastEntryID = 0;

    public Journal() throws SQLException {
        SwingUtilities.invokeLater(() -> window = new ViewingWindow(this));
    }

    public void init() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            dbConnection = DriverManager.getConnection(Journal.DB);
            ResultSet resultSet = dbConnection.getMetaData().getTables(null, null, "ENTRIES", null);
            if (!resultSet.next()) {
                dbConnection.createStatement().execute("create table entries(id int, date date, mood varchar(10), text clob)");
            }
            entries = readFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (entries.size() > 0) {
            lastEntryID = entries.get(entries.size()-1).getID();
        }
        window.updateUI();
    }

    public Entry[] searchForDate(int day, int month, int year) {
        ArrayList<Entry> list = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            if (day == -1 || entries.get(i).date.getDayOfMonth() == day) {
                if (month == -1 || entries.get(i).date.getMonthValue() == month) {
                    if (year == -1 || entries.get(i).date.getYear() == year) {
                        list.add(entries.get(i));
                    }
                }
            }
        }

        return getArrayFromList(list);
    }

    public Entry[] searchForString(String text) {
        ArrayList<Entry> list = new ArrayList<>();
        if (getMood(text) != null) {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).mood == getMood(text)) {
                    list.add(entries.get(i));
                }
            }
        } else {
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).comment.toLowerCase().contains(text.toLowerCase())) {
                    list.add(entries.get(i));
                }
            }
        }
        return getArrayFromList(list);
    }

    public Entry[] getArrayFromList(ArrayList<Entry> list) {
        Entry[] result = new Entry[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public ArrayList<Entry> readFromDB() {
        ArrayList<Entry> entries = new ArrayList<>();
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
        return entries;
    }

    public void readFromFile(String filePath) {
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
            writeToDB(list);
            entries.addAll(list);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToDB(Entry entry) {
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

    public void writeToDB(ArrayList<Entry> entries) {
        try {
            Statement s = dbConnection.createStatement();
            for (int i = 0; i < entries.size(); i++) {
                s.execute("insert into entries values (" +
                        entries.get(i).getID() + ", '" +
                        entries.get(i).date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "', '" +
                        entries.get(i).mood.toString() + "', '" +
                        escapeText(entries.get(i).comment) + "')");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
            for (int i = 0; i < entries.size(); i++) {
                bw.write(entries.get(i).date.toString() + "\n");
                bw.write(entries.get(i).mood.toString() + "\n");
                bw.write(entries.get(i).comment + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEntry(Entry entry) {
        try {
            Statement s = dbConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String escapeText(String text) {
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
    public int getNextID() {
        return ++lastEntryID;
    }
	public static void main(String[] args) throws SQLException {
		new Journal();
	}
    @Override
    public void windowOpened(WindowEvent e) {

    }
    @Override
    public void windowClosing(WindowEvent e) {
        window.updateUI();
        if (e.getSource() == window) {
            try {
                initThread.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
    @Override
    public void windowClosed(WindowEvent e) {

    }
    @Override
    public void windowIconified(WindowEvent e) {

    }
    @Override
    public void windowDeiconified(WindowEvent e) {

    }
    @Override
    public void windowActivated(WindowEvent e) {
        if (e.getSource() == window) {
            initThread = new Thread(() -> init());
            initThread.start();
        }
    }
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
