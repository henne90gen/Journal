package journal.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.google.common.flogger.FluentLogger;
import journal.JournalHelper;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileHandler {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final FileHandler INSTANCE = new FileHandler();

	private FileHandler() {
		MAPPER.findAndRegisterModules();
	}

	public List<Entry> readFromFile() {
		return readFromFile(new File(JournalHelper.DEFAULT_JSON_FILE_NAME));
	}

	public List<Entry> readFromFile(File file) {
		if (file == null) {
			LOGGER.atWarning().log("Could not read JSON from file.");
			return Collections.emptyList();
		}

		if (!file.exists()) {
			LOGGER.atWarning().log("JSON file does not exist.");
			return Collections.emptyList();
		}

		try {
			CollectionType clz = MAPPER.getTypeFactory().constructCollectionType(List.class, Entry.class);
			return MAPPER.readValue(file, clz);
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Could not read JSON file. %s", file.getAbsolutePath());
			return Collections.emptyList();
		}
	}

	public void writeToFile(List<Entry> entries) {
		writeToFile(entries, new File(JournalHelper.DEFAULT_JSON_FILE_NAME));
	}

	public void writeToFile(List<Entry> entries, File file) {
		if (file == null) {
			LOGGER.atWarning().log("Could not write JSON to file.");
			return;
		}

		try {
			MAPPER.writeValue(file, entries);
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Could not write JSON file. %s", file.getAbsolutePath());
		}
	}

	public void writeToFileOld(List<Entry> entries) {
		writeToFileOld(entries, new File(JournalHelper.DEFAULT_FILE_NAME));
	}

	public void writeToFileOld(List<Entry> entries, File file) {
		if (file == null) {
			LOGGER.atWarning().log("Could not write to file.");
			return;
		}

		String filePath = file.getAbsolutePath();
		LOGGER.atInfo().log("Saving data to %s", filePath);
		try {
			if (!file.exists()) {
				LOGGER.atInfo().log("File %s does not exist, trying to create it", filePath);
				boolean success = file.createNewFile();
				if (!success) {
					LOGGER.atInfo().log("Could not create %s", filePath);
					return;
				}
			}

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
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

	public List<Entry> readFromFileOld() {
		return readFromFileOld(new File(JournalHelper.DEFAULT_FILE_NAME));
	}

	public List<Entry> readFromFileOld(File file) {
		if (file == null) {
			LOGGER.atWarning().log("Could not load from file.");
			return Collections.emptyList();
		}

		String filePath = file.getAbsolutePath();
		LOGGER.atInfo().log("Loading data from %s", filePath);

		List<Entry> entries = new ArrayList<>();
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				LOGGER.atWarning().log("File %s does not exist", filePath);
				return Collections.emptyList();
			}

			try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
				String line = br.readLine();
				while (line != null) {
					LocalDate date = JournalHelper.INSTANCE.getDate(line);
					Entry.Mood mood = JournalHelper.INSTANCE.getMood(br.readLine());
					Entry entry = new Entry(date, mood, br.readLine());
					entries.add(entry);
					line = br.readLine();
				}
			}
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Error while loading data from %s", filePath);
		}
		LOGGER.atInfo().log("Loaded data from %s", filePath);
		return entries;
	}
}
