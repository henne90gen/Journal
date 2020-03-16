package journal.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import journal.JournalHelper;

import java.io.File;
import java.io.IOException;

public class FileHandler {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final FileHandler INSTANCE = new FileHandler();

	private FileHandler() {
		MAPPER.findAndRegisterModules();
	}

	public EntryStorage readFromFile() {
		return readFromFile(new File(JournalHelper.DEFAULT_JSON_FILE_NAME));
	}

	public EntryStorage readFromFile(File file) {
		EntryStorage result = new EntryStorage();
		if (file == null) {
			LOGGER.atWarning().log("Could not read JSON from file.");
			return result;
		}

		if (!file.exists()) {
			LOGGER.atWarning().log("JSON file does not exist.");
			return result;
		}

		try {
			return MAPPER.readValue(file, EntryStorage.class);
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Could not read JSON file. %s", file.getAbsolutePath());
			return result;
		}
	}

	public void writeToFile(EntryStorage entryStorage) {
		writeToFile(entryStorage, new File(JournalHelper.DEFAULT_JSON_FILE_NAME));
	}

	public void writeToFile(EntryStorage entryStorage, File file) {
		if (file == null) {
			LOGGER.atWarning().log("Could not write JSON to file.");
			return;
		}

		try {
			MAPPER.writeValue(file, entryStorage);
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Could not write JSON file. %s", file.getAbsolutePath());
		}
	}
}
