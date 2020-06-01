package journal.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.flogger.FluentLogger;
import journal.Journal;
import journal.JournalHelper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class FileHandler {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final FileHandler INSTANCE = new FileHandler();

	private FileHandler() {
		MAPPER.findAndRegisterModules();
	}

	public EntryStorage readFromFile() {
		File file = getStorageFile();
		return readFromFile(file);
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
			LOGGER.atInfo().log("Reading from file: %s", file);
			return MAPPER.readValue(file, EntryStorage.class);
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Could not read JSON file. %s", file.getAbsolutePath());
			return result;
		}
	}

	public void writeToFile(EntryStorage entryStorage) {
		File file = getStorageFile();
		writeToFile(entryStorage, file);
	}

	public void writeToFile(EntryStorage entryStorage, File file) {
		if (file == null) {
			LOGGER.atWarning().log("Could not write JSON to file.");
			return;
		}

		try {
			LOGGER.atInfo().log("Writing to file: %s", file);
			MAPPER.writeValue(file, entryStorage);
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Could not write JSON file. %s", file.getAbsolutePath());
		}
	}


	private File getStorageFile() {
		final Class<?> referenceClass = Journal.class;
		final URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();
		File directory = new File(".");
		try {
			final File jarPath = new File(url.toURI()).getParentFile();
			if ("lib".equals(jarPath.getName())) {
				// we are inside the 'lib' folder of the installation
				directory = jarPath.getParentFile();
			} else {
				directory = jarPath;
			}
		} catch (final URISyntaxException e) {
			LOGGER.atWarning().log("Could not get jar path.");
		}
		return new File(directory, JournalHelper.DEFAULT_FILE_NAME);
	}
}
