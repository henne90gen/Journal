package Journal;

import com.google.common.flogger.FluentLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExporter {

	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

	private static final String DEFAULT_FILE_NAME = "journal.txt";

	private final IJournalData data;

	public FileExporter(IJournalData data) {
		this.data = data;
	}

	void writeToFile() {
		writeToFile(DEFAULT_FILE_NAME);
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
				for (Entry entry : data.getAllEntries()) {
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

}
