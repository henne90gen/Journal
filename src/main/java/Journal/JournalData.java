package Journal;

import org.apache.derby.client.ClientAutoloadedDriver;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

class JournalData {

	private static final String DEFAULT_FILE_NAME = "journal.txt";

	ArrayList<Entry> entries = new ArrayList<>();

	private int lastEntryID = 0;

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

	void readFromFile(String filePath) {
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				return;
			}
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			ArrayList<Entry> list = new ArrayList<>();
			while (line != null) {
				LocalDate date = getDate(line);
				Entry.Mood mood = getMood(br.readLine());
				list.add(new Entry(getNextID(), date, mood, br.readLine()));
				line = br.readLine();
			}
			entries.addAll(list);
			br.close();
		} catch (IOException e) {
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
	}

	public void load() {
		readFromFile(DEFAULT_FILE_NAME);
	}

	public void save() {
		writeToFile(DEFAULT_FILE_NAME);
	}
}
