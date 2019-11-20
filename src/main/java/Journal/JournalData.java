package Journal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JournalData implements IJournalData {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final String DEFAULT_FILE_NAME = "journal.txt";

	private static ObjectMapper MAPPER = new ObjectMapper();

	private final List<Entry> entries = new ArrayList<>();

	private int lastEntryID = 0;

	@Override
	public List<Entry> findByDate(int day, int month, int year) {
		List<Entry> list = new ArrayList<>();
		for (Entry entry : entries) {
			if (day == -1 || entry.date.getDayOfMonth() == day) {
				if (month == -1 || entry.date.getMonthValue() == month) {
					if (year == -1 || entry.date.getYear() == year) {
						list.add(entry);
					}
				}
			}
		}
		return list;
	}

	@Override
	public List<Entry> getAllEntries() {
		return entries;
	}

	@Override
	public List<Entry> findByString(String text) {
		if (getMood(text) != null) {
			return entries.stream()
					.filter(entry -> entry.mood == getMood(text))
					.collect(Collectors.toList());
		}

		return entries.stream()
				.filter(entry -> entry.comment.toLowerCase().contains(text.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public void save(Entry entry) {
		entries.add(entry);
	}

	void readFromFile(String filePath) {
		LOGGER.atInfo().log("Loading data from %s", filePath);
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				LOGGER.atWarning().log("File %s does not exist", filePath);
				return;
			}

			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line = br.readLine();
				ArrayList<Entry> list = new ArrayList<>();
				while (line != null) {
					LocalDate date = getDate(line);
					Entry.Mood mood = getMood(br.readLine());
					list.add(new Entry(getNextID(), date, mood, br.readLine()));
					line = br.readLine();
				}
				entries.addAll(list);
			}
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Error while loading data from %s", filePath);
		}
		LOGGER.atInfo().log("Loaded data from %s", filePath);
	}

	void writeToFile(String filePath) {
		LOGGER.atInfo().log("Saving data to %s", filePath);
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				LOGGER.atInfo().log("File %s does not exist, trying to create it", filePath);
				boolean success = f.createNewFile();
				if (!success) {
					LOGGER.atInfo().log("Could not create %s", filePath);
					return;
				}
			}

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
				for (Entry entry : entries) {
					bw.write(entry.date.toString() + "\n");
					bw.write(entry.mood.toString() + "\n");
					bw.write(entry.comment + "\n");
				}
			}
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Error while writing to %s", filePath);
		}
		LOGGER.atInfo().log("Saved data to %s", filePath);
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

	@Override
	public void delete(Entry entry) {
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).id == entry.id) {
				entries.remove(i);
				break;
			}
		}
		if (entries.size() > 0) {
			lastEntryID = entries.get(entries.size() - 1).id;
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
