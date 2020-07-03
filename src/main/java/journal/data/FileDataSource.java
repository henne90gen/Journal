package journal.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.flogger.FluentLogger;
import journal.Journal;
import journal.JournalHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;

public class FileDataSource {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final ObjectMapper MAPPER = new ObjectMapper();
	public static final FileDataSource INSTANCE = new FileDataSource();

	private FileDataSource() {
		MAPPER.findAndRegisterModules();
	}

	public EntryStorage readFromFile() {
		File file = getStorageFile();
		return readFromFile(file);
	}

	public EntryStorage readFromFile(File file) {
		LOGGER.atInfo().log("Reading from file: %s", file);
		EntryStorage result = new EntryStorage();
		if (file == null) {
			LOGGER.atWarning().log("Could not read JSON from file.");
			return result;
		}

		if (!file.exists()) {
			LOGGER.atWarning().log("JSON file does not exist.");
			return result;
		}

		try (InputStream is = new FileInputStream(file)) {
			return readFromInputStream(is);
		} catch (IOException e) {
			// TODO logging
			e.printStackTrace();
			return result;
		}
	}

	public EntryStorage readFromInputStream(InputStream inputStream) {
		EntryStorage result = new EntryStorage();
		JsonNode json;
		try {
			json = MAPPER.readTree(inputStream);
		} catch (IllegalStateException | IOException e) {
			LOGGER.atSevere().withCause(e).log("Could not read json file.");
			return result;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (json == null) {
			return result;
		}
		if (!json.has("version")) {
			LOGGER.atSevere().log("JSON does not contain a version number.");
			return result;
		}

		int version;
		try {
			version = json.get("version").intValue();
		} catch (ClassCastException | IllegalStateException e) {
			LOGGER.atSevere().withCause(e).log();
			return result;
		}

		if (version <= 0 || version > EntryStorage.CURRENT_VERSION) {
			// something went wrong while parsing the version
			return result;
		}

		return withSchemaMigrations(json, version);
	}

	private EntryStorage withSchemaMigrations(JsonNode json, int version) {
		if (version != EntryStorage.CURRENT_VERSION) {
			performSchemaMigrations(version, json);
			((ObjectNode) json).set("version", new IntNode(EntryStorage.CURRENT_VERSION));
		}

		EntryStorage result = new EntryStorage();
		try {
			result = MAPPER.readValue(json.toString(), EntryStorage.class);
		} catch (IOException e) {
			LOGGER.atWarning().withCause(e).log("Could not read JSON.");
			return result;
		}

		return result;
	}

	private void performSchemaMigrations(int version, JsonNode json) {
		if (version < 2) {
			migrateToVersion2(json);
		}
		if (version < 3) {
			migrateToVersion3(json);
		}
		if (version < 4) {
			migrateToVersion4(json);
		}
	}

	/**
	 * Changed date from LocalDateTime to LocalDate
	 */
	private void migrateToVersion2(JsonNode json) {
		ArrayNode entries = (ArrayNode) json.get("entries");
		for (int i = 0; i < entries.size(); i++) {
			ObjectNode entry = (ObjectNode) entries.get(i);
			JsonNode dateNode = entry.get("date");
			if (dateNode instanceof ArrayNode) {
				ArrayNode date = (ArrayNode) dateNode;
				date.remove(6);
				date.remove(5);
				date.remove(4);
				date.remove(3);
			}
		}
	}

	/**
	 * Add 'deleted' flag to all entries
	 */
	private void migrateToVersion3(JsonNode json) {
		ArrayNode entries = (ArrayNode) json.get("entries");
		for (int i = 0; i < entries.size(); i++) {
			ObjectNode entry = (ObjectNode) entries.get(i);
			entry.set("deleted", BooleanNode.FALSE);
		}
	}

	/**
	 * Add 'lastModified' field to all entries
	 */
	private void migrateToVersion4(JsonNode json) {
		ArrayNode entries = (ArrayNode) json.get("entries");
		LocalDateTime lastModified = LocalDateTime.now();
		// "lastEdited":[2020,7,3,23,14,12,493265000]
		ArrayNode lastModifiedArray = new ArrayNode(JsonNodeFactory.instance);
		lastModifiedArray.add(lastModified.getYear());
		lastModifiedArray.add(lastModified.getMonthValue());
		lastModifiedArray.add(lastModified.getDayOfMonth());
		lastModifiedArray.add(lastModified.getHour());
		lastModifiedArray.add(lastModified.getMinute());
		lastModifiedArray.add(lastModified.getSecond());
		lastModifiedArray.add(lastModified.getNano());
		for (int i = 0; i < entries.size(); i++) {
			ObjectNode entry = (ObjectNode) entries.get(i);
			entry.set("lastModified", lastModifiedArray);
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

	public File getStorageFile() {
		final Class<?> referenceClass = Journal.class;
		final URL url = referenceClass.getProtectionDomain().getCodeSource().getLocation();
		String fileName = JournalHelper.DEFAULT_FILE_NAME;
		File directory = new File(".");
		try {
			final File jarPath = new File(url.toURI()).getParentFile();
			if ("lib".equals(jarPath.getName())) {
				// we are inside the 'lib' folder of the installation
				directory = jarPath.getParentFile();
			} else {
				directory = jarPath;
				fileName = JournalHelper.DEFAULT_DEV_FILE_NAME;
			}
		} catch (final URISyntaxException e) {
			LOGGER.atWarning().log("Could not get jar path.");
		}
		return new File(directory, fileName);
	}
}
